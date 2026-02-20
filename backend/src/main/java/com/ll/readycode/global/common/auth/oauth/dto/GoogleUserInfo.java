package com.ll.readycode.global.common.auth.oauth.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class GoogleUserInfo {

  @JsonAlias({"sub", "id"})
  private String id;

  private String email;

  @JsonAlias({"email_verified", "verified_email"})
  private boolean verifiedEmail;

  private String name;

  @JsonProperty("given_name")
  private String givenName;

  @JsonProperty("family_name")
  private String familyName;

  @JsonProperty("picture")
  private String picture;

  private String locale;
}
