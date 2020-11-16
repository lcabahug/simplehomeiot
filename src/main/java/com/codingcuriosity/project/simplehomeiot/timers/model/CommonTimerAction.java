package com.codingcuriosity.project.simplehomeiot.timers.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CommonTimerAction {
  SWITCH_ON("switch-on"), //
  SWITCH_OFF("switch-off"), //
  DIRECT("direct"), //
  SLIDER_UP("slider-up"), //
  SLIDER_DOWN("slider-down"), //
  TOGGLE("toggle");

  private String value;

  CommonTimerAction(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static CommonTimerAction fromValue(String text) {
    for (CommonTimerAction b : CommonTimerAction.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
