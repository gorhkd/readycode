package com.ll.readycode.api.templates.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "커서 기반 템플릿 목록 응답")
public record TemplateScrollResponse(
    @Schema(description = "템플릿 요약 정보 리스트") List<TemplateSummary> templates,
    @Schema(description = "다음 페이지 요청을 위한 커서", example = "2024-12-09T15:30:00") String nextCursor) {}
