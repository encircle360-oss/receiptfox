package com.encircle360.oss.receiptfox.dto.api;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CreateUpdateTaxRate", description = "DTO for creating or updating a tax rate.")
public class CreateUpdateTaxRateDTO {

    @NotBlank
    @Schema(description = "Name of the tax rate", example = "19% MwSt.")
    private String name;

    @NotNull
    @Schema(description = "Rate of the tax in percent", example = "0.19")
    private BigDecimal rate;

    @Schema(description = "Describes if this tax rate applies for countries, which are not in european union.")
    private boolean nonEu;
}
