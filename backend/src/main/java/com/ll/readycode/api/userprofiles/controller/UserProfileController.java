package com.ll.readycode.api.userprofiles.controller;

import com.ll.readycode.api.dto.userauths.UserAuthResponseDto.Token;
import com.ll.readycode.api.userprofiles.dto.request.UserProfileRequestDto.Signup;
import com.ll.readycode.domain.users.userprofiles.service.UserProfileService;
import com.ll.readycode.global.common.auth.user.TempUserPrincipal;
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
}
