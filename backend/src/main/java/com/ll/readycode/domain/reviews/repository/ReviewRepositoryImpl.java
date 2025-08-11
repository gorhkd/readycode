package com.ll.readycode.domain.reviews.repository;

import com.ll.readycode.domain.reviews.entity.QReview;
import com.ll.readycode.domain.reviews.entity.Review;
import com.ll.readycode.domain.reviews.enums.OrderType;
import com.ll.readycode.domain.reviews.enums.SortType;
import com.ll.readycode.domain.users.userprofiles.entity.QUserProfile;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.List;
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

  @Override
  public List<Review> findByTemplateWithCursor(
      Long templateId, String cursor, int limit, SortType sortType, OrderType orderType) {
    QReview r = QReview.review;
    QUserProfile u = QUserProfile.userProfile;

    return queryFactory
        .selectFrom(r)
        .join(r.userProfile, u)
        .fetchJoin()
        .where(r.template.id.eq(templateId), cursorPredicate(r, cursor, sortType, orderType))
        .orderBy(orderSpecifiers(r, sortType, orderType))
        .limit(limit)
        .fetch();
  }

  private BooleanExpression cursorPredicate(
      QReview r, String cursor, SortType sortType, OrderType orderType) {
    if (cursor == null || cursor.isBlank()) return null;

    String[] parts = cursor.split("\\|");
    if (parts.length != 2) return null; // 포맷 이상 → 첫 페이지 취급

    Long cursorId;
    try {
      cursorId = Long.valueOf(parts[1]);
    } catch (NumberFormatException e) {
      return null;
    }

    if (sortType == SortType.RATING) {
      BigDecimal curRating;
      try {
        curRating = new BigDecimal(parts[0]);
      } catch (NumberFormatException e) {
        return null;
      }
      if (orderType == OrderType.DESC) {
        // rating ↓, 동점이면 id ↓
        return r.rating.lt(curRating).or(r.rating.eq(curRating).and(r.id.lt(cursorId)));
      } else {
        // rating ↑, 동점이면 id ↑
        return r.rating.gt(curRating).or(r.rating.eq(curRating).and(r.id.gt(cursorId)));
      }
    } else {
      // LATEST = createdAt
      LocalDateTime curCreated;
      try {
        curCreated = LocalDateTime.parse(parts[0]); // ISO-8601 가정
      } catch (DateTimeParseException e) {
        return null;
      }
      if (orderType == OrderType.DESC) {
        // createdAt ↓, 동시간이면 id ↓
        return r.createdAt.lt(curCreated).or(r.createdAt.eq(curCreated).and(r.id.lt(cursorId)));
      } else {
        // createdAt ↑, 동시간이면 id ↑
        return r.createdAt.gt(curCreated).or(r.createdAt.eq(curCreated).and(r.id.gt(cursorId)));
      }
    }
  }

  @SuppressWarnings("unchecked")
  private OrderSpecifier<?>[] orderSpecifiers(QReview r, SortType sortType, OrderType orderType) {
    Order dir = (orderType == OrderType.DESC) ? Order.DESC : Order.ASC;

    if (sortType == SortType.RATING) {
      return new OrderSpecifier[] {
        new OrderSpecifier<>(dir, r.rating), new OrderSpecifier<>(dir, r.id) // tie-breaker
      };
    }
    // LATEST = createdAt
    return new OrderSpecifier[] {
      new OrderSpecifier<>(dir, r.createdAt), new OrderSpecifier<>(dir, r.id)
    };
  }
}
