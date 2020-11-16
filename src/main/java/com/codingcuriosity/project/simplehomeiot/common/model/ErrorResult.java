package com.codingcuriosity.project.simplehomeiot.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

/**
 * ControllerErrorResult
 */
@Validated

public class ErrorResult {
  @JsonProperty("type")
  private ErrorType type = null;

  @JsonProperty("message")
  private String message = null;

  @JsonProperty("timestamp")
  private OffsetDateTime timestamp = null;

  @JsonProperty("details")
  private String details = null;

  @JsonProperty("logId")
  private UUID logId = null;

  public ErrorResult type(ErrorType type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * 
   * @return type
   **/
  public ErrorType getType() {
    return type;
  }

  public void setType(ErrorType type) {
    this.type = type;
  }

  public ErrorResult message(String message) {
    this.message = message;
    return this;
  }

  /**
   * Get message
   * 
   * @return message
   **/
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public ErrorResult timestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * 
   * @return timestamp
   **/
  @Valid
  public OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public ErrorResult details(String details) {
    this.details = details;
    return this;
  }

  /**
   * Get details
   * 
   * @return details
   **/
  @Size(min = 4)
  public String getDetails() {
    return details;
  }

  public void setDetails(String details) {
    this.details = details;
  }

  public ErrorResult logId(UUID logId) {
    this.logId = logId;
    return this;
  }

  /**
   * Get logId
   * 
   * @return logId
   **/
  @Valid
  public UUID getLogId() {
    return logId;
  }

  public void setLogId(UUID logId) {
    this.logId = logId;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ErrorResult controllerErrorResult = (ErrorResult) o;
    return Objects.equals(this.type, controllerErrorResult.type)
        && Objects.equals(this.message, controllerErrorResult.message)
        && Objects.equals(this.timestamp, controllerErrorResult.timestamp)
        && Objects.equals(this.details, controllerErrorResult.details)
        && Objects.equals(this.logId, controllerErrorResult.logId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, message, timestamp, details, logId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ControllerErrorResult {\n");

    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    details: ").append(toIndentedString(details)).append("\n");
    sb.append("    logId: ").append(toIndentedString(logId)).append("\n");
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
