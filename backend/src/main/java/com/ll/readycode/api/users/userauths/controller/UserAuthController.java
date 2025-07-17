package com.ll.readycode.api.users.userauths.controller;

import com.ll.readycode.api.users.userauths.dto.request.UserAuthRequestDto.Logout;
import com.ll.readycode.api.users.userauths.dto.request.UserAuthRequestDto.OAuthLogin;
import com.ll.readycode.api.users.userauths.dto.request.UserAuthRequestDto.TokenReissue;
import com.ll.readycode.api.users.userauths.dto.response.UserAuthResponseDto.Token;
import com.ll.readycode.global.common.auth.oauth.factory.OAuthServiceFactory;
import com.ll.readycode.global.common.auth.oauth.service.OAuthService;
import com.ll.readycode.global.common.auth.token.RefreshTokenService;
import com.ll.readycode.global.common.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserAuthController {

  private final OAuthServiceFactory oAuthServiceFactory;
  private final RefreshTokenService refreshTokenService;

  @PostMapping("/login")
  public ResponseEntity<SuccessResponse<Token>> login(@RequestBody OAuthLogin loginRequest) {

    OAuthService loginService = oAuthServiceFactory.getService(loginRequest.provider());
    Token tokenInfo = loginService.login(loginRequest.authCode());

    return ResponseEntity.ok(SuccessResponse.of(tokenInfo));
  }

  @PostMapping("/reissue")
  public ResponseEntity<SuccessResponse<Token>> reissue(@RequestBody TokenReissue reissueRequest) {

    Token tokenInfo = refreshTokenService.reissue(reissueRequest.refreshToken());

    return ResponseEntity.ok(SuccessResponse.of(tokenInfo));
  }

  @PostMapping("/logout")
  public ResponseEntity<SuccessResponse<Void>> logout(@RequestBody Logout logoutRequest) {

    refreshTokenService.delete(logoutRequest.refreshToken());

    return ResponseEntity.noContent().build();
  }
}
