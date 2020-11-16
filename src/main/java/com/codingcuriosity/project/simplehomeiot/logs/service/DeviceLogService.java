package com.codingcuriosity.project.simplehomeiot.logs.service;

import com.codingcuriosity.project.simplehomeiot.common.LogItem;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonGetListResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonOperResponse;
import com.codingcuriosity.project.simplehomeiot.logs.model.DeviceLogInfo;
import java.util.UUID;

public interface DeviceLogService extends LogItem {

  public abstract CommonGetListResponse<DeviceLogInfo> getLogInfo(Integer skip, Integer limit);

  public abstract CommonGetListResponse<DeviceLogInfo> getFilteredLogInfo(UUID deviceId,
      Integer skip, Integer limit);

  public abstract CommonOperResponse deleteAllLogs();

  public abstract CommonOperResponse deleteSpecificLogs(UUID deviceId);

}
