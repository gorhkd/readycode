package com.ll.readycode.domain.reviews.reader;

import com.ll.readycode.domain.reviews.entity.Review;
import com.ll.readycode.domain.reviews.repository.ReviewRepository;
import com.ll.readycode.global.exception.CustomException;
import com.ll.readycode.global.exception.ErrorCode;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewReader {

  private final ReviewRepository reviewRepository;

  // 템플릿 집합 중, 유저가 리뷰한 템플릿 ID 모음
  public Set<Long> findTemplateIdsWithReviewByUser(Long userId, Set<Long> templateIds) {
    return reviewRepository.findTemplateIdsWithReviewByUser(userId, templateIds);
  }

  // 존재 여부
  public boolean existsByUserAndTemplate(Long userId, Long templateId) {
    return reviewRepository.existsByUserProfileIdAndTemplateId(userId, templateId);
  }

  // 단건 조회 (없으면 예외)
  public Review getByUserAndTemplate(Long userId, Long templateId) {
    return reviewRepository
        .findByUserProfileIdAndTemplateId(userId, templateId)
        .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
  }

  // 템플릿별 리뷰 목록 조회
  public List<Review> findByTemplate(Long templateId) {
    return reviewRepository.findByTemplateId(templateId);
  }
}
