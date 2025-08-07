package com.ll.readycode.api.reviews.controller;

import com.ll.readycode.api.reviews.dto.request.ReviewCreateRequest;
import com.ll.readycode.domain.reviews.service.ReviewService;
import com.ll.readycode.global.common.auth.user.UserPrincipal;
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
  public ResponseEntity<Void> createReview(
      @PathVariable Long templateId,
      @RequestBody @Valid ReviewCreateRequest request,
      @AuthenticationPrincipal UserPrincipal userPrincipal) {
    reviewService.createReview(templateId, userPrincipal.getUserProfile(), request);
    return ResponseEntity.ok().build();
  }
}
