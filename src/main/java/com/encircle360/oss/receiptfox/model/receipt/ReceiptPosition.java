package com.encircle360.oss.receiptfox.model.receipt;

import java.math.BigDecimal;

import javax.persistence.ManyToOne;

import com.encircle360.oss.receiptfox.model.TaxRate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptPosition {

    private String title;

    private String description;

    @ManyToOne
    private TaxRate taxRate;

    private Integer quantity;

    private Unit unit;

    private BigDecimal singleNetAmount;

    private BigDecimal singleGrossAmount;

    private BigDecimal totalNetAmount;

    private BigDecimal singleTaxAmount;

    private BigDecimal totalTaxAmount;

    private BigDecimal totalGrossAmount;

    private BigDecimal taxRatePercent;

}
