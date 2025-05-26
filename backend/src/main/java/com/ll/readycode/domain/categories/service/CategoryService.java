package com.ll.readycode.domain.categories.service;

import com.ll.readycode.domain.categories.entity.Category;
import com.ll.readycode.domain.categories.repository.CategoryRepository;
import com.ll.readycode.global.exception.CustomException;
import com.ll.readycode.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryService {
  private final CategoryRepository categoryRepository;

  public Category findCategoryById(Long id) {
    return categoryRepository
        .findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
  }
}
