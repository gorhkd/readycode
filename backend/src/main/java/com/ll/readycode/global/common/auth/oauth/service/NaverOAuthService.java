package com.ll.readycode.global.common.auth.oauth.service;

import com.ll.readycode.domain.users.userauths.repository.UserAuthRepository;
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

  protected NaverOAuthService(
      OAuthProperties oAuthProperties,
      UserAuthRepository userAuthRepository,
      JwtProvider jwtProvider,
      RefreshTokenStore refreshTokenStore) {
    super(oAuthProperties, userAuthRepository, jwtProvider, refreshTokenStore);
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
    return restTemplate.postForObject(naver.getTokenUri(), request, NaverTokenResponse.class);
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
  protected String extractEmail(NaverUserInfo userInfo) {
    return userInfo.getEmail();
  }
}
