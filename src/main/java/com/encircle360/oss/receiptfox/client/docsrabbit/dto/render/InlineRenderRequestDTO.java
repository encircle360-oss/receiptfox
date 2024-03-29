package com.encircle360.oss.receiptfox.client.docsrabbit.dto.render;

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
@Schema(name = "InlineRenderRequest", description = "Request dto for rendering a template which is submitted inline")
public class InlineRenderRequestDTO extends AbstractRenderRequestDTO {

    @Schema(description = "The contents of this template")
    private String template;
}
