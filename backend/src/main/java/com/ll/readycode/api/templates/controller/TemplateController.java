package com.ll.readycode.api.templates.controller;

import com.ll.readycode.api.templates.dto.request.TemplateCreateRequest;
import com.ll.readycode.api.templates.dto.request.TemplateUpdateRequest;
import com.ll.readycode.api.templates.dto.response.TemplateDetailResponse;
import com.ll.readycode.api.templates.dto.response.TemplateResponse;
import com.ll.readycode.api.templates.dto.response.TemplateScrollResponse;
import com.ll.readycode.domain.templates.downloads.service.TemplateDownloadService;
import com.ll.readycode.domain.templates.purchases.service.TemplatePurchaseService;
import com.ll.readycode.domain.templates.templates.entity.Template;
import com.ll.readycode.domain.templates.templates.service.TemplateService;
import com.ll.readycode.global.common.auth.user.UserPrincipal;
import com.ll.readycode.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "템플릿 API", description = "템플릿 조회, 생성, 수정, 삭제 등의 기능을 제공합니다.")
@RequestMapping("/api/templates")
@RequiredArgsConstructor
@RestController
public class TemplateController {
  private final TemplateService templateService;
  private final TemplatePurchaseService templatePurchaseService;
  private final TemplateDownloadService templateDownloadService;

  @Operation(
      summary = "템플릿 목록 조회",
      description = "정렬(latest|rating|popular), 카테고리, 커서 기반 페이징으로 템플릿을 조회합니다.")
  @GetMapping
  public ResponseEntity<SuccessResponse<TemplateScrollResponse>> getTemplates(
      @RequestParam(required = false) String cursor,
      @RequestParam(defaultValue = "latest") String sort,
      @RequestParam(defaultValue = "desc") String order,
      @RequestParam(required = false) Long categoryId,
      @RequestParam(defaultValue = "10") @Min(1) @Max(50) Integer limit) {
    TemplateScrollResponse response =
        templateService.getTemplateList(cursor, sort, order, categoryId, limit);
    return ResponseEntity.ok(SuccessResponse.of("템플릿 목록을 성공적으로 조회했습니다.", response));
  }

  @Operation(summary = "템플릿 상세 조회", description = "템플릿 ID를 기준으로 상세 정보를 조회합니다.")
  @GetMapping("/{templateId}")
  public ResponseEntity<SuccessResponse<TemplateDetailResponse>> detailsTemplate(
      @PathVariable Long templateId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
    Template template = templateService.findTemplateWithCategoryById(templateId);

    boolean purchased = false;
    if (userPrincipal != null) {
      purchased =
          templatePurchaseService.existsByBuyerIdAndTemplateId(
              userPrincipal.getUserProfile().getId(), templateId);
    }

    TemplateDetailResponse response = TemplateDetailResponse.of(template, purchased);

    return ResponseEntity.ok(SuccessResponse.of("성공적으로 템플릿 상세 정보를 조회했습니다.", response));
  }

  @Operation(summary = "템플릿 생성", description = "템플릿을 생성합니다.")
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<SuccessResponse<TemplateResponse>> createTemplate(
      @RequestPart("request") @Valid TemplateCreateRequest request,
      @RequestPart("file") MultipartFile file,
      @AuthenticationPrincipal UserPrincipal userPrincipal) {
    Template template = templateService.create(request, file, userPrincipal.getUserProfile());
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(SuccessResponse.of("게시물이 성공적으로 생성되었습니다.", TemplateResponse.of(template)));
  }

  @Operation(summary = "템플릿 수정", description = "템플릿 ID를 기준으로 수정합니다.")
  @PatchMapping(value = "/{templateId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<SuccessResponse<TemplateResponse>> modifyTemplate(
      @RequestPart("request") @Valid TemplateUpdateRequest request,
      @RequestPart(value = "file", required = false) MultipartFile file,
      @PathVariable Long templateId,
      @AuthenticationPrincipal UserPrincipal userPrincipal) {
    Template template =
        templateService.update(templateId, request, userPrincipal.getUserProfile(), file);
    return ResponseEntity.ok(
        SuccessResponse.of("게시물이 성공적으로 수정되었습니다.", TemplateResponse.of(template)));
  }

  @Operation(summary = "템플릿 삭제", description = "템플릿 ID를 기준으로 삭제합니다.")
  @DeleteMapping("/{templatesId}")
  public ResponseEntity<SuccessResponse> deleteTemplate(
      @PathVariable Long templatesId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
    templateService.delete(templatesId, userPrincipal.getUserProfile());
    return ResponseEntity.ok(SuccessResponse.of("게시물이 성공적으로 삭제되었습니다.", null));
  }

  @Operation(summary = "템플릿 다운로드", description = "해당 템플릿을 다운로드합니다.")
  @GetMapping("/{templateId}/download")
  public ResponseEntity<Resource> downloadTemplate(
      @PathVariable Long templateId,
      @AuthenticationPrincipal UserPrincipal userPrincipal,
      HttpServletRequest request) {

    String ip = request.getRemoteAddr();
    String userAgent = request.getHeader("User-Agent");

    return templateDownloadService.downloadTemplate(
        templateId, userPrincipal.getUserProfile(), ip, userAgent);
  }
}
