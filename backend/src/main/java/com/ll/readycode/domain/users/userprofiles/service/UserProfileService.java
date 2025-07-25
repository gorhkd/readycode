package com.ll.readycode.domain.users.userprofiles.service;

import static com.ll.readycode.global.exception.ErrorCode.*;

import com.ll.readycode.api.users.userauths.dto.response.UserAuthResponseDto.Token;
import com.ll.readycode.api.users.userprofiles.dto.request.UserProfileRequestDto.Signup;
import com.ll.readycode.api.users.userprofiles.dto.request.UserProfileRequestDto.UpdateProfile;
import com.ll.readycode.api.users.userprofiles.dto.response.UserProfileResponseDto.ProfileWithSocial;
import com.ll.readycode.api.users.userprofiles.dto.response.UserProfileResponseDto.ProfileWithSocial.Social;
import com.ll.readycode.domain.users.userauths.entity.UserAuth;
import com.ll.readycode.domain.users.userprofiles.entity.UserProfile;
import com.ll.readycode.domain.users.userprofiles.entity.UserRole;
import com.ll.readycode.domain.users.userprofiles.repository.UserProfileRepository;
import com.ll.readycode.global.common.auth.jwt.JwtProvider;
import com.ll.readycode.global.common.auth.user.TempUserPrincipal;
import com.ll.readycode.global.common.auth.user.UserPrincipal;
import com.ll.readycode.global.exception.CustomException;
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
      throw new CustomException(INVALID_TOKEN);
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

  @Transactional
  public void update(UserPrincipal userPrincipal, UpdateProfile updateProfile) {

    // 유효하지 않는 토큰일 경우, 401 에러 반환
    if (userPrincipal == null) {
      throw new CustomException(INVALID_TOKEN);
    }

    userPrincipal.getUserProfile().updateNickname(updateProfile.nickname());
  }

  @Transactional(readOnly = true)
  public ProfileWithSocial getProfileWithSocialInfo(UserPrincipal userPrincipal) {

    // 유효하지 않는 토큰일 경우, 401 에러 반환
    if (userPrincipal == null) {
      throw new CustomException(INVALID_TOKEN);
    }

    UserProfile userProfile = userPrincipal.getUserProfile();
    ProfileWithSocial profileWithSocial =
        ProfileWithSocial.builder()
            .nickname(userProfile.getNickname())
            .phoneNumber(userProfile.getPhoneNumber())
            .build();

    for (UserAuth userAuth : userProfile.getUserAuths()) {

      Social social =
          Social.builder().provider(userAuth.getProvider()).email(userAuth.getEmail()).build();

      profileWithSocial.addSocial(social);
    }

    return profileWithSocial;
  }

  @Transactional
  public void delete(UserPrincipal userPrincipal) {

    // 유효하지 않는 토큰일 경우, 401 에러 반환
    if (userPrincipal == null) {
      throw new CustomException(INVALID_TOKEN);
    }

    UserProfile userProfile = userPrincipal.getUserProfile();
    userProfile.updateDeletedStatus(true);
  }

  @Transactional
  public void restore(UserPrincipal userPrincipal) {

    // 유효하지 않는 토큰일 경우, 401 에러 반환
    if (userPrincipal == null) {
      throw new CustomException(INVALID_TOKEN);
    }

    UserProfile userProfile = userPrincipal.getUserProfile();

    // 해당 회원이 이미 활성화 되어있을 경우, 400 에러 반환
    if (!userProfile.isDeleted()) {
      throw new CustomException(ALREADY_ACTIVE_USER);
    }

    userProfile.updateDeletedStatus(false);
  }
}
