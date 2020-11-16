package com.codingcuriosity.project.simplehomeiot.devices.api;

import com.codingcuriosity.project.simplehomeiot.common.Utils;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonAddResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonGetListResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonGetResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonOperResponse;
import com.codingcuriosity.project.simplehomeiot.devices.model.DeviceInfo;
import com.codingcuriosity.project.simplehomeiot.devices.model.DeviceModification;
import com.codingcuriosity.project.simplehomeiot.devices.model.DeviceRegistration;
import com.codingcuriosity.project.simplehomeiot.devices.model.DeviceRegistrationResponse;
import com.codingcuriosity.project.simplehomeiot.devices.model.DeviceSwitchOnOff;
import com.codingcuriosity.project.simplehomeiot.devices.service.DeviceService;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DevicesApiController implements DevicesApi {

  private final HttpServletRequest request;
  private final DeviceService deviceService;

  @Autowired
  public DevicesApiController(HttpServletRequest request, DeviceService deviceService) {
    this.request = request;
    this.deviceService = deviceService;
  }

  @Override
  public ResponseEntity<?> getDevices(@Valid Integer skip, @Valid Integer limit,
      @Valid Boolean includeRegistered, @Valid Boolean includeUnregistered) {
    if (Utils.isClientNoSupportJsonResponse(request)) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    CommonGetListResponse<DeviceInfo> getListResp =
        deviceService.getDevices(skip, limit, includeRegistered, includeUnregistered);
    if (getListResp.isError()) {
      if (getListResp.getErrorResult() == null) {
        return ResponseEntity.status(getListResp.getResponse()).build();
      } else {
        return ResponseEntity.status(getListResp.getResponse()).body(getListResp.getErrorResult());
      }
    }
    return ResponseEntity.ok().body(getListResp.getObjList());
  }

  @Override
  public ResponseEntity<?> registerDevices(@Valid DeviceRegistration body) {
    if (Utils.isClientNoSupportJsonResponse(request)) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    CommonAddResponse registerResponse = deviceService.registerDevice(body);
    if (registerResponse.getId() != null) {
      DeviceRegistrationResponse respBody =
          new DeviceRegistrationResponse().deviceId(registerResponse.getId());
      return ResponseEntity.status(HttpStatus.CREATED).body(respBody);
    } else if (registerResponse.getErrorResult() == null) {
      return ResponseEntity.status(registerResponse.getResponse()).build();
    } else {
      return ResponseEntity.status(registerResponse.getResponse())
          .body(registerResponse.getErrorResult());
    }
  }

  @Override
  public ResponseEntity<?> getDevice(UUID deviceId) {
    if (Utils.isClientNoSupportJsonResponse(request)) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    CommonGetResponse<DeviceInfo> getResp = deviceService.getDeviceById(deviceId);
    if (getResp.isError()) {
      if (getResp.getErrorResult() != null) {
        return ResponseEntity.status(getResp.getResponse()).body(getResp.getErrorResult());
      } else {
        return ResponseEntity.status(getResp.getResponse()).build();
      }
    }

    DeviceInfo deviceInfo = getResp.getObj();
    return ResponseEntity.ok().body(deviceInfo);
  }

  @Override
  public ResponseEntity<?> modifyDevice(UUID deviceId, @Valid DeviceModification body) {
    if (Utils.isClientNoSupportJsonResponse(request)) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    CommonOperResponse updateResponse = deviceService.modifyDevice(deviceId, body);
    if (updateResponse.getErrorResult() == null) {
      return ResponseEntity.status(updateResponse.getResponse()).build();
    } else {
      return ResponseEntity.status(updateResponse.getResponse())
          .body(updateResponse.getErrorResult());
    }
  }

  @Override
  public ResponseEntity<?> switchOnOffDevice(UUID deviceId, @Valid DeviceSwitchOnOff body) {
    if (Utils.isClientNoSupportJsonResponse(request)) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    CommonOperResponse patchResponse = deviceService.switchDeviceOnOff(deviceId, body);
    if (patchResponse.getErrorResult() == null) {
      return ResponseEntity.status(patchResponse.getResponse()).build();
    } else {
      return ResponseEntity.status(patchResponse.getResponse())
          .body(patchResponse.getErrorResult());
    }
  }

  @Override
  public ResponseEntity<?> deleteDevice(UUID deviceId) {
    if (Utils.isClientNoSupportJsonResponse(request)) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    CommonOperResponse deleteResponse = deviceService.deleteDevice(deviceId);
    if (deleteResponse.getErrorResult() == null) {
      return ResponseEntity.status(deleteResponse.getResponse()).build();
    } else {
      return ResponseEntity.status(deleteResponse.getResponse())
          .body(deleteResponse.getErrorResult());
    }
  }

}
