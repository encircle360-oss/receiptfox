package com.encircle360.oss.receiptfox.controller.receipt;

import com.encircle360.oss.receiptfox.client.docsrabbit.dto.render.RenderResultDTO;
import com.encircle360.oss.receiptfox.dto.pagination.PageContainer;
import com.encircle360.oss.receiptfox.dto.receipt.ReceiptFileDTO;
import com.encircle360.oss.receiptfox.mapping.receipt.ReceiptFileMapper;
import com.encircle360.oss.receiptfox.model.receipt.ReceiptFile;
import com.encircle360.oss.receiptfox.service.PageContainerFactory;
import com.encircle360.oss.receiptfox.service.SimpleStorageService;
import com.encircle360.oss.receiptfox.service.receipt.ReceiptFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/receipt-files")
public class ReceiptFileController {

    private final ReceiptFileService receiptFileService;
    private final SimpleStorageService simpleStorageService;
    private final ReceiptFileMapper receiptFileMapper = ReceiptFileMapper.INSTANCE;

    private final PageContainerFactory pageContainerFactory;
    private final static Base64.Encoder encoder = Base64.getEncoder();

    @Operation(
        operationId = "listReceiptFiles",
        description = "Lists all receipt files.",
        parameters = {
            @Parameter(name = "size", description = "The size of the page."),
            @Parameter(name = "page", description = "The number of the page."),
            @Parameter(name = "sort", description = "The sorting of the page."),
        }
    )
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

    @Operation(
        operationId = "getReceiptFile",
        description = "Returns a receipt file by its id.",
        responses = {
            @ApiResponse(responseCode = "200", description = "ReceiptFile was found."),
            @ApiResponse(responseCode = "404", description = "ReceiptFile was not found.")
        }
    )
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReceiptFileDTO> get(@PathVariable final Long id) {
        ReceiptFile receiptFile = receiptFileService.get(id);
        if (receiptFile == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        ReceiptFileDTO dto = receiptFileMapper.toDto(receiptFile);

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @Operation(
        operationId = "downloadReceiptFile",
        description = "Returns a receipt file contents as download by its id.",
        responses = {
            @ApiResponse(responseCode = "200", description = "ReceiptFile was found."),
            @ApiResponse(responseCode = "404", description = "ReceiptFile was not found.")
        }
    )
    @GetMapping(value = {"/{id}/download", "/{id}/download/receipt-{id}.pdf"})
    public ResponseEntity<Resource> download(@PathVariable final Long id) throws IOException {
        ReceiptFile receiptFile = receiptFileService.get(id);
        if (receiptFile == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        byte[] data = simpleStorageService.get(receiptFile.getS3Bucket(), receiptFile.getS3Path());
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity.status(HttpStatus.OK)
            .contentLength(data.length)
            .contentType(MediaType.APPLICATION_PDF)
            .body(resource);
    }

    @Operation(
        operationId = "getReceiptFileBas64",
        description = "Returns a receipt file contents as base64 wrapped in an object, by its id.",
        responses = {
            @ApiResponse(responseCode = "200", description = "ReceiptFile was found."),
            @ApiResponse(responseCode = "404", description = "ReceiptFile was not found.")
        }
    )
    @GetMapping(value = "/{id}/base64")
    public ResponseEntity<RenderResultDTO> base64(@PathVariable final Long id) throws IOException {
        ReceiptFile receiptFile = receiptFileService.get(id);
        if (receiptFile == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        byte[] data = simpleStorageService.get(receiptFile.getS3Bucket(), receiptFile.getS3Path());
        String base64Date = encoder.encodeToString(data);

        RenderResultDTO result = RenderResultDTO.builder()
            .base64(base64Date)
            .contentLength(data.length)
            .mimeType(MediaType.APPLICATION_PDF_VALUE)
            .build();

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
