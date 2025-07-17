package com.ll.readycode.global.common.auth.jwt;

import com.ll.readycode.global.common.auth.user.TempUserPrincipal;
import com.ll.readycode.global.exception.CustomException;
import com.ll.readycode.global.exception.ErrorCode;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.UUID;
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

  public String createTempAccessToken(String provider, String providerId, String email) {
    return createTempToken(
            provider,
            providerId,
            email,
            Duration.ofMinutes(jwtProperties.getAccessToken().getValidMinute()));
  }

  public String createRefreshToken() {
    return UUID.randomUUID().toString();
  }

  public void validateToken(String token) {
    Jwts.parserBuilder().setSigningKey(jwtSigningKey).build().parseClaimsJws(token);
  }

  public Object getClaimsFromToken(String token) {

    var claimsJws = Jwts.parserBuilder().setSigningKey(jwtSigningKey).build().parseClaimsJws(token);

    var body = claimsJws.getBody();
    String subject = body.getSubject();

    if ("AUTH".equals(subject)) {
      return body.get("userId", Long.class);

    } else if ("TEMP".equals(subject)) {
      String provider = body.get("provider", String.class);
      String providerId = body.get("providerId", String.class);
      String email = body.get("email", String.class);

      return new TempUserPrincipal(provider, providerId, email);
    }

    throw new CustomException(ErrorCode.INVALID_TOKEN);
  }

  private String createToken(Long userId, Duration validity) {
    Date now = new Date();
    Date expiry = new Date(now.getTime() + validity.toMillis());

    return Jwts.builder()
            .setSubject("AUTH")
            .setIssuedAt(now)
            .setExpiration(expiry)
            .claim("userId", userId)
            .signWith(jwtSigningKey, SignatureAlgorithm.HS256)
            .compact();
  }

  private String createTempToken(
          String provider, String providerId, String email, Duration validity) {
    Date now = new Date();
    Date expiry = new Date(now.getTime() + validity.toMillis());

    return Jwts.builder()
            .setSubject("TEMP")
            .setIssuedAt(now)
            .setExpiration(expiry)
            .claim("provider", provider)
            .claim("providerId", providerId)
            .claim("email", email)
            .signWith(jwtSigningKey, SignatureAlgorithm.HS256)
            .compact();
  }
}
