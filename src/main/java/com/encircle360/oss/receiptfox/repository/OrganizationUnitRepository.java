package com.encircle360.oss.receiptfox.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.encircle360.oss.receiptfox.model.OrganizationUnit;

public interface OrganizationUnitRepository extends CrudRepository<OrganizationUnit, Long> {
    Page<OrganizationUnit> findAll(Pageable pageable);
}
