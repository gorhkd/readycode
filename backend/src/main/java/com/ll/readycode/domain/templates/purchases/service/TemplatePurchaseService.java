package com.ll.readycode.domain.templates.purchases.service;

import com.ll.readycode.api.templates.dto.response.PurchasedTemplateResponse;
import com.ll.readycode.domain.templates.purchases.entity.TemplatePurchase;
import com.ll.readycode.domain.templates.purchases.repository.TemplatePurchaseRepository;
import com.ll.readycode.domain.templates.templates.entity.Template;
import com.ll.readycode.domain.templates.templates.service.TemplateService;
import com.ll.readycode.domain.users.userprofiles.entity.UserProfile;
import com.ll.readycode.global.exception.CustomException;
import com.ll.readycode.global.exception.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TemplatePurchaseService {

  private final TemplatePurchaseRepository templatePurchaseRepository;
  private final TemplateService templateService;

  @Transactional
  public void purchaseFreeTemplate(UserProfile userProfile, Long templateId) {
    boolean alreadyPurchased =
        templatePurchaseRepository.existsByBuyerIdAndTemplateId(userProfile.getId(), templateId);
    if (alreadyPurchased) {
      throw new CustomException(ErrorCode.ALREADY_PURCHASED);
    }

    Template template = templateService.findTemplateById(templateId);

    if (template.getPrice() > 0) {
      throw new CustomException(ErrorCode.NOT_FREE_TEMPLATE);
    }

    TemplatePurchase templatePurchase =
        TemplatePurchase.builder()
            .buyer(userProfile)
            .template(template)
            .price(template.getPrice())
            .build();

    try {
      templatePurchaseRepository.save(templatePurchase);
    } catch (DataIntegrityViolationException e) {
      throw new CustomException(ErrorCode.ALREADY_PURCHASED);
    }
  }

  @Transactional(readOnly = true)
  public List<PurchasedTemplateResponse> getPurchasedTemplates(Long userId) {
    List<TemplatePurchase> purchases = templatePurchaseRepository.findByBuyerIdWithTemplate(userId);

    return purchases.stream()
        .sorted((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()))
        .map(purchase -> PurchasedTemplateResponse.of(purchase.getTemplate()))
        .toList();
  }
}
