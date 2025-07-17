package com.ll.readycode.api.templates.dto.response;

import com.ll.readycode.domain.templates.templates.entity.Template;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;

@Schema(description = "템플릿 응답 DTO (간단 목록용)")
@Builder
public record TemplateResponse(
    @Schema(description = "템플릿 ID", example = "17") Long templateId,
    @Schema(description = "템플릿 제목", example = "JWT 로그인 템플릿") String title,
    @Schema(description = "템플릿 설명", example = "JWT 기반 로그인/회원가입 구현") String description,
    @Schema(description = "생성일시", example = "2024-12-10T12:00:00") LocalDateTime createdAt,
    @Schema(description = "수정일시", example = "2024-12-11T13:00:00") LocalDateTime updatedAt,
    @Schema(description = "카테고리 이름", example = "백엔드") String category) {
  public static TemplateResponse of(Template template) {
    return TemplateResponse.builder()
        .templateId(template.getId())
        .title(template.getTitle())
        .description(template.getDescription())
        .createdAt(template.getCreatedAt())
        .updatedAt(template.getUpdatedAt())
        .category(template.getCategory().getName())
        .build();
  }
}
