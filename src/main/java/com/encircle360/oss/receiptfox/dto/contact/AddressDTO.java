package com.encircle360.oss.receiptfox.dto.contact;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Address", description = "An address with all needed properties.")
public class AddressDTO {
    @Schema(description = "The name of the company this address belongs to.")
    private String company;

    @Schema(description = "The first name of the person/stakeholder this address belongs to.")
    private String firstName;

    @Schema(description = "The last name of the person/stakeholder this address belongs to.")
    private String lastName;

    @Schema(description = "The street this address belongs to.")
    private String street;

    @Schema(description = "The house number this address belongs to.")
    private String houseNumber;

    @Schema(description = "The postal code this address belongs to.")
    private String postalCode;

    @Schema(description = "The city this address belongs to.")
    private String city;

    @Schema(description = "The country code this address belongs to.")
    private String countryCode;
}
