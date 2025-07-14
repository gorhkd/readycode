package com.ll.readycode.domain.users.userprofiles.service;

import static com.ll.readycode.global.exception.ErrorCode.USER_NOT_FOUND;

import com.ll.readycode.api.userauths.dto.response.UserAuthResponseDto.Token;
import com.ll.readycode.api.userprofiles.dto.request.UserProfileRequestDto.Signup;
import com.ll.readycode.domain.users.userauths.entity.UserAuth;
import com.ll.readycode.domain.users.userprofiles.entity.UserProfile;
import com.ll.readycode.domain.users.userprofiles.entity.UserRole;
import com.ll.readycode.domain.users.userprofiles.repository.UserProfileRepository;
import com.ll.readycode.global.common.auth.jwt.JwtProvider;
import com.ll.readycode.global.common.auth.user.TempUserPrincipal;
import com.ll.readycode.global.exception.CustomException;
import com.ll.readycode.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserProfileService {

  private final UserProfileRepository userProfileRepository;
  private final JwtProvider jwtProvider;

  @Transactional
  public Token signup(TempUserPrincipal tempUserPrincipal, Signup signupRequest) {

    // 임시 토큰이 유효하지 않을 경우, 401에러
    if (tempUserPrincipal == null) {
      throw new CustomException(ErrorCode.INVALID_TOKEN);
    }

    UserAuth userAuth =
        UserAuth.builder()
            .email(tempUserPrincipal.email())
            .provider(tempUserPrincipal.provider())
            .providerId(tempUserPrincipal.providerId())
            .build();

    UserProfile userProfile =
        UserProfile.builder()
            .phoneNumber(signupRequest.phoneNumber())
            .nickname(signupRequest.nickname())
            .purpose(signupRequest.purpose())
            .role(UserRole.USER)
            .build();

    userProfile.addUserAuth(userAuth);

    UserProfile savedProfile = userProfileRepository.save(userProfile);

    return Token.builder()
        .accessToken(jwtProvider.createAccessToken(savedProfile.getId()))
        .refreshToken(jwtProvider.createRefreshToken())
        .isRegistered(true)
        .build();
  }

  @Transactional(readOnly = true)
  public Long getUserIdBySocialInfo(String provider, String providerId) {

    UserProfile userProfile =
        userProfileRepository
            .findProfileByProviderAndProviderId(provider, providerId)
            .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

    return userProfile.getId();
  }
}
