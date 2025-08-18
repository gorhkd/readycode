package com.ll.readycode.global.common.util.file;

import com.ll.readycode.global.common.auth.oauth.properties.TemplateFileProperties;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/** 파일 저장 경로 생성을 담당하는 컴포넌트 파일의 최종 저장 경로를 생성합니다. */
@RequiredArgsConstructor
@Component
public class FilePathResolver {
  private final TemplateFileProperties properties;

  public String resolveFilePath(String fileName) {
    return Paths.get(properties.getBaseDir(), fileName).toString();
  }
}
