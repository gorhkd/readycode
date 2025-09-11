package com.ll.readycode.api.admin.dto.response;

import com.ll.readycode.domain.users.userauths.entity.UserAuth;
import com.ll.readycode.domain.users.userprofiles.entity.UserProfile;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Schema(description = "관리자용 응답 DTO")
public class AdminResponseDto {

  @Schema(description = "유저 목록 조회 응답 DTO")
  @Builder
  public record UserProfileDetails(
      @Schema(description = "유저 ID", example = "1") Long userId,
      @Schema(description = "닉네임", example = "홍길동") String nickname,
      @Schema(description = "전화번호", example = "010-1234-5678") String phoneNumber,
      @Schema(description = "보유 포인트", example = "1200") Long point,
      @Schema(description = "가입일시", example = "2025-01-01T12:34:56") LocalDateTime createdAt,
      @Schema(description = "연동된 SNS 목록") List<SocialDetails> socials) {

    public static UserProfileDetails of(UserProfile userProfile, List<SocialDetails> socials) {
      return UserProfileDetails.builder()
          .userId(userProfile.getId())
          .nickname(userProfile.getNickname())
          .phoneNumber(userProfile.getPhoneNumber())
          .point(0L) // TODO: 결제 시스템 도입 시, 변경 필요
          .createdAt(userProfile.getCreatedAt())
          .socials(socials)
          .build();
    }
  }

  @Schema(description = "유저 목록 조회 응답 DTO 내 SNS 정보")
  @Builder
  public record SocialDetails(
      @Schema(description = "SNS 계정 이메일", example = "user@example.com") String email,
      @Schema(description = "SNS 제공자", example = "kakao") String provider) {

    public static SocialDetails of(UserAuth userAuth) {
      return SocialDetails.builder()
          .email(userAuth.getEmail())
          .provider(userAuth.getProvider())
          .build();
    }
  }

  @Schema(description = "템플릿 다운로드 통계 조회 응답 DTO")
  @Builder
  public record TemplateDownloadDetails(
      @Schema(description = "템플릿 ID", example = "1") Long templateId,
      @Schema(description = "템플릿 제목", example = "스프링 시큐리티 JWT 템플릿") String templateTitle,
      @Schema(description = "다운로드 수", example = "123") Long downloadCount) {}
}
