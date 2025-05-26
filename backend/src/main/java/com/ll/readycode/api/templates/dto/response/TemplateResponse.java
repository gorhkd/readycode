package com.ll.readycode.api.templates.dto.response;

import com.ll.readycode.domain.templates.templates.entity.Template;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record TemplateResponse(
    Long postId,
    String title,
    String description,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    String category) {
  public static TemplateResponse of(Template template) {
    return TemplateResponse.builder()
        .postId(template.getId())
        .title(template.getTitle())
        .description(template.getDescription())
        .createdAt(template.getCreatedAt())
        .updatedAt(template.getUpdatedAt())
        .category(template.getCategory().getName())
        .build();
  }
}
