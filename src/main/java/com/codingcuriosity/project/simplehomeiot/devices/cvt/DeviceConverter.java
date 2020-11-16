package com.codingcuriosity.project.simplehomeiot.devices.cvt;

import com.codingcuriosity.project.simplehomeiot.devices.model.DeviceInfo;
import com.codingcuriosity.project.simplehomeiot.devices.model.DeviceModification;
import com.codingcuriosity.project.simplehomeiot.devices.model.DeviceRegistration;
import com.codingcuriosity.project.simplehomeiot.devices.model.DeviceSwitchOnOff;
import java.util.ArrayList;
import javax.validation.constraints.NotNull;

public class DeviceConverter {

  public static DeviceInfo createNewDeviceInfo(DeviceInfo deviceInfo,
      DeviceRegistration newDevice) {
    DeviceInfo newDeviceInfo = new DeviceInfo();
    newDeviceInfo.setDeviceId(deviceInfo.getDeviceId());
    newDeviceInfo.setName(newDevice.getName());
    newDeviceInfo.setManufacturer(newDevice.getManufacturer());
    newDeviceInfo.setComment(newDevice.getComment());
    newDeviceInfo.setAddress(deviceInfo.getAddress());
    newDeviceInfo.setState(deviceInfo.getState());
    if (deviceInfo.getAvailableControls() == null) {
      newDeviceInfo.setAvailableControls(new ArrayList<>());
    } else {
      newDeviceInfo.setAvailableControls(deviceInfo.getAvailableControls());
    }
    newDeviceInfo.setIsRegistered(true);
    newDeviceInfo.setIsGrouped(deviceInfo.isIsGrouped());
    newDeviceInfo.setGroupId(deviceInfo.getGroupId());
    return newDeviceInfo;
  }

  public static DeviceInfo getModifiedDeviceInfo(DeviceInfo deviceInfo,
      @NotNull DeviceModification deviceModInfo) {
    DeviceInfo newDeviceInfo = new DeviceInfo();
    newDeviceInfo.setDeviceId(deviceInfo.getDeviceId());
    newDeviceInfo.setName(deviceModInfo.getName());
    newDeviceInfo.setManufacturer(deviceModInfo.getManufacturer());
    newDeviceInfo.setComment(deviceModInfo.getComment());
    newDeviceInfo.setAddress(deviceInfo.getAddress());
    newDeviceInfo.setState(deviceInfo.getState());
    if (deviceInfo.getAvailableControls() == null) {
      newDeviceInfo.setAvailableControls(new ArrayList<>());
    } else {
      newDeviceInfo.setAvailableControls(deviceInfo.getAvailableControls());
    }
    newDeviceInfo.setIsRegistered(deviceInfo.isIsRegistered());
    newDeviceInfo.setIsGrouped(deviceInfo.isIsGrouped());
    newDeviceInfo.setGroupId(deviceInfo.getGroupId());
    return newDeviceInfo;
  }

  public static DeviceInfo getModifiedDeviceInfo(DeviceInfo deviceInfo,
      @NotNull DeviceSwitchOnOff deviceModInfo) {
    DeviceInfo newDeviceInfo = new DeviceInfo();
    newDeviceInfo.setDeviceId(deviceInfo.getDeviceId());
    newDeviceInfo.setName(deviceInfo.getName());
    newDeviceInfo.setManufacturer(deviceInfo.getManufacturer());
    newDeviceInfo.setComment(deviceInfo.getComment());
    newDeviceInfo.setAddress(deviceInfo.getAddress());
    newDeviceInfo.setState(deviceModInfo.getState());
    if (deviceInfo.getAvailableControls() == null) {
      newDeviceInfo.setAvailableControls(new ArrayList<>());
    } else {
      newDeviceInfo.setAvailableControls(deviceInfo.getAvailableControls());
    }
    newDeviceInfo.setIsRegistered(deviceInfo.isIsRegistered());
    newDeviceInfo.setIsGrouped(deviceInfo.isIsGrouped());
    newDeviceInfo.setGroupId(deviceInfo.getGroupId());
    return newDeviceInfo;
  }
}
