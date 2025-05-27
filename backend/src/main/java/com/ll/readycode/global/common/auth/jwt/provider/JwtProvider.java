package com.ll.readycode.global.common.auth.jwt.provider;

import com.ll.readycode.global.common.auth.jwt.properties.JwtProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.security.oauth2.jwt.JwtException;
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

  public String createAccessToken(String email) {
    return createToken(email, Duration.ofMinutes(jwtProperties.getAccessToken().getValidMinute()));
  }

  public String createRefreshToken(String email) {
    return createToken(email, Duration.ofDays(jwtProperties.getRefreshToken().getValidDay()));
  }

  private String createToken(String email, Duration validity) {
    Date now = new Date();
    Date expiry = new Date(now.getTime() + validity.toMillis());

    return Jwts.builder()
        .setSubject(email)
        .setIssuedAt(now)
        .setExpiration(expiry)
        .signWith(jwtSigningKey, SignatureAlgorithm.HS256)
        .compact();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(jwtSigningKey).build().parseClaimsJws(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  public String getSubject(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(jwtSigningKey)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }
}
