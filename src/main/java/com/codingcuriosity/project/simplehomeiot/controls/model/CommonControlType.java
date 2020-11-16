package com.codingcuriosity.project.simplehomeiot.controls.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CommonControlType {
  DIRECT("direct"), //
  SLIDER("slider"), //
  TOGGLE("toggle");

  private String value;

  CommonControlType(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static CommonControlType fromValue(String text) {
    for (CommonControlType b : CommonControlType.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
