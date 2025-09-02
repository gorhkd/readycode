package com.ll.readycode.global.common.pagination;

public final class PaginationPolicy {
  public static final int DEFAULT_LIMIT = 10;
  public static final int MIN_LIMIT = 1;
  public static final int MAX_LIMIT = 50;

  private PaginationPolicy() {}

  public static int clamp(Integer limit) {
    int v = (limit == null) ? DEFAULT_LIMIT : limit;
    return Math.max(MIN_LIMIT, Math.min(MAX_LIMIT, v));
  }
}
