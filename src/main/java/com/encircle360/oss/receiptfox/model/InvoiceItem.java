package com.encircle360.oss.receiptfox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItem {

    private BigDecimal count;

    private String name;
    private String description;

    private BigDecimal netPrice;

    private BigDecimal vatRate; // in decimal representation like 0.19

    private BigDecimal vat; // the real vat amount for one item (count=1)

    private BigDecimal totalNetPrice;

    private BigDecimal totalVat;

    private BigDecimal totalPrice;
}
