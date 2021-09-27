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

import com.encircle360.oss.receiptfox.dto.pagination.PageContainer;
import com.encircle360.oss.receiptfox.dto.receipt.ReceiptDTO;
import com.encircle360.oss.receiptfox.dto.receipt.api.CreateUpdateReceiptDTO;
import com.encircle360.oss.receiptfox.mapping.ReceiptMapper;
import com.encircle360.oss.receiptfox.model.OrganizationUnit;
import com.encircle360.oss.receiptfox.model.contact.Contact;
import com.encircle360.oss.receiptfox.model.receipt.Receipt;
import com.encircle360.oss.receiptfox.model.receipt.ReceiptFile;
import com.encircle360.oss.receiptfox.model.receipt.ReceiptStatus;
import com.encircle360.oss.receiptfox.service.ContactService;
import com.encircle360.oss.receiptfox.service.OrganizationUnitService;
import com.encircle360.oss.receiptfox.service.PageContainerFactory;
import com.encircle360.oss.receiptfox.service.receipt.ReceiptFileService;
import com.encircle360.oss.receiptfox.service.receipt.ReceiptService;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/receipts")
public class ReceiptController {

    private final ReceiptMapper receiptMapper = ReceiptMapper.INSTANCE;

    private final PageContainerFactory pageContainerFactory;

    private final OrganizationUnitService organizationUnitService;
    private final ReceiptFileService receiptFileService;
    private final ReceiptService receiptService;
    private final ContactService contactService;

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

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReceiptDTO> get(@PathVariable final Long id) {
        Receipt receipt = receiptService.get(id);
        if (receipt == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        ReceiptDTO dto = receiptMapper.toDto(receipt);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReceiptDTO> create(@RequestBody @Valid final CreateUpdateReceiptDTO createUpdateReceiptDTO) {
        OrganizationUnit organizationUnit = organizationUnitService.get(createUpdateReceiptDTO.getOrganizationUnitId());
        ReceiptFile receiptFile = receiptFileService.get(createUpdateReceiptDTO.getReceiptFileId());
        Contact contact = contactService.get(createUpdateReceiptDTO.getContactId());

        if (organizationUnit == null) {
            return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).build();
        }

        Receipt receipt = receiptMapper.createFromDto(createUpdateReceiptDTO, organizationUnit, receiptFile, contact);
        receipt = receiptService.save(receipt);

        ReceiptDTO dto = receiptMapper.toDto(receipt);

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

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

        if (organizationUnit == null) {
            return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).build();
        }

        receiptMapper.updateFromDto(createUpdateReceiptDTO, organizationUnit, receiptFile, contact, receipt);
        receipt = receiptService.save(receipt);
        ReceiptDTO dto = receiptMapper.toDto(receipt);

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        Receipt receipt = receiptService.get(id);
        if (receipt == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        receiptService.delete(receipt);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
