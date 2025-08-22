package com.ll.readycode.domain.reviews;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import com.ll.readycode.api.reviews.dto.request.ReviewCreateRequest;
import com.ll.readycode.api.reviews.dto.request.ReviewUpdateRequest;
import com.ll.readycode.api.reviews.dto.response.CursorPage;
import com.ll.readycode.api.reviews.dto.response.ReviewResponse;
import com.ll.readycode.api.reviews.dto.response.ReviewSummaryResponse;
import com.ll.readycode.domain.reviews.entity.Review;
import com.ll.readycode.domain.reviews.query.ReviewSortType;
import com.ll.readycode.domain.reviews.reader.ReviewReader;
import com.ll.readycode.domain.reviews.repository.ReviewRepository;
import com.ll.readycode.domain.reviews.service.ReviewService;
import com.ll.readycode.domain.templates.purchases.service.TemplatePurchaseService;
import com.ll.readycode.domain.templates.templates.entity.Template;
import com.ll.readycode.domain.templates.templates.service.TemplateService;
import com.ll.readycode.domain.users.userprofiles.entity.UserProfile;
import com.ll.readycode.domain.users.userprofiles.entity.UserPurpose;
import com.ll.readycode.domain.users.userprofiles.entity.UserRole;
import com.ll.readycode.global.common.types.OrderType;
import com.ll.readycode.global.exception.CustomException;
import com.ll.readycode.global.exception.ErrorCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

  @InjectMocks private ReviewService reviewService;

  @Mock private ReviewRepository reviewRepository;
  @Mock private ReviewReader reviewReader;
  @Mock private TemplateService templateService;
  @Mock private TemplatePurchaseService templatePurchaseService;

  private static UserProfile user(long id) {
    return UserProfile.builder()
        .id(id)
        .nickname("user-" + id)
        .phoneNumber("010-0000-0000")
        .role(UserRole.USER)
        .purpose(UserPurpose.LEARNING)
        .build();
  }

  private static Template template(long id) {
    Template real =
        Template.builder().id(id).title("t-" + id).description("d-" + id).price(0).build();

    ReflectionTestUtils.setField(real, "id", id);
    return spy(real);
  }

  private static Review review(
      long id, Template t, UserProfile u, BigDecimal rating, LocalDateTime createdAt) {
    Review r =
        Review.builder()
            .id(id)
            .template(t)
            .userProfile(u)
            .rating(rating)
            .content("content-" + id)
            .build();
    // createdAt은 BaseEntity에 있을 가능성 → 스텁이 필요하면 스파이나 목으로 대체
    return r;
  }

  @Test
  @DisplayName("리뷰 생성 성공: 구매 확인 + 중복 리뷰 검증 + 템플릿 집계 갱신")
  void createReview_success() {
    // given
    long templateId = 10L;
    UserProfile u = user(1L);
    Template t = template(templateId);

    ReviewCreateRequest req = new ReviewCreateRequest("굿", new BigDecimal("4.5"));

    given(templateService.findTemplateById(templateId)).willReturn(t);
    willDoNothing().given(templatePurchaseService).throwIfNotPurchased(u.getId(), templateId);
    given(reviewReader.existsByUserAndTemplate(u.getId(), templateId)).willReturn(false);

    // save 시 아이디 채워서 반환
    given(reviewRepository.save(any(Review.class)))
        .willAnswer(
            inv -> {
              Review r = inv.getArgument(0);
              return Review.builder()
                  .id(100L)
                  .template(r.getTemplate())
                  .userProfile(r.getUserProfile())
                  .rating(r.getRating())
                  .content(r.getContent())
                  .build();
            });

    // when
    ReviewResponse resp = reviewService.createReview(templateId, u, req);

    // then
    assertThat(resp).isNotNull();
    assertThat(resp.getReviewId()).isEqualTo(100L);
    assertThat(resp.getTemplateId()).isEqualTo(templateId);
    assertThat(resp.getUserProfileId()).isEqualTo(u.getId());
    assertThat(resp.getRating())
        .isEqualTo(new BigDecimal("4.5").setScale(1, java.math.RoundingMode.DOWN));
  }

  @Test
  @DisplayName("리뷰 생성 실패: 동일 템플릿에 이미 리뷰가 있으면 예외")
  void createReview_fail_whenAlreadyReviewed() {
    long templateId = 10L;
    UserProfile u = user(1L);
    Template t = template(templateId);
    ReviewCreateRequest req = new ReviewCreateRequest("굿", new BigDecimal("4.0"));

    given(templateService.findTemplateById(templateId)).willReturn(t);
    willDoNothing().given(templatePurchaseService).throwIfNotPurchased(u.getId(), templateId);
    given(reviewReader.existsByUserAndTemplate(u.getId(), templateId)).willReturn(true);

    assertThatThrownBy(() -> reviewService.createReview(templateId, u, req))
        .isInstanceOf(CustomException.class)
        .matches(ex -> ((CustomException) ex).getErrorCode() == ErrorCode.ALREADY_REVIEWED);

    verify(reviewRepository, never()).save(any());
    verify(t, never()).addReview(any());
  }

  @Test
  @DisplayName("리뷰 단건 조회 성공: 사용자+템플릿 기준")
  void getReview_success() {
    long templateId = 10L;
    UserProfile u = user(1L);
    Template t = template(templateId);
    Review r = review(100L, t, u, new BigDecimal("3.5"), LocalDateTime.now());

    given(reviewReader.getByUserAndTemplate(u.getId(), templateId)).willReturn(r);

    ReviewResponse resp = reviewService.getReview(templateId, u);

    assertThat(resp.getReviewId()).isEqualTo(100L);
    assertThat(resp.getTemplateId()).isEqualTo(templateId);
    assertThat(resp.getUserProfileId()).isEqualTo(u.getId());
    assertThat(resp.getRating())
        .isEqualTo(new BigDecimal("3.5").setScale(1, java.math.RoundingMode.DOWN));
  }

  @Test
  @DisplayName("리뷰 수정 성공: 템플릿 집계 updateReview 호출")
  void updateReview_success() {
    long templateId = 10L;
    UserProfile u = user(1L);
    Template t = template(templateId);

    Review existing = spy(review(100L, t, u, new BigDecimal("4.0"), LocalDateTime.now()));
    given(reviewReader.getByUserAndTemplate(u.getId(), templateId)).willReturn(existing);
    given(templateService.findTemplateById(templateId)).willReturn(t);

    ReviewUpdateRequest req = new ReviewUpdateRequest("수정", new BigDecimal("3.0"));

    ReviewResponse resp = reviewService.updateReview(templateId, u, req);

    assertThat(resp.getReviewId()).isEqualTo(100L);
    assertThat(existing.getContent()).isEqualTo("수정");
    assertThat(existing.getRating()).isEqualTo(new BigDecimal("3.0"));

    verify(t, times(1)).updateReview(new BigDecimal("4.0"), new BigDecimal("3.0"));
  }

  @Test
  @DisplayName("리뷰 삭제 성공: 템플릿 집계 removeReview 호출 + 레포 delete")
  void deleteReview_success() {
    long templateId = 10L;
    UserProfile u = user(1L);
    Template t = template(templateId);

    Review r = review(100L, t, u, new BigDecimal("2.5"), LocalDateTime.now());
    given(reviewReader.getByUserAndTemplate(u.getId(), templateId)).willReturn(r);
    given(templateService.findTemplateById(templateId)).willReturn(t);

    Long deletedId = reviewService.deleteReview(templateId, u);

    assertThat(deletedId).isEqualTo(100L);
    verify(t, times(1)).removeReview(new BigDecimal("2.5"));
    verify(reviewRepository, times(1)).delete(r);
  }

  @Test
  @DisplayName("리뷰 목록 조회: hasNext 계산 및 커서 인코딩(RATING 정렬)")
  void getReviewList_withCursor_rating() {
    long templateId = 10L;
    ReviewSortType reviewSortType = ReviewSortType.RATING;
    OrderType orderType = OrderType.DESC;
    int pageSize = 2;

    Template t = template(templateId);
    UserProfile u = user(1L);

    Review r1 = review(101L, t, u, new BigDecimal("4.5"), LocalDateTime.now().minusMinutes(1));
    Review r2 = review(102L, t, u, new BigDecimal("4.0"), LocalDateTime.now().minusMinutes(2));
    Review r3 =
        review(103L, t, u, new BigDecimal("3.5"), LocalDateTime.now().minusMinutes(3)); // 초과분

    given(
            reviewReader.findByTemplateWithCursor(
                eq(templateId), isNull(), eq(pageSize + 1), eq(reviewSortType), eq(orderType)))
        .willReturn(List.of(r1, r2, r3));

    CursorPage<ReviewSummaryResponse> page =
        reviewService.getReviewList(templateId, null, pageSize, "RATING", "DESC");

    assertThat(page.hasNext()).isTrue();
    assertThat(page.items()).hasSize(2);
    // nextCursor 형식: rating|id
    assertThat(page.nextCursor()).isEqualTo(r2.getRating().toPlainString() + "|" + r2.getId());
  }

  @Test
  @DisplayName("리뷰 목록 조회: 마지막 페이지면 hasNext=false, nextCursor=null")
  void getReviewList_lastPage() {
    long templateId = 10L;
    int pageSize = 2;

    Template t = template(templateId);
    UserProfile u = user(1L);
    Review r1 = review(201L, t, u, new BigDecimal("5.0"), LocalDateTime.now().minusMinutes(1));
    Review r2 = review(202L, t, u, new BigDecimal("4.5"), LocalDateTime.now().minusMinutes(2));

    given(
            reviewReader.findByTemplateWithCursor(
                eq(templateId),
                isNull(),
                eq(pageSize + 1),
                eq(ReviewSortType.RATING),
                eq(OrderType.DESC)))
        .willReturn(List.of(r1, r2)); // 초과분 없음

    CursorPage<ReviewSummaryResponse> page =
        reviewService.getReviewList(templateId, null, pageSize, "RATING", "DESC");

    assertThat(page.hasNext()).isFalse();
    assertThat(page.nextCursor()).isNull();
    assertThat(page.items()).hasSize(2);
  }
}
