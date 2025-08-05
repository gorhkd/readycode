package com.ll.readycode.api.users.userauths.controller;

import com.ll.readycode.api.users.userauths.dto.request.UserAuthRequestDto.Logout;
import com.ll.readycode.api.users.userauths.dto.request.UserAuthRequestDto.OAuthLogin;
import com.ll.readycode.api.users.userauths.dto.request.UserAuthRequestDto.TokenReissue;
import com.ll.readycode.api.users.userauths.dto.response.UserAuthResponseDto.Token;
import com.ll.readycode.global.common.auth.oauth.factory.OAuthServiceFactory;
import com.ll.readycode.global.common.auth.oauth.service.OAuthService;
import com.ll.readycode.global.common.auth.token.RefreshTokenService;
import com.ll.readycode.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "인증 API", description = "인증 관련 API 입니다.")
public class UserAuthController {

  private final OAuthServiceFactory oAuthServiceFactory;
  private final RefreshTokenService refreshTokenService;

  @PostMapping("/login")
  @Operation(summary = "SNS별 로그인", description = "SNS별 로그인 후 인증에 사용될 토큰을 반환합니다.")
  public ResponseEntity<SuccessResponse<Token>> login(@RequestBody OAuthLogin loginRequest) {

    OAuthService loginService = oAuthServiceFactory.getService(loginRequest.provider());
    Token tokenInfo = loginService.login(loginRequest.authCode());

    return ResponseEntity.ok(SuccessResponse.of(tokenInfo));
  }

  @PostMapping("/reissue")
  @Operation(summary = "인증 토큰 재발급", description = "토큰이 만료되었을 경우, 유효한 인증 토큰을 대상으로 재발급합니다.")
  public ResponseEntity<SuccessResponse<Token>> reissue(@RequestBody TokenReissue reissueRequest) {

    Token tokenInfo = refreshTokenService.reissue(reissueRequest.refreshToken());

    return ResponseEntity.ok(SuccessResponse.of(tokenInfo));
  }

  @PostMapping("/logout")
  @Operation(summary = "로그아웃", description = "해당 유저의 인증 토큰을 삭제하고 로그아웃합니다.")
  public ResponseEntity<SuccessResponse<Void>> logout(@RequestBody Logout logoutRequest) {

    refreshTokenService.delete(logoutRequest.refreshToken());

    return ResponseEntity.noContent().build();
  }
}
