package com.ll.readycode.api.admin.controller;

import com.ll.readycode.api.admin.dto.response.AdminResponseDto.TemplateDownloadDetails;
import com.ll.readycode.api.admin.dto.response.AdminResponseDto.UserProfileDetails;
import com.ll.readycode.domain.admin.service.AdminService;
import com.ll.readycode.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "관리자용 API", description = "관리자용 API 입니다.")
public class AdminController {

  private final AdminService adminService;

  @GetMapping("/users")
  @Operation(summary = "유저 목록 조회", description = "가입된 회원 프로필과 연동된 SNS 정보들을 조회합니다.")
  public ResponseEntity<SuccessResponse<List<UserProfileDetails>>> getProfileWithSocialInfo() {

    List<UserProfileDetails> userInfos = adminService.getUserProfilesWithSocialInfo();

    return ResponseEntity.ok(SuccessResponse.of(userInfos));
  }

  @GetMapping("/downloads")
  @Operation(summary = "템플릿 다운로드 통계 조회", description = "전체 템플릿의 다운로드 수 통계 정보를 조회합니다.")
  public ResponseEntity<SuccessResponse<List<TemplateDownloadDetails>>> getTemplateDownloadStatistics(
      @RequestParam(required = false) LocalDateTime startDate,
      @RequestParam(required = false) LocalDateTime endDate,
      @RequestParam(required = false) Long templateId) {

    List<TemplateDownloadDetails> downloadInfo = adminService.getTemplateDownloadStatistics(
        startDate, endDate, templateId);

    return ResponseEntity.ok(SuccessResponse.of(
        "템플릿 다운로드 통계를 성공적으로 조회했습니다.", downloadInfo));
  }
}
