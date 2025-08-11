package com.ll.readycode.api.reviews.dto.response;

import com.ll.readycode.domain.reviews.entity.Review;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

public class ReviewResponse {

  private Long reviewId;
  private Long templateId;
  private Long userProfileId;
  private String authorNickname;
  private BigDecimal rating;
  private String content;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static ReviewResponse of(Review review) {
    ReviewResponse dto = new ReviewResponse();
    dto.reviewId = review.getId();
    dto.templateId = review.getTemplate().getId();
    dto.userProfileId = review.getUserProfile().getId();
    dto.authorNickname = review.getUserProfile().getNickname();
    dto.rating =
        review.getRating() == null
            ? BigDecimal.ZERO.setScale(1, RoundingMode.DOWN)
            : review.getRating().setScale(1, RoundingMode.DOWN);
    dto.content = review.getContent();
    dto.createdAt = review.getCreatedAt();
    dto.updatedAt = review.getUpdatedAt();
    return dto;
  }
}
