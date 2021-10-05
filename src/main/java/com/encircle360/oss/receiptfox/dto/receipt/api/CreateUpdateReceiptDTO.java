package com.encircle360.oss.receiptfox.dto.receipt.api;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.encircle360.oss.receiptfox.dto.contact.AddressDTO;
import com.encircle360.oss.receiptfox.dto.receipt.ReceiptTypeDTO;
import com.encircle360.oss.receiptfox.validation.receipt.ValidReceipt;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@ValidReceipt
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CreateUpdateReceipt", description = "DTO for creating a receipt")
public class CreateUpdateReceiptDTO {

    @NotNull
    @Schema(description = "Type of a receipt.", example = "INVOICE")
    private ReceiptTypeDTO receiptType;

    @NotNull
    @Schema(description = "The id of the related organization unit.", example = "20")
    private Long organizationUnitId;

    @Schema(description = "Meta information for this receipt.")
    private Map<String, String> meta;

    @Schema(description = "The id of the receipt file, only existent if created.")
    private Long receiptFileId;

    @NotNull
    @Schema(description = "The address of the sender.")
    private @Valid AddressDTO senderAddress;

    @NotNull
    @Schema(description = "The address of the receiver.")
    private @Valid AddressDTO receiverAddress;

    @Schema(description = "Id of the contact, this receipt belongs to.")
    private Long contactId;

    @NotNull
    @Schema(description = "List of all positions in this invoice.")
    private List<@Valid CreateUpdateReceiptPositionDTO> positions;

    @Schema(description = "A docsrabbit template id, which is related to this invoice.")
    private String templateId;

    @NotNull
    @Schema(description = "The number of this receipt, will be used for legal purpose.", example = "20210100001")
    private String receiptNumber;

    @NotNull
    @Schema(description = "Date of the receipt.")
    private LocalDate receiptDate;

    @Schema(description = "The delivery date, means the delivery date of the receipt positions.")
    private LocalDate deliveryDate;

    @Schema(description = "The benefit period start, means the start date of the billed service.")
    private LocalDate benefitPeriodStart;

    @Schema(description = "The benefit period end, means the end date of the billed service.")
    private LocalDate benefitPeriodEnd;
}
