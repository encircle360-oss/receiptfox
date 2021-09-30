package com.encircle360.oss.receiptfox.client.docsrabbit;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.encircle360.oss.receiptfox.client.docsrabbit.dto.OCRResultDTO;

@FeignClient(value = "ocrClient", url = "http://docsrabbit-service:50005/ocr", configuration = MultipartFeignConfiguration.class)
public interface OcrClient {

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<OCRResultDTO> ocrParse(@RequestPart("file") MultipartFile file);
}
