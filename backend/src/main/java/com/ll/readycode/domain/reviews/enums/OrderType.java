package com.ll.readycode.domain.reviews.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderType {
  ASC("asc"),
  DESC("desc");

  private final String direction;

  public static OrderType from(String value) {
    try {
      return OrderType.valueOf(value.toUpperCase());
    } catch (IllegalArgumentException e) {
      return DESC; // 기본값
    }
  }
}
