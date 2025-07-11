package com.ll.readycode.domain.users.userprofiles.service;

import static com.ll.readycode.global.exception.ErrorCode.USER_NOT_FOUND;

import com.ll.readycode.domain.users.userauths.entity.UserAuth;
import com.ll.readycode.domain.users.userauths.repository.UserAuthRepository;
import com.ll.readycode.domain.users.userprofiles.entity.UserProfile;
import com.ll.readycode.domain.users.userprofiles.entity.UserRole;
import com.ll.readycode.domain.users.userprofiles.repository.UserProfileRepository;
import com.ll.readycode.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserProfileService {

  private final UserProfileRepository userProfileRepository;
  private final UserAuthRepository userAuthRepository;

  @Transactional
  public Long createProfileWithAuth(String email, String provider, String providerId) {

    UserAuth savedAuth =
        userAuthRepository.save(
            UserAuth.builder().email(email).provider(provider).providerId(providerId).build());

    UserProfile userProfile = UserProfile.builder().role(UserRole.USER).build();
    userProfile.addUserAuth(savedAuth);

    userProfileRepository.save(userProfile);

    return userProfile.getId();
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
