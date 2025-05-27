package com.ll.readycode.global.common.auth.oauth.factory;

import static com.ll.readycode.global.exception.ErrorCode.INVALID_SOCIAL_PROVIDER;

import com.ll.readycode.global.common.auth.oauth.service.OAuthService;
import com.ll.readycode.global.exception.CustomException;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuthServiceFactory {

  private final Map<String, OAuthService> serviceMap;

  public OAuthService getService(String provider) {
    // 지원하지 않는 SNS를 요청할 경우, 400에러 반환
    return Optional.ofNullable(serviceMap.get(provider.toLowerCase()))
        .orElseThrow(() -> new CustomException(INVALID_SOCIAL_PROVIDER));
  }
}
