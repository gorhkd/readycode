package com.ll.readycode.api.templates.dto.request;

import java.nio.file.Path;

public record TemplateSeedRequest(
    String title,
    String description,
    String image,
    int price,
    Long categoryId,
    Path filePath,
    String originalFilename) {}
