package com.codingcuriosity.project.simplehomeiot.controls.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import javax.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

/**
 * ControlRegistration
 */
@Validated

public class ControlRegistration {
  @JsonProperty("name")
  private String name = null;

  @JsonProperty("description")
  private String description = null;

  public ControlRegistration name(String name) {
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

  public ControlRegistration description(String description) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ControlRegistration controlRegistration = (ControlRegistration) o;
    return Objects.equals(this.name, controlRegistration.name)
        && Objects.equals(this.description, controlRegistration.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, description);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ControlRegistration {\n");

    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
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
