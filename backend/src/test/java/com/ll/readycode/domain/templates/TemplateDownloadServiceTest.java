package com.ll.readycode.domain.templates;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;

import com.ll.readycode.domain.templates.downloads.repository.TemplateDownloadRepository;
import com.ll.readycode.domain.templates.downloads.service.TemplateDownloadService;
import com.ll.readycode.domain.templates.files.entity.TemplateFile;
import com.ll.readycode.domain.templates.files.service.TemplateFileService;
import com.ll.readycode.domain.templates.purchases.repository.TemplatePurchaseRepository;
import com.ll.readycode.domain.templates.purchases.service.TemplatePurchaseService;
import com.ll.readycode.domain.templates.templates.entity.Template;
import com.ll.readycode.domain.templates.templates.service.TemplateService;
import com.ll.readycode.domain.users.userprofiles.entity.UserProfile;
import com.ll.readycode.global.common.auth.oauth.properties.TemplateFileProperties;
import com.ll.readycode.global.common.util.file.FilePathResolver;
import com.ll.readycode.global.exception.CustomException;
import com.ll.readycode.global.exception.ErrorCode;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.unit.DataSize;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TemplateDownloadServiceTest {

  @InjectMocks private TemplateDownloadService downloadService;

  @Mock private TemplatePurchaseService purchaseService;
  @Mock private TemplateFileService templateFileService;
  @Mock private TemplatePurchaseRepository templatePurchaseRepository;
  @Mock private TemplateDownloadRepository templateDownloadRepository;
  @Mock private FilePathResolver filePathResolver;
  @Mock private TemplateService templateService;
  @Mock private TemplateFileProperties properties;

  private final UserProfile user = UserProfile.builder().id(1L).build();
  private final Template template = Template.builder().id(1L).build();
  private final String ip = "127.0.0.1";
  private final String device = "Chrome";

  @TempDir Path tempDir;
  private Path tempFilePath;

  private TemplateFile createTemplateFile() {
    return TemplateFile.builder()
        .originalFilename("example.zip")
        .url(tempFilePath.getFileName().toString())
        .build();
  }

  @BeforeEach
  void setup() throws IOException {
    tempFilePath = tempDir.resolve("example.zip");
    Files.write(tempFilePath, "dummy".getBytes());

    given(properties.getBaseDir()).willReturn(tempFilePath.getParent().toString() + "/");
    given(properties.getMaxSize()).willReturn(DataSize.ofMegabytes(10));
  }

  @AfterEach
  void cleanUp() throws IOException {
    Files.deleteIfExists(tempFilePath);
  }

  @Test
  @DisplayName("정상 다운로드 요청 시 파일 응답 반환")
  void downloadTemplate_whenValid_thenReturnResponse() {
    // given
    TemplateFile templateFile = createTemplateFile();
    given(templatePurchaseRepository.existsByBuyerIdAndTemplateId(user.getId(), 1L))
        .willReturn(true);
    given(templateFileService.findByTemplateId(1L)).willReturn(templateFile);
    given(templateService.findTemplateById(1L)).willReturn(template);
    given(filePathResolver.resolveForRead(templateFile.getUrl())).willReturn(tempFilePath);
    given(templateDownloadRepository.save(any())).willAnswer(inv -> inv.getArgument(0));

    // when
    ResponseEntity<Resource> response = downloadService.downloadTemplate(1L, user, ip, device);

    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getHeaders().getContentDisposition().getFilename())
        .isEqualTo("example.zip");
    assertThat(response.getBody()).isInstanceOf(Resource.class);
  }

  @Test
  @DisplayName("구매하지 않은 사용자는 예외 발생")
  void downloadTemplate_whenNotPurchased_thenThrowException() {
    // given
    willThrow(new CustomException(ErrorCode.PURCHASE_NOT_FOUND))
        .given(purchaseService)
        .throwIfNotPurchased(user.getId(), 1L);

    // then
    assertThrows(
        CustomException.class, () -> downloadService.downloadTemplate(1L, user, ip, device));
  }

  @Test
  @DisplayName("존재하지 않는 파일 요청 시 예외 발생")
  void downloadTemplate_whenFileNotExists_thenThrowException() {
    // given
    TemplateFile nonExistFile =
        TemplateFile.builder().originalFilename("nope.zip").url("nonexistent.zip").build();

    given(templatePurchaseRepository.existsByBuyerIdAndTemplateId(user.getId(), 1L))
        .willReturn(true);
    given(templateFileService.findByTemplateId(1L)).willReturn(nonExistFile);
    given(templateService.findTemplateById(1L)).willReturn(template);

    // then
    assertThrows(
        CustomException.class, () -> downloadService.downloadTemplate(1L, user, ip, device));
  }

  @Test
  @DisplayName("파일 크기 초과 시 예외 발생")
  void downloadTemplate_whenFileTooLarge_thenThrowException() throws IOException {
    // given
    Path bigFile = Files.createTempFile("big", ".zip");
    Files.write(bigFile, new byte[11 * 1024 * 1024]); // 11MB

    TemplateFile bigTemplateFile =
        TemplateFile.builder()
            .originalFilename("big.zip")
            .url(bigFile.getFileName().toString())
            .build();

    given(templatePurchaseRepository.existsByBuyerIdAndTemplateId(user.getId(), 1L))
        .willReturn(true);
    given(templateFileService.findByTemplateId(1L)).willReturn(bigTemplateFile);
    given(templateService.findTemplateById(1L)).willReturn(template);

    // when & then
    assertThrows(
        CustomException.class, () -> downloadService.downloadTemplate(1L, user, ip, device));

    Files.deleteIfExists(bigFile);
  }

  @Test
  @DisplayName("디렉터리 이탈 경로 요청 시 예외 발생")
  void downloadTemplate_whenInvalidPath_thenThrowException() {
    // given
    TemplateFile maliciousFile =
        TemplateFile.builder().originalFilename("hack.zip").url("../../etc/passwd").build();

    given(templatePurchaseRepository.existsByBuyerIdAndTemplateId(user.getId(), 1L))
        .willReturn(true);
    given(templateFileService.findByTemplateId(1L)).willReturn(maliciousFile);

    // then
    assertThrows(
        CustomException.class, () -> downloadService.downloadTemplate(1L, user, ip, device));
  }
}
