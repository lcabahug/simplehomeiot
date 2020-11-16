package com.codingcuriosity.project.simplehomeiot.timers.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

/**
 * TimerRegistration
 */
@Validated

public class TimerRegistration {
  @JsonProperty("timerType")
  private CommonTimerType timerType = null;

  @JsonProperty("intervalPattern")
  private CommonTimerIntervalPattern intervalPattern = null;

  @JsonProperty("startDateTime")
  private OffsetDateTime startDateTime = null;

  @JsonProperty("endDateTime")
  private OffsetDateTime endDateTime = null;

  @JsonProperty("intervalPeriod")
  private Integer intervalPeriod = null;

  @JsonProperty("setDateTimes")
  @Valid
  private List<OffsetDateTime> setDateTimes = null;

  @JsonProperty("timerTarget")
  @Valid
  private List<TimerInfoTimerTarget> timerTarget = new ArrayList<TimerInfoTimerTarget>();

  public TimerRegistration timerType(CommonTimerType timerType) {
    this.timerType = timerType;
    return this;
  }

  /**
   * Get timerType
   * 
   * @return timerType
   **/
  @NotNull

  public CommonTimerType getTimerType() {
    return timerType;
  }

  public void setTimerType(CommonTimerType timerType) {
    this.timerType = timerType;
  }

  public TimerRegistration intervalPattern(CommonTimerIntervalPattern intervalPattern) {
    this.intervalPattern = intervalPattern;
    return this;
  }

  /**
   * Get intervalPattern
   * 
   * @return intervalPattern
   **/
  @NotNull

  public CommonTimerIntervalPattern getIntervalPattern() {
    return intervalPattern;
  }

  public void setIntervalPattern(CommonTimerIntervalPattern intervalPattern) {
    this.intervalPattern = intervalPattern;
  }

  public TimerRegistration startDateTime(OffsetDateTime startDateTime) {
    this.startDateTime = startDateTime;
    return this;
  }

  /**
   * Get startDateTime
   * 
   * @return startDateTime
   **/
  @Valid
  public OffsetDateTime getStartDateTime() {
    return startDateTime;
  }

  public void setStartDateTime(OffsetDateTime startDateTime) {
    this.startDateTime = startDateTime;
  }

  public TimerRegistration endDateTime(OffsetDateTime endDateTime) {
    this.endDateTime = endDateTime;
    return this;
  }

  /**
   * Get endDateTime
   * 
   * @return endDateTime
   **/
  @Valid
  public OffsetDateTime getEndDateTime() {
    return endDateTime;
  }

  public void setEndDateTime(OffsetDateTime endDateTime) {
    this.endDateTime = endDateTime;
  }

  public TimerRegistration intervalPeriod(Integer intervalPeriod) {
    this.intervalPeriod = intervalPeriod;
    return this;
  }

  /**
   * Get intervalPeriod
   * 
   * @return intervalPeriod
   **/
  public Integer getIntervalPeriod() {
    return intervalPeriod;
  }

  public void setIntervalPeriod(Integer intervalPeriod) {
    this.intervalPeriod = intervalPeriod;
  }

  public TimerRegistration setDateTimes(List<OffsetDateTime> setDateTimes) {
    this.setDateTimes = setDateTimes;
    return this;
  }

  public TimerRegistration addSetDateTimesItem(OffsetDateTime setDateTimesItem) {
    if (this.setDateTimes == null) {
      this.setDateTimes = new ArrayList<OffsetDateTime>();
    }
    this.setDateTimes.add(setDateTimesItem);
    return this;
  }

  /**
   * Get setDateTimes
   * 
   * @return setDateTimes
   **/
  @Valid
  public List<OffsetDateTime> getSetDateTimes() {
    return setDateTimes;
  }

  public void setSetDateTimes(List<OffsetDateTime> setDateTimes) {
    this.setDateTimes = setDateTimes;
  }

  public TimerRegistration timerTarget(List<TimerInfoTimerTarget> timerTarget) {
    this.timerTarget = timerTarget;
    return this;
  }

  public TimerRegistration addTimerTargetItem(TimerInfoTimerTarget timerTargetItem) {
    this.timerTarget.add(timerTargetItem);
    return this;
  }

  /**
   * Get timerTarget
   * 
   * @return timerTarget
   **/
  @NotNull
  @Valid
  public List<TimerInfoTimerTarget> getTimerTarget() {
    return timerTarget;
  }

  public void setTimerTarget(List<TimerInfoTimerTarget> timerTarget) {
    this.timerTarget = timerTarget;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TimerRegistration timerRegistration = (TimerRegistration) o;
    return Objects.equals(this.timerType, timerRegistration.timerType)
        && Objects.equals(this.intervalPattern, timerRegistration.intervalPattern)
        && Objects.equals(this.startDateTime, timerRegistration.startDateTime)
        && Objects.equals(this.endDateTime, timerRegistration.endDateTime)
        && Objects.equals(this.intervalPeriod, timerRegistration.intervalPeriod)
        && Objects.equals(this.setDateTimes, timerRegistration.setDateTimes)
        && Objects.equals(this.timerTarget, timerRegistration.timerTarget);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timerType, intervalPattern, startDateTime, endDateTime, intervalPeriod,
        setDateTimes, timerTarget);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TimerRegistration {\n");

    sb.append("    timerType: ").append(toIndentedString(timerType)).append("\n");
    sb.append("    intervalPattern: ").append(toIndentedString(intervalPattern)).append("\n");
    sb.append("    startDateTime: ").append(toIndentedString(startDateTime)).append("\n");
    sb.append("    endDateTime: ").append(toIndentedString(endDateTime)).append("\n");
    sb.append("    intervalPeriod: ").append(toIndentedString(intervalPeriod)).append("\n");
    sb.append("    setDateTimes: ").append(toIndentedString(setDateTimes)).append("\n");
    sb.append("    timerTarget: ").append(toIndentedString(timerTarget)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
