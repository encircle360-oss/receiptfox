package com.encircle360.oss.receiptfox.mapping;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.encircle360.oss.receiptfox.dto.contact.ContactDTO;
import com.encircle360.oss.receiptfox.dto.contact.api.CreateUpdateContactDTO;
import com.encircle360.oss.receiptfox.model.contact.Contact;

@Mapper
public interface ContactMapper {

    ContactMapper INSTANCE = Mappers.getMapper(ContactMapper.class);

    ContactDTO toDto(Contact contact);

    List<ContactDTO> toDtos(List<Contact> contacts);

    Contact createFromDto(CreateUpdateContactDTO createUpdateContact);

    void updateFromDto(CreateUpdateContactDTO createUpdateContact, @MappingTarget Contact contact);
}

