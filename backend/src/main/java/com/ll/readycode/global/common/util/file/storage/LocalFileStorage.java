package com.ll.readycode.global.common.util.file.storage;

import com.ll.readycode.global.common.util.file.FilePathResolver;
import com.ll.readycode.global.exception.CustomException;
import com.ll.readycode.global.exception.ErrorCode;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Repository
public class LocalFileStorage implements FileStorage {
  private final FilePathResolver filePathResolver;

  @Override
  public String save(MultipartFile file, String saveAsName) {
    if (file == null || file.isEmpty()) {
      throw new CustomException(ErrorCode.FILE_SAVE_ERROR);
    }

    String fullPath = filePathResolver.resolveFilePath(saveAsName);
    File target = new File(fullPath);
    File parent = target.getParentFile();

    if (parent != null && !parent.exists() && !parent.mkdirs()) {
      throw new CustomException(ErrorCode.FILE_SAVE_ERROR);
    }

    try (var in = file.getInputStream()) {
      Files.copy(in, target.toPath(), StandardCopyOption.REPLACE_EXISTING);

      return fullPath;

    } catch (IOException | IllegalStateException e) {
      log.error("FILE_SAVE_ERROR path={}, cause={}", fullPath, e.toString(), e);
      throw new CustomException(ErrorCode.FILE_SAVE_ERROR);
    }
  }

  @Override
  public void delete(String path) {
    File f = new File(path);
    if (f.exists()) {
      boolean deleted = f.delete();
      if (!deleted) {
        throw new CustomException(ErrorCode.FILE_DELETE_FAILED);
      }
    }
  }

  @Override
  public boolean exists(String path) {
    return new File(path).exists();
  }

  @Override
  public long size(String path) {
    File f = new File(path);
    if (!f.exists()) throw new CustomException(ErrorCode.DOWNLOAD_NOT_FOUND);
    return f.length();
  }
}
