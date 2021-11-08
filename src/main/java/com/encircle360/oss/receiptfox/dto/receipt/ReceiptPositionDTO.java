package com.encircle360.oss.receiptfox.dto.receipt;

import java.math.BigDecimal;
import java.util.HashMap;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ReceiptPosition", description = "All information for a receipt position.")
public class ReceiptPositionDTO {

    @Schema(description = "Title of the position, eg. the name of the product.", example = "Hotdogs")
    private String title;

    @Schema(description = "Description of the position, eg. the description of the product.", example = "Bread with a sausage, mustard and ketchup inside.")
    private String description;

    @Schema(description = "The id of the related tax rate.")
    private Long taxRateId;

    @Schema(description = "Quantity of the product/service.", example = "3")
    private Integer quantity;

    @Schema(description = "Unit of the product/service.", example = "PIECES")
    private UnitDTO unit;

    @Schema(description = "Single price for the product / service without tax.", example = "10.00")
    private BigDecimal unitNetAmount;

    @Schema(description = "Single price for the product / service within tax.", example = "11.90")
    private BigDecimal unitGrossAmount;

    @Schema(description = "Total price for the product / service without tax (quantity * singleNetAmount).", example = "30.00")
    private BigDecimal totalNetAmount;

    @Schema(description = "Single tax amount for the product / service.", example = "1.90")
    private BigDecimal unitTaxAmount;

    @Schema(description = "Total tax amount for the product / service (quantity * singleTaxAmount).", example = "5.70")
    private BigDecimal totalTaxAmount;

    @Schema(description = "Total price for the product / service within tax (quantity * singleGrossAmount).", example = "35.70")
    private BigDecimal totalGrossAmount;

    @Schema(description = "The tax rate for this position.", example = "0.19")
    private BigDecimal taxRatePercent;

    @Schema(description = "Additional information for an invoice position.")
    private HashMap<String, Object> meta;

}
