package com.encircle360.oss.receiptfox.listener;

import static com.encircle360.oss.receiptfox.service.SimpleStorageService.PDF_EXTENSION;

import java.io.IOException;
import java.util.Map;

import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.encircle360.oss.receiptfox.client.docsrabbit.RenderClient;
import com.encircle360.oss.receiptfox.client.docsrabbit.TemplateClient;
import com.encircle360.oss.receiptfox.client.docsrabbit.dto.render.RenderFormatDTO;
import com.encircle360.oss.receiptfox.client.docsrabbit.dto.render.RenderRequestDTO;
import com.encircle360.oss.receiptfox.client.docsrabbit.dto.render.RenderResultDTO;
import com.encircle360.oss.receiptfox.client.docsrabbit.dto.template.TemplateDTO;
import com.encircle360.oss.receiptfox.event.CreateReceiptDocumentEvent;
import com.encircle360.oss.receiptfox.model.receipt.Receipt;
import com.encircle360.oss.receiptfox.model.receipt.ReceiptFile;
import com.encircle360.oss.receiptfox.service.SimpleStorageService;
import com.encircle360.oss.receiptfox.service.receipt.ReceiptFileService;
import com.encircle360.oss.receiptfox.service.receipt.ReceiptService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateReceiptDocumentListener {

    // services
    private final SimpleStorageService simpleStorageService;
    private final ReceiptFileService receiptFileService;
    private final ReceiptService receiptService;

    // clients
    private final TemplateClient templateClient;
    private final RenderClient renderClient;

    @EventListener(CreateReceiptDocumentEvent.class)
    public void listener(CreateReceiptDocumentEvent event) throws IOException {
        Receipt receipt = receiptService.get(event.getReceiptId());
        if (receipt == null) {
            return;
        }

        String templateId = receipt.getTemplateId();
        if (templateId == null) {
            templateId = receipt.getOrganizationUnit().getDefaultTemplateId();
        }

        ResponseEntity<TemplateDTO> templateResponseEntity = templateClient.get(templateId);
        if (templateResponseEntity.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
            throw new IllegalArgumentException("Template not found");
        }

        RenderRequestDTO renderRequestDTO = RenderRequestDTO
            .builder()
            .templateId(templateId)
            .format(RenderFormatDTO.PDF)
            .model(receipt)
            .build();

        ResponseEntity<RenderResultDTO> resultResponseEntity = renderClient.render(renderRequestDTO);
        RenderResultDTO result = resultResponseEntity.getBody();

        if (!resultResponseEntity.getStatusCode().is2xxSuccessful() || result == null || result.getBase64() == null) {
            throw new IllegalStateException("Rendering was not successful.");
        }

        byte[] data = simpleStorageService.decode(result.getBase64());
        ReceiptFile receiptFile = receipt.getReceiptFile();

        // if there is already a file override it
        if (receiptFile != null) {
            boolean saved = simpleStorageService.save(receiptFile.getS3Bucket(), receiptFile.getS3Path(), data, result.getMimeType());

            if (!saved) {
                throw new IOException("File was not saved");
            }
            receiptFileService.save(receiptFile);
            return;
        }

        String receiptNumber = receipt.getReceiptNumber();
        String fileName = receiptNumber + "." + PDF_EXTENSION;

        String path = simpleStorageService.pathForNewFile(fileName);
        boolean saved = simpleStorageService.save(path, data, result.getMimeType());

        if (!saved) {
            throw new IOException("File was not saved");
        }

        receiptFile = ReceiptFile
            .builder()
            .s3Path(path)
            .s3Bucket(simpleStorageService.getDefaultBucket())
            .name(fileName)
            .meta(Map.of())
            .build();

        receiptFile = receiptFileService.save(receiptFile);

        receipt.setReceiptFile(receiptFile);
        receiptService.save(receipt);
    }

}
