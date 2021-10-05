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

import com.encircle360.oss.receiptfox.dto.organizationunit.OrganizationUnitDTO;
import com.encircle360.oss.receiptfox.dto.organizationunit.api.CreateUpdateOrganizationUnitDTO;
import com.encircle360.oss.receiptfox.dto.pagination.PageContainer;
import com.encircle360.oss.receiptfox.mapping.OrganizationUnitMapper;
import com.encircle360.oss.receiptfox.model.OrganizationUnit;
import com.encircle360.oss.receiptfox.service.OrganizationUnitService;
import com.encircle360.oss.receiptfox.service.PageContainerFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/organization-units")
public class OrganizationUnitController {

    private final OrganizationUnitService organizationUnitService;
    private final PageContainerFactory pageContainerFactory;

    private final OrganizationUnitMapper mapper = OrganizationUnitMapper.INSTANCE;

    @Operation(
        operationId = "listOrganizationUnits",
        description = "Lists all organization units.",
        parameters = {
            @Parameter(name = "size", description = "The size of the page."),
            @Parameter(name = "page", description = "The number of the page."),
            @Parameter(name = "sort", description = "The sort of the page.")
        }
    )
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageContainer<OrganizationUnitDTO>> list(@RequestParam(required = false) Integer size,
                                                                   @RequestParam(required = false) Integer page,
                                                                   @RequestParam(required = false) String sort) {
        Pageable pageable = pageContainerFactory.mapRequestToPageable(size, page, sort);
        Page<OrganizationUnit> organizationUnits = organizationUnitService.getAll(pageable);
        List<OrganizationUnitDTO> unitDTOList = mapper.toDtos(organizationUnits.getContent());
        PageContainer<OrganizationUnitDTO> pageContainer = pageContainerFactory.getPageContainer(pageable, organizationUnits, unitDTOList);

        return ResponseEntity.status(HttpStatus.OK).body(pageContainer);
    }

    @Operation(
        operationId = "getOrganizationUnit",
        description = "Gets one organization unit by its id.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Organization unit was found."),
            @ApiResponse(responseCode = "404", description = "Organization unit was not found.")
        }
    )
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrganizationUnitDTO> get(@PathVariable final Long id) {
        OrganizationUnit organizationUnit = organizationUnitService.get(id);
        if (organizationUnit == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        OrganizationUnitDTO dto = mapper.toDto(organizationUnit);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @Operation(
        operationId = "createOrganizationUnit",
        description = "Creates one organization unit.",
        responses = {
            @ApiResponse(responseCode = "201", description = "Organization unit was created."),
            @ApiResponse(responseCode = "400", description = "The request body was not correct."),
        }
    )
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrganizationUnitDTO> create(@RequestBody @Valid CreateUpdateOrganizationUnitDTO createUpdateOrganizationUnitDTO) {
        OrganizationUnit organizationUnit = mapper.createFromDto(createUpdateOrganizationUnitDTO);
        organizationUnit = organizationUnitService.save(organizationUnit);
        OrganizationUnitDTO dto = mapper.toDto(organizationUnit);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(
        operationId = "updateOrganizationUnit",
        description = "Updates an organization unit.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Update was successful."),
            @ApiResponse(responseCode = "400", description = "The request body was not correct."),
            @ApiResponse(responseCode = "404", description = "The organization unit was not found.")
        }
    )
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrganizationUnitDTO> update(@PathVariable final Long id, @RequestBody @Valid CreateUpdateOrganizationUnitDTO createUpdateOrganizationUnitDTO) {
        OrganizationUnit organizationUnit = organizationUnitService.get(id);
        if (organizationUnit == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        mapper.updateFromDto(createUpdateOrganizationUnitDTO, organizationUnit);
        organizationUnit = organizationUnitService.save(organizationUnit);
        OrganizationUnitDTO dto = mapper.toDto(organizationUnit);

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @Operation(
        operationId = "deleteOrganizationUnit",
        description = "Deletes an organization unit.",
        responses = {
            @ApiResponse(responseCode = "204", description = "Unit was deleted."),
            @ApiResponse(responseCode = "404", description = "Unit was not found.")
        }
    )
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        OrganizationUnit organizationUnit = organizationUnitService.get(id);
        if (organizationUnit == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        organizationUnitService.delete(organizationUnit);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
