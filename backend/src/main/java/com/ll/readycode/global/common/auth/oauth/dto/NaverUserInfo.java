package com.ll.readycode.global.common.auth.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class NaverUserInfo {

  private String id;

  private String email;

  private String name;

  private String nickname;

  @JsonProperty("profile_image")
  private String profileImage;

  private String gender;

  private String age;

  private String birthday;
}
