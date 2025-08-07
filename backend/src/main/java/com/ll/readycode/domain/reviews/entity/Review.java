package com.ll.readycode.domain.reviews.entity;

import com.ll.readycode.domain.templates.templates.entity.Template;
import com.ll.readycode.domain.users.userprofiles.entity.UserProfile;
import com.ll.readycode.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
public class Review extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "template_id", nullable = false)
  private Template template;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_profile_id", nullable = false)
  private UserProfile userProfile;

  @Column(nullable = false)
  private Double rating;

  @Column(nullable = false, length = 1000)
  private String content;

  public void updateReview(String content, Double rating) {
    this.content = content;
    this.rating = rating;
  }
}
