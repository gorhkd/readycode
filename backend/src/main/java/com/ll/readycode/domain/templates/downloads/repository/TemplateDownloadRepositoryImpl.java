package com.ll.readycode.domain.templates.downloads.repository;

import com.ll.readycode.api.admin.dto.response.AdminResponseDto.TemplateDownloadDetails;
import com.ll.readycode.domain.templates.downloads.entity.QTemplateDownload;
import com.ll.readycode.domain.templates.templates.entity.QTemplate;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TemplateDownloadRepositoryImpl implements TemplateDownloadCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<TemplateDownloadDetails> findTemplatesForDownloadStatistics(
      LocalDateTime startDate,
      LocalDateTime endDate,
      Long templateId) {

    QTemplate t = QTemplate.template;
    QTemplateDownload td = QTemplateDownload.templateDownload;

    BooleanBuilder where = new BooleanBuilder();

    if (startDate != null) {
      where.and(td.createdAt.goe(startDate));
    }

    if (endDate != null) {
      where.and(td.createdAt.loe(endDate));
    }

    if (templateId != null) {
      where.and(t.id.eq(templateId));
    }

    return queryFactory.select(
            Projections.constructor(TemplateDownloadDetails.class,
                t.id,
                t.title,
                td.id.count()
            ))
        .from(td)
          .join(td.template, t)
        .where(where)
        .groupBy(t.id, t.title)
        .orderBy(t.id.asc())
        .fetch();
  }
}
