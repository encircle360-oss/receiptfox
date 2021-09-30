package com.encircle360.oss.receiptfox.controller.receipt;

import java.util.List;
import java.util.stream.Collectors;

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

import com.encircle360.oss.receiptfox.dto.pagination.PageContainer;
import com.encircle360.oss.receiptfox.dto.receipt.ReceiptDTO;
import com.encircle360.oss.receiptfox.dto.receipt.api.CreateUpdateReceiptDTO;
import com.encircle360.oss.receiptfox.mapping.receipt.ReceiptMapper;
import com.encircle360.oss.receiptfox.mapping.receipt.ReceiptPositionMapper;
import com.encircle360.oss.receiptfox.model.OrganizationUnit;
import com.encircle360.oss.receiptfox.model.TaxRate;
import com.encircle360.oss.receiptfox.model.contact.Contact;
import com.encircle360.oss.receiptfox.model.receipt.Receipt;
import com.encircle360.oss.receiptfox.model.receipt.ReceiptFile;
import com.encircle360.oss.receiptfox.model.receipt.ReceiptPosition;
import com.encircle360.oss.receiptfox.model.receipt.ReceiptStatus;
import com.encircle360.oss.receiptfox.service.ContactService;
import com.encircle360.oss.receiptfox.service.OrganizationUnitService;
import com.encircle360.oss.receiptfox.service.PageContainerFactory;
import com.encircle360.oss.receiptfox.service.TaxRateService;
import com.encircle360.oss.receiptfox.service.receipt.ReceiptFileService;
import com.encircle360.oss.receiptfox.service.receipt.ReceiptService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/receipts")
public class ReceiptController {

    // mappers
    private final ReceiptPositionMapper receiptPositionMapper = ReceiptPositionMapper.INSTANCE;
    private final ReceiptMapper receiptMapper = ReceiptMapper.INSTANCE;

    // services
    private final OrganizationUnitService organizationUnitService;
    private final PageContainerFactory pageContainerFactory;
    private final ReceiptFileService receiptFileService;
    private final ReceiptService receiptService;
    private final ContactService contactService;
    private final TaxRateService taxRateService;

    @Operation(
        operationId = "listReceipts",
        description = "Lists all receipts in database.",
        parameters = {
            @Parameter(name = "page", description = "The number of the page", example = "0"),
            @Parameter(name = "size", description = "The size of the page", example = "50"),
            @Parameter(name = "sort", description = "The sorting of the page", example = "id,desc"),
            @Parameter(name = "organizationUnitId", description = "The id of an organization unit, the page should be filtered for.", example = "2")
        }
    )
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageContainer<ReceiptDTO>> list(@RequestParam(name = "page", required = false) final Integer page,
                                                          @RequestParam(name = "size", required = false) final Integer size,
                                                          @RequestParam(name = "sort", required = false) final String sort,
                                                          @RequestParam(name = "organizationUnitId", required = false) final Long organizationUnitId) {
        Pageable pageable = pageContainerFactory.mapRequestToPageable(size, page, sort);
        Page<Receipt> receipts = receiptService.filter(pageable, organizationUnitId);
        List<ReceiptDTO> receiptDTOList = receiptMapper.toDtos(receipts.getContent());
        PageContainer<ReceiptDTO> pageContainer = pageContainerFactory.getPageContainer(pageable, receipts, receiptDTOList);

        return ResponseEntity.status(HttpStatus.OK).body(pageContainer);
    }

