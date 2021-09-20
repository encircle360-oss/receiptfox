package com.encircle360.oss.receiptfox.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.encircle360.oss.receiptfox.model.contact.Contact;

@Repository
public interface ContactRepository extends CrudRepository<Contact, Long> {
    Page<Contact> findAll(Pageable pageable);
}
