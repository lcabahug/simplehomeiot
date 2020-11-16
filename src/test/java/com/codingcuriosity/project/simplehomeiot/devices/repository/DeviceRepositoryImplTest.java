package com.codingcuriosity.project.simplehomeiot.devices.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.codingcuriosity.project.simplehomeiot.common.repository.RepoKey;
import com.codingcuriosity.project.simplehomeiot.devices.model.DeviceInfo;
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
public class DeviceRepositoryImplTest {

  RedisTemplate<String, DeviceInfo> redisTemplate;

  HashOperations<String, UUID, DeviceInfo> hashOperationsMock;

  DeviceRepository deviceRepository;

  RepoKey expectedKey = RepoKey.DEVICE;

  private Map<UUID, DeviceInfo> createObjMap(DeviceInfo... devInfos) {
    Map<UUID, DeviceInfo> deviceMap = new HashMap<>();
    for (DeviceInfo devInfo : devInfos) {
      deviceMap.put(devInfo.getDeviceId(), devInfo);
    }
    return deviceMap;
  }

  @SuppressWarnings("unchecked")
  @BeforeEach
  void setMocksAndInitRepo() {
    this.redisTemplate = mock(RedisTemplate.class);
    this.hashOperationsMock = mock(HashOperations.class);
    doReturn(this.hashOperationsMock).when(this.redisTemplate).opsForHash();

    this.deviceRepository = new DeviceRepositoryImpl(this.redisTemplate);
  }

  @Test
  @DisplayName("TEST getKey")
  void testGetKey() {
    // Call method
    RepoKey key = this.deviceRepository.getKey();

    // Validate
    assertEquals(this.expectedKey, key, "Repo Key must match");
  }

  @Test
  @DisplayName("TEST findAll")
  void testFindAll() {
    UUID dev1id = UUID.fromString("4f895f64-0314-0111-032c-2b963f789af1");
    UUID dev2id = UUID.fromString("5b895f64-5147-4222-b3fc-48963f50ace2");
    DeviceInfo dev1 = new DeviceInfo().deviceId(dev1id);
    DeviceInfo dev2 = new DeviceInfo().deviceId(dev2id);
    Map<UUID, DeviceInfo> deviceMap = createObjMap(dev1, dev2);

    // Mock(s)
    when(this.hashOperationsMock.entries(eq(this.expectedKey.name()))).thenReturn(deviceMap);

    // Argument Captors
    ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);

    // Call method
    Map<UUID, DeviceInfo> resp = this.deviceRepository.findAll();

    // Verify that appropriate Redis' method was called
    verify(this.hashOperationsMock, times(1)).entries(arg1.capture());

