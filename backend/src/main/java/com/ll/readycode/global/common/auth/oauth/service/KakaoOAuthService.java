package com.ll.readycode.global.common.auth.oauth.service;

import com.ll.readycode.domain.users.userauths.repository.UserAuthRepository;
import com.ll.readycode.global.common.auth.jwt.JwtProvider;
import com.ll.readycode.global.common.auth.oauth.dto.KakaoTokenResponse;
import com.ll.readycode.global.common.auth.oauth.dto.KakaoUserInfo;
import com.ll.readycode.global.common.auth.oauth.properties.OAuthProperties;
import com.ll.readycode.global.common.auth.oauth.properties.OAuthProperties.Provider;
import com.ll.readycode.global.common.auth.token.RefreshTokenStore;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service("kakao")
public class KakaoOAuthService extends AbstractOAuthService<KakaoTokenResponse, KakaoUserInfo> {

  protected KakaoOAuthService(
      OAuthProperties oAuthProperties,
      UserAuthRepository userAuthRepository,
      JwtProvider jwtProvider,
      RefreshTokenStore refreshTokenStore) {
    super(oAuthProperties, userAuthRepository, jwtProvider, refreshTokenStore);
  }

  @Override
  protected KakaoTokenResponse getAccessToken(String authCode) {

    Provider kakao = oAuthProperties.getKakao();

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("grant_type", "authorization_code");
    body.add("client_id", kakao.getClientId());
    body.add("client_secret", kakao.getClientSecret());
    body.add("redirect_uri", kakao.getRedirectUri());
    body.add("code", authCode);

    HttpEntity<?> request = new HttpEntity<>(body, headers);
    return restTemplate.postForObject(kakao.getTokenUri(), request, KakaoTokenResponse.class);
  }

  @Override
  protected KakaoUserInfo getUserInfo(String accessToken) {

    Provider kakao = oAuthProperties.getKakao();

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);

    HttpEntity<Void> request = new HttpEntity<>(headers);
    ResponseEntity<KakaoUserInfo> response =
        restTemplate.exchange(kakao.getUserInfoUri(), HttpMethod.GET, request, KakaoUserInfo.class);

    return response.getBody();
  }

  @Override
  protected String getAccessTokenFromResponse(KakaoTokenResponse tokenResponse) {
    return tokenResponse.accessToken();
  }

  @Override
  protected String extractEmail(KakaoUserInfo userInfo) {
    return userInfo.getEmail();
  }
}
