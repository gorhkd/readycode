package com.ll.readycode.api.templates.dto.response;

import com.ll.readycode.domain.templates.templates.entity.Template;
import java.time.LocalDateTime;

public record PurchasedTemplateResponse(
    Long id,
    String title,
    String description,
    int price,
    String category,
    LocalDateTime createdAt) {
  public static PurchasedTemplateResponse of(Template template) {
    return new PurchasedTemplateResponse(
        template.getId(),
        template.getTitle(),
        template.getDescription(),
        template.getPrice(),
        template.getCategory().getName(),
        template.getCreatedAt());
  }
}
