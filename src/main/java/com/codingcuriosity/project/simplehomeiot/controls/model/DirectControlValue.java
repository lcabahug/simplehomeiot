package com.codingcuriosity.project.simplehomeiot.controls.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import javax.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

/**
 * DirectControlValue
 */
@Validated

public class DirectControlValue {
  @JsonProperty("newValue")
  private String newValue = null;

  public DirectControlValue newValue(String newValue) {
    this.newValue = newValue;
    return this;
  }

  /**
   * Get newValue
   * 
   * @return newValue
   **/
  @Size(min = 1, max = 128)
  public String getNewValue() {
    return newValue;
  }

  public void setNewValue(String newValue) {
    this.newValue = newValue;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DirectControlValue directControlValue = (DirectControlValue) o;
    return Objects.equals(this.newValue, directControlValue.newValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(newValue);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DirectControlValue {\n");

    sb.append("    newValue: ").append(toIndentedString(newValue)).append("\n");
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
