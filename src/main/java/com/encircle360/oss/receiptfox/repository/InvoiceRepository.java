package com.encircle360.oss.receiptfox.repository;

import com.encircle360.oss.receiptfox.model.Invoice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends CrudRepository<Invoice, Long> {
}
