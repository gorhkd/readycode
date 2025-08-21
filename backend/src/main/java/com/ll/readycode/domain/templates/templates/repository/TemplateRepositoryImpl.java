package com.ll.readycode.domain.templates.templates.repository;

import com.ll.readycode.domain.templates.query.TemplateSortType;
import com.ll.readycode.domain.templates.templates.entity.QTemplate;
import com.ll.readycode.domain.templates.templates.entity.Template;
import com.ll.readycode.global.common.types.OrderType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TemplateRepositoryImpl implements TemplateRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<Template> findScrollTemplates(
      String cursor, TemplateSortType sortType, OrderType orderType, Long categoryId, int limit) {
    QTemplate template = QTemplate.template;

    return queryFactory
        .selectFrom(template)
        .where(cursor != null ? template.createdAt.lt(cursor) : null)
        .orderBy(template.createdAt.desc())
        .limit(limit)
        .fetch();
  }
}
