package com.codingcuriosity.project.simplehomeiot.logs.service;

import com.codingcuriosity.project.simplehomeiot.common.AppLogger;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonGetListResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonOperResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.ErrorResult;
import com.codingcuriosity.project.simplehomeiot.common.model.ErrorType;
import com.codingcuriosity.project.simplehomeiot.devices.model.DeviceInfo;
import com.codingcuriosity.project.simplehomeiot.devices.repository.DeviceRepository;
import com.codingcuriosity.project.simplehomeiot.logs.model.DeviceLogInfo;
import com.codingcuriosity.project.simplehomeiot.logs.repository.ControllerLogRepository;
import com.codingcuriosity.project.simplehomeiot.logs.repository.DeviceLogRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class DeviceLogServiceImpl implements DeviceLogService {

  private final DeviceLogRepository deviceLogRepository;
  private final DeviceRepository deviceRepository;
  private final ControllerLogRepository logRepository;

  @Autowired
  public DeviceLogServiceImpl(DeviceLogRepository deviceLogRepository,
      DeviceRepository deviceRepository, ControllerLogRepository logRepository) {
    this.deviceLogRepository = deviceLogRepository;
    this.deviceRepository = deviceRepository;
    this.logRepository = logRepository;
  }

  @Override
  public ControllerLogRepository getLogRepo() {
    return this.logRepository;
  }

  private CommonGetListResponse<DeviceLogInfo> getLogInfoCommon(Integer skip, Integer limit,
      Predicate<DeviceLogInfo> logFilter) {
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;

    Map<UUID, DeviceLogInfo> logMap = this.deviceLogRepository.findAll();
    if (logMap == null || logMap.isEmpty()) {
      return new CommonGetListResponse<>(Collections.emptyList(), response, errResult);
    }

    Predicate<DeviceLogInfo> entryFilter;
    if (logFilter != null) {
      entryFilter = logFilter;
    } else {
      entryFilter = log -> true;
    }

    // Extract Log List from Map (apply filter when available)
    List<DeviceLogInfo> logList = logMap.values().stream() //
        .filter(entryFilter) //
        .collect(Collectors.toList());

    // Set 'skip' filter
    int offset = 0;
    if (skip != null) {
      if (skip < 0) {
        response = HttpStatus.BAD_REQUEST;
        return new CommonGetListResponse<>(Collections.emptyList(), response, errResult);
      } else if (skip >= logList.size()) {
        return new CommonGetListResponse<>(Collections.emptyList(), response, errResult);
      }
      offset = skip;
    }

    // Set 'limit' filter
    int limitVal = logList.size();
    if (limit != null) {
      if (limit < 0) {
        response = HttpStatus.BAD_REQUEST;
        return new CommonGetListResponse<>(Collections.emptyList(), response, errResult);
      }
      limitVal = limit;
    }

    // Sort List based on Timestamp (Ascending) and apply filters
    List<DeviceLogInfo> sortedList = logList.stream() //
        .sorted(Comparator.comparing(DeviceLogInfo::getTimestamp)) //
        .skip(offset) //
        .limit(limitVal) //
        .collect(Collectors.toCollection(ArrayList::new));

    return new CommonGetListResponse<>(sortedList, response, errResult);
  }

  @Override
  public CommonGetListResponse<DeviceLogInfo> getLogInfo(Integer skip, Integer limit) {
    return getLogInfoCommon(skip, limit, null);
  }

  @Override
  public CommonGetListResponse<DeviceLogInfo> getFilteredLogInfo(UUID deviceId, Integer skip,
      Integer limit) {
    HttpStatus response;
    if (deviceId == null) {
      response = HttpStatus.BAD_REQUEST;
      return new CommonGetListResponse<>(Collections.emptyList(), response, null);
    }

    DeviceInfo deviceInfo = this.deviceRepository.findById(deviceId);
    if (deviceInfo == null) {
      response = HttpStatus.NOT_FOUND;
      return new CommonGetListResponse<>(Collections.emptyList(), response, null);
    }

    return getLogInfoCommon(skip, limit, log -> deviceId.equals(log.getDeviceId()));
  }

  @Override
  public CommonOperResponse deleteAllLogs() {
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;

    // Indiscriminately delete all device log entries :)
    boolean result = this.deviceLogRepository.deleteAll();
    if (!result) {
      response = HttpStatus.INTERNAL_SERVER_ERROR;
      String errMessage = "Cannot delete all log entries.";
      String errDetails = "Some or all device log entries may not have been deleted.";
      errResult = AppLogger.error(this, ErrorType.ERROR, errMessage, errDetails);
    }

    return new CommonOperResponse(response, errResult);
  }

  @Override
  public CommonOperResponse deleteSpecificLogs(UUID deviceId) {
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;

    Map<UUID, DeviceLogInfo> deviceLogMap = this.deviceLogRepository.findAll();
    if (deviceLogMap != null && !deviceLogMap.isEmpty()) {
      Set<UUID> idsToDelSet = deviceLogMap.values().stream() //
          .filter(logInfo -> (deviceId.equals(logInfo.getDeviceId()))) //
          .map(DeviceLogInfo::getLogId) //
          .collect(Collectors.toSet());

      // Delete all filtered device logs
      for (UUID idToDel : idsToDelSet) {
        this.deviceLogRepository.delete(idToDel);
      }

      // Check if all filtered logs were deleted
      List<DeviceLogInfo> remainingLogs = this.deviceLogRepository.findManyById(idsToDelSet);
      if (remainingLogs != null && !remainingLogs.isEmpty()) {
        boolean nullsOnly = remainingLogs.stream().allMatch(Objects::isNull);
        if (!nullsOnly) {
          response = HttpStatus.INTERNAL_SERVER_ERROR;
          String errMessage =
              String.format("Cannot delete all log entries for device %s.", deviceId);
          String errDetails =
              "Some or all device log entries for this device may not have been deleted.";
          errResult = AppLogger.error(this, ErrorType.ERROR, errMessage, errDetails);
        }
      }
    }

    return new CommonOperResponse(response, errResult);
  }

}
