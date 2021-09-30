package com.encircle360.oss.receiptfox.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import com.encircle360.oss.receiptfox.mapping.ReceiptTypeMapper;
import com.encircle360.oss.receiptfox.mapping.TemplateMappingMapper;
import com.encircle360.oss.receiptfox.model.OrganizationUnit;
import com.encircle360.oss.receiptfox.model.TemplateMapping;
import com.encircle360.oss.receiptfox.model.receipt.ReceiptType;
import com.encircle360.oss.receiptfox.service.OrganizationUnitService;
import com.encircle360.oss.receiptfox.service.PageContainerFactory;
import com.encircle360.oss.receiptfox.service.TemplateMappingService;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/template-mappings")
public class TemplateMappingController {

    private final OrganizationUnitService organizationUnitService;
    private final TemplateMappingService templateMappingService;

    private final TemplateMappingMapper templateMappingMapper = TemplateMappingMapper.INSTANCE;
    private final ReceiptTypeMapper receiptTypeMapper = ReceiptTypeMapper.INSTANCE;

    private final PageContainerFactory pageContainerFactory;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageContainer<TemplateMappingDTO>> filter(@RequestParam(required = false) final Integer size,
                                                                    @RequestParam(required = false) final Integer page,
                                                                    @RequestParam(required = false) final String sort,
                                                                    @RequestParam(required = false) final Long organizationUnitId,
                                                                    @RequestParam(required = false, name = "receiptType") final ReceiptTypeDTO receiptTypeDTO) {
        Pageable pageable = pageContainerFactory.mapRequestToPageable(size, page, sort);
        ReceiptType type = receiptTypeMapper.fromDto(receiptTypeDTO);
        Page<TemplateMapping> templateMappings = templateMappingService.filter(organizationUnitId, type, pageable);
        List<TemplateMappingDTO> dtos = templateMappingMapper.toDtos(templateMappings.getContent());
        PageContainer<TemplateMappingDTO> pageContainer = pageContainerFactory.getPageContainer(pageable, templateMappings, dtos);

        return ResponseEntity.status(HttpStatus.OK).body(pageContainer);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TemplateMappingDTO> get(@PathVariable final Long id) {
        TemplateMapping templateMapping = templateMappingService.get(id);
        if (templateMapping == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        TemplateMappingDTO dto = templateMappingMapper.toDto(templateMapping);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TemplateMappingDTO> create(@RequestBody @Valid final CreateUpdateTemplateMappingDTO createUpdateTemplateMappingDTO) {
        OrganizationUnit organizationUnit = organizationUnitService.get(createUpdateTemplateMappingDTO.getOrganizationUnitId());
        if (organizationUnit == null) {
            return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).build();
        }

        TemplateMapping templateMapping = templateMappingMapper.createFromDto(createUpdateTemplateMappingDTO, organizationUnit);
        templateMapping = templateMappingService.save(templateMapping);
        TemplateMappingDTO dto = templateMappingMapper.toDto(templateMapping);

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TemplateMappingDTO> update(@RequestBody @Valid final CreateUpdateTemplateMappingDTO createUpdateTemplateMappingDTO,
                                                     @PathVariable final Long id) {
        TemplateMapping templateMapping = templateMappingService.get(id);
        if (templateMapping == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        OrganizationUnit organizationUnit = organizationUnitService.get(createUpdateTemplateMappingDTO.getOrganizationUnitId());
        if (organizationUnit == null) {
            return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).build();
        }

        templateMappingMapper.updateFromDto(createUpdateTemplateMappingDTO, organizationUnit, templateMapping);
        templateMapping = templateMappingService.save(templateMapping);
        TemplateMappingDTO dto = templateMappingMapper.toDto(templateMapping);

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        TemplateMapping templateMapping = templateMappingService.get(id);
        if (templateMapping == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        templateMappingService.delete(templateMapping);

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
