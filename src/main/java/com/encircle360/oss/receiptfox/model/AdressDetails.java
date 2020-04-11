package com.encircle360.oss.receiptfox.model;

import com.encircle360.oss.receiptfox.util.LocaleUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdressDetails {

    private String companyName;
    private String firstName;
    private String lastName;
    private String addressLine1;
    private String addressLine2;
    private String postalCode;
    private String city;
    private String countryCode;
    private String email;
    private String phoneNumber;
    private String faxNumber;
    private String websiteUrl;
    private String vatId;

    // TODO might be refactored in future for i18n
    public String getCountryDisplayName() {
        return LocaleUtils.getCountryDisplayName(this.countryCode);
    }
}
