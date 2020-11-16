package com.codingcuriosity.project.simplehomeiot.devices.service;

import com.codingcuriosity.project.simplehomeiot.common.LogItem;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonAddResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonGetListResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonGetResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonOperResponse;
import com.codingcuriosity.project.simplehomeiot.devices.model.DeviceInfo;
import com.codingcuriosity.project.simplehomeiot.devices.model.DeviceModification;
import com.codingcuriosity.project.simplehomeiot.devices.model.DeviceRegistration;
import com.codingcuriosity.project.simplehomeiot.devices.model.DeviceSwitchOnOff;
import java.util.UUID;

public interface DeviceService extends LogItem {

  public abstract CommonGetListResponse<DeviceInfo> getDevices(Integer skip, Integer limit,
      Boolean includeRegistered, Boolean includeUnregistered);

  public abstract CommonGetResponse<DeviceInfo> getDeviceById(UUID deviceId);

  public abstract CommonAddResponse registerDevice(DeviceRegistration deviceRegInfo);

  public abstract CommonOperResponse modifyDevice(UUID deviceId, DeviceModification deviceModInfo);

  public abstract CommonOperResponse switchDeviceOnOff(UUID deviceId,
      DeviceSwitchOnOff deviceModInfo);

  public abstract CommonOperResponse deleteDevice(UUID deviceId);
}
