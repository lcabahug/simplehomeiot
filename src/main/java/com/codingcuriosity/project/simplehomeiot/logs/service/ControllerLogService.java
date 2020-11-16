package com.codingcuriosity.project.simplehomeiot.logs.service;

import com.codingcuriosity.project.simplehomeiot.common.LogItem;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonGetListResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonOperResponse;
import com.codingcuriosity.project.simplehomeiot.logs.model.ControllerLogInfo;

public interface ControllerLogService extends LogItem {

  public abstract CommonGetListResponse<ControllerLogInfo> getLogInfo(Integer skip, Integer limit);

  public abstract CommonOperResponse deleteLogs();

}
