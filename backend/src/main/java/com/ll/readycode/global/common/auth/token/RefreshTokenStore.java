package com.ll.readycode.global.common.auth.token;

import com.ll.readycode.global.common.auth.jwt.JwtProperties;
import java.time.Duration;
import java.util.Optional;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenStore {

  private final RedisTemplate<String, String> redisTemplate;
  private final JwtProperties jwtProperties;
  private final Duration refreshTokenExpire;

  public RefreshTokenStore(
      RedisTemplate<String, String> redisTemplate, JwtProperties jwtProperties) {
    this.redisTemplate = redisTemplate;
    this.jwtProperties = jwtProperties;
    this.refreshTokenExpire = Duration.ofDays(jwtProperties.getRefreshToken().getValidDay());
  }

  public void save(String refreshToken, Long userId) {
    redisTemplate
        .opsForValue()
        .set(buildKey(refreshToken), String.valueOf(userId), refreshTokenExpire);
  }

  public void delete(String refreshToken) {
    redisTemplate.delete(buildKey(refreshToken));
  }

  public Optional<String> get(String refreshToken) {
    return Optional.ofNullable(redisTemplate.opsForValue().get(buildKey(refreshToken)));
  }

  private String buildKey(String refreshToken) {
    return "refresh_token:" + refreshToken;
  }
}
