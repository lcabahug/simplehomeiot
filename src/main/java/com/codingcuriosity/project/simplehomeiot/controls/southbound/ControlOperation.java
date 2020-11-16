package com.codingcuriosity.project.simplehomeiot.controls.southbound;

import com.codingcuriosity.project.simplehomeiot.controls.model.DirectControlValue;
import com.codingcuriosity.project.simplehomeiot.controls.model.SliderControlValue;
import java.util.UUID;

public interface ControlOperation {

  public abstract void setDirect(UUID id, DirectControlValue directControlValue);

  public abstract void setSlider(UUID id, SliderControlValue sliderControlValue);

  public abstract void setToggle(UUID id);

}
