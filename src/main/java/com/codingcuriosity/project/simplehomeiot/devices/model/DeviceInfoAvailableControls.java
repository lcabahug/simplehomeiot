package com.codingcuriosity.project.simplehomeiot.devices.model;

import com.codingcuriosity.project.simplehomeiot.controls.model.CommonControlType;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

/**
 * DeviceInfoAvailableControls
 */
@Validated

public class DeviceInfoAvailableControls implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 2408848090280694518L;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("controlType")
  private CommonControlType controlType = null;

  @JsonProperty("value")
  private String value = null;

  @JsonProperty("isManaged")
  private Boolean isManaged = false;

  public DeviceInfoAvailableControls name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * 
   * @return name
   **/
  @Size(min = 2, max = 64)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public DeviceInfoAvailableControls description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * 
   * @return description
   **/
  @Size(max = 128)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public DeviceInfoAvailableControls controlType(CommonControlType controlType) {
    this.controlType = controlType;
    return this;
  }

  /**
   * Get controlType
   * 
   * @return controlType
   **/
  public CommonControlType getControlType() {
    return controlType;
  }

  public void setControlType(CommonControlType controlType) {
    this.controlType = controlType;
  }

  public DeviceInfoAvailableControls value(String value) {
    this.value = value;
    return this;
  }

  /**
   * Get value
   * 
   * @return value
   **/
  @Size(min = 1, max = 128)
  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public DeviceInfoAvailableControls isManaged(Boolean isManaged) {
    this.isManaged = isManaged;
    return this;
  }

  /**
   * Get isManaged
   * 
   * @return isManaged
   **/
  public Boolean isIsManaged() {
    return isManaged;
  }

  public void setIsManaged(Boolean isManaged) {
    this.isManaged = isManaged;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeviceInfoAvailableControls deviceInfoAvailableControls = (DeviceInfoAvailableControls) o;
    return Objects.equals(this.name, deviceInfoAvailableControls.name)
        && Objects.equals(this.description, deviceInfoAvailableControls.description)
        && Objects.equals(this.controlType, deviceInfoAvailableControls.controlType)
        && Objects.equals(this.value, deviceInfoAvailableControls.value)
        && Objects.equals(this.isManaged, deviceInfoAvailableControls.isManaged);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, description, controlType, value, isManaged);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeviceInfoAvailableControls {\n");

    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    controlType: ").append(toIndentedString(controlType)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    isManaged: ").append(toIndentedString(isManaged)).append("\n");
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
