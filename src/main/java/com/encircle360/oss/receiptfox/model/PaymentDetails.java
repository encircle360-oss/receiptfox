package com.encircle360.oss.receiptfox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetails {

    private Instant paidDate; // if not null, already paid
    private String paymentMethod;
    private String iban;
    private String bic;
    private String bankName;
}
