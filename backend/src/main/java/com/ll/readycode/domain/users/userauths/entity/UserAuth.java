package com.ll.readycode.domain.users.userauths.entity;

import com.ll.readycode.domain.users.userprofiles.entity.UserProfile;
import com.ll.readycode.global.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAuth extends BaseEntity {

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private String provider;

  @Column(nullable = false)
  private String providerId;

  @ManyToOne(fetch = FetchType.LAZY)
  private UserProfile userProfile;

  @Builder
  public UserAuth(String email, String provider, String providerId, UserProfile userProfile) {
    this.email = email;
    this.provider = provider;
    this.providerId = providerId;
    this.userProfile = userProfile;
  }

  public void updateUserProfile(UserProfile userProfile) {
    this.userProfile = userProfile;
  }
}
