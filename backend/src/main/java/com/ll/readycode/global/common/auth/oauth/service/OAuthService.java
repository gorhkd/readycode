package com.ll.readycode.global.common.auth.oauth.service;

import com.ll.readycode.api.userauths.dto.response.UserAuthResponseDto.Token;

public interface OAuthService {

  Token login(String authCode);
}
