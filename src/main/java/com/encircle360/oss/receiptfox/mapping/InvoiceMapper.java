package com.encircle360.oss.receiptfox.mapping;

import com.encircle360.oss.receiptfox.dto.CreateInvoiceRequestDTO;
import com.encircle360.oss.receiptfox.model.Invoice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface InvoiceMapper {

    InvoiceMapper INSTANCE = Mappers.getMapper(InvoiceMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "document", ignore = true)
    Invoice createInvoiceRequestDTOToInvoice(CreateInvoiceRequestDTO createInvoiceRequestDTO);
}
