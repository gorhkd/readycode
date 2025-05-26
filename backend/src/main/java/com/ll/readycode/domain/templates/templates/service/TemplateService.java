package com.ll.readycode.domain.templates.templates.service;

import com.ll.readycode.domain.templates.templates.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TemplateService {
  private final TemplateRepository templateRepository;
}
