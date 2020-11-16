package com.codingcuriosity.project.simplehomeiot.timers.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

/**
 * TimerSetAction
 */
@Validated

public class TimerSetAction {
  @JsonProperty("timerTarget")
  @Valid
  private List<TimerInfoTimerTarget> timerTarget = new ArrayList<TimerInfoTimerTarget>();

  public TimerSetAction timerTarget(List<TimerInfoTimerTarget> timerTarget) {
    this.timerTarget = timerTarget;
    return this;
  }

  public TimerSetAction addTimerTargetItem(TimerInfoTimerTarget timerTargetItem) {
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
    TimerSetAction timerSetAction = (TimerSetAction) o;
    return Objects.equals(this.timerTarget, timerSetAction.timerTarget);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timerTarget);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TimerSetAction {\n");

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
