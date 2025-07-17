package com.ll.readycode.global.common.auth.jwt;

import static com.ll.readycode.global.common.auth.jwt.JwtFilterExcludeProperties.EXCLUDE_URIS;

import com.ll.readycode.global.common.auth.user.CustomUserDetailsService;
import com.ll.readycode.global.common.auth.user.TempUserPrincipal;
import com.ll.readycode.global.common.auth.user.UserPrincipal;
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

    String token = getTokenFromHeader(request);

    if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      try {
        jwtProvider.validateToken(token);

        Object claimsObject = jwtProvider.getClaimsFromToken(token);
        Authentication auth = null;

        if (claimsObject instanceof Long userId) {
          // 정식 발급 토큰일 경우
          UserDetails userDetails =
              customUserDetailsService.loadUserByUsername(String.valueOf(userId));
          String role = ((UserPrincipal) userDetails).getUserProfile().getRole().name();

          auth =
              new UsernamePasswordAuthenticationToken(
                  userDetails, null, List.of(new SimpleGrantedAuthority("ROLE_" + role)));

        } else if (claimsObject instanceof TempUserPrincipal tempUserPrincipal) {
          // 임시 발급 토큰일 경우
          auth = new UsernamePasswordAuthenticationToken(tempUserPrincipal, null, List.of());
        }

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

  private String getTokenFromHeader(HttpServletRequest request) {

    String bearer = request.getHeader("Authorization");
    if (bearer == null || !bearer.startsWith("Bearer ")) {
      return null;
    }

    return bearer.substring(7);
  }
}
