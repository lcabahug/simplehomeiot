package com.codingcuriosity.project.simplehomeiot.groups.repository;

import com.codingcuriosity.project.simplehomeiot.common.repository.RedisRepository;
import com.codingcuriosity.project.simplehomeiot.groups.model.GroupRawInfo;
import java.util.Map;
import java.util.UUID;

public interface GroupRepository extends RedisRepository {

  public abstract Map<UUID, GroupRawInfo> findAll();

  public abstract GroupRawInfo findById(UUID groupId);

  public void add(GroupRawInfo groupInfo);

  public void update(UUID groupId, GroupRawInfo groupInfo);

  public void delete(UUID groupId);

}
