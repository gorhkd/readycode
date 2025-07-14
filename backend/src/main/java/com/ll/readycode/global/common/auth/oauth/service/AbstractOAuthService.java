package com.ll.readycode.global.common.auth.oauth.service;

import com.ll.readycode.api.userauths.dto.response.UserAuthResponseDto.Token;
import com.ll.readycode.domain.users.userprofiles.service.UserProfileService;
import com.ll.readycode.global.common.auth.jwt.JwtProvider;
import com.ll.readycode.global.common.auth.oauth.properties.OAuthProperties;
import com.ll.readycode.global.common.auth.token.RefreshTokenStore;
import com.ll.readycode.global.exception.CustomException;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.client.RestTemplate;

public abstract class AbstractOAuthService<T, U> implements OAuthService {

  protected final OAuthProperties oAuthProperties;
  protected final UserProfileService userProfileService;
  protected final JwtProvider jwtProvider;
  protected final RestTemplate restTemplate = new RestTemplate();
  protected final RefreshTokenStore refreshTokenStore;

  protected AbstractOAuthService(
      OAuthProperties oAuthProperties,
      UserProfileService userProfileService,
      JwtProvider jwtProvider,
      RefreshTokenStore refreshTokenStore) {
    this.oAuthProperties = oAuthProperties;
    this.userProfileService = userProfileService;
    this.jwtProvider = jwtProvider;
    this.refreshTokenStore = refreshTokenStore;
  }

  @Override
  public Token login(String authCode) {

    T tokenResponse = getAccessToken(authCode);
    U userInfo = getUserInfo(getAccessTokenFromResponse(tokenResponse));
    String provider = getProvider();
    String providerId = extractId(userInfo);
    String email = extractEmail(userInfo);

    Token token;

    try {
      // 가입 이력이 있을 경우, AccessToken & RefreshToken 발급
      Long userId = userProfileService.getUserIdBySocialInfo(provider, providerId);

      String accessToken = jwtProvider.createAccessToken(userId);
      String refreshToken = refreshTokenStore.get(userId).orElse(null);

      boolean needToCreate = (refreshToken == null);

      // Redis에 Refresh 토큰이 존재하지 않을 경우, Refresh 토큰 생성 및 Redis 저장
      if (!needToCreate) {
        try {
          jwtProvider.validateToken(refreshToken);
        } catch (JwtException | IllegalArgumentException e) {
          needToCreate = true;
        }
      }

      if (needToCreate) {
        refreshToken = jwtProvider.createRefreshToken();
        refreshTokenStore.save(userId, refreshToken);
      }

      token =
          Token.builder()
              .accessToken(accessToken)
              .refreshToken(refreshToken)
              .isRegistered(true)
              .build();

    } catch (CustomException ce) {
      // 가입 이력이 없을 경우, 임시 AccessToken 발급
      String tempAccessToken = jwtProvider.createTempAccessToken(provider, providerId, email);

      token = Token.builder().accessToken(tempAccessToken).isRegistered(false).build();
    }

    return token;
  }

  /**
   * SNS 인가 코드를 통해 Access 토큰을 요청합니다.
   *
   * @param authCode SNS 로그인 성공 후 리디렉션으로 전달된 인가 코드
   * @return TokenResponse Access & Refresh 토큰 정보
   */
  protected abstract T getAccessToken(String authCode);

  /**
   * SNS API를 통해 사용자 정보를 조회합니다.
   *
   * @param accessToken SNS 통해 발급받은 액세스 토큰
   * @return UserInfo 사용자 프로필 정보
   */
  protected abstract U getUserInfo(String accessToken);

  /**
   * SNS Access & Refresh 토큰 정보 내 AccessToken을 반환합니다.
   *
   * @param tokenResponse Access & Refresh 토큰 정보
   * @return String Access 토큰 정보
   */
  protected abstract String getAccessTokenFromResponse(T tokenResponse);

  /**
   * SNS API를 통해 얻은 사용자 정보 내 ID 정보를 반환합니다.
   *
   * @param userInfo 사용자 프로필 정보
   * @return String 유저의 ID 정보
   */
  protected abstract String extractId(U userInfo);

  /**
   * SNS API를 통해 얻은 사용자 정보 내 이메일 정보를 반환합니다.
   *
   * @param userInfo 사용자 프로필 정보
   * @return String 유저의 이메일 정보
   */
  protected abstract String extractEmail(U userInfo);

  /**
   * Provider 정보를 반환합니다.
   *
   * @return String Provider 정보
   */
  protected abstract String getProvider();
}
