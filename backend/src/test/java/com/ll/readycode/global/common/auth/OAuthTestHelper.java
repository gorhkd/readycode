package com.ll.readycode.global.common.auth;

import com.ll.readycode.domain.users.userauths.repository.UserAuthRepository;
import com.ll.readycode.global.common.auth.jwt.JwtProvider;
import com.ll.readycode.global.common.auth.oauth.dto.*;
import com.ll.readycode.global.common.auth.oauth.properties.OAuthProperties;
import com.ll.readycode.global.common.auth.oauth.properties.OAuthProperties.Provider;
import com.ll.readycode.global.common.auth.oauth.service.GoogleOAuthService;
import com.ll.readycode.global.common.auth.oauth.service.KakaoOAuthService;
import com.ll.readycode.global.common.auth.oauth.service.NaverOAuthService;
import com.ll.readycode.global.common.auth.token.RefreshTokenStore;
import org.springframework.test.util.ReflectionTestUtils;

public class OAuthTestHelper {

  private static final String kakaoProvider = "kakao";
  private static final String kakaoClientId = "kakao-test-client-id";
  private static final String kakaoClientSecret = "kakao-test-secret";
  private static final String kakaoRedirectUri = "kakao-test-redirect-uri";
  private static final String kakaoTokenUri = "kakao-test-token-uri";
  private static final String kakaoUserInfoUri = "kakao-test-user-info-uri";

  private static final String googleProvider = "google";
  private static final String googleClientId = "google-test-client-id";
  private static final String googleClientSecret = "google-test-secret";
  private static final String googleRedirectUri = "google-test-redirect-uri";
  private static final String googleTokenUri = "google-test-token-uri";
  private static final String googleUserInfoUri = "google-test-user-info-uri";

  private static final String naverProvider = "naver";
  private static final String naverClientId = "naver-test-client-id";
  private static final String naverClientSecret = "naver-test-secret";
  private static final String naverRedirectUri = "naver-test-redirect-uri";
  private static final String naverTokenUri = "naver-test-token-uri";
  private static final String naverUserInfoUri = "naver-test-user-info-uri";

  private static final String accessToken = "mock-access-token";
  private static final String tokenType = "mock-token-type";
  private static final String refreshToken = "mock-refresh-token";
  private static final int expiresIn = 3600;
  private static final String scope = "mock-scope";
  private static final String idToken = "mock-id-token";

  private static final String nickname = "테스트유저";
  private static final String email = "testuser@example.com";

  public static Provider initKakaoProvider() {

    Provider kakaoProvider = new Provider();

    kakaoProvider.setClientId(kakaoClientId);
    kakaoProvider.setClientSecret(kakaoClientSecret);
    kakaoProvider.setRedirectUri(kakaoRedirectUri);
    kakaoProvider.setTokenUri(kakaoTokenUri);
    kakaoProvider.setUserInfoUri(kakaoUserInfoUri);

    return kakaoProvider;
  }

  public static Provider initGoogleProvider() {

    Provider googleProvider = new Provider();

    googleProvider.setClientId(googleClientId);
    googleProvider.setClientSecret(googleClientSecret);
    googleProvider.setRedirectUri(googleRedirectUri);
    googleProvider.setTokenUri(googleTokenUri);
    googleProvider.setUserInfoUri(googleUserInfoUri);

    return googleProvider;
  }

  public static Provider initNaverProvider() {

    Provider naverProvider = new Provider();

    naverProvider.setClientId(naverClientId);
    naverProvider.setClientSecret(naverClientSecret);
    naverProvider.setRedirectUri(naverRedirectUri);
    naverProvider.setTokenUri(naverTokenUri);
    naverProvider.setUserInfoUri(naverUserInfoUri);

    return naverProvider;
  }

