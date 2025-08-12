package com.ll.readycode.domain.templates;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

import com.ll.readycode.api.templates.dto.request.TemplateCreateRequest;
import com.ll.readycode.api.templates.dto.request.TemplateUpdateRequest;
import com.ll.readycode.api.templates.dto.response.TemplateScrollResponse;
import com.ll.readycode.domain.categories.entity.Category;
import com.ll.readycode.domain.categories.service.CategoryService;
import com.ll.readycode.domain.templates.templates.entity.Template;
import com.ll.readycode.domain.templates.templates.repository.TemplateRepository;
import com.ll.readycode.domain.templates.templates.service.TemplateService;
import com.ll.readycode.domain.users.userprofiles.entity.UserProfile;
import com.ll.readycode.domain.users.userprofiles.entity.UserPurpose;
import com.ll.readycode.domain.users.userprofiles.entity.UserRole;
import com.ll.readycode.global.exception.CustomException;
import com.ll.readycode.global.exception.ErrorCode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TemplateServiceTest {

  @InjectMocks private TemplateService templateService;

  @Mock private TemplateRepository templateRepository;

  @Mock private CategoryService categoryService;

  Category category1 = Category.builder().id(1L).name("백엔드").build();
  Category category2 = Category.builder().id(2L).name("프론트").build();

  UserProfile userProfile =
      UserProfile.builder()
          .id(1L)
          .nickname("abc")
          .phoneNumber("010")
          .role(UserRole.USER)
          .purpose(UserPurpose.LEARNING)
          .build();

  private Template createTemplate(
      Long id, String title, Category category, LocalDateTime localDateTime) {
    return Template.builder()
        .id(id)
        .title(title)
        .description("Old")
        .price(100)
        .image("old.png")
        .seller(userProfile)
        .category(category)
        .createdAt(localDateTime)
        .build();
  }

  @Test
  @DisplayName("템플릿 생성 성공")
  void createTemplate_success() {
    // given
    TemplateCreateRequest request =
        new TemplateCreateRequest("로그인 템플릿", "JWT 기반 로그인", 100, 1L, "image.png");
    given(categoryService.findCategoryById(anyLong())).willReturn(category1);
    given(templateRepository.save(any())).willAnswer(invocation -> invocation.getArgument(0));

    // when
    Template result = templateService.create(request, userProfile);

    // then
    assertThat(result.getTitle()).isEqualTo("로그인 템플릿");
    assertThat(result.getCategory()).isEqualTo(category1);
    verify(templateRepository).save(any());
  }

  @Test
  @DisplayName("템플릿 수정 성공")
  void updateTemplate_success() {
    // given
    Template existing = createTemplate(1L, "템플릿1", category1, LocalDateTime.now());

    TemplateUpdateRequest request =
        new TemplateUpdateRequest("New Title", "New Desc", 200, 1L, "new.png");

    given(templateRepository.findById(1L)).willReturn(Optional.of(existing));
    given(categoryService.findCategoryById(1L)).willReturn(category1);

    // when
    Template result = templateService.update(1L, request, userProfile.getId());

    // then
    assertThat(result.getTitle()).isEqualTo("New Title");
    assertThat(result.getCategory().getName()).isEqualTo("백엔드");
    assertThat(result.getPrice()).isEqualTo(200);
  }

  @Test
  @DisplayName("템플릿 삭제 성공")
  void deleteTemplate_success() {
    // given
    Template template = createTemplate(1L, "템플릿1", category1, LocalDateTime.now());
    given(templateRepository.findById(1L)).willReturn(Optional.of(template));

    // when
    templateService.delete(1L, userProfile.getId());

    // then
    verify(templateRepository).delete(template);
  }

  @Test
  @DisplayName("템플릿 조회 cursor 값이 없을 때 성공")
  void getTemplates_whenCursorIsNull_thenReturnLatestTemplates() {
    // given
    List<Template> templates =
        List.of(
            createTemplate(1L, "템플릿1", category1, LocalDateTime.now()),
            createTemplate(2L, "템플릿2", category2, LocalDateTime.now().minusMinutes(1)));

    given(templateRepository.findScrollTemplates(null, 10)).willReturn(templates);

    // when
    TemplateScrollResponse response = templateService.getTemplateList(null, 10);

    // then
    assertThat(response.templates()).hasSize(2);
    assertThat(response.nextCursor()).isEqualTo(templates.get(1).getCreatedAt());
  }

  @Test
  @DisplayName("템플릿 조회 cursor 값이 있을 때 성공")
  void getTemplates_whenCursorExists_thenReturnTemplatesBeforeCursor() {
    // given
    LocalDateTime cursor = LocalDateTime.now();

    List<Template> templates =
        List.of(
            createTemplate(1L, "템플릿1", category1, cursor.minusMinutes(1)),
            createTemplate(2L, "템플릿2", category2, cursor.minusMinutes(2)));

    given(templateRepository.findScrollTemplates(cursor, 10)).willReturn(templates);

    // when
    TemplateScrollResponse response = templateService.getTemplateList(cursor, 10);

    // then
    assertThat(response.templates()).hasSize(2);
    assertThat(response.nextCursor()).isEqualTo(templates.get(1).getCreatedAt());
  }

  @Test
  @DisplayName("템플릿 조회 결과가 비어 있을 때 nextCursor는 null")
  void getTemplates_whenResultIsEmpty_thenNextCursorIsNull() {
    // given
    given(templateRepository.findScrollTemplates(null, 10)).willReturn(List.of());

    // when
    TemplateScrollResponse response = templateService.getTemplateList(null, 10);

    // then
    assertThat(response.templates()).isEmpty();
    assertThat(response.nextCursor()).isNull();
  }

  @Test
  @DisplayName("템플릿 조회 예외처리")
  void findTemplateById_whenNotFound_thenThrowException() {
    // given
    given(templateRepository.findById(99L)).willReturn(Optional.empty());

    // expect
    CustomException exception =
        assertThrows(CustomException.class, () -> templateService.findTemplateById(99L));

    assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.TEMPLATE_NOT_FOUND);
  }
}
