package com.codingcuriosity.project.simplehomeiot.controls.service;

import com.codingcuriosity.project.simplehomeiot.common.LogItem;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonAddResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonGetListResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonOperResponse;
import com.codingcuriosity.project.simplehomeiot.controls.model.ControlModification;
import com.codingcuriosity.project.simplehomeiot.controls.model.ControlRegistration;
import com.codingcuriosity.project.simplehomeiot.controls.model.DeviceControlInfo;
import com.codingcuriosity.project.simplehomeiot.controls.model.DirectControlValue;
import com.codingcuriosity.project.simplehomeiot.controls.model.SliderControlValue;
import java.util.UUID;

public interface ControlService extends LogItem {

  public abstract CommonGetListResponse<DeviceControlInfo> getDeviceControls();

  public abstract CommonGetListResponse<DeviceControlInfo> getFilteredDeviceControls(UUID deviceId);

  public abstract CommonAddResponse registerControl(UUID deviceId,
      ControlRegistration controlRegistration);

  public abstract CommonOperResponse setDirectControl(UUID controlId,
      DirectControlValue directControl);

  public abstract CommonOperResponse setSliderControl(UUID controlId,
      SliderControlValue sliderControl);

  public abstract CommonOperResponse setToggleControl(UUID controlId);

  public abstract CommonOperResponse modifyControl(UUID controlId,
      ControlModification controlModification);

  public abstract CommonOperResponse deleteControl(UUID controlId);

}
