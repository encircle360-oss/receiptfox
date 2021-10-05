package com.encircle360.oss.receiptfox.client.docsrabbit.dto.template;

import java.util.List;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CreateUpdateTemplate", description = "Data for creating a template")
public class CreateUpdateTemplateDTO {

    @NotBlank
    @Schema(description = "Name of the template in database")
    private String name;

    @NotBlank
    @Schema(description = "Content of the template in database (HTML, plain text, base64 encoded file content, etc.)")
    private String content;

    @Schema(description = "Locale of the template in database")
    private String locale;

    @Schema(description = "List of tags which this template has.")
    private List<String> tags;

}
