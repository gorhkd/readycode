package com.ll.readycode.api.userauths.dto.request;

public class UserAuthRequestDto {

  public record OAuthLogin(String provider, String authCode) {}

  public record TokenReissue(String refreshToken) {}

  public record Logout(String refreshToken) {}
}
