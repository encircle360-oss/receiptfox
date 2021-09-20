package com.encircle360.oss.receiptfox.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.encircle360.oss.receiptfox.dto.contact.AddressDTO;
import com.encircle360.oss.receiptfox.model.contact.Address;

@Mapper
public interface AddressMapper {

    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);

    AddressDTO toDto(Address address);

    Address fromDto(AddressDTO address);
}
