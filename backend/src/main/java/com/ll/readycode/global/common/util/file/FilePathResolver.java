package com.ll.readycode.global.common.util.file;

import com.ll.readycode.global.common.auth.oauth.properties.TemplateFileProperties;
import com.ll.readycode.global.exception.CustomException;
import com.ll.readycode.global.exception.ErrorCode;
import jakarta.annotation.PostConstruct;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/** 파일 저장 경로 생성을 담당하는 컴포넌트 파일의 최종 저장 경로를 생성합니다. */
@Slf4j
@RequiredArgsConstructor
@Component
public class FilePathResolver {
  private final TemplateFileProperties properties;

  @Value("${custom.app.upload.base-dir}")
  private String uploadBaseDir;

  @PostConstruct
  public void debugConfig() {
    log.info("=== FilePathResolver Configuration ===");
    log.info("uploadBaseDir: {}", uploadBaseDir);
    log.info("properties.getBaseDir(): {}", properties.getBaseDir());
    log.info("=====================================");
  }

  public String resolveFilePath(String fileName) {
    Path base = Paths.get(uploadBaseDir).toAbsolutePath().normalize();
    Path target = base.resolve(properties.getBaseDir()).resolve(fileName).normalize();

    if (!target.startsWith(base)) {
      throw new IllegalArgumentException("Invalid path: " + fileName);
    }
    return target.toString();
  }

  public Path resolveForRead(String fileUrl) {
    Path base = Paths.get(uploadBaseDir).toAbsolutePath().normalize();
    Path target = base.resolve(properties.getBaseDir()).resolve(fileUrl).normalize();

    if (!target.startsWith(base)) {
      log.warn("Directory traversal attempt detected. Requested path: {}", fileUrl);
      throw new CustomException(ErrorCode.INVALID_FILE_PATH);
    }
    return target;
  }
}
