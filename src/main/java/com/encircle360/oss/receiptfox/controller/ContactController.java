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
import com.encircle360.oss.receiptfox.dto.contact.api.CreateUpdateContactDTO;
import com.encircle360.oss.receiptfox.dto.pagination.PageContainer;
import com.encircle360.oss.receiptfox.mapping.ContactMapper;
import com.encircle360.oss.receiptfox.model.contact.Contact;
import com.encircle360.oss.receiptfox.service.ContactService;
import com.encircle360.oss.receiptfox.service.PageContainerFactory;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/contacts")
public class ContactController {

    private final ContactService contactService;

    private final ContactMapper mapper = ContactMapper.INSTANCE;

    private final PageContainerFactory pageContainerFactory;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageContainer<ContactDTO>> list(@RequestParam(required = false) Integer size,
                                                          @RequestParam(required = false) Integer page,
                                                          @RequestParam(required = false) String sort) {
        Pageable pageable = pageContainerFactory.mapRequestToPageable(size, page, sort);
        Page<Contact> contacts = contactService.findAll(pageable);
        List<ContactDTO> contactDTOList = mapper.toDtos(contacts.getContent());
        PageContainer<ContactDTO> pageContainer = pageContainerFactory.getPageContainer(pageable, contacts, contactDTOList);

        return ResponseEntity.status(HttpStatus.OK).body(pageContainer);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ContactDTO> get(@PathVariable final Long id) {
        Contact contact = contactService.get(id);
        if (contact == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        ContactDTO contactDTO = mapper.toDto(contact);

        return ResponseEntity.status(HttpStatus.OK).body(contactDTO);
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ContactDTO> create(@RequestBody @Valid CreateUpdateContactDTO createUpdateContactDTO) {
        Contact contact = mapper.createFromDto(createUpdateContactDTO);
        contact = contactService.save(contact);
        ContactDTO contactDTO = mapper.toDto(contact);
        return ResponseEntity.status(HttpStatus.CREATED).body(contactDTO);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ContactDTO> update(@PathVariable final Long id, @RequestBody @Valid CreateUpdateContactDTO createUpdateContactDTO) {
        Contact contact = contactService.get(id);
        if (contact == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        mapper.updateFromDto(createUpdateContactDTO, contact);
        contact = contactService.save(contact);
        ContactDTO contactDTO = mapper.toDto(contact);

        return ResponseEntity.status(HttpStatus.OK).body(contactDTO);
    }

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
