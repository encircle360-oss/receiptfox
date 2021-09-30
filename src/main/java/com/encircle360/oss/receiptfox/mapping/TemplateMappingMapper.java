package com.encircle360.oss.receiptfox.mapping;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.encircle360.oss.receiptfox.dto.TemplateMappingDTO;
import com.encircle360.oss.receiptfox.dto.api.CreateUpdateTemplateMappingDTO;
import com.encircle360.oss.receiptfox.model.OrganizationUnit;
import com.encircle360.oss.receiptfox.model.TemplateMapping;

@Mapper
public interface TemplateMappingMapper {

    TemplateMappingMapper INSTANCE = Mappers.getMapper(TemplateMappingMapper.class);

    List<TemplateMappingDTO> toDtos(List<TemplateMapping> content);

    @Mapping(target = "organizationUnitId", source = "organizationUnit.id")
    TemplateMappingDTO toDto(TemplateMapping templateMapping);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "organizationUnit", source = "organizationUnit")
    TemplateMapping createFromDto(CreateUpdateTemplateMappingDTO createUpdateTemplateMappingDTO, OrganizationUnit organizationUnit);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "organizationUnit", source = "organizationUnit")
    void updateFromDto(CreateUpdateTemplateMappingDTO createUpdateTemplateMappingDTO, OrganizationUnit organizationUnit, @MappingTarget TemplateMapping templateMapping);
}
