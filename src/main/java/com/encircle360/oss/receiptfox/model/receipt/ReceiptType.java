package com.encircle360.oss.receiptfox.model.receipt;

public enum ReceiptType {
    INVOICE,
    INTERIM_BILL, // Abschlagsrechnung
    CREDIT_VOUCHER, // Gutschrift
    INVOICE_CORRECTION, // Rechnungskorrektur
    DOWN_PAYMENT // Anzahlungsrechnung
}
