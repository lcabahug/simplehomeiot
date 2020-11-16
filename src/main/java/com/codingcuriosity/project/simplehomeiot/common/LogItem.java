package com.codingcuriosity.project.simplehomeiot.common;

import com.codingcuriosity.project.simplehomeiot.logs.repository.ControllerLogRepository;

public interface LogItem {

  public abstract ControllerLogRepository getLogRepo();

}
