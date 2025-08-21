package com.ll.readycode.domain.templates.query;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TemplateSortType {
  LATEST("createdAt"),
  RATING("rating"),
  POPULAR("popular");

  private final String field;

  public static TemplateSortType from(String value) {
    try {
      return TemplateSortType.valueOf(value.toUpperCase());
    } catch (IllegalArgumentException e) {
      return LATEST;
    }
  }
}
