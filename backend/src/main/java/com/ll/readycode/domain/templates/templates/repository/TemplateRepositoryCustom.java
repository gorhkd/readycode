package com.ll.readycode.domain.templates.templates.repository;

import com.ll.readycode.domain.templates.query.TemplateSortType;
import com.ll.readycode.domain.templates.templates.entity.Template;
import com.ll.readycode.global.common.types.OrderType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TemplateRepositoryCustom {
  List<Template> findScrollTemplates(
      TemplateSortType sortType,
      OrderType orderType,
      Long categoryId,
      int limit,
      LocalDateTime ts,
      Long lastId,
      BigDecimal rating,
      Long purchaseCount);
}
