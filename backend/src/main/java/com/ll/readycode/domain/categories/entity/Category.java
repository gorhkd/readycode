package com.ll.readycode.domain.categories.entity;

import com.ll.readycode.global.common.entity.BaseCreatedOnlyEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseCreatedOnlyEntity {

  @Column(nullable = false, unique = true)
  private String name;

  @Builder
  public Category(String name) {
    this.name = name;
  }
}
