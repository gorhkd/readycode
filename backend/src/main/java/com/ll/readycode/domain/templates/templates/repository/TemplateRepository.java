package com.ll.readycode.domain.templates.templates.repository;

import com.ll.readycode.domain.templates.templates.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TemplateRepository
    extends JpaRepository<Template, Long>, TemplateRepositoryCustom {
  @Modifying
  @Transactional
  @Query("UPDATE Template t SET t.purchaseCount = t.purchaseCount + 1 WHERE t.id = :id")
  int incrementPurchaseCount(@Param("id") Long templateId);
}
