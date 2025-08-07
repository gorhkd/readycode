package com.ll.readycode.domain.reviews.repository;

import java.util.Set;

public interface ReviewRepositoryCustom {
  Set<Long> findTemplateIdsWithReviewByUser(Long userId, Set<Long> templateIds);
}
