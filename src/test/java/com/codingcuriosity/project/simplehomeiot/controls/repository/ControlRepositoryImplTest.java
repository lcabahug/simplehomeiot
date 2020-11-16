package com.codingcuriosity.project.simplehomeiot.controls.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.codingcuriosity.project.simplehomeiot.common.repository.RepoKey;
import com.codingcuriosity.project.simplehomeiot.controls.model.DeviceControlInfo;
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
public class ControlRepositoryImplTest {

  RedisTemplate<String, DeviceControlInfo> redisTemplate;

  HashOperations<String, UUID, DeviceControlInfo> hashOperationsMock;

  ControlRepository controlRepository;

  RepoKey expectedKey = RepoKey.CONTROL_DEVICE;

  private Map<UUID, DeviceControlInfo> createObjMap(DeviceControlInfo... devInfos) {
    Map<UUID, DeviceControlInfo> deviceMap = new HashMap<>();
    for (DeviceControlInfo devInfo : devInfos) {
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

    this.controlRepository = new ControlRepositoryImpl(this.redisTemplate);
  }

  @Test
  @DisplayName("TEST getKey")
  void testGetKey() {
    // Call method
    RepoKey key = this.controlRepository.getKey();

    // Validate
    assertEquals(this.expectedKey, key, "Repo Key must match");
  }

  @Test
  @DisplayName("TEST findAll")
  void testFindAll() {
    UUID contr1id = UUID.fromString("5f895f64-0314-0111-032c-2b963f789af1");
    UUID contr2id = UUID.fromString("6b895f64-5147-4222-b3fc-48963f50ace2");
    DeviceControlInfo contr1 = new DeviceControlInfo().controlId(contr1id);
    DeviceControlInfo contr2 = new DeviceControlInfo().controlId(contr2id);
    Map<UUID, DeviceControlInfo> controlMap = createObjMap(contr1, contr2);

    // Mock(s)
    when(this.hashOperationsMock.entries(eq(this.expectedKey.name()))).thenReturn(controlMap);

    // Argument Captors
    ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);

    // Call method
    Map<UUID, DeviceControlInfo> resp = this.controlRepository.findAll();

    // Verify that appropriate Redis' method was called
    verify(this.hashOperationsMock, times(1)).entries(arg1.capture());

    // Assertions
    assertEquals(this.expectedKey.name(), arg1.getValue(), "Argument must match.");
    assertNotNull(resp, "Return object is not null.");
    assertEquals(controlMap, resp, "Returned object must match.");
  }

  @Test
  @DisplayName("TEST findById")
  void testFindById() {
    UUID contr1id = UUID.fromString("5f895f64-0314-0111-032c-2b963f789af1");
    UUID contr2id = UUID.fromString("6b895f64-5147-4222-b3fc-48963f50ace2");
    UUID contr3id = UUID.fromString("7a895f64-842b-3333-9990-6c963f1b3bd3");
    DeviceControlInfo contr1 = new DeviceControlInfo().controlId(contr1id);
    DeviceControlInfo contr2 = new DeviceControlInfo().controlId(contr2id);
    DeviceControlInfo contr3 = new DeviceControlInfo().controlId(contr3id);

    // Mock(s)
    when(this.hashOperationsMock.get(eq(this.expectedKey.name()), eq(contr1id))).thenReturn(contr1);
    when(this.hashOperationsMock.get(eq(this.expectedKey.name()), eq(contr2id))).thenReturn(contr2);
    when(this.hashOperationsMock.get(eq(this.expectedKey.name()), eq(contr3id))).thenReturn(contr3);

    // Argument Captors
    ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);

    // Call method (round 1)
    DeviceControlInfo retObj = this.controlRepository.findById(contr1id);

    // Verify that appropriate Redis' method was called
    verify(this.hashOperationsMock, times(1)).get(arg1.capture(), arg2.capture());

    // Assertions (round 1)
    assertEquals(this.expectedKey.name(), arg1.getValue(), "Argument must match.");
    assertEquals(contr1id, arg2.getValue(), "Argument must match.");
    assertNotNull(retObj, "Response must not be null.");
    assertEquals(contr1, retObj, "Returned object must match.");

