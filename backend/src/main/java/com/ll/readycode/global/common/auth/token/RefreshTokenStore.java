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

  public void save(String uuid, Long userId) {
    redisTemplate
        .opsForValue()
        .set(buildKey(uuid), String.valueOf(userId), refreshTokenExpire);
  }

  public void delete(String uuid) {
    redisTemplate.delete(buildKey(uuid));
  }

  public Optional<String> get(String uuid) {
    return Optional.ofNullable(redisTemplate.opsForValue().get(buildKey(uuid)));
  }

  private String buildKey(String uuid) {
    return "refresh_token:" + uuid;
  }
}
