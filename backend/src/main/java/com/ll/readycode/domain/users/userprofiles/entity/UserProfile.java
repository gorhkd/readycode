package com.ll.readycode.domain.users.userprofiles.entity;

import com.ll.readycode.domain.users.userauths.entity.UserAuth;
import com.ll.readycode.global.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.Getter;

@Entity
@Getter
public class UserProfile extends BaseEntity {

  private String nickname;

  private String createdReason;

  @OneToMany
  List<UserAuth> userAuths;
}
