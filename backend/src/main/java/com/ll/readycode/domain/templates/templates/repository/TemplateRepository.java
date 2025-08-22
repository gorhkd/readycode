package com.ll.readycode.domain.templates.templates.repository;

import com.ll.readycode.domain.templates.templates.entity.Template;
import java.util.Optional;
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

  @Query("SELECT t FROM Template t JOIN FETCH t.category WHERE t.id = :templateId")
  Optional<Template> findByIdWithCategory(@Param("templateId") Long templateId);
}
