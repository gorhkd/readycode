package com.ll.readycode.domain.templates.downloads.service;

import com.ll.readycode.domain.templates.downloads.entity.TemplateDownload;
import com.ll.readycode.domain.templates.downloads.repository.TemplateDownloadRepository;
import com.ll.readycode.domain.templates.files.entity.TemplateFile;
import com.ll.readycode.domain.templates.files.service.TemplateFileService;
import com.ll.readycode.domain.templates.purchases.service.TemplatePurchaseService;
import com.ll.readycode.domain.templates.templates.entity.Template;
import com.ll.readycode.domain.templates.templates.service.TemplateService;
import com.ll.readycode.domain.users.userprofiles.entity.UserProfile;
import com.ll.readycode.global.common.auth.oauth.properties.TemplateFileProperties;
import com.ll.readycode.global.exception.CustomException;
import com.ll.readycode.global.exception.ErrorCode;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriUtils;

@Slf4j
@RequiredArgsConstructor
@Service
public class TemplateDownloadService {

  private final TemplatePurchaseService templatePurchaseService;
  private final TemplateFileService templateFileService;
  private final TemplateDownloadRepository templateDownloadRepository;
  private final TemplateService templateService;
  private final TemplateFileProperties properties;

  public ResponseEntity<Resource> downloadTemplate(
      Long templateId, UserProfile userProfile, String ipAddress, String deviceInfo) {

    try {
      // 구매 여부 확인
      templatePurchaseService.throwIfNotPurchased(userProfile.getId(), templateId);

      // 템플릿 파일 정보 조회
      TemplateFile templateFile = templateFileService.findByTemplateId(templateId);

      // 파일 경로 보안 검증
      Path filePath = validateAndResolvePath(templateFile.getUrl());
      File file = filePath.toFile();

      // 파일 존재 및 접근 가능성 확인
      if (!file.exists()) {
        throw new CustomException(ErrorCode.DOWNLOAD_NOT_FOUND);
      }

      if (!file.canRead()) {
        throw new CustomException(ErrorCode.FILE_READ_ERROR);
      }

      // 파일 크기 제한 확인
      if (file.length() > properties.getMaxSize().toBytes()) {
        throw new CustomException(ErrorCode.FILE_TOO_LARGE);
      }

      // 템플릿 정보 조회
      Template template = templateService.findTemplateById(templateId);

      // 다운로드 로그 기록 (별도 트랜잭션으로 처리)
      logTemplateDownload(userProfile, template, ipAddress, deviceInfo);

      // 리소스 생성 및 응답
      Resource resource = new FileSystemResource(file);
      String encodedFilename =
          UriUtils.encode(templateFile.getOriginalFilename(), StandardCharsets.UTF_8);

      return ResponseEntity.ok()
          .contentType(MediaType.APPLICATION_OCTET_STREAM)
          .contentLength(file.length())
          .header(
              HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFilename + "\"")
          .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
          .body(resource);

    } catch (IOException e) {
      throw new CustomException(ErrorCode.FILE_READ_ERROR);
    } catch (Exception e) {
      throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
    }
  }

  /** 파일 경로의 보안성을 검증하고 안전한 Path 를 반환 */
  private Path validateAndResolvePath(String fileUrl) throws IOException {
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

  /** 다운로드 로그를 별도 트랜잭션으로 기록 */
  @Async
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  private void logTemplateDownload(
      UserProfile userProfile, Template template, String ipAddress, String deviceInfo) {
    try {
      TemplateDownload downloadLog =
          TemplateDownload.builder()
              .user(userProfile)
              .template(template)
              .ipAddress(ipAddress)
              .device(deviceInfo)
              .build();

      templateDownloadRepository.save(downloadLog);
      log.info(
          "Template download started. TemplateId: {}, UserId: {}",
          template.getId(),
          userProfile.getId());
      logTemplateDownload(userProfile, template, ipAddress, deviceInfo);

    } catch (Exception e) {
      // 로그 저장 실패가 다운로드 자체를 방해하지 않도록 함
      log.error(
          "Failed to log template download. TemplateId: {}, UserId: {}",
          template.getId(),
          userProfile.getId(),
          e);
    }
  }
}
