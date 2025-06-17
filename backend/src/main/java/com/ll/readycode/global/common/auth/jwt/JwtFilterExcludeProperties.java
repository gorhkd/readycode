package com.ll.readycode.global.common.auth.jwt;

import java.util.List;

public class JwtFilterExcludeProperties {

  /** JWT 토큰 체크 제외 목록 */
  public static final List<String> EXCLUDE_URIS =
      List.of(
              "/api/auth/**",
              "/swagger-ui/**",
              "/v3/api-docs/**"
      );
}
