package com.ll.readycode.api.reviews.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

public record ReviewCreateRequest(
    @Schema(description = "리뷰 내용", example = "정말 유용한 템플릿이었습니다!", maxLength = 1000)
        @NotBlank(message = "리뷰 내용을 입력해주세요.")
        String content,
    @Schema(description = "별점 (1.0 ~ 5.0)", example = "4.5", minimum = "1.0", maximum = "5.0")
        @DecimalMin(value = "1.0", inclusive = true, message = "별점은 1.0 이상이어야 합니다.")
        @DecimalMax(value = "5.0", inclusive = true, message = "별점은 5.0 이하여야 합니다.")
        BigDecimal rating) {}
