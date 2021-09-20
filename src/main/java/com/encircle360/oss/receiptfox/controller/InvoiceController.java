package com.encircle360.oss.receiptfox.controller;

import com.encircle360.oss.receiptfox.dto.CreateInvoiceRequestDTO;
import com.encircle360.oss.receiptfox.mapping.InvoiceMapper;
import com.encircle360.oss.receiptfox.model.Invoice;
import com.encircle360.oss.receiptfox.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/invoices")
public class InvoiceController {

    private final InvoiceMapper invoiceMapper = InvoiceMapper.INSTANCE;

    private final InvoiceService invoiceService;

    @PostMapping
    public ResponseEntity<?> createInvoice(@RequestBody CreateInvoiceRequestDTO createInvoiceRequestDTO) {
        Invoice invoice = invoiceMapper.createInvoiceRequestDTOToInvoice(createInvoiceRequestDTO);
        try {
            if (invoiceService.save(invoice) == null) {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            log.error("An error occured while invoice creation and saving.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "{id}/download", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> downloadInvoiceDocument(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Invoice invoice = invoiceService.findById(id);

        if (invoice == null) {
            return ResponseEntity.notFound().build();
        }

        response.setHeader("Content-Disposition", "attachment; filename=\"invoice_" + id + ".pdf\"");
        IOUtils.copy(new ByteArrayInputStream(invoice.getDocument()), response.getOutputStream());
        response.flushBuffer();

        return ResponseEntity.ok().build();
    }
}
