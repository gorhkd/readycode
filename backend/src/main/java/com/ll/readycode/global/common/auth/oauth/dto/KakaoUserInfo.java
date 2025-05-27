package com.ll.readycode.global.common.auth.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class KakaoUserInfo {

  private Long id;

  @JsonProperty("kakao_account")
  private KakaoAccount kakaoAccount;

  public String getEmail() {
    return kakaoAccount.getEmail();
  }

  public String getNickname() {
    return kakaoAccount.getProfile().getNickname();
  }

  @Getter
  public static class KakaoAccount {
    private String email;
    private Profile profile;
  }

  @Getter
  public static class Profile {
    private String nickname;
  }
}
