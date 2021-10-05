package com.encircle360.oss.receiptfox.dto;

import java.math.BigDecimal;

import javax.persistence.Entity;

import com.encircle360.oss.receiptfox.dto.contact.AbstractEntityDTO;
import com.encircle360.oss.receiptfox.model.AbstractEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(name = "TaxRate", description = "Tax rate for receipts")
public class TaxRateDTO extends AbstractEntityDTO {

    @Schema(description = "Name of the tax rate", example = "19% MwSt.")
    private String name;

    @Schema(description = "Rate of the tax in percent", example = "0.19")
    private BigDecimal rate;

    @Schema(description = "Describes if this tax rate applies for countries, which are not in european union.")
    private boolean nonEu;
}
