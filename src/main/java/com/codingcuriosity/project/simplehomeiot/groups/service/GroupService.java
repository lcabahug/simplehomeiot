package com.codingcuriosity.project.simplehomeiot.groups.service;

import com.codingcuriosity.project.simplehomeiot.common.LogItem;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonAddResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonGetListResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonGetResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonOperResponse;
import com.codingcuriosity.project.simplehomeiot.groups.model.EnableDisableGroup;
import com.codingcuriosity.project.simplehomeiot.groups.model.GroupInfo;
import com.codingcuriosity.project.simplehomeiot.groups.model.GroupModification;
import com.codingcuriosity.project.simplehomeiot.groups.model.GroupRegistration;
import java.util.UUID;

public interface GroupService extends LogItem {

  public abstract CommonGetListResponse<GroupInfo> getGroups(Integer skip, Integer limit);

  public abstract CommonGetResponse<GroupInfo> getGroupById(UUID groupId);

  public abstract CommonAddResponse registerGroup(GroupRegistration groupRegistration);

  public abstract CommonOperResponse modifyGroup(UUID groupId, GroupModification groupModInfo);

  public abstract CommonOperResponse enableDisableGroup(UUID groupId,
      EnableDisableGroup groupPatchInfo);

  public abstract CommonOperResponse deleteGroup(UUID groupId);

  public abstract CommonOperResponse addDeviceToGroup(UUID groupId, UUID deviceId);

  public abstract CommonOperResponse deleteDeviceFromGroup(UUID groupId, UUID deviceId);

}
