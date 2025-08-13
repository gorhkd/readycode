package com.ll.readycode.api.users.userauths.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "인증 응답 DTO")
public class UserAuthResponseDto {

  @Schema(description = "토큰 응답 DTO")
  @Builder
  public record Token(
      @Schema(
              description = "액세스 토큰",
              example =
                  "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjMiLCJpYXQiOjE3MDE1MDM4NDksImV4cCI6MTcwMTUwNDIwOX0.pDlZhx7nBBr_Q9qWZKUcq3KgoZ7ePQUf6pDgHTYFcDk")
          String accessToken,
      @Schema(description = "리프레시 토큰", example = "cc3dc914-646c-4fd1-8044-78d54e4b4e44")
          String refreshToken,
      @Schema(description = "회원 등록 여부", example = "true") boolean isRegistered) {}
}
