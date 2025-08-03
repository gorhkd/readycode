package com.ll.readycode.domain.templates.purchases.repository;

import com.ll.readycode.domain.templates.purchases.entity.TemplatePurchase;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TemplatePurchaseRepository extends JpaRepository<TemplatePurchase, Long> {

  boolean existsByBuyerIdAndTemplateId(Long buyerId, Long templateId);

  @Query(
"""
    SELECT tp FROM TemplatePurchase tp
    JOIN FETCH tp.template
    WHERE tp.buyer.id = :buyerId
    ORDER BY tp.createdAt DESC
""")
  List<TemplatePurchase> findByBuyerIdWithTemplate(@Param("buyerId") Long buyerId);
}
