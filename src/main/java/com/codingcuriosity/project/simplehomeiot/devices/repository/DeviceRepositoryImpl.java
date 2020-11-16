package com.codingcuriosity.project.simplehomeiot.devices.repository;

import com.codingcuriosity.project.simplehomeiot.common.repository.RepoKey;
import com.codingcuriosity.project.simplehomeiot.devices.model.DeviceInfo;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DeviceRepositoryImpl implements DeviceRepository {

  private final RedisTemplate<String, DeviceInfo> redisTemplate;
  private final HashOperations<String, UUID, DeviceInfo> hashOperations;
  private final String keyName;

  @Autowired
  public DeviceRepositoryImpl(RedisTemplate<String, DeviceInfo> redisTemplate) {
    this.redisTemplate = redisTemplate;
    this.hashOperations = this.redisTemplate.opsForHash();
    this.keyName = getKey().name();
  }

  @Override
  public RepoKey getKey() {
    return RepoKey.DEVICE;
  }

  @Override
  public Map<UUID, DeviceInfo> findAll() {
    return this.hashOperations.entries(this.keyName);
  }

  @Override
  public DeviceInfo findById(UUID deviceId) {
    return this.hashOperations.get(this.keyName, deviceId);
  }

  @Override
  public List<DeviceInfo> findManyById(Set<UUID> deviceIdSet) {
    return this.hashOperations.multiGet(this.keyName, deviceIdSet);
  }

  @Override
  public void add(DeviceInfo deviceInfo) {
    update(deviceInfo.getDeviceId(), deviceInfo);
  }

  @Override
  public void update(UUID deviceId, DeviceInfo deviceInfo) {
    this.hashOperations.put(this.keyName, deviceId, deviceInfo);
  }

  @Override
  public void delete(UUID deviceId) {
    this.hashOperations.delete(this.keyName, deviceId);
  }

}
