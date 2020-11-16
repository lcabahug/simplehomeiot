package com.codingcuriosity.project.simplehomeiot.controls.api;

import com.codingcuriosity.project.simplehomeiot.common.Utils;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonAddResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonGetListResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonOperResponse;
import com.codingcuriosity.project.simplehomeiot.controls.model.ControlModification;
import com.codingcuriosity.project.simplehomeiot.controls.model.ControlRegistration;
import com.codingcuriosity.project.simplehomeiot.controls.model.ControlRegistrationResponse;
import com.codingcuriosity.project.simplehomeiot.controls.model.DeviceControlInfo;
import com.codingcuriosity.project.simplehomeiot.controls.model.DirectControlValue;
import com.codingcuriosity.project.simplehomeiot.controls.model.SliderControlValue;
import com.codingcuriosity.project.simplehomeiot.controls.service.ControlService;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ControlsApiController implements ControlsApi {

  private final HttpServletRequest request;
  private final ControlService controlService;

  @Autowired
  public ControlsApiController(HttpServletRequest request, ControlService controlService) {
    this.request = request;
    this.controlService = controlService;
  }

  @Override
  public ResponseEntity<?> getDevicesControls() {
    if (Utils.isClientNoSupportJsonResponse(request)) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    CommonGetListResponse<DeviceControlInfo> getListResp = controlService.getDeviceControls();
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
  public ResponseEntity<?> getDeviceControls(UUID deviceId) {
    if (Utils.isClientNoSupportJsonResponse(request)) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    CommonGetListResponse<DeviceControlInfo> getListResp =
        controlService.getFilteredDeviceControls(deviceId);
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
  public ResponseEntity<?> registerDevicesControls(UUID deviceId, @Valid ControlRegistration body) {
    if (Utils.isClientNoSupportJsonResponse(request)) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    CommonAddResponse registerResponse = controlService.registerControl(deviceId, body);
    if (registerResponse.getId() != null) {
      ControlRegistrationResponse respBody =
          new ControlRegistrationResponse().controlId(registerResponse.getId());
      return ResponseEntity.status(HttpStatus.CREATED).body(respBody);
    } else if (registerResponse.getErrorResult() == null) {
      return ResponseEntity.status(registerResponse.getResponse()).build();
    } else {
      return ResponseEntity.status(registerResponse.getResponse())
          .body(registerResponse.getErrorResult());
    }
  }

  @Override
  public ResponseEntity<?> setDirectControl(UUID controlId, @Valid DirectControlValue body) {
    if (Utils.isClientNoSupportJsonResponse(request)) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    CommonOperResponse operResponse = controlService.setDirectControl(controlId, body);
    if (operResponse.getErrorResult() == null) {
      return ResponseEntity.status(operResponse.getResponse()).build();
    } else {
      return ResponseEntity.status(operResponse.getResponse()).body(operResponse.getErrorResult());
    }
  }

  @Override
  public ResponseEntity<?> setSliderControl(UUID controlId, @Valid SliderControlValue body) {
    if (Utils.isClientNoSupportJsonResponse(request)) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    CommonOperResponse operResponse = controlService.setSliderControl(controlId, body);
    if (operResponse.getErrorResult() == null) {
      return ResponseEntity.status(operResponse.getResponse()).build();
    } else {
      return ResponseEntity.status(operResponse.getResponse()).body(operResponse.getErrorResult());
    }
  }

  @Override
  public ResponseEntity<?> setToggleControl(UUID controlId) {
    if (Utils.isClientNoSupportJsonResponse(request)) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    CommonOperResponse operResponse = controlService.setToggleControl(controlId);
    if (operResponse.getErrorResult() == null) {
      return ResponseEntity.status(operResponse.getResponse()).build();
    } else {
      return ResponseEntity.status(operResponse.getResponse()).body(operResponse.getErrorResult());
    }
  }

  @Override
  public ResponseEntity<?> modifyDeviceControls(UUID controlId, @Valid ControlModification body) {
    if (Utils.isClientNoSupportJsonResponse(request)) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    CommonOperResponse updateResponse = controlService.modifyControl(controlId, body);
    if (updateResponse.getErrorResult() == null) {
      return ResponseEntity.status(updateResponse.getResponse()).build();
    } else {
      return ResponseEntity.status(updateResponse.getResponse())
          .body(updateResponse.getErrorResult());
    }
  }

  @Override
  public ResponseEntity<?> deleteControl(UUID controlId) {
    if (Utils.isClientNoSupportJsonResponse(request)) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    CommonOperResponse deleteResponse = controlService.deleteControl(controlId);
    if (deleteResponse.getErrorResult() == null) {
      return ResponseEntity.status(deleteResponse.getResponse()).build();
    } else {
      return ResponseEntity.status(deleteResponse.getResponse())
          .body(deleteResponse.getErrorResult());
    }
  }

}
