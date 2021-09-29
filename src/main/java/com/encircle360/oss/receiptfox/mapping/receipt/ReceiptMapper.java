package com.encircle360.oss.receiptfox.mapping.receipt;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.encircle360.oss.receiptfox.dto.receipt.ReceiptDTO;
import com.encircle360.oss.receiptfox.dto.receipt.api.CreateUpdateReceiptDTO;
import com.encircle360.oss.receiptfox.model.OrganizationUnit;
import com.encircle360.oss.receiptfox.model.contact.Contact;
import com.encircle360.oss.receiptfox.model.receipt.Receipt;
import com.encircle360.oss.receiptfox.model.receipt.ReceiptFile;
import com.encircle360.oss.receiptfox.model.receipt.ReceiptPosition;
import com.encircle360.oss.receiptfox.model.receipt.ReceiptStatus;

@Mapper(uses = ReceiptPositionMapper.class)
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

        for (ReceiptPosition position : positions) {
            BigDecimal singleNetAmount = position.getSingleNetAmount();
            BigDecimal singleGrossAmount = position.getSingleGrossAmount();
            BigDecimal taxMultiplier = BigDecimal.ONE.add(position.getTaxRatePercent());

            // Both values are null should not be possible
            // only for avoiding null pointers
            if (singleNetAmount == null && singleGrossAmount == null) {
                continue;
            }

            if (singleNetAmount != null) {
                singleGrossAmount = singleNetAmount.multiply(taxMultiplier);
                position.setSingleGrossAmount(singleGrossAmount);
            } else {
                singleNetAmount = singleGrossAmount.divide(taxMultiplier, 20, RoundingMode.HALF_UP);
                position.setSingleNetAmount(singleNetAmount);
            }

            BigDecimal quantity = BigDecimal.valueOf(position.getQuantity());

            position.setSingleTaxAmount(singleGrossAmount.subtract(singleNetAmount));
            position.setTotalNetAmount(singleNetAmount.multiply(quantity));
            position.setTotalGrossAmount(singleGrossAmount.multiply(quantity));
            position.setTotalTaxAmount(position.getTotalGrossAmount().subtract(position.getTotalNetAmount()));
        }

        receipt.setPositions(positions);

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
