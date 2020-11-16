package com.codingcuriosity.project.simplehomeiot.zones.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;

/**
 * ZoneRegistrationResponse
 */
@Validated

public class ZoneRegistrationResponse {
  @JsonProperty("zoneId")
  private UUID zoneId = null;

  public ZoneRegistrationResponse zoneId(UUID zoneId) {
    this.zoneId = zoneId;
    return this;
  }

  /**
   * Get zoneId
   * 
   * @return zoneId
   **/
  @Valid
  public UUID getZoneId() {
    return zoneId;
  }

  public void setZoneId(UUID zoneId) {
    this.zoneId = zoneId;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ZoneRegistrationResponse zoneRegistrationResponse = (ZoneRegistrationResponse) o;
    return Objects.equals(this.zoneId, zoneRegistrationResponse.zoneId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(zoneId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ZoneRegistrationResponse {\n");

    sb.append("    zoneId: ").append(toIndentedString(zoneId)).append("\n");
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
