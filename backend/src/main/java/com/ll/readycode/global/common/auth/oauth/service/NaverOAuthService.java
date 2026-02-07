package com.ll.readycode.global.common.auth.oauth.service;

import com.ll.readycode.domain.users.userprofiles.service.UserProfileService;
import com.ll.readycode.global.common.auth.jwt.JwtProvider;
import com.ll.readycode.global.common.auth.oauth.dto.NaverTokenResponse;
import com.ll.readycode.global.common.auth.oauth.dto.NaverUserInfo;
import com.ll.readycode.global.common.auth.oauth.properties.OAuthProperties;
import com.ll.readycode.global.common.auth.oauth.properties.OAuthProperties.Provider;
import com.ll.readycode.global.common.auth.token.RefreshTokenStore;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service("naver")
public class NaverOAuthService extends AbstractOAuthService<NaverTokenResponse, NaverUserInfo> {

  private final String provider;

  protected NaverOAuthService(
      OAuthProperties oAuthProperties,
      UserProfileService userProfileService,
      JwtProvider jwtProvider,
      RefreshTokenStore refreshTokenStore) {
    super(oAuthProperties, userProfileService, jwtProvider, refreshTokenStore);
    this.provider = "naver";
  }

  @Override
  protected NaverTokenResponse getAccessToken(String authCode) {
    Provider naver = oAuthProperties.getNaver();

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("grant_type", "authorization_code");
    body.add("client_id", naver.getClientId());
    body.add("client_secret", naver.getClientSecret());
    body.add("redirect_uri", naver.getRedirectUri());
    body.add("code", authCode);

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

    // ✅ ResponseEntity로 받기
    ResponseEntity<NaverTokenResponse> response =
        restTemplate.postForEntity(naver.getTokenUri(), request, NaverTokenResponse.class);

    NaverTokenResponse tokenResponse = response.getBody();

    if (tokenResponse == null || tokenResponse.accessToken() == null) {
      throw new RuntimeException("네이버 토큰 파싱 실패");
    }

    System.out.println("✅ 토큰 파싱 성공: " + tokenResponse.accessToken().substring(0, 10));

    return tokenResponse;
  }

  @Override
  protected NaverUserInfo getUserInfo(String accessToken) {

    Provider naver = oAuthProperties.getNaver();

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);

    HttpEntity<Void> request = new HttpEntity<>(headers);
    ResponseEntity<NaverUserInfo> response =
        restTemplate.exchange(naver.getUserInfoUri(), HttpMethod.GET, request, NaverUserInfo.class);

    return response.getBody();
  }

  @Override
  protected String getAccessTokenFromResponse(NaverTokenResponse tokenResponse) {
    return tokenResponse.accessToken();
  }

  @Override
  protected String extractId(NaverUserInfo userInfo) {
    return userInfo.getId();
  }

  @Override
  protected String extractEmail(NaverUserInfo userInfo) {
    return userInfo.getEmail();
  }

  @Override
  protected String getProvider() {
    return this.provider;
  }
}
