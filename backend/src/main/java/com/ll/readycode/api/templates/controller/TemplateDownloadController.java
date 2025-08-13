package com.ll.readycode.api.templates.controller;

import com.ll.readycode.domain.templates.downloads.service.TemplateDownloadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/downloads")
public class TemplateDownloadController {

  public final TemplateDownloadService templateDownloadService;
}
