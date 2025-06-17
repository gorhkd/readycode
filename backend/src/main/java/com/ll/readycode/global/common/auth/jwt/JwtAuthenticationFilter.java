package com.ll.readycode.global.common.auth.jwt;

import static com.ll.readycode.global.common.auth.jwt.JwtFilterExcludeProperties.EXCLUDE_URIS;

import com.ll.readycode.global.common.auth.user.CustomUserDetails;
import com.ll.readycode.global.common.auth.user.CustomUserDetailsService;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtProvider jwtProvider;
  private final AntPathMatcher pathMatcher = new AntPathMatcher();
  private final CustomUserDetailsService customUserDetailsService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String token = resolveToken(request);

    if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      try {
        jwtProvider.validateToken(token);
        Long userId = jwtProvider.getUserIdFromToken(token);

        UserDetails userDetails =
            customUserDetailsService.loadUserByUsername(String.valueOf(userId));
        String role = ((CustomUserDetails) userDetails).getUserProfile().getRole().name();

        Authentication auth =
            new UsernamePasswordAuthenticationToken(
                userDetails, null, List.of(new SimpleGrantedAuthority("ROLE_" + role)));

        SecurityContextHolder.getContext().setAuthentication(auth);

      } catch (ExpiredJwtException e) {
        request.setAttribute("errorCode", ErrorCode.EXPIRED_TOKEN);
      } catch (JwtException | IllegalArgumentException e) {
        request.setAttribute("errorCode", ErrorCode.INVALID_TOKEN);
      }
    }

    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {

    String path = request.getRequestURI();

    return EXCLUDE_URIS.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
  }

  private String resolveToken(HttpServletRequest request) {

    String bearer = request.getHeader("Authorization");
    if (bearer == null || !bearer.startsWith("Bearer ")) {
      return null;
    }

    return bearer.substring(7);
  }
}
