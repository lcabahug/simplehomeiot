package com.codingcuriosity.project.simplehomeiot.controls.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;

/**
 * ControlRegistrationResponse
 */
@Validated

public class ControlRegistrationResponse {
  @JsonProperty("controlId")
  private UUID controlId = null;

  public ControlRegistrationResponse controlId(UUID controlId) {
    this.controlId = controlId;
    return this;
  }

  /**
   * Get controlId
   * 
   * @return controlId
   **/
  @Valid
  public UUID getControlId() {
    return controlId;
  }

  public void setControlId(UUID controlId) {
    this.controlId = controlId;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ControlRegistrationResponse controlRegistrationResponse = (ControlRegistrationResponse) o;
    return Objects.equals(this.controlId, controlRegistrationResponse.controlId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(controlId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ControlRegistrationResponse {\n");

    sb.append("    controlId: ").append(toIndentedString(controlId)).append("\n");
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
