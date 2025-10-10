package com.ll.readycode.global.config;

import com.ll.readycode.domain.users.userprofiles.entity.UserRole;
import com.ll.readycode.global.common.auth.handler.CustomAccessDeniedHandler;
import com.ll.readycode.global.common.auth.handler.CustomAuthenticationEntryPoint;
import com.ll.readycode.global.common.auth.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
  private final CustomAccessDeniedHandler customAccessDeniedHandler;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.cors(Customizer.withDefaults())
        .csrf(AbstractHttpConfigurer::disable)
        .headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
        .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            auth ->
                auth
                    // 1) 공개 엔드포인트 (헬스/핑)
                    .requestMatchers("/actuator/health", "/actuator/health/**", "/ping")
                    .permitAll()

                    // 2) Swagger (운영에서는 닫을 수 있음)
                    .requestMatchers(
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/swagger-ui*/**",
                        "/webjars/**",
                        "/swagger/**")
                    .permitAll()

                    // 3) H2 콘솔 (운영 비권장)
                    .requestMatchers("/h2-console/**")
                    .permitAll()

                    // 4) 인증(로그인/재발급)
                    .requestMatchers(HttpMethod.POST, "/api/auth/login", "/api/auth/reissue")
                    .permitAll()

                    // 5) 공개 조회 API
                    .requestMatchers(
                        HttpMethod.GET,
                        "/api/templates",
                        "/api/templates/*",
                        "/api/categories",
                        "/api/reviews/*")
                    .permitAll()

                    // 6) 관리자 권한이 필요한 API
                    .requestMatchers("/api/admin/**")
                    .hasRole(UserRole.ADMIN.name())
                    .requestMatchers(HttpMethod.POST, "/api/templates")
                    .hasRole(UserRole.ADMIN.name())
                    .requestMatchers(HttpMethod.PATCH, "/api/templates/*")
                    .hasRole(UserRole.ADMIN.name())
                    .requestMatchers(HttpMethod.DELETE, "/api/templates/*")
                    .hasRole(UserRole.ADMIN.name())
                    .requestMatchers(HttpMethod.POST, "/api/categories")
                    .hasRole(UserRole.ADMIN.name())
                    .requestMatchers(HttpMethod.PATCH, "/api/categories/*")
                    .hasRole(UserRole.ADMIN.name())
                    .requestMatchers(HttpMethod.DELETE, "/api/categories/*")
                    .hasRole(UserRole.ADMIN.name())

                    // 7) 그 외 전부 인증 필요 — 항상 맨 마지막!
                    .anyRequest()
                    .authenticated())
        .exceptionHandling(
            ex ->
                ex.authenticationEntryPoint(customAuthenticationEntryPoint)
                    .accessDeniedHandler(customAccessDeniedHandler))
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
