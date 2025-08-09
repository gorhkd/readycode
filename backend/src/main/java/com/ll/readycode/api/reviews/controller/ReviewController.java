package com.ll.readycode.api.reviews.controller;

import com.ll.readycode.api.reviews.dto.request.ReviewCreateRequest;
import com.ll.readycode.api.reviews.dto.request.ReviewUpdateRequest;
import com.ll.readycode.domain.reviews.service.ReviewService;
import com.ll.readycode.global.common.auth.user.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
  public ResponseEntity<Void> createReview(
      @PathVariable Long templateId,
      @RequestBody @Valid ReviewCreateRequest request,
      @AuthenticationPrincipal UserPrincipal userPrincipal) {
    reviewService.createReview(templateId, userPrincipal.getUserProfile(), request);
    return ResponseEntity.ok().build();
  }

  @PatchMapping("/{templateId}")
  @Operation(summary = "리뷰 수정", description = "내가 작성한 해당 템플릿의 리뷰를 수정합니다.")
  public ResponseEntity<Void> modifyReview(
      @PathVariable Long templateId,
      @RequestBody @Valid ReviewUpdateRequest request,
      @AuthenticationPrincipal UserPrincipal userPrincipal) {
    reviewService.updateReview(templateId, userPrincipal.getUserProfile(), request);
    return ResponseEntity.ok().build();
  }
}
