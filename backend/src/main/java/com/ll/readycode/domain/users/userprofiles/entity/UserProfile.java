package com.ll.readycode.domain.users.userprofiles.entity;

import com.ll.readycode.domain.users.userauths.entity.UserAuth;
import com.ll.readycode.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfile extends BaseEntity {

  private String phoneNumber;

  private String nickname;

  private UserPurpose purpose;

  @Enumerated(EnumType.STRING)
  private UserRole role;

  @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL)
  private List<UserAuth> userAuths;

  @Builder
  public UserProfile(String phoneNumber, String nickname, UserPurpose purpose, UserRole role) {
    this.phoneNumber = phoneNumber;
    this.nickname = nickname;
    this.purpose = purpose;
    this.role = role;
    this.userAuths = new ArrayList<>();
  }

  public void updateNickname(String nickname) {
    this.nickname = nickname;
  }

  public void addUserAuth(UserAuth userAuth) {
    userAuths.add(userAuth);
    userAuth.updateUserProfile(this);
  }
}
