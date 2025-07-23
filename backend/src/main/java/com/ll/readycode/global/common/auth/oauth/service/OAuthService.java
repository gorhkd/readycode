package com.ll.readycode.global.common.auth.oauth.service;

import com.ll.readycode.api.users.userauths.dto.response.UserAuthResponseDto.Token;

public interface OAuthService {

  Token login(String authCode);
}
