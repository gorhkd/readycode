package com.ll.readycode.domain.users.userauths.repository;

import com.ll.readycode.domain.users.userauths.entity.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAuthRepository extends JpaRepository<UserAuth, Long> {

  Optional<UserAuth> findByEmail(String email);

}
