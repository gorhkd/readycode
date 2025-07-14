package com.ll.readycode.api.userprofiles.dto.request;

import com.ll.readycode.domain.users.userprofiles.entity.UserPurpose;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserProfileRequestDto {

  public record Signup(
      @NotBlank
          @Size(min = 2, max = 20)
          @Pattern(regexp = "^[가-힣a-zA-Z0-9_]+$", message = "닉네임은 특수문자를 제외한 2~20자여야 합니다.")
          String nickname,
      @NotBlank
          @Pattern(regexp = "^01[0-9]{1}-?[0-9]{3,4}-?[0-9]{4}$", message = "유효한 휴대폰 번호 형식이어야 합니다.")
          String phoneNumber,
      @NotNull(message = "가입 목적은 필수 항목입니다.") UserPurpose purpose) {}

  public record UpdateProfile(
      @NotBlank
          @Size(min = 2, max = 20)
          @Pattern(regexp = "^[가-힣a-zA-Z0-9_]+$", message = "닉네임은 특수문자를 제외한 2~20자여야 합니다.")
          String nickname) {}
}
