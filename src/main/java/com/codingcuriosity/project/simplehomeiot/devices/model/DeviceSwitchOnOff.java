package com.codingcuriosity.project.simplehomeiot.devices.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

/**
 * DeviceSwitchOnOff
 */
@Validated

public class DeviceSwitchOnOff {
  @JsonProperty("state")
  private CommonDeviceState state = null;

  public DeviceSwitchOnOff state(CommonDeviceState state) {
    this.state = state;
    return this;
  }

  /**
   * Get state
   * 
   * @return state
   **/
  @NotNull
  @Valid
  public CommonDeviceState getState() {
    return state;
  }

  public void setState(CommonDeviceState state) {
    this.state = state;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeviceSwitchOnOff deviceSwitchOnOff = (DeviceSwitchOnOff) o;
    return Objects.equals(this.state, deviceSwitchOnOff.state);
  }

  @Override
  public int hashCode() {
    return Objects.hash(state);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeviceSwitchOnOff {\n");

    sb.append("    state: ").append(toIndentedString(state)).append("\n");
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
