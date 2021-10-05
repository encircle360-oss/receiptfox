package com.encircle360.oss.receiptfox.client.docsrabbit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "OCRResult", description = "Contains the OCR scan results.")
public class OCRResultDTO {

    @Schema(description = "The content of the scanned result.")
    private String content;

}
