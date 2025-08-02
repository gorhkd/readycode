package com.ll.readycode.domain.templates.purchases.entity;

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
public class TemplatePurchase extends BaseCreatedOnlyEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "buyer_id", nullable = false)
  private UserProfile buyer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "template_id", nullable = false)
  private Template template;

  @Column(nullable = false)
  private int price;
}
