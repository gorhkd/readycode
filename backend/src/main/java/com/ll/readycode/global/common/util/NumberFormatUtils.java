package com.ll.readycode.global.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 숫자 포맷/스케일링 유틸. - 별점 표시용 문자열 포맷(소수 1자리, 절삭) - 별점 ×10 정수 스케일링(반올림) - 스케일 합계와 개수로 평균 계산(소수 2자리, 반올림)
 */
public final class NumberFormatUtils {

  private NumberFormatUtils() {}

  /** null 안전, 소수 1자리 "절삭" 후 문자열 반환 (예: 4.59 -> "4.5") */
  public static String formatToOneDecimal(BigDecimal v) {
    if (v == null) return "0.0";
    return v.setScale(1, RoundingMode.DOWN).toPlainString();
  }

  /** 별점 BigDecimal을 ×10 후 정수(long)로 (예: 4.3 -> 43), 반올림(HALF_UP) */
  public static long toScaledRating(BigDecimal rating) {
    if (rating == null) return 0L;
    return rating.multiply(BigDecimal.TEN).setScale(0, RoundingMode.HALF_UP).longValueExact();
  }

  /** 스케일 합계(ratingSum×10)와 개수로 평균(소수 2자리 반올림) 계산 */
  public static BigDecimal avgFromScaled(long ratingSumTimes10, long count) {
    if (count <= 0) return new BigDecimal("0.00");
    BigDecimal sum = BigDecimal.valueOf(ratingSumTimes10).divide(BigDecimal.TEN);
    return sum.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP);
  }
}
