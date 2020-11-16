package com.codingcuriosity.project.simplehomeiot.logs.api;

import com.codingcuriosity.project.simplehomeiot.common.Utils;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonGetListResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonOperResponse;
import com.codingcuriosity.project.simplehomeiot.logs.model.ControllerLogInfo;
import com.codingcuriosity.project.simplehomeiot.logs.model.DeviceLogInfo;
import com.codingcuriosity.project.simplehomeiot.logs.service.ControllerLogService;
import com.codingcuriosity.project.simplehomeiot.logs.service.DeviceLogService;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogsApiController implements LogsApi {

  private final HttpServletRequest request;
  private final ControllerLogService controllerLogService;
  private final DeviceLogService deviceLogService;

  @Autowired
  public LogsApiController(HttpServletRequest request, ControllerLogService controllerLogService,
      DeviceLogService deviceLogService) {
    this.request = request;
    this.deviceLogService = deviceLogService;
    this.controllerLogService = controllerLogService;
  }

  @Override
  public ResponseEntity<?> getControllerLogs(@Valid Integer skip, @Valid Integer limit) {
    if (Utils.isClientNoSupportJsonResponse(request)) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    CommonGetListResponse<ControllerLogInfo> getListResp =
        controllerLogService.getLogInfo(skip, limit);
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
  public ResponseEntity<?> deleteControllerLogs() {
    if (Utils.isClientNoSupportJsonResponse(request)) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    CommonOperResponse deleteResponse = controllerLogService.deleteLogs();
    if (deleteResponse.getErrorResult() == null) {
      return ResponseEntity.status(deleteResponse.getResponse()).build();
    } else {
      return ResponseEntity.status(deleteResponse.getResponse())
          .body(deleteResponse.getErrorResult());
    }
  }

  @Override
  public ResponseEntity<?> getAllDevicesLogs(@Valid Integer skip, @Valid Integer limit) {
    if (Utils.isClientNoSupportJsonResponse(request)) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    CommonGetListResponse<DeviceLogInfo> getListResp = deviceLogService.getLogInfo(skip, limit);
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
  public ResponseEntity<?> deleteDevicesLogs() {
    if (Utils.isClientNoSupportJsonResponse(request)) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    CommonOperResponse deleteResponse = deviceLogService.deleteAllLogs();
    if (deleteResponse.getErrorResult() == null) {
      return ResponseEntity.status(deleteResponse.getResponse()).build();
    } else {
      return ResponseEntity.status(deleteResponse.getResponse())
          .body(deleteResponse.getErrorResult());
    }
  }

  @Override
  public ResponseEntity<?> getDeviceLogs(UUID deviceId, @Valid Integer skip, @Valid Integer limit) {
    if (Utils.isClientNoSupportJsonResponse(request)) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    CommonGetListResponse<DeviceLogInfo> getListResp =
        deviceLogService.getFilteredLogInfo(deviceId, skip, limit);
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
  public ResponseEntity<?> deleteDeviceLogs(UUID deviceId) {
    if (Utils.isClientNoSupportJsonResponse(request)) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    CommonOperResponse deleteResponse = deviceLogService.deleteSpecificLogs(deviceId);
    if (deleteResponse.getErrorResult() == null) {
      return ResponseEntity.status(deleteResponse.getResponse()).build();
    } else {
      return ResponseEntity.status(deleteResponse.getResponse())
          .body(deleteResponse.getErrorResult());
    }
  }
}
