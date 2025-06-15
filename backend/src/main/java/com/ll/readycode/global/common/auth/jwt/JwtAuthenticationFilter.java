package com.ll.readycode.global.common.auth.jwt;

import com.ll.readycode.global.exception.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtProvider jwtProvider;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String token = resolveToken(request);

    try {
      jwtProvider.validateToken(token);

      String email = jwtProvider.getSubject(token);
      Authentication auth =
          new UsernamePasswordAuthenticationToken(
              email, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));

      SecurityContextHolder.getContext().setAuthentication(auth);

    } catch (ExpiredJwtException e) {
      request.setAttribute("errorCode", ErrorCode.EXPIRED_TOKEN);

    } catch (JwtException | IllegalArgumentException e) {
      request.setAttribute("errorCode", ErrorCode.INVALID_TOKEN);
    }

    filterChain.doFilter(request, response);
  }

  private String resolveToken(HttpServletRequest request) {

    String bearer = request.getHeader("Authorization");
    if (bearer == null || !bearer.startsWith("Bearer ")) {
      return null;
    }

    return bearer.substring(7);
  }
}