    // Call method (round 2)
    retObj = this.controlRepository.findById(contr2id);

    // Assertions (round 2)
    assertNotNull(retObj, "Response must not be null.");
    assertEquals(contr2, retObj, "Returned object must match.");

    // Call method (round 3)
    retObj = this.controlRepository.findById(contr3id);

    // Assertions (round 3)
    assertNotNull(retObj, "Response must not be null.");
    assertEquals(contr3, retObj, "Returned object must match.");
  }

  @Test
  @DisplayName("TEST findManyById")
  void testFindManyById() {
    UUID contr1id = UUID.fromString("5f895f64-0314-0111-032c-2b963f789af1");
    UUID contr2id = UUID.fromString("6b895f64-5147-4222-b3fc-48963f50ace2");
    UUID contr3id = UUID.fromString("7a895f64-842b-3333-9990-6c963f1b3bd3");

    Set<UUID> contrIdSet = new HashSet<>();
    contrIdSet.add(contr1id);
    contrIdSet.add(contr2id);
    contrIdSet.add(contr3id);

    // Argument Captors
    ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
    @SuppressWarnings("unchecked")
    ArgumentCaptor<Set<UUID>> arg2 = ArgumentCaptor.forClass(Set.class);

    // Call method
    this.controlRepository.findManyById(contrIdSet);

    // Verify that appropriate Redis' method was called
    verify(this.hashOperationsMock, times(1)).multiGet(arg1.capture(), arg2.capture());

    // Assertions (round 1)
    assertEquals(this.expectedKey.name(), arg1.getValue(), "Argument must match.");
    assertEquals(contrIdSet, arg2.getValue(), "Argument must match.");
  }

  @Test
  @DisplayName("TEST add")
  void testAdd() {
    UUID contr1id = UUID.fromString("4f895f64-0314-0111-032c-2b963f789af1");
    DeviceControlInfo contr1 = new DeviceControlInfo().controlId(contr1id);

    // Argument Captors
    ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceControlInfo> arg3 = ArgumentCaptor.forClass(DeviceControlInfo.class);

    // Call method
    this.controlRepository.add(contr1);

    // Verify that appropriate Redis' method was called
    verify(this.hashOperationsMock, times(1)).put(arg1.capture(), arg2.capture(), arg3.capture());

    // Assertions
    assertEquals(this.expectedKey.name(), arg1.getValue(), "Argument must match.");
    assertEquals(contr1id, arg2.getValue(), "Argument must match.");
    assertEquals(contr1, arg3.getValue(), "Argument must match.");
  }

  @Test
  @DisplayName("TEST update")
  void testUpdate() {
    UUID contr1id = UUID.fromString("4f895f64-0314-0111-032c-2b963f789af1");
    UUID dev2id = UUID.fromString("5b895f64-5147-4222-b3fc-48963f50ace2");
    DeviceControlInfo contr1 = new DeviceControlInfo().deviceId(contr1id);

    // Argument Captors
    ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceControlInfo> arg3 = ArgumentCaptor.forClass(DeviceControlInfo.class);

    // Call method
    this.controlRepository.update(dev2id, contr1);

    // Verify that appropriate Redis' method was called
    verify(this.hashOperationsMock, times(1)).put(arg1.capture(), arg2.capture(), arg3.capture());

    // Assertions
    assertEquals(this.expectedKey.name(), arg1.getValue(), "Argument must match.");
    assertEquals(dev2id, arg2.getValue(), "Argument must match.");
    assertEquals(contr1, arg3.getValue(), "Argument must match.");
  }

  @Test
  @DisplayName("TEST delete")
  void testDelete() {
    UUID contr1id = UUID.fromString("4f895f64-0314-0111-032c-2b963f789af1");

    // Argument Captors
    ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    this.controlRepository.delete(contr1id);

    // Verify that appropriate Redis' method was called
    verify(this.hashOperationsMock, times(1)).delete(arg1.capture(), arg2.capture());

    // Assertions
    assertEquals(this.expectedKey.name(), arg1.getValue(), "Argument must match.");
    assertEquals(contr1id, arg2.getValue(), "Argument must match.");
  }
}
