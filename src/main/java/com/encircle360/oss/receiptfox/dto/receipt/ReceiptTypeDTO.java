package com.encircle360.oss.receiptfox.dto.receipt;

public enum ReceiptTypeDTO {
    INVOICE,
    INTERIM_BILL, // Abschlagsrechnung
    CREDIT_VOUCHER, // Gutschrift
    INVOICE_CORRECTION, // Rechnungskorrektur
    DOWN_PAYMENT // Anzahlungsrechnung
}
