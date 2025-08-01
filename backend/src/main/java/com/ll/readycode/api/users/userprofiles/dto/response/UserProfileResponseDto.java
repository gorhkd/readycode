package com.ll.readycode.api.users.userprofiles.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "회원 프로필 응답 DTO")
public class UserProfileResponseDto {

  @Schema(description = "소셜 계정이 포함된 회원 프로필 정보")
  @Getter
  public static class ProfileWithSocial {

    @Schema(description = "회원 닉네임", example = "readyUser")
    private final String nickname;

    @Schema(description = "회원 전화번호", example = "010-1234-5678")
    private final String phoneNumber;

    @Schema(description = "연동된 소셜 계정 목록")
    private final List<Social> socials;

    @Builder
    public ProfileWithSocial(String nickname, String phoneNumber) {
      this.nickname = nickname;
      this.phoneNumber = phoneNumber;
      this.socials = new ArrayList<>();
    }

    public void addSocial(Social social) {
      this.socials.add(social);
    }

    @Schema(description = "소셜 계정 정보")
    @Builder
    public record Social(
        @Schema(description = "소셜 제공자", example = "kakao") String provider,
        @Schema(description = "소셜 계정 이메일", example = "user@kakao.com") String email) {}
  }
}
