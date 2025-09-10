package com.ll.readycode.domain.templates.downloads.repository;

import com.ll.readycode.api.admin.dto.response.AdminResponseDto.TemplateDownloadDetails;
import java.time.LocalDateTime;
import java.util.List;

public interface TemplateDownloadCustom {

  List<TemplateDownloadDetails> findTemplatesForDownloadStatistics(
      LocalDateTime startDate,
      LocalDateTime endDate,
      Long templateId
  );
}
