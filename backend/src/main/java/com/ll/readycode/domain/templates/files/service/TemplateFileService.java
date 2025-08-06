package com.ll.readycode.domain.templates.files.service;

import com.ll.readycode.domain.templates.files.repository.TemplateFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TemplateFileService {

  private final TemplateFileRepository templateFileRepository;

  private static final String BASE_DIR = "uploads/templates/";
  private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
}
