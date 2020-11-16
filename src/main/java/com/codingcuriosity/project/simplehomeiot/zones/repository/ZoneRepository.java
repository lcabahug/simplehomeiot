package com.codingcuriosity.project.simplehomeiot.zones.repository;

import com.codingcuriosity.project.simplehomeiot.common.repository.RedisRepository;
import com.codingcuriosity.project.simplehomeiot.zones.model.ZoneInfo;
import java.util.Map;
import java.util.UUID;

public interface ZoneRepository extends RedisRepository {

  public abstract Map<UUID, ZoneInfo> findAll();

  public abstract ZoneInfo findById(UUID zoneId);

  public abstract void add(ZoneInfo zoneInfo);

  public abstract void update(UUID zoneId, ZoneInfo zoneInfo);

  public abstract void delete(UUID zoneId);

}
