package com.encircle360.oss.receiptfox.mapping.receipt;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.encircle360.oss.receiptfox.dto.receipt.ReceiptPositionDTO;
import com.encircle360.oss.receiptfox.dto.receipt.api.CreateUpdateReceiptPositionDTO;
import com.encircle360.oss.receiptfox.model.TaxRate;
import com.encircle360.oss.receiptfox.model.receipt.ReceiptPosition;

@Mapper
public interface ReceiptPositionMapper {

    ReceiptPositionMapper INSTANCE = Mappers.getMapper(ReceiptPositionMapper.class);

    @Mapping(source = "taxRate", target = "taxRate")
    @Mapping(target = "totalNetAmount", ignore = true)
    @Mapping(target = "totalTaxAmount", ignore = true)
    @Mapping(target = "singleTaxAmount", ignore = true)
    @Mapping(target = "totalGrossAmount", ignore = true)
    @Mapping(source = "taxRate.rate", target = "taxRatePercent")
    ReceiptPosition fromDto(CreateUpdateReceiptPositionDTO receiptPositionDTO, TaxRate taxRate);

    @Mapping(source = "taxRate.id", target = "taxRateId")
    ReceiptPositionDTO toDto(ReceiptPosition receiptPosition);

    List<ReceiptPositionDTO> toDtos(List<ReceiptPosition> receiptPositions);
}
