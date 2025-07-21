package com.ll.readycode.domain.users.userprofiles;

import static com.ll.readycode.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ll.readycode.api.users.userauths.dto.response.UserAuthResponseDto.Token;
import com.ll.readycode.api.users.userprofiles.dto.request.UserProfileRequestDto.Signup;
import com.ll.readycode.api.users.userprofiles.dto.request.UserProfileRequestDto.UpdateProfile;
import com.ll.readycode.api.users.userprofiles.dto.response.UserProfileResponseDto.ProfileWithSocial;
import com.ll.readycode.api.users.userprofiles.dto.response.UserProfileResponseDto.ProfileWithSocial.Social;
import com.ll.readycode.domain.users.userauths.entity.UserAuth;
import com.ll.readycode.domain.users.userprofiles.entity.UserProfile;
import com.ll.readycode.domain.users.userprofiles.entity.UserPurpose;
import com.ll.readycode.domain.users.userprofiles.entity.UserRole;
import com.ll.readycode.domain.users.userprofiles.repository.UserProfileRepository;
import com.ll.readycode.domain.users.userprofiles.service.UserProfileService;
import com.ll.readycode.global.common.auth.jwt.JwtProvider;
import com.ll.readycode.global.common.auth.user.TempUserPrincipal;
import com.ll.readycode.global.common.auth.user.UserPrincipal;
import com.ll.readycode.global.exception.CustomException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

public class UserProfileServiceTest {

  private UserProfileService userProfileService;

  @Mock private UserProfileRepository userProfileRepository;

  @Mock private JwtProvider jwtProvider;

  @BeforeEach
  void setUp() {
    userProfileRepository = mock(UserProfileRepository.class);
    jwtProvider = mock(JwtProvider.class);
    userProfileService = new UserProfileService(userProfileRepository, jwtProvider);
  }

  @DisplayName("회원가입 성공 테스트")
  @Test
  void signupSuccessTest() {

    // given
    String provider = "mock-provider";
    String providerId = "mock-provider-id";
    String email = "mock-email";
    String nickname = "mock-nickname";
    String phoneNumber = "mock-phone-number";
    UserPurpose purpose = UserPurpose.LEARNING;
    String newAccessToken = "mock-new-access-token";
    String newRefreshToken = "mock-new-refresh-token";
    UserProfile userProfile =
        UserProfile.builder()
            .nickname(nickname)
            .phoneNumber(phoneNumber)
            .purpose(purpose)
            .role(UserRole.USER)
            .build();

    TempUserPrincipal tempUserPrincipal =
        TempUserPrincipal.builder().provider(provider).providerId(providerId).email(email).build();

    Signup signupRequest = new Signup(nickname, phoneNumber, purpose);
    ReflectionTestUtils.setField(userProfile, "id", 1L);

    when(userProfileRepository.save(any(UserProfile.class))).thenReturn(userProfile);
    when(jwtProvider.createAccessToken(1L)).thenReturn(newAccessToken);
    when(jwtProvider.createRefreshToken()).thenReturn(newRefreshToken);

    // when
    Token signupResult = userProfileService.signup(tempUserPrincipal, signupRequest);

    // then
    assertThat(newAccessToken).isEqualTo(signupResult.accessToken());
    assertThat(newRefreshToken).isEqualTo(signupResult.refreshToken());
  }

  @DisplayName("회원가입 실패 테스트: 임시 토큰이 유효하지 않을 경우")
  @Test
  void signupInvalidTokenTest() {

    // given & when
    CustomException exception =
        assertThrows(CustomException.class, () -> userProfileService.signup(null, null));

    // then
    assertThat(INVALID_TOKEN).isEqualTo(exception.getErrorCode());
  }

  @DisplayName("회원 정보 조회 성공 테스트")
  @Test
  void getUserBySocialInfoTest() {

    // given
    String provider = "mock-provider";
    String providerId = "mock-provider-id";
    Long userId = 1L;

    UserProfile userProfile = UserProfile.builder().build();
    ReflectionTestUtils.setField(userProfile, "id", 1L);

    when(userProfileRepository.findProfileByProviderAndProviderId(provider, providerId))
        .thenReturn(Optional.of(userProfile));

    // when
    Long userIdResult = userProfileService.getUserIdBySocialInfo(provider, providerId);

    // then
    assertThat(userId).isEqualTo(userIdResult);
  }

  @DisplayName("회원 정보 조회 실패 테스트: 회원이 존재하지 않을 경우")
  @Test
  void getUserBySocialInfoUserNotFoundTest() {

    // given & when
    CustomException exception =
        assertThrows(
            CustomException.class, () -> userProfileService.getUserIdBySocialInfo(null, null));

    // then
    assertThat(USER_NOT_FOUND).isEqualTo(exception.getErrorCode());
  }

