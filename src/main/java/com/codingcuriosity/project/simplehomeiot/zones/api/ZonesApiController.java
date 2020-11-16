package com.codingcuriosity.project.simplehomeiot.zones.api;

import com.codingcuriosity.project.simplehomeiot.common.Utils;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonAddResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonGetListResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonGetResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonOperResponse;
import com.codingcuriosity.project.simplehomeiot.zones.model.ZoneCreation;
import com.codingcuriosity.project.simplehomeiot.zones.model.ZoneInfo;
import com.codingcuriosity.project.simplehomeiot.zones.model.ZoneModification;
import com.codingcuriosity.project.simplehomeiot.zones.model.ZoneRegistrationResponse;
import com.codingcuriosity.project.simplehomeiot.zones.model.ZoneSetDevice;
import com.codingcuriosity.project.simplehomeiot.zones.service.ZoneService;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ZonesApiController implements ZonesApi {

  private final HttpServletRequest request;
  private final ZoneService zoneService;

  @Autowired
  public ZonesApiController(HttpServletRequest request, ZoneService zoneService) {
    this.request = request;
    this.zoneService = zoneService;
  }

  @Override
  public ResponseEntity<?> getZones(@Valid Integer skip, @Valid Integer limit) {
    if (Utils.isClientNoSupportJsonResponse(request)) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    CommonGetListResponse<ZoneInfo> getListResp = zoneService.getZones(skip, limit);
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
  public ResponseEntity<?> createZone(@Valid ZoneCreation body) {
    if (Utils.isClientNoSupportJsonResponse(request)) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    CommonAddResponse registerResponse = zoneService.registerZone(body);
    if (registerResponse.getId() != null) {
      ZoneRegistrationResponse respBody =
          new ZoneRegistrationResponse().zoneId(registerResponse.getId());
      return ResponseEntity.status(HttpStatus.CREATED).body(respBody);
    } else if (registerResponse.getErrorResult() == null) {
      return ResponseEntity.status(registerResponse.getResponse()).build();
    } else {
      return ResponseEntity.status(registerResponse.getResponse())
          .body(registerResponse.getErrorResult());
    }
  }

  @Override
  public ResponseEntity<?> getZone(UUID zoneId) {
    if (Utils.isClientNoSupportJsonResponse(request)) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    CommonGetResponse<ZoneInfo> getResp = zoneService.getZoneById(zoneId);
    if (getResp.isError()) {
      if (getResp.getErrorResult() != null) {
        return ResponseEntity.status(getResp.getResponse()).body(getResp.getErrorResult());
      } else {
        return ResponseEntity.status(getResp.getResponse()).build();
      }
    }

    ZoneInfo timerInfo = getResp.getObj();
    return ResponseEntity.ok().body(timerInfo);
  }

  @Override
  public ResponseEntity<?> modifyZone(UUID zoneId, @Valid ZoneModification body) {
    if (Utils.isClientNoSupportJsonResponse(request)) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    CommonOperResponse updateResponse = zoneService.modifyZone(zoneId, body);
    if (updateResponse.getErrorResult() == null) {
      return ResponseEntity.status(updateResponse.getResponse()).build();
    } else {
      return ResponseEntity.status(updateResponse.getResponse())
          .body(updateResponse.getErrorResult());
    }
  }

  @Override
  public ResponseEntity<?> addDeviceToZone(UUID zoneId, @Valid ZoneSetDevice body) {
    if (Utils.isClientNoSupportJsonResponse(request)) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    CommonOperResponse patchResponse = zoneService.setDevicesToZone(zoneId, body);
    if (patchResponse.getErrorResult() == null) {
      return ResponseEntity.status(patchResponse.getResponse()).build();
    } else {
      return ResponseEntity.status(patchResponse.getResponse())
          .body(patchResponse.getErrorResult());
    }
  }

  @Override
  public ResponseEntity<?> deleteZone(UUID zoneId) {
    if (Utils.isClientNoSupportJsonResponse(request)) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    CommonOperResponse deleteResponse = zoneService.deleteZone(zoneId);
    if (deleteResponse.getErrorResult() == null) {
      return ResponseEntity.status(deleteResponse.getResponse()).build();
    } else {
      return ResponseEntity.status(deleteResponse.getResponse())
          .body(deleteResponse.getErrorResult());
    }
  }

}
