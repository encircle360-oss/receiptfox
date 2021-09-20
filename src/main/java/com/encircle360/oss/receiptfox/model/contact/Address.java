package com.encircle360.oss.receiptfox.model.contact;

import com.encircle360.oss.receiptfox.util.LocaleUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    private String companyName;
    private String firstName;
    private String lastName;
    private String street;
    private String houseNumber;
    private String postalCode;
    private String city;
    private String countryCode;

    // TODO might be refactored in future for i18n
    public String getCountryDisplayName() {
        return LocaleUtils.getCountryDisplayName(this.countryCode);
    }
}
