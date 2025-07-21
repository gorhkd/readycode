package com.ll.readycode.global.common.auth.token;

import static com.ll.readycode.global.exception.ErrorCode.*;

import com.ll.readycode.api.users.userauths.dto.response.UserAuthResponseDto.Token;
import com.ll.readycode.global.common.auth.jwt.JwtProvider;
import com.ll.readycode.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

  private final JwtProvider jwtProvider;
  private final RefreshTokenStore refreshTokenStore;

  public Token reissue(String refreshToken) {

    // Redis에서 userId에 해당하는 Refresh 토큰이 존재하지 않을 경우, 401에러 반환
    String userIdStr =
        refreshTokenStore.get(refreshToken).orElseThrow(() -> new CustomException(EXPIRED_TOKEN));

    return Token.builder()
        .accessToken(jwtProvider.createAccessToken(Long.parseLong(userIdStr)))
        .refreshToken(refreshToken)
        .build();
  }

  public void delete(String refreshToken) {

    // Refresh 토큰이 누락되었을 경우, 400에러 반환
    if (refreshToken == null) {
      throw new CustomException(MISSING_REFRESH_TOKEN);
    }

    // Redis에 Refresh 토큰에 해당하는 userId가 존재하지 않을 경우, 403에러 반환
    refreshTokenStore.get(refreshToken).orElseThrow(() -> new CustomException(LOGOUT_ALREADY));

    // Redis에서 Refresh 토큰(uuid)에 해당하는 Refresh 토큰 삭제
    refreshTokenStore.delete(refreshToken);
  }
}
