package com.encircle360.oss.receiptfox.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.encircle360.oss.receiptfox.model.TemplateMapping;

@Repository
public interface TemplateMappingRepository extends CrudRepository<TemplateMapping, Long> {
}
