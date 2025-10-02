package com.ll.readycode.domain.users.userprofiles.repository;

import com.ll.readycode.domain.users.userprofiles.entity.UserProfile;
import com.ll.readycode.domain.users.userprofiles.entity.UserRole;
import com.ll.readycode.global.common.types.OrderType;
import java.util.List;
import java.util.Optional;

public interface UserProfileRepositoryCustom {

  Optional<UserProfile> findProfileByProviderAndProviderId(String provider, String providerId);

  List<UserProfile> findAllByRoleWithCursor(
      UserRole role, int limit, Long cursor, String keyword, OrderType orderType);
}
