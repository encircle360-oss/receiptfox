package com.encircle360.oss.receiptfox.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TemplateMappingMapper {

    TemplateMappingMapper INSTANCE = Mappers.getMapper(TemplateMappingMapper.class);
}
