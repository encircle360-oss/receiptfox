package com.encircle360.oss.receiptfox.dto;

import java.math.BigDecimal;

import javax.persistence.Entity;

import com.encircle360.oss.receiptfox.dto.contact.AbstractEntityDTO;
import com.encircle360.oss.receiptfox.model.AbstractEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TaxRateDTO extends AbstractEntityDTO {

    private String name;

    private BigDecimal rate;

    private boolean nonEu;
}
