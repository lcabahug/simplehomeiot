package com.codingcuriosity.project.simplehomeiot.controls.repository;

import com.codingcuriosity.project.simplehomeiot.common.repository.RedisRepository;
import com.codingcuriosity.project.simplehomeiot.controls.model.DeviceControlInfo;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface ControlRepository extends RedisRepository {

  public abstract Map<UUID, DeviceControlInfo> findAll();

  public abstract DeviceControlInfo findById(UUID controlId);

  /* Note: If a set of 5 deleted ids are passed, this will return a list of 5 nulls. */
  public abstract List<DeviceControlInfo> findManyById(Set<UUID> controlIdSet);

  public abstract void add(DeviceControlInfo controlInfo);

  public abstract void update(UUID controlId, DeviceControlInfo controlInfo);

  public abstract void delete(UUID controlId);

}
