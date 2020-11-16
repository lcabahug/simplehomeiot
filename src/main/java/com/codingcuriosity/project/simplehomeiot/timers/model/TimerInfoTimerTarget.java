package com.codingcuriosity.project.simplehomeiot.timers.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

/**
 * TimerInfoTimerTarget
 */
@Validated

public class TimerInfoTimerTarget implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -4976147674528258885L;

  @JsonProperty("controlId")
  private UUID controlId = null;

  @JsonProperty("action")
  private CommonTimerAction action = null;

  @JsonProperty("directValue")
  private String directValue = null;

  public TimerInfoTimerTarget controlId(UUID controlId) {
    this.controlId = controlId;
    return this;
  }

  /**
   * Get controlId
   * 
   * @return controlId
   **/
  @Valid
  public UUID getControlId() {
    return controlId;
  }

  public void setControlId(UUID controlId) {
    this.controlId = controlId;
  }

  public TimerInfoTimerTarget action(CommonTimerAction action) {
    this.action = action;
    return this;
  }

  /**
   * Get action
   * 
   * @return action
   **/
  public CommonTimerAction getAction() {
    return action;
  }

  public void setAction(CommonTimerAction action) {
    this.action = action;
  }

  public TimerInfoTimerTarget directValue(String directValue) {
    this.directValue = directValue;
    return this;
  }

  /**
   * Get directValue
   * 
   * @return directValue
   **/
  @Size(min = 1, max = 128)
  public String getDirectValue() {
    return directValue;
  }

  public void setDirectValue(String directValue) {
    this.directValue = directValue;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TimerInfoTimerTarget timerInfoTimerTarget = (TimerInfoTimerTarget) o;
    return Objects.equals(this.controlId, timerInfoTimerTarget.controlId)
        && Objects.equals(this.action, timerInfoTimerTarget.action)
        && Objects.equals(this.directValue, timerInfoTimerTarget.directValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(controlId, action, directValue);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TimerInfoTimerTarget {\n");

    sb.append("    controlId: ").append(toIndentedString(controlId)).append("\n");
    sb.append("    action: ").append(toIndentedString(action)).append("\n");
    sb.append("    directValue: ").append(toIndentedString(directValue)).append("\n");
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
