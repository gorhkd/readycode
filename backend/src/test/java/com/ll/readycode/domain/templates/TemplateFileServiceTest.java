package com.ll.readycode.domain.templates;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import com.ll.readycode.domain.templates.files.entity.TemplateFile;
import com.ll.readycode.domain.templates.files.repository.TemplateFileRepository;
import com.ll.readycode.domain.templates.files.service.TemplateFileService;
import com.ll.readycode.global.common.util.file.FileNameSanitizer;
import com.ll.readycode.global.common.util.file.FileValidator;
import com.ll.readycode.global.common.util.file.storage.FileStorage;
import com.ll.readycode.global.exception.CustomException;
import com.ll.readycode.global.exception.ErrorCode;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class TemplateFileServiceTest {

  @InjectMocks private TemplateFileService templateFileService;

  @Mock private TemplateFileRepository templateFileRepository;
  @Mock private FileValidator fileValidator;
  @Mock private FileNameSanitizer fileNameSanitizer;
  @Mock private FileStorage fileStorage;

  @Test
  @DisplayName("정상적인 파일 업로드 시 TemplateFile 객체 생성")
  void create_whenValidFile_thenReturnTemplateFile() {
    // given
    MultipartFile file =
        new MockMultipartFile("file", "sample.zip", "application/zip", "hello".getBytes());

    // validator는 호출만 → 기본 no-op
    given(fileNameSanitizer.getFileExtension("sample.zip")).willReturn("zip");
    given(fileStorage.save(eq(file), anyString())).willReturn("/tmp/somewhere/any-uuid.zip");

    // when
    TemplateFile result = templateFileService.create(file);

    // then
    verify(fileValidator).validateFile(file);
    verify(fileNameSanitizer, times(2)).getFileExtension("sample.zip");
    verify(fileStorage).save(eq(file), anyString());

    assertThat(result.getOriginalFilename()).isEqualTo("sample.zip");
    assertThat(result.getExtension()).isEqualTo("zip");
    assertThat(result.getUrl()).endsWith(".zip");
  }

  @Test
  @DisplayName("비어있는 파일 업로드 시 예외 발생")
  void create_whenEmptyFile_thenThrowException() {
    MultipartFile file = new MockMultipartFile("file", "", "application/zip", new byte[0]);

    doThrow(new CustomException(ErrorCode.EMPTY_FILE)).when(fileValidator).validateFile(file);

    assertThrows(CustomException.class, () -> templateFileService.create(file));

    verify(fileNameSanitizer, never()).getFileExtension(anyString());
    verify(fileStorage, never()).save(any(), anyString());
  }

  @Test
  @DisplayName("확장자가 없는 파일 업로드 시 예외 발생")
  void create_whenMissingExtension_thenThrowException() {
    // given
    MultipartFile file = new MockMultipartFile("file", "noextension", "", "abc".getBytes());

    // Validator가 '확장자 없음'으로 예외 던지도록 스텁
    doThrow(new CustomException(ErrorCode.INVALID_FILENAME)).when(fileValidator).validateFile(file);

    // expect
    assertThrows(CustomException.class, () -> templateFileService.create(file));

    // 이후 단계가 호출되지 않았는지 확인(선택)
    verify(fileNameSanitizer, never()).getFileExtension(anyString());
    verify(fileStorage, never()).save(any(), anyString());
  }

  @Test
  @DisplayName("허용되지 않은 확장자 업로드 시 예외 발생")
  void create_whenUnsupportedExtension_thenThrowException() {
    MultipartFile file = new MockMultipartFile("file", "malware.exe", "", "virus".getBytes());

    // Validator가 '미허용 확장자'로 예외 던지도록 스텁
    doThrow(new CustomException(ErrorCode.UNSUPPORTED_EXTENSION))
        .when(fileValidator)
        .validateFile(file);

    assertThrows(CustomException.class, () -> templateFileService.create(file));

    // 이후 단계 호출되지 않았는지 확인(선택)
    verify(fileNameSanitizer, never()).getFileExtension(anyString());
    verify(fileStorage, never()).save(any(), anyString());
  }

  @Test
  @DisplayName("파일 용량 초과 시 예외 발생")
  void create_whenFileSizeExceedsLimit_thenThrowException() {
    // given
    MultipartFile file =
        new MockMultipartFile("file", "large.zip", "application/zip", new byte[20 * 1024 * 1024]);

    // Validator가 용량 초과 예외를 던지도록 스텁
    doThrow(new CustomException(ErrorCode.FILE_TOO_LARGE)).when(fileValidator).validateFile(file);

    // expect
    assertThrows(CustomException.class, () -> templateFileService.create(file));

    // 파일 저장/확장자 추출은 호출되지 않아야 함
    verify(fileStorage, never()).save(any(), anyString());
    verify(fileNameSanitizer, never()).getFileExtension(anyString());
  }

  @Test
  @DisplayName("기존 파일 삭제 후 새 파일 저장")
  void updateFile_whenValid_thenDeleteOldAndCreateNew() {
    TemplateFile oldFile = TemplateFile.builder().url("/old/path/old.zip").build();
    MultipartFile newFile =
        new MockMultipartFile("file", "new.zip", "application/zip", "new".getBytes());

    given(fileNameSanitizer.getFileExtension("new.zip")).willReturn("zip");
    given(fileStorage.save(eq(newFile), anyString())).willReturn("/tmp/new-uuid.zip");

    TemplateFile result = templateFileService.updateFile(oldFile, newFile);

    verify(fileStorage).delete("/old/path/old.zip"); // 삭제 위임 확인
    verify(fileValidator).validateFile(newFile); // 새 파일 검증
    verify(fileNameSanitizer, times(2)).getFileExtension("new.zip"); // 확장자 추출
    verify(fileStorage).save(eq(newFile), anyString()); // 저장 호출

    assertThat(result.getOriginalFilename()).isEqualTo("new.zip");
    assertThat(result.getUrl()).endsWith(".zip");
  }

  @Test
  @DisplayName("실제 파일 삭제 위임")
  void deleteFile_whenCalled_thenDelegateToStorage() {
    TemplateFile templateFile = TemplateFile.builder().url("/tmp/todelete.zip").build();

    templateFileService.deleteFile(templateFile);

    verify(fileStorage).delete("/tmp/todelete.zip");
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
