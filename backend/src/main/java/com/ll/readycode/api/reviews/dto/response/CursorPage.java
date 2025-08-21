package com.ll.readycode.api.reviews.dto.response;

import java.util.List;

public record CursorPage<T>(List<T> items, String nextCursor, boolean hasNext) {}
