package com.encircle360.oss.receiptfox.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.encircle360.oss.receiptfox.model.TaxRate;
import com.encircle360.oss.receiptfox.repository.TaxRateRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaxRateService {

    private final TaxRateRepository taxRateRepository;

    public Page<TaxRate> findAll(Pageable pageable) {
        return taxRateRepository.findAll(pageable);
    }

    public TaxRate get(Long id) {
        return taxRateRepository.findById(id).orElse(null);
    }

    public TaxRate save(TaxRate taxRate) {
        return taxRateRepository.save(taxRate);
    }

    public void delete(TaxRate taxRate) {
        taxRateRepository.delete(taxRate);
    }
}

