package com.ll.readycode.api.users.userprofiles.controller;

import com.ll.readycode.api.users.userauths.dto.response.UserAuthResponseDto.Token;
import com.ll.readycode.api.users.userprofiles.dto.request.UserProfileRequestDto.Signup;
import com.ll.readycode.api.users.userprofiles.dto.request.UserProfileRequestDto.UpdateProfile;
import com.ll.readycode.api.users.userprofiles.dto.response.UserProfileResponseDto.ProfileWithSocial;
import com.ll.readycode.domain.users.userprofiles.service.UserProfileService;
import com.ll.readycode.global.common.auth.user.TempUserPrincipal;
import com.ll.readycode.global.common.auth.user.UserPrincipal;
import com.ll.readycode.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "유저 프로필 API", description = "유저 프로필 관련 API 입니다.")
public class UserProfileController {

  private final UserProfileService userProfileService;

  @PostMapping("/signup")
  @Operation(summary = "회원가입", description = "임시 토큰을 가진 사용자를 대상으로 회원가입 후 인증에 사용될 토큰을 반환합니다.")
  public ResponseEntity<SuccessResponse<Token>> signup(
      @AuthenticationPrincipal TempUserPrincipal tempUserPrincipal,
      @RequestBody @Valid Signup signupRequest) {

    Token tokenInfo = userProfileService.signup(tempUserPrincipal, signupRequest);

    return ResponseEntity.ok(SuccessResponse.of(tokenInfo));
  }

  @GetMapping("/me")
  @Operation(summary = "회원 정보 조회", description = "회원 프로필과 연동된 SNS 정보를 조회합니다.")
  public ResponseEntity<SuccessResponse<ProfileWithSocial>> getProfileWithSocialInfo(
      @AuthenticationPrincipal UserPrincipal userPrincipal) {

    ProfileWithSocial profileInfo = userProfileService.getProfileWithSocialInfo(userPrincipal);

    return ResponseEntity.ok(SuccessResponse.of(profileInfo));
  }

  @PatchMapping("/me")
  @Operation(summary = "회원 프로필 정보 수정", description = "회원 프로필을 수정합니다.")
  public ResponseEntity<SuccessResponse<Void>> updateProfile(
      @AuthenticationPrincipal UserPrincipal userPrincipal,
      @RequestBody UpdateProfile updateProfile) {

    userProfileService.update(userPrincipal, updateProfile);

    return ResponseEntity.ok(SuccessResponse.of("사용자 정보가 성공적으로 수정되었습니다.", null));
  }

  @DeleteMapping("/me")
  @Operation(summary = "회원 탈퇴", description = "회원 탈퇴 요청 시, 회원 탈퇴 상태를 변경합니다.")
  public ResponseEntity<SuccessResponse<Void>> deleteProfile(
      @AuthenticationPrincipal UserPrincipal userPrincipal) {

    userProfileService.delete(userPrincipal);

    return ResponseEntity.ok(SuccessResponse.of("회원 탈퇴가 성공적으로 처리되었습니다.", null));
  }

  @PostMapping("/me/restore")
  @Operation(summary = "회원 탈퇴 취소", description = "회원 탈퇴 취소 요청 시, 회원 탈퇴 상태를 변경합니다.")
  public ResponseEntity<SuccessResponse<Void>> restoreProfile(
      @AuthenticationPrincipal UserPrincipal userPrincipal) {

    userProfileService.restore(userPrincipal);

    return ResponseEntity.ok(SuccessResponse.of("회원 탈퇴 요청을 성공적으로 취소했습니다.", null));
  }
}
