package com.ll.readycode.api.reviews.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

public record ReviewCreateRequest(
    @NotBlank(message = "리뷰 내용을 입력해주세요.") String content,
    @DecimalMin(value = "1.0", inclusive = true, message = "별점은 1.0 이상이어야 합니다.")
        @DecimalMax(value = "5.0", inclusive = true, message = "별점은 5.0 이하여야 합니다.")
        Double rating) {}
