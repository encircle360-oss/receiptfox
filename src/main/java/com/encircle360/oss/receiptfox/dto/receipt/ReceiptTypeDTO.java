package com.encircle360.oss.receiptfox.dto.receipt;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ReceiptType", description = "Type of a receipt.")
public enum ReceiptTypeDTO {
    INVOICE,
    INTERIM_BILL, // Abschlagsrechnung
    CREDIT_VOUCHER, // Gutschrift
    INVOICE_CORRECTION, // Rechnungskorrektur
    DOWN_PAYMENT // Anzahlungsrechnung
}
