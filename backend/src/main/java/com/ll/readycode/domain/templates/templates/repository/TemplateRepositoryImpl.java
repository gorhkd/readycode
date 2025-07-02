package com.ll.readycode.domain.templates.templates.repository;

import com.ll.readycode.domain.templates.templates.entity.QTemplate;
import com.ll.readycode.domain.templates.templates.entity.Template;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TemplateRepositoryImpl implements TemplateRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<Template> findScrollTemplates(LocalDateTime cursor, int limit) {
    QTemplate template = QTemplate.template;

    return queryFactory
        .selectFrom(template)
        .where(cursor != null ? template.createdAt.lt(cursor) : null)
        .orderBy(template.createdAt.desc())
        .limit(limit)
        .fetch();
  }
}
