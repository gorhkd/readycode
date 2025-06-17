package com.ll.readycode.domain.categories.entity;

import com.ll.readycode.global.common.entity.BaseCreatedOnlyEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class Category extends BaseCreatedOnlyEntity {

  @Column(nullable = false, unique = true)
  private String name;
}
