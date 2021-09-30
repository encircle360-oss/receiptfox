package com.encircle360.oss.receiptfox.dto;

import com.encircle360.oss.receiptfox.dto.contact.AbstractEntityDTO;
import com.encircle360.oss.receiptfox.dto.receipt.ReceiptTypeDTO;

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
@Schema(name = "TemplateMapping", description = "A template mapped to an organization unit with type.")
public class TemplateMappingDTO extends AbstractEntityDTO {

    @Schema(description = "The docsrabbit id of the template.")
    private String templateId;

    @Schema(description = "The receipt type this template id should be applied.")
    private ReceiptTypeDTO type;

    @Schema(description = "The id of the related organization unit.")
    private Long organizationUnitId;

    @Schema(description = "A description for the template.")
    private String description;

    @Schema(description = "Marks if this template should be used as default.")
    private boolean standart;
}
