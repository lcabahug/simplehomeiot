package com.codingcuriosity.project.simplehomeiot.groups.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.codingcuriosity.project.simplehomeiot.common.repository.RepoKey;
import com.codingcuriosity.project.simplehomeiot.groups.model.GroupRawInfo;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class GroupRepositoryImplTest {

  RedisTemplate<String, GroupRawInfo> redisTemplate;

  HashOperations<String, UUID, GroupRawInfo> hashOperationsMock;

  GroupRepository groupRepository;

  RepoKey expectedKey = RepoKey.DEVICE_GROUP;

  private Map<UUID, GroupRawInfo> createObjMap(GroupRawInfo... devInfos) {
    Map<UUID, GroupRawInfo> groupMap = new HashMap<>();
    for (GroupRawInfo devInfo : devInfos) {
      groupMap.put(devInfo.getGroupId(), devInfo);
    }
    return groupMap;
  }

  @SuppressWarnings("unchecked")
  @BeforeEach
  void setMocksAndInitRepo() {
    this.redisTemplate = mock(RedisTemplate.class);
    this.hashOperationsMock = mock(HashOperations.class);
    doReturn(this.hashOperationsMock).when(this.redisTemplate).opsForHash();

    this.groupRepository = new GroupRepositoryImpl(this.redisTemplate);
  }

  @Test
  @DisplayName("TEST getKey")
  void testGetKey() {
    // Call method
    RepoKey key = this.groupRepository.getKey();

    // Validate
    assertEquals(this.expectedKey, key, "Repo Key must match");
  }

  @Test
  @DisplayName("TEST findAll")
  void testFindAll() {
    UUID group1id = UUID.fromString("4f895f64-0314-0111-032c-2b963f789af1");
    UUID group2id = UUID.fromString("5b895f64-5147-4222-b3fc-48963f50ace2");
    GroupRawInfo group1 = new GroupRawInfo().groupId(group1id);
    GroupRawInfo group2 = new GroupRawInfo().groupId(group2id);
    Map<UUID, GroupRawInfo> groupMap = createObjMap(group1, group2);

    // Mock(s)
    when(this.hashOperationsMock.entries(eq(this.expectedKey.name()))).thenReturn(groupMap);

    // Argument Captors
    ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);

    // Call method
    Map<UUID, GroupRawInfo> resp = this.groupRepository.findAll();

    // Verify that appropriate Redis' method was called
    verify(this.hashOperationsMock, times(1)).entries(arg1.capture());

    // Assertions
    assertEquals(this.expectedKey.name(), arg1.getValue(), "Argument must match.");
    assertNotNull(resp, "Return object is not null.");
    assertEquals(groupMap, resp, "Returned object must match.");
  }

  @Test
  @DisplayName("TEST findById")
  void testFindById() {
    UUID group1id = UUID.fromString("4f895f64-0314-0111-032c-2b963f789af1");
    UUID group2id = UUID.fromString("5b895f64-5147-4222-b3fc-48963f50ace2");
    UUID dev3id = UUID.fromString("2a895f64-842b-3333-9990-6c963f1b3bd3");
    GroupRawInfo group1 = new GroupRawInfo().groupId(group1id);
    GroupRawInfo group2 = new GroupRawInfo().groupId(group2id);
    GroupRawInfo dev3 = new GroupRawInfo().groupId(dev3id);

    // Mock(s)
    when(this.hashOperationsMock.get(eq(this.expectedKey.name()), eq(group1id))).thenReturn(group1);
    when(this.hashOperationsMock.get(eq(this.expectedKey.name()), eq(group2id))).thenReturn(group2);
    when(this.hashOperationsMock.get(eq(this.expectedKey.name()), eq(dev3id))).thenReturn(dev3);

    // Argument Captors
    ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);

    // Call method (round 1)
    GroupRawInfo retObj = this.groupRepository.findById(group1id);

    // Verify that appropriate Redis' method was called
    verify(this.hashOperationsMock, times(1)).get(arg1.capture(), arg2.capture());

    // Assertions (round 1)
    assertEquals(this.expectedKey.name(), arg1.getValue(), "Argument must match.");
    assertEquals(group1id, arg2.getValue(), "Argument must match.");
    assertNotNull(retObj, "Response must not be null.");
    assertEquals(group1, retObj, "Returned object must match.");

    // Call method (round 2)
    retObj = this.groupRepository.findById(group2id);

    // Assertions (round 2)
    assertNotNull(retObj, "Response must not be null.");
    assertEquals(group2, retObj, "Returned object must match.");

    // Call method (round 3)
    retObj = this.groupRepository.findById(dev3id);

    // Assertions (round 3)
    assertNotNull(retObj, "Response must not be null.");
    assertEquals(dev3, retObj, "Returned object must match.");
  }

  @Test
  @DisplayName("TEST add")
  void testAdd() {
    UUID group1id = UUID.fromString("4f895f64-0314-0111-032c-2b963f789af1");
    GroupRawInfo group1 = new GroupRawInfo().groupId(group1id);

    // Argument Captors
    ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<GroupRawInfo> arg3 = ArgumentCaptor.forClass(GroupRawInfo.class);

    // Call method
    this.groupRepository.add(group1);

    // Verify that appropriate Redis' method was called
    verify(this.hashOperationsMock, times(1)).put(arg1.capture(), arg2.capture(), arg3.capture());

    // Assertions
    assertEquals(this.expectedKey.name(), arg1.getValue(), "Argument must match.");
    assertEquals(group1id, arg2.getValue(), "Argument must match.");
    assertEquals(group1, arg3.getValue(), "Argument must match.");
  }

  @Test
  @DisplayName("TEST update")
  void testUpdate() {
    UUID group1id = UUID.fromString("4f895f64-0314-0111-032c-2b963f789af1");
    UUID group2id = UUID.fromString("5b895f64-5147-4222-b3fc-48963f50ace2");
    GroupRawInfo group1 = new GroupRawInfo().groupId(group1id);

    // Argument Captors
    ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<GroupRawInfo> arg3 = ArgumentCaptor.forClass(GroupRawInfo.class);

    // Call method
    this.groupRepository.update(group2id, group1);

    // Verify that appropriate Redis' method was called
    verify(this.hashOperationsMock, times(1)).put(arg1.capture(), arg2.capture(), arg3.capture());

    // Assertions
    assertEquals(this.expectedKey.name(), arg1.getValue(), "Argument must match.");
    assertEquals(group2id, arg2.getValue(), "Argument must match.");
    assertEquals(group1, arg3.getValue(), "Argument must match.");
  }

  @Test
  @DisplayName("TEST delete")
  void testDelete() {
    UUID group1id = UUID.fromString("4f895f64-0314-0111-032c-2b963f789af1");

    // Argument Captors
    ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    this.groupRepository.delete(group1id);

    // Verify that appropriate Redis' method was called
    verify(this.hashOperationsMock, times(1)).delete(arg1.capture(), arg2.capture());

    // Assertions
    assertEquals(this.expectedKey.name(), arg1.getValue(), "Argument must match.");
    assertEquals(group1id, arg2.getValue(), "Argument must match.");
  }
}
