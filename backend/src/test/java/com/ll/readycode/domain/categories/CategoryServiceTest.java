package com.ll.readycode.domain.categories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.ll.readycode.api.categories.dto.request.CategoryRequest;
import com.ll.readycode.api.categories.dto.response.CategoryResponse;
import com.ll.readycode.domain.categories.entity.Category;
import com.ll.readycode.domain.categories.repository.CategoryRepository;
import com.ll.readycode.domain.categories.service.CategoryService;
import com.ll.readycode.global.exception.CustomException;
import com.ll.readycode.global.exception.ErrorCode;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

  @InjectMocks private CategoryService categoryService;

  @Mock private CategoryRepository categoryRepository;

  Category category1 = Category.builder().id(1L).name("백엔드").build();
  Category category2 = Category.builder().id(2L).name("프론트").build();

  @Test
  @DisplayName("카테고리 생성 성공")
  void createCategory_success() {
    // given
    CategoryRequest request = new CategoryRequest("백엔드");
    given(categoryRepository.existsByName("백엔드")).willReturn(false);
    given(categoryRepository.save(any()))
        .willAnswer(
            invocation -> {
              Category c = invocation.getArgument(0);
              return Category.builder().id(1L).name(c.getName()).build();
            });

    // when
    CategoryResponse response = categoryService.createCategory(request);

    // then
    assertThat(response.name()).isEqualTo("백엔드");
    verify(categoryRepository).save(any());
  }

  @Test
  @DisplayName("카테고리 이름 중복 시 예외 발생")
  void createCategory_whenDuplicate_thenThrowException() {
    // given
    CategoryRequest request = new CategoryRequest("백엔드");
    given(categoryRepository.existsByName("백엔드")).willReturn(true);

    // expect
    CustomException exception =
        assertThrows(CustomException.class, () -> categoryService.createCategory(request));

    assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.DUPLICATE_CATEGORY);
  }

  @Test
  @DisplayName("카테고리 수정 성공")
  void updateCategory_success() {
    // given
    CategoryRequest request = new CategoryRequest("프론트");
    Category existing = Category.builder().id(1L).name("백엔드").build();

    given(categoryRepository.findById(1L)).willReturn(Optional.of(existing));
    given(categoryRepository.existsByName("프론트")).willReturn(false);

    // when
    CategoryResponse response = categoryService.updateCategory(1L, request);

    // then
    assertThat(response.name()).isEqualTo("프론트");
  }

  @Test
  @DisplayName("카테고리 삭제 성공")
  void deleteCategory_success() {
    // given
    Category category = mock(Category.class);
    given(categoryRepository.findById(1L)).willReturn(Optional.of(category));

    // when
    categoryService.deleteCategory(1L);

    // then
    verify(categoryRepository).delete(category);
  }

  @Test
  @DisplayName("카테고리 조회 실패 시 예외 발생")
  void findCategoryById_whenNotFound_thenThrowException() {
    // given
    given(categoryRepository.findById(99L)).willReturn(Optional.empty());

    // expect
    CustomException exception =
        assertThrows(CustomException.class, () -> categoryService.findCategoryById(99L));

    assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.CATEGORY_NOT_FOUND);
  }

  @Test
  @DisplayName("카테고리 목록 조회 성공")
  void getAllCategories_success() {
    // given
    List<Category> categories = List.of(category1, category2);
    given(categoryRepository.findAll()).willReturn(categories);

    // when
    List<CategoryResponse> result = categoryService.getAllCategories();

    // then
    assertThat(result).hasSize(2);
    assertThat(result.get(0).name()).isEqualTo("백엔드");
    assertThat(result.get(1).name()).isEqualTo("프론트");
  }
}
