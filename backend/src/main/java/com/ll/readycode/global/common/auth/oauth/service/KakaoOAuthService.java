package com.ll.readycode.global.common.auth.oauth.service;

import static com.ll.readycode.global.exception.ErrorCode.USER_NOT_FOUND;

import com.ll.readycode.api.dto.userauths.UserAuthResponseDto.Token;
import com.ll.readycode.domain.users.userauths.entity.UserAuth;
import com.ll.readycode.domain.users.userauths.repository.UserAuthRepository;
import com.ll.readycode.global.common.auth.jwt.provider.JwtProvider;
import com.ll.readycode.global.common.auth.oauth.dto.KakaoTokenResponse;
import com.ll.readycode.global.common.auth.oauth.dto.KakaoUserInfo;
import com.ll.readycode.global.common.auth.oauth.properties.OAuthProperties;
import com.ll.readycode.global.common.auth.oauth.properties.OAuthProperties.Provider;
import com.ll.readycode.global.common.auth.token.RefreshTokenStore;
import com.ll.readycode.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service("kakao")
@RequiredArgsConstructor
public class KakaoOAuthService implements OAuthService {

  private final OAuthProperties oAuthProperties;
  private final UserAuthRepository userAuthRepository;
  private final JwtProvider jwtProvider;
  private final RestTemplate restTemplate = new RestTemplate();
  private final RefreshTokenStore refreshTokenStore;

  @Override
  public Token login(String authCode) {

    KakaoTokenResponse kakaoToken = getAccessToken(authCode);
    KakaoUserInfo userInfo = getUserInfo(kakaoToken.accessToken());

    // 가입 이력이 없을 경우, 404 에러 반환
    UserAuth user =
        userAuthRepository
            .findByEmail(userInfo.getEmail())
            .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

    String accessToken = jwtProvider.createAccessToken(user.getEmail());
    String refreshToken = refreshTokenStore.get(user.getEmail());

    // Redis에 Refresh 토큰이 존재하지 않을 경우, Refresh 토큰 생성 및 Redis 저장
    if (refreshToken == null || !jwtProvider.validateToken(refreshToken)) {
      refreshToken = jwtProvider.createRefreshToken(user.getEmail());
      refreshTokenStore.save(user.getEmail(), refreshToken);
    }

    return Token.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
  }

  /**
   * Kakao 인가 코드를 통해 Access 토큰을 요청합니다.
   *
   * @param authCode Kakao 로그인 성공 후 리디렉션으로 전달된 인가 코드
   * @return KakaoTokenResponse Kakao로부터 받은 Access & Refresh 토큰 정보
   */
  private KakaoTokenResponse getAccessToken(String authCode) {

    Provider kakao = oAuthProperties.getKakao();

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("grant_type", "authorization_code");
    body.add("client_id", kakao.getClientId());
    body.add("client_secret", kakao.getClientSecret());
    body.add("redirect_url", kakao.getRedirectUri());
    body.add("code", authCode);

    HttpEntity<?> request = new HttpEntity<>(body, headers);
    return restTemplate.postForObject(kakao.getTokenUri(), request, KakaoTokenResponse.class);
  }

  /**
   * Kakao API를 통해 사용자 정보를 조회합니다.
   *
   * @param accessToken Kakao에서 발급받은 액세스 토큰
   * @return KakaoUserInfo Kakao 사용자 프로필 정보
   */
  private KakaoUserInfo getUserInfo(String accessToken) {

    Provider kakao = oAuthProperties.getKakao();

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);

    HttpEntity<Void> request = new HttpEntity<>(headers);
    ResponseEntity<KakaoUserInfo> response =
        restTemplate.exchange(kakao.getUserInfoUri(), HttpMethod.GET, request, KakaoUserInfo.class);
    return response.getBody();
  }
}
