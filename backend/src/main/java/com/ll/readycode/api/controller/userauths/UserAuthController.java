package com.ll.readycode.api.controller.userauths;

import com.ll.readycode.api.dto.userauths.UserAuthRequestDto.OAuthLogin;
import com.ll.readycode.api.dto.userauths.UserAuthResponseDto.Token;
import com.ll.readycode.global.common.auth.oauth.factory.OAuthServiceFactory;
import com.ll.readycode.global.common.auth.oauth.service.OAuthService;
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

  @PostMapping("/login")
  public ResponseEntity<SuccessResponse<Token>> login(@RequestBody OAuthLogin loginRequest) {

    OAuthService loginService = oAuthServiceFactory.getService(loginRequest.provider());
    Token tokenInfo = loginService.login(loginRequest.authCode());

    return ResponseEntity.ok(SuccessResponse.of(tokenInfo));
  }
}
