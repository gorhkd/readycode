package com.ll.readycode.api.users.userauths.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "인증 요청 DTO")
public class UserAuthRequestDto {

  @Schema(description = "소셜 로그인 요청 DTO")
  public record OAuthLogin(
      @Schema(description = "OAuth 공급자 (예: google, kakao, naver)", example = "kakao")
          String provider,
      @Schema(
              description = "SNS 서버에서 응답 받은 인가 코드",
              example = "O4WxKz1w9v4i9X3zkQXxHbR9x1d5aZbA12345678xyzABC")
          String authCode) {}

  @Schema(description = "토큰 재발급 요청 DTO")
  public record TokenReissue(
      @Schema(description = "리프레시 토큰", example = "cc3dc914-646c-4fd1-8044-78d54e4b4e44")
          String refreshToken) {}

  @Schema(description = "로그아웃 요청 DTO")
  public record Logout(
      @Schema(description = "리프레시 토큰", example = "cc3dc914-646c-4fd1-8044-78d54e4b4e44")
          String refreshToken) {}
}
