package com.codingcuriosity.project.simplehomeiot.zones.repository;

import com.codingcuriosity.project.simplehomeiot.common.repository.RepoKey;
import com.codingcuriosity.project.simplehomeiot.zones.model.ZoneInfo;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ZoneRepositoryImpl implements ZoneRepository {

  private final RedisTemplate<String, ZoneInfo> redisTemplate;
  private final HashOperations<String, UUID, ZoneInfo> hashOperations;
  private final String keyName;

  @Autowired
  public ZoneRepositoryImpl(RedisTemplate<String, ZoneInfo> redisTemplate) {
    this.redisTemplate = redisTemplate;
    this.hashOperations = this.redisTemplate.opsForHash();
    this.keyName = getKey().name();
  }

  @Override
  public RepoKey getKey() {
    return RepoKey.ZONE;
  }

  @Override
  public Map<UUID, ZoneInfo> findAll() {
    return this.hashOperations.entries(this.keyName);
  }

  @Override
  public ZoneInfo findById(UUID zoneId) {
    return this.hashOperations.get(this.keyName, zoneId);
  }

  @Override
  public void add(ZoneInfo zoneInfo) {
    update(zoneInfo.getZoneId(), zoneInfo);
  }

  @Override
  public void update(UUID zoneId, ZoneInfo zoneInfo) {
    this.hashOperations.put(this.keyName, zoneId, zoneInfo);
  }

  @Override
  public void delete(UUID zoneId) {
    this.hashOperations.delete(this.keyName, zoneId);
  }

}
