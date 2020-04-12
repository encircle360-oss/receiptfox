package com.encircle360.oss.receiptfox.model;

import com.encircle360.oss.receiptfox.util.LocaleUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@Builder
@Document
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {

    @Id
    private String id;

    @Builder.Default
    private LocalDate date = LocalDate.now();

    private LocalDate dateFrom;
    private LocalDate dateTo;
    private String reference; // some id or reference like billingAccountId for an invoice
    private AdressDetails sender;
    private AdressDetails receiver;
    private PaymentDetails payment;
    private List<InvoiceItem> items;
    private BigDecimal totalPrice;
    private BigDecimal totalNetPrice;
    private BigDecimal totalVat;
    private BigDecimal vatRate;
    private String currencyCode;
    private boolean isReverseCharge;
    private String footerHeadline;
    private String footerText;
    private Map<String, String> attributes; // some meta attributes
    private byte[] document;

    public String getCurrencySymbol() {
        return LocaleUtils.getCurrencySymbol(this.currencyCode);
    }
}
