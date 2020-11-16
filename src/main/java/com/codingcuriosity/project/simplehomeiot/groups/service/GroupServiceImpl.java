package com.codingcuriosity.project.simplehomeiot.groups.service;

import com.codingcuriosity.project.simplehomeiot.common.AppLogger;
import com.codingcuriosity.project.simplehomeiot.common.Utils;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonAddResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonGetListResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonGetResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonOperResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.ErrorResult;
import com.codingcuriosity.project.simplehomeiot.common.model.ErrorType;
import com.codingcuriosity.project.simplehomeiot.devices.model.DeviceInfo;
import com.codingcuriosity.project.simplehomeiot.devices.repository.DeviceRepository;
import com.codingcuriosity.project.simplehomeiot.groups.cvt.GroupConverter;
import com.codingcuriosity.project.simplehomeiot.groups.model.EnableDisableGroup;
import com.codingcuriosity.project.simplehomeiot.groups.model.GroupInfo;
import com.codingcuriosity.project.simplehomeiot.groups.model.GroupModification;
import com.codingcuriosity.project.simplehomeiot.groups.model.GroupRawInfo;
import com.codingcuriosity.project.simplehomeiot.groups.model.GroupRegistration;
import com.codingcuriosity.project.simplehomeiot.groups.repository.GroupRepository;
import com.codingcuriosity.project.simplehomeiot.logs.repository.ControllerLogRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class GroupServiceImpl implements GroupService {

  private final GroupRepository groupRepository;
  private final DeviceRepository deviceRepository;
  private final ControllerLogRepository logRepository;

  @Autowired
  public GroupServiceImpl(GroupRepository groupRepository, DeviceRepository deviceRepository,
      ControllerLogRepository logRepository) {
    this.groupRepository = groupRepository;
    this.deviceRepository = deviceRepository;
    this.logRepository = logRepository;
  }

  @Override
  public ControllerLogRepository getLogRepo() {
    return this.logRepository;
  }

  @Override
  public CommonGetListResponse<GroupInfo> getGroups(Integer skip, Integer limit) {
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;

    // Get All Groups
    Map<UUID, GroupRawInfo> groupMap = this.groupRepository.findAll();
    if (groupMap == null || groupMap.isEmpty()) {
      return new CommonGetListResponse<GroupInfo>(Collections.emptyList(), response, errResult);
    }

    // Get All Devices
    Map<UUID, DeviceInfo> deviceMap;
    Map<UUID, DeviceInfo> deviceMapResp = this.deviceRepository.findAll();
    if (deviceMapResp == null) {
      deviceMap = Collections.emptyMap();
    } else {
      deviceMap = deviceMapResp;
    }

    // Extract Group Collection from Map
    Collection<GroupRawInfo> groupList = groupMap.values();

    // Set 'skip' filter
    int offset = 0;
    if (skip != null) {
      if (skip < 0) {
        response = HttpStatus.BAD_REQUEST;
        return new CommonGetListResponse<>(Collections.emptyList(), response, errResult);
      } else if (skip >= groupList.size()) {
        return new CommonGetListResponse<>(Collections.emptyList(), response, errResult);
      }
      offset = skip;
    }

    // Set 'limit' filter
    int limitVal = groupList.size();
    if (limit != null) {
      if (limit < 0) {
        response = HttpStatus.BAD_REQUEST;
        return new CommonGetListResponse<>(Collections.emptyList(), response, errResult);
      }
      limitVal = limit;
    }

    List<GroupInfo> finalList;

    // Sort List based on Name (Ascending) and apply filters
    List<GroupRawInfo> sortedGrpList = groupList.stream() //
        .sorted(Comparator.comparing(GroupRawInfo::getName)) //
        .skip(offset) //
        .limit(limitVal) //
        .collect(Collectors.toCollection(ArrayList::new));
    if (sortedGrpList.isEmpty()) {
      finalList = Collections.emptyList();
    } else if (deviceMap.isEmpty()) {
      // Convert to the formal Group List but with no device Ids
      finalList = GroupConverter.createGroupInfoList(sortedGrpList);
    } else {
      // Prepare the Group - Device Mapping
      Map<UUID, List<UUID>> grpDevicesMap = sortedGrpList.stream() //
          .collect(Collectors.toMap(GroupRawInfo::getGroupId, grpRaw -> new ArrayList<>()));

      // Create another list representing GroupIds on the sorted list
      List<UUID> groupIdList = sortedGrpList.stream() //
          .map(GroupRawInfo::getGroupId) //
          .collect(Collectors.toCollection(ArrayList::new));

      // Fill the Group - Device Map
      deviceMap.entrySet().stream() //
          .filter(entry -> (entry.getValue().getGroupId() != null
              && groupIdList.contains(entry.getValue().getGroupId()))) //
          .forEach(entry -> grpDevicesMap.get(entry.getValue().getGroupId()).add(entry.getKey()));

      // Create the Group List with Device Ids in it
      finalList = GroupConverter.createGroupInfoList(sortedGrpList, grpDevicesMap);
    }

    return new CommonGetListResponse<>(finalList, response, errResult);
  }

  @Override
  public CommonGetResponse<GroupInfo> getGroupById(UUID groupId) {
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;

    // Get All Groups
    GroupRawInfo groupRawInfo = this.groupRepository.findById(groupId);
    if (groupRawInfo == null) {
      response = HttpStatus.NOT_FOUND;
      return new CommonGetResponse<>(null, response, errResult);
    }

    // Get All Devices
    Map<UUID, DeviceInfo> deviceMap;
    Map<UUID, DeviceInfo> deviceMapResp = this.deviceRepository.findAll();
    if (deviceMapResp == null) {
      deviceMap = Collections.emptyMap();
    } else {
      deviceMap = deviceMapResp;
    }

    GroupInfo groupInfo;
    if (deviceMap.isEmpty()) {
      groupInfo = GroupConverter.createGroupInfo(groupRawInfo);
    } else {
      // Filter list to only include devices for the specified group
      List<UUID> deviceIdList = deviceMap.values().stream() //
          .filter(Objects::nonNull) //
          .filter(devInfo -> (groupId.equals(devInfo.getGroupId()))) //
          .map(DeviceInfo::getDeviceId) //
          .collect(Collectors.toCollection(ArrayList::new));
      if (deviceIdList.isEmpty()) {
        groupInfo = GroupConverter.createGroupInfo(groupRawInfo);
      } else {
        groupInfo = GroupConverter.createGroupInfo(groupRawInfo, deviceIdList);
      }
    }

    return new CommonGetResponse<>(groupInfo, response, errResult);
  }

  @Override
  public CommonAddResponse registerGroup(GroupRegistration groupRegistration) {
    HttpStatus response = HttpStatus.CREATED;
    ErrorResult errResult = null;
    UUID groupId = null;

    // Check for any existing conflicting entries
    Map<UUID, GroupRawInfo> groupMap = this.groupRepository.findAll();
    if (groupMap != null && !groupMap.isEmpty()) {
      String nameToFind = groupRegistration.getName();

      Optional<GroupRawInfo> existingEntry = groupMap.values().stream() //
          .filter(Objects::nonNull) //
          .filter(groupInfo -> nameToFind.equals(groupInfo.getName())) //
          .findFirst();
      if (existingEntry.isPresent()) {
        response = HttpStatus.CONFLICT;
        return new CommonAddResponse(groupId, response, errResult);
      }
    }

    // Generate Group ID
    groupId = Utils.generateId(id -> {
      GroupRawInfo existingGroup = this.groupRepository.findById(id);
      return (existingGroup != null);
    });

    GroupRawInfo groupInfo = GroupConverter.createGroupRawInfo(groupId, groupRegistration);

    this.groupRepository.add(groupInfo);

    return new CommonAddResponse(groupId, response, errResult);
  }

  @Override
  public CommonOperResponse modifyGroup(UUID groupId, GroupModification groupModInfo) {
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;

    Map<UUID, GroupRawInfo> groupMap = this.groupRepository.findAll();
    if (groupMap == null || groupMap.isEmpty()) {
      response = HttpStatus.NOT_FOUND;
      return new CommonOperResponse(response, errResult);
    }

    String nameToFind = groupModInfo.getName();
    UUID idToFind = groupId;

    GroupRawInfo foundGroup = null;
    for (GroupRawInfo grp : groupMap.values()) {
      if (idToFind.equals(grp.getGroupId())) {
        foundGroup = grp;
        continue;
      } else if (nameToFind.equals(grp.getName())) {
        response = HttpStatus.CONFLICT;
        return new CommonOperResponse(response, errResult);
      }
    }
    if (foundGroup == null) {
      response = HttpStatus.NOT_FOUND;
      return new CommonOperResponse(response, errResult);
    }

    GroupRawInfo modifiedGroupInfo = GroupConverter.getModifiedGroupInfo(foundGroup, groupModInfo);
    this.groupRepository.update(groupId, modifiedGroupInfo);

    return new CommonOperResponse(response, errResult);
  }

  @Override
  public CommonOperResponse enableDisableGroup(UUID groupId, EnableDisableGroup groupPatchInfo) {
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;

    GroupRawInfo groupInfo = this.groupRepository.findById(groupId);
    if (groupInfo == null) {
      response = HttpStatus.NOT_FOUND;
      return new CommonOperResponse(response, errResult);
    }

    boolean paramIsEnabled = groupPatchInfo.isIsEnabled();
    if (paramIsEnabled == groupInfo.isIsEnabled()) {
      response = HttpStatus.CONFLICT;
      return new CommonOperResponse(response, errResult);
    }

    GroupRawInfo modifiedGroupInfo = GroupConverter.getPatchedGroupInfo(groupInfo, groupPatchInfo);

    this.groupRepository.update(groupId, modifiedGroupInfo);

    return new CommonOperResponse(response, errResult);
  }

  @Override
  public CommonOperResponse deleteGroup(UUID groupId) {
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;

    GroupRawInfo groupInfo = this.groupRepository.findById(groupId);
    if (groupInfo == null) {
      response = HttpStatus.NOT_FOUND;
      return new CommonOperResponse(response, errResult);
    }

    // Cannot Delete Group Info if it is enabled
    if (groupInfo.isIsEnabled()) {
      response = HttpStatus.INTERNAL_SERVER_ERROR;
      String errMessage = "Cannot delete group";
      String errDetails = "Enabled groups canot be deleted.";
      errResult = AppLogger.error(this, ErrorType.ERROR, errMessage, errDetails);
      return new CommonOperResponse(response, errResult);
    }

    // Search for devices registered in this group and remove this group from those
    Map<UUID, DeviceInfo> deviceMap = this.deviceRepository.findAll();
    if (deviceMap != null && !deviceMap.isEmpty()) {
      List<DeviceInfo> devicesToUpdate = deviceMap.values().stream() //
          .filter(Objects::nonNull) //
          .filter(devInfo -> groupId.equals(devInfo.getGroupId())) //
          .collect(Collectors.toList());
      if (!devicesToUpdate.isEmpty()) {
        for (DeviceInfo deviceToUpdate : devicesToUpdate) {
          deviceToUpdate.setIsGrouped(false);
          deviceToUpdate.setGroupId(null);
          this.deviceRepository.update(deviceToUpdate.getDeviceId(), deviceToUpdate);
        }
      }
    }

    this.groupRepository.delete(groupId);

    return new CommonOperResponse(response, errResult);
  }

  @Override
  public CommonOperResponse addDeviceToGroup(UUID groupId, UUID deviceId) {
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;

    GroupRawInfo groupInfo = this.groupRepository.findById(groupId);
    if (groupInfo == null) {
      response = HttpStatus.NOT_FOUND;
      return new CommonOperResponse(response, errResult);
    }

    DeviceInfo deviceInfo = this.deviceRepository.findById(deviceId);
    if (deviceInfo == null) {
      response = HttpStatus.NOT_FOUND;
      return new CommonOperResponse(response, errResult);
    } else if (!deviceInfo.isIsRegistered()) {
      response = HttpStatus.INTERNAL_SERVER_ERROR;
      String errMsg = "Cannot add Device to Group";
      String errDetails = "Device is not registered.";
      errResult = AppLogger.error(this, ErrorType.ERROR, errMsg, errDetails);
      return new CommonOperResponse(response, errResult);
    } else if (deviceInfo.isIsGrouped()) {
      response = HttpStatus.INTERNAL_SERVER_ERROR;
      String errMsg = "Cannot add Device to Group";
      String errDetails = String.format("Device with deviceId = %s already belongs in a group.",
          deviceInfo.getDeviceId());
      errResult = AppLogger.error(this, ErrorType.ERROR, errMsg, errDetails);
      return new CommonOperResponse(response, errResult);
    }

    deviceInfo.setIsGrouped(true);
    deviceInfo.setGroupId(groupId);
    this.deviceRepository.update(deviceId, deviceInfo);

    return new CommonOperResponse(response, errResult);
  }

  @Override
  public CommonOperResponse deleteDeviceFromGroup(UUID groupId, UUID deviceId) {
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;

    GroupRawInfo groupInfo = this.groupRepository.findById(groupId);
    if (groupInfo == null) {
      response = HttpStatus.NOT_FOUND;
      return new CommonOperResponse(response, errResult);
    }

    DeviceInfo deviceInfo = this.deviceRepository.findById(deviceId);
    if (deviceInfo == null) {
      response = HttpStatus.NOT_FOUND;
      return new CommonOperResponse(response, errResult);
    } else if (!deviceInfo.isIsRegistered()) {
      response = HttpStatus.INTERNAL_SERVER_ERROR;
      String errMsg = "Cannot remove Device from Group";
      String errDetails = "Device is not registered.";
      errResult = AppLogger.error(this, ErrorType.ERROR, errMsg, errDetails);
      return new CommonOperResponse(response, errResult);
    } else if (!deviceInfo.isIsGrouped()) {
      response = HttpStatus.INTERNAL_SERVER_ERROR;
      String errMsg = "Cannot remove Device from Group";
      String errDetails = "Device does not belong to a Group.";
      errResult = AppLogger.error(this, ErrorType.ERROR, errMsg, errDetails);
      return new CommonOperResponse(response, errResult);
    } else if (deviceInfo.getGroupId() == null) {
      response = HttpStatus.INTERNAL_SERVER_ERROR;
      String errMsg = "Cannot remove Device from Group";
      String errDetails = "Device does not belong to a Group.";
      errResult = AppLogger.error(this, ErrorType.ERROR, errMsg, errDetails);
      return new CommonOperResponse(response, errResult);
    } else if (!deviceInfo.getGroupId().equals(groupId)) {
      response = HttpStatus.INTERNAL_SERVER_ERROR;
      String errMsg = "Cannot remove Device from Group";
      String errDetails = "Device does not belong to the specified Group.";
      errResult = AppLogger.error(this, ErrorType.ERROR, errMsg, errDetails);
      return new CommonOperResponse(response, errResult);
    }

    deviceInfo.setIsGrouped(false);
    deviceInfo.setGroupId(null);
    this.deviceRepository.update(deviceId, deviceInfo);

    return new CommonOperResponse(response, errResult);
  }

}
