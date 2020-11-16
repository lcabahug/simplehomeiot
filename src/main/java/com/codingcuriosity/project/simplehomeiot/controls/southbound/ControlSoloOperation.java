package com.codingcuriosity.project.simplehomeiot.controls.southbound;

import com.codingcuriosity.project.simplehomeiot.controls.model.DirectControlValue;
import com.codingcuriosity.project.simplehomeiot.controls.model.SliderControlValue;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class ControlSoloOperation implements ControlOperation {

  @Override
  public void setDirect(UUID id, DirectControlValue directControlValue) {
    // Will be available on next release
  }

  @Override
  public void setSlider(UUID id, SliderControlValue sliderControlValue) {
    // Will be available on next release
  }

  @Override
  public void setToggle(UUID id) {
    // Will be available on next release
  }
}
