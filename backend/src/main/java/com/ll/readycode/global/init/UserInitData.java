package com.ll.readycode.global.init;

import com.ll.readycode.api.users.userprofiles.dto.request.UserProfileRequestDto.Signup;
import com.ll.readycode.domain.users.userprofiles.entity.UserPurpose;
import com.ll.readycode.domain.users.userprofiles.entity.UserRole;
import com.ll.readycode.domain.users.userprofiles.service.UserProfileService;
import com.ll.readycode.global.common.auth.user.TempUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;

@Profile({"frontend", "local", "prod"})
@Configuration
@RequiredArgsConstructor
public class UserInitData {

  private final UserProfileService userProfileService;

  @Autowired @Lazy private UserInitData self;

  @Bean
  @Order(1)
  public ApplicationRunner userInitDataApplicationRunner() {
    return args -> {
      self.createInitUser();
    };
  }

  @Transactional
  public void createInitUser() {

    if (userProfileService.countUserProfile() > 0) {
      return;
    }

    Signup adminUserProfile =
        Signup.builder()
            .phoneNumber("010-9999-9999")
            .nickname("관리자")
            .userRole(UserRole.ADMIN)
            .purpose(UserPurpose.LECTURE)
            .build();

    TempUserPrincipal adminUserAuth =
        TempUserPrincipal.builder()
            .provider("kakao")
            .providerId("9999999")
            .email("admin@kakao.com")
            .build();

    Signup userProfile1 =
        Signup.builder()
            .phoneNumber("010-0000-0001")
            .nickname("일반유저1")
            .userRole(UserRole.USER)
            .purpose(UserPurpose.LEARNING)
            .build();

    TempUserPrincipal userAuth1 =
        TempUserPrincipal.builder()
            .provider("kakao")
            .providerId("1111111")
            .email("user1@kakao.com")
            .build();

    Signup userProfile2 =
        Signup.builder()
            .phoneNumber("010-0000-0002")
            .nickname("일반유저2")
            .userRole(UserRole.USER)
            .purpose(UserPurpose.LEARNING)
            .build();

    TempUserPrincipal userAuth2 =
        TempUserPrincipal.builder()
            .provider("naver")
            .providerId("2222222")
            .email("user2@naver.com")
            .build();

    userProfileService.signup(adminUserAuth, adminUserProfile);
    userProfileService.signup(userAuth1, userProfile1);
    userProfileService.signup(userAuth2, userProfile2);
  }
}
