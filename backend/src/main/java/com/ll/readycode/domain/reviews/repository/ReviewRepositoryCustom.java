package com.ll.readycode.domain.reviews.repository;

import com.ll.readycode.domain.reviews.entity.Review;
import com.ll.readycode.domain.reviews.enums.OrderType;
import com.ll.readycode.domain.reviews.enums.SortType;
import java.util.List;
import java.util.Set;

public interface ReviewRepositoryCustom {
  Set<Long> findTemplateIdsWithReviewByUser(Long userId, Set<Long> templateIds);

  List<Review> findByTemplateWithCursor(
      Long templateId, String cursor, int limit, SortType sortType, OrderType orderType);
}
