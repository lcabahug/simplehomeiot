package com.codingcuriosity.project.simplehomeiot.zones.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

/**
 * ZoneCreation
 */
@Validated

public class ZoneCreation {
  @JsonProperty("name")
  private String name = null;

  @JsonProperty("comment")
  private String comment = null;

  @JsonProperty("deviceIds")
  @Valid
  private List<UUID> deviceIds = null;

  public ZoneCreation name(String name) {
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

  public ZoneCreation comment(String comment) {
    this.comment = comment;
    return this;
  }

  /**
   * Get comment
   * 
   * @return comment
   **/
  @NotNull
  @Size(min = 8, max = 1024)
  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public ZoneCreation deviceIds(List<UUID> deviceIds) {
    this.deviceIds = deviceIds;
    return this;
  }

  public ZoneCreation addDeviceIdsItem(UUID deviceIdsItem) {
    if (this.deviceIds == null) {
      this.deviceIds = new ArrayList<UUID>();
    }
    this.deviceIds.add(deviceIdsItem);
    return this;
  }

  /**
   * Get deviceIds
   * 
   * @return deviceIds
   **/
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
    ZoneCreation zoneCreation = (ZoneCreation) o;
    return Objects.equals(this.name, zoneCreation.name)
        && Objects.equals(this.comment, zoneCreation.comment)
        && Objects.equals(this.deviceIds, zoneCreation.deviceIds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, comment, deviceIds);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ZoneCreation {\n");

    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
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
