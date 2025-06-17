package com.ll.readycode.domain.templates.templates.repository;

import com.ll.readycode.domain.templates.templates.entity.Template;
import java.time.LocalDateTime;
import java.util.List;

public interface TemplateRepositoryCustom {
  List<Template> findScrollTemplates(LocalDateTime cursor, int limit);
}
