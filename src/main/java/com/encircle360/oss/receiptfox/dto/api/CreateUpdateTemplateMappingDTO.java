package com.encircle360.oss.receiptfox.dto.api;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.encircle360.oss.receiptfox.dto.receipt.ReceiptTypeDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CreateUpdateTemplateMapping", description = "A template mapped to an organization unit with type.")
public class CreateUpdateTemplateMappingDTO {

    @NotBlank
    @Schema(description = "The docsrabbit id of the template.")
    private String templateId;

    @NotNull
    @Schema(description = "The receipt type this template id should be applied.")
    private ReceiptTypeDTO type;

    @NotNull
    @Schema(description = "The id of the related organization unit.")
    private Long organizationUnitId;

    @Schema(description = "A description for the template.")
    private String description;

    @Schema(description = "Marks if this template should be used as default.")
    private boolean isDefault;
}