    @Operation(
        operationId = "getReceipt",
        description = "Gets one receipt from database.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Receipt was found."),
            @ApiResponse(responseCode = "404", description = "The receipt was not found.")
        }
    )
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReceiptDTO> get(@PathVariable final Long id) {
        Receipt receipt = receiptService.get(id);
        if (receipt == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        ReceiptDTO dto = receiptMapper.toDto(receipt);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @Operation(
        operationId = "createReceipt",
        description = "Creates a receipt by the given payload.",
        responses = {
            @ApiResponse(responseCode = "201", description = "The receipt was created successfully."),
            @ApiResponse(responseCode = "400", description = "The request body was not correct."),
            @ApiResponse(responseCode = "424", description = "The linked organization unit does not exists.")
        }
    )
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReceiptDTO> create(@RequestBody @Valid final CreateUpdateReceiptDTO createUpdateReceiptDTO) {
        OrganizationUnit organizationUnit = organizationUnitService.get(createUpdateReceiptDTO.getOrganizationUnitId());
        ReceiptFile receiptFile = receiptFileService.get(createUpdateReceiptDTO.getReceiptFileId());
        Contact contact = contactService.get(createUpdateReceiptDTO.getContactId());

        if (organizationUnit == null) {
            return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).build();
        }

        List<ReceiptPosition> receiptPositions = receiptPositions(createUpdateReceiptDTO);

        if (receiptPositions.contains(null)) {
            return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).build();
        }

        Receipt receipt = receiptMapper.createFromDto(createUpdateReceiptDTO, receiptPositions, organizationUnit, receiptFile, contact);
        receipt = receiptService.save(receipt);

        ReceiptDTO dto = receiptMapper.toDto(receipt);

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(
        operationId = "updateReceipt",
        description = "Update a receipt by the given payload and the id.",
        responses = {
            @ApiResponse(responseCode = "200", description = "The receipt was updated successfully."),
            @ApiResponse(responseCode = "400", description = "The request body was not correct."),
            @ApiResponse(responseCode = "412", description = "The receipt is not in draft status."),
            @ApiResponse(responseCode = "424", description = "The linked organization unit does not exists."),
        }
    )
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReceiptDTO> update(@PathVariable final Long id, @RequestBody @Valid final CreateUpdateReceiptDTO createUpdateReceiptDTO) {
        Receipt receipt = receiptService.get(id);
        if (receipt == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (!receipt.getStatus().equals(ReceiptStatus.DRAFT)) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }

        OrganizationUnit organizationUnit = organizationUnitService.get(createUpdateReceiptDTO.getOrganizationUnitId());
        ReceiptFile receiptFile = receiptFileService.get(createUpdateReceiptDTO.getReceiptFileId());
        Contact contact = contactService.get(createUpdateReceiptDTO.getContactId());

        List<ReceiptPosition> receiptPositions = receiptPositions(createUpdateReceiptDTO);

        if (receiptPositions.contains(null) || organizationUnit == null) {
            return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).build();
        }

        receiptMapper.updateFromDto(createUpdateReceiptDTO, receiptPositions, organizationUnit, receiptFile, contact, receipt);
        receipt = receiptService.save(receipt);
        ReceiptDTO dto = receiptMapper.toDto(receipt);

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @Operation(
        operationId = "deleteReceipt",
        description = "Deletes a receipt from database",
        responses = {
            @ApiResponse(responseCode = "204", description = "Deletion was successful."),
            @ApiResponse(responseCode = "404", description = "The receipt was not found."),
            @ApiResponse(responseCode = "412", description = "Completed receipts cannot be deleted, only if status is DRAFT.")
        }
    )
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        Receipt receipt = receiptService.get(id);
        if (receipt == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (!receipt.getStatus().equals(ReceiptStatus.DRAFT)) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }

        receiptService.delete(receipt);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private List<ReceiptPosition> receiptPositions(CreateUpdateReceiptDTO createUpdateReceiptDTO) {
        return createUpdateReceiptDTO
            .getPositions()
            .stream()
            .map(createUpdateReceiptPositionDTO -> {
                TaxRate taxRate = taxRateService.get(createUpdateReceiptPositionDTO.getTaxRateId());
                if (taxRate == null) {
                    return null;
                }
                return receiptPositionMapper.fromDto(createUpdateReceiptPositionDTO, taxRate);
            })
            .collect(Collectors.toList());
    }
}
