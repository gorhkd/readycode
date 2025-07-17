package com.ll.readycode.domain.users.userprofiles.repository;

import com.ll.readycode.domain.users.userprofiles.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long>, UserProfileRepositoryCustom {}
