package com.codingcuriosity.project.simplehomeiot.logs.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets CommonLogLevel
 */
public enum CommonLogLevel {
  CRITICAL("critical"), //
  ERROR("error"), //
  WARNING("warning"), //
  INFO("info"), //
  DEBUG("debug"), //
  TRACE("trace");

  private String value;

  CommonLogLevel(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static CommonLogLevel fromValue(String text) {
    for (CommonLogLevel b : CommonLogLevel.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
