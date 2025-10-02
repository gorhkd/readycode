package com.ll.readycode.global.common.util;

import com.ll.readycode.global.exception.CustomException;
import com.ll.readycode.global.exception.ErrorCode;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Encoder {

  public static final int ENCODING_TYPE_LONG = 1;
  public static final int ENCODING_TYPE_INTEGER = 2;
  public static final int ENCODING_TYPE_STRING = 3;

  // 인스턴스 생성 방지
  private Encoder() {}

  /** 인코딩 (Long -> Base64) */
  public static String encode(Long target) {
    if (target == null) {
      return null;
    }

    return Base64.getUrlEncoder()
        .withoutPadding()
        .encodeToString(target.toString().getBytes(StandardCharsets.UTF_8));
  }

  /** 인코딩 (Integer -> Base64) */
  public static String encode(Integer target) {
    if (target == null) {
      return null;
    }

    return Base64.getUrlEncoder()
        .withoutPadding()
        .encodeToString(target.toString().getBytes(StandardCharsets.UTF_8));
  }

  /** 인코딩 (String -> Base64) */
  public static String encode(String target) {
    if (target == null) {
      return null;
    }

    return Base64.getUrlEncoder()
        .withoutPadding()
        .encodeToString(target.getBytes(StandardCharsets.UTF_8));
  }

  /** 디코딩 (Base64 -> Long, Integer, String) */
  public static Object decode(String encodedTarget, int encodedType) {
    if (encodedTarget == null || encodedTarget.isEmpty()) {
      return null;
    }

    try {
      byte[] decoded = Base64.getUrlDecoder().decode(encodedTarget);

      return switch (encodedType) {
        case ENCODING_TYPE_LONG -> Long.parseLong(new String(decoded, StandardCharsets.UTF_8));
        case ENCODING_TYPE_INTEGER -> Integer.parseInt(new String(decoded, StandardCharsets.UTF_8));
        case ENCODING_TYPE_STRING -> new String(decoded, StandardCharsets.UTF_8);
        default -> throw new CustomException(ErrorCode.UNSUPPORTED_RESULT_TYPE);
      };

    } catch (CustomException ce) {
      throw ce;

    } catch (Exception e) {
      throw new CustomException(ErrorCode.INVALID_ENCODED_VALUE);
    }
  }
}
