package com.encircle360.oss.receiptfox.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.encircle360.oss.receiptfox.model.contact.Address;
import com.encircle360.oss.receiptfox.util.LocaleUtils;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)

public class Invoice extends AbstractEntity {

    @Builder.Default
    private LocalDate date = LocalDate.now();

    private LocalDate dateFrom;
    private LocalDate dateTo;
    private String reference;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Address sender;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Address receiver;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private PaymentDetails payment;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private List<InvoiceItem> items;

    private BigDecimal totalPrice;
    private BigDecimal totalNetPrice;
    private BigDecimal totalVat;
    private BigDecimal vatRate;
    private String currencyCode;
    private boolean isReverseCharge;
    private String footerHeadline;
    private String footerText;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Map<String, String> attributes; // some meta attributes

    // todo: should this be here?
    private byte[] document;

    public String getCurrencySymbol() {
        return LocaleUtils.getCurrencySymbol(this.currencyCode);
    }
}
