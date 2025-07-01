package com.ll.readycode.domain.categories.service;

import com.ll.readycode.api.categories.dto.request.CategoryRequest;
import com.ll.readycode.api.categories.dto.response.CategoryResponse;
import com.ll.readycode.domain.categories.entity.Category;
import com.ll.readycode.domain.categories.repository.CategoryRepository;
import com.ll.readycode.global.exception.CustomException;
import com.ll.readycode.global.exception.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CategoryService {
  private final CategoryRepository categoryRepository;

  @Transactional(readOnly = true)
  public List<CategoryResponse> getAllCategories() {
    return categoryRepository.findAll().stream().map(CategoryResponse::from).toList();
  }

  @Transactional
  public CategoryResponse createCategory(CategoryRequest request) {
    validateDuplicateName(request.name());

    Category category = Category.builder().name(request.name()).build();

    categoryRepository.save(category);
    return CategoryResponse.from(category);
  }

  @Transactional
  public CategoryResponse updateCategory(Long categoriesId, CategoryRequest request) {
    Category category = findCategoryById(categoriesId);

    if (!category.getName().equals(request.name())) {
      validateDuplicateName(request.name());
      category.updateName(request.name());
    }

    return CategoryResponse.from(category);
  }

  @Transactional
  public void deleteCategory(Long categoriesId) {
    Category category = findCategoryById(categoriesId);
    categoryRepository.delete(category);
  }

  @Transactional(readOnly = true)
  private void validateDuplicateName(String name) {
    if (categoryRepository.existsByName(name)) {
      throw new CustomException(ErrorCode.DUPLICATE_CATEGORY);
    }
  }

  @Transactional(readOnly = true)
  public Category findCategoryById(Long categoriesId) {
    return categoryRepository
        .findById(categoriesId)
        .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
  }
}
