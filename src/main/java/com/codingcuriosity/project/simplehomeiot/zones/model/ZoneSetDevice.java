package com.codingcuriosity.project.simplehomeiot.zones.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

/**
 * ZoneSetDevice
 */
@Validated

public class ZoneSetDevice {
  @JsonProperty("deviceIds")
  @Valid
  private List<UUID> deviceIds = new ArrayList<UUID>();

  public ZoneSetDevice deviceIds(List<UUID> deviceIds) {
    this.deviceIds = deviceIds;
    return this;
  }

  public ZoneSetDevice addDeviceIdsItem(UUID deviceIdsItem) {
    this.deviceIds.add(deviceIdsItem);
    return this;
  }

  /**
   * Get deviceIds
   * 
   * @return deviceIds
   **/
  @NotNull
  @Valid
  public List<UUID> getDeviceIds() {
    return deviceIds;
  }

  public void setDeviceIds(List<UUID> deviceIds) {
    this.deviceIds = deviceIds;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ZoneSetDevice zoneSetDevice = (ZoneSetDevice) o;
    return Objects.equals(this.deviceIds, zoneSetDevice.deviceIds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(deviceIds);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ZoneSetDevice {\n");

    sb.append("    deviceIds: ").append(toIndentedString(deviceIds)).append("\n");
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
