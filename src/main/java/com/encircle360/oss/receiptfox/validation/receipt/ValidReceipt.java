package com.encircle360.oss.receiptfox.validation.receipt;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ReceiptValidator.class)
public @interface ValidReceipt {

    String message() default "You can only use one of them: deliveryDate or benefit period";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
