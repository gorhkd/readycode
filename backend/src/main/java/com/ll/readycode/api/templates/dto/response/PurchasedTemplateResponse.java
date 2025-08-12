package com.ll.readycode.api.templates.dto.response;

import com.ll.readycode.domain.templates.templates.entity.Template;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "구매한 템플릿 응답 DTO")
public record PurchasedTemplateResponse(
    @Schema(description = "템플릿 ID", example = "1") Long id,
    @Schema(description = "템플릿 제목", example = "JWT 로그인 템플릿") String title,
    @Schema(description = "템플릿 설명", example = "JWT를 이용한 로그인 기능 템플릿입니다.") String description,
    @Schema(description = "템플릿 가격", example = "100") int price,
    @Schema(description = "카테고리 이름", example = "백엔드") String category,
    @Schema(description = "템플릿 생성일시", example = "2025-08-03T13:44:00") LocalDateTime createdAt,
    @Schema(description = "리뷰 여부", example = "false") boolean hasReview) {
  public static PurchasedTemplateResponse of(Template template, boolean hasReview) {
    return new PurchasedTemplateResponse(
        template.getId(),
        template.getTitle(),
        template.getDescription(),
        template.getPrice(),
        template.getCategory().getName(),
        template.getCreatedAt(),
        hasReview);
  }
}
