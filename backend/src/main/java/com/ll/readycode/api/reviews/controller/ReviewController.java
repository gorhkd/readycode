package com.ll.readycode.api.reviews.controller;

import com.ll.readycode.api.reviews.dto.request.ReviewCreateRequest;
import com.ll.readycode.api.reviews.dto.request.ReviewUpdateRequest;
import com.ll.readycode.api.reviews.dto.response.ReviewResponse;
import com.ll.readycode.api.reviews.dto.response.ReviewSummaryResponse;
import com.ll.readycode.domain.reviews.entity.Review;
import com.ll.readycode.domain.reviews.service.ReviewService;
import com.ll.readycode.global.common.auth.user.UserPrincipal;
import com.ll.readycode.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {
  private final ReviewService reviewService;

  @PostMapping("/{templateId}")
  @Operation(summary = "리뷰 생성", description = "특정 템플릿에 대한 리뷰를 생성합니다. 구매한 사용자만 작성 가능합니다.")
  public ResponseEntity<SuccessResponse<ReviewResponse>> createReview(
      @PathVariable Long templateId,
      @RequestBody @Valid ReviewCreateRequest request,
      @AuthenticationPrincipal UserPrincipal userPrincipal) {
    Review review = reviewService.createReview(templateId, userPrincipal.getUserProfile(), request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(SuccessResponse.of("리뷰가 생성되었습니다.", ReviewResponse.of(review)));
  }

  @GetMapping("/{templateId}/me")
  @Operation(summary = "리뷰 단건 조회", description = "내가 작성한 해당 템플릿의 리뷰를 조회합니다.")
  public ResponseEntity<SuccessResponse<ReviewResponse>> getReview(
      @PathVariable Long templateId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
    Review review = reviewService.getReview(templateId, userPrincipal.getUserProfile());
    return ResponseEntity.ok(SuccessResponse.of("리뷰 조회를 성공했습니다.", ReviewResponse.of(review)));
  }

  @GetMapping("/{templateId}")
  @Operation(summary = "리뷰 목록 조회", description = "해당 템플릿의 리뷰를 조회합니다.")
  public ResponseEntity<SuccessResponse<List<ReviewSummaryResponse>>> getReviewList(
      @PathVariable Long templateId) {
    List<ReviewSummaryResponse> list = reviewService.getReviewList(templateId);
    return ResponseEntity.ok(SuccessResponse.of(list));
  }

  @PatchMapping("/{templateId}/me")
  @Operation(summary = "리뷰 수정", description = "내가 작성한 해당 템플릿의 리뷰를 수정합니다.")
  public ResponseEntity<SuccessResponse<ReviewResponse>> updateReview(
      @PathVariable Long templateId,
      @RequestBody @Valid ReviewUpdateRequest request,
      @AuthenticationPrincipal UserPrincipal userPrincipal) {
    Review review = reviewService.updateReview(templateId, userPrincipal.getUserProfile(), request);
    return ResponseEntity.ok(SuccessResponse.of("리뷰가 수정되었습니다.", ReviewResponse.of(review)));
  }

  @DeleteMapping("/{templateId}/me")
  @Operation(summary = "리뷰 삭제", description = "내가 작성한 해당 템플릿의 리뷰를 삭제합니다.")
  public ResponseEntity<SuccessResponse<Long>> deleteReview(
      @PathVariable Long templateId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
    Long deletedId = reviewService.deleteReview(templateId, userPrincipal.getUserProfile());
    return ResponseEntity.ok(SuccessResponse.of("리뷰가 삭제되었습니다.", deletedId));
  }
}
