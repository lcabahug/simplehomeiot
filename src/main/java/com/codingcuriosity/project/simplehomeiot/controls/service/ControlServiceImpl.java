package com.codingcuriosity.project.simplehomeiot.controls.service;

import com.codingcuriosity.project.simplehomeiot.common.AppLogger;
import com.codingcuriosity.project.simplehomeiot.common.Utils;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonAddResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonGetListResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonOperResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.ErrorResult;
import com.codingcuriosity.project.simplehomeiot.common.model.ErrorType;
import com.codingcuriosity.project.simplehomeiot.controls.cvt.ControlConverter;
import com.codingcuriosity.project.simplehomeiot.controls.model.ControlModification;
import com.codingcuriosity.project.simplehomeiot.controls.model.ControlRegistration;
import com.codingcuriosity.project.simplehomeiot.controls.model.DeviceControlInfo;
import com.codingcuriosity.project.simplehomeiot.controls.model.DirectControlValue;
import com.codingcuriosity.project.simplehomeiot.controls.model.SliderControlValue;
import com.codingcuriosity.project.simplehomeiot.controls.repository.ControlRepository;
import com.codingcuriosity.project.simplehomeiot.controls.southbound.ControlOperation;
import com.codingcuriosity.project.simplehomeiot.controls.southbound.ControlSoloOperation;
import com.codingcuriosity.project.simplehomeiot.devices.model.DeviceInfo;
import com.codingcuriosity.project.simplehomeiot.devices.model.DeviceInfoAvailableControls;
import com.codingcuriosity.project.simplehomeiot.devices.repository.DeviceRepository;
import com.codingcuriosity.project.simplehomeiot.logs.repository.ControllerLogRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ControlServiceImpl implements ControlService {

  private final ControlRepository controlRepository;
  private final ControllerLogRepository logRepository;
  private final DeviceRepository deviceRepository;
  private final ControlOperation controlOperation;

  @Autowired
  public ControlServiceImpl(ControlRepository controlRepository,
      ControllerLogRepository logRepository, DeviceRepository deviceRepository,
      ControlSoloOperation controlOperation) {
    this.controlRepository = controlRepository;
    this.logRepository = logRepository;
    this.deviceRepository = deviceRepository;
    this.controlOperation = controlOperation;
  }

  @Override
  public ControllerLogRepository getLogRepo() {
    return this.logRepository;
  }

  private CommonGetListResponse<DeviceControlInfo> commonGetDeviceControls(
      Predicate<DeviceControlInfo> controlFilter) {
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;

    Map<UUID, DeviceControlInfo> controlMap = this.controlRepository.findAll();
    if (controlMap == null || controlMap.isEmpty()) {
      return new CommonGetListResponse<>(Collections.emptyList(), response, errResult);
    }

    // Set Filter
    Predicate<DeviceControlInfo> entryFilter;
    if (controlFilter != null) {
      entryFilter = controlFilter;
    } else {
      entryFilter = log -> true;
    }

    // Extract Control List from the Map (apply filter if provided)
    List<DeviceControlInfo> deviceControlList = controlMap.values().stream() //
        .filter(entryFilter) //
        .collect(Collectors.toCollection(ArrayList::new));

    return new CommonGetListResponse<>(deviceControlList, response, errResult);
  }

  private DeviceControlInfo findByNameAndDeviceId(String name, UUID deviceId) {
    Map<UUID, DeviceControlInfo> controlMap = this.controlRepository.findAll();
    if (controlMap == null || controlMap.isEmpty()) {
      // DB is empty / no entries
      return null;
    }

    // Lookup the specific name from the map
    Optional<DeviceControlInfo> matchingEntry = controlMap.values().stream() //
        .filter(Objects::nonNull) //
        .filter(controlInfo -> (deviceId.equals(controlInfo.getDeviceId()))
            && (name.equals(controlInfo.getName()))) //
        .findFirst();

    return matchingEntry.orElse(null);
  }

  @Override
  public CommonGetListResponse<DeviceControlInfo> getDeviceControls() {
    return commonGetDeviceControls(null);
  }

  @Override
  public CommonGetListResponse<DeviceControlInfo> getFilteredDeviceControls(UUID deviceId) {
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    if (deviceId == null) {
      response = HttpStatus.BAD_REQUEST;
      return new CommonGetListResponse<>(Collections.emptyList(), response, errResult);
    }

    // Check if deviceId exists and is registered
    DeviceInfo deviceInfo = this.deviceRepository.findById(deviceId);
    if (deviceInfo == null) {
      response = HttpStatus.NOT_FOUND;
      return new CommonGetListResponse<>(Collections.emptyList(), response, errResult);
    } else if (!deviceInfo.isIsRegistered()) {
      response = HttpStatus.INTERNAL_SERVER_ERROR;
      String errMsg = "Cannot list controls";
      String errDetails = "This action is not possible for an unregistered device.";
      errResult = AppLogger.error(this, ErrorType.ERROR, errMsg, errDetails);
      return new CommonGetListResponse<>(Collections.emptyList(), response, errResult);
    }

    return commonGetDeviceControls(devctrInfo -> deviceId.equals(devctrInfo.getDeviceId()));
  }

  @Override
  public CommonAddResponse registerControl(UUID deviceId, ControlRegistration controlRegistration) {
    HttpStatus response = HttpStatus.CREATED;
    ErrorResult errResult = null;
    UUID controlId = null;

    // Check if deviceId exists and is registered
    DeviceInfo deviceInfo = this.deviceRepository.findById(deviceId);
    if (deviceInfo == null) {
      response = HttpStatus.NOT_FOUND;
      return new CommonAddResponse(controlId, response, errResult);
    } else if (!deviceInfo.isIsRegistered()) {
      response = HttpStatus.INTERNAL_SERVER_ERROR;
      String errMsg = "Cannot register control";
      String errDetails = String.format("Device %s is unregistered.", deviceId);
      errResult = AppLogger.error(this, ErrorType.ERROR, errMsg, errDetails);
      return new CommonAddResponse(controlId, response, errResult);
    }

    // Check the available controls for the specified device
    List<DeviceInfoAvailableControls> availableControls = deviceInfo.getAvailableControls();
    if (availableControls == null || availableControls.isEmpty()) {
      response = HttpStatus.INTERNAL_SERVER_ERROR;
      String errMsg = "Cannot register control";
      String errDetails = String.format("Device %s has no available controls.", deviceId);
      errResult = AppLogger.error(this, ErrorType.ERROR, errMsg, errDetails);
      return new CommonAddResponse(controlId, response, errResult);
    }

    // Look up control name from the device's available controls
    String nameToReg = controlRegistration.getName();
    DeviceInfoAvailableControls matchingControl = null;
    boolean isMarkedRegistered = false;
    for (DeviceInfoAvailableControls availableControl : availableControls) {
      if (nameToReg.equals(availableControl.getName())) {
        matchingControl = availableControl;
        isMarkedRegistered = availableControl.isIsManaged();
        break;
      }
    }
    if (matchingControl == null) {
      response = HttpStatus.INTERNAL_SERVER_ERROR;
      String errMsg = "Cannot register control";
      String errDetails = String.format("Control %s is not inside Device %s available controls.",
          nameToReg, deviceId);
      errResult = AppLogger.error(this, ErrorType.ERROR, errMsg, errDetails);
      return new CommonAddResponse(controlId, response, errResult);
    } else if (isMarkedRegistered) {
      response = HttpStatus.CONFLICT;
      return new CommonAddResponse(controlId, response, errResult);
    }

    DeviceControlInfo existingControlInfo = findByNameAndDeviceId(nameToReg, deviceId);
    if (existingControlInfo != null) {
      response = HttpStatus.CONFLICT;
      return new CommonAddResponse(controlId, response, errResult);
    }

    // Generate Id for the new entry
    controlId = Utils.generateId(id -> {
      DeviceControlInfo existingEntry = this.controlRepository.findById(id);
      return (existingEntry != null);
    });

    // Create a new entry
    DeviceControlInfo controlInfo = new DeviceControlInfo();
    controlInfo.setControlId(controlId);
    controlInfo.setDeviceId(deviceId);
    controlInfo.setCurrentValue(matchingControl.getValue());
    controlInfo.setName(matchingControl.getName());

    // Get the updated fields and set to the new entry
    String newDesc = controlRegistration.getDescription();
    controlInfo.setDescription(newDesc);

    this.controlRepository.add(controlInfo);

    // Reflect the settings to the device's available controls
    matchingControl.setIsManaged(true);
    matchingControl.setDescription(newDesc);
    this.deviceRepository.update(deviceId, deviceInfo);

    return new CommonAddResponse(controlId, response, errResult);
  }

  @Override
  public CommonOperResponse setDirectControl(UUID controlId, DirectControlValue directControl) {
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;

    DeviceControlInfo controlInfo = this.controlRepository.findById(controlId);
    if (controlInfo == null) {
      response = HttpStatus.NOT_FOUND;
      return new CommonOperResponse(response, errResult);
    }

    UUID refDeviceId = controlInfo.getDeviceId();
    if (refDeviceId == null) {
      response = HttpStatus.INTERNAL_SERVER_ERROR;
      String errMsg = "Cannot perform operation on control";
      String errDetails = "There is no deviceId associated with this control.";
      errResult = AppLogger.error(this, ErrorType.EXCEPTION, errMsg, errDetails);
      return new CommonOperResponse(response, errResult);
    }

    DeviceInfo refDeviceInfo = this.deviceRepository.findById(refDeviceId);
    if (refDeviceInfo == null) {
      response = HttpStatus.INTERNAL_SERVER_ERROR;
      String errMsg = "Cannot perform operation on control";
      String errDetails = "The device associated with this control does not exist.";
      errResult = AppLogger.error(this, ErrorType.EXCEPTION, errMsg, errDetails);
      return new CommonOperResponse(response, errResult);
    }

    if (!refDeviceInfo.isIsRegistered()) {
      response = HttpStatus.INTERNAL_SERVER_ERROR;
      String errMsg = "Cannot perform operation on control";
      String errDetails = "The device associated with this control is not registered.";
      errResult = AppLogger.error(this, ErrorType.EXCEPTION, errMsg, errDetails);
      return new CommonOperResponse(response, errResult);
    }

    DeviceControlInfo modifiedControlInfo =
        ControlConverter.getModifiedControlInfo(controlInfo, directControl);

    this.controlRepository.update(controlId, modifiedControlInfo);

    this.controlOperation.setDirect(controlId, directControl);

    return new CommonOperResponse(response, errResult);
  }

  @Override
  public CommonOperResponse setSliderControl(UUID controlId, SliderControlValue sliderControl) {
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;

    DeviceControlInfo controlInfo = this.controlRepository.findById(controlId);
    if (controlInfo == null) {
      response = HttpStatus.NOT_FOUND;
      return new CommonOperResponse(response, errResult);
    }

    UUID refDeviceId = controlInfo.getDeviceId();
    if (refDeviceId == null) {
      response = HttpStatus.INTERNAL_SERVER_ERROR;
      String errMsg = "Cannot perform operation on control";
      String errDetails = "There is no deviceId associated with this control.";
      errResult = AppLogger.error(this, ErrorType.EXCEPTION, errMsg, errDetails);
      return new CommonOperResponse(response, errResult);
    }

    DeviceInfo refDeviceInfo = this.deviceRepository.findById(refDeviceId);
    if (refDeviceInfo == null) {
      response = HttpStatus.INTERNAL_SERVER_ERROR;
      String errMsg = "Cannot perform operation on control";
      String errDetails = "The device associated with this control does not exist.";
      errResult = AppLogger.error(this, ErrorType.EXCEPTION, errMsg, errDetails);
      return new CommonOperResponse(response, errResult);
    }

    if (!refDeviceInfo.isIsRegistered()) {
      response = HttpStatus.INTERNAL_SERVER_ERROR;
      String errMsg = "Cannot perform operation on control";
      String errDetails = "The device associated with this control is not registered.";
      errResult = AppLogger.error(this, ErrorType.EXCEPTION, errMsg, errDetails);
      return new CommonOperResponse(response, errResult);
    }

    this.controlOperation.setSlider(controlId, sliderControl);

    return new CommonOperResponse(response, errResult);
  }

  @Override
  public CommonOperResponse setToggleControl(UUID controlId) {
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;

    DeviceControlInfo controlInfo = this.controlRepository.findById(controlId);
    if (controlInfo == null) {
      response = HttpStatus.NOT_FOUND;
      return new CommonOperResponse(response, errResult);
    }

    UUID refDeviceId = controlInfo.getDeviceId();
    if (refDeviceId == null) {
      response = HttpStatus.INTERNAL_SERVER_ERROR;
      String errMsg = "Cannot perform operation on control";
      String errDetails = "There is no deviceId associated with this control.";
      errResult = AppLogger.error(this, ErrorType.EXCEPTION, errMsg, errDetails);
      return new CommonOperResponse(response, errResult);
    }

    DeviceInfo refDeviceInfo = this.deviceRepository.findById(refDeviceId);
    if (refDeviceInfo == null) {
      response = HttpStatus.INTERNAL_SERVER_ERROR;
      String errMsg = "Cannot perform operation on control";
      String errDetails = "The device associated with this control does not exist.";
      errResult = AppLogger.error(this, ErrorType.EXCEPTION, errMsg, errDetails);
      return new CommonOperResponse(response, errResult);
    }

    if (!refDeviceInfo.isIsRegistered()) {
      response = HttpStatus.INTERNAL_SERVER_ERROR;
      String errMsg = "Cannot perform operation on control";
      String errDetails = "The device associated with this control is not registered.";
      errResult = AppLogger.error(this, ErrorType.EXCEPTION, errMsg, errDetails);
      return new CommonOperResponse(response, errResult);
    }

    this.controlOperation.setToggle(controlId);

    return new CommonOperResponse(response, errResult);
  }

  @Override
  public CommonOperResponse modifyControl(UUID controlId, ControlModification controlModification) {
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;

    DeviceControlInfo controlInfo = this.controlRepository.findById(controlId);
    if (controlInfo == null) {
      response = HttpStatus.NOT_FOUND;
      return new CommonOperResponse(response, errResult);
    }

    UUID refDeviceId = controlInfo.getDeviceId();
    if (refDeviceId == null) {
      response = HttpStatus.INTERNAL_SERVER_ERROR;
      String errMsg = "Cannot modify control";
      String errDetails = "There is no deviceId associated with this control.";
      errResult = AppLogger.error(this, ErrorType.EXCEPTION, errMsg, errDetails);
      return new CommonOperResponse(response, errResult);
    }

    DeviceInfo refDeviceInfo = this.deviceRepository.findById(refDeviceId);
    if (refDeviceInfo == null) {
      response = HttpStatus.INTERNAL_SERVER_ERROR;
      String errMsg = "Cannot modify control";
      String errDetails = "The device associated with this control does not exist.";
      errResult = AppLogger.error(this, ErrorType.EXCEPTION, errMsg, errDetails);
      return new CommonOperResponse(response, errResult);
    }

    if (!refDeviceInfo.isIsRegistered()) {
      response = HttpStatus.INTERNAL_SERVER_ERROR;
      String errMsg = "Cannot modify control";
      String errDetails = "The device associated with this control is not registered.";
      errResult = AppLogger.error(this, ErrorType.EXCEPTION, errMsg, errDetails);
      return new CommonOperResponse(response, errResult);
    }

    // Check the available controls for the specified device
    List<DeviceInfoAvailableControls> availableControls = refDeviceInfo.getAvailableControls();
    if (availableControls == null || availableControls.isEmpty()) {
      response = HttpStatus.INTERNAL_SERVER_ERROR;
      String errMsg = "Cannot modify control";
      String errDetails = String.format("Device %s has no available controls.", refDeviceId);
      errResult = AppLogger.error(this, ErrorType.ERROR, errMsg, errDetails);
      return new CommonOperResponse(response, errResult);
    }

    // Look up control name from the device's available controls
    String nameToReg = controlInfo.getName();
    DeviceInfoAvailableControls matchingControl = null;
    for (DeviceInfoAvailableControls availableControl : availableControls) {
      if (nameToReg.equals(availableControl.getName())) {
        matchingControl = availableControl;
        break;
      }
    }
    if (matchingControl == null) {
      response = HttpStatus.INTERNAL_SERVER_ERROR;
      String errMsg = "Cannot modify control";
      String errDetails = String.format("Control %s is not inside Device %s available controls.",
          nameToReg, refDeviceId);
      errResult = AppLogger.error(this, ErrorType.ERROR, errMsg, errDetails);
      return new CommonOperResponse(response, errResult);
    } else if (!matchingControl.isIsManaged()) {
      response = HttpStatus.INTERNAL_SERVER_ERROR;
      String errMsg = "Cannot modify control";
      String errDetails = String.format("Control %s is not registered.", nameToReg);
      errResult = AppLogger.error(this, ErrorType.ERROR, errMsg, errDetails);
      return new CommonOperResponse(response, errResult);
    }

    // Update the existing entry with the new field(s)
    String newDesc = controlModification.getDescription();
    controlInfo.setDescription(newDesc);

    this.controlRepository.update(controlId, controlInfo);

    // Reflect the settings to the device's available controls
    matchingControl.setIsManaged(true);
    matchingControl.setDescription(newDesc);
    this.deviceRepository.update(refDeviceId, refDeviceInfo);

    return new CommonOperResponse(response, errResult);
  }

  @Override
  public CommonOperResponse deleteControl(UUID controlId) {
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;

    DeviceControlInfo controlInfo = this.controlRepository.findById(controlId);
    if (controlInfo == null) {
      response = HttpStatus.NOT_FOUND;
      return new CommonOperResponse(response, errResult);
    }

    UUID refDeviceId = controlInfo.getDeviceId();
    if (refDeviceId == null) {
      response = HttpStatus.INTERNAL_SERVER_ERROR;
      String errMsg = "Cannot delete control";
      String errDetails = "There is no deviceId associated with this control.";
      errResult = AppLogger.error(this, ErrorType.EXCEPTION, errMsg, errDetails);
      return new CommonOperResponse(response, errResult);
    }

    DeviceInfo refDeviceInfo = this.deviceRepository.findById(refDeviceId);
    if (refDeviceInfo == null) {
      response = HttpStatus.INTERNAL_SERVER_ERROR;
      String errMsg = "Cannot delete control";
      String errDetails = "The device associated with this control does not exist.";
      errResult = AppLogger.error(this, ErrorType.EXCEPTION, errMsg, errDetails);
      return new CommonOperResponse(response, errResult);
    }

    if (!refDeviceInfo.isIsRegistered()) {
      response = HttpStatus.INTERNAL_SERVER_ERROR;
      String errMsg = "Cannot delete control";
      String errDetails = "The device associated with this control is not registered.";
      errResult = AppLogger.error(this, ErrorType.EXCEPTION, errMsg, errDetails);
      return new CommonOperResponse(response, errResult);
    }

    // Check the available controls for the specified device
    List<DeviceInfoAvailableControls> availableControls = refDeviceInfo.getAvailableControls();
    if (availableControls == null || availableControls.isEmpty()) {
      response = HttpStatus.INTERNAL_SERVER_ERROR;
      String errMsg = "Cannot delete control";
      String errDetails = String.format("Device %s has no available controls.", refDeviceId);
      errResult = AppLogger.error(this, ErrorType.ERROR, errMsg, errDetails);
      return new CommonOperResponse(response, errResult);
    }

    // Look up control name from the device's available controls
    String nameToReg = controlInfo.getName();
    DeviceInfoAvailableControls matchingControl = null;
    for (DeviceInfoAvailableControls availableControl : availableControls) {
      if (nameToReg.equals(availableControl.getName())) {
        matchingControl = availableControl;
        break;
      }
    }
    if (matchingControl == null) {
      response = HttpStatus.INTERNAL_SERVER_ERROR;
      String errMsg = "Cannot delete control";
      String errDetails = String.format("Control %s is not inside Device %s available controls.",
          nameToReg, refDeviceId);
      errResult = AppLogger.error(this, ErrorType.ERROR, errMsg, errDetails);
      return new CommonOperResponse(response, errResult);
    }

    // Reflect the settings to the device's available controls
    matchingControl.setIsManaged(false);
    this.deviceRepository.update(refDeviceId, refDeviceInfo);

    this.controlRepository.delete(controlId);

    return new CommonOperResponse(response, errResult);
  }

}
