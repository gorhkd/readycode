package com.ll.readycode.api.categories.dto.response;

import com.ll.readycode.domain.categories.entity.Category;

public record CategoryResponse(Long id, String name) {

  public static CategoryResponse from(Category category) {
    return new CategoryResponse(category.getId(), category.getName());
  }
}