  public static KakaoOAuthService initKakaoOAuthService(
      OAuthProperties oAuthProperties,
      UserAuthRepository userAuthRepository,
      JwtProvider jwtProvider,
      RefreshTokenStore refreshTokenStore) {

    return new KakaoOAuthService(
        oAuthProperties, userAuthRepository, jwtProvider, refreshTokenStore) {

      @Override
      protected KakaoTokenResponse getAccessToken(String authCode) {
        return new KakaoTokenResponse(accessToken, tokenType, refreshToken, expiresIn, scope);
      }

      @Override
      protected KakaoUserInfo getUserInfo(String accessToken) {

        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo();
        KakaoUserInfo.KakaoAccount kakaoAccount = new KakaoUserInfo.KakaoAccount();
        KakaoUserInfo.Profile profile = new KakaoUserInfo.Profile();

        ReflectionTestUtils.setField(profile, "nickname", nickname);
        ReflectionTestUtils.setField(kakaoAccount, "email", email);
        ReflectionTestUtils.setField(kakaoAccount, "profile", profile);
        ReflectionTestUtils.setField(kakaoUserInfo, "kakaoAccount", kakaoAccount);

        return kakaoUserInfo;
      }

      @Override
      protected String getAccessTokenFromResponse(KakaoTokenResponse tokenResponse) {
        return tokenResponse.accessToken();
      }

      @Override
      protected String extractEmail(KakaoUserInfo userInfo) {
        return userInfo.getEmail();
      }

      @Override
      protected String extractId(KakaoUserInfo userInfo) {
        return kakaoClientId;
      }

      @Override
      protected String getProvider() {
        return kakaoProvider;
      }
    };
  }

  public static GoogleOAuthService initGoogleOAuthService(
      OAuthProperties oAuthProperties,
      UserAuthRepository userAuthRepository,
      JwtProvider jwtProvider,
      RefreshTokenStore refreshTokenStore) {

    return new GoogleOAuthService(
        oAuthProperties, userAuthRepository, jwtProvider, refreshTokenStore) {

      @Override
      protected GoogleTokenResponse getAccessToken(String authCode) {
        return new GoogleTokenResponse(
            accessToken, expiresIn, refreshToken, scope, tokenType, idToken);
      }

      @Override
      protected GoogleUserInfo getUserInfo(String accessToken) {

        GoogleUserInfo googleUserInfo = new GoogleUserInfo();

        ReflectionTestUtils.setField(googleUserInfo, "name", nickname);
        ReflectionTestUtils.setField(googleUserInfo, "email", email);

        return googleUserInfo;
      }

      @Override
      protected String getAccessTokenFromResponse(GoogleTokenResponse tokenResponse) {
        return tokenResponse.accessToken();
      }

      @Override
      protected String extractEmail(GoogleUserInfo userInfo) {
        return userInfo.getEmail();
      }

      @Override
      protected String extractId(GoogleUserInfo userInfo) {
        return googleClientId;
      }

      @Override
      protected String getProvider() {
        return googleProvider;
      }
    };
  }

  public static NaverOAuthService initNaverOAuthService(
      OAuthProperties oAuthProperties,
      UserAuthRepository userAuthRepository,
      JwtProvider jwtProvider,
      RefreshTokenStore refreshTokenStore) {

    return new NaverOAuthService(
        oAuthProperties, userAuthRepository, jwtProvider, refreshTokenStore) {

      @Override
      protected NaverTokenResponse getAccessToken(String authCode) {
        return new NaverTokenResponse(
            accessToken, refreshToken, tokenType, String.valueOf(expiresIn));
      }

      @Override
      protected NaverUserInfo getUserInfo(String accessToken) {

        NaverUserInfo naverUserInfo = new NaverUserInfo();

        ReflectionTestUtils.setField(naverUserInfo, "nickname", nickname);
        ReflectionTestUtils.setField(naverUserInfo, "email", email);

        return naverUserInfo;
      }

      @Override
      protected String getAccessTokenFromResponse(NaverTokenResponse tokenResponse) {
        return tokenResponse.accessToken();
      }

      @Override
      protected String extractEmail(NaverUserInfo userInfo) {
        return userInfo.getEmail();
      }

      @Override
      protected String extractId(NaverUserInfo userInfo) {
        return naverClientId;
      }

      @Override
      protected String getProvider() {
        return naverProvider;
      }
    };
  }
}
