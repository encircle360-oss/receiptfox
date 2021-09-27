package com.encircle360.oss.receiptfox.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.encircle360.oss.receiptfox.model.TaxRate;

@Repository
public interface TaxRateRepository extends CrudRepository<TaxRate, Long> {
    Page<TaxRate> findAll(Pageable pageable);
}
