package com.encircle360.oss.receiptfox.dto.organizationunit.api;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.encircle360.oss.receiptfox.dto.contact.AddressDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CreateUpdateOrganizationUnit", description = "Payload for creating or updating an organziation unit.")
public class CreateUpdateOrganizationUnitDTO {

    @NotBlank
    @Schema(description = "Name of the organization unit.")
    private String name;

    @NotNull
    @Schema(description = "Address of the organization unit.")
    private AddressDTO address;

    @Schema(description = "Image of the organization unit.")
    private String organizationImage;

    @Schema(description = "Number pattern for receipts of the organization unit.")
    private String receiptNumberPattern;

    @Schema(description = "A default template which is used for receipts")
    private String defaultTemplateId;
}
