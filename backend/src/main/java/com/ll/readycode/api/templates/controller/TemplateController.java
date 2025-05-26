package com.ll.readycode.api.templates.controller;

import com.ll.readycode.domain.templates.templates.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/templates")
@RequiredArgsConstructor
@RestController
public class TemplateController {
  private final TemplateService templateService;
}
