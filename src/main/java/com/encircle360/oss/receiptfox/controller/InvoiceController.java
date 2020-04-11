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
@RequestMapping("/invoices")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InvoiceController {

    private static InvoiceMapper invoiceMapper = InvoiceMapper.INSTANCE;
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

    @GetMapping("{id}/download")
    public ResponseEntity<?> downloadInvoiceDocument(@PathVariable String id, HttpServletResponse response) throws IOException {
        Optional<Invoice> invoiceOptional = invoiceService.findById(id);

        if (invoiceOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader("Content-Disposition", "attachment; filename=\"invoice_" + id + ".pdf\"");
        IOUtils.copy(new ByteArrayInputStream(invoiceOptional.get().getDocument()), response.getOutputStream());
        response.flushBuffer();

        return ResponseEntity.ok().build();
    }
}
