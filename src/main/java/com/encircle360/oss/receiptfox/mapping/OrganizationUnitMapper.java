package com.encircle360.oss.receiptfox.mapping;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.encircle360.oss.receiptfox.dto.organizationunit.OrganizationUnitDTO;
import com.encircle360.oss.receiptfox.dto.organizationunit.api.CreateUpdateOrganizationUnitDTO;
import com.encircle360.oss.receiptfox.model.OrganizationUnit;

@Mapper(uses = AddressMapper.class)
public interface OrganizationUnitMapper {

    OrganizationUnitMapper INSTANCE = Mappers.getMapper(OrganizationUnitMapper.class);

    OrganizationUnitDTO toDto(OrganizationUnit organizationUnit);

    List<OrganizationUnitDTO> toDtos(List<OrganizationUnit> organizationUnits);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    @Mapping(target = "version", ignore = true)
    OrganizationUnit createFromDto(CreateUpdateOrganizationUnitDTO createUpdateOrganizationUnit);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateFromDto(CreateUpdateOrganizationUnitDTO createUpdateOrganizationUnitDTO, @MappingTarget OrganizationUnit organizationUnit);
}
