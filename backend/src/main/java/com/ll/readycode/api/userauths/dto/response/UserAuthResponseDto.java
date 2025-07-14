package com.ll.readycode.api.userauths.dto.response;

import lombok.Builder;

public class UserAuthResponseDto {

  @Builder
  public record Token(String accessToken, String refreshToken, boolean isRegistered) {}
}
