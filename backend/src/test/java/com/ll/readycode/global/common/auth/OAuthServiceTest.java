package com.ll.readycode.global.common.auth;

import static com.ll.readycode.global.common.auth.OAuthTestHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ll.readycode.api.dto.userauths.UserAuthResponseDto.Token;
import com.ll.readycode.domain.users.userauths.entity.UserAuth;
import com.ll.readycode.domain.users.userauths.repository.UserAuthRepository;
import com.ll.readycode.global.common.auth.jwt.JwtProvider;
import com.ll.readycode.global.common.auth.oauth.properties.OAuthProperties;
import com.ll.readycode.global.common.auth.oauth.properties.OAuthProperties.Provider;
import com.ll.readycode.global.common.auth.oauth.service.GoogleOAuthService;
import com.ll.readycode.global.common.auth.oauth.service.KakaoOAuthService;
import com.ll.readycode.global.common.auth.oauth.service.NaverOAuthService;
import com.ll.readycode.global.common.auth.token.RefreshTokenStore;
import com.ll.readycode.global.exception.CustomException;
import com.ll.readycode.global.exception.ErrorCode;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

public class OAuthServiceTest {

  private KakaoOAuthService kakaoOAuthService;

  private GoogleOAuthService googleOAuthService;

  private NaverOAuthService naverOAuthService;

  @Mock private UserAuthRepository userAuthRepository;

  @Mock private JwtProvider jwtProvider;

  @Mock private RefreshTokenStore refreshTokenStore;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    Provider kakaoProvider = initKakaoProvider();
    Provider googleProvider = initGoogleProvider();
    Provider naverProvider = initNaverProvider();

    OAuthProperties oAuthProperties = mock(OAuthProperties.class);
    when(oAuthProperties.getKakao()).thenReturn(kakaoProvider);
    when(oAuthProperties.getGoogle()).thenReturn(googleProvider);
    when(oAuthProperties.getNaver()).thenReturn(naverProvider);

    kakaoOAuthService =
        initKakaoOAuthService(oAuthProperties, userAuthRepository, jwtProvider, refreshTokenStore);
    googleOAuthService =
        initGoogleOAuthService(oAuthProperties, userAuthRepository, jwtProvider, refreshTokenStore);
    naverOAuthService =
        initNaverOAuthService(oAuthProperties, userAuthRepository, jwtProvider, refreshTokenStore);
  }

  @DisplayName("카카오 로그인 성공 테스트")
  @Test
  void kakaoLoginSuccessTest() {

    // given
    String authCode = "mock-auth-code";
    String email = "testuser@example.com";
    String accessToken = "mock-access-token";
    String refreshToken = "mock-refresh-token";

    UserAuth mockUser = new UserAuth();
    ReflectionTestUtils.setField(mockUser, "id", 1L);
    ReflectionTestUtils.setField(mockUser, "email", email);

    when(userAuthRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
    when(jwtProvider.createAccessToken(1L)).thenReturn(accessToken);
    when(refreshTokenStore.get(1L)).thenReturn(Optional.of(refreshToken));

    // when
    Token loginResult = kakaoOAuthService.login(authCode);

    // then
    assertThat(accessToken).isEqualTo(loginResult.accessToken());
    assertThat(refreshToken).isEqualTo(loginResult.refreshToken());
  }

  @DisplayName("카카오 로그인 실패 테스트 - 가입 이력이 없을 경우")
  @Test
  void kakaoLoginUserNotFoundTest() {

    // given
    String authCode = "mock-auth-code";

    // when
    CustomException exception =
        assertThrows(CustomException.class, () -> kakaoOAuthService.login(authCode));

    // then
    assertThat(ErrorCode.USER_NOT_FOUND).isEqualTo(exception.getErrorCode());
  }

  @DisplayName("구글 로그인 성공 테스트")
  @Test
  void googleLoginSuccessTest() {

    // given
    String authCode = "mock-auth-code";
    String email = "testuser@example.com";
    String accessToken = "mock-access-token";
    String refreshToken = "mock-refresh-token";

    UserAuth mockUser = new UserAuth();
    ReflectionTestUtils.setField(mockUser, "id", 1L);
    ReflectionTestUtils.setField(mockUser, "email", email);

    when(userAuthRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
    when(jwtProvider.createAccessToken(1L)).thenReturn(accessToken);
    when(refreshTokenStore.get(1L)).thenReturn(Optional.of(refreshToken));

    // when
    Token loginResult = googleOAuthService.login(authCode);

    // then
    assertThat(accessToken).isEqualTo(loginResult.accessToken());
    assertThat(refreshToken).isEqualTo(loginResult.refreshToken());
  }

  @DisplayName("구글 로그인 실패 테스트 - 가입 이력이 없을 경우")
  @Test
  void googleLoginUserNotFoundTest() {

    // given
    String authCode = "mock-auth-code";

    // when
    CustomException exception =
        assertThrows(CustomException.class, () -> googleOAuthService.login(authCode));

    // then
    assertThat(ErrorCode.USER_NOT_FOUND).isEqualTo(exception.getErrorCode());
  }

  @DisplayName("네이버 로그인 성공 테스트")
  @Test
  void naverLoginSuccessTest() {

    // given
    String authCode = "mock-auth-code";
    String email = "testuser@example.com";
    String accessToken = "mock-access-token";
    String refreshToken = "mock-refresh-token";

    UserAuth mockUser = new UserAuth();
    ReflectionTestUtils.setField(mockUser, "id", 1L);
    ReflectionTestUtils.setField(mockUser, "email", email);

    when(userAuthRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
    when(jwtProvider.createAccessToken(1L)).thenReturn(accessToken);
    when(refreshTokenStore.get(1L)).thenReturn(Optional.of(refreshToken));

    // when
    Token loginResult = naverOAuthService.login(authCode);

    // then
    assertThat(accessToken).isEqualTo(loginResult.accessToken());
    assertThat(refreshToken).isEqualTo(loginResult.refreshToken());
  }

  @DisplayName("네이버 로그인 실패 테스트 - 가입 이력이 없을 경우")
  @Test
  void naverLoginUserNotFoundTest() {

    // given
    String authCode = "mock-auth-code";

    // when
    CustomException exception =
        assertThrows(CustomException.class, () -> naverOAuthService.login(authCode));

    // then
    assertThat(ErrorCode.USER_NOT_FOUND).isEqualTo(exception.getErrorCode());
  }
}
