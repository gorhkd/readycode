package com.ll.readycode.domain.reviews.service;

import com.ll.readycode.api.reviews.dto.request.ReviewCreateRequest;
import com.ll.readycode.api.reviews.dto.request.ReviewUpdateRequest;
import com.ll.readycode.api.reviews.dto.response.CursorPage;
import com.ll.readycode.api.reviews.dto.response.ReviewResponse;
import com.ll.readycode.api.reviews.dto.response.ReviewSummaryResponse;
import com.ll.readycode.domain.reviews.entity.Review;
import com.ll.readycode.domain.reviews.query.ReviewSortType;
import com.ll.readycode.domain.reviews.reader.ReviewReader;
import com.ll.readycode.domain.reviews.repository.ReviewRepository;
import com.ll.readycode.domain.templates.purchases.service.TemplatePurchaseService;
import com.ll.readycode.domain.templates.templates.entity.Template;
import com.ll.readycode.domain.templates.templates.service.TemplateService;
import com.ll.readycode.domain.users.userprofiles.entity.UserProfile;
import com.ll.readycode.global.common.types.OrderType;
import com.ll.readycode.global.exception.CustomException;
import com.ll.readycode.global.exception.ErrorCode;
import java.math.BigDecimal;
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
    templatePurchaseService.throwIfNotPurchased(user.getId(), templateId);

    validateNotAlreadyReviewed(user.getId(), templateId);

    Review review =
        Review.builder()
            .template(template)
            .userProfile(user)
            .rating(req.rating())
            .content(req.content())
            .build();

    Review saved = reviewRepository.save(review);

    template.addReview((req.rating()));
    return ReviewResponse.of(saved);
  }

  @Transactional(readOnly = true)
  public CursorPage<ReviewSummaryResponse> getReviewList(
      Long templateId, String cursor, Integer limit, String sort, String order) {
    ReviewSortType reviewSortType = ReviewSortType.from(sort);
    OrderType orderType = OrderType.from(order);
    int pageSize = (limit == null) ? 10 : Math.min(Math.max(limit, 1), 50);

    List<Review> rows =
        reviewReader.findByTemplateWithCursor(
            templateId, cursor, pageSize + 1, reviewSortType, orderType);

    boolean hasNext = rows.size() > pageSize;
    if (hasNext) rows = rows.subList(0, pageSize);

    String nextCursor = hasNext ? encodeCursor(rows.get(rows.size() - 1), reviewSortType) : null;

    List<ReviewSummaryResponse> items = rows.stream().map(ReviewSummaryResponse::of).toList();

    return new CursorPage<>(items, nextCursor, hasNext);
  }

  @Transactional(readOnly = true)
  public ReviewResponse getReview(Long templateId, UserProfile user) {
    Review review = reviewReader.getByUserAndTemplate(user.getId(), templateId);
    return ReviewResponse.of(review); // DTO 변환은 Service에서
  }

  @Transactional
  public ReviewResponse updateReview(Long templateId, UserProfile user, ReviewUpdateRequest req) {
    Review review = reviewReader.getByUserAndTemplate(user.getId(), templateId);
    Template template = templateService.findTemplateById(templateId);
    BigDecimal old = review.getRating();

    review.updateReview(req.content(), req.rating());
    template.updateReview(old, req.rating());
    return ReviewResponse.of(review);
  }

  @Transactional
  public Long deleteReview(Long templateId, UserProfile user) {
    Review review = reviewReader.getByUserAndTemplate(user.getId(), templateId);
    Template template = templateService.findTemplateById(templateId);
    Long deletedId = review.getId();

    template.removeReview(review.getRating());
    reviewRepository.delete(review);
    return deletedId;
  }

  private void validateNotAlreadyReviewed(Long userId, Long templateId) {
    if (reviewReader.existsByUserAndTemplate(userId, templateId)) {
      throw new CustomException(ErrorCode.ALREADY_REVIEWED);
    }
  }

  private String encodeCursor(Review r, ReviewSortType reviewSortType) {
    if (reviewSortType == ReviewSortType.RATING) {
      return r.getRating().toPlainString() + "|" + r.getId();
    }
    return r.getCreatedAt() + "|" + r.getId();
  }
}
