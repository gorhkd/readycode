package com.ll.readycode.domain.users.userprofiles.entity;

import static com.ll.readycode.global.exception.ErrorCode.INVALID_USAGE_PURPOSE;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.ll.readycode.global.exception.CustomException;

public enum UserPurpose {
  LEARNING("학습용"),
  LECTURE("강의용");

  private final String label;

  UserPurpose(String label) {
    this.label = label;
  }

  @JsonValue
  public String getLabel() {
    return label;
  }

  @JsonCreator
  public static UserPurpose from(String value) {

    for (UserPurpose purpose : UserPurpose.values()) {
      if (purpose.name().equalsIgnoreCase(value) || purpose.label.equalsIgnoreCase(value)) {
        return purpose;
      }
    }

    throw new CustomException(INVALID_USAGE_PURPOSE);
  }
}
