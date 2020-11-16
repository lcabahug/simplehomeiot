package com.codingcuriosity.project.simplehomeiot.groups.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

/**
 * GroupRegistration
 */
@Validated

public class GroupRegistration {
  @JsonProperty("name")
  private String name = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("comment")
  private String comment = null;

  public GroupRegistration name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * 
   * @return name
   **/
  @NotNull
  @Size(min = 3, max = 64)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public GroupRegistration description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * 
   * @return description
   **/
  @NotNull
  @Size(max = 128)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public GroupRegistration comment(String comment) {
    this.comment = comment;
    return this;
  }

  /**
   * Get comment
   * 
   * @return comment
   **/
  @NotNull
  @Size(max = 128)
  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GroupRegistration groupRegistration = (GroupRegistration) o;
    return Objects.equals(this.name, groupRegistration.name)
        && Objects.equals(this.description, groupRegistration.description)
        && Objects.equals(this.comment, groupRegistration.comment);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, description, comment);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GroupRegistration {\n");

    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
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
