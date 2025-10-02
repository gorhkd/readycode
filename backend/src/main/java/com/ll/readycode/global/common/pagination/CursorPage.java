package com.ll.readycode.global.common.pagination;

import java.util.List;

public record CursorPage<T>(String nextCursor, boolean hasNext, List<T> items) {}
