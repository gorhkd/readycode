package com.ll.readycode.global.common.util.file;

import com.ll.readycode.global.common.auth.oauth.properties.TemplateFileProperties;
import com.ll.readycode.global.exception.CustomException;
import com.ll.readycode.global.exception.ErrorCode;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/** 파일 저장 경로 생성을 담당하는 컴포넌트 파일의 최종 저장 경로를 생성합니다. */
@Slf4j
@RequiredArgsConstructor
@Component
public class FilePathResolver {
  private final TemplateFileProperties properties;

  public String resolveFilePath(String fileName) {
    return Paths.get(properties.getBaseDir(), fileName).toString();
  }

  public Path resolveForRead(String fileUrl) {
    try {
      // 설정에서 가져오거나 환경변수로 관리하는 것이 좋음
      Path basePath = Paths.get(properties.getBaseDir()).normalize();
      Path requestedPath = Paths.get(fileUrl).normalize();

      // 절대 경로로 변환
      Path resolvedPath = basePath.resolve(requestedPath).normalize();

      // 기본 디렉토리를 벗어나는지 확인 (Directory Traversal 공격 방지)
      if (!resolvedPath.startsWith(basePath)) {
        log.warn("Directory traversal attempt detected. Requested path: {}", fileUrl);
        throw new CustomException(ErrorCode.INVALID_FILE_PATH);
      }

      return resolvedPath;

    } catch (InvalidPathException e) {
      log.warn("Invalid file path requested: {}", fileUrl);
      throw new CustomException(ErrorCode.INVALID_FILE_PATH);
    }
  }
}
