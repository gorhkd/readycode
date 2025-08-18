package com.ll.readycode.domain.templates.files.service;

import com.ll.readycode.domain.templates.files.entity.TemplateFile;
import com.ll.readycode.domain.templates.files.repository.TemplateFileRepository;
import com.ll.readycode.global.common.util.file.FileNameSanitizer;
import com.ll.readycode.global.common.util.file.FileValidator;
import com.ll.readycode.global.common.util.file.storage.FileStorage;
import com.ll.readycode.global.exception.CustomException;
import com.ll.readycode.global.exception.ErrorCode;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class TemplateFileService {

  private final TemplateFileRepository templateFileRepository;
  private final FileValidator fileValidator;
  private final FileNameSanitizer fileNameSanitizer;
  private final FileStorage fileStorage;

  public TemplateFile create(MultipartFile file) {
    fileValidator.validateFile(file);
    String savedPath = saveFile(file);
    return createTemplateFile(file, savedPath);
  }

  private String saveFile(MultipartFile file) {
    String extension = fileNameSanitizer.getFileExtension(file.getOriginalFilename());
    String uuid = UUID.randomUUID().toString();
    String savedFileName = uuid + "." + extension;
    return fileStorage.save(file, savedFileName);
  }

  private TemplateFile createTemplateFile(MultipartFile file, String savedPath) {
    String originalFilename = file.getOriginalFilename();
    String extension = fileNameSanitizer.getFileExtension(originalFilename);

    return TemplateFile.builder()
        .originalFilename(originalFilename)
        .extension(extension)
        .fileSize(file.getSize())
        .url(savedPath)
        .build();
  }

  public TemplateFile updateFile(TemplateFile oldFile, MultipartFile newFile) {
    fileStorage.delete(oldFile.getUrl());
    return create(newFile);
  }

  public void deleteFile(TemplateFile templateFile) {
    fileStorage.delete(templateFile.getUrl());
  }

  public TemplateFile findByTemplateId(Long templateId) {
    return templateFileRepository
        .findByTemplateId(templateId)
        .orElseThrow(() -> new CustomException(ErrorCode.TEMPLATE_FILE_NOT_FOUND));
  }
}
