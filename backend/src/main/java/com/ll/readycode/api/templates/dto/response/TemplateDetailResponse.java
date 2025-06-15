package com.ll.readycode.api.templates.dto.response;

import com.ll.readycode.domain.templates.templates.entity.Template;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record TemplateDetailResponse(
    Long templateId,
    String title,
    String description,
    String imageUrl,
    int price,
    String category,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {
  public static TemplateDetailResponse of(Template template) {
    return TemplateDetailResponse.builder()
        .templateId(template.getId())
        .title(template.getTitle())
        .description(template.getDescription())
        .imageUrl(template.getImage())
        .price(template.getPrice())
        .category(template.getCategory().getName())
        .createdAt(template.getCreatedAt())
        .updatedAt(template.getUpdatedAt())
        .build();
  }
}
