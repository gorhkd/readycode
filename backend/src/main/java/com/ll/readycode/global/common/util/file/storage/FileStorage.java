package com.ll.readycode.global.common.util.file.storage;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorage {
  String save(MultipartFile file, String saveAsName);

  void delete(String path);

  boolean exists(String path);

  long size(String path);
}
