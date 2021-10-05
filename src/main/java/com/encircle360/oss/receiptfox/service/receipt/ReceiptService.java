package com.encircle360.oss.receiptfox.service.receipt;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.encircle360.oss.receiptfox.model.receipt.Receipt;
import com.encircle360.oss.receiptfox.repository.receipt.ReceiptRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReceiptService {

    private final ReceiptRepository receiptRepository;

    public Page<Receipt> filter(Pageable pageable, Long organizationUnitId) {
        if (organizationUnitId == null) {
            return receiptRepository.findAll(pageable);
        }
        return receiptRepository.findAllByOrganizationUnitId(organizationUnitId, pageable);
    }

    public Receipt get(Long id) {
        return receiptRepository.findById(id).orElse(null);
    }

    public Receipt save(Receipt receipt) {
        return receiptRepository.save(receipt);
    }

    public void delete(Receipt receipt) {
        receiptRepository.delete(receipt);
    }
}
