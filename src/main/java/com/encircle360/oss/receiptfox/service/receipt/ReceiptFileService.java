package com.encircle360.oss.receiptfox.service.receipt;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.encircle360.oss.receiptfox.model.receipt.ReceiptFile;
import com.encircle360.oss.receiptfox.repository.receipt.ReceiptFileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReceiptFileService {

    private final ReceiptFileRepository receiptFileRepository;

    public ReceiptFile get(final Long id) {
        return receiptFileRepository.findById(id).orElse(null);
    }
}
