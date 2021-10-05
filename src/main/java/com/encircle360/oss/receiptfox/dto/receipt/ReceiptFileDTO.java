package com.encircle360.oss.receiptfox.dto.receipt;

import java.util.Map;

import com.encircle360.oss.receiptfox.dto.contact.AbstractEntityDTO;

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
@Schema(name = "ReceiptFile", description = "Information for the PDF file of a receipt.")
public class ReceiptFileDTO extends AbstractEntityDTO {

    @Schema(description = "The name of this file.", example = "RE2021010001.pdf")
    private String name;

    @Schema(description = "The ocr contents of this file.")
    private String ocr;

    @Schema(description = "Meta information for the file.")
    private Map<String, String> meta;

}
