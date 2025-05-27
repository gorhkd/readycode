package com.ll.readycode.domain.users.userauths.entity;

import com.ll.readycode.domain.users.userprofiles.entity.UserProfile;
import com.ll.readycode.global.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Entity
@Getter
public class UserAuth extends BaseEntity {

  private String email;

  private String provider;

  private String providerId;

  private String role;

  @ManyToOne
  private UserProfile userProfile;
}
