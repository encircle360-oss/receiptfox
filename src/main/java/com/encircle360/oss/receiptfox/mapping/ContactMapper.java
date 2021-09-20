package com.encircle360.oss.receiptfox.mapping;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.encircle360.oss.receiptfox.dto.contact.ContactDTO;
import com.encircle360.oss.receiptfox.dto.contact.api.CreateUpdateContactDTO;
import com.encircle360.oss.receiptfox.model.OrganizationUnit;
import com.encircle360.oss.receiptfox.model.contact.Contact;

@Mapper(uses = {ContactTypeMapper.class, SalutationMapper.class, AddressMapper.class})
public interface ContactMapper {

    ContactMapper INSTANCE = Mappers.getMapper(ContactMapper.class);

    @Mapping(target = "organizationUnitId", source = "organizationUnit.id")
    ContactDTO toDto(Contact contact);

    List<ContactDTO> toDtos(List<Contact> contacts);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "organizationUnit", source = "organizationUnit")
    @Mapping(target = "address", source = "createUpdateContact.address")
    Contact createFromDto(CreateUpdateContactDTO createUpdateContact, OrganizationUnit organizationUnit);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "organizationUnit", source = "organizationUnit")
    @Mapping(target = "address", source = "createUpdateContact.address")
    void updateFromDto(CreateUpdateContactDTO createUpdateContact, OrganizationUnit organizationUnit, @MappingTarget Contact contact);
}

