package com.ll.readycode.global.common.auth.oauth.service;

import com.ll.readycode.api.dto.userauths.UserAuthResponseDto.Token;

public interface OAuthService {

  Token login(String authCode);
}
