package com.encircle360.oss.receiptfox.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.encircle360.oss.receiptfox.model.TemplateMapping;
import com.encircle360.oss.receiptfox.model.receipt.ReceiptType;
import com.encircle360.oss.receiptfox.repository.TemplateMappingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TemplateMappingService {

    private final TemplateMappingRepository templateMappingRepository;

    public Page<TemplateMapping> filter(Long organizationUnitId, ReceiptType receiptType, Pageable pageable) {
        if (organizationUnitId == null && receiptType == null) {
            return templateMappingRepository.findAll(pageable);
        }

        if (organizationUnitId == null) {
            return templateMappingRepository.findAllByType(receiptType, pageable);
        }

        if (receiptType == null) {
            return templateMappingRepository.findAllByOrganizationUnitId(organizationUnitId, pageable);
        }
        return templateMappingRepository.findAllByTypeAndOrganizationUnitId(receiptType, organizationUnitId, pageable);
    }

    public TemplateMapping get(Long id) {
        return templateMappingRepository.findById(id).orElse(null);
    }

    public TemplateMapping save(TemplateMapping templateMapping) {
        return templateMappingRepository.save(templateMapping);
    }

    public void delete(TemplateMapping templateMapping) {
        templateMappingRepository.delete(templateMapping);
    }
}
