package com.encircle360.oss.receiptfox.client.docsrabbit.dto.template;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Template")
public class TemplateDTO {

    @Schema(description = "Id of the template in database")
    private String id;

    @Schema(description = "Name of the template in database")
    private String name;

    @Schema(description = "Content of the template in database (HTML, plain text, base64 encoded file content, etc.)")
    private String content;

    @Schema(description = "Locale of the template in database")
    private String locale;

    @Schema(description = "List of tags which this template has.")
    private List<String> tags;

    @Schema(description = "Added lastupdate for caching of templates")
    private LocalDateTime lastUpdate;
}
