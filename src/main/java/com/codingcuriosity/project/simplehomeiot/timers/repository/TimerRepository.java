package com.codingcuriosity.project.simplehomeiot.timers.repository;

import com.codingcuriosity.project.simplehomeiot.common.repository.RedisRepository;
import com.codingcuriosity.project.simplehomeiot.timers.model.TimerInfo;
import java.util.Map;
import java.util.UUID;

public interface TimerRepository extends RedisRepository {

  public abstract Map<UUID, TimerInfo> findAll();

  public abstract TimerInfo findById(UUID timerId);

  public abstract void add(TimerInfo timerInfo);

  public abstract void update(UUID timerId, TimerInfo timerInfo);

  public abstract void delete(UUID timerId);

}
