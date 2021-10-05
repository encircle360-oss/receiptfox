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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.encircle360.oss.receiptfox.dto.TaxRateDTO;
import com.encircle360.oss.receiptfox.dto.api.CreateUpdateTaxRateDTO;
import com.encircle360.oss.receiptfox.dto.pagination.PageContainer;
import com.encircle360.oss.receiptfox.mapping.TaxRateMapper;
import com.encircle360.oss.receiptfox.model.TaxRate;
import com.encircle360.oss.receiptfox.service.PageContainerFactory;
import com.encircle360.oss.receiptfox.service.TaxRateService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

/**
 * Tax rates cannot be updated
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/tax-rates")
public class TaxRateController {

    private final TaxRateService taxRateService;

    private final PageContainerFactory pageContainerFactory;

    private final TaxRateMapper mapper = TaxRateMapper.INSTANCE;

    @Operation(
        operationId = "listTaxRates",
        description = "List all tax rates",
        parameters = {
            @Parameter(name = "size", description = "The size of the page."),
            @Parameter(name = "page", description = "The number of the page."),
            @Parameter(name = "sort", description = "The sort of the page.")
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Page was returned.")
        }
    )
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageContainer<TaxRateDTO>> list(@RequestParam(required = false) final Integer size,
                                                          @RequestParam(required = false) final Integer page,
                                                          @RequestParam(required = false) final String sort) {
        Pageable pageable = pageContainerFactory.mapRequestToPageable(size, page, sort);
        Page<TaxRate> taxRates = taxRateService.findAll(pageable);
        List<TaxRateDTO> dtos = mapper.toDtos(taxRates.getContent());

        PageContainer<TaxRateDTO> pageContainer = pageContainerFactory.getPageContainer(pageable, taxRates, dtos);
        return ResponseEntity.status(HttpStatus.OK).body(pageContainer);
    }

    @Operation(
        operationId = "getTaxRate",
        description = "Returns one tax rate by its id.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Tax rate was returned."),
            @ApiResponse(responseCode = "404", description = "The tax rate was not found.")
        }
    )
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaxRateDTO> get(@PathVariable final Long id) {
        TaxRate taxRate = taxRateService.get(id);
        if (taxRate == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        TaxRateDTO dto = mapper.toDto(taxRate);

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @Operation(
        operationId = "createTaxRate",
        description = "Creates a tax rate.",
        responses = {
            @ApiResponse(responseCode = "201", description = "The tax rate was created successfully.")
        }
    )
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaxRateDTO> create(@RequestBody @Valid final CreateUpdateTaxRateDTO createUpdateTaxRateDTO) {
        TaxRate taxRate = mapper.createFromDto(createUpdateTaxRateDTO);
        taxRate = taxRateService.save(taxRate);
        TaxRateDTO dto = mapper.toDto(taxRate);

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(
        operationId = "deleteTaxRate",
        description = "Deletes a tax rate.",
        responses = {
            @ApiResponse(responseCode = "204", description = "The tax rate was deleted."),
            @ApiResponse(responseCode = "404", description = "The tax rate was not found.")
        }
    )
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        TaxRate taxRate = taxRateService.get(id);
        if (taxRate == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        taxRateService.delete(taxRate);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
