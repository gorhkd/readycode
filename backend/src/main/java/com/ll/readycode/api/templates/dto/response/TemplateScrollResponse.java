package com.ll.readycode.api.templates.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record TemplateScrollResponse(List<TemplateSummary> templates, LocalDateTime nextCursor) {}
