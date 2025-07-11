package com.ll.readycode.api.dto.userauths;

import lombok.Builder;

public class UserAuthResponseDto {

  @Builder
  public record Token(String accessToken, String refreshToken, boolean isRegistered) {}
}
