package com.codingcuriosity.project.simplehomeiot.groups.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

/**
 * GroupInfo
 */
@Validated

public class GroupInfo {
  @JsonProperty("groupId")
  private UUID groupId = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("comment")
  private String comment = null;

  @JsonProperty("isEnabled")
  private Boolean isEnabled = true;

  @JsonProperty("devices")
  @Valid
  private List<UUID> devices = null;

  public GroupInfo groupId(UUID groupId) {
    this.groupId = groupId;
    return this;
  }

  /**
   * Get groupId
   * 
   * @return groupId
   **/
  @Valid
  public UUID getGroupId() {
    return groupId;
  }

  public void setGroupId(UUID groupId) {
    this.groupId = groupId;
  }

  public GroupInfo name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * 
   * @return name
   **/
  @Size(min = 3, max = 64)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public GroupInfo description(String description) {
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

  public GroupInfo comment(String comment) {
    this.comment = comment;
    return this;
  }

  /**
   * Get comment
   * 
   * @return comment
   **/
  @Size(max = 128)
  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public GroupInfo isEnabled(Boolean isEnabled) {
    this.isEnabled = isEnabled;
    return this;
  }

  /**
   * Get isEnabled
   * 
   * @return isEnabled
   **/
  public Boolean isIsEnabled() {
    return isEnabled;
  }

  public void setIsEnabled(Boolean isEnabled) {
    this.isEnabled = isEnabled;
  }

  public GroupInfo devices(List<UUID> devices) {
    this.devices = devices;
    return this;
  }

  public GroupInfo addDevicesItem(UUID devicesItem) {
    if (this.devices == null) {
      this.devices = new ArrayList<UUID>();
    }
    this.devices.add(devicesItem);
    return this;
  }

  /**
   * Get devices
   * 
   * @return devices
   **/
  @Valid
  public List<UUID> getDevices() {
    return devices;
  }

  public void setDevices(List<UUID> devices) {
    this.devices = devices;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GroupInfo groupInfo = (GroupInfo) o;
    return Objects.equals(this.groupId, groupInfo.groupId)
        && Objects.equals(this.name, groupInfo.name)
        && Objects.equals(this.description, groupInfo.description)
        && Objects.equals(this.comment, groupInfo.comment)
        && Objects.equals(this.isEnabled, groupInfo.isEnabled)
        && Objects.equals(this.devices, groupInfo.devices);
  }

  @Override
  public int hashCode() {
    return Objects.hash(groupId, name, description, comment, isEnabled, devices);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GroupInfo {\n");

    sb.append("    groupId: ").append(toIndentedString(groupId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
    sb.append("    isEnabled: ").append(toIndentedString(isEnabled)).append("\n");
    sb.append("    devices: ").append(toIndentedString(devices)).append("\n");
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
