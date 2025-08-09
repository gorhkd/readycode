package com.ll.readycode.domain.reviews.service;

import com.ll.readycode.api.reviews.dto.request.ReviewCreateRequest;
import com.ll.readycode.api.reviews.dto.request.ReviewUpdateRequest;
import com.ll.readycode.domain.reviews.entity.Review;
import com.ll.readycode.domain.reviews.repository.ReviewRepository;
import com.ll.readycode.domain.templates.purchases.service.TemplatePurchaseService;
import com.ll.readycode.domain.templates.templates.entity.Template;
import com.ll.readycode.domain.templates.templates.service.TemplateService;
import com.ll.readycode.domain.users.userprofiles.entity.UserProfile;
import com.ll.readycode.global.exception.CustomException;
import com.ll.readycode.global.exception.ErrorCode;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {
  private final ReviewRepository reviewRepository;
  private final TemplateService templateService;
  private final TemplatePurchaseService templatePurchaseService;

  @Transactional
  public Review createReview(
      Long templateId, UserProfile userProfile, ReviewCreateRequest request) {

    // 1. 템플릿 존재 여부 확인
    Template template = templateService.findTemplateById(templateId);

    // 2. 구매자만 리뷰 가능
    templatePurchaseService.validatePurchasedOrThrow(userProfile.getId(), templateId);

    // 3. 중복 리뷰 방지 (한 사용자당 하나의 리뷰만 허용)
    validateNotAlreadyReviewed(userProfile.getId(), templateId);

    Review review =
        Review.builder()
            .template(template)
            .userProfile(userProfile)
            .rating(request.rating())
            .content(request.content())
            .build();

    return reviewRepository.save(review);
  }

  @Transactional
  public Review updateReview(
      Long templateId, UserProfile userProfile, ReviewUpdateRequest request) {
    Review review =
        reviewRepository
            .findByUserProfileIdAndTemplateId(userProfile.getId(), templateId)
            .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

    review.updateReview(request.content(), request.rating());
    return review;
  }

  @Transactional
  public Long deleteReview(Long templateId, UserProfile userProfile) {
    Review review =
        reviewRepository
            .findByUserProfileIdAndTemplateId(userProfile.getId(), templateId)
            .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

    reviewRepository.delete(review);
    return templateId;
  }

  // 리뷰가 있으면 예외
  @Transactional(readOnly = true)
  private void validateNotAlreadyReviewed(Long userId, Long templateId) {
    if (reviewRepository.existsByUserProfileIdAndTemplateId(userId, templateId)) {
      throw new CustomException(ErrorCode.ALREADY_REVIEWED);
    }
  }

  // 리뷰가 없으면 예외
  @Transactional(readOnly = true)
  private void findExistingReviewOrThrow(Long userId, Long templateId) {
    if (!reviewRepository.existsByUserProfileIdAndTemplateId(userId, templateId)) {
      throw new CustomException(ErrorCode.REVIEW_NOT_FOUND);
    }
  }

  @Transactional(readOnly = true)
  public boolean existsByTemplateIdAndUserProfileId(Long templateId, Long userId) {
    return reviewRepository.existsByUserProfileIdAndTemplateId(userId, templateId);
  }

  @Transactional(readOnly = true)
  public Set<Long> findTemplateIdsWithReviewByUser(Long userId, Set<Long> templateIds) {
    return reviewRepository.findTemplateIdsWithReviewByUser(userId, templateIds);
  }
}
