package com.encircle360.oss.receiptfox.dto.receipt;

import java.util.Map;

import com.encircle360.oss.receiptfox.dto.contact.AbstractEntityDTO;

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
public class ReceiptFileDTO extends AbstractEntityDTO {

    private String name;

    private String ocr;

    private Map<String, String> meta;

}
