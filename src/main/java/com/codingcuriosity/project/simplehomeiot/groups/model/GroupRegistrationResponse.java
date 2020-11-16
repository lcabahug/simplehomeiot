package com.codingcuriosity.project.simplehomeiot.groups.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;

/**
 * GroupRegistrationResponse
 */
@Validated

public class GroupRegistrationResponse {
  @JsonProperty("groupId")
  private UUID groupId = null;

  public GroupRegistrationResponse groupId(UUID groupId) {
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
    GroupRegistrationResponse groupRegistrationResponse = (GroupRegistrationResponse) o;
    return Objects.equals(this.groupId, groupRegistrationResponse.groupId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(groupId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GroupRegistrationResponse {\n");

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
