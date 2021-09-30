package com.encircle360.oss.receiptfox.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.encircle360.oss.receiptfox.model.OrganizationUnit;
import com.encircle360.oss.receiptfox.model.TemplateMapping;
import com.encircle360.oss.receiptfox.model.receipt.ReceiptType;

@Repository
public interface TemplateMappingRepository extends CrudRepository<TemplateMapping, Long> {
    Page<TemplateMapping> findAll(Pageable pageable);

    Page<TemplateMapping> findAllByOrganizationUnitId(Long organizationUnitId, Pageable pageable);

    Page<TemplateMapping> findAllByType(ReceiptType receiptType, Pageable pageable);

    Page<TemplateMapping> findAllByTypeAndOrganizationUnitId(ReceiptType receiptType, Long organizationUnitId, Pageable pageable);

    TemplateMapping findFirstByStandartAndOrganizationUnitAndType(boolean b, OrganizationUnit organizationUnit, ReceiptType receiptType);
}
