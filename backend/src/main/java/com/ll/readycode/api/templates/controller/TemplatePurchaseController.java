package com.ll.readycode.api.templates.controller;

import com.ll.readycode.domain.templates.purchases.service.TemplatePurchaseService;
import com.ll.readycode.global.common.auth.user.UserPrincipal;
import com.ll.readycode.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/purchases")
public class TemplatePurchaseController {

  private final TemplatePurchaseService templatePurchaseService;

  @PostMapping("/{templateId}")
  @Operation(summary = "템플릿 구매 (무료 담기)", description = "무료 템플릿을 구매 내역에 추가합니다.")
  public ResponseEntity<SuccessResponse<Void>> purchaseTemplate(
      @PathVariable Long templateId, @AuthenticationPrincipal UserPrincipal userPrincipal) {

    templatePurchaseService.purchaseFreeTemplate(userPrincipal.getUserProfile(), templateId);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(SuccessResponse.of("템플릿이 구매 내역에 담겼습니다.", null));
  }
}
