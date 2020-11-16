package com.codingcuriosity.project.simplehomeiot.controls.repository;

import com.codingcuriosity.project.simplehomeiot.common.repository.RepoKey;
import com.codingcuriosity.project.simplehomeiot.controls.model.DeviceControlInfo;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ControlRepositoryImpl implements ControlRepository {

  private final RedisTemplate<String, DeviceControlInfo> redisTemplate;
  private final HashOperations<String, UUID, DeviceControlInfo> hashOperations;
  private final String keyName;

  @Autowired
  public ControlRepositoryImpl(RedisTemplate<String, DeviceControlInfo> redisTemplate) {
    this.redisTemplate = redisTemplate;
    this.hashOperations = this.redisTemplate.opsForHash();
    this.keyName = getKey().name();
  }

  @Override
  public RepoKey getKey() {
    return RepoKey.CONTROL_DEVICE;
  }

  @Override
  public Map<UUID, DeviceControlInfo> findAll() {
    return this.hashOperations.entries(this.keyName);
  }

  @Override
  public DeviceControlInfo findById(UUID controlId) {
    return this.hashOperations.get(this.keyName, controlId);
  }

  @Override
  public List<DeviceControlInfo> findManyById(Set<UUID> controlIdSet) {
    return this.hashOperations.multiGet(this.keyName, controlIdSet);
  }

  @Override
  public void add(DeviceControlInfo controlInfo) {
    update(controlInfo.getControlId(), controlInfo);
  }

  @Override
  public void update(UUID controlId, DeviceControlInfo controlInfo) {
    this.hashOperations.put(this.keyName, controlId, controlInfo);
  }

  @Override
  public void delete(UUID controlId) {
    this.hashOperations.delete(this.keyName, controlId);
  }

}
