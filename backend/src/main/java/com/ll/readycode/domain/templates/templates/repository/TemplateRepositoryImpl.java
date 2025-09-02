package com.ll.readycode.domain.templates.templates.repository;

import com.ll.readycode.domain.templates.query.TemplateSortType;
import com.ll.readycode.domain.templates.templates.entity.QTemplate;
import com.ll.readycode.domain.templates.templates.entity.Template;
import com.ll.readycode.global.common.types.OrderType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TemplateRepositoryImpl implements TemplateRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<Template> findScrollTemplates(
      TemplateSortType sortType,
      OrderType orderType,
      Long categoryId,
      int limit,
      LocalDateTime ts,
      Long lastId,
      BigDecimal rating,
      Long purchaseCount) {
    QTemplate t = QTemplate.template;
    BooleanBuilder where = new BooleanBuilder();

    if (categoryId != null) {
      where.and(t.category.id.eq(categoryId));
    }

    boolean desc = (orderType == OrderType.DESC);

    switch (sortType) {
      case LATEST -> applyLatestCursor(where, t, ts, lastId, desc);
      case RATING -> applyRatingCursor(where, t, rating, ts, lastId, desc);
      case POPULAR -> applyPopularCursor(where, t, purchaseCount, ts, lastId, desc);
    }

    OrderSpecifier<?>[] orders = buildOrders(t, sortType, desc);

    return queryFactory.selectFrom(t).where(where).orderBy(orders).limit(limit).fetch();
  }

  private void applyLatestCursor(
      BooleanBuilder where, QTemplate t, LocalDateTime ts, Long lastId, boolean desc) {
    if (ts == null || lastId == null) return;

    BooleanExpression edge = desc ? t.createdAt.lt(ts) : t.createdAt.gt(ts);
    BooleanExpression tie = t.createdAt.eq(ts).and(desc ? t.id.lt(lastId) : t.id.gt(lastId));

    where.and(edge.or(tie));
  }

  private void applyRatingCursor(
      BooleanBuilder where,
      QTemplate t,
      BigDecimal rating,
      LocalDateTime ts,
      Long lastId,
      boolean desc) {
    if (rating == null || ts == null || lastId == null) return;

    NumberExpression<BigDecimal> r = t.avgRating.coalesce(BigDecimal.ZERO);
    BooleanExpression edge = desc ? r.lt(rating) : r.gt(rating);
    BooleanExpression tie1 = r.eq(rating).and(desc ? t.createdAt.lt(ts) : t.createdAt.gt(ts));
    BooleanExpression tie2 =
        r.eq(rating).and(t.createdAt.eq(ts)).and(desc ? t.id.lt(lastId) : t.id.gt(lastId));

    where.and(edge.or(tie1).or(tie2));
  }

  private void applyPopularCursor(
      BooleanBuilder where,
      QTemplate t,
      Long purchaseCount,
      LocalDateTime ts,
      Long lastId,
      boolean desc) {
    if (purchaseCount == null || ts == null || lastId == null) return;

    NumberExpression<Long> c = t.purchaseCount.coalesce(0L);
    BooleanExpression edge = desc ? c.lt(purchaseCount) : c.gt(purchaseCount);
    BooleanExpression tie1 =
        c.eq(purchaseCount).and(desc ? t.createdAt.lt(ts) : t.createdAt.gt(ts));
    BooleanExpression tie2 =
        c.eq(purchaseCount).and(t.createdAt.eq(ts)).and(desc ? t.id.lt(lastId) : t.id.gt(lastId));

    where.and(edge.or(tie1).or(tie2));
  }

  private OrderSpecifier<?>[] buildOrders(QTemplate t, TemplateSortType sortType, boolean desc) {
    return switch (sortType) {
      case RATING ->
          desc
              ? new OrderSpecifier<?>[] {t.avgRating.desc(), t.createdAt.desc(), t.id.desc()}
              : new OrderSpecifier<?>[] {t.avgRating.asc(), t.createdAt.asc(), t.id.asc()};
      case POPULAR ->
          desc
              ? new OrderSpecifier<?>[] {t.purchaseCount.desc(), t.createdAt.desc(), t.id.desc()}
              : new OrderSpecifier<?>[] {t.purchaseCount.asc(), t.createdAt.asc(), t.id.asc()};
      case LATEST ->
          desc
              ? new OrderSpecifier<?>[] {t.createdAt.desc(), t.id.desc()}
              : new OrderSpecifier<?>[] {t.createdAt.asc(), t.id.asc()};
    };
  }
}
