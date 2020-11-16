package com.codingcuriosity.project.simplehomeiot.logs.repository;

import com.codingcuriosity.project.simplehomeiot.common.repository.RedisRepository;
import com.codingcuriosity.project.simplehomeiot.logs.model.DeviceLogInfo;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface DeviceLogRepository extends RedisRepository {

  public abstract Map<UUID, DeviceLogInfo> findAll();

  public abstract DeviceLogInfo findById(UUID logId);

  /* Note: If a set of 5 deleted ids are passed, this will return a list of 5 nulls. */
  public abstract List<DeviceLogInfo> findManyById(Set<UUID> logIdSet);

  public abstract void add(DeviceLogInfo logInfo);

  public abstract void update(UUID logId, DeviceLogInfo logInfo);

  public abstract void delete(UUID logId);

  public abstract boolean deleteAll();

}
