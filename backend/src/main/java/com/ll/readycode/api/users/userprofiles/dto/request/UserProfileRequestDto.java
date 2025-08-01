package com.ll.readycode.api.users.userprofiles.dto.request;

import com.ll.readycode.domain.users.userprofiles.entity.UserPurpose;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "유저 프로필 요청 DTO")
public class UserProfileRequestDto {

  @Schema(description = "회원가입 요청 DTO")
  public record Signup(
      @Schema(description = "닉네임 (2~20자, 특수문자 제외)", example = "readyUser")
          @NotBlank
          @Size(min = 2, max = 20)
          @Pattern(regexp = "^[가-힣a-zA-Z0-9_]+$", message = "닉네임은 특수문자를 제외한 2~20자여야 합니다.")
          String nickname,
      @Schema(description = "휴대폰 번호", example = "010-1234-5678")
          @NotBlank
          @Pattern(regexp = "^01[0-9]{1}-?[0-9]{3,4}-?[0-9]{4}$", message = "유효한 휴대폰 번호 형식이어야 합니다.")
          String phoneNumber,
      @Schema(description = "가입 목적 (예: LEARNING, LECTURE)", example = "LEARNING")
          @NotNull(message = "가입 목적은 필수 항목입니다.")
          UserPurpose purpose) {}

  @Schema(description = "회원 정보 수정 요청 DTO")
  public record UpdateProfile(
      @Schema(description = "닉네임 (2~20자, 특수문자 제외)", example = "readyUser")
          @NotBlank
          @Size(min = 2, max = 20)
          @Pattern(regexp = "^[가-힣a-zA-Z0-9_]+$", message = "닉네임은 특수문자를 제외한 2~20자여야 합니다.")
          String nickname) {}
}
