package com.encircle360.oss.receiptfox.listener;

import static com.encircle360.oss.receiptfox.service.SimpleStorageService.PDF_EXTENSION;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.encircle360.oss.receiptfox.client.docsrabbit.OcrClient;
import com.encircle360.oss.receiptfox.client.docsrabbit.RenderClient;
import com.encircle360.oss.receiptfox.client.docsrabbit.TemplateClient;
import com.encircle360.oss.receiptfox.client.docsrabbit.dto.OCRResultDTO;
import com.encircle360.oss.receiptfox.client.docsrabbit.dto.render.RenderFormatDTO;
import com.encircle360.oss.receiptfox.client.docsrabbit.dto.render.RenderRequestDTO;
import com.encircle360.oss.receiptfox.client.docsrabbit.dto.render.RenderResultDTO;
import com.encircle360.oss.receiptfox.event.ReceiptProcessedEvent;
import com.encircle360.oss.receiptfox.model.TemplateMapping;
import com.encircle360.oss.receiptfox.model.contact.Contact;
import com.encircle360.oss.receiptfox.model.receipt.Receipt;
import com.encircle360.oss.receiptfox.model.receipt.ReceiptFile;
import com.encircle360.oss.receiptfox.service.ContactService;
import com.encircle360.oss.receiptfox.service.SimpleStorageService;
import com.encircle360.oss.receiptfox.service.TemplateMappingService;
import com.encircle360.oss.receiptfox.service.receipt.ReceiptFileService;
import com.encircle360.oss.receiptfox.service.receipt.ReceiptService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReceiptProcessedListeners {

    // services
    private final TemplateMappingService templateMappingService;
    private final SimpleStorageService simpleStorageService;
    private final ReceiptFileService receiptFileService;
    private final ContactService contactService;
    private final ReceiptService receiptService;

    // clients
    private final TemplateClient templateClient;
    private final RenderClient renderClient;
    private final OcrClient ocrClient;

    @EventListener(ReceiptProcessedEvent.class)
    public void generateReceiptFile(ReceiptProcessedEvent event) throws IOException {
        Receipt receipt = receiptService.get(event.getReceiptId());
        if (receipt == null) {
            return;
        }

        String templateId = getTemplateId(receipt);

        Map<String, Object> model = new HashMap<>();
        Contact contact = receipt.getContact();

        model.put("receipt", receipt);
        model.put("contact", contact);

        RenderRequestDTO renderRequestDTO = RenderRequestDTO
            .builder()
            .templateId(templateId)
            .format(RenderFormatDTO.PDF)
            .model(model)
            .build();

        ResponseEntity<RenderResultDTO> resultResponseEntity = renderClient.render(renderRequestDTO);
        RenderResultDTO result = resultResponseEntity.getBody();

        if (!resultResponseEntity.getStatusCode().is2xxSuccessful() || result == null || result.getBase64() == null) {
            throw new IllegalStateException("Rendering was not successful.");
        }

        byte[] data = simpleStorageService.decode(result.getBase64());
        ReceiptFile receiptFile = receipt.getReceiptFile();

        // if there is already a file override it
        if (receiptFile == null) {
            receiptFile = createReceiptFile(receipt.getReceiptNumber(), data, result.getMimeType());
            receipt.setReceiptFile(receiptFile);
            receiptService.save(receipt);
            return;
        }

        boolean saved = simpleStorageService.save(receiptFile.getS3Bucket(), receiptFile.getS3Path(), data, result.getMimeType());

        if (!saved) {
            throw new IOException("File was not saved");
        }
        receiptFileService.save(receiptFile);

    }

    @Async
    @EventListener(ReceiptProcessedEvent.class)
    public void ocrScan(ReceiptProcessedEvent ocrEvent) throws IOException {
        Receipt receipt = receiptService.get(ocrEvent.getReceiptId());
        if (receipt == null) {
            log.error("Receipt not found while ocr parsing");
            return;
        }

        ReceiptFile receiptFile = receipt.getReceiptFile();
        if (receiptFile == null) {
            log.error("ReceiptFile not found while ocr parsing");
            return;
        }

        byte[] data = simpleStorageService.get(receiptFile.getS3Bucket(), receiptFile.getS3Path());
        MultipartFile file = new ByteArrayMultipartFile(receiptFile.getS3Path(), MediaType.APPLICATION_PDF_VALUE, data);

        ResponseEntity<OCRResultDTO> ocrResultDTOResponseEntity = ocrClient.ocrParse(file);
        if (!ocrResultDTOResponseEntity.getStatusCode().is2xxSuccessful()) {
            log.error("OCR parsing failed, status code: " + ocrResultDTOResponseEntity.getStatusCode());
            return;
        }

        OCRResultDTO resultDTO = ocrResultDTOResponseEntity.getBody();
        if (resultDTO == null) {
            log.error("OCR parsing failed, no body");
            return;
        }

        receiptFile.setOcr(resultDTO.getContent());
        receiptFileService.save(receiptFile);
    }

    private String getTemplateId(Receipt receipt) {
        String templateId = receipt.getTemplateId();
        if (templateId == null) {
            TemplateMapping templateMapping = templateMappingService
                .getDefaultForOrganizationUnitAndType(receipt.getOrganizationUnit(), receipt.getReceiptType());

            if (templateMapping != null) {
                templateId = templateMapping.getTemplateId();
            }
        }

        if (templateId == null) {
            return receipt.getOrganizationUnit().getDefaultTemplateId();
        }
        return templateId;
    }

    private ReceiptFile createReceiptFile(String receiptNumber, byte[] data, String mimeType) throws IOException {

        String fileName = receiptNumber + "." + PDF_EXTENSION;

        String path = simpleStorageService.pathForNewFile(fileName);
        boolean saved = simpleStorageService.save(path, data, mimeType);

        if (!saved) {
            throw new IOException("File was not saved");
        }

        ReceiptFile receiptFile = ReceiptFile
            .builder()
            .s3Path(path)
            .s3Bucket(simpleStorageService.getDefaultBucket())
            .name(fileName)
            .meta(Map.of())
            .build();

        return receiptFileService.save(receiptFile);
    }

    private static class ByteArrayMultipartFile implements MultipartFile {
        private final String name;
        private final String contentType;

        private final byte[] data;

        public ByteArrayMultipartFile(String name, String contentType, byte[] data) {
            this.data = data;
            this.name = name;
            this.contentType = contentType;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getOriginalFilename() {
            return name;
        }

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public boolean isEmpty() {
            return data.length == 0;
        }

        @Override
        public long getSize() {
            return data.length;
        }

        @Override
        public byte[] getBytes() throws IOException {
            return data;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(data);
        }

        @Override
        public void transferTo(File dest) throws IOException, IllegalStateException {
            Files.write(Path.of(dest.getAbsolutePath()), data);
        }
    }
}
