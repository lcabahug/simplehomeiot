package com.codingcuriosity.project.simplehomeiot.devices.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

/**
 * DeviceModification
 */
@Validated

public class DeviceModification {
  @JsonProperty("name")
  private String name = null;

  @JsonProperty("manufacturer")
  private String manufacturer = null;

  @JsonProperty("comment")
  private String comment = null;

  public DeviceModification name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * 
   * @return name
   **/
  @NotNull
  @Size(min = 2, max = 128)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public DeviceModification manufacturer(String manufacturer) {
    this.manufacturer = manufacturer;
    return this;
  }

  /**
   * Get manufacturer
   * 
   * @return manufacturer
   **/
  @NotNull
  @Size(min = 2, max = 256)
  public String getManufacturer() {
    return manufacturer;
  }

  public void setManufacturer(String manufacturer) {
    this.manufacturer = manufacturer;
  }

  public DeviceModification comment(String comment) {
    this.comment = comment;
    return this;
  }

  /**
   * Get comment
   * 
   * @return comment
   **/
  @NotNull
  @Size(min = 2, max = 1024)
  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeviceModification deviceModification = (DeviceModification) o;
    return Objects.equals(this.name, deviceModification.name)
        && Objects.equals(this.manufacturer, deviceModification.manufacturer)
        && Objects.equals(this.comment, deviceModification.comment);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, manufacturer, comment);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeviceModification {\n");

    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    manufacturer: ").append(toIndentedString(manufacturer)).append("\n");
    sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
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
