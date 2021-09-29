package com.encircle360.oss.receiptfox.mapping.receipt;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.encircle360.oss.receiptfox.dto.receipt.ReceiptEventDTO;
import com.encircle360.oss.receiptfox.model.receipt.ReceiptEvent;

@Mapper
public interface ReceiptEventMapper {

    ReceiptEventMapper INSTANCE = Mappers.getMapper(ReceiptEventMapper.class);

    ReceiptEvent fromDto(ReceiptEventDTO receiptEventDTO);

}
