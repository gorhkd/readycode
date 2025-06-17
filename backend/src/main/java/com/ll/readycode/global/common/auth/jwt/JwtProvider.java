package com.ll.readycode.global.common.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

  private final JwtProperties jwtProperties;
  private final SecretKey jwtSigningKey;

  public JwtProvider(JwtProperties jwtProperties) {
    this.jwtProperties = jwtProperties;
    this.jwtSigningKey =
        Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
  }

  public String createAccessToken(Long userId) {
    return createToken(userId, Duration.ofMinutes(jwtProperties.getAccessToken().getValidMinute()));
  }

  public String createRefreshToken(Long userId) {
    return createToken(userId, Duration.ofDays(jwtProperties.getRefreshToken().getValidDay()));
  }

  private String createToken(Long userId, Duration validity) {
    Date now = new Date();
    Date expiry = new Date(now.getTime() + validity.toMillis());

    return Jwts.builder()
        .setSubject(userId.toString())
        .setIssuedAt(now)
        .setExpiration(expiry)
        .signWith(jwtSigningKey, SignatureAlgorithm.HS256)
        .compact();
  }

  public void validateToken(String token) {
    Jwts.parserBuilder().setSigningKey(jwtSigningKey).build().parseClaimsJws(token);
  }

  public Long getUserIdFromToken(String token) {
    return Long.parseLong(
        Jwts.parserBuilder()
            .setSigningKey(jwtSigningKey)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject());
  }
}
