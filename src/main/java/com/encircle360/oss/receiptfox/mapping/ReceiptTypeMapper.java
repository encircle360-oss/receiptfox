package com.encircle360.oss.receiptfox.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.encircle360.oss.receiptfox.dto.receipt.ReceiptTypeDTO;
import com.encircle360.oss.receiptfox.model.receipt.ReceiptType;

@Mapper
public interface ReceiptTypeMapper {

    ReceiptTypeMapper INSTANCE = Mappers.getMapper(ReceiptTypeMapper.class);

    ReceiptType fromDto(ReceiptTypeDTO dto);
}
