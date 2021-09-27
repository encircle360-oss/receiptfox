package com.encircle360.oss.receiptfox.client.docsrabbit;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.encircle360.oss.receiptfox.client.docsrabbit.dto.OCRResultDTO;

@FeignClient(value = "ocrClient", url = "http://docsrabbit-service:50005")
public interface OcrClient {

    @PostMapping("")
    ResponseEntity<OCRResultDTO> ocrParse(@RequestParam MultipartFile file);
}
