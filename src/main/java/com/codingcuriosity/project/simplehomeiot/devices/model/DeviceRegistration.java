package com.codingcuriosity.project.simplehomeiot.devices.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

/**
 * DeviceRegistration
 */
@Validated

public class DeviceRegistration {
  @NotNull
  @Size(min = 2, max = 128, message = "Device name must be between 2 to 128 characters long.")
  @JsonProperty("name")
  private String name = null;

  @NotNull
  @Size(min = 2, max = 256,
      message = "Device manufacturer must be between 2 to 256 characters long.")
  @JsonProperty("manufacturer")
  private String manufacturer = null;

  @NotNull
  @Size(min = 2, max = 1024, message = "Device name must be between 2 to 1024 characters long.")
  @JsonProperty("comment")
  private String comment = null;

  @NotNull
  // TODO: add validator!
  @JsonProperty("address")
  private String address = null;

  public DeviceRegistration name(String name) {
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

  public DeviceRegistration manufacturer(String manufacturer) {
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

  public DeviceRegistration comment(String comment) {
    this.comment = comment;
    return this;
  }

  /**
   * Get comment
   * 
   * @return comment
   **/
  @Size(min = 2, max = 1024)
  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public DeviceRegistration address(String address) {
    this.address = address;
    return this;
  }

  /**
   * Get address
   * 
   * @return address
   **/
  @NotNull
  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeviceRegistration deviceRegistration = (DeviceRegistration) o;
    return Objects.equals(this.name, deviceRegistration.name)
        && Objects.equals(this.manufacturer, deviceRegistration.manufacturer)
        && Objects.equals(this.comment, deviceRegistration.comment)
        && Objects.equals(this.address, deviceRegistration.address);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, manufacturer, comment, address);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeviceRegistration {\n");

    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    manufacturer: ").append(toIndentedString(manufacturer)).append("\n");
    sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
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
