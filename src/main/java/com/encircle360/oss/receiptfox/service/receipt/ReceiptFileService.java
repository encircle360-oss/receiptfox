package com.encircle360.oss.receiptfox.service.receipt;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        if(id == null) {
            return null;
        }
        return receiptFileRepository.findById(id).orElse(null);
    }

    public Page<ReceiptFile> findAll(Pageable pageable) {
        return receiptFileRepository.findAll(pageable);
    }

    public ReceiptFile save(ReceiptFile receiptFile) {
        return receiptFileRepository.save(receiptFile);
    }
}
