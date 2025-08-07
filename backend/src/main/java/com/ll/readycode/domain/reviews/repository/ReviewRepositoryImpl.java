package com.ll.readycode.domain.reviews.repository;

import com.ll.readycode.domain.reviews.entity.QReview;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public Set<Long> findTemplateIdsWithReviewByUser(Long userId, Set<Long> templateIds) {
    QReview review = QReview.review;

    return new HashSet<>(
        queryFactory
            .select(review.template.id)
            .from(review)
            .where(review.userProfile.id.eq(userId), review.template.id.in(templateIds))
            .fetch());
  }
}
