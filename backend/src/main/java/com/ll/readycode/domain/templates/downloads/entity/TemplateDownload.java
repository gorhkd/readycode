package com.ll.readycode.domain.templates.downloads.entity;

import com.ll.readycode.domain.templates.templates.entity.Template;
import com.ll.readycode.domain.users.userprofiles.entity.UserProfile;
import com.ll.readycode.global.common.entity.BaseCreatedOnlyEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
@Entity
public class TemplateDownload extends BaseCreatedOnlyEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private UserProfile user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "template_id", nullable = false)
  private Template template;

  @Column(nullable = false)
  private String ipAddress;

  @Column(nullable = true)
  private String device;
}
