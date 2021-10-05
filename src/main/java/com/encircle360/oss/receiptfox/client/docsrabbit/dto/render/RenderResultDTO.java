package com.encircle360.oss.receiptfox.client.docsrabbit.dto.render;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "RenderResult", description = "Holds the rendering results in base64 format and has additional information")
public class RenderResultDTO {

    @Schema(description = "Database ID of the template used", example = "")
    private String templateId;

    @Schema(description = "MimeType of content", example = "application/pdf")
    private String mimeType;

    @Schema(description = "Content length", example = "1024")
    private long contentLength;

    @Schema(description = "base64 encoded content as string", example = "==0")
    private String base64;

}
