package com.encircle360.oss.receiptfox.dto.receipt;

import java.math.BigDecimal;

import javax.persistence.ManyToOne;

import com.encircle360.oss.receiptfox.model.TaxRate;
import com.encircle360.oss.receiptfox.model.receipt.Unit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptPositionDTO {

    private String title;

    private String description;

    private Long taxRateId;

    private Integer quantity;

    private UnitDTO unit;

    private BigDecimal singleNetAmount;

    private BigDecimal singleGrossAmount;

    private BigDecimal totalNetAmount;

    private BigDecimal singleTaxAmount;

    private BigDecimal totalTaxAmount;

    private BigDecimal totalGrossAmount;

    private BigDecimal taxRatePercent;

}
