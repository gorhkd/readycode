package com.ll.readycode.api.reviews.dto.response;

import com.ll.readycode.domain.reviews.entity.Review;
import java.time.LocalDateTime;

public class ReviewResponse {

  private Long id;
  private Long templateId;
  private Long userProfileId;
  private String authorNickname;
  private Double rating;
  private String content;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static ReviewResponse of(Review review) {
    ReviewResponse dto = new ReviewResponse();
    dto.id = review.getId();
    dto.templateId = review.getTemplate().getId();
    dto.userProfileId = review.getUserProfile().getId();
    dto.authorNickname = review.getUserProfile().getNickname();
    dto.rating = review.getRating();
    dto.content = review.getContent();
    dto.createdAt = review.getCreatedAt();
    dto.updatedAt = review.getUpdatedAt();
    return dto;
  }
}
