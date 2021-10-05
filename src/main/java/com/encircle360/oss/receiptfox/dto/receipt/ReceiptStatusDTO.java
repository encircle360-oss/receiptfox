package com.encircle360.oss.receiptfox.dto.receipt;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ReceiptStatus", description = "Status of a receipt.")
public enum ReceiptStatusDTO {
    DRAFT, OPEN, PAID, CANCELED
}
