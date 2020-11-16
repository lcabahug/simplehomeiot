package com.codingcuriosity.project.simplehomeiot.devices.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets CommonDeviceState
 */
public enum CommonDeviceState {
  OFF("off"), //
  ON("on");

  private String value;

  CommonDeviceState(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static CommonDeviceState fromValue(String text) {
    for (CommonDeviceState b : CommonDeviceState.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
