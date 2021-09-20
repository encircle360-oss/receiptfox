package com.encircle360.oss.receiptfox.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.encircle360.oss.receiptfox.model.contact.Contact;
import com.encircle360.oss.receiptfox.model.contact.ContactType;
import com.encircle360.oss.receiptfox.repository.ContactRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;

    public Page<Contact> findAll(Pageable pageable) {
        return contactRepository.findAll(pageable);
    }

    public Contact get(Long id) {
        return contactRepository.findById(id).orElse(null);
    }

    public Contact save(Contact contact) {
        return contactRepository.save(contact);
    }

    public void delete(Contact contact) {
        contactRepository.delete(contact);
    }

    public Page<Contact> filter(ContactType contactType, Pageable pageable) {
        if(contactType == null) {
            return findAll(pageable);
        }
        return contactRepository.findAllByContactType(contactType, pageable);
    }
}
