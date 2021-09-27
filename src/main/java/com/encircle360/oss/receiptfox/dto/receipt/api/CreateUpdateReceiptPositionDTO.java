package com.encircle360.oss.receiptfox.dto.receipt.api;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.encircle360.oss.receiptfox.dto.receipt.UnitDTO;
import com.encircle360.oss.receiptfox.validation.receipt.ValidReceiptPosition;
import com.fasterxml.jackson.databind.jsonschema.JsonSerializableSchema;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ValidReceiptPosition
@Schema(name = "CreateUpdateReceiptPosition", description = "Dto for creating a receipt position.")
public class CreateUpdateReceiptPositionDTO {

    @NotBlank
    @Schema(description = "Title of the position, eg. the name of the product.", example = "Hotdogs")
    private String title;

    @Schema(description = "Description of the position, eg. the description of the product.", example = "Bread with a sausage, mustard and ketchup inside.")
    private String description;

    @NotNull
    @Schema(description = "The id of the related tax rate.")
    private Long taxRateId;

    @NotNull
    @Schema(description = "Quantity of the product/service.", example = "3")
    private Integer quantity;

    @NotNull
    @Schema(description = "Unit of the product/service.", example = "PIECES")
    private UnitDTO unit;

    @Schema(description = "Single price for the product / service without tax.", example = "10.00")
    private BigDecimal singleNetAmount;

    @Schema(description = "Single price for the product / service within tax.", example = "11.90")
    private BigDecimal singleGrossAmount;

}
