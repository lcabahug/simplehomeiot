package com.codingcuriosity.project.simplehomeiot.groups.cvt;

import com.codingcuriosity.project.simplehomeiot.groups.model.EnableDisableGroup;
import com.codingcuriosity.project.simplehomeiot.groups.model.GroupInfo;
import com.codingcuriosity.project.simplehomeiot.groups.model.GroupModification;
import com.codingcuriosity.project.simplehomeiot.groups.model.GroupRawInfo;
import com.codingcuriosity.project.simplehomeiot.groups.model.GroupRegistration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GroupConverter {

  private static GroupInfo commonCreateGroupInfo(GroupRawInfo groupRaw) {
    GroupInfo retObj = new GroupInfo();
    retObj.setGroupId(groupRaw.getGroupId());
    retObj.setName(groupRaw.getName());
    retObj.setDescription(groupRaw.getDescription());
    retObj.setComment(groupRaw.getComment());
    retObj.setIsEnabled(groupRaw.isIsEnabled());
    return retObj;
  }

  public static GroupInfo createGroupInfo(GroupRawInfo groupRaw) {
    GroupInfo retObj = commonCreateGroupInfo(groupRaw);
    retObj.setDevices(Collections.emptyList());
    return retObj;
  }

  public static GroupInfo createGroupInfo(GroupRawInfo groupRaw, List<UUID> deviceList) {
    GroupInfo retObj = commonCreateGroupInfo(groupRaw);
    List<UUID> mappedDevices = (deviceList == null) ? Collections.emptyList() : deviceList;
    retObj.setDevices(mappedDevices);
    return retObj;
  }

  public static List<GroupInfo> createGroupInfoList(List<GroupRawInfo> groupRawList) {
    List<GroupInfo> retObjList = new ArrayList<>();
    for (GroupRawInfo grpRaw : groupRawList) {
      retObjList.add(createGroupInfo(grpRaw));
    }
    return retObjList;
  }

  public static List<GroupInfo> createGroupInfoList(List<GroupRawInfo> groupRawList,
      Map<UUID, List<UUID>> grpDevicesMap) {
    List<GroupInfo> retObjList = new ArrayList<>();
    for (GroupRawInfo grpRaw : groupRawList) {
      retObjList.add(createGroupInfo(grpRaw, grpDevicesMap.get(grpRaw.getGroupId())));
    }
    return retObjList;
  }

  public static GroupRawInfo createGroupRawInfo(UUID groupId, GroupRegistration groupRegInfo) {
    GroupRawInfo retObj = new GroupRawInfo();
    retObj.setGroupId(groupId);
    retObj.setName(groupRegInfo.getName());
    retObj.setDescription(groupRegInfo.getDescription());
    retObj.setComment(groupRegInfo.getComment());
    retObj.setIsEnabled(false);
    return retObj;
  }

  public static GroupRawInfo getModifiedGroupInfo(GroupRawInfo groupInfo,
      GroupModification groupModInfo) {
    GroupRawInfo retObj = new GroupRawInfo();
    retObj.setGroupId(groupInfo.getGroupId());
    retObj.setName(groupModInfo.getName());
    retObj.setDescription(groupModInfo.getDescription());
    retObj.setComment(groupModInfo.getComment());
    retObj.setIsEnabled(groupInfo.isIsEnabled());
    return retObj;
  }

  public static GroupRawInfo getPatchedGroupInfo(GroupRawInfo groupInfo,
      EnableDisableGroup groupPatchInfo) {
    GroupRawInfo retObj = new GroupRawInfo();
    retObj.setGroupId(groupInfo.getGroupId());
    retObj.setName(groupInfo.getName());
    retObj.setDescription(groupInfo.getDescription());
    retObj.setComment(groupInfo.getComment());
    retObj.setIsEnabled(groupPatchInfo.isIsEnabled());
    return retObj;
  }
}
