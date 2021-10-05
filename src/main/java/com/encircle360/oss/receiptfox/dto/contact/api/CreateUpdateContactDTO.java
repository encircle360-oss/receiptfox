package com.encircle360.oss.receiptfox.dto.contact.api;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.encircle360.oss.receiptfox.dto.contact.AddressDTO;
import com.encircle360.oss.receiptfox.dto.contact.ContactTypeDTO;
import com.encircle360.oss.receiptfox.dto.contact.SalutationDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CreateUpdateContact", description = "Payload for creating a contact.")
public class CreateUpdateContactDTO {

    @NotNull
    @Schema(description = "Salutation of the contact.")
    private SalutationDTO salutation;

    @Schema(description = "Company of the contact.")
    private String company;

    @Schema(description = "First name of the contact.")
    private String firstName;

    @Schema(description = "Last name of the contact.")
    private String lastName;

    @NotNull
    @Schema(description = "The complete address of the contact as an object.")
    private @Valid AddressDTO address;

    @Schema(description = "Internal number for the contact, generated by the system.")
    private String contactNumber;

    @Schema(description = "The telephone number of the contact.")
    private String phone;

    @Schema(description = "The mobile number of the contact.")
    private String mobile;

    @Schema(description = "The email address of the contact.")
    private String email;

    @NotNull
    @Schema(description = "The type of the contact.", example = "CUSTOMER")
    private ContactTypeDTO contactType;

    @NotNull
    @Schema(description = "The id of the organization unit.", example = "23")
    private Long organizationUnitId;
}
