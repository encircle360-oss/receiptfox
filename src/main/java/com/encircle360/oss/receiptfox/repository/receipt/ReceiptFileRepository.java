package com.encircle360.oss.receiptfox.repository.receipt;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.encircle360.oss.receiptfox.model.receipt.ReceiptFile;

@Repository
public interface ReceiptFileRepository extends CrudRepository<ReceiptFile, Long> {
}
