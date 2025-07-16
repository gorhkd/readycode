package com.ll.readycode.api.users.userprofiles.dto.response;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

public class UserProfileResponseDto {

  @Getter
  public static class ProfileWithSocial {

    private final String nickname;

    private final String phoneNumber;

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

    @Builder
    public record Social(String provider, String email) {}
  }
}
