package com.ll.readycode.api.templates.dto.response;

import com.ll.readycode.domain.templates.templates.entity.Template;
import java.time.LocalDateTime;

public record TemplateSummary(
    Long id, String title, int price, String category, LocalDateTime createdAt) {
  public static TemplateSummary from(Template template) {
    return new TemplateSummary(
        template.getId(),
        template.getTitle(),
        template.getPrice(),
        template.getCategory().toString(),
        template.getCreatedAt());
  }
}
