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

  public record Latest(LocalDateTime ts, Long id) {}

  public record Rating(BigDecimal rating, LocalDateTime ts, Long id) {}

  public record Popular(Long purchaseCount, LocalDateTime ts, Long id) {}

  private static final int RATING_CURSOR_PARTS = 3;
  private static final int POPULAR_CURSOR_PARTS = 3;
  private static final int LATEST_CURSOR_PARTS = 2;

  @Override
  public List<Template> findScrollTemplates(
      String cursor, TemplateSortType sortType, OrderType orderType, Long categoryId, int limit) {
    QTemplate t = QTemplate.template;
    BooleanBuilder where = new BooleanBuilder();

    if (categoryId != null) {
      where.and(t.category.id.eq(categoryId));
    }

    boolean desc = (orderType == OrderType.DESC);

    switch (sortType) {
      case LATEST -> applyLatestCursor(where, t, cursor, desc);
      case RATING -> applyRatingCursor(where, t, cursor, desc);
      case POPULAR -> applyPopularCursor(where, t, cursor, desc);
    }

    OrderSpecifier<?>[] orders = buildOrders(t, sortType, desc);

    return queryFactory.selectFrom(t).where(where).orderBy(orders).limit(limit).fetch();
  }

  private Latest decodeLatest(String cursor) {
    if (cursor == null) return null;
    String[] parts = cursor.split("\\|");
    if (parts.length != LATEST_CURSOR_PARTS) return null;

    try {
      LocalDateTime ts = LocalDateTime.parse(parts[0]);
      Long id = Long.parseLong(parts[1]);
      return new Latest(ts, id);
    } catch (Exception e) {
      return null;
    }
  }

  private void applyLatestCursor(BooleanBuilder where, QTemplate t, String cursor, boolean desc) {
    Latest key = decodeLatest(cursor);
    if (key == null) return;

    BooleanExpression edge = desc ? t.createdAt.lt(key.ts()) : t.createdAt.gt(key.ts());
    BooleanExpression tie =
        t.createdAt.eq(key.ts()).and(desc ? t.id.lt(key.id()) : t.id.gt(key.id()));

    where.and(edge.or(tie));
  }

  private Rating decodeRating(String cursor) {
    if (cursor == null) return null;
    String[] parts = cursor.split("\\|");
    if (parts.length != RATING_CURSOR_PARTS) return null;

    try {
      BigDecimal rating = new BigDecimal(parts[0]);
      LocalDateTime ts = LocalDateTime.parse(parts[1]);
      Long id = Long.parseLong(parts[2]);
      return new Rating(rating, ts, id);
    } catch (Exception e) {
      return null;
    }
  }

  private void applyRatingCursor(BooleanBuilder where, QTemplate t, String cursor, boolean desc) {
    Rating key = decodeRating(cursor);
    if (key == null) return;

    NumberExpression<BigDecimal> r = t.avgRating.coalesce(BigDecimal.ZERO);
    BooleanExpression edge = desc ? r.lt(key.rating()) : r.gt(key.rating());
    BooleanExpression tie1 =
        r.eq(key.rating()).and(desc ? t.createdAt.lt(key.ts()) : t.createdAt.gt(key.ts()));
    BooleanExpression tie2 =
        r.eq(key.rating())
            .and(t.createdAt.eq(key.ts()))
            .and(desc ? t.id.lt(key.id()) : t.id.gt(key.id()));

    where.and(edge.or(tie1).or(tie2));
  }

  private Popular decodePopular(String cursor) {
    if (cursor == null) return null;
    String[] parts = cursor.split("\\|");
    if (parts.length != POPULAR_CURSOR_PARTS) return null;

    try {
      Long purchase = Long.parseLong(parts[0]);
      LocalDateTime ts = LocalDateTime.parse(parts[1]);
      Long id = Long.parseLong(parts[2]);
      return new Popular(purchase, ts, id);
    } catch (Exception e) {
      return null;
    }
  }

  private void applyPopularCursor(BooleanBuilder where, QTemplate t, String cursor, boolean desc) {
    Popular key = decodePopular(cursor);
    if (key == null) return;

    BooleanExpression edge =
        desc ? t.purchaseCount.lt(key.purchaseCount()) : t.purchaseCount.gt(key.purchaseCount());
    BooleanExpression tie1 =
        t.purchaseCount
            .eq(key.purchaseCount())
            .and(desc ? t.createdAt.lt(key.ts()) : t.createdAt.gt(key.ts()));
    BooleanExpression tie2 =
        t.purchaseCount
            .eq(key.purchaseCount())
            .and(t.createdAt.eq(key.ts()))
            .and(desc ? t.id.lt(key.id()) : t.id.gt(key.id()));

    where.and(edge.or(tie1).or(tie2));
  }

  private OrderSpecifier<?>[] buildOrders(QTemplate t, TemplateSortType sortType, boolean desc) {
    switch (sortType) {
      case RATING:
        // avgRating 필드가 있을 때만 활성화
        return desc
            ? new OrderSpecifier<?>[] {t.avgRating.desc(), t.createdAt.desc(), t.id.desc()}
            : new OrderSpecifier<?>[] {t.avgRating.asc(), t.createdAt.asc(), t.id.asc()};
      case POPULAR:
        // purchaseCount 필드가 있을 때만 활성화
        return desc
            ? new OrderSpecifier<?>[] {t.purchaseCount.desc(), t.createdAt.desc(), t.id.desc()}
            : new OrderSpecifier<?>[] {t.purchaseCount.asc(), t.createdAt.asc(), t.id.asc()};
      case LATEST:
      default:
        return desc
            ? new OrderSpecifier<?>[] {t.createdAt.desc(), t.id.desc()}
            : new OrderSpecifier<?>[] {t.createdAt.asc(), t.id.asc()};
    }
  }
}
