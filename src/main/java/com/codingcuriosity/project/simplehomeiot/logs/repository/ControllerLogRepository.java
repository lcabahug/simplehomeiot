package com.codingcuriosity.project.simplehomeiot.logs.repository;

import com.codingcuriosity.project.simplehomeiot.common.repository.RedisRepository;
import com.codingcuriosity.project.simplehomeiot.logs.model.ControllerLogInfo;
import java.util.Map;
import java.util.UUID;

public interface ControllerLogRepository extends RedisRepository {

  public abstract Map<UUID, ControllerLogInfo> findAll();

  public abstract ControllerLogInfo findById(UUID logId);

  public abstract void add(ControllerLogInfo logInfo);

  public abstract void update(UUID logId, ControllerLogInfo logInfo);

  public abstract void delete(UUID logId);

  public abstract boolean deleteAll();

}
