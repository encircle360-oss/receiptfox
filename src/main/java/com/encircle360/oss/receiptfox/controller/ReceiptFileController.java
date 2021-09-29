package com.encircle360.oss.receiptfox.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.encircle360.oss.receiptfox.dto.pagination.PageContainer;
import com.encircle360.oss.receiptfox.dto.receipt.ReceiptFileDTO;
import com.encircle360.oss.receiptfox.mapping.receipt.ReceiptFileMapper;
import com.encircle360.oss.receiptfox.model.receipt.ReceiptFile;
import com.encircle360.oss.receiptfox.service.PageContainerFactory;
import com.encircle360.oss.receiptfox.service.receipt.ReceiptFileService;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/receipt-files")
public class ReceiptFileController {

    private final ReceiptFileService receiptFileService;

    private final ReceiptFileMapper receiptFileMapper = ReceiptFileMapper.INSTANCE;

    private final PageContainerFactory pageContainerFactory;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageContainer<ReceiptFileDTO>> list(@RequestParam(required = false) final Integer size,
                                                              @RequestParam(required = false) final Integer page,
                                                              @RequestParam(required = false) final String sort) {
        Pageable pageable = pageContainerFactory.mapRequestToPageable(size, page, sort);
        Page<ReceiptFile> receiptFiles = receiptFileService.findAll(pageable);
        List<ReceiptFileDTO> receiptFileDTOList = receiptFileMapper.toDtos(receiptFiles.getContent());

        PageContainer<ReceiptFileDTO> pageContainer = pageContainerFactory.getPageContainer(pageable, receiptFiles, receiptFileDTOList);
        return ResponseEntity.status(HttpStatus.OK).body(pageContainer);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReceiptFileDTO> get(@PathVariable final Long id) {
        ReceiptFile receiptFile = receiptFileService.get(id);
        if (receiptFile == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        ReceiptFileDTO dto = receiptFileMapper.toDto(receiptFile);

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @GetMapping(value = "/{id}/download")
    public ResponseEntity<byte[]> download(@PathVariable final Long id) {
        ReceiptFile receiptFile = receiptFileService.get(id);
        if (receiptFile == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // todo implement
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
