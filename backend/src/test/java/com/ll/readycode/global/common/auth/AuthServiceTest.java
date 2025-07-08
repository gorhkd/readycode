package com.ll.readycode.global.common.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.ll.readycode.api.dto.userauths.UserAuthResponseDto.Token;
import com.ll.readycode.global.common.auth.jwt.JwtProvider;
import com.ll.readycode.global.common.auth.token.RefreshTokenService;
import com.ll.readycode.global.common.auth.token.RefreshTokenStore;
import com.ll.readycode.global.exception.CustomException;
import com.ll.readycode.global.exception.ErrorCode;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class AuthServiceTest {

  private RefreshTokenService refreshTokenService;

  @Mock private JwtProvider jwtProvider;

  @Mock private RefreshTokenStore refreshTokenStore;

  @BeforeEach
  void setUp() {
    jwtProvider = mock(JwtProvider.class);
    refreshTokenStore = mock(RefreshTokenStore.class);
    refreshTokenService = new RefreshTokenService(jwtProvider, refreshTokenStore);
  }

  @DisplayName("JWT 토큰 재발급 성공 테스트")
  @Test
  void tokenReissueSuccessTest() {

    // given
    String prevRefreshToken = "mock-prev-refresh-token";
    String newAccessToken = "mock-new-access-token";

    when(jwtProvider.getUserIdFromToken(prevRefreshToken)).thenReturn(1L);
    when(refreshTokenStore.get(1L)).thenReturn(Optional.of(prevRefreshToken));
    when(jwtProvider.createAccessToken(1L)).thenReturn(newAccessToken);

    // when
    Token reissueResult = refreshTokenService.reissue(prevRefreshToken);

    // then
    assertThat(newAccessToken).isEqualTo(reissueResult.accessToken());
    assertThat(prevRefreshToken).isEqualTo(reissueResult.refreshToken());
  }

  @DisplayName("JWT 토큰 재발급 실패 테스트 - Refresh 토큰이 유효하지 않을 경우")
  @Test
  void tokenReissueInvalidTokenTest() {

    // given
    String invalidToken = "mock-invalid-refresh-token";

    doThrow(new RuntimeException("Invalid token")).when(jwtProvider).validateToken(invalidToken);

    // when
    CustomException exception =
        assertThrows(CustomException.class, () -> refreshTokenService.reissue(invalidToken));

    // then
    assertThat(ErrorCode.INVALID_TOKEN).isEqualTo(exception.getErrorCode());
  }

  @DisplayName("JWT 토큰 재발급 실패 테스트 - UserId에 해당하는 Refresh 토큰이 존재하지 않을 경우")
  @Test
  void tokenReissueExpiredTokenTest() {

    // given
    String invalidToken = "mock-invalid-refresh-token";

    // when
    CustomException exception =
        assertThrows(CustomException.class, () -> refreshTokenService.reissue(invalidToken));

    // then
    assertThat(ErrorCode.EXPIRED_TOKEN).isEqualTo(exception.getErrorCode());
  }

  @DisplayName("로그아웃 성공 테스트")
  @Test
  void logoutSuccessTest() {

    // given
    String validRefreshToken = "mock-valid-refresh-token";
    Long userId = 1L;

    // when
    doNothing().when(jwtProvider).validateToken(validRefreshToken);
    when(jwtProvider.getUserIdFromToken(validRefreshToken)).thenReturn(userId);

    // then
    refreshTokenService.delete(validRefreshToken);
    verify(refreshTokenStore, times(1)).delete(userId);
  }

  @DisplayName("로그아웃 실패 테스트 - Refresh 토큰이 누락되었을 경우")
  @Test
  void logoutMissingRefreshTokenTest() {

    // given
    String refreshToken = null;

    // when
    CustomException exception =
        assertThrows(CustomException.class, () -> refreshTokenService.delete(refreshToken));

    // then
    assertThat(ErrorCode.MISSING_REFRESH_TOKEN).isEqualTo(exception.getErrorCode());
  }

  @DisplayName("로그아웃 실패 테스트 - 유효하지 않은 Refresh 토큰일 경우")
  @Test
  void logoutInvalidTokenTest() {

    // given
    String refreshToken = "mock-refresh-token";

    doThrow(new RuntimeException("Invalid token")).when(jwtProvider).validateToken(refreshToken);

    // when
    CustomException exception =
        assertThrows(CustomException.class, () -> refreshTokenService.delete(refreshToken));

    // then
    assertThat(ErrorCode.INVALID_TOKEN).isEqualTo(exception.getErrorCode());
  }

  @DisplayName("로그아웃 실패 테스트 - Refresh 토큰에 해당하는 userId가 존재하지 않을 경우")
  @Test
  void logoutAlreadyTest() {

    // given
    String refreshToken = "mock-refresh-token";

    // when
    CustomException exception = assertThrows(CustomException.class, () -> refreshTokenService.delete(refreshToken));

    // then
    assertThat(ErrorCode.LOGOUT_ALREADY).isEqualTo(exception.getErrorCode());
  }
}
