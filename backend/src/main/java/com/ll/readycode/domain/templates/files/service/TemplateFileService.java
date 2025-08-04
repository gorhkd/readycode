package com.ll.readycode.domain.templates.files.service;

import com.ll.readycode.domain.templates.files.entity.TemplateFile;
import com.ll.readycode.domain.templates.files.repository.TemplateFileRepository;
import com.ll.readycode.global.exception.CustomException;
import com.ll.readycode.global.exception.ErrorCode;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class TemplateFileService {

  private final TemplateFileRepository templateFileRepository;

  private static final String BASE_DIR = "uploads/templates/";
  private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

  public TemplateFile create(MultipartFile file) {
    validateFile(file);
    String savedPath = saveFile(file);
    return createTemplateFile(file, savedPath);
  }

  private void validateFile(MultipartFile file) {
    if (file.isEmpty()) {
      throw new CustomException(ErrorCode.EMPTY_FILE);
    }

    String originalFilename = file.getOriginalFilename();
    if (originalFilename == null || !originalFilename.contains(".")) {
      throw new CustomException(ErrorCode.INVALID_FILENAME);
    }
    String extension = getFileExtension(originalFilename);

    if (!isAllowedExtension(extension)) {
      throw new CustomException(ErrorCode.UNSUPPORTED_EXTENSION);
    }

    if (file.getSize() > MAX_FILE_SIZE) {
      throw new CustomException(ErrorCode.FILE_TOO_LARGE);
    }
  }

  private String saveFile(MultipartFile file) {
    String extension = getFileExtension(file.getOriginalFilename());
    String uuid = UUID.randomUUID().toString();
    String savedFileName = uuid + "." + extension;
    String fullPath = BASE_DIR + savedFileName;

    File directory = new File(BASE_DIR);
    if (!directory.exists()) {
      directory.mkdirs();
    }

    try {
      file.transferTo(new File(fullPath));
    } catch (IOException | IllegalStateException e) {
      throw new CustomException(ErrorCode.FILE_SAVE_ERROR);
    }

    return fullPath;
  }

  private TemplateFile createTemplateFile(MultipartFile file, String savedPath) {
    String extension = getFileExtension(file.getOriginalFilename());

    TemplateFile templateFile =
        TemplateFile.builder()
            .originalName(file.getOriginalFilename())
            .extension(extension)
            .fileSize(file.getSize())
            .url(savedPath)
            .build();

    return templateFileRepository.save(templateFile);
  }

  private String getFileExtension(String filename) {
    if (filename == null || !filename.contains(".")) {
      throw new CustomException(ErrorCode.UNSUPPORTED_EXTENSION);
    }
    return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
  }

  private boolean isAllowedExtension(String extension) {
    return List.of("zip", "rar", "txt", "java", "pdf").contains(extension);
  }
}
