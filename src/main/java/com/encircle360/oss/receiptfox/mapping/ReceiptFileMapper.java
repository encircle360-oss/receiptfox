package com.encircle360.oss.receiptfox.mapping;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.encircle360.oss.receiptfox.dto.receipt.ReceiptFileDTO;
import com.encircle360.oss.receiptfox.model.receipt.ReceiptFile;

@Mapper
public interface ReceiptFileMapper {

    ReceiptFileMapper INSTANCE = Mappers.getMapper(ReceiptFileMapper.class);

    ReceiptFileDTO toDto(ReceiptFile receiptFile);

    List<ReceiptFileDTO> toDtos(List<ReceiptFile> receiptFiles);

}
