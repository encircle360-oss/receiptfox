package com.encircle360.oss.receiptfox.mapping;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.encircle360.oss.receiptfox.dto.TaxRateDTO;
import com.encircle360.oss.receiptfox.dto.api.CreateUpdateTaxRateDTO;
import com.encircle360.oss.receiptfox.model.TaxRate;

@Mapper
public interface TaxRateMapper {

    TaxRateMapper INSTANCE = Mappers.getMapper(TaxRateMapper.class);

    TaxRateDTO toDto(TaxRate taxRate);

    List<TaxRateDTO> toDtos(List<TaxRate> taxRates);

    TaxRate createFromDto(CreateUpdateTaxRateDTO createUpdateTaxRateDTO);
}
