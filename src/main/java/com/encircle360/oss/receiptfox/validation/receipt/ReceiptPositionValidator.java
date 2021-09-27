package com.encircle360.oss.receiptfox.validation.receipt;

import java.math.BigDecimal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.encircle360.oss.receiptfox.dto.receipt.api.CreateUpdateReceiptPositionDTO;

public class ReceiptPositionValidator implements ConstraintValidator<ValidReceiptPosition, CreateUpdateReceiptPositionDTO> {
    @Override
    public boolean isValid(CreateUpdateReceiptPositionDTO value, ConstraintValidatorContext context) {
        BigDecimal singleGrossAmount = value.getSingleGrossAmount();
        BigDecimal singleNetAmount = value.getSingleNetAmount();

        if (singleGrossAmount == null && singleNetAmount == null) {
            return false;
        }

        return singleGrossAmount == null || singleNetAmount == null;
    }
}
