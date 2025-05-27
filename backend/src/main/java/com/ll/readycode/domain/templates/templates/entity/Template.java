package com.ll.readycode.domain.templates.templates.entity;

import com.ll.readycode.domain.categories.entity.Category;
import com.ll.readycode.domain.users.userprofiles.entity.UserProfile;
import com.ll.readycode.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Template extends BaseEntity {

  @Column(nullable = false)
  private String title;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(nullable = false)
  private int price;

  @Column(nullable = false)
  private String image;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "seller_id", nullable = false)
  private UserProfile seller;

  @Builder
  public Template(
      String title,
      String description,
      int price,
      String image,
      Category category,
      UserProfile seller) {
    this.title = title;
    this.description = description;
    this.price = price;
    this.image = image;
    this.category = category;
    this.seller = seller;
  }

  public void update(String title, String description, int price, String image, Category category) {
    this.title = title;
    this.description = description;
    this.price = price;
    this.image = image;
    this.category = category;
  }
}
