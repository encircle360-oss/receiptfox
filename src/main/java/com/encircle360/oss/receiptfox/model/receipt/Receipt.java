package com.encircle360.oss.receiptfox.model.receipt;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.encircle360.oss.receiptfox.model.AbstractEntity;
import com.encircle360.oss.receiptfox.model.OrganizationUnit;
import com.encircle360.oss.receiptfox.model.contact.Address;
import com.encircle360.oss.receiptfox.model.contact.Contact;

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
@Table(indexes = {@Index(columnList = "receiptNumber", unique = true)})
public class Receipt extends AbstractEntity {

    @Enumerated(EnumType.STRING)
    private ReceiptType receiptType;

    @ManyToOne
    private OrganizationUnit organizationUnit;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Map<String, String> meta;

    @OneToOne
    private ReceiptFile receiptFile;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Address senderAddress;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Address receiverAddress;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Address benefitReceiverAddress;

    @ManyToOne
    private Contact contact;

    private BigDecimal netAmount;

    private BigDecimal grossAmount;

    private BigDecimal taxAmount;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private List<ReceiptPosition> positions;

    // Templates handled by docs rabbit
    private String templateId;

    private String receiptNumber;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private ReceiptStatus status = ReceiptStatus.DRAFT;

    private LocalDate receiptDate;

    private LocalDate deliveryDate;

    private LocalDate benefitPeriodStart;

    private LocalDate benefitPeriodEnd;
}
