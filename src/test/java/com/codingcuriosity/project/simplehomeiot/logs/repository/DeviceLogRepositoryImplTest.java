package com.codingcuriosity.project.simplehomeiot.logs.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.codingcuriosity.project.simplehomeiot.common.repository.RepoKey;
import com.codingcuriosity.project.simplehomeiot.logs.model.DeviceLogInfo;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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
public class DeviceLogRepositoryImplTest {

  RedisTemplate<String, DeviceLogInfo> redisTemplate;

  HashOperations<String, UUID, DeviceLogInfo> hashOperationsMock;

  DeviceLogRepository logRepository;

  RepoKey expectedKey = RepoKey.LOGS_DEVICE;

  private Map<UUID, DeviceLogInfo> createObjMap(DeviceLogInfo... logInfos) {
    Map<UUID, DeviceLogInfo> logMap = new HashMap<>();
    for (DeviceLogInfo logInfo : logInfos) {
      logMap.put(logInfo.getLogId(), logInfo);
    }
    return logMap;
  }

  @SuppressWarnings("unchecked")
  @BeforeEach
  void setMocksAndInitRepo() {
    this.redisTemplate = mock(RedisTemplate.class);
    this.hashOperationsMock = mock(HashOperations.class);
    doReturn(this.hashOperationsMock).when(this.redisTemplate).opsForHash();

    this.logRepository = new DeviceLogRepositoryImpl(this.redisTemplate);
  }

  @Test
  @DisplayName("TEST getKey")
  void testGetKey() {
    // Call method
    RepoKey key = this.logRepository.getKey();

    // Validate
    assertEquals(this.expectedKey, key, "Repo Key must match");
  }

  @Test
  @DisplayName("TEST findAll")
  void testFindAll() {
    UUID log1id = UUID.fromString("4f895f64-0314-0111-032c-2b963f789af1");
    UUID log2id = UUID.fromString("5b895f64-5147-4222-b3fc-48963f50ace2");
    DeviceLogInfo log1 = new DeviceLogInfo().logId(log1id);
    DeviceLogInfo log2 = new DeviceLogInfo().logId(log2id);
    Map<UUID, DeviceLogInfo> logMap = createObjMap(log1, log2);

    // Mock(s)
    when(this.hashOperationsMock.entries(eq(this.expectedKey.name()))).thenReturn(logMap);

    // Argument Captors
    ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);

    // Call method
    Map<UUID, DeviceLogInfo> resp = this.logRepository.findAll();

    // Verify that appropriate Redis' method was called
    verify(this.hashOperationsMock, times(1)).entries(arg1.capture());

