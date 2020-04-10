package com.encircle360.oss.receiptfox.dto;

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
public class InvoiceItemDTO {

    @NotNull
    private BigDecimal count;

    @NotNull
    private String name;
    private String description;

    @NotNull
    private BigDecimal netPrice;

    @NotNull
    private BigDecimal vatRate; // in decimal representation like 0.19

    @NotNull
    private BigDecimal vat; // the real vat amount for one item (count=1)

    private BigDecimal totalNetPrice;
    private BigDecimal totalVat;
    private BigDecimal totalPrice;
}
