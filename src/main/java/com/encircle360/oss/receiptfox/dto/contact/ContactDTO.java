package com.encircle360.oss.receiptfox.dto.contact;

import com.encircle360.oss.receiptfox.model.contact.Address;
import com.encircle360.oss.receiptfox.model.contact.ContactType;
import com.encircle360.oss.receiptfox.model.contact.Salutation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ContactDTO extends AbstractEntityDTO {

    private SalutationDTO salutation;

    private String company;

    private String firstName;

    private String lastName;

    private Address address;

    private String contactNumber;

    private String phone;

    private String mobile;

    private String email;

    private ContactTypeDTO contactType;
}
