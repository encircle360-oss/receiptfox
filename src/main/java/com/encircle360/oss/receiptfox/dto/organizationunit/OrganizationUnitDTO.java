package com.encircle360.oss.receiptfox.dto.organizationunit;

import com.encircle360.oss.receiptfox.dto.contact.AbstractEntityDTO;
import com.encircle360.oss.receiptfox.dto.contact.AddressDTO;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "OrganizationUnit", description = "An organization unit.")
public class OrganizationUnitDTO extends AbstractEntityDTO {

    @Schema(description = "Name of the organization unit.")
    private String name;

    @Schema(description = "Address of the organization unit.")
    private AddressDTO address;

    @Schema(description = "Image of the organization unit.")
    private String organizationImage;

    @Schema(description = "Number pattern for receipts of the organization unit.")
    private String receiptNumberPattern;

    @Schema(description = "A default template which is used for receipts")
    private String defaultTemplateId;
}
