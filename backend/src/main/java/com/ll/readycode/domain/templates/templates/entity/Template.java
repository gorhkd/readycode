package com.ll.readycode.domain.templates.templates.entity;

import com.ll.readycode.domain.categories.entity.Category;
import com.ll.readycode.domain.templates.files.entity.TemplateFile;
import com.ll.readycode.domain.users.userprofiles.entity.UserProfile;
import com.ll.readycode.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class Template extends BaseEntity {

  @Column(nullable = false)
  private String title;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(nullable = false)
  private int price;

  @Column(nullable = false)
  private String image;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "seller_id", nullable = false)
  private UserProfile seller;

  @OneToOne(
      mappedBy = "template",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  private TemplateFile templateFile;

  public void update(String title, String description, int price, String image, Category category) {
    this.title = title;
    this.description = description;
    this.price = price;
    this.image = image;
    this.category = category;
  }

  public void setTemplateFile(TemplateFile templateFile) {
    this.templateFile = templateFile;
    templateFile.setTemplate(this);
  }
}
