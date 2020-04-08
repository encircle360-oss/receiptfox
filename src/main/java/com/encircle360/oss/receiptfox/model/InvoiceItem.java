package com.encircle360.oss.receiptfox.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
public class InvoiceItem {

    @NotNull
    private BigDecimal count;

    @NotNull
    private String name;
    private String description;

    @NotNull
    private BigDecimal price;

    @NotNull
    private BigDecimal vatRate; // in decimal representation like 0.19

    @NotNull
    private BigDecimal vat; // the real vat amount for one item (count=1)
}
