package com.codingcuriosity.project.simplehomeiot.timers.repository;

import com.codingcuriosity.project.simplehomeiot.common.repository.RepoKey;
import com.codingcuriosity.project.simplehomeiot.timers.model.TimerInfo;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TimerRepositoryImpl implements TimerRepository {

  private final RedisTemplate<String, TimerInfo> redisTemplate;
  private final HashOperations<String, UUID, TimerInfo> hashOperations;
  private final String keyName;

  @Autowired
  public TimerRepositoryImpl(RedisTemplate<String, TimerInfo> redisTemplate) {
    this.redisTemplate = redisTemplate;
    this.hashOperations = this.redisTemplate.opsForHash();
    this.keyName = getKey().name();
  }

  @Override
  public RepoKey getKey() {
    return RepoKey.TIMER;
  }

  @Override
  public Map<UUID, TimerInfo> findAll() {
    return this.hashOperations.entries(this.keyName);
  }

  @Override
  public TimerInfo findById(UUID timerId) {
    return this.hashOperations.get(this.keyName, timerId);
  }

  @Override
  public void add(TimerInfo timerInfo) {
    update(timerInfo.getTimerId(), timerInfo);
  }

  @Override
  public void update(UUID timerId, TimerInfo timerInfo) {
    this.hashOperations.put(this.keyName, timerId, timerInfo);
  }

  @Override
  public void delete(UUID timerId) {
    this.hashOperations.delete(this.keyName, timerId);
  }

}
