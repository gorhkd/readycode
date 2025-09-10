package com.ll.readycode.domain.admin.service;

import com.ll.readycode.api.admin.dto.response.AdminResponseDto.SocialDetails;
import com.ll.readycode.api.admin.dto.response.AdminResponseDto.UserProfileDetails;
import com.ll.readycode.domain.users.userprofiles.entity.UserRole;
import com.ll.readycode.domain.users.userprofiles.repository.UserProfileRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

  private final UserProfileRepository userProfileRepository;

  @Transactional(readOnly = true)
  public List<UserProfileDetails> getUserProfilesWithSocialInfo() {
    return userProfileRepository.findAllByRole(UserRole.USER).stream()
        .map(
            userProfile ->
                UserProfileDetails.of(
                        userProfile,
                        userProfile.getUserAuths().stream()
                            .map(SocialDetails::of)
                            .toList()))
        .toList();
  }
}
