package com.ll.readycode.domain.templates;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.ll.readycode.domain.templates.purchases.entity.TemplatePurchase;
import com.ll.readycode.domain.templates.purchases.repository.TemplatePurchaseRepository;
import com.ll.readycode.domain.templates.purchases.service.TemplatePurchaseService;
import com.ll.readycode.domain.templates.templates.entity.Template;
import com.ll.readycode.domain.templates.templates.service.TemplateService;
import com.ll.readycode.domain.users.userprofiles.entity.UserProfile;
import com.ll.readycode.domain.users.userprofiles.entity.UserPurpose;
import com.ll.readycode.domain.users.userprofiles.entity.UserRole;
import com.ll.readycode.global.exception.CustomException;
import com.ll.readycode.global.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class TemplatePurchaseServiceTest {

  @InjectMocks private TemplatePurchaseService templatePurchaseService;

  @Mock private TemplatePurchaseRepository templatePurchaseRepository;
  @Mock private TemplateService templateService;

  private final Long userId = 1L;
  private final Long templateId = 10L;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  private UserProfile mockUser() {
    return UserProfile.builder()
        .nickname("abc")
        .phoneNumber("010")
        .role(UserRole.USER)
        .purpose(UserPurpose.LEARNING)
        .build();
  }

  private Template mockTemplate(int price) {
    return Template.builder().id(templateId).price(price).build();
  }

  private TemplatePurchase mockPurchase(Template template) {
    return TemplatePurchase.builder()
        .buyer(mockUser())
        .template(template)
        .price(template != null ? template.getPrice() : 0)
        .build();
  }

  @Test
  @DisplayName("무료 템플릿 구매 성공")
  void purchaseFreeTemplate_success() {
    // given
    UserProfile user = mockUser();
    Template template = mockTemplate(0);

    given(templatePurchaseRepository.existsByBuyerIdAndTemplateId(userId, templateId))
        .willReturn(false);
    given(templateService.findTemplateById(templateId)).willReturn(template);
    given(templatePurchaseRepository.save(any())).willReturn(null);

    // when & then
    assertThatCode(() -> templatePurchaseService.purchaseFreeTemplate(user, templateId))
        .doesNotThrowAnyException();
  }

  // TODO: UserProfile에 @SuperBuilder 적용되면 다시 테스트 활성화
  /*  @Test
  @DisplayName("이미 구매한 템플릿 예외처리")
  void purchaseFreeTemplate_fails_whenAlreadyPurchased() {
    given(templatePurchaseRepository.existsByBuyerIdAndTemplateId(userId, templateId))
        .willReturn(true);

    CustomException ex =
        assertThrows(
            CustomException.class,
            () -> templatePurchaseService.purchaseFreeTemplate(mockUser(), templateId));
    assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.ALREADY_PURCHASED);
  }*/

  @Test
  @DisplayName("유료 템플릿을 무료 구매 API 구매시 예외처리")
  void purchaseFreeTemplate_fails_whenTemplateIsNotFree() {
    Template template = mockTemplate(100);
    given(templatePurchaseRepository.existsByBuyerIdAndTemplateId(userId, templateId))
        .willReturn(false);
    given(templateService.findTemplateById(templateId)).willReturn(template);

    CustomException ex =
        assertThrows(
            CustomException.class,
            () -> templatePurchaseService.purchaseFreeTemplate(mockUser(), templateId));
    assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.NOT_FREE_TEMPLATE);
  }

  // TODO: UserProfile에 @SuperBuilder 적용되면 다시 테스트 활성화
  /*  @Test
  @DisplayName("구매한 템플릿 중 삭제된 템플릿 응답에서 제외")
  void getPurchasedTemplates_filtersOutNullTemplates() {
    Template validTemplate = mockTemplate(0);
    TemplatePurchase validPurchase = mockPurchase(validTemplate);
    TemplatePurchase nullTemplatePurchase = mockPurchase(null);

    given(templatePurchaseRepository.findByBuyerIdWithTemplate(userId))
        .willReturn(List.of(validPurchase, nullTemplatePurchase));

    List<PurchasedTemplateResponse> result = templatePurchaseService.getPurchasedTemplates(userId);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).id()).isEqualTo(validTemplate.getId());
  }*/

  @Test
  @DisplayName("특정 템플릿의 구매 여부 확인")
  void existsByBuyerIdAndTemplateId_returnsTrue_whenPurchased() {
    given(templatePurchaseRepository.existsByBuyerIdAndTemplateId(userId, templateId))
        .willReturn(true);

    boolean result = templatePurchaseService.existsByBuyerIdAndTemplateId(userId, templateId);

    assertThat(result).isTrue();
  }
}