  @DisplayName("회원 정보 수정 성공 테스트")
  @Test
  void updateSuccessTest() {

    // given
    UserProfile userProfile = UserProfile.builder().build();

    UserPrincipal userPrincipal = mock(UserPrincipal.class);
    when(userPrincipal.getUserProfile()).thenReturn(userProfile);

    String newNickname = "mock-new-nickname";
    UpdateProfile updateProfile = new UpdateProfile(newNickname);

    // when
    userProfileService.update(userPrincipal, updateProfile);

    // then
    assertThat(userProfile.getNickname()).isEqualTo(newNickname);
  }

  @DisplayName("회원 정보 수정 실패 테스트: 토큰이 존재하지 않을 경우")
  @Test
  void updateInvalidTokenTest() {

    // given & when
    CustomException exception =
        assertThrows(CustomException.class, () -> userProfileService.update(null, null));

    // then
    assertThat(INVALID_TOKEN).isEqualTo(exception.getErrorCode());
  }

  @DisplayName("회원 프로필 조회 성공 테스트")
  @Test
  void getProfileWithSocialInfoSuccessTest() {

    // given
    String phoneNumber = "mock-phone-number";
    String nickname = "mock-nickname";
    String provider = "mock-provider";
    String email = "mock-email";

    UserAuth userAuth = UserAuth.builder().provider(provider).email(email).build();

    UserProfile userProfile =
        UserProfile.builder().nickname(nickname).phoneNumber(phoneNumber).build();
    userProfile.addUserAuth(userAuth);

    UserPrincipal userPrincipal = new UserPrincipal(userProfile);

    // when
    ProfileWithSocial profileWithSocialInfo =
        userProfileService.getProfileWithSocialInfo(userPrincipal);

    // then
    assertThat(nickname).isEqualTo(profileWithSocialInfo.getNickname());
    assertThat(phoneNumber).isEqualTo(profileWithSocialInfo.getPhoneNumber());

    List<Social> socials = profileWithSocialInfo.getSocials();
    Social social = socials.get(0);

    assertThat(provider).isEqualTo(social.provider());
    assertThat(email).isEqualTo(social.email());
  }

  @DisplayName("회원 프로필 조회 실패 테스트: 토큰이 존재하지 않을 경우")
  @Test
  void getProfileWithSocialInfoInvalidTokenTest() {

    // given & when
    CustomException exception =
        assertThrows(
            CustomException.class, () -> userProfileService.getProfileWithSocialInfo(null));

    // then
    assertThat(INVALID_TOKEN).isEqualTo(exception.getErrorCode());
  }

  @DisplayName("회원 삭제 성공 테스트")
  @Test
  void deleteSuccessTest() {

    // given
    UserProfile userProfile = UserProfile.builder().build();
    UserPrincipal userPrincipal = new UserPrincipal(userProfile);

    // when
    userProfileService.delete(userPrincipal);

    // then
    assertThat(userProfile.isDeleted()).isTrue();
  }

  @DisplayName("회원 삭제 실패 테스트: 토큰이 존재하지 않을 경우")
  @Test
  void deleteInvalidTokenTest() {

    // given & when
    CustomException exception =
        assertThrows(CustomException.class, () -> userProfileService.delete(null));

    // then
    assertThat(INVALID_TOKEN).isEqualTo(exception.getErrorCode());
  }

  @DisplayName("회원 삭제 취소 성공 테스트")
  @Test
  void restoreSuccessTest() {

    // given
    UserProfile userProfile = UserProfile.builder().build();
    UserPrincipal userPrincipal = new UserPrincipal(userProfile);

    userProfile.updateDeletedStatus(true);

    // when
    userProfileService.restore(userPrincipal);

    // then
    assertThat(userProfile.isDeleted()).isFalse();
  }

  @DisplayName("회원 삭제 취소 실패 테스트: 토큰이 존재하지 않을 경우")
  @Test
  void restoreInvalidTokenTest() {

    // given & when
    CustomException exception =
        assertThrows(CustomException.class, () -> userProfileService.restore(null));

    // then
    assertThat(INVALID_TOKEN).isEqualTo(exception.getErrorCode());
  }

  @DisplayName("회원 삭제 취소 실패 테스트: 회원 상태가 이미 활성화 상태일 경우")
  @Test
  void restoreAlreadyActiveUser() {

    // given
    UserProfile userProfile = UserProfile.builder().build();
    UserPrincipal userPrincipal = new UserPrincipal(userProfile);

    // when
    CustomException exception = assertThrows(CustomException.class, () -> userProfileService.restore(userPrincipal));

    // then
    assertThat(ALREADY_ACTIVE_USER).isEqualTo(exception.getErrorCode());
  }
}