    // Assertions
    assertEquals(this.expectedKey.name(), arg1.getValue(), "Argument must match.");
    assertNotNull(resp, "Return object is not null.");
    assertEquals(logMap, resp, "Returned object must match.");
  }

  @Test
  @DisplayName("TEST findById")
  void testFindById() {
    UUID log1id = UUID.fromString("4f895f64-0314-0111-032c-2b963f789af1");
    UUID log2id = UUID.fromString("5b895f64-5147-4222-b3fc-48963f50ace2");
    UUID log3id = UUID.fromString("2a895f64-842b-3333-9990-6c963f1b3bd3");
    DeviceLogInfo log1 = new DeviceLogInfo().logId(log1id);
    DeviceLogInfo log2 = new DeviceLogInfo().logId(log2id);
    DeviceLogInfo log3 = new DeviceLogInfo().logId(log3id);

    // Mock(s)
    when(this.hashOperationsMock.get(eq(this.expectedKey.name()), eq(log1id))).thenReturn(log1);
    when(this.hashOperationsMock.get(eq(this.expectedKey.name()), eq(log2id))).thenReturn(log2);
    when(this.hashOperationsMock.get(eq(this.expectedKey.name()), eq(log3id))).thenReturn(log3);

    // Argument Captors
    ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);

    // Call method (round 1)
    DeviceLogInfo retObj = this.logRepository.findById(log1id);

    // Verify that appropriate Redis' method was called
    verify(this.hashOperationsMock, times(1)).get(arg1.capture(), arg2.capture());

    // Assertions (round 1)
    assertEquals(this.expectedKey.name(), arg1.getValue(), "Argument must match.");
    assertEquals(log1id, arg2.getValue(), "Argument must match.");
    assertNotNull(retObj, "Response must not be null.");
    assertEquals(log1, retObj, "Returned object must match.");

    // Call method (round 2)
    retObj = this.logRepository.findById(log2id);

    // Assertions (round 2)
    assertNotNull(retObj, "Response must not be null.");
    assertEquals(log2, retObj, "Returned object must match.");

    // Call method (round 3)
    retObj = this.logRepository.findById(log3id);

    // Assertions (round 3)
    assertNotNull(retObj, "Response must not be null.");
    assertEquals(log3, retObj, "Returned object must match.");
  }

  @Test
  @DisplayName("TEST findManyById")
  void testFindManyById() {
    UUID log1id = UUID.fromString("4f895f64-0314-0111-032c-2b963f789af1");
    UUID log2id = UUID.fromString("5b895f64-5147-4222-b3fc-48963f50ace2");
    UUID log3id = UUID.fromString("2a895f64-842b-3333-9990-6c963f1b3bd3");

    Set<UUID> logIdSet = new HashSet<>();
    logIdSet.add(log1id);
    logIdSet.add(log2id);
    logIdSet.add(log3id);

    // Argument Captors
    ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Set<UUID>> arg2 = ArgumentCaptor.forClass(Set.class);

    // Call method
    this.logRepository.findManyById(logIdSet);

    // Verify that appropriate Redis' method was called
    verify(this.hashOperationsMock, times(1)).multiGet(arg1.capture(), arg2.capture());

    // Assertions (round 1)
    assertEquals(this.expectedKey.name(), arg1.getValue(), "Argument must match.");
    assertEquals(logIdSet, arg2.getValue(), "Argument must match.");
  }

  @Test
  @DisplayName("TEST add")
  void testAdd() {
    UUID log1id = UUID.fromString("4f895f64-0314-0111-032c-2b963f789af1");
    DeviceLogInfo log1 = new DeviceLogInfo().logId(log1id);

    // Argument Captors
    ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceLogInfo> arg3 = ArgumentCaptor.forClass(DeviceLogInfo.class);

    // Call method
    this.logRepository.add(log1);

    // Verify that appropriate Redis' method was called
    verify(this.hashOperationsMock, times(1)).put(arg1.capture(), arg2.capture(), arg3.capture());

    // Assertions
    assertEquals(this.expectedKey.name(), arg1.getValue(), "Argument must match.");
    assertEquals(log1id, arg2.getValue(), "Argument must match.");
    assertEquals(log1, arg3.getValue(), "Argument must match.");
  }

  @Test
  @DisplayName("TEST update")
  void testUpdate() {
    UUID log1id = UUID.fromString("4f895f64-0314-0111-032c-2b963f789af1");
    UUID log2id = UUID.fromString("5b895f64-5147-4222-b3fc-48963f50ace2");
    DeviceLogInfo log1 = new DeviceLogInfo().logId(log1id);

    // Argument Captors
    ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceLogInfo> arg3 = ArgumentCaptor.forClass(DeviceLogInfo.class);

    // Call method
    this.logRepository.update(log2id, log1);

    // Verify that appropriate Redis' method was called
    verify(this.hashOperationsMock, times(1)).put(arg1.capture(), arg2.capture(), arg3.capture());

    // Assertions
    assertEquals(this.expectedKey.name(), arg1.getValue(), "Argument must match.");
    assertEquals(log2id, arg2.getValue(), "Argument must match.");
    assertEquals(log1, arg3.getValue(), "Argument must match.");
  }

  @Test
  @DisplayName("TEST delete")
  void testDelete() {
    UUID log1id = UUID.fromString("4f895f64-0314-0111-032c-2b963f789af1");

    // Argument Captors
    ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<String> arg3 = ArgumentCaptor.forClass(String.class);

    // Call method
    this.logRepository.delete(log1id);

    // Verify that appropriate Redis' method was called
    verify(this.hashOperationsMock, times(1)).delete(arg1.capture(), arg2.capture());

    // Verify that appropriate Redis' method was NOT called
    verify(this.redisTemplate, times(0)).delete(arg3.capture());

    // Assertions
    assertEquals(this.expectedKey.name(), arg1.getValue(), "Argument must match.");
    assertEquals(log1id, arg2.getValue(), "Argument must match.");
  }

  @Test
  @DisplayName("TEST deleteAll")
  void testDeleteAll() {
    // Argument Captors
    ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<String> arg3 = ArgumentCaptor.forClass(String.class);

    // Call method
    this.logRepository.deleteAll();

    // Verify that appropriate Redis' method was NOT called
    verify(this.hashOperationsMock, times(0)).delete(arg1.capture(), arg2.capture());

    // Verify that appropriate Redis' method was called
    verify(this.redisTemplate, times(1)).delete(arg3.capture());

    // Assertions
    assertEquals(this.expectedKey.name(), arg3.getValue(), "Argument must match.");
  }
}
