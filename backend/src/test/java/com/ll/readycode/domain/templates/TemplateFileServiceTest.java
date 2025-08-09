package com.ll.readycode.domain.templates;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import com.ll.readycode.domain.templates.files.entity.TemplateFile;
import com.ll.readycode.domain.templates.files.repository.TemplateFileRepository;
import com.ll.readycode.domain.templates.files.service.TemplateFileService;
import com.ll.readycode.global.common.auth.oauth.properties.TemplateFileProperties;
import com.ll.readycode.global.exception.CustomException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class TemplateFileServiceTest {

  @InjectMocks private TemplateFileService templateFileService;

  @Mock private TemplateFileRepository templateFileRepository;

  @Mock private TemplateFileProperties properties;

  @TempDir Path tempDir;

  @Test
  @DisplayName("정상적인 파일 업로드 시 TemplateFile 객체 생성")
  void create_whenValidFile_thenReturnTemplateFile() {
    // given
    MultipartFile file =
        new MockMultipartFile("file", "sample.zip", "application/zip", "hello".getBytes());

    given(properties.getBaseDir()).willReturn(tempDir.toString() + "/");
    given(properties.getMaxSize()).willReturn(DataSize.ofMegabytes(10));

    // when
    TemplateFile result = templateFileService.create(file);

    // then
    assertThat(result.getOriginalFilename()).isEqualTo("sample.zip");
    assertThat(result.getExtension()).isEqualTo("zip");
    assertThat(result.getUrl()).contains(".zip");
  }

  @Test
  @DisplayName("비어있는 파일 업로드 시 예외 발생")
  void create_whenEmptyFile_thenThrowException() {
    // given
    MultipartFile file = new MockMultipartFile("file", "", "application/zip", new byte[0]);

    // expect
    assertThrows(CustomException.class, () -> templateFileService.create(file));
  }

  @Test
  @DisplayName("확장자가 없는 파일 업로드 시 예외 발생")
  void create_whenMissingExtension_thenThrowException() {
    MultipartFile file = new MockMultipartFile("file", "noextension", "", "abc".getBytes());

    assertThrows(CustomException.class, () -> templateFileService.create(file));
  }

  @Test
  @DisplayName("허용되지 않은 확장자 업로드 시 예외 발생")
  void create_whenUnsupportedExtension_thenThrowException() {
    MultipartFile file = new MockMultipartFile("file", "malware.exe", "", "virus".getBytes());

    assertThrows(CustomException.class, () -> templateFileService.create(file));
  }

  @Test
  @DisplayName("파일 용량 초과 시 예외 발생")
  void create_whenFileSizeExceedsLimit_thenThrowException() {
    MultipartFile file =
        new MockMultipartFile("file", "large.zip", "application/zip", new byte[20 * 1024 * 1024]);
    given(properties.getMaxSize()).willReturn(DataSize.ofMegabytes(10));

    assertThrows(CustomException.class, () -> templateFileService.create(file));
  }

  @Test
  @DisplayName("기존 파일 삭제 후 새 파일 저장")
  void updateFile_whenValid_thenDeleteOldAndCreateNew() throws IOException {
    // given
    Path oldFilePath = Files.createFile(tempDir.resolve("old.zip"));
    TemplateFile oldFile = TemplateFile.builder().url(oldFilePath.toString()).build();

    MultipartFile newFile =
        new MockMultipartFile("file", "new.zip", "application/zip", "new content".getBytes());

    given(properties.getBaseDir()).willReturn(tempDir.toString() + "/");
    given(properties.getMaxSize()).willReturn(DataSize.ofMegabytes(10));

    // when
    TemplateFile result = templateFileService.updateFile(oldFile, newFile);

    // then
    assertThat(result.getOriginalFilename()).isEqualTo("new.zip");
    assertThat(Files.exists(Path.of(result.getUrl()))).isTrue();
    assertThat(Files.exists(oldFilePath)).isFalse(); // old file should be deleted
  }

  @Test
  @DisplayName("실제 파일 삭제 성공")
  void deleteFile_whenFileExists_thenDeleteSuccessfully() throws IOException {
    // given
    Path filePath = Files.createFile(tempDir.resolve("todelete.zip"));
    TemplateFile templateFile = TemplateFile.builder().url(filePath.toString()).build();

    // when
    templateFileService.deleteFile(templateFile);

    // then
    assertThat(Files.exists(filePath)).isFalse();
  }

  @Test
  @DisplayName("템플릿 ID로 파일 조회 성공")
  void findByTemplateId_whenExists_thenReturnFile() {
    // given
    TemplateFile file = TemplateFile.builder().id(1L).originalFilename("test.zip").build();
    given(templateFileRepository.findByTemplateId(1L)).willReturn(Optional.of(file));

    // when
    TemplateFile result = templateFileService.findByTemplateId(1L);

    // then
    assertThat(result.getOriginalFilename()).isEqualTo("test.zip");
  }

  @Test
  @DisplayName("템플릿 ID로 파일 조회 실패 시 예외 발생")
  void findByTemplateId_whenNotExists_thenThrowException() {
    // given
    given(templateFileRepository.findByTemplateId(1L)).willReturn(Optional.empty());

    // expect
    assertThrows(CustomException.class, () -> templateFileService.findByTemplateId(1L));
  }
}
