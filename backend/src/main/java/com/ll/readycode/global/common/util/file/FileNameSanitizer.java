package com.ll.readycode.global.common.util.file;

import org.springframework.stereotype.Component;

/** 파일명 정규화 및 확장자 추출을 담당하는 유틸리티 컴포넌트 */
@Component
public class FileNameSanitizer {

  public String getFileExtension(String filename) {
    return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
  }
}
