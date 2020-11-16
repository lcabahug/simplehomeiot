package com.codingcuriosity.project.simplehomeiot.devices.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.validation.annotation.Validated;

/**
 * DeviceInfo
 */
@Validated

@RedisHash("devices")
public class DeviceInfo implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -1896329016832658930L;

  @JsonProperty("deviceId")
  @Id
  private UUID deviceId = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("manufacturer")
  private String manufacturer = null;

  @JsonProperty("comment")
  private String comment = null;

  @JsonProperty("address")
  private String address = null;

  @JsonProperty("state")
  private CommonDeviceState state = null;

  @JsonProperty("availableControls")
  @Valid
  private List<DeviceInfoAvailableControls> availableControls = null;

  @JsonProperty("isRegistered")
  private Boolean isRegistered = null;

  @JsonProperty("isGrouped")
  private Boolean isGrouped = false;

  @JsonProperty("groupId")
  private UUID groupId = null;

  public DeviceInfo deviceId(UUID deviceId) {
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

  public DeviceInfo name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * 
   * @return name
   **/
  @Size(min = 2, max = 128)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public DeviceInfo manufacturer(String manufacturer) {
    this.manufacturer = manufacturer;
    return this;
  }

  /**
   * Get manufacturer
   * 
   * @return manufacturer
   **/
  @Size(min = 2, max = 256)
  public String getManufacturer() {
    return manufacturer;
  }

  public void setManufacturer(String manufacturer) {
    this.manufacturer = manufacturer;
  }

  public DeviceInfo comment(String comment) {
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

  public DeviceInfo address(String address) {
    this.address = address;
    return this;
  }

  /**
   * Get address
   * 
   * @return address
   **/
  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public DeviceInfo state(CommonDeviceState state) {
    this.state = state;
    return this;
  }

  /**
   * Get state
   * 
   * @return state
   **/
  @Valid
  public CommonDeviceState getState() {
    return state;
  }

  public void setState(CommonDeviceState state) {
    this.state = state;
  }

  public DeviceInfo availableControls(List<DeviceInfoAvailableControls> availableControls) {
    this.availableControls = availableControls;
    return this;
  }

  public DeviceInfo addAvailableControlsItem(DeviceInfoAvailableControls availableControlsItem) {
    if (this.availableControls == null) {
      this.availableControls = new ArrayList<DeviceInfoAvailableControls>();
    }
    this.availableControls.add(availableControlsItem);
    return this;
  }

  /**
   * Get availableControls
   * 
   * @return availableControls
   **/
  @Valid
  public List<DeviceInfoAvailableControls> getAvailableControls() {
    return availableControls;
  }

  public void setAvailableControls(List<DeviceInfoAvailableControls> availableControls) {
    this.availableControls = availableControls;
  }

  public DeviceInfo isRegistered(Boolean isRegistered) {
    this.isRegistered = isRegistered;
    return this;
  }

  /**
   * Get isRegistered
   * 
   * @return isRegistered
   **/
  public Boolean isIsRegistered() {
    return isRegistered;
  }

  public void setIsRegistered(Boolean isRegistered) {
    this.isRegistered = isRegistered;
  }

  public DeviceInfo isGrouped(Boolean isGrouped) {
    this.isGrouped = isGrouped;
    return this;
  }

  /**
   * Get isGrouped
   * 
   * @return isGrouped
   **/
  public Boolean isIsGrouped() {
    return isGrouped;
  }

  public void setIsGrouped(Boolean isGrouped) {
    this.isGrouped = isGrouped;
  }

  public DeviceInfo groupId(UUID groupId) {
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeviceInfo deviceInfo = (DeviceInfo) o;
    return Objects.equals(this.deviceId, deviceInfo.deviceId)
        && Objects.equals(this.name, deviceInfo.name)
        && Objects.equals(this.manufacturer, deviceInfo.manufacturer)
        && Objects.equals(this.comment, deviceInfo.comment)
        && Objects.equals(this.address, deviceInfo.address)
        && Objects.equals(this.state, deviceInfo.state)
        && Objects.equals(this.availableControls, deviceInfo.availableControls)
        && Objects.equals(this.isRegistered, deviceInfo.isRegistered)
        && Objects.equals(this.isGrouped, deviceInfo.isGrouped)
        && Objects.equals(this.groupId, deviceInfo.groupId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(deviceId, name, manufacturer, comment, address, state, availableControls,
        isRegistered, isGrouped, groupId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeviceInfo {\n");

    sb.append("    deviceId: ").append(toIndentedString(deviceId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    manufacturer: ").append(toIndentedString(manufacturer)).append("\n");
    sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    availableControls: ").append(toIndentedString(availableControls)).append("\n");
    sb.append("    isRegistered: ").append(toIndentedString(isRegistered)).append("\n");
    sb.append("    isGrouped: ").append(toIndentedString(isGrouped)).append("\n");
    sb.append("    groupId: ").append(toIndentedString(groupId)).append("\n");
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
