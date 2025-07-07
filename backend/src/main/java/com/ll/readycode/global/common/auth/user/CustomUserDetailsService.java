package com.ll.readycode.global.common.auth.user;

import static com.ll.readycode.global.exception.ErrorCode.USER_NOT_FOUND;

import com.ll.readycode.domain.users.userprofiles.entity.UserProfile;
import com.ll.readycode.domain.users.userprofiles.repository.UserProfileRepository;
import com.ll.readycode.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserProfileRepository userProfileRepository;

  @Override
  public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

    UserProfile userProfile =
        userProfileRepository
            .findById(Long.parseLong(userId))
            .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

    return new CustomUserDetails(userProfile);
  }
}
