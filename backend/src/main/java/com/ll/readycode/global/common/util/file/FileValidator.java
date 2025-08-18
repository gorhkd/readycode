package com.ll.readycode.global.common.util.file;

import com.ll.readycode.global.common.auth.oauth.properties.TemplateFileProperties;
import com.ll.readycode.global.exception.CustomException;
import com.ll.readycode.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Component
public class FileValidator {

  private final TemplateFileProperties properties;
  private final FileNameSanitizer fileNameSanitizer;

  public void validateFile(MultipartFile file) {
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

    if (file.getSize() > properties.getMaxSize().toBytes()) {
      throw new CustomException(ErrorCode.FILE_TOO_LARGE);
    }
  }

  private boolean isAllowedExtension(String extension) {
    return properties.getAllowedExtensions().contains(extension);
  }

  private String getFileExtension(String filename) {
    if (filename == null || !filename.contains(".")) {
      throw new CustomException(ErrorCode.UNSUPPORTED_EXTENSION);
    }
    return fileNameSanitizer.getFileExtension(filename);
  }
}
