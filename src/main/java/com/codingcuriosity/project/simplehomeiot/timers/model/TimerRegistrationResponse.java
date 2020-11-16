package com.codingcuriosity.project.simplehomeiot.timers.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;

/**
 * TimerRegistrationResponse
 */
@Validated

public class TimerRegistrationResponse {
  @JsonProperty("timerId")
  private UUID timerId = null;

  public TimerRegistrationResponse timerId(UUID timerId) {
    this.timerId = timerId;
    return this;
  }

  /**
   * Get timerId
   * 
   * @return timerId
   **/
  @Valid
  public UUID getTimerId() {
    return timerId;
  }

  public void setTimerId(UUID timerId) {
    this.timerId = timerId;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TimerRegistrationResponse timerRegistrationResponse = (TimerRegistrationResponse) o;
    return Objects.equals(this.timerId, timerRegistrationResponse.timerId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timerId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TimerRegistrationResponse {\n");

    sb.append("    timerId: ").append(toIndentedString(timerId)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
