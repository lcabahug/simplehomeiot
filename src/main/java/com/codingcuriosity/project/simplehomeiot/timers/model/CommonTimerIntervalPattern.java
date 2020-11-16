package com.codingcuriosity.project.simplehomeiot.timers.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CommonTimerIntervalPattern {
  TIMES_PER_DAY("timesPerDay"), //
  DAILY("daily"), //
  DAYS_PER_WEEK("daysPerWeek"), //
  WEEKLY("weekly"), //
  DAYS_PER_MONTH("daysPerMonth"), //
  MONTHLY("monthly"), //
  DAYS_PER_YEAR("daysPerYear"), //
  YEARLY("yearly"), //
  CUSTOM("custom"); //

  private String value;

  CommonTimerIntervalPattern(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static CommonTimerIntervalPattern fromValue(String text) {
    for (CommonTimerIntervalPattern b : CommonTimerIntervalPattern.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
