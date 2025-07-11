package com.ll.readycode.domain.users.userprofiles.repository;

import com.ll.readycode.domain.users.userprofiles.entity.UserProfile;

import java.util.Optional;

public interface UserProfileRepositoryCustom {

  Optional<UserProfile> findProfileByProviderAndProviderId(String provider, String providerId);
}
