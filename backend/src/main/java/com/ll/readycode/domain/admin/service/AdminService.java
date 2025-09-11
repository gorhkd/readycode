package com.ll.readycode.domain.admin.service;

import com.ll.readycode.api.admin.dto.response.AdminResponseDto.SocialDetails;
import com.ll.readycode.api.admin.dto.response.AdminResponseDto.TemplateDownloadDetails;
import com.ll.readycode.api.admin.dto.response.AdminResponseDto.UserProfileDetails;
import com.ll.readycode.domain.templates.downloads.repository.TemplateDownloadRepository;
import com.ll.readycode.domain.users.userprofiles.entity.UserRole;
import com.ll.readycode.domain.users.userprofiles.repository.UserProfileRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

  private final UserProfileRepository userProfileRepository;
  private final TemplateDownloadRepository templateDownloadRepository;

  @Transactional(readOnly = true)
  public List<UserProfileDetails> getUserProfilesWithSocialInfo() {
    return userProfileRepository.findAllByRole(UserRole.USER).stream()
        .map(
            userProfile ->
                UserProfileDetails.of(
                    userProfile,
                    userProfile.getUserAuths().stream().map(SocialDetails::of).toList()))
        .toList();
  }

  @Transactional(readOnly = true)
  public List<TemplateDownloadDetails> getTemplateDownloadStatistics(
      LocalDateTime startDate, LocalDateTime endDate, Long templateId) {
    return templateDownloadRepository.findTemplatesForDownloadStatistics(
        startDate, endDate, templateId);
  }
}
