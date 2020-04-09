package com.encircle360.oss.receiptfox.controller;

import com.encircle360.oss.receiptfox.dto.CreateInvoiceRequestDTO;
import com.encircle360.oss.receiptfox.mapping.InvoiceMapper;
import com.encircle360.oss.receiptfox.model.Invoice;
import com.encircle360.oss.receiptfox.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/invoices")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InvoiceController {

    private static InvoiceMapper invoiceMapper = InvoiceMapper.INSTANCE;
    private final InvoiceService invoiceService;

    @PostMapping
    public ResponseEntity<?> createInvoice(@RequestBody CreateInvoiceRequestDTO createInvoiceRequestDTO) {
        Invoice invoice = invoiceMapper.createInvoiceRequestDTOToInvoice(createInvoiceRequestDTO);
        if (invoiceService.save(invoice) == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
