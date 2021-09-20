package com.encircle360.oss.receiptfox.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.encircle360.oss.receiptfox.model.OrganizationUnit;
import com.encircle360.oss.receiptfox.repository.OrganizationUnitRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrganizationUnitService {

    private OrganizationUnitRepository organizationUnitRepository;

    public Page<OrganizationUnit> getAll(Pageable pageable) {
        return organizationUnitRepository.findAll(pageable);
    }

    public OrganizationUnit get(Long id) {
        if(id == null) {
            return null;
        }
        return organizationUnitRepository.findById(id).orElse(null);
    }

    public OrganizationUnit save(OrganizationUnit organizationUnit) {
        return organizationUnitRepository.save(organizationUnit);
    }
}
