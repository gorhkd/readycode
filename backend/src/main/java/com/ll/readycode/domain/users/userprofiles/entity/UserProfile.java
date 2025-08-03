package com.ll.readycode.domain.users.userprofiles.entity;

import com.ll.readycode.domain.users.userauths.entity.UserAuth;
import com.ll.readycode.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfile extends BaseEntity {

  @Column(nullable = false)
  private String phoneNumber;

  @Column(nullable = false)
  private String nickname;

  @Enumerated(EnumType.STRING)
  private UserPurpose purpose;

  private boolean isDeleted;

  @Embedded private DeleteInfo deleteInfo;

  @Enumerated(EnumType.STRING)
  private UserRole role;

  @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL)
  @Builder.Default
  private List<UserAuth> userAuths = new ArrayList<>();

  public void updateNickname(String nickname) {
    this.nickname = nickname;
  }

  public void updateDeletedStatus(boolean isDeleted) {

    this.isDeleted = isDeleted;

    if (isDeleted) {
      this.deleteInfo =
          DeleteInfo.builder()
              .deletedAt(LocalDateTime.now())
              .scheduledDeletionDate(LocalDateTime.now().plusDays(30))
              .build();

    } else {
      this.deleteInfo = null;
    }
  }

  public void addUserAuth(UserAuth userAuth) {
    userAuths.add(userAuth);
    userAuth.updateUserProfile(this);
  }

  @Embeddable
  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class DeleteInfo {

    private LocalDateTime deletedAt;

    private LocalDateTime scheduledDeletionDate;

    @Builder
    public DeleteInfo(LocalDateTime deletedAt, LocalDateTime scheduledDeletionDate) {
      this.deletedAt = deletedAt;
      this.scheduledDeletionDate = scheduledDeletionDate;
    }
  }
}
