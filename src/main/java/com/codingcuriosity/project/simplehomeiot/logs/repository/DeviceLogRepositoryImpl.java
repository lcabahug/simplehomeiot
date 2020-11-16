package com.codingcuriosity.project.simplehomeiot.logs.repository;

import com.codingcuriosity.project.simplehomeiot.common.repository.RepoKey;
import com.codingcuriosity.project.simplehomeiot.logs.model.DeviceLogInfo;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DeviceLogRepositoryImpl implements DeviceLogRepository {

  private final RedisTemplate<String, DeviceLogInfo> redisTemplate;
  private final HashOperations<String, UUID, DeviceLogInfo> hashOperations;
  private final String keyName;

  @Autowired
  public DeviceLogRepositoryImpl(RedisTemplate<String, DeviceLogInfo> redisTemplate) {
    this.redisTemplate = redisTemplate;
    this.hashOperations = this.redisTemplate.opsForHash();
    this.keyName = getKey().name();
  }

  @Override
  public RepoKey getKey() {
    return RepoKey.LOGS_DEVICE;
  }

  @Override
  public Map<UUID, DeviceLogInfo> findAll() {
    return this.hashOperations.entries(this.keyName);
  }

  @Override
  public DeviceLogInfo findById(UUID logId) {
    return this.hashOperations.get(this.keyName, logId);
  }

  @Override
  public List<DeviceLogInfo> findManyById(Set<UUID> logIdSet) {
    return this.hashOperations.multiGet(this.keyName, logIdSet);
  }

  @Override
  public void add(DeviceLogInfo logInfo) {
    update(logInfo.getLogId(), logInfo);
  }

  @Override
  public void update(UUID logId, DeviceLogInfo logInfo) {
    this.hashOperations.put(this.keyName, logId, logInfo);
  }

  @Override
  public void delete(UUID logId) {
    this.hashOperations.delete(this.keyName, logId);
  }

  @Override
  public boolean deleteAll() {
    return this.redisTemplate.delete(this.keyName);
  }

}
