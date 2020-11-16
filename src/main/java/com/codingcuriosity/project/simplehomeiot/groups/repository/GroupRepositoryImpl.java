package com.codingcuriosity.project.simplehomeiot.groups.repository;

import com.codingcuriosity.project.simplehomeiot.common.repository.RepoKey;
import com.codingcuriosity.project.simplehomeiot.groups.model.GroupRawInfo;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GroupRepositoryImpl implements GroupRepository {

  private final RedisTemplate<String, GroupRawInfo> redisTemplate;
  private final HashOperations<String, UUID, GroupRawInfo> hashOperations;
  private final String keyName;

  @Autowired
  public GroupRepositoryImpl(RedisTemplate<String, GroupRawInfo> redisTemplate) {
    this.redisTemplate = redisTemplate;
    this.hashOperations = this.redisTemplate.opsForHash();
    this.keyName = getKey().name();
  }

  @Override
  public RepoKey getKey() {
    return RepoKey.DEVICE_GROUP;
  }

  @Override
  public Map<UUID, GroupRawInfo> findAll() {
    return this.hashOperations.entries(this.keyName);
  }

  @Override
  public GroupRawInfo findById(UUID groupId) {
    return this.hashOperations.get(this.keyName, groupId);
  }

  @Override
  public void add(GroupRawInfo groupInfo) {
    update(groupInfo.getGroupId(), groupInfo);
  }

  @Override
  public void update(UUID groupId, GroupRawInfo groupInfo) {
    this.hashOperations.put(this.keyName, groupId, groupInfo);
  }

  @Override
  public void delete(UUID groupId) {
    this.hashOperations.delete(this.keyName, groupId);
  }

}
