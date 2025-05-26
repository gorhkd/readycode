package com.ll.readycode.api.templates.dto.request;

import jakarta.validation.constraints.*;

public record TemplateCreateRequest(
    @NotBlank(message = "제목은 필수입니다.") String title,
    @NotBlank(message = "설명은 필수입니다.") String description,
    @Min(value = 0, message = "가격은 0 이상이어야 합니다.") int price,
    @NotNull(message = "카테고리는 필수입니다.") Long categoryId,
    @NotBlank(message = "이미지 URL은 필수입니다.") String image) {}
