package com.codingcuriosity.project.simplehomeiot.controls.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

/**
 * DeviceControlInfo
 */
@Validated

public class DeviceControlInfo implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -3933112688012745897L;

  @JsonProperty("deviceId")
  private UUID deviceId = null;

  @JsonProperty("controlId")
  private UUID controlId = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("currentValue")
  private String currentValue = null;

  public DeviceControlInfo deviceId(UUID deviceId) {
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

  public DeviceControlInfo controlId(UUID controlId) {
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

  public DeviceControlInfo name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * 
   * @return name
   **/
  @Valid
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public DeviceControlInfo description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * 
   * @return description
   **/
  @Valid
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public DeviceControlInfo currentValue(String currentValue) {
    this.currentValue = currentValue;
    return this;
  }

  /**
   * Get currentValue
   * 
   * @return currentValue
   **/
  @Size(min = 1, max = 128)
  public String getCurrentValue() {
    return currentValue;
  }

  public void setCurrentValue(String currentValue) {
    this.currentValue = currentValue;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeviceControlInfo deviceControlInfo = (DeviceControlInfo) o;
    return Objects.equals(this.deviceId, deviceControlInfo.deviceId)
        && Objects.equals(this.controlId, deviceControlInfo.controlId)
        && Objects.equals(this.name, deviceControlInfo.name)
        && Objects.equals(this.description, deviceControlInfo.description)
        && Objects.equals(this.currentValue, deviceControlInfo.currentValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(deviceId, controlId, currentValue);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeviceControlInfo {\n");

    sb.append("    deviceId: ").append(toIndentedString(deviceId)).append("\n");
    sb.append("    controlId: ").append(toIndentedString(controlId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    currentValue: ").append(toIndentedString(currentValue)).append("\n");
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
