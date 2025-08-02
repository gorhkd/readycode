package com.ll.readycode.domain.templates.purchases.repository;

import com.ll.readycode.domain.templates.purchases.entity.TemplatePurchase;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemplatePurchaseRepository extends JpaRepository<TemplatePurchase, Long> {

  boolean existsByBuyerIdAndTemplateId(Long buyerId, Long templateId);

  List<TemplatePurchase> findByBuyerId(Long buyerId);
}
