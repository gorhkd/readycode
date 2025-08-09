package com.ll.readycode.domain.reviews.service;

import com.ll.readycode.api.reviews.dto.request.ReviewCreateRequest;
import com.ll.readycode.api.reviews.dto.request.ReviewUpdateRequest;
import com.ll.readycode.api.reviews.dto.response.ReviewResponse;
import com.ll.readycode.api.reviews.dto.response.ReviewSummaryResponse;
import com.ll.readycode.domain.reviews.entity.Review;
import com.ll.readycode.domain.reviews.reader.ReviewReader;
import com.ll.readycode.domain.reviews.repository.ReviewRepository;
import com.ll.readycode.domain.templates.purchases.service.TemplatePurchaseService;
import com.ll.readycode.domain.templates.templates.entity.Template;
import com.ll.readycode.domain.templates.templates.service.TemplateService;
import com.ll.readycode.domain.users.userprofiles.entity.UserProfile;
import com.ll.readycode.global.exception.CustomException;
import com.ll.readycode.global.exception.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

  private final ReviewRepository reviewRepository;
  private final ReviewReader reviewReader;
  private final TemplateService templateService;
  private final TemplatePurchaseService templatePurchaseService;

  @Transactional
  public ReviewResponse createReview(Long templateId, UserProfile user, ReviewCreateRequest req) {
    Template template = templateService.findTemplateById(templateId);
    templatePurchaseService.validatePurchasedOrThrow(user.getId(), templateId);

    validateNotAlreadyReviewed(user.getId(), templateId);

    Review review =
        Review.builder()
            .template(template)
            .userProfile(user)
            .rating(req.rating())
            .content(req.content())
            .build();

    Review saved = reviewRepository.save(review);
    return ReviewResponse.of(saved);
  }

  @Transactional(readOnly = true)
  public ReviewResponse getReview(Long templateId, UserProfile user) {
    Review review = reviewReader.getByUserAndTemplate(user.getId(), templateId);
    return ReviewResponse.of(review); // DTO 변환은 Service에서
  }

  @Transactional
  public ReviewResponse updateReview(Long templateId, UserProfile user, ReviewUpdateRequest req) {
    Review review = reviewReader.getByUserAndTemplate(user.getId(), templateId);
    review.updateReview(req.content(), req.rating());
    return ReviewResponse.of(review);
  }

  @Transactional
  public Long deleteReview(Long templateId, UserProfile user) {
    Review review = reviewReader.getByUserAndTemplate(user.getId(), templateId);
    Long deletedId = review.getId();
    reviewRepository.delete(review);
    return deletedId;
  }

  @Transactional(readOnly = true)
  public List<ReviewSummaryResponse> getReviewList(Long templateId) {
    List<Review> reviews = reviewReader.findByTemplate(templateId);
    return reviews.stream().map(ReviewSummaryResponse::of).toList();
  }

  private void validateNotAlreadyReviewed(Long userId, Long templateId) {
    if (reviewReader.existsByUserAndTemplate(userId, templateId)) {
      throw new CustomException(ErrorCode.ALREADY_REVIEWED);
    }
  }
}
