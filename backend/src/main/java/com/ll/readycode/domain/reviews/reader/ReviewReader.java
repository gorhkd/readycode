package com.ll.readycode.domain.reviews.reader;

import com.ll.readycode.domain.reviews.repository.ReviewRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewReader {

  private final ReviewRepository reviewRepository;

  public Set<Long> findTemplateIdsWithReviewByUser(Long userId, Set<Long> templateIds) {
    return reviewRepository.findTemplateIdsWithReviewByUser(userId, templateIds);
  }
}
