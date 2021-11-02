package com.encircle360.oss.receiptfox.dto.receipt;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.encircle360.oss.receiptfox.dto.contact.AbstractEntityDTO;
import com.encircle360.oss.receiptfox.dto.contact.AddressDTO;
import com.encircle360.oss.receiptfox.model.contact.Address;

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
@Schema(name = "Receipt", description = "Dto for a receipt")
public class ReceiptDTO extends AbstractEntityDTO {

    @Schema(description = "Type of a receipt.", example = "INVOICE")
    private ReceiptTypeDTO receiptType;

    @Schema(description = "The id of the related organization unit.", example = "20")
    private Long organizationUnitId;

    @Schema(description = "Meta information for this receipt.")
    private Map<String, String> meta;

    @Schema(description = "The id of the receipt file, only existent if created.")
    private Long receiptFileId;

    @Schema(description = "The address of the sender.")
    private AddressDTO senderAddress;

    @Schema(description = "The address of the receiver.")
    private AddressDTO receiverAddress;

    @Schema(description = "The address of the benefit receiver.")
    private Address benefitReceiverAddress;

    @Schema(description = "Id of the contact, this receipt belongs to.")
    private Long contactId;

    @Schema(description = "Total amount of all invoice positions without tax.")
    private BigDecimal netAmount;

    @Schema(description = "Total amount of all invoice positions with tax.")
    private BigDecimal grossAmount;

    @Schema(description = "Total amount of all invoice positions tax amounts.")
    private BigDecimal taxAmount;

    @Schema(description = "List of all positions in this invoice.")
    private List<ReceiptPositionDTO> positions;

    @Schema(description = "A docsrabbit template id, which is related to this invoice.")
    private String templateId;

    @Schema(description = "The number of this receipt, will be used for legal purpose.", example = "20210100001")
    private String receiptNumber;

    @Schema(description = "Status of the receipt", example = "DRAFT")
    private ReceiptStatusDTO status;

    @Schema(description = "Date of the receipt.")
    private LocalDate receiptDate;

    @Schema(description = "The delivery date, means the delivery date of the receipt positions.")
    private LocalDate deliveryDate;

    @Schema(description = "The benefit period start, means the start date of the billed service.")
    private LocalDate benefitPeriodStart;

    @Schema(description = "The benefit period end, means the end date of the billed service.")
    private LocalDate benefitPeriodEnd;
}
