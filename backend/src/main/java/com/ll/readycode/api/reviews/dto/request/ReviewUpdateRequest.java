package com.ll.readycode.api.reviews.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record ReviewUpdateRequest(
    @Schema(description = "수정할 리뷰 내용", example = "내용을 조금 수정했습니다.", maxLength = 1000)
        @NotBlank(message = "리뷰 내용은 필수입니다.")
        @Size(max = 1000, message = "리뷰 내용은 1000자 이하로 작성해주세요.")
        String content,
    @Schema(description = "수정할 별점 (1.0 ~ 5.0)", example = "3.0", minimum = "1.0", maximum = "5.0")
        @NotNull(message = "평점은 필수입니다.")
        @Min(value = 1, message = "평점은 최소 1점이어야 합니다.")
        @Max(value = 5, message = "평점은 최대 5점이어야 합니다.")
        Double rating) {}
