package com.encircle360.oss.receiptfox.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.encircle360.oss.receiptfox.dto.contact.SalutationDTO;
import com.encircle360.oss.receiptfox.model.contact.Salutation;

@Mapper
public interface SalutationMapper {

    SalutationMapper INSTANCE = Mappers.getMapper(SalutationMapper.class);

    SalutationDTO toDto(Salutation salutation);

    Salutation fromDto(SalutationDTO salutationDTO);
}
