package com.ll.readycode.api.categories.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record CategoryRequest(@Schema(description = "카테고리 이름", example = "백엔드") String name) {}
