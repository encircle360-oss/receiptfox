package com.encircle360.oss.receiptfox.repository;

import com.encircle360.oss.receiptfox.model.Invoice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends MongoRepository<Invoice, String> {
}
