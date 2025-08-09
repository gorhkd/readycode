package com.ll.readycode.api.reviews.dto.response;

import com.ll.readycode.domain.reviews.entity.Review;
import java.time.LocalDateTime;

public class ReviewSummaryResponse {

  private Long id;
  private String authorNickname;
  private Double rating;
  private String content;
  private LocalDateTime createdAt;

  public static ReviewSummaryResponse of(Review review) {
    ReviewSummaryResponse dto = new ReviewSummaryResponse();
    dto.id = review.getId();
    dto.authorNickname = review.getUserProfile().getNickname();
    dto.rating = review.getRating();
    dto.content = review.getContent();
    dto.createdAt = review.getCreatedAt();
    return dto;
  }
}
