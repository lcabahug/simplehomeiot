package com.codingcuriosity.project.simplehomeiot.controls.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import org.springframework.validation.annotation.Validated;

/**
 * SliderControlValue
 */
@Validated

public class SliderControlValue {
  @JsonProperty("direction")
  private CommonSliderDirection direction = CommonSliderDirection.UP;

  public SliderControlValue direction(CommonSliderDirection direction) {
    this.direction = direction;
    return this;
  }

  /**
   * Get direction
   * 
   * @return direction
   **/
  public CommonSliderDirection getDirection() {
    return direction;
  }

  public void setDirection(CommonSliderDirection direction) {
    this.direction = direction;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SliderControlValue sliderControlValue = (SliderControlValue) o;
    return Objects.equals(this.direction, sliderControlValue.direction);
  }

  @Override
  public int hashCode() {
    return Objects.hash(direction);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SliderControlValue {\n");

    sb.append("    direction: ").append(toIndentedString(direction)).append("\n");
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
