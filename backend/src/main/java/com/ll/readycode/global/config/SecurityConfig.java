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
        .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            auth ->
                auth
                    /* 인증 없어도 되는 API */
                    // Swagger
                    .requestMatchers(
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/swagger-ui*/**",
                        "/webjars/**",
                        "/swagger/**")
                    .permitAll()
                    // H2
                    .requestMatchers("/h2-console/**").permitAll()
                    // 인증 API
                    .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/auth/reissue").permitAll()
                    // 템플릿 API
                    .requestMatchers(HttpMethod.GET, "/api/templates").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/templates/*").permitAll()
                    // 카테고리 API
                    .requestMatchers(HttpMethod.GET, "/api/categories").permitAll()
                    // 리뷰 API
                    .requestMatchers(HttpMethod.GET, "/api/reviews/*").permitAll()

                    /* 인증 필요한 API */
                    // 관리자 권한 API
                    .requestMatchers("/api/admin/**").hasRole(UserRole.ADMIN.name())
                    // 템플릿 API
                    .requestMatchers(HttpMethod.POST, "/api/templates").hasRole(UserRole.ADMIN.name())
                    .requestMatchers(HttpMethod.PATCH, "/api/templates/*").hasRole(UserRole.ADMIN.name())
                    .requestMatchers(HttpMethod.DELETE, "/api/templates/*").hasRole(UserRole.ADMIN.name())
                    // 카테고리 API
                    .requestMatchers(HttpMethod.POST, "/api/categories").hasRole(UserRole.ADMIN.name())
                    .requestMatchers(HttpMethod.PATCH, "/api/categories/*").hasRole(UserRole.ADMIN.name())
                    .requestMatchers(HttpMethod.DELETE, "/api/categories/*").hasRole(UserRole.ADMIN.name())

                    // 그 외 API
                    .anyRequest().authenticated())

        .exceptionHandling(
            exception ->
                exception
                    /* 예외 처리 Handler */
                    // 인증 실패 했을 경우
                    .authenticationEntryPoint(customAuthenticationEntryPoint)
                    // 접근 권한이 없는 경우
                    .accessDeniedHandler(customAccessDeniedHandler))
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
