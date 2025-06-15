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

  public void save(String email, String refreshToken) {
    redisTemplate.opsForValue().set(buildKey(email), refreshToken, refreshTokenExpire);
  }

  public void delete(String email) {
    redisTemplate.delete(buildKey(email));
  }

  public Optional<String> get(String email) {
    return Optional.ofNullable(redisTemplate.opsForValue().get(buildKey(email)));
  }

  private String buildKey(String email) {
    return "refresh_token:" + email;
  }
}
