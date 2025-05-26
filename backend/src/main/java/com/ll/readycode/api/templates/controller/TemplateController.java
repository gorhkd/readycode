package com.ll.readycode.api.templates.controller;

import com.ll.readycode.api.templates.dto.request.TemplateCreateRequest;
import com.ll.readycode.api.templates.dto.response.TemplateResponse;
import com.ll.readycode.domain.templates.templates.entity.Template;
import com.ll.readycode.domain.templates.templates.service.TemplateService;
import com.ll.readycode.global.common.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/templates")
@RequiredArgsConstructor
@RestController
public class TemplateController {
  private final TemplateService templateService;

  @PostMapping
  public ResponseEntity<SuccessResponse<TemplateResponse>> createTemplate(
      @Valid @RequestBody TemplateCreateRequest request) {
    Template template = templateService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(SuccessResponse.of("게시물이 성공적으로 생성되었습니다.", TemplateResponse.of(template)));
  }
}
