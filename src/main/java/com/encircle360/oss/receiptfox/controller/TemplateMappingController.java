package com.encircle360.oss.receiptfox.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.encircle360.oss.receiptfox.dto.TemplateMappingDTO;
import com.encircle360.oss.receiptfox.dto.api.CreateUpdateTemplateMappingDTO;
import com.encircle360.oss.receiptfox.dto.pagination.PageContainer;
import com.encircle360.oss.receiptfox.dto.receipt.ReceiptTypeDTO;
import com.encircle360.oss.receiptfox.mapping.TemplateMappingMapper;
import com.encircle360.oss.receiptfox.service.PageContainerFactory;
import com.encircle360.oss.receiptfox.service.TemplateMappingService;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/template-mappings")
public class TemplateMappingController {

    private final TemplateMappingService templateMappingService;

    private final TemplateMappingMapper templateMappingMapper = TemplateMappingMapper.INSTANCE;

    private final PageContainerFactory pageContainerFactory;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageContainer<TemplateMappingDTO>> filter(@RequestParam(required = false) final Integer size,
                                                                    @RequestParam(required = false) final Integer page,
                                                                    @RequestParam(required = false) final String sort,
                                                                    @RequestParam(required = false) final Long organizationUnitId,
                                                                    @RequestParam(required = false, name = "receiptType") final ReceiptTypeDTO receiptTypeDTO) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TemplateMappingDTO> get(@PathVariable final Long id) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TemplateMappingDTO> create(@RequestBody @Valid final CreateUpdateTemplateMappingDTO createUpdateTemplateMappingDTO) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TemplateMappingDTO> update(@RequestBody @Valid final CreateUpdateTemplateMappingDTO createUpdateTemplateMappingDTO,
                                                     @PathVariable final Long id) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
