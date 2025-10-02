package com.ll.readycode.domain.admin.service;

import com.ll.readycode.api.admin.dto.response.AdminResponseDto.SocialDetails;
import com.ll.readycode.api.admin.dto.response.AdminResponseDto.TemplateDownloadDetails;
import com.ll.readycode.api.admin.dto.response.AdminResponseDto.UserProfileDetails;
import com.ll.readycode.domain.templates.downloads.repository.TemplateDownloadRepository;
import com.ll.readycode.domain.users.userprofiles.entity.UserProfile;
import com.ll.readycode.domain.users.userprofiles.entity.UserRole;
import com.ll.readycode.domain.users.userprofiles.repository.UserProfileRepository;
import com.ll.readycode.global.common.pagination.CursorPage;
import com.ll.readycode.global.common.pagination.PaginationPolicy;
import com.ll.readycode.global.common.types.OrderType;
import com.ll.readycode.global.common.util.Encoder;
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
  public CursorPage<UserProfileDetails> getUserProfilesWithSocialInfo(
      Integer limit, Long cursor, String keyword, String orderTypeStr) {

    int pageSize = PaginationPolicy.clamp(limit);
    OrderType orderType = OrderType.from(orderTypeStr);

    // 다음 페이지 존재 여부 확인을 위해 +1개 조회
    List<UserProfile> userProfiles =
        userProfileRepository.findAllByRoleWithCursor(
            UserRole.USER, pageSize + 1, cursor, keyword, orderType);

    // 다음 페이지 존재 여부 확인
    boolean hasNext = userProfiles.size() > pageSize;
    if (hasNext) userProfiles = userProfiles.subList(0, pageSize);

    // 다음 커서 생성
    String nextCursor =
        hasNext ? Encoder.encode(userProfiles.get(userProfiles.size() - 1).getId()) : null;

    // UserProfile -> UserProfileDetails 변환
    List<UserProfileDetails> items =
        userProfiles.stream()
            .map(
                userProfile ->
                    UserProfileDetails.of(
                        userProfile,
                        userProfile.getUserAuths().stream().map(SocialDetails::of).toList()))
            .toList();

    return new CursorPage<>(items, nextCursor, hasNext);
  }

  @Transactional(readOnly = true)
  public List<TemplateDownloadDetails> getTemplateDownloadStatistics(
      LocalDateTime startDate, LocalDateTime endDate, Long templateId) {
    return templateDownloadRepository.findTemplatesForDownloadStatistics(
        startDate, endDate, templateId);
  }
}
