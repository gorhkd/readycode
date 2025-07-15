package com.ll.readycode.api.templates.controller;

import com.ll.readycode.api.templates.dto.request.TemplateCreateRequest;
import com.ll.readycode.api.templates.dto.request.TemplateUpdateRequest;
import com.ll.readycode.api.templates.dto.response.TemplateDetailResponse;
import com.ll.readycode.api.templates.dto.response.TemplateResponse;
import com.ll.readycode.api.templates.dto.response.TemplateScrollResponse;
import com.ll.readycode.domain.templates.templates.entity.Template;
import com.ll.readycode.domain.templates.templates.service.TemplateService;
import com.ll.readycode.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/templates")
@RequiredArgsConstructor
@RestController
public class TemplateController {
  private final TemplateService templateService;

  @Operation(summary = "템플릿 목록 조회", description = "템플릿을 최신순 기준으로 커서 기반 페이징 방식으로 조회합니다.")
  @GetMapping
  public ResponseEntity<SuccessResponse<TemplateScrollResponse>> getTemplates(
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          LocalDateTime cursor,
      @RequestParam(defaultValue = "10") int limit) {
    TemplateScrollResponse response = templateService.getTemplates(cursor, limit);
    return ResponseEntity.ok(SuccessResponse.of("템플릿 목록을 성공적으로 조회했습니다.", response));
  }

  @Operation(summary = "템플릿 상세 조회", description = "템플릿 ID를 기준으로 상세 정보를 조회합니다.")
  @GetMapping("/{templatesId}")
  public ResponseEntity<SuccessResponse<TemplateDetailResponse>> detailsTemplate(
      @PathVariable Long templatesId) {
    Template template = templateService.findTemplateById(templatesId);
    return ResponseEntity.ok(
        SuccessResponse.of("성공적으로 게시물 상세 정보를 조회했습니다.", TemplateDetailResponse.of(template)));
  }

  @Operation(summary = "템플릿 생성", description = "템플릿을 생성합니다.")
  @PostMapping
  public ResponseEntity<SuccessResponse<TemplateResponse>> createTemplate(
      @Valid @RequestBody TemplateCreateRequest request) {
    Template template = templateService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(SuccessResponse.of("게시물이 성공적으로 생성되었습니다.", TemplateResponse.of(template)));
  }

  @Operation(summary = "템플릿 수정", description = "템플릿 ID를 기준으로 수정합니다.")
  @PatchMapping("/{templatesId}")
  public ResponseEntity<SuccessResponse<TemplateResponse>> modifyTemplate(
      @Valid @RequestBody TemplateUpdateRequest request, @PathVariable Long templatesId) {
    Template template = templateService.update(templatesId, request);
    return ResponseEntity.ok(
        SuccessResponse.of("게시물이 성공적으로 수정되었습니다.", TemplateResponse.of(template)));
  }

  @Operation(summary = "템플릿 삭제", description = "템플릿 ID를 기준으로 삭제합니다.")
  @DeleteMapping("/{templatesId}")
  public ResponseEntity<SuccessResponse> deleteTemplate(@PathVariable Long templatesId) {
    templateService.delete(templatesId);
    return ResponseEntity.ok(SuccessResponse.of("게시물이 성공적으로 삭제되었습니다.", null));
  }
}
