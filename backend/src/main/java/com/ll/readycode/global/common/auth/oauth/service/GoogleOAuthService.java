package com.ll.readycode.global.common.auth.oauth.service;

import com.ll.readycode.domain.users.userprofiles.service.UserProfileService;
import com.ll.readycode.global.common.auth.jwt.JwtProvider;
import com.ll.readycode.global.common.auth.oauth.dto.GoogleTokenResponse;
import com.ll.readycode.global.common.auth.oauth.dto.GoogleUserInfo;
import com.ll.readycode.global.common.auth.oauth.properties.OAuthProperties;
import com.ll.readycode.global.common.auth.oauth.properties.OAuthProperties.Provider;
import com.ll.readycode.global.common.auth.token.RefreshTokenStore;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service("google")
public class GoogleOAuthService extends AbstractOAuthService<GoogleTokenResponse, GoogleUserInfo> {

  private final String provider;

  protected GoogleOAuthService(
      OAuthProperties oAuthProperties,
      UserProfileService userProfileService,
      JwtProvider jwtProvider,
      RefreshTokenStore refreshTokenStore) {
    super(oAuthProperties, userProfileService, jwtProvider, refreshTokenStore);
    this.provider = "google";
  }

  @Override
  protected GoogleTokenResponse getAccessToken(String authCode) {

    Provider google = oAuthProperties.getGoogle();

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("grant_type", "authorization_code");
    body.add("client_id", google.getClientId());
    body.add("client_secret", google.getClientSecret());
    body.add("redirect_uri", google.getRedirectUri());
    body.add("code", authCode);

    HttpEntity<?> request = new HttpEntity<>(body, headers);
    return restTemplate.postForObject(google.getTokenUri(), request, GoogleTokenResponse.class);
  }

  @Override
  protected GoogleUserInfo getUserInfo(String accessToken) {

    Provider google = oAuthProperties.getGoogle();

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);

    HttpEntity<Void> request = new HttpEntity<>(headers);
    ResponseEntity<GoogleUserInfo> response =
        restTemplate.exchange(
            google.getUserInfoUri(), HttpMethod.GET, request, GoogleUserInfo.class);

    return response.getBody();
  }

  @Override
  protected String getAccessTokenFromResponse(GoogleTokenResponse tokenResponse) {
    return tokenResponse.accessToken();
  }

  @Override
  protected String extractId(GoogleUserInfo userInfo) {
    return userInfo.getId();
  }

  @Override
  protected String extractEmail(GoogleUserInfo userInfo) {
    return userInfo.getEmail();
  }

  @Override
  protected String getProvider() {
    return this.provider;
  }
}
