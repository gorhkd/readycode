package com.ll.readycode.domain.templates.downloads.service;

import com.ll.readycode.domain.templates.downloads.repository.TemplateDownloadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TemplateDownloadService {

  private final TemplateDownloadRepository templateDownloadRepository;
}
