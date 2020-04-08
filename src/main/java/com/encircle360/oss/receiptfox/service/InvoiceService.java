package com.encircle360.oss.receiptfox.service;

import com.encircle360.oss.receiptfox.model.Invoice;
import com.encircle360.oss.receiptfox.model.InvoiceItem;
import com.encircle360.oss.receiptfox.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public Invoice save(Invoice invoice) {
        this.prepareInvoiceSave(invoice);
        return invoiceRepository.save(invoice);
    }

    private BigDecimal calculateTotalAmount(List<InvoiceItem> items) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (InvoiceItem invoiceItem : items) {
            totalAmount = totalAmount.add(this.getFullPriceForPosition(invoiceItem));
        }

        return totalAmount;
    }

    private BigDecimal calculateTotalNetAmount(List<InvoiceItem> items) {
        BigDecimal netAmount = BigDecimal.ZERO;
        for (InvoiceItem invoiceItem : items) {
            netAmount = netAmount.add(this.getPriceForPosition(invoiceItem));
        }

        return netAmount;
    }

    private BigDecimal calculateTotalVatAmount(List<InvoiceItem> items) {
        BigDecimal vatAmount = BigDecimal.ZERO;
        for (InvoiceItem invoiceItem : items) {
            vatAmount = vatAmount.add(this.getVatForPosition(invoiceItem));
        }

        return vatAmount;
    }

    private BigDecimal getPriceForPosition(InvoiceItem item) {
        return item.getCount().multiply(item.getPrice());
    }

    private BigDecimal getVatForPosition(InvoiceItem item) {
        return item.getCount().multiply(item.getVat());
    }

    private BigDecimal getFullPriceForPosition(InvoiceItem item) {
        return this.getPriceForPosition(item).add(this.getVatForPosition(item));
    }

    private void prepareInvoiceSave(Invoice invoice) {
        if(invoice.getTotalAmount() == null) {
            invoice.setTotalAmount(this.calculateTotalAmount(invoice.getItems()));
        }

        if(invoice.getTotalNetAmount() == null) {
            invoice.setTotalNetAmount(this.calculateTotalNetAmount(invoice.getItems()));
        }

        if(invoice.getTotalVat() == null) {
            invoice.setTotalVat(this.calculateTotalVatAmount(invoice.getItems()));
        }
    }
}
