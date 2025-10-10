package com.ll.readycode.domain.templates;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.ll.readycode.api.templates.dto.request.TemplateCreateRequest;
import com.ll.readycode.api.templates.dto.request.TemplateUpdateRequest;
import com.ll.readycode.api.templates.dto.response.TemplateScrollResponse;
import com.ll.readycode.domain.categories.entity.Category;
import com.ll.readycode.domain.categories.service.CategoryService;
import com.ll.readycode.domain.templates.files.entity.TemplateFile;
import com.ll.readycode.domain.templates.files.service.TemplateFileService;
import com.ll.readycode.domain.templates.query.TemplateSortType;
import com.ll.readycode.domain.templates.templates.entity.Template;
import com.ll.readycode.domain.templates.templates.repository.TemplateRepository;
import com.ll.readycode.domain.templates.templates.service.TemplateService;
import com.ll.readycode.domain.users.userprofiles.entity.UserProfile;
import com.ll.readycode.domain.users.userprofiles.entity.UserPurpose;
import com.ll.readycode.domain.users.userprofiles.entity.UserRole;
import com.ll.readycode.global.common.types.OrderType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class TemplateServiceTest {

  @InjectMocks private TemplateService templateService;

  @Mock private TemplateRepository templateRepository;

  @Mock private TemplateFileService templateFileService;

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
      Long id,
      String title,
      Category category,
      LocalDateTime createdAt,
      BigDecimal avgRating,
      Long purchaseCount) {
    return Template.builder()
        .id(id)
        .title(title)
        .description("Old")
        .price(100)
        .image("old.png")
        .seller(userProfile)
        .category(category)
        .createdAt(createdAt)
        .seller(userProfile)
        .avgRating(avgRating)
        .purchaseCount(purchaseCount)
        .build();
  }

  private String decode(String b64) {
    if (b64 == null) return null;
    return new String(
        java.util.Base64.getUrlDecoder().decode(b64), java.nio.charset.StandardCharsets.UTF_8);
  }

  @Test
  @DisplayName("템플릿 생성 성공")
  void createTemplate_success() {
    // given
    TemplateCreateRequest request =
        new TemplateCreateRequest("로그인 템플릿", "JWT 기반 로그인", 100, 1L, "image.png");

    MockMultipartFile file =
        new MockMultipartFile("file", "sample.zip", "application/zip", "test".getBytes());

    given(categoryService.findCategoryById(anyLong())).willReturn(category1);
    given(templateFileService.create(any())).willReturn(mock(TemplateFile.class));
    given(templateRepository.save(any())).willAnswer(invocation -> invocation.getArgument(0));

    // when
    Template result = templateService.create(request, file, userProfile);

    // then
    assertThat(result.getTitle()).isEqualTo("로그인 템플릿");
    verify(templateRepository).save(any());
  }

  @Test
  @DisplayName("템플릿 수정 성공")
  void updateTemplate_success() {
    // given
    Template existing = createTemplate(1L, "템플릿1", category1, LocalDateTime.now(), null, null);
    TemplateFile mockFile = mock(TemplateFile.class);
    existing.setTemplateFile(mockFile);

    TemplateUpdateRequest request =
        new TemplateUpdateRequest("New Title", "New Desc", 200, 1L, "new.png");

    MockMultipartFile file =
        new MockMultipartFile("file", "sample.zip", "application/zip", "test".getBytes());

    given(templateRepository.findById(1L)).willReturn(Optional.of(existing));
    given(categoryService.findCategoryById(1L)).willReturn(category1);
    given(templateFileService.updateFile(any(TemplateFile.class), any(MultipartFile.class)))
        .willReturn(mockFile);

    // when
    Template result = templateService.update(1L, request, userProfile, file);

    // then
    assertThat(result.getTitle()).isEqualTo("New Title");
    assertThat(result.getPrice()).isEqualTo(200);
  }

  @Test
  @DisplayName("템플릿 삭제 성공")
  void deleteTemplate_success() {
    // given
    Template template = createTemplate(1L, "템플릿1", category1, LocalDateTime.now(), null, null);
    TemplateFile mockFile = mock(TemplateFile.class);
    template.setTemplateFile(mockFile);

    given(templateRepository.findById(1L)).willReturn(Optional.of(template));
    doNothing().when(templateFileService).deleteFile(any());

    // when
    templateService.delete(1L, userProfile);

    // then
    verify(templateRepository).delete(template);
    verify(templateFileService).deleteFile(mockFile);
  }

  @Test
  @DisplayName("템플릿 조회 - latest 첫 페이지: nextCursor는 null")
  void getTemplates_latest_firstPage_noNextCursor() {
    // given
    int limit = 10;
    int fetchSize = limit + 1;
    LocalDateTime now = LocalDateTime.of(2025, 8, 22, 12, 0);
    List<Template> templates =
        List.of(
            createTemplate(10L, "T1", category1, now, null, null),
            createTemplate(9L, "T2", category2, now.minusMinutes(1), null, null));

    given(
            templateRepository.findScrollTemplates(
                eq(TemplateSortType.LATEST),
                eq(OrderType.DESC),
                isNull(),
                eq(fetchSize),
                isNull(),
                isNull(),
                isNull(),
                isNull()))
        .willReturn(templates);

    // when
    TemplateScrollResponse res =
        templateService.getTemplateList(null, "latest", "desc", null, limit);

    // then
    assertThat(res.templates()).hasSize(2);
    assertThat(res.nextCursor()).isNull(); // hasNext=false 이므로
    verify(templateRepository)
        .findScrollTemplates(
            eq(TemplateSortType.LATEST),
            eq(OrderType.DESC),
            isNull(),
            eq(fetchSize),
            isNull(),
            isNull(),
            isNull(),
            isNull());
  }

  @Test
  @DisplayName("템플릿 조회 - latest 다음 페이지 존재: nextCursor는 마지막 항목 ts|id")
  void getTemplates_latest_hasNext() {
    // given
    int limit = 1;
    int fetchSize = limit + 1;
    LocalDateTime ts1 = LocalDateTime.of(2025, 8, 22, 12, 0);
    LocalDateTime ts2 = ts1.minusMinutes(1);
    // 최신순 DESC로 반환되었다고 가정: ts1(id=10), ts2(id=9)
    List<Template> repoRows =
        List.of(
            createTemplate(10L, "T1", category1, ts1, null, null),
            createTemplate(9L, "T2", category2, ts2, null, null));

    given(
            templateRepository.findScrollTemplates(
                eq(TemplateSortType.LATEST),
                eq(OrderType.DESC),
                isNull(),
                eq(fetchSize),
                isNull(),
                isNull(),
                isNull(),
                isNull()))
        .willReturn(repoRows);

    // when
    TemplateScrollResponse res =
        templateService.getTemplateList(null, "latest", "desc", null, limit);

    // then (화면에는 1개만, nextCursor는 화면 마지막=ts1|10)
    assertThat(res.templates()).hasSize(1);
    assertThat(res.templates().get(0).id()).isEqualTo(10L);
    assertThat(decode(res.nextCursor())).isEqualTo("2025-08-22T12:00:00|10");
  }

  @Test
  @DisplayName("템플릿 조회 - rating 정렬 커서 포맷: rating|ts|id")
  void getTemplates_rating_hasNext_cursorFormat() {
    // given
    int limit = 1;
    int fetchSize = limit + 1;
    LocalDateTime ts = LocalDateTime.of(2025, 8, 22, 13, 0);

    List<Template> repoRows =
        List.of(
            createTemplate(100L, "T1", category1, ts, new BigDecimal("4.5"), null),
            createTemplate(99L, "T2", category2, ts.minusMinutes(1), new BigDecimal("4.0"), null));

    given(
            templateRepository.findScrollTemplates(
                eq(TemplateSortType.RATING),
                eq(OrderType.DESC),
                isNull(),
                eq(fetchSize),
                isNull(),
                isNull(),
                isNull(),
                isNull()))
        .willReturn(repoRows);

    // when
    TemplateScrollResponse res =
        templateService.getTemplateList(null, "rating", "desc", null, limit);

    // then
    assertThat(res.templates()).hasSize(1);
    assertThat(decode(res.nextCursor())).isEqualTo("4.5|2025-08-22T13:00:00|100");
  }

  @Test
  @DisplayName("템플릿 조회 - popular 정렬 커서 포맷: purchaseCount|ts|id")
  void getTemplates_popular_hasNext_cursorFormat() {
    // given
    int limit = 1;
    int fetchSize = limit + 1;
    LocalDateTime ts = LocalDateTime.of(2025, 8, 22, 14, 0);

    Template t1 = createTemplate(200L, "T1", category1, ts, null, 37L);
    Template t2 = createTemplate(199L, "T2", category2, ts.minusMinutes(1), null, 12L);
    List<Template> repoRows = List.of(t1, t2);

    given(
            templateRepository.findScrollTemplates(
                eq(TemplateSortType.POPULAR),
                eq(OrderType.DESC),
                isNull(),
                eq(fetchSize),
                isNull(),
                isNull(),
                isNull(),
                isNull()))
        .willReturn(repoRows);

    // when
    TemplateScrollResponse res =
        templateService.getTemplateList(null, "popular", "desc", null, limit);

    // then
    assertThat(res.templates()).hasSize(1);
    assertThat(decode(res.nextCursor())).isEqualTo("37|2025-08-22T14:00:00|200");
  }

  @Test
  @DisplayName("정렬값이 잘못되면 LATEST로 fallback")
  void getTemplates_invalidSort_fallbackToLatest() {
    // given
    int limit = 5;
    int fetchSize = limit + 1;
    given(
            templateRepository.findScrollTemplates(
                eq(TemplateSortType.LATEST),
                any(OrderType.class),
                any(),
                eq(fetchSize),
                any(),
                any(),
                any(),
                any()))
        .willReturn(List.of());

    // when
    TemplateScrollResponse res =
        templateService.getTemplateList(null, "weird", "desc", null, limit);

    // then
    assertThat(res.templates()).isEmpty();
    assertThat(res.nextCursor()).isNull();
    verify(templateRepository)
        .findScrollTemplates(
            eq(TemplateSortType.LATEST),
            any(OrderType.class),
            any(),
            eq(fetchSize),
            any(),
            any(),
            any(),
            any());
  }

  @Test
  @DisplayName("categoryId가 있으면 존재 검증 호출")
  void getTemplates_callsCategoryAssert_whenCategoryIdPresent() {
    // given
    int limit = 3;
    int fetchSize = limit + 1;
    Long categoryId = 1L;

    // 존재 검증은 void → doNothing
    doNothing().when(categoryService).assertCategoryExists(categoryId);
    given(
            templateRepository.findScrollTemplates(
                any(TemplateSortType.class),
                any(OrderType.class),
                eq(categoryId),
                eq(fetchSize),
                any(),
                any(),
                any(),
                any()))
        .willReturn(List.of());

    // when
    TemplateScrollResponse res =
        templateService.getTemplateList(null, "latest", "desc", categoryId, limit);

    // then
    verify(categoryService).assertCategoryExists(categoryId);
  }
}
