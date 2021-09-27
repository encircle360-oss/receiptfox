package com.encircle360.oss.receiptfox.dto.receipt;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.encircle360.oss.receiptfox.validation.receipt.ValidReceiptPosition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ValidReceiptPosition
public class ReceiptPositionDTO {

    @NotBlank
    private String title;

    private String description;

    @NotNull
    private Long taxRateId;

    @NotNull
    private Integer quantity;

    @NotNull
    private UnitDTO unit;

    // single gross and net should be or
    private BigDecimal singleNetAmount;

    private BigDecimal singleGrossAmount;

}
