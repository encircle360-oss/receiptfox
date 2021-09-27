package com.encircle360.oss.receiptfox.repository.receipt;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.encircle360.oss.receiptfox.model.receipt.Receipt;

@Repository
public interface ReceiptRepository extends CrudRepository<Receipt, Long> {
    Page<Receipt> findAllByOrganizationUnitId(Long organizationUnitId, Pageable pageable);

    Page<Receipt> findAll(Pageable pageable);
}
