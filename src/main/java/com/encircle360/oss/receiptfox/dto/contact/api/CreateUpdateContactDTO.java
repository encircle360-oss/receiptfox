package com.encircle360.oss.receiptfox.dto.contact.api;

import javax.validation.constraints.NotNull;

import com.encircle360.oss.receiptfox.model.contact.Address;
import com.encircle360.oss.receiptfox.model.contact.ContactType;
import com.encircle360.oss.receiptfox.model.contact.Salutation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUpdateContactDTO {

    @NotNull
    private Salutation salutation;

    private String company;

    private String firstName;

    private String lastName;

    @NotNull
    private Address address;

    private String contactNumber;

    private String phone;

    private String mobile;

    private String email;

    @NotNull
    private ContactType contactType;
}
