package com.encircle360.oss.receiptfox.mapping.receipt;

import com.encircle360.oss.receiptfox.dto.receipt.ReceiptDTO;
import com.encircle360.oss.receiptfox.dto.receipt.api.CreateUpdateReceiptDTO;
import com.encircle360.oss.receiptfox.model.OrganizationUnit;
import com.encircle360.oss.receiptfox.model.contact.Contact;
import com.encircle360.oss.receiptfox.model.receipt.Receipt;
import com.encircle360.oss.receiptfox.model.receipt.ReceiptFile;
import com.encircle360.oss.receiptfox.model.receipt.ReceiptPosition;
import com.encircle360.oss.receiptfox.model.receipt.ReceiptStatus;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;

@Mapper(
        uses = ReceiptPositionMapper.class,
        builder = @Builder(disableBuilder = true)
)
public interface ReceiptMapper {

    ReceiptMapper INSTANCE = Mappers.getMapper(ReceiptMapper.class);

    @Mapping(target = "contactId", source = "receipt.contact.id")
    @Mapping(target = "receiptFileId", source = "receipt.receiptFile.id")
    @Mapping(target = "organizationUnitId", source = "receipt.organizationUnit.id")
    ReceiptDTO toDto(Receipt receipt);

    List<ReceiptDTO> toDtos(List<Receipt> receipts);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "taxAmount", ignore = true)
    @Mapping(target = "netAmount", ignore = true)
    @Mapping(target = "grossAmount", ignore = true)
    @Mapping(target = "contact", source = "contact")
    @Mapping(target = "positions", source = "positions")
    @Mapping(target = "receiptFile", source = "receiptFile")
    @Mapping(target = "meta", source = "createUpdateReceiptDTO.meta")
    @Mapping(target = "organizationUnit", source = "organizationUnit")
    Receipt createFromDto(CreateUpdateReceiptDTO createUpdateReceiptDTO,
                          List<ReceiptPosition> positions,
                          OrganizationUnit organizationUnit,
                          ReceiptFile receiptFile,
                          Contact contact);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "taxAmount", ignore = true)
    @Mapping(target = "netAmount", ignore = true)
    @Mapping(target = "grossAmount", ignore = true)
    @Mapping(target = "contact", source = "contact")
    @Mapping(target = "positions", source = "positions")
    @Mapping(target = "receiptFile", source = "receiptFile")
    @Mapping(target = "meta", source = "createUpdateReceiptDTO.meta")
    @Mapping(target = "organizationUnit", source = "organizationUnit")
    void updateFromDto(CreateUpdateReceiptDTO createUpdateReceiptDTO,
                       List<ReceiptPosition> positions,
                       OrganizationUnit organizationUnit,
                       ReceiptFile receiptFile,
                       Contact contact,
                       @MappingTarget Receipt receipt);

    @AfterMapping
    default void postProcess(@MappingTarget Receipt receipt) {
        List<ReceiptPosition> positions = receipt.getPositions();
        if (positions == null || positions.isEmpty()) {
            return;
        }

        BigDecimal totalGrossAmount = positions.stream()
            .map(ReceiptPosition::getTotalGrossAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalNetAmount = positions.stream()
            .map(ReceiptPosition::getTotalNetAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalTaxAmount = positions.stream()
            .map(ReceiptPosition::getTotalTaxAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        receipt.setGrossAmount(totalGrossAmount);
        receipt.setNetAmount(totalNetAmount);
        receipt.setTaxAmount(totalTaxAmount);

        if (receipt.getStatus() == null) {
            receipt.setStatus(ReceiptStatus.DRAFT);
        }
    }
}
