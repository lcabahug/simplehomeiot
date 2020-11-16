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
 * TimerModification
 */
@Validated

public class TimerModification {
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

  public TimerModification timerType(CommonTimerType timerType) {
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

  public TimerModification intervalPattern(CommonTimerIntervalPattern intervalPattern) {
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

  public TimerModification startDateTime(OffsetDateTime startDateTime) {
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

  public TimerModification endDateTime(OffsetDateTime endDateTime) {
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

  public TimerModification intervalPeriod(Integer intervalPeriod) {
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

  public TimerModification setDateTimes(List<OffsetDateTime> setDateTimes) {
    this.setDateTimes = setDateTimes;
    return this;
  }

  public TimerModification addSetDateTimesItem(OffsetDateTime setDateTimesItem) {
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

  public TimerModification timerTarget(List<TimerInfoTimerTarget> timerTarget) {
    this.timerTarget = timerTarget;
    return this;
  }

  public TimerModification addTimerTargetItem(TimerInfoTimerTarget timerTargetItem) {
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
    TimerModification timerModification = (TimerModification) o;
    return Objects.equals(this.timerType, timerModification.timerType)
        && Objects.equals(this.intervalPattern, timerModification.intervalPattern)
        && Objects.equals(this.startDateTime, timerModification.startDateTime)
        && Objects.equals(this.endDateTime, timerModification.endDateTime)
        && Objects.equals(this.intervalPeriod, timerModification.intervalPeriod)
        && Objects.equals(this.setDateTimes, timerModification.setDateTimes)
        && Objects.equals(this.timerTarget, timerModification.timerTarget);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timerType, intervalPattern, startDateTime, endDateTime, intervalPeriod,
        setDateTimes, timerTarget);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TimerModification {\n");

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
