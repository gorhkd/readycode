package com.ll.readycode.domain.admin.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AdminSortType {
  CREATED("createdAt"),
  DOWNLOAD("download"),
  LIKE("like"),
  REVIEW("review");

  private final String field;

  public static AdminSortType from(String value) {
    try {
      return AdminSortType.valueOf(value.toUpperCase());
    } catch (IllegalArgumentException e) {
      return CREATED;
    }
  }
}
