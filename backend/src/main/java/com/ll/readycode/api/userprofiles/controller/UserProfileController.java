package com.ll.readycode.api.userprofiles.controller;

import com.ll.readycode.api.userauths.dto.response.UserAuthResponseDto.Token;
import com.ll.readycode.api.userprofiles.dto.request.UserProfileRequestDto.Signup;
import com.ll.readycode.api.userprofiles.dto.request.UserProfileRequestDto.UpdateProfile;
import com.ll.readycode.api.userprofiles.dto.response.UserProfileResponseDto.ProfileWithSocial;
import com.ll.readycode.domain.users.userprofiles.service.UserProfileService;
import com.ll.readycode.global.common.auth.user.TempUserPrincipal;
import com.ll.readycode.global.common.auth.user.UserPrincipal;
import com.ll.readycode.global.common.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserProfileController {

  private final UserProfileService userProfileService;

  @PostMapping("/signup")
  public ResponseEntity<SuccessResponse<Token>> signup(
      @AuthenticationPrincipal TempUserPrincipal tempUserPrincipal,
      @RequestBody @Valid Signup signupRequest) {

    Token tokenInfo = userProfileService.signup(tempUserPrincipal, signupRequest);

    return ResponseEntity.ok(SuccessResponse.of(tokenInfo));
  }

  @GetMapping("/me")
  public ResponseEntity<SuccessResponse<ProfileWithSocial>> getProfileWithSocialInfo(
      @AuthenticationPrincipal UserPrincipal userPrincipal) {

    ProfileWithSocial profileInfo = userProfileService.getProfileWithSocialInfo(userPrincipal);

    return ResponseEntity.ok(SuccessResponse.of(profileInfo));
  }

  @PatchMapping("/me")
  public ResponseEntity<SuccessResponse<Void>> updateProfile(
      @AuthenticationPrincipal UserPrincipal userPrincipal,
      @RequestBody UpdateProfile updateProfile) {

    userProfileService.update(userPrincipal, updateProfile);

    return ResponseEntity.ok(SuccessResponse.of("사용자 정보가 성공적으로 수정되었습니다.", null));
  }

  @DeleteMapping("/me")
  public ResponseEntity<SuccessResponse<Void>> deleteProfile(
      @AuthenticationPrincipal UserPrincipal userPrincipal) {

    userProfileService.delete(userPrincipal);

    return ResponseEntity.ok(SuccessResponse.of("회원 탈퇴가 성공적으로 처리되었습니다.", null));
  }
}
