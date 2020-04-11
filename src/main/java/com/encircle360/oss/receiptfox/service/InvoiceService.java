package com.encircle360.oss.receiptfox.service;

import com.encircle360.oss.receiptfox.model.Invoice;
import com.encircle360.oss.receiptfox.model.InvoiceItem;
import com.encircle360.oss.receiptfox.repository.InvoiceRepository;
import com.github.jhonnymertz.wkhtmltopdf.wrapper.Pdf;
import com.github.jhonnymertz.wkhtmltopdf.wrapper.configurations.WrapperConfig;
import com.github.jhonnymertz.wkhtmltopdf.wrapper.configurations.XvfbConfig;
import com.github.jhonnymertz.wkhtmltopdf.wrapper.params.Param;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private static final String TMP_INVOICE_DIR = "/tmp/invoices/";
    private static final String INVOICE_TEMPLATE = "invoice.ftl";
    private final Configuration freemarkerConfig;

    public Optional<Invoice> findById(String id) {
        return invoiceRepository.findById(id);
    }

    public Invoice save(Invoice invoice) throws IOException, InterruptedException, TemplateException {
        this.prepareInvoiceSave(invoice);
        return invoiceRepository.save(invoice);
    }

    private BigDecimal calculateTotalPrice(List<InvoiceItem> items) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (InvoiceItem invoiceItem : items) {
            totalAmount = totalAmount.add(invoiceItem.getTotalPrice());
        }

        return totalAmount;
    }

    private BigDecimal calculateTotalNetPrice(List<InvoiceItem> items) {
        BigDecimal netAmount = BigDecimal.ZERO;
        for (InvoiceItem invoiceItem : items) {
            netAmount = netAmount.add(invoiceItem.getTotalNetPrice());
        }

        return netAmount;
    }

    private BigDecimal calculateTotalVat(List<InvoiceItem> items) {
        BigDecimal vatAmount = BigDecimal.ZERO;
        for (InvoiceItem invoiceItem : items) {
            vatAmount = vatAmount.add(invoiceItem.getTotalVat());
        }

        return vatAmount;
    }

    private void prepareInvoiceSave(Invoice invoice) throws IOException, InterruptedException, TemplateException {
        if (invoice.isReverseCharge()) {
            if (invoice.getReceiver().getVatId() == null || invoice.getReceiver().getVatId().isEmpty()) {
                log.error("VAT ID is missing or empty for invoice with reference {}! This doesn't apply with the Reverse Charge regulations.", invoice.getReference());
            }
        }

        // process invoice item calculations
        for (InvoiceItem item : invoice.getItems()) {
            if (item.getTotalNetPrice() == null) {
                item.setTotalNetPrice(item.getNetPrice().multiply(item.getCount()));
            }

            if (item.getTotalVat() == null) {
                item.setTotalVat(item.getVat().multiply(item.getCount()));
            }

            if (item.getTotalPrice() == null) {
                item.setTotalPrice(item.getTotalNetPrice().add(item.getTotalVat()));
            }
        }

        // process invoice calculations
        if (invoice.getTotalPrice() == null) {
            invoice.setTotalPrice(this.calculateTotalPrice(invoice.getItems()));
        }

        if (invoice.getTotalNetPrice() == null) {
            invoice.setTotalNetPrice(this.calculateTotalNetPrice(invoice.getItems()));
        }

        if (invoice.getTotalVat() == null) {
            invoice.setTotalVat(this.calculateTotalVat(invoice.getItems()));
        }

        if (invoice.getDocument() == null) {
            invoice.setDocument(this.generateInvoiceDocument(invoice));
        }
    }

    public static Boolean isRunningInsideDocker() {
        try (Stream<String> stream =
                     Files.lines(Paths.get("/proc/1/cgroup"))) {
            return stream.anyMatch(line -> line.contains("/docker"));
        } catch (IOException e) {
            return false;
        }
    }

    private byte[] generateInvoiceDocument(Invoice invoice) throws IOException, InterruptedException, TemplateException {
        Pdf pdf = new Pdf(this.getWkhtmlToPdfWrapperConfig());
        pdf.addPageFromString(this.renderInvoiceDocumentHTML(invoice));
        pdf.addToc();
        File invoiceDocumentFile = pdf.saveAsDirect(TMP_INVOICE_DIR + UUID.randomUUID() + ".pdf");
        byte[] document = Files.readAllBytes(invoiceDocumentFile.toPath());
        invoiceDocumentFile.delete();

        return document;
    }

    private WrapperConfig getWkhtmlToPdfWrapperConfig() {
        XvfbConfig xc = new XvfbConfig();
        xc.addParams(new Param("--auto-servernum"));

        WrapperConfig wc = new WrapperConfig();

        if (isRunningInsideDocker()) {
            wc.setXvfbConfig(xc);
        }

        return wc;
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
