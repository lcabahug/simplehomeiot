package com.codingcuriosity.project.simplehomeiot.controls.cvt;

import com.codingcuriosity.project.simplehomeiot.controls.model.ControlModification;
import com.codingcuriosity.project.simplehomeiot.controls.model.DeviceControlInfo;
import com.codingcuriosity.project.simplehomeiot.controls.model.DirectControlValue;

public class ControlConverter {

  public static DeviceControlInfo getModifiedControlInfo(DeviceControlInfo controlInfo,
      ControlModification controlModInfo) {
    DeviceControlInfo newObj = controlInfo;
    newObj.setDescription(controlModInfo.getDescription());
    return newObj;
  }

  public static DeviceControlInfo getModifiedControlInfo(DeviceControlInfo controlInfo,
      DirectControlValue directControlValue) {
    DeviceControlInfo newObj = controlInfo;
    newObj.setCurrentValue(directControlValue.getNewValue());
    return newObj;
  }
}
