package com.codingcuriosity.project.simplehomeiot.logs.service;

import com.codingcuriosity.project.simplehomeiot.common.AppLogger;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonGetListResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonOperResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.ErrorResult;
import com.codingcuriosity.project.simplehomeiot.common.model.ErrorType;
import com.codingcuriosity.project.simplehomeiot.logs.model.ControllerLogInfo;
import com.codingcuriosity.project.simplehomeiot.logs.repository.ControllerLogRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ControllerLogServiceImpl implements ControllerLogService {

  private final ControllerLogRepository controllerLogRepository;

  @Autowired
  public ControllerLogServiceImpl(ControllerLogRepository controllerLogRepository) {
    this.controllerLogRepository = controllerLogRepository;
  }

  @Override
  public ControllerLogRepository getLogRepo() {
    return this.controllerLogRepository;
  }

  @Override
  public CommonGetListResponse<ControllerLogInfo> getLogInfo(Integer skip, Integer limit) {
    Map<UUID, ControllerLogInfo> logMap = this.controllerLogRepository.findAll();
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;

    if (logMap == null || logMap.isEmpty()) {
      return new CommonGetListResponse<>(Collections.emptyList(), response, errResult);
    }

    // Extract Log Collection from Map
    Collection<ControllerLogInfo> logList = logMap.values();

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
    List<ControllerLogInfo> sortedList = logList.stream() //
        .sorted(Comparator.comparing(ControllerLogInfo::getTimestamp)) //
        .skip(offset) //
        .limit(limitVal) //
        .collect(Collectors.toCollection(ArrayList::new));

    return new CommonGetListResponse<>(sortedList, response, errResult);
  }

  @Override
  public CommonOperResponse deleteLogs() {
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;

    // Indiscriminately delete all controller log entries :)
    boolean result = this.controllerLogRepository.deleteAll();
    if (!result) {
      response = HttpStatus.INTERNAL_SERVER_ERROR;
      String errMessage = "Cannot delete all log entries.";
      String errDetails = "Some or all controller log entries may not have been deleted.";
      errResult = AppLogger.error(this, ErrorType.ERROR, errMessage, errDetails);
    }

    return new CommonOperResponse(response, errResult);
  }

}
