package com.encircle360.oss.receiptfox.validation.receipt;

import java.time.LocalDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.encircle360.oss.receiptfox.dto.receipt.api.CreateUpdateReceiptDTO;

public class ReceiptValidator implements ConstraintValidator<ValidReceipt, CreateUpdateReceiptDTO> {
    @Override
    public boolean isValid(CreateUpdateReceiptDTO value, ConstraintValidatorContext context) {
        LocalDate deliveryDate = value.getDeliveryDate();
        LocalDate benefitPeriodStart = value.getBenefitPeriodStart();
        LocalDate benefitPeriodEnd = value.getBenefitPeriodEnd();

        if (deliveryDate != null && (benefitPeriodStart != null || benefitPeriodEnd != null)) {
            return false;
        }

        return !(deliveryDate == null && (benefitPeriodStart == null || benefitPeriodEnd == null));
    }
}
