package com.ll.readycode.global.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SuccessResponse<T> {
  private final int status;
  private final String message;
  private final T data;

  public static <T> SuccessResponse<T> of(T data) {
    return new SuccessResponse<>(200, "요청이 성공했습니다.", data);
  }

  public static <T> SuccessResponse<T> of(String message, T data) {
    return new SuccessResponse<>(200, message, data);
  }
}
