package com.ll.readycode.global.common.auth.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "custom.jwt")
@Getter
@Setter
public class JwtProperties {

  private String secretKey;
  private TempAccessToken tempAccessToken;
  private AccessToken accessToken;
  private RefreshToken refreshToken;

  @Getter
  @Setter
  public static class TempAccessToken {
    private int validMinute;
  }

  @Getter
  @Setter
  public static class AccessToken {
    private int validMinute;
  }

  @Getter
  @Setter
  public static class RefreshToken {
    private int validDay;
  }
}