    // Assertions
    assertEquals(this.expectedKey.name(), arg1.getValue(), "Argument must match.");
    assertNotNull(resp, "Return object is not null.");
    assertEquals(deviceMap, resp, "Returned object must match.");
  }

  @Test
  @DisplayName("TEST findById")
  void testFindById() {
    UUID dev1id = UUID.fromString("4f895f64-0314-0111-032c-2b963f789af1");
    UUID dev2id = UUID.fromString("5b895f64-5147-4222-b3fc-48963f50ace2");
    UUID dev3id = UUID.fromString("2a895f64-842b-3333-9990-6c963f1b3bd3");
    DeviceInfo dev1 = new DeviceInfo().deviceId(dev1id);
    DeviceInfo dev2 = new DeviceInfo().deviceId(dev2id);
    DeviceInfo dev3 = new DeviceInfo().deviceId(dev3id);

    // Mock(s)
    when(this.hashOperationsMock.get(eq(this.expectedKey.name()), eq(dev1id))).thenReturn(dev1);
    when(this.hashOperationsMock.get(eq(this.expectedKey.name()), eq(dev2id))).thenReturn(dev2);
    when(this.hashOperationsMock.get(eq(this.expectedKey.name()), eq(dev3id))).thenReturn(dev3);

    // Argument Captors
    ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);

    // Call method (round 1)
    DeviceInfo retObj = this.deviceRepository.findById(dev1id);

    // Verify that appropriate Redis' method was called
    verify(this.hashOperationsMock, times(1)).get(arg1.capture(), arg2.capture());

    // Assertions (round 1)
    assertEquals(this.expectedKey.name(), arg1.getValue(), "Argument must match.");
    assertEquals(dev1id, arg2.getValue(), "Argument must match.");
    assertNotNull(retObj, "Response must not be null.");
    assertEquals(dev1, retObj, "Returned object must match.");

    // Call method (round 2)
    retObj = this.deviceRepository.findById(dev2id);

    // Assertions (round 2)
    assertNotNull(retObj, "Response must not be null.");
    assertEquals(dev2, retObj, "Returned object must match.");

    // Call method (round 3)
    retObj = this.deviceRepository.findById(dev3id);

    // Assertions (round 3)
    assertNotNull(retObj, "Response must not be null.");
    assertEquals(dev3, retObj, "Returned object must match.");
  }

  @Test
  @DisplayName("TEST findManyById")
  void testFindManyById() {
    UUID dev1id = UUID.fromString("4f895f64-0314-0111-032c-2b963f789af1");
    UUID dev2id = UUID.fromString("5b895f64-5147-4222-b3fc-48963f50ace2");
    UUID dev3id = UUID.fromString("2a895f64-842b-3333-9990-6c963f1b3bd3");

    Set<UUID> devIdSet = new HashSet<>();
    devIdSet.add(dev1id);
    devIdSet.add(dev2id);
    devIdSet.add(dev3id);

    // Argument Captors
    ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
    @SuppressWarnings("unchecked")
    ArgumentCaptor<Set<UUID>> arg2 = ArgumentCaptor.forClass(Set.class);

    // Call method
    this.deviceRepository.findManyById(devIdSet);

    // Verify that appropriate Redis' method was called
    verify(this.hashOperationsMock, times(1)).multiGet(arg1.capture(), arg2.capture());

    // Assertions (round 1)
    assertEquals(this.expectedKey.name(), arg1.getValue(), "Argument must match.");
    assertEquals(devIdSet, arg2.getValue(), "Argument must match.");
  }

  @Test
  @DisplayName("TEST add")
  void testAdd() {
    UUID dev1id = UUID.fromString("4f895f64-0314-0111-032c-2b963f789af1");
    DeviceInfo dev1 = new DeviceInfo().deviceId(dev1id);

    // Argument Captors
    ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg3 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call method
    this.deviceRepository.add(dev1);

    // Verify that appropriate Redis' method was called
    verify(this.hashOperationsMock, times(1)).put(arg1.capture(), arg2.capture(), arg3.capture());

    // Assertions
    assertEquals(this.expectedKey.name(), arg1.getValue(), "Argument must match.");
    assertEquals(dev1id, arg2.getValue(), "Argument must match.");
    assertEquals(dev1, arg3.getValue(), "Argument must match.");
  }

  @Test
  @DisplayName("TEST update")
  void testUpdate() {
    UUID dev1id = UUID.fromString("4f895f64-0314-0111-032c-2b963f789af1");
    UUID dev2id = UUID.fromString("5b895f64-5147-4222-b3fc-48963f50ace2");
    DeviceInfo dev1 = new DeviceInfo().deviceId(dev1id);

    // Argument Captors
    ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg3 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call method
    this.deviceRepository.update(dev2id, dev1);

    // Verify that appropriate Redis' method was called
    verify(this.hashOperationsMock, times(1)).put(arg1.capture(), arg2.capture(), arg3.capture());

    // Assertions
    assertEquals(this.expectedKey.name(), arg1.getValue(), "Argument must match.");
    assertEquals(dev2id, arg2.getValue(), "Argument must match.");
    assertEquals(dev1, arg3.getValue(), "Argument must match.");
  }

  @Test
  @DisplayName("TEST delete")
  void testDelete() {
    UUID dev1id = UUID.fromString("4f895f64-0314-0111-032c-2b963f789af1");

    // Argument Captors
    ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    this.deviceRepository.delete(dev1id);

    // Verify that appropriate Redis' method was called
    verify(this.hashOperationsMock, times(1)).delete(arg1.capture(), arg2.capture());

    // Assertions
    assertEquals(this.expectedKey.name(), arg1.getValue(), "Argument must match.");
    assertEquals(dev1id, arg2.getValue(), "Argument must match.");
  }
}
