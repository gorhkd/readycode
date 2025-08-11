package com.ll.readycode.api.templates.dto.response;

import com.ll.readycode.domain.templates.templates.entity.Template;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Schema(description = "템플릿 요약 정보 (목록용)")
public record TemplateSummary(
    @Schema(description = "템플릿 ID", example = "17") Long id,
    @Schema(description = "템플릿 제목", example = "JWT 로그인 템플릿") String title,
    @Schema(description = "가격 (포인트 단위)", example = "300") int price,
    @Schema(description = "카테고리 이름", example = "백엔드") String category,
    @Schema(description = "리뷰 총 개수", example = "100") long reviewCount,
    @Schema(description = "별별점 평균(소수 1자리, 절삭)", example = "4.5") String avgRating,
    @Schema(description = "생성일시", example = "2024-12-10T12:00:00") LocalDateTime createdAt) {
  public static TemplateSummary from(Template template) {
    return new TemplateSummary(
        template.getId(),
        template.getTitle(),
        template.getPrice(),
        template.getCategory().getName(),
        template.getReviewCount(),
        format1(template.getAvgRating()),
        template.getCreatedAt());
  }

  private static String format1(BigDecimal v) {
    if (v == null) return "0.0";
    return v.setScale(1, RoundingMode.DOWN).toPlainString();
  }
}
