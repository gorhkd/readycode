package com.ll.readycode.domain.users.userprofiles.entity;

import static com.ll.readycode.global.exception.ErrorCode.INVALID_USAGE_ROLE;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.ll.readycode.global.exception.CustomException;

public enum UserRole {
  USER("일반유저"),
  ADMIN("관리자");

  private final String label;

  UserRole(String label) {
    this.label = label;
  }

  @JsonValue
  public String getLabel() {
    return label;
  }

  @JsonCreator
  public static UserRole from(String value) {

    for (UserRole role : UserRole.values()) {
      if (role.name().equalsIgnoreCase(value) || role.label.equalsIgnoreCase(value)) {
        return role;
      }
    }

    throw new CustomException(INVALID_USAGE_ROLE);
  }
}
