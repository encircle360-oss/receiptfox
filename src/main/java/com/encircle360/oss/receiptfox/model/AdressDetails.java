package com.encircle360.oss.receiptfox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdressDetails {

    private String firstName;
    private String lastName;
    private String addressLine1;
    private String addressLine2;
    private String postalCode;
    private String city;
    private String countryCode;
    private String email;
    private String vatId;
}
