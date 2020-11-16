package com.codingcuriosity.project.simplehomeiot.zones.service;

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
import com.codingcuriosity.project.simplehomeiot.logs.repository.ControllerLogRepository;
import com.codingcuriosity.project.simplehomeiot.zones.cvt.ZoneConverter;
import com.codingcuriosity.project.simplehomeiot.zones.model.ZoneCreation;
import com.codingcuriosity.project.simplehomeiot.zones.model.ZoneInfo;
import com.codingcuriosity.project.simplehomeiot.zones.model.ZoneModification;
import com.codingcuriosity.project.simplehomeiot.zones.model.ZoneSetDevice;
import com.codingcuriosity.project.simplehomeiot.zones.repository.ZoneRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ZoneServiceImpl implements ZoneService {

  private final DeviceRepository deviceRepository;
  private final ZoneRepository zoneRepository;
  private final ControllerLogRepository logRepository;

  @Autowired
  public ZoneServiceImpl(DeviceRepository deviceRepository, ZoneRepository zoneRepository,
      ControllerLogRepository logRepository) {
    this.deviceRepository = deviceRepository;
    this.zoneRepository = zoneRepository;
    this.logRepository = logRepository;
  }

  @Override
  public ControllerLogRepository getLogRepo() {
    return this.logRepository;
  }

  private CommonOperResponse checkDeviceIdList(List<UUID> deviceIdList, String errorMessage) {
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;

    List<UUID> devIdsInZone = deviceIdList;
    if (devIdsInZone == null) {
      response = HttpStatus.BAD_REQUEST;
      return new CommonOperResponse(response, errResult);
    } else if (!devIdsInZone.isEmpty()) {
      boolean hasNullDeviceId = devIdsInZone.stream().anyMatch(Objects::isNull);
      if (hasNullDeviceId) {
        response = HttpStatus.BAD_REQUEST;
        return new CommonOperResponse(response, errResult);
      }

      // Check for duplicate entries
      Set<UUID> uniqueDevIds = new HashSet<>(devIdsInZone);
      if (uniqueDevIds.size() < devIdsInZone.size()) {
        response = HttpStatus.BAD_REQUEST;
        return new CommonOperResponse(response, errResult);
      }

      // Check if all DeviceIds involved exist and are registered
      List<DeviceInfo> devInfoInZoneList = this.deviceRepository.findManyById(uniqueDevIds);
      if (devInfoInZoneList == null || devInfoInZoneList.isEmpty()) {
        response = HttpStatus.INTERNAL_SERVER_ERROR;
        String errMsg = errorMessage;
        String errDetails = "There are no existing devices for this zone.";
        errResult = AppLogger.error(this, ErrorType.ERROR, errMsg, errDetails);
        return new CommonOperResponse(response, errResult);
      }

      boolean hasNullDeviceInfo = devInfoInZoneList.stream().anyMatch(Objects::isNull);
      if (hasNullDeviceInfo) {
        response = HttpStatus.INTERNAL_SERVER_ERROR;
        String errMsg = errorMessage;
        String errDetails = "One or more devices in this zone do not exist.";
        errResult = AppLogger.error(this, ErrorType.ERROR, errMsg, errDetails);
        return new CommonOperResponse(response, errResult);
      }

      boolean hasUnregisteredDeviceInfo =
          devInfoInZoneList.stream().anyMatch(devInfo -> devInfo.isIsRegistered() != true);
      if (hasUnregisteredDeviceInfo) {
        response = HttpStatus.INTERNAL_SERVER_ERROR;
        String errMsg = errorMessage;
        String errDetails = "One or more devices in this zone are not registered.";
        errResult = AppLogger.error(this, ErrorType.ERROR, errMsg, errDetails);
        return new CommonOperResponse(response, errResult);
      }
    }

    return new CommonOperResponse(response, errResult);
  }

  @Override
  public CommonGetListResponse<ZoneInfo> getZones(Integer skip, Integer limit) {
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;

    Map<UUID, ZoneInfo> zoneMap = this.zoneRepository.findAll();
    if (zoneMap == null || zoneMap.isEmpty()) {
      // Return OK but blank since this simply means the DB is empty
      String logMsg = "Empty list return";
      String logDtls = "No zones found in repository.";
      AppLogger.info(this, logMsg, logDtls);
      return new CommonGetListResponse<>(Collections.emptyList(), response, errResult);
    }

    // Extract Zone Collection from Map
    Collection<ZoneInfo> zoneList = zoneMap.values();

    // Set 'skip' filter
    int offset = 0;
    if (skip != null) {
      if (skip < 0) {
        response = HttpStatus.BAD_REQUEST;
        return new CommonGetListResponse<>(Collections.emptyList(), response, errResult);
      } else if (skip >= zoneList.size()) {
        return new CommonGetListResponse<>(Collections.emptyList(), response, errResult);
      }
      offset = skip;
    }

    // Set 'limit' filter
    int limitVal = zoneList.size();
    if (limit != null) {
      if (limit < 0) {
        response = HttpStatus.BAD_REQUEST;
        return new CommonGetListResponse<>(Collections.emptyList(), response, errResult);
      }
      limitVal = limit;
    }

    // Sort List based on Name (Ascending) and apply filters
    List<ZoneInfo> sortedList = zoneList.stream() //
        .sorted(Comparator.comparing(ZoneInfo::getName)) //
        .skip(offset) //
        .limit(limitVal) //
        .collect(Collectors.toCollection(ArrayList::new));

    return new CommonGetListResponse<>(sortedList, response, errResult);
  }

  @Override
  public CommonGetResponse<ZoneInfo> getZoneById(UUID zoneId) {
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;

    String logMsg;
    String logDtls;
    ZoneInfo zoneInfo = this.zoneRepository.findById(zoneId);
    if (zoneInfo == null) {
      response = HttpStatus.NOT_FOUND;
      logMsg = "Zone Not Found";
      logDtls = String.format("Cannot find Zone with zoneId = %s in the repository", zoneId);
    } else {
      logMsg = "Request Successful";
      logDtls = String.format("ZoneInfo = %s", zoneInfo.toString());
    }

    AppLogger.info(this, logMsg, logDtls);

    return new CommonGetResponse<>(zoneInfo, response, errResult);
  }

  @Override
  public CommonAddResponse registerZone(ZoneCreation zoneCreation) {
    HttpStatus response = HttpStatus.CREATED;
    ErrorResult errResult = null;
    UUID zoneId = null;

    // Check for any existing conflicting entries
    Map<UUID, ZoneInfo> zoneMap = this.zoneRepository.findAll();
    if (zoneMap != null && !zoneMap.isEmpty()) {
      String nameToFind = zoneCreation.getName();

      Optional<ZoneInfo> existingEntry = zoneMap.values().stream() //
          .filter(Objects::nonNull) //
          .filter(zoneInfo -> nameToFind.equals(zoneInfo.getName())) //
          .findFirst();
      if (existingEntry.isPresent()) {
        response = HttpStatus.CONFLICT;
        return new CommonAddResponse(zoneId, response, errResult);
      }
    }

    // Check the deviceId list
    CommonOperResponse resp =
        checkDeviceIdList(zoneCreation.getDeviceIds(), "Cannot register zone");
    if (!HttpStatus.OK.equals(resp.getResponse())) {
      return new CommonAddResponse(zoneId, resp.getResponse(), resp.getErrorResult());
    }

    // Generate Zone ID
    zoneId = Utils.generateId(id -> {
      ZoneInfo existingZone = this.zoneRepository.findById(id);
      return (existingZone != null);
    });

    ZoneInfo newZone = ZoneConverter.createZoneInfo(zoneId, zoneCreation);

    this.zoneRepository.add(newZone);

    return new CommonAddResponse(zoneId, response, errResult);
  }

  @Override
  public CommonOperResponse modifyZone(UUID zoneId, ZoneModification zoneModInfo) {
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;

    Map<UUID, ZoneInfo> zoneMap = this.zoneRepository.findAll();
    if (zoneMap == null || zoneMap.isEmpty()) {
      response = HttpStatus.NOT_FOUND;
      return new CommonOperResponse(response, errResult);
    }

    String nameToFind = zoneModInfo.getName();
    UUID idToFind = zoneId;

    ZoneInfo foundZone = null;
    for (ZoneInfo zone : zoneMap.values()) {
      if (idToFind.equals(zone.getZoneId())) {
        foundZone = zone;
        continue;
      } else if (nameToFind.equals(zone.getName())) {
        response = HttpStatus.CONFLICT;
        return new CommonOperResponse(response, errResult);
      }
    }
    if (foundZone == null) {
      response = HttpStatus.NOT_FOUND;
      return new CommonOperResponse(response, errResult);
    }

    // Check the deviceId list
    CommonOperResponse resp = checkDeviceIdList(zoneModInfo.getDeviceIds(), "Cannot modify zone");
    if (!HttpStatus.OK.equals(resp.getResponse())) {
      return new CommonOperResponse(resp.getResponse(), resp.getErrorResult());
    }

    ZoneInfo updatedZone = ZoneConverter.getModifiedZoneInfo(foundZone, zoneModInfo);

    this.zoneRepository.update(zoneId, updatedZone);

    return new CommonOperResponse(response, errResult);
  }

  @Override
  public CommonOperResponse setDevicesToZone(UUID zoneId, ZoneSetDevice zonePatchInfo) {
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;

    ZoneInfo zoneInfo = this.zoneRepository.findById(zoneId);
    if (zoneInfo == null) {
      response = HttpStatus.NOT_FOUND;
      return new CommonOperResponse(response, errResult);
    }

    // Check the deviceId list
    CommonOperResponse resp =
        checkDeviceIdList(zonePatchInfo.getDeviceIds(), "Cannot set devices in zone");
    if (!HttpStatus.OK.equals(resp.getResponse())) {
      return new CommonOperResponse(resp.getResponse(), resp.getErrorResult());
    }

    ZoneInfo updatedZone = ZoneConverter.getPatchedZoneInfo(zoneInfo, zonePatchInfo);

    this.zoneRepository.update(zoneId, updatedZone);

    return new CommonOperResponse(response, errResult);
  }

  @Override
  public CommonOperResponse deleteZone(UUID zoneId) {
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;

    ZoneInfo zoneInfo = this.zoneRepository.findById(zoneId);
    if (zoneInfo == null) {
      response = HttpStatus.NOT_FOUND;
      return new CommonOperResponse(response, errResult);
    }

    this.zoneRepository.delete(zoneId);

    return new CommonOperResponse(response, errResult);
  }

}
