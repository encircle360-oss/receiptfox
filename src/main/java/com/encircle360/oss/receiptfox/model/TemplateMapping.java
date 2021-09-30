package com.encircle360.oss.receiptfox.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.encircle360.oss.receiptfox.model.receipt.ReceiptType;

import lombok.AllArgsConstructor;
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
public class TemplateMapping extends AbstractEntity {

    private String templateId;

    private ReceiptType type;

    @ManyToOne
    private OrganizationUnit organizationUnit;

    private String description;

    private boolean standart;
}
