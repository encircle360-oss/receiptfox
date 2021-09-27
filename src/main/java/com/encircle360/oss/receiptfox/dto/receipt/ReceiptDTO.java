package com.encircle360.oss.receiptfox.dto.receipt;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.encircle360.oss.receiptfox.dto.contact.AbstractEntityDTO;
import com.encircle360.oss.receiptfox.dto.contact.AddressDTO;

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
public class ReceiptDTO extends AbstractEntityDTO {

    private ReceiptTypeDTO receiptType;

    private Long organizationUnitId;

    private Map<String, String> meta;

    private Long receiptFileId;

    private AddressDTO senderAddress;

    private AddressDTO receiverAddress;

    private Long contactId;

    private BigDecimal netAmount;

    private BigDecimal grossAmount;

    private BigDecimal taxAmount;

    private List<ReceiptPositionDTO> positions;

    // Templates handled by docs rabbit
    private String templateId;

    private String receiptNumber;

    private ReceiptStatusDTO status;

    private LocalDate receiptDate;

    private LocalDate deliveryDate;

    private LocalDate benefitPeriodStart;

    private LocalDate benefitPeriodEnd;
}
