package com.encircle360.oss.receiptfox.client.docsrabbit.dto.render;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "AbstractRenderRequest")
public abstract class AbstractRenderRequestDTO {
    @NotNull
    @Schema(description = "The format in which the template should be rendered")
    private RenderFormatDTO format;

    @Schema(description = "Map with all attributes needed for rendering the template")
    private Object model;

    @Schema(description = "The locale which should be used for rendering, only needed when using filesystem", example = "de")
    private String locale;
}
