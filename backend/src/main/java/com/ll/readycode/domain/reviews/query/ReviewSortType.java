package com.ll.readycode.domain.reviews.query;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReviewSortType {
  LATEST("createdAt"),
  RATING("rating");

  private final String field;

  public static ReviewSortType from(String value) {
    try {
      return ReviewSortType.valueOf(value.toUpperCase());
    } catch (IllegalArgumentException e) {
      return LATEST; // 기본값
    }
  }
}
