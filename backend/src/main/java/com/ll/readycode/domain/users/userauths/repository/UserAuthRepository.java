package com.ll.readycode.domain.users.userauths.repository;

import com.ll.readycode.domain.users.userauths.entity.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAuthRepository extends JpaRepository<UserAuth, Long> {}
