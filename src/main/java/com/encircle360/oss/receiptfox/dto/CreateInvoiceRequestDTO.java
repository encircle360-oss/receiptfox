package com.encircle360.oss.receiptfox.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateInvoiceRequestDTO {

    private String reference; // some id or reference like billingAccountId for an invoice
    private AddressDetailsDTO sender;
    private AddressDetailsDTO receiver;
    private PaymentDetailsDTO payment;
    private List<InvoiceItemDTO> items;
    private BigDecimal totalAmount;
    private BigDecimal totalNetAmount;
    private BigDecimal totalVat;
    private BigDecimal vatRate;
    private String currencyCode;
    private boolean isReverseCharge;
    private String footerText;
    private Map<String, String> attributes; // some meta attributes
}
