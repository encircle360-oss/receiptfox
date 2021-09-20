package com.encircle360.oss.receiptfox.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.encircle360.oss.receiptfox.dto.contact.ContactTypeDTO;
import com.encircle360.oss.receiptfox.model.contact.ContactType;

@Mapper
public interface ContactTypeMapper {

    ContactTypeMapper INSTANCE = Mappers.getMapper(ContactTypeMapper.class);

    ContactTypeDTO toDto(ContactType contactType);

    ContactType fromDto(ContactTypeDTO contactTypeDTO);
}
