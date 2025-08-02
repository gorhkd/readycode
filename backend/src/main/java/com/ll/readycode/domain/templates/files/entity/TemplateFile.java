package com.ll.readycode.domain.templates.files.entity;

import com.ll.readycode.domain.templates.templates.entity.Template;
import com.ll.readycode.global.common.entity.BaseCreatedOnlyEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@Entity
public class TemplateFile extends BaseCreatedOnlyEntity {
  @Column(nullable = false)
  private String originalName;

  @Column(nullable = false, length = 512)
  private String url;

  @Column(nullable = false, length = 50)
  private String extension;

  @Column(nullable = false)
  private Long fileSize;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "template_id", nullable = false)
  private Template template;
}
