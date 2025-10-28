package com.ll.readycode.domain.admin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.ll.readycode.api.admin.dto.response.AdminResponseDto.TemplateDetails;
import com.ll.readycode.api.admin.dto.response.AdminResponseDto.TemplateDownloadDetails;
import com.ll.readycode.api.admin.dto.response.AdminResponseDto.UserProfileDetails;
import com.ll.readycode.domain.admin.entity.AdminSortType;
import com.ll.readycode.domain.admin.service.AdminService;
import com.ll.readycode.domain.categories.entity.Category;
import com.ll.readycode.domain.templates.downloads.repository.TemplateDownloadRepository;
import com.ll.readycode.domain.templates.templates.entity.Template;
import com.ll.readycode.domain.templates.templates.repository.TemplateRepository;
import com.ll.readycode.domain.users.userprofiles.entity.UserProfile;
import com.ll.readycode.domain.users.userprofiles.entity.UserPurpose;
import com.ll.readycode.domain.users.userprofiles.entity.UserRole;
import com.ll.readycode.domain.users.userprofiles.repository.UserProfileRepository;
import com.ll.readycode.global.common.pagination.CursorPage;
import com.ll.readycode.global.common.types.OrderType;
import com.ll.readycode.global.common.util.EncodeHelper;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

  @InjectMocks private AdminService adminService;

  @Mock private UserProfileRepository userProfileRepository;

  @Mock private TemplateDownloadRepository templateDownloadRepository;

  @Mock private TemplateRepository templateRepository;

  private UserProfile createUserProfile(Long id, String nickname, String phoneNumber) {
    return UserProfile.builder()
        .id(id)
        .nickname(nickname)
        .phoneNumber(phoneNumber)
        .role(UserRole.USER)
        .purpose(UserPurpose.LEARNING)
        .build();
  }

  private Template createTemplate(
      Long id, String title, Long purchaseCount, BigDecimal avgRating, Long reviewCount) {
    Category category = Category.builder().id(1L).name("백엔드").build();
    UserProfile seller = createUserProfile(1L, "seller", "010-1234-5678");

    return Template.builder()
        .id(id)
        .title(title)
        .description("설명")
        .price(1000)
        .image("image.png")
        .category(category)
        .seller(seller)
        .purchaseCount(purchaseCount)
        .avgRating(avgRating)
        .reviewCount(reviewCount)
        .build();
  }

  @Test
  @DisplayName("회원 목록 조회 성공 - 첫 페이지")
  void getUserProfiles_firstPage_success() {
    // given
    UserProfile user1 = createUserProfile(1L, "user1", "010-1111-1111");
    UserProfile user2 = createUserProfile(2L, "user2", "010-2222-2222");

    given(
            userProfileRepository.findAllByRoleWithCursor(
                eq(UserRole.USER), eq(11), isNull(), isNull(), eq(OrderType.DESC)))
        .willReturn(List.of(user1, user2));

    // when
    CursorPage<UserProfileDetails> result =
        adminService.getUserProfilesWithSocialInfo(10, null, null, "desc");

    // then
    assertThat(result.items()).hasSize(2);
    assertThat(result.hasNext()).isFalse();
    assertThat(result.nextCursor()).isNull();
    assertThat(EncodeHelper.decode(result.items().get(0).userId(), EncodeHelper.ENCODING_TYPE_LONG))
        .isEqualTo(1L);
    assertThat(result.items().get(0).nickname()).isEqualTo("user1");
  }

  @Test
  @DisplayName("회원 목록 조회 성공 - 다음 페이지 존재")
  void getUserProfiles_hasNext_success() {
    // given
    List<UserProfile> users =
        List.of(
            createUserProfile(1L, "user1", "010-1111-1111"),
            createUserProfile(2L, "user2", "010-2222-2222"),
            createUserProfile(3L, "user3", "010-3333-3333"));

    given(
            userProfileRepository.findAllByRoleWithCursor(
                eq(UserRole.USER), eq(3), isNull(), isNull(), eq(OrderType.DESC)))
        .willReturn(users);

    // when
    CursorPage<UserProfileDetails> result =
        adminService.getUserProfilesWithSocialInfo(2, null, null, "desc");

    // then
    assertThat(result.items()).hasSize(2);
    assertThat(result.hasNext()).isTrue();
    assertThat(result.nextCursor()).isNotNull();
    assertThat(EncodeHelper.decode(result.items().get(0).userId(), EncodeHelper.ENCODING_TYPE_LONG))
        .isEqualTo(1L);
    assertThat(EncodeHelper.decode(result.items().get(1).userId(), EncodeHelper.ENCODING_TYPE_LONG))
        .isEqualTo(2L);
  }

  @Test
  @DisplayName("회원 목록 조회 - 커서 기반 페이징")
  void getUserProfiles_withCursor_success() {
    // given
    Long cursor = 5L;
    UserProfile user1 = createUserProfile(6L, "user6", "010-6666-6666");

    given(
            userProfileRepository.findAllByRoleWithCursor(
                eq(UserRole.USER), eq(11), eq(cursor), isNull(), eq(OrderType.DESC)))
        .willReturn(List.of(user1));

    // when
    String encodedCursor = java.util.Base64.getEncoder().encodeToString("5".getBytes());
    CursorPage<UserProfileDetails> result =
        adminService.getUserProfilesWithSocialInfo(10, encodedCursor, null, "desc");

    // then
    assertThat(result.items()).hasSize(1);
    verify(userProfileRepository)
        .findAllByRoleWithCursor(
            eq(UserRole.USER), eq(11), eq(cursor), isNull(), eq(OrderType.DESC));
  }

  @Test
  @DisplayName("회원 목록 조회 - 키워드 검색")
  void getUserProfiles_withKeyword_success() {
    // given
    String keyword = "user1";
    UserProfile user1 = createUserProfile(1L, "user1", "010-1111-1111");

    given(
            userProfileRepository.findAllByRoleWithCursor(
                eq(UserRole.USER), eq(11), isNull(), eq(keyword), eq(OrderType.DESC)))
        .willReturn(List.of(user1));

    // when
    CursorPage<UserProfileDetails> result =
        adminService.getUserProfilesWithSocialInfo(10, null, keyword, "desc");

    // then
    assertThat(result.items()).hasSize(1);
    assertThat(result.items().get(0).nickname()).isEqualTo("user1");
    verify(userProfileRepository)
        .findAllByRoleWithCursor(
            eq(UserRole.USER), eq(11), isNull(), eq(keyword), eq(OrderType.DESC));
  }

  @Test
  @DisplayName("템플릿 다운로드 통계 조회 성공")
  void getTemplateDownloadStatistics_success() {
    // given
    LocalDateTime startDate = LocalDateTime.of(2025, 1, 1, 0, 0);
    LocalDateTime endDate = LocalDateTime.of(2025, 1, 31, 23, 59);
    Long templateId = 1L;

    List<TemplateDownloadDetails> mockData =
        List.of(mock(TemplateDownloadDetails.class), mock(TemplateDownloadDetails.class));

    given(
            templateDownloadRepository.findTemplatesForDownloadStatistics(
                startDate, endDate, templateId))
        .willReturn(mockData);

    // when
    List<TemplateDownloadDetails> result =
        adminService.getTemplateDownloadStatistics(startDate, endDate, templateId);

    // then
    assertThat(result).hasSize(2);
    verify(templateDownloadRepository)
        .findTemplatesForDownloadStatistics(startDate, endDate, templateId);
  }

  @Test
  @DisplayName("템플릿 다운로드 통계 조회 - templateId 없이")
  void getTemplateDownloadStatistics_withoutTemplateId_success() {
    // given
    LocalDateTime startDate = LocalDateTime.of(2025, 1, 1, 0, 0);
    LocalDateTime endDate = LocalDateTime.of(2025, 1, 31, 23, 59);

    List<TemplateDownloadDetails> mockData =
        List.of(mock(TemplateDownloadDetails.class), mock(TemplateDownloadDetails.class));

    given(templateDownloadRepository.findTemplatesForDownloadStatistics(startDate, endDate, null))
        .willReturn(mockData);

    // when
    List<TemplateDownloadDetails> result =
        adminService.getTemplateDownloadStatistics(startDate, endDate, null);

    // then
    assertThat(result).hasSize(2);
    verify(templateDownloadRepository).findTemplatesForDownloadStatistics(startDate, endDate, null);
  }

  @Test
  @DisplayName("템플릿 통계 조회 성공 - 첫 페이지")
  void getTemplateStatistics_firstPage_success() {
    // given
    Template template1 = createTemplate(1L, "템플릿1", 100L, new BigDecimal("4.5"), 20L);
    Template template2 = createTemplate(2L, "템플릿2", 80L, new BigDecimal("4.0"), 15L);

    given(
            templateRepository.findTemplateStatisticsWithCursor(
                eq(11), isNull(), eq(AdminSortType.CREATED), eq(OrderType.DESC)))
        .willReturn(List.of(template1, template2));

    // when
    CursorPage<TemplateDetails> result =
        adminService.getTemplateStatistics(10, null, "created", "desc");

    // then
    assertThat(result.items()).hasSize(2);
    assertThat(result.hasNext()).isFalse();
    assertThat(result.nextCursor()).isNull();
    assertThat(
            EncodeHelper.decode(
                result.items().get(0).templateId(), EncodeHelper.ENCODING_TYPE_LONG))
        .isEqualTo(1L);
    assertThat(result.items().get(0).templateTitle()).isEqualTo("템플릿1");
  }

  @Test
  @DisplayName("템플릿 통계 조회 성공 - 다음 페이지 존재")
  void getTemplateStatistics_hasNext_success() {
    // given
    List<Template> templates =
        List.of(
            createTemplate(1L, "템플릿1", 100L, new BigDecimal("4.5"), 20L),
            createTemplate(2L, "템플릿2", 80L, new BigDecimal("4.0"), 15L),
            createTemplate(3L, "템플릿3", 60L, new BigDecimal("3.5"), 10L));

    given(
            templateRepository.findTemplateStatisticsWithCursor(
                eq(3), isNull(), eq(AdminSortType.CREATED), eq(OrderType.DESC)))
        .willReturn(templates);

    // when
    CursorPage<TemplateDetails> result =
        adminService.getTemplateStatistics(2, null, "created", "desc");

    // then
    assertThat(result.items()).hasSize(2);
    assertThat(result.hasNext()).isTrue();
    assertThat(result.nextCursor()).isNotNull();
    assertThat(
            EncodeHelper.decode(
                result.items().get(0).templateId(), EncodeHelper.ENCODING_TYPE_LONG))
        .isEqualTo(1L);
    assertThat(
            EncodeHelper.decode(
                result.items().get(1).templateId(), EncodeHelper.ENCODING_TYPE_LONG))
        .isEqualTo(2L);
  }

  @Test
  @DisplayName("템플릿 통계 조회 - 다운로드 수 정렬")
  void getTemplateStatistics_sortByDownloadCount_success() {
    // given
    Template template1 = createTemplate(1L, "템플릿1", 100L, new BigDecimal("4.5"), 20L);

    given(
            templateRepository.findTemplateStatisticsWithCursor(
                eq(11), isNull(), eq(AdminSortType.DOWNLOAD), eq(OrderType.DESC)))
        .willReturn(List.of(template1));

    // when
    CursorPage<TemplateDetails> result =
        adminService.getTemplateStatistics(10, null, "download", "desc");

    // then
    assertThat(result.items()).hasSize(1);
    verify(templateRepository)
        .findTemplateStatisticsWithCursor(
            eq(11), isNull(), eq(AdminSortType.DOWNLOAD), eq(OrderType.DESC));
  }

  @Test
  @DisplayName("템플릿 통계 조회 - 좋아요 수 정렬")
  void getTemplateStatistics_sortByAvgRating_success() {
    // given
    Template template1 = createTemplate(1L, "템플릿1", 100L, new BigDecimal("4.5"), 20L);

    given(
            templateRepository.findTemplateStatisticsWithCursor(
                eq(11), isNull(), eq(AdminSortType.LIKE), eq(OrderType.DESC)))
        .willReturn(List.of(template1));

    // when
    CursorPage<TemplateDetails> result =
        adminService.getTemplateStatistics(10, null, "like", "desc");

    // then
    assertThat(result.items()).hasSize(1);
    verify(templateRepository)
        .findTemplateStatisticsWithCursor(
            eq(11), isNull(), eq(AdminSortType.LIKE), eq(OrderType.DESC));
  }

  @Test
  @DisplayName("템플릿 통계 조회 - 리뷰 수 정렬")
  void getTemplateStatistics_sortByReviewCount_success() {
    // given
    Template template1 = createTemplate(1L, "템플릿1", 100L, new BigDecimal("4.5"), 20L);

    given(
            templateRepository.findTemplateStatisticsWithCursor(
                eq(11), isNull(), eq(AdminSortType.REVIEW), eq(OrderType.DESC)))
        .willReturn(List.of(template1));

    // when
    CursorPage<TemplateDetails> result =
        adminService.getTemplateStatistics(10, null, "review", "desc");

    // then
    assertThat(result.items()).hasSize(1);
    verify(templateRepository)
        .findTemplateStatisticsWithCursor(
            eq(11), isNull(), eq(AdminSortType.REVIEW), eq(OrderType.DESC));
  }

  @Test
  @DisplayName("템플릿 통계 조회 - 오름차순 정렬")
  void getTemplateStatistics_orderByAsc_success() {
    // given
    Template template1 = createTemplate(1L, "템플릿1", 10L, new BigDecimal("3.0"), 5L);

    given(
            templateRepository.findTemplateStatisticsWithCursor(
                eq(11), isNull(), eq(AdminSortType.CREATED), eq(OrderType.ASC)))
        .willReturn(List.of(template1));

    // when
    CursorPage<TemplateDetails> result =
        adminService.getTemplateStatistics(10, null, "created", "asc");

    // then
    assertThat(result.items()).hasSize(1);
    verify(templateRepository)
        .findTemplateStatisticsWithCursor(
            eq(11), isNull(), eq(AdminSortType.CREATED), eq(OrderType.ASC));
  }

  @Test
  @DisplayName("템플릿 통계 조회 - 커서 기반 페이징")
  void getTemplateStatistics_withCursor_success() {
    // given
    Long cursor = 5L;
    Template template1 = createTemplate(6L, "템플릿6", 50L, new BigDecimal("3.5"), 10L);

    given(
            templateRepository.findTemplateStatisticsWithCursor(
                eq(11), eq(cursor), eq(AdminSortType.CREATED), eq(OrderType.DESC)))
        .willReturn(List.of(template1));

    // when
    String encodedCursor = java.util.Base64.getEncoder().encodeToString("5".getBytes());
    CursorPage<TemplateDetails> result =
        adminService.getTemplateStatistics(10, encodedCursor, "created", "desc");

    // then
    assertThat(result.items()).hasSize(1);
    verify(templateRepository)
        .findTemplateStatisticsWithCursor(
            eq(11), eq(cursor), eq(AdminSortType.CREATED), eq(OrderType.DESC));
  }
}
