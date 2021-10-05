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

import com.encircle360.oss.receiptfox.dto.contact.ContactDTO;
import com.encircle360.oss.receiptfox.dto.contact.ContactTypeDTO;
import com.encircle360.oss.receiptfox.dto.contact.api.CreateUpdateContactDTO;
import com.encircle360.oss.receiptfox.dto.pagination.PageContainer;
import com.encircle360.oss.receiptfox.mapping.ContactMapper;
import com.encircle360.oss.receiptfox.mapping.ContactTypeMapper;
import com.encircle360.oss.receiptfox.model.OrganizationUnit;
import com.encircle360.oss.receiptfox.model.contact.Contact;
import com.encircle360.oss.receiptfox.model.contact.ContactType;
import com.encircle360.oss.receiptfox.service.ContactService;
import com.encircle360.oss.receiptfox.service.OrganizationUnitService;
import com.encircle360.oss.receiptfox.service.PageContainerFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/contacts")
public class ContactController {

    // Mappers
    private final ContactTypeMapper contactTypeMapper = ContactTypeMapper.INSTANCE;
    private final ContactMapper contactMapper = ContactMapper.INSTANCE;

    // Services
    private final OrganizationUnitService organizationUnitService;
    private final PageContainerFactory pageContainerFactory;
    private final ContactService contactService;

    @Operation(
        operationId = "listContacts",
        description = "List all contacts",
        parameters = {
            @Parameter(name = "size", description = "The size of the page."),
            @Parameter(name = "page", description = "The number of the page."),
            @Parameter(name = "sort", description = "The sorting of the page."),
            @Parameter(name = "contactType", description = "The type of contacts."),
        }
    )
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageContainer<ContactDTO>> list(@RequestParam(required = false) Integer size,
                                                          @RequestParam(required = false) Integer page,
                                                          @RequestParam(required = false) String sort,
                                                          @RequestParam(required = false, name = "contactType") ContactTypeDTO contactTypeDTO) {
        ContactType contactType = contactTypeMapper.fromDto(contactTypeDTO);
        Pageable pageable = pageContainerFactory.mapRequestToPageable(size, page, sort);

        Page<Contact> contacts = contactService.filter(contactType, pageable);
        List<ContactDTO> contactDTOList = contactMapper.toDtos(contacts.getContent());
        PageContainer<ContactDTO> pageContainer = pageContainerFactory.getPageContainer(pageable, contacts, contactDTOList);

        return ResponseEntity.status(HttpStatus.OK).body(pageContainer);
    }

    @Operation(
        operationId = "getContact",
        description = "Returns one contact by its id",
        responses = {
            @ApiResponse(responseCode = "200", description = "Contact was found."),
            @ApiResponse(responseCode = "404", description = "Contact was not found.")
        }
    )
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ContactDTO> get(@PathVariable final Long id) {
        Contact contact = contactService.get(id);
        if (contact == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        ContactDTO contactDTO = contactMapper.toDto(contact);

        return ResponseEntity.status(HttpStatus.OK).body(contactDTO);
    }

    @Operation(
        operationId = "createContact",
        description = "Creates a contact",
        responses = {
            @ApiResponse(responseCode = "201", description = "Contact was created."),
            @ApiResponse(responseCode = "400", description = "The requestbody was not correct."),
            @ApiResponse(responseCode = "424", description = "The organization unit was not found."),
        }
    )
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ContactDTO> create(@RequestBody @Valid CreateUpdateContactDTO createUpdateContactDTO) {
        OrganizationUnit organizationUnit = organizationUnitService.get(createUpdateContactDTO.getOrganizationUnitId());
        if (organizationUnit == null) {
            return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).build();
        }

        Contact contact = contactMapper.createFromDto(createUpdateContactDTO, organizationUnit);
        contact = contactService.save(contact);
        ContactDTO contactDTO = contactMapper.toDto(contact);
        return ResponseEntity.status(HttpStatus.CREATED).body(contactDTO);
    }

    @Operation(
        operationId = "updateContact",
        description = "Updates a contact by the given payload.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Contact was updated."),
            @ApiResponse(responseCode = "400", description = "The requestbody was not correct."),
            @ApiResponse(responseCode = "404", description = "Contact was not found."),
            @ApiResponse(responseCode = "424", description = "The organization unit was not found.")
        }
    )
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ContactDTO> update(@PathVariable final Long id, @RequestBody @Valid CreateUpdateContactDTO createUpdateContactDTO) {
        Contact contact = contactService.get(id);
        if (contact == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        OrganizationUnit organizationUnit = organizationUnitService.get(createUpdateContactDTO.getOrganizationUnitId());
        if (organizationUnit == null) {
            return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).build();
        }
        contactMapper.updateFromDto(createUpdateContactDTO, organizationUnit, contact);
        contact = contactService.save(contact);
        ContactDTO contactDTO = contactMapper.toDto(contact);

        return ResponseEntity.status(HttpStatus.OK).body(contactDTO);
    }

    @Operation(
        operationId = "deleteContact",
        description = "Will delete a contact by its id.",
        responses = {
            @ApiResponse(responseCode = "204", description = "Deletion was successful."),
            @ApiResponse(responseCode = "404", description = "The contact was not found.")
        }
    )
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        Contact contact = contactService.get(id);
        if (contact == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        contactService.delete(contact);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
