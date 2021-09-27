package com.encircle360.oss.receiptfox.dto.receipt.api;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
    private ReceiptTypeDTO receiptType;

    @NotNull
    private Long organizationUnitId;

    private Map<String, String> meta;

    private Long receiptFileId;

    @NotNull
    private AddressDTO senderAddress;

    @NotNull
    private AddressDTO receiverAddress;

    private Long contactId;

    @NotNull
    private List<CreateUpdateReceiptPositionDTO> positions;

    // Templates handled by docs rabbit
    private String templateId;

    @NotNull
    private String receiptNumber;

    @NotNull
    private LocalDate receiptDate;

    private LocalDate deliveryDate;

    private LocalDate benefitPeriodStart;

    private LocalDate benefitPeriodEnd;
}
