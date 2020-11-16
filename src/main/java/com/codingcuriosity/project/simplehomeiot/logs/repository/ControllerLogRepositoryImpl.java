package com.codingcuriosity.project.simplehomeiot.logs.repository;

import com.codingcuriosity.project.simplehomeiot.common.repository.RepoKey;
import com.codingcuriosity.project.simplehomeiot.logs.model.ControllerLogInfo;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ControllerLogRepositoryImpl implements ControllerLogRepository {

  private final RedisTemplate<String, ControllerLogInfo> redisTemplate;
  private final HashOperations<String, UUID, ControllerLogInfo> hashOperations;
  private final String keyName;

  @Autowired
  public ControllerLogRepositoryImpl(RedisTemplate<String, ControllerLogInfo> redisTemplate) {
    this.redisTemplate = redisTemplate;
    this.hashOperations = this.redisTemplate.opsForHash();
    this.keyName = getKey().name();
  }

  @Override
  public RepoKey getKey() {
    return RepoKey.LOGS_CONTROLLER;
  }

  @Override
  public Map<UUID, ControllerLogInfo> findAll() {
    return this.hashOperations.entries(this.keyName);
  }

  @Override
  public ControllerLogInfo findById(UUID logId) {
    return this.hashOperations.get(this.keyName, logId);
  }

  @Override
  public void add(ControllerLogInfo logInfo) {
    update(logInfo.getLogId(), logInfo);
  }

  @Override
  public void update(UUID logId, ControllerLogInfo logInfo) {
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
