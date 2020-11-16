package com.codingcuriosity.project.simplehomeiot.devices.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;

/**
 * DeviceRegistrationResponse
 */
@Validated

public class DeviceRegistrationResponse {
  @JsonProperty("deviceId")
  private UUID deviceId = null;

  public DeviceRegistrationResponse deviceId(UUID deviceId) {
    this.deviceId = deviceId;
    return this;
  }

  /**
   * Get deviceId
   * 
   * @return deviceId
   **/
  @Valid
  public UUID getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(UUID deviceId) {
    this.deviceId = deviceId;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeviceRegistrationResponse deviceRegistrationResponse = (DeviceRegistrationResponse) o;
    return Objects.equals(this.deviceId, deviceRegistrationResponse.deviceId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(deviceId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeviceRegistrationResponse {\n");

    sb.append("    deviceId: ").append(toIndentedString(deviceId)).append("\n");
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
