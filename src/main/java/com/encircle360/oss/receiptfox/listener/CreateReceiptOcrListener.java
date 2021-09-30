package com.encircle360.oss.receiptfox.listener;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.encircle360.oss.receiptfox.client.docsrabbit.OcrClient;
import com.encircle360.oss.receiptfox.client.docsrabbit.dto.OCRResultDTO;
import com.encircle360.oss.receiptfox.event.CreateReceiptOcrEvent;
import com.encircle360.oss.receiptfox.model.receipt.Receipt;
import com.encircle360.oss.receiptfox.model.receipt.ReceiptFile;
import com.encircle360.oss.receiptfox.service.SimpleStorageService;
import com.encircle360.oss.receiptfox.service.receipt.ReceiptFileService;
import com.encircle360.oss.receiptfox.service.receipt.ReceiptService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateReceiptOcrListener {

    private final SimpleStorageService simpleStorageService;
    private final ReceiptFileService receiptFileService;
    private final ReceiptService receiptService;

    private final OcrClient ocrClient;

    @Async
    @EventListener(CreateReceiptOcrEvent.class)
    public void listener(CreateReceiptOcrEvent ocrEvent) throws IOException {
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

    private class ByteArrayMultipartFile implements MultipartFile {
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
