package com.codingcuriosity.project.simplehomeiot.groups.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.Size;

public class GroupRawInfo implements Serializable {
  /**
   *  
   */
  private static final long serialVersionUID = 1087191000148839954L;

  private UUID groupId = null;

  private String name = null;

  private String description = null;

  private String comment = null;

  private Boolean isEnabled = false;

  public GroupRawInfo groupId(UUID groupId) {
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

  public GroupRawInfo name(String name) {
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

  public GroupRawInfo description(String description) {
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

  public GroupRawInfo comment(String comment) {
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

  public GroupRawInfo isEnabled(Boolean isEnabled) {
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

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GroupRawInfo groupInfo = (GroupRawInfo) o;
    return Objects.equals(this.groupId, groupInfo.groupId)
        && Objects.equals(this.name, groupInfo.name)
        && Objects.equals(this.description, groupInfo.description)
        && Objects.equals(this.comment, groupInfo.comment)
        && Objects.equals(this.isEnabled, groupInfo.isEnabled);
  }

  @Override
  public int hashCode() {
    return Objects.hash(groupId, name, description, comment, isEnabled);
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
