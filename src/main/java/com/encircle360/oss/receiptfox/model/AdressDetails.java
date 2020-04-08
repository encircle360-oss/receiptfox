package com.encircle360.oss.receiptfox.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
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
