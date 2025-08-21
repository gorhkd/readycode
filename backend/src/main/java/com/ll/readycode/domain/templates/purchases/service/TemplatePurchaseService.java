package com.ll.readycode.domain.templates.purchases.service;

import com.ll.readycode.api.templates.dto.response.PurchasedTemplateResponse;
import com.ll.readycode.domain.reviews.reader.ReviewReader;
import com.ll.readycode.domain.templates.purchases.entity.TemplatePurchase;
import com.ll.readycode.domain.templates.purchases.repository.TemplatePurchaseRepository;
import com.ll.readycode.domain.templates.templates.entity.Template;
import com.ll.readycode.domain.templates.templates.service.TemplateService;
import com.ll.readycode.domain.users.userprofiles.entity.UserProfile;
import com.ll.readycode.global.exception.CustomException;
import com.ll.readycode.global.exception.ErrorCode;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TemplatePurchaseService {

  private final TemplatePurchaseRepository templatePurchaseRepository;
  private final TemplateService templateService;
  private final ReviewReader reviewReader;

  @Transactional
  public void purchaseFreeTemplate(UserProfile userProfile, Long templateId) {
    if (templatePurchaseRepository.existsByBuyerIdAndTemplateId(userProfile.getId(), templateId)) {
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
  // TODO: 삭제된 템플릿에 대한 정책 정의 필요 (구매 내역에 표시 vs 숨김)
  public List<PurchasedTemplateResponse> getPurchasedTemplates(Long userId) {
    List<TemplatePurchase> purchases = templatePurchaseRepository.findByBuyerIdWithTemplate(userId);

    List<TemplatePurchase> valid = purchases.stream().filter(p -> p.getTemplate() != null).toList();

    if (valid.isEmpty()) {
      return List.of();
    }

    Set<Long> templateIds =
        valid.stream().map(p -> p.getTemplate().getId()).collect(Collectors.toSet());

    Set<Long> reviewedTemplateIds =
        reviewReader.findTemplateIdsWithReviewByUser(userId, templateIds);

    return valid.stream()
        .sorted((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()))
        .map(
            purchase -> {
              Template template = purchase.getTemplate();
              boolean hasReview = reviewedTemplateIds.contains(template.getId());
              return PurchasedTemplateResponse.of(template, hasReview);
            })
        .toList();
  }

  @Transactional(readOnly = true)
  public boolean existsByBuyerIdAndTemplateId(Long buyerId, Long templateId) {
    return templatePurchaseRepository.existsByBuyerIdAndTemplateId(buyerId, templateId);
  }

  @Transactional(readOnly = true)
  public void throwIfNotPurchased(Long userId, Long templateId) {
    boolean purchased = templatePurchaseRepository.existsByBuyerIdAndTemplateId(userId, templateId);
    if (!purchased) {
      throw new CustomException(ErrorCode.FORBIDDEN);
    }
  }
}
