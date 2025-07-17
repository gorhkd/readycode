package com.ll.readycode.api.categories.dto.response;

import com.ll.readycode.domain.categories.entity.Category;
import io.swagger.v3.oas.annotations.media.Schema;

public record CategoryResponse(
    @Schema(description = "카테고리 ID", example = "1") Long id,
    @Schema(description = "카테고리 이름", example = "백엔드") String name) {

  public static CategoryResponse from(Category category) {
    return new CategoryResponse(category.getId(), category.getName());
  }
}
