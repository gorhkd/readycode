package com.ll.readycode.domain.reviews.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SortType {
  LATEST("createdAt"),
  RATING("rating");

  private final String field;

  public static SortType from(String value) {
    try {
      return SortType.valueOf(value.toUpperCase());
    } catch (IllegalArgumentException e) {
      return LATEST; // 기본값
    }
  }
}
