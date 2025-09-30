package com.ll.readycode.global.common.pagination;

import java.util.List;

public record CursorPage<T>(List<T> items, String nextCursor, boolean hasNext) {}
