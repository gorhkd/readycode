package com.ll.readycode.api.templates.dto.response;

import com.ll.readycode.domain.templates.templates.entity.Template;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;

@Schema(description = "템플릿 상세 응답 DTO")
@Builder
public record TemplateDetailResponse(
    @Schema(description = "템플릿 ID", example = "17") Long templateId,
    @Schema(description = "템플릿 제목", example = "JWT 로그인 템플릿") String title,
    @Schema(description = "템플릿 설명", example = "JWT 기반 로그인/회원가입 구현") String description,
    @Schema(description = "이미지 URL", example = "https://image.url/sample.png") String imageUrl,
    @Schema(description = "가격 (포인트)", example = "300") int price,
    @Schema(description = "카테고리 이름", example = "백엔드") String category,
    @Schema(description = "생성일시", example = "2024-12-10T12:00:00") LocalDateTime createdAt,
    @Schema(description = "수정일시", example = "2024-12-11T13:00:00") LocalDateTime updatedAt) {
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
