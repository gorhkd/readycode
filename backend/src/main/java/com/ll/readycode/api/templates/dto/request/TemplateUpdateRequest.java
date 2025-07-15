package com.ll.readycode.api.templates.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "템플릿 수정 요청 DTO")
public record TemplateUpdateRequest(
    @Schema(description = "템플릿 제목", example = "나만의 로그인 템플릿") @NotBlank(message = "제목은 필수입니다.")
        String title,
    @Schema(description = "템플릿 설명", example = "JWT 기반 로그인 기능 포함") @NotBlank(message = "설명은 필수입니다.")
        String description,
    @Schema(description = "템플릿 가격 (포인트 단위)", example = "300")
        @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
        int price,
    @Schema(description = "카테고리 ID", example = "1") @NotNull(message = "카테고리는 필수입니다.")
        Long categoryId,
    @Schema(description = "대표 이미지 URL", example = "https://image.url/sample.png")
        @NotBlank(message = "이미지 URL은 필수입니다.")
        String image) {}
