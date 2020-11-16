package com.codingcuriosity.project.simplehomeiot.groups.api;

import com.codingcuriosity.project.simplehomeiot.common.Utils;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonAddResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonGetListResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonGetResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonOperResponse;
import com.codingcuriosity.project.simplehomeiot.groups.model.EnableDisableGroup;
import com.codingcuriosity.project.simplehomeiot.groups.model.GroupInfo;
import com.codingcuriosity.project.simplehomeiot.groups.model.GroupModification;
import com.codingcuriosity.project.simplehomeiot.groups.model.GroupRegistration;
import com.codingcuriosity.project.simplehomeiot.groups.model.GroupRegistrationResponse;
import com.codingcuriosity.project.simplehomeiot.groups.service.GroupService;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GroupsApiController implements GroupsApi {

  private final HttpServletRequest request;
  private final GroupService groupService;

  @Autowired
  public GroupsApiController(HttpServletRequest request, GroupService groupService) {
    this.request = request;
    this.groupService = groupService;
  }

  @Override
  public ResponseEntity<?> getGroups(@Valid Integer skip, @Valid Integer limit) {
    if (Utils.isClientNoSupportJsonResponse(request)) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    CommonGetListResponse<GroupInfo> getListResp = groupService.getGroups(skip, limit);
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
  public ResponseEntity<?> registerGroup(@Valid GroupRegistration body) {
    if (Utils.isClientNoSupportJsonResponse(request)) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    CommonAddResponse registerResponse = groupService.registerGroup(body);
    if (registerResponse.getId() != null) {
      GroupRegistrationResponse respBody =
          new GroupRegistrationResponse().groupId(registerResponse.getId());
      return ResponseEntity.status(HttpStatus.CREATED).body(respBody);
    } else if (registerResponse.getErrorResult() == null) {
      return ResponseEntity.status(registerResponse.getResponse()).build();
    } else {
      return ResponseEntity.status(registerResponse.getResponse())
          .body(registerResponse.getErrorResult());
    }
  }

  @Override
  public ResponseEntity<?> getGroup(UUID groupId) {
    if (Utils.isClientNoSupportJsonResponse(request)) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    CommonGetResponse<GroupInfo> getResp = groupService.getGroupById(groupId);
    if (getResp.isError()) {
      if (getResp.getErrorResult() != null) {
        return ResponseEntity.status(getResp.getResponse()).body(getResp.getErrorResult());
      } else {
        return ResponseEntity.status(getResp.getResponse()).build();
      }
    }

    GroupInfo groupInfo = getResp.getObj();
    return ResponseEntity.ok().body(groupInfo);
  }

  @Override
  public ResponseEntity<?> modifyGroup(UUID groupId, @Valid GroupModification body) {
    if (Utils.isClientNoSupportJsonResponse(request)) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    CommonOperResponse updateResponse = groupService.modifyGroup(groupId, body);
    if (updateResponse.getErrorResult() == null) {
      return ResponseEntity.status(updateResponse.getResponse()).build();
    } else {
      return ResponseEntity.status(updateResponse.getResponse())
          .body(updateResponse.getErrorResult());
    }
  }

  @Override
  public ResponseEntity<?> enableDisableGroup(UUID groupId, @Valid EnableDisableGroup body) {
    if (Utils.isClientNoSupportJsonResponse(request)) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    CommonOperResponse patchResponse = groupService.enableDisableGroup(groupId, body);
    if (patchResponse.getErrorResult() == null) {
      return ResponseEntity.status(patchResponse.getResponse()).build();
    } else {
      return ResponseEntity.status(patchResponse.getResponse())
          .body(patchResponse.getErrorResult());
    }
  }

  @Override
  public ResponseEntity<?> deleteGroup(UUID groupId) {
    if (Utils.isClientNoSupportJsonResponse(request)) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    CommonOperResponse deleteResponse = groupService.deleteGroup(groupId);
    if (deleteResponse.getErrorResult() == null) {
      return ResponseEntity.status(deleteResponse.getResponse()).build();
    } else {
      return ResponseEntity.status(deleteResponse.getResponse())
          .body(deleteResponse.getErrorResult());
    }
  }

  @Override
  public ResponseEntity<?> addDeviceToGroup(UUID groupId, UUID deviceId) {
    if (Utils.isClientNoSupportJsonResponse(request)) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    CommonOperResponse patchResponse = groupService.addDeviceToGroup(groupId, deviceId);
    if (patchResponse.getErrorResult() == null) {
      return ResponseEntity.status(patchResponse.getResponse()).build();
    } else {
      return ResponseEntity.status(patchResponse.getResponse())
          .body(patchResponse.getErrorResult());
    }
  }

  @Override
  public ResponseEntity<?> deleteDeviceFromGroup(UUID groupId, UUID deviceId) {
    if (Utils.isClientNoSupportJsonResponse(request)) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    CommonOperResponse patchResponse = groupService.deleteDeviceFromGroup(groupId, deviceId);
    if (patchResponse.getErrorResult() == null) {
      return ResponseEntity.status(patchResponse.getResponse()).build();
    } else {
      return ResponseEntity.status(patchResponse.getResponse())
          .body(patchResponse.getErrorResult());
    }
  }

}
