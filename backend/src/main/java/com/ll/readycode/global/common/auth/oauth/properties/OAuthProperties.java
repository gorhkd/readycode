package com.ll.readycode.global.common.auth.oauth.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "custom.oauth")
@Getter
@Setter
public class OAuthProperties {

  private Provider kakao;
  private Provider google;
  private Provider naver;

  @Getter
  @Setter
  public static class Provider {

    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String tokenUri;
    private String userInfoUri;
  }
}
