package com.codingcuriosity.project.simplehomeiot.logs.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.validation.annotation.Validated;

/**
 * ControllerLogInfo
 */
@Validated

@RedisHash("controller-logs")
public class ControllerLogInfo implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 4194401720365148405L;

  @JsonProperty("timestamp")
  private OffsetDateTime timestamp = null;

  @JsonProperty("level")
  private CommonLogLevel level = null;

  @JsonProperty("title")
  private String title = null;

  @JsonProperty("message")
  private String message = null;

  @JsonProperty("logId")
  private UUID logId = null;

  public ControllerLogInfo timestamp(OffsetDateTime timestamp) {
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

  public ControllerLogInfo level(CommonLogLevel level) {
    this.level = level;
    return this;
  }

  /**
   * Get level
   * 
   * @return level
   **/
  @Valid
  public CommonLogLevel getLevel() {
    return level;
  }

  public void setLevel(CommonLogLevel level) {
    this.level = level;
  }

  public ControllerLogInfo title(String title) {
    this.title = title;
    return this;
  }

  /**
   * Get title
   * 
   * @return title
   **/
  @Size(min = 4, max = 128)
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public ControllerLogInfo message(String message) {
    this.message = message;
    return this;
  }

  /**
   * Get message
   * 
   * @return message
   **/
  @Size(min = 8)
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public ControllerLogInfo logId(UUID logId) {
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
    ControllerLogInfo controllerLogInfo = (ControllerLogInfo) o;
    return Objects.equals(this.timestamp, controllerLogInfo.timestamp)
        && Objects.equals(this.level, controllerLogInfo.level)
        && Objects.equals(this.title, controllerLogInfo.title)
        && Objects.equals(this.message, controllerLogInfo.message)
        && Objects.equals(this.logId, controllerLogInfo.logId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestamp, level, title, message, logId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ControllerLogInfo {\n");

    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
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
