package com.ll.readycode.global.config;

import com.ll.readycode.global.common.auth.handler.CustomAccessDeniedHandler;
import com.ll.readycode.global.common.auth.handler.CustomAuthenticationEntryPoint;
import com.ll.readycode.global.common.auth.jwt.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
  private final CustomAccessDeniedHandler customAccessDeniedHandler;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                    /* 인증 없어도 되는 API */
                    // 인증 API
                    .requestMatchers("/api/auth/**").permitAll()
                    // Swagger
                    .requestMatchers("v3/api-docs/**", "/swagger-resources/**", "/swagger-ui*/**",
                            "/webjars/**", "/swagger/**").permitAll()

                    .anyRequest().authenticated()
            )
            .exceptionHandling(exception -> exception
                    /* 예외 처리 Handler */
                    // 인증 실패 했을 경우
                    .authenticationEntryPoint(customAuthenticationEntryPoint)
                    // 접근 권한이 없는 경우
                    .accessDeniedHandler(customAccessDeniedHandler)
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
