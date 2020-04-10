package com.encircle360.oss.receiptfox.service;

import com.encircle360.oss.receiptfox.model.Invoice;
import com.encircle360.oss.receiptfox.model.InvoiceItem;
import com.encircle360.oss.receiptfox.repository.InvoiceRepository;
import com.github.jhonnymertz.wkhtmltopdf.wrapper.Pdf;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private static final String TMP_INVOICE_DIR = "/tmp/invoices/";
    private static final String INVOICE_TEMPLATE = "invoice.ftl";
    private final Configuration freemarkerConfig;

    public Invoice save(Invoice invoice) throws IOException, InterruptedException, TemplateException {
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

    private void prepareInvoiceSave(Invoice invoice) throws IOException, InterruptedException, TemplateException {
        if (invoice.getTotalAmount() == null) {
            invoice.setTotalAmount(this.calculateTotalAmount(invoice.getItems()));
        }

        if (invoice.getTotalNetAmount() == null) {
            invoice.setTotalNetAmount(this.calculateTotalNetAmount(invoice.getItems()));
        }

        if (invoice.getTotalVat() == null) {
            invoice.setTotalVat(this.calculateTotalVatAmount(invoice.getItems()));
        }

        if (invoice.getDocument() == null) {
            invoice.setDocument(this.generateInvoiceDocument(invoice));
        }
    }

    private byte[] generateInvoiceDocument(Invoice invoice) throws IOException, InterruptedException, TemplateException {
        Pdf pdf = new Pdf();
        pdf.addPageFromString(this.renderInvoiceDocumentHTML(invoice));
        pdf.addToc();
        File invoiceDocumentFile = pdf.saveAsDirect(TMP_INVOICE_DIR + UUID.randomUUID() + ".pdf");
        byte[] document = Files.readAllBytes(invoiceDocumentFile.toPath());
        invoiceDocumentFile.delete();

        return document;
    }

    private String renderInvoiceDocumentHTML(Invoice invoice) throws IOException, TemplateException {
        Template template = freemarkerConfig.getTemplate(INVOICE_TEMPLATE);
        Map<String, Object> model = new HashMap<>();
        model.put("invoice", invoice);
        return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
    }

    @PostConstruct
    public void ensureDirectories() {
        new File(TMP_INVOICE_DIR).mkdir();
    }
}
