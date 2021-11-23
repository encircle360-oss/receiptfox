package com.encircle360.oss.receiptfox.mapping.receipt;

import com.encircle360.oss.receiptfox.dto.receipt.ReceiptPositionDTO;
import com.encircle360.oss.receiptfox.dto.receipt.api.CreateUpdateReceiptPositionDTO;
import com.encircle360.oss.receiptfox.model.TaxRate;
import com.encircle360.oss.receiptfox.model.receipt.ReceiptPosition;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Mapper(builder = @Builder(disableBuilder = true))
public interface ReceiptPositionMapper {

    ReceiptPositionMapper INSTANCE = Mappers.getMapper(ReceiptPositionMapper.class);

    @Mapping(source = "taxRate", target = "taxRate")
    @Mapping(target = "unitTaxAmount", ignore = true)
    @Mapping(target = "totalNetAmount", ignore = true)
    @Mapping(target = "totalTaxAmount", ignore = true)
    @Mapping(target = "totalGrossAmount", ignore = true)
    @Mapping(source = "taxRate.rate", target = "taxRatePercent")
    ReceiptPosition fromDto(CreateUpdateReceiptPositionDTO receiptPositionDTO, TaxRate taxRate);

    @Mapping(source = "taxRate.id", target = "taxRateId")
    ReceiptPositionDTO toDto(ReceiptPosition receiptPosition);

    List<ReceiptPositionDTO> toDtos(List<ReceiptPosition> receiptPositions);

    @AfterMapping
    default ReceiptPosition postProcess(@MappingTarget ReceiptPosition position) {
        BigDecimal singleNetAmount = position.getUnitNetAmount();
        BigDecimal singleGrossAmount = position.getUnitGrossAmount();
        BigDecimal taxMultiplier = BigDecimal.ONE.add(position.getTaxRatePercent());

        // Both values are null should not be possible
        // only for avoiding null pointers
        if (singleNetAmount == null && singleGrossAmount == null) {
            return null;
        }

        if (singleNetAmount != null) {
            singleGrossAmount = singleNetAmount.multiply(taxMultiplier);
            position.setUnitGrossAmount(singleGrossAmount);
        } else {
            singleNetAmount = singleGrossAmount.divide(taxMultiplier, 20, RoundingMode.HALF_UP);
            position.setUnitNetAmount(singleNetAmount);
        }

        BigDecimal quantity = BigDecimal.valueOf(position.getQuantity());

        position.setUnitTaxAmount(singleGrossAmount.subtract(singleNetAmount));
        position.setTotalNetAmount(singleNetAmount.multiply(quantity));
        position.setTotalGrossAmount(singleGrossAmount.multiply(quantity));
        position.setTotalTaxAmount(position.getTotalGrossAmount().subtract(position.getTotalNetAmount()));

        return position;
    }
}
