package com.ll.readycode.api.templates.controller;

import com.ll.readycode.api.templates.dto.response.PurchasedTemplateResponse;
import com.ll.readycode.domain.templates.purchases.service.TemplatePurchaseService;
import com.ll.readycode.global.common.auth.user.UserPrincipal;
import com.ll.readycode.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

  @GetMapping
  @Operation(summary = "마이페이지 구매 내역 조회", description = "사용자가 구매한 템플릿 목록을 조회합니다.")
  public ResponseEntity<SuccessResponse<List<PurchasedTemplateResponse>>> getMyPurchases(
      @AuthenticationPrincipal UserPrincipal userPrincipal) {

    List<PurchasedTemplateResponse> responses =
        templatePurchaseService.getPurchasedTemplates(userPrincipal.getUserProfile().getId());

    return ResponseEntity.ok(SuccessResponse.of("구매 내역 목록을 성공적으로 조회했습니다.", responses));
  }
}
