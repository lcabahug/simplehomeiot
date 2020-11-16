package com.codingcuriosity.project.simplehomeiot.devices.service;

import com.codingcuriosity.project.simplehomeiot.common.AppLogger;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonAddResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonGetListResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonGetResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonOperResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.ErrorResult;
import com.codingcuriosity.project.simplehomeiot.common.model.ErrorType;
import com.codingcuriosity.project.simplehomeiot.devices.cvt.DeviceConverter;
import com.codingcuriosity.project.simplehomeiot.devices.model.CommonDeviceState;
import com.codingcuriosity.project.simplehomeiot.devices.model.DeviceInfo;
import com.codingcuriosity.project.simplehomeiot.devices.model.DeviceInfoAvailableControls;
import com.codingcuriosity.project.simplehomeiot.devices.model.DeviceModification;
import com.codingcuriosity.project.simplehomeiot.devices.model.DeviceRegistration;
import com.codingcuriosity.project.simplehomeiot.devices.model.DeviceSwitchOnOff;
import com.codingcuriosity.project.simplehomeiot.devices.repository.DeviceRepository;
import com.codingcuriosity.project.simplehomeiot.logs.repository.ControllerLogRepository;
import com.codingcuriosity.project.simplehomeiot.zones.model.ZoneInfo;
import com.codingcuriosity.project.simplehomeiot.zones.repository.ZoneRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class DeviceServiceImpl implements DeviceService {

  private final DeviceRepository deviceRepository;
  private final ZoneRepository zoneRepository;
  private final ControllerLogRepository logRepository;

  @Autowired
  public DeviceServiceImpl(DeviceRepository deviceRepository, ZoneRepository zoneRepository,
      ControllerLogRepository logRepository) {
    this.deviceRepository = deviceRepository;
    this.zoneRepository = zoneRepository;
    this.logRepository = logRepository;
  }

  @Override
  public ControllerLogRepository getLogRepo() {
    return this.logRepository;
  }

  private DeviceInfo findByIpv6Address(String address) {
    DeviceInfo foundDevice = null;

    Map<UUID, DeviceInfo> deviceMap = this.deviceRepository.findAll();
    if (deviceMap != null && !deviceMap.isEmpty()) {
      List<DeviceInfo> deviceInfoList = deviceMap.values().stream() //
          .filter(entry -> address.equals(entry.getAddress())) //
          .collect(Collectors.toList());

      // Only return the first element if found
      if (!deviceInfoList.isEmpty()) {
        foundDevice = deviceInfoList.get(0);
      }
    }

    String logMsg;
    String logDtls;
    if (foundDevice == null) {
      logMsg = "Device Not Found";
      logDtls = String.format("Cannot find Device with address = %s in the repository", address);
    } else {
      logMsg = "Request Successful";
      logDtls = String.format("DeviceInfo = %s", foundDevice.toString());
    }
    AppLogger.info(this, logMsg, logDtls);

    return foundDevice;
  }

  @Override
  public CommonGetListResponse<DeviceInfo> getDevices(Integer skip, Integer limit,
      Boolean includeRegistered, Boolean includeUnregistered) {
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;

    Map<UUID, DeviceInfo> deviceMap = this.deviceRepository.findAll();
    if (deviceMap == null || deviceMap.isEmpty()) {
      // Return OK but blank since this simply means the DB is empty
      String logMsg = "Empty list return";
      String logDtls = "No devices found in repository.";
      AppLogger.info(this, logMsg, logDtls);
      return new CommonGetListResponse<>(Collections.emptyList(), response, errResult);
    }

    // Extract Device Collection from Map
    Collection<DeviceInfo> deviceList = deviceMap.values();

    // Set 'includeRegistered' filter
    boolean excludeRegistered = false;
    Predicate<DeviceInfo> includeRegisteredCond = x -> true;
    if (includeRegistered != null && !includeRegistered) {
      excludeRegistered = true;
      includeRegisteredCond = device -> device.isIsRegistered() == false;
    }

    // Set 'includeUnregistered' filter
    boolean excludeUnregistered = false;
    Predicate<DeviceInfo> includeUnregisteredCond = x -> true;
    if (includeUnregistered != null && !includeUnregistered) {
      excludeUnregistered = true;
      includeUnregisteredCond = device -> device.isIsRegistered() == true;
    }

    // Return blank when isRegistered and isUnregistered filters conflict
    if (excludeRegistered && excludeUnregistered) {
      String logMsg = "Empty list return";
      String logDtls = "Parameter conflict (excludeRegistered and excludeUnregistered are false)";
      AppLogger.error(this, ErrorType.ERROR, logMsg, logDtls);
      return new CommonGetListResponse<>(Collections.emptyList(), response, errResult);
    }

    // Set 'skip' filter
    int offset = 0;
    if (skip != null) {
      if (skip < 0) {
        response = HttpStatus.BAD_REQUEST;
        return new CommonGetListResponse<>(Collections.emptyList(), response, errResult);
      } else if (skip >= deviceList.size()) {
        return new CommonGetListResponse<>(Collections.emptyList(), response, errResult);
      }
      offset = skip;
    }

    // Set 'limit' filter
    int limitVal = deviceList.size();
    if (limit != null) {
      if (limit < 0) {
        response = HttpStatus.BAD_REQUEST;
        return new CommonGetListResponse<>(Collections.emptyList(), response, errResult);
      }
      limitVal = limit;
    }

    // Sort List based on Name (Ascending) and apply filters
    List<DeviceInfo> sortedList = deviceList.stream() //
        .filter(includeRegisteredCond) //
        .filter(includeUnregisteredCond) //
        .sorted(Comparator.comparing(DeviceInfo::getName)) //
        .skip(offset) //
        .limit(limitVal) //
        .collect(Collectors.toCollection(ArrayList::new));

    String logMsg = "Request Successful";
    String logDtls = String.format("DeviceInfo list = %s", sortedList.toString());
    AppLogger.info(this, logMsg, logDtls);

    return new CommonGetListResponse<>(sortedList, response, errResult);
  }

  @Override
  public CommonGetResponse<DeviceInfo> getDeviceById(UUID deviceId) {
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;

    String logMsg;
    String logDtls;
    DeviceInfo devInfo = this.deviceRepository.findById(deviceId);
    if (devInfo == null) {
      response = HttpStatus.NOT_FOUND;
      logMsg = "Device Not Found";
      logDtls = String.format("Cannot find Device with deviceId = %s in the repository", deviceId);
    } else {
      logMsg = "Request Successful";
      logDtls = String.format("DeviceInfo = %s", devInfo.toString());
    }

    AppLogger.info(this, logMsg, logDtls);

    return new CommonGetResponse<>(devInfo, response, errResult);
  }

  @Override
  public CommonAddResponse registerDevice(DeviceRegistration deviceRegInfo) {
    HttpStatus response = HttpStatus.CREATED;
    ErrorResult errResult = null;
    UUID deviceId = null;

    // Check If device address belongs to a registered device
    String address = deviceRegInfo.getAddress();
    DeviceInfo deviceInfo = findByIpv6Address(address);
    if (deviceInfo == null) {
      response = HttpStatus.INTERNAL_SERVER_ERROR;
      String errMessage = "Cannot register device";
      String errDetails = "Only detected devices are allowed to be registered.";
      errResult = AppLogger.error(this, ErrorType.ERROR, errMessage, errDetails);
      return new CommonAddResponse(null, response, errResult);
    }

    String logMsg;
    String logDtls;
    if (deviceInfo.isIsRegistered()) {
      // Device is already managed by the controller; no need to re-register
      response = HttpStatus.CONFLICT;
      logMsg = "Cannot register device";
      logDtls = String.format("Device with deviceId = %s has already been registered.",
          deviceInfo.getDeviceId());
    } else {
      deviceInfo = DeviceConverter.createNewDeviceInfo(deviceInfo, deviceRegInfo);
      this.deviceRepository.add(deviceInfo);
      deviceId = deviceInfo.getDeviceId();

      logMsg = "Request Successful";
      logDtls = String.format("registered DeviceInfo = %s", deviceInfo.toString());
    }
    AppLogger.info(this, logMsg, logDtls);

    return new CommonAddResponse(deviceId, response, errResult);
  }

  @Override
  public CommonOperResponse modifyDevice(UUID deviceId, DeviceModification deviceModInfo) {
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;

    DeviceInfo deviceInfo = this.deviceRepository.findById(deviceId);
    if (deviceInfo == null) {
      response = HttpStatus.NOT_FOUND;
      String errMessage = "Cannot update device";
      String errDetails = "Device is not found in the repository.";
      AppLogger.error(this, ErrorType.ERROR, errMessage, errDetails);
      return new CommonOperResponse(response, errResult);
    }

    if (!deviceInfo.isIsRegistered()) {
      response = HttpStatus.INTERNAL_SERVER_ERROR;
      String errMsg = "Cannot update device";
      String errDetails = "Only registered devices are allowed to be modified.";
      errResult = AppLogger.error(this, ErrorType.ERROR, errMsg, errDetails);
      return new CommonOperResponse(response, errResult);
    }

    DeviceInfo modifiedDeviceInfo =
        DeviceConverter.getModifiedDeviceInfo(deviceInfo, deviceModInfo);
    this.deviceRepository.update(deviceId, modifiedDeviceInfo);

    String logMsg = "Request Successful";
    String logDtls = String.format("updated DeviceInfo = %s", deviceInfo.toString());
    AppLogger.info(this, logMsg, logDtls);

    return new CommonOperResponse(response, errResult);
  }

  @Override
  public CommonOperResponse switchDeviceOnOff(UUID deviceId, DeviceSwitchOnOff deviceModInfo) {
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;

    DeviceInfo deviceInfo = this.deviceRepository.findById(deviceId);
    if (deviceInfo == null) {
      response = HttpStatus.NOT_FOUND;
      return new CommonOperResponse(response, errResult);
    }

    CommonDeviceState newState = deviceModInfo.getState();
    if (!deviceInfo.isIsRegistered()) {
      response = HttpStatus.INTERNAL_SERVER_ERROR;
      String errMsg = String.format("Cannot switch device to %s", newState.toString());
      String errDetails = "Only registered devices are allowed to be operated upon.";
      errResult = AppLogger.error(this, ErrorType.ERROR, errMsg, errDetails);
      return new CommonOperResponse(response, errResult);
    }

    CommonDeviceState curState = deviceInfo.getState();
    if (curState.equals(newState)) {
      response = HttpStatus.INTERNAL_SERVER_ERROR;
      String errMsg = String.format("Cannot switch device to %s", newState.toString());
      String errDetails = String.format("Device is already %s", curState.toString());
      errResult = AppLogger.error(this, ErrorType.ERROR, errMsg, errDetails);
      return new CommonOperResponse(response, errResult);
    }

    DeviceInfo modifiedDeviceInfo =
        DeviceConverter.getModifiedDeviceInfo(deviceInfo, deviceModInfo);
    this.deviceRepository.update(deviceId, modifiedDeviceInfo);

    String logMsg = "Request Successful";
    String logDtls = String.format("updated DeviceInfo = %s", deviceInfo.toString());
    AppLogger.info(this, logMsg, logDtls);

    return new CommonOperResponse(response, errResult);
  }

  @Override
  public CommonOperResponse deleteDevice(UUID deviceId) {
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;

    DeviceInfo deviceInfo = this.deviceRepository.findById(deviceId);
    if (deviceInfo == null) {
      response = HttpStatus.NOT_FOUND;
      return new CommonOperResponse(response, errResult);
    }

    if (!deviceInfo.isIsRegistered()) {
      response = HttpStatus.INTERNAL_SERVER_ERROR;
      String errMsg = "Cannot delete device";
      String errDetails = "Only registered devices are allowed to be deleted.";
      errResult = AppLogger.error(this, ErrorType.ERROR, errMsg, errDetails);
      return new CommonOperResponse(response, errResult);
    }

    // Deletion can only occur if the device has been switched off
    if (deviceInfo.getState().equals(CommonDeviceState.ON)) {
      response = HttpStatus.INTERNAL_SERVER_ERROR;
      String errMessage = "Cannot delete device";
      String errDetails = "Device is switched on.";
      errResult = AppLogger.error(this, ErrorType.ERROR, errMessage, errDetails);
      return new CommonOperResponse(response, errResult);
    }

    // Device with a Registered Controller cannot be deleted
    List<DeviceInfoAvailableControls> availableControls = deviceInfo.getAvailableControls();
    if (availableControls != null && !availableControls.isEmpty()) {
      for (DeviceInfoAvailableControls availControl : availableControls) {
        if (availControl.isIsManaged()) {
          response = HttpStatus.INTERNAL_SERVER_ERROR;
          String errMessage = "Cannot delete device";
          String errDetails = "One or more controls in this device are registered.";
          errResult = AppLogger.error(this, ErrorType.ERROR, errMessage, errDetails);
          return new CommonOperResponse(response, errResult);
        }
      }
    }

    deviceInfo.setIsRegistered(false);
    deviceInfo.setIsGrouped(false);
    deviceInfo.setGroupId(null);
    this.deviceRepository.update(deviceId, deviceInfo);

    String logMsg = "Request Successful";
    String logDtls = String.format("Device with deviceId = %s has been deleted.", deviceId);
    AppLogger.info(this, logMsg, logDtls);

    // Also delete all of its instance in zones (if there are present)
    Map<UUID, ZoneInfo> zoneMap = this.zoneRepository.findAll();
    if (zoneMap != null && !zoneMap.isEmpty()) {
      for (ZoneInfo zoneInfo : zoneMap.values()) {
        if (zoneInfo == null) {
          continue;
        }
        List<UUID> devIdInZoneList = zoneInfo.getDeviceIds();
        if (devIdInZoneList == null) {
          continue;
        }
        if (devIdInZoneList.contains(deviceId)) {
          devIdInZoneList.remove(deviceId);
          this.zoneRepository.update(zoneInfo.getZoneId(), zoneInfo);
          continue;
        }
      }
    }

    return new CommonOperResponse(response, errResult);
  }

}
