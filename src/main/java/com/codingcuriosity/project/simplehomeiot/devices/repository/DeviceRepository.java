package com.codingcuriosity.project.simplehomeiot.devices.repository;

import com.codingcuriosity.project.simplehomeiot.common.repository.RedisRepository;
import com.codingcuriosity.project.simplehomeiot.devices.model.DeviceInfo;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface DeviceRepository extends RedisRepository {

  public abstract Map<UUID, DeviceInfo> findAll();

  public abstract DeviceInfo findById(UUID deviceId);

  /* Note: If a set of 5 deleted ids are passed, this will return a list of 5 nulls. */
  public abstract List<DeviceInfo> findManyById(Set<UUID> deviceIdSet);

  public abstract void add(DeviceInfo deviceInfo);

  public abstract void update(UUID deviceId, DeviceInfo deviceInfo);

  public abstract void delete(UUID deviceId);

}
