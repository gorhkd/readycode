package com.ll.readycode.api.categories.controller;

import com.ll.readycode.api.categories.dto.request.CategoryRequest;
import com.ll.readycode.api.categories.dto.response.CategoryResponse;
import com.ll.readycode.domain.categories.service.CategoryService;
import com.ll.readycode.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/categories")
@RequiredArgsConstructor
@RestController
public class CategoryController {
  private final CategoryService categoryService;

  @Operation(summary = "카테고리 목록 조회", description = "카테고리 목록을 조회합니다.")
  @GetMapping
  public ResponseEntity<SuccessResponse<List<CategoryResponse>>> getAllCategories() {
    List<CategoryResponse> categories = categoryService.getAllCategories();
    return ResponseEntity.ok(SuccessResponse.of("카테고리 목록을 조회했습니다.", categories));
  }

  @Operation(summary = "카테고리 생성", description = "카테고리를 생성합니다.")
  @PostMapping
  public ResponseEntity<SuccessResponse<CategoryResponse>> createCategory(
      @RequestBody @Valid CategoryRequest request) {
    CategoryResponse response = categoryService.createCategory(request);
    return ResponseEntity.ok(SuccessResponse.of("카테고리를 생성했습니다.", response));
  }

  @Operation(summary = "카테고리를 수정합니다.", description = "카테고리 ID를 기준으로 수정합니다.")
  @PatchMapping("/{categoriesId}")
  public ResponseEntity<SuccessResponse<CategoryResponse>> updateCategory(
      @PathVariable Long categoriesId, @RequestBody @Valid CategoryRequest request) {
    CategoryResponse response = categoryService.updateCategory(categoriesId, request);
    return ResponseEntity.ok(SuccessResponse.of("카테고리를 수정했습니다.", response));
  }

  @Operation(summary = "카테고리를 삭제합니다.", description = "카테고리를 ID를 기준으로 삭제합니다.")
  @DeleteMapping("/{categoriesId}")
  public ResponseEntity<SuccessResponse<Void>> deleteCategory(@PathVariable Long categoriesId) {
    categoryService.deleteCategory(categoriesId);
    return ResponseEntity.ok(SuccessResponse.of("카테고리를 삭제했습니다.", null));
  }
}
