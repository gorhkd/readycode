package com.ll.readycode.api.dto.userauths;

public class UserAuthRequestDto {

  public record OAuthLogin(String provider, String authCode) {}

  public record TokenReissue(String refreshToken) {}
}
