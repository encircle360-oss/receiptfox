package com.encircle360.oss.receiptfox.dto.receipt;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Unit", description = "Unit for a receipt position")
public enum UnitDTO {
    PIECES, HOURS, DAYS, WEEKS, MONTH, YEARS
}
