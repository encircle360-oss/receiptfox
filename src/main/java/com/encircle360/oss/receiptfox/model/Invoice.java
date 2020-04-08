package com.encircle360.oss.receiptfox.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
@Document
public class Invoice {

    @Id
    private String id;
    private String reference; // some id or reference like billingAccountId for an invoice

    private AdressDetails sender;
    private AdressDetails receiver;

    private List<InvoiceItem> items;

    private BigDecimal totalAmount;
    private BigDecimal totalNetAmount;
    private BigDecimal totalVat;
    private BigDecimal vatRate;
    private boolean isReverseCharge;
    private String footerText;
    private Map<String, String> attributes; // some meta attributes

    private byte[] document;
}
