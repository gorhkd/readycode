package com.ll.readycode.domain.templates.files.service;

import com.ll.readycode.domain.templates.files.entity.TemplateFile;
import com.ll.readycode.domain.templates.files.repository.TemplateFileRepository;
import com.ll.readycode.global.common.auth.oauth.properties.TemplateFileProperties;
import com.ll.readycode.global.common.util.file.FileNameSanitizer;
import com.ll.readycode.global.common.util.file.FilePathResolver;
import com.ll.readycode.global.common.util.file.FileValidator;
import com.ll.readycode.global.common.util.file.storage.FileStorage;
import com.ll.readycode.global.exception.CustomException;
import com.ll.readycode.global.exception.ErrorCode;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class TemplateFileService {

  private final TemplateFileRepository templateFileRepository;
  private final FileValidator fileValidator;
  private final FileNameSanitizer fileNameSanitizer;
  private final FileStorage fileStorage;

  private final FilePathResolver filePathResolver;
  private final TemplateFileProperties properties;

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

  @Transactional
  public TemplateFile createFromPath(Path filePath, String originalFilename) {
    try {
      String extension =
          fileNameSanitizer.getFileExtension(originalFilename).toLowerCase(Locale.ROOT);
      String savedFileName = UUID.randomUUID() + "." + extension;

      Path target = Paths.get(filePathResolver.resolveFilePath(savedFileName));
      Files.createDirectories(target.getParent());

      if (Files.exists(filePath)) {
        Files.copy(filePath, target, StandardCopyOption.REPLACE_EXISTING);
      } else {
        String cp = filePath.toString().replace("\\", "/");
        ClassPathResource res = new ClassPathResource(cp);
        if (!res.exists()) {
          throw new FileNotFoundException(
              "Missing FS=" + filePath.toAbsolutePath() + " , CP=" + cp);
        }
        try (InputStream in = res.getInputStream()) {
          Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }
      }

      long size = Files.size(target);
      if (size > properties.getMaxSize().toBytes()) {
        Files.deleteIfExists(target);
        throw new IllegalStateException("File too large: " + size);
      }

      String url = properties.getBaseDir() + "/" + savedFileName; // "templates/xxxx.zip"

      return TemplateFile.builder()
          .originalFilename(originalFilename)
          .extension(extension)
          .fileSize(size)
          .url(url)
          .build();

    } catch (IOException e) {
      throw new IllegalStateException(
          "Seed copy failed: "
              + originalFilename
              + " | reason="
              + e.getClass().getSimpleName()
              + " | "
              + e.getMessage(),
          e);
    }
  }
}
