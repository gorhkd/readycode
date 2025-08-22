package com.ll.readycode.domain.reviews.repository;

import com.ll.readycode.domain.reviews.entity.Review;
import com.ll.readycode.domain.reviews.query.ReviewSortType;
import com.ll.readycode.global.common.types.OrderType;
import java.util.List;
import java.util.Set;

public interface ReviewRepositoryCustom {
  Set<Long> findTemplateIdsWithReviewByUser(Long userId, Set<Long> templateIds);

  List<Review> findByTemplateWithCursor(
      Long templateId,
      String cursor,
      int limit,
      ReviewSortType reviewSortType,
      OrderType orderType);
}
