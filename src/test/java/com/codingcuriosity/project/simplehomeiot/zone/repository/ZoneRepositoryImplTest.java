package com.codingcuriosity.project.simplehomeiot.zone.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.codingcuriosity.project.simplehomeiot.common.repository.RepoKey;
import com.codingcuriosity.project.simplehomeiot.zones.model.ZoneInfo;
import com.codingcuriosity.project.simplehomeiot.zones.repository.ZoneRepository;
import com.codingcuriosity.project.simplehomeiot.zones.repository.ZoneRepositoryImpl;
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
public class ZoneRepositoryImplTest {

  RedisTemplate<String, ZoneInfo> redisTemplate;

  HashOperations<String, UUID, ZoneInfo> hashOperationsMock;

  ZoneRepository zoneRepository;

  RepoKey expectedKey = RepoKey.ZONE;

  private Map<UUID, ZoneInfo> createObjMap(ZoneInfo... zoneInfos) {
    Map<UUID, ZoneInfo> zoneMap = new HashMap<>();
    for (ZoneInfo zoneInfo : zoneInfos) {
      zoneMap.put(zoneInfo.getZoneId(), zoneInfo);
    }
    return zoneMap;
  }

  @SuppressWarnings("unchecked")
  @BeforeEach
  void setMocksAndInitRepo() {
    this.redisTemplate = mock(RedisTemplate.class);
    this.hashOperationsMock = mock(HashOperations.class);
    doReturn(this.hashOperationsMock).when(this.redisTemplate).opsForHash();

    this.zoneRepository = new ZoneRepositoryImpl(this.redisTemplate);
  }

  @Test
  @DisplayName("TEST getKey")
  void testGetKey() {
    // Call method
    RepoKey key = this.zoneRepository.getKey();

    // Validate
    assertEquals(this.expectedKey, key, "Repo Key must match");
  }

  @Test
  @DisplayName("TEST findAll")
  void testFindAll() {
    UUID zone1id = UUID.fromString("4f895f64-0314-0111-032c-2b963f789af1");
    UUID zone2id = UUID.fromString("5b895f64-5147-4222-b3fc-48963f50ace2");
    ZoneInfo zone1 = new ZoneInfo().zoneId(zone1id);
    ZoneInfo zone2 = new ZoneInfo().zoneId(zone2id);
    Map<UUID, ZoneInfo> zoneMap = createObjMap(zone1, zone2);

    // Mock(s)
    when(this.hashOperationsMock.entries(eq(this.expectedKey.name()))).thenReturn(zoneMap);

    // Argument Captors
    ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);

    // Call method
    Map<UUID, ZoneInfo> resp = this.zoneRepository.findAll();

    // Verify that appropriate Redis' method was called
    verify(this.hashOperationsMock, times(1)).entries(arg1.capture());

    // Assertions
    assertEquals(this.expectedKey.name(), arg1.getValue(), "Argument must match.");
    assertNotNull(resp, "Return object is not null.");
    assertEquals(zoneMap, resp, "Returned object must match.");
  }

  @Test
  @DisplayName("TEST findById")
  void testFindById() {
    UUID zone1id = UUID.fromString("4f895f64-0314-0111-032c-2b963f789af1");
    UUID zone2id = UUID.fromString("5b895f64-5147-4222-b3fc-48963f50ace2");
    UUID zone3id = UUID.fromString("2a895f64-842b-3333-9990-6c963f1b3bd3");
    ZoneInfo zone1 = new ZoneInfo().zoneId(zone1id);
    ZoneInfo zone2 = new ZoneInfo().zoneId(zone2id);
    ZoneInfo zone3 = new ZoneInfo().zoneId(zone3id);

    // Mock(s)
    when(this.hashOperationsMock.get(eq(this.expectedKey.name()), eq(zone1id))).thenReturn(zone1);
    when(this.hashOperationsMock.get(eq(this.expectedKey.name()), eq(zone2id))).thenReturn(zone2);
    when(this.hashOperationsMock.get(eq(this.expectedKey.name()), eq(zone3id))).thenReturn(zone3);

    // Argument Captors
    ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);

    // Call method (round 1)
    ZoneInfo retObj = this.zoneRepository.findById(zone1id);

    // Verify that appropriate Redis' method was called
    verify(this.hashOperationsMock, times(1)).get(arg1.capture(), arg2.capture());

    // Assertions (round 1)
    assertEquals(this.expectedKey.name(), arg1.getValue(), "Argument must match.");
    assertEquals(zone1id, arg2.getValue(), "Argument must match.");
    assertNotNull(retObj, "Response must not be null.");
    assertEquals(zone1, retObj, "Returned object must match.");

    // Call method (round 2)
    retObj = this.zoneRepository.findById(zone2id);

    // Assertions (round 2)
    assertNotNull(retObj, "Response must not be null.");
    assertEquals(zone2, retObj, "Returned object must match.");

    // Call method (round 3)
    retObj = this.zoneRepository.findById(zone3id);

    // Assertions (round 3)
    assertNotNull(retObj, "Response must not be null.");
    assertEquals(zone3, retObj, "Returned object must match.");
  }

  @Test
  @DisplayName("TEST add")
  void testAdd() {
    UUID zone1id = UUID.fromString("4f895f64-0314-0111-032c-2b963f789af1");
    ZoneInfo zone1 = new ZoneInfo().zoneId(zone1id);

    // Argument Captors
    ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ZoneInfo> arg3 = ArgumentCaptor.forClass(ZoneInfo.class);

    // Call method
    this.zoneRepository.add(zone1);

    // Verify that appropriate Redis' method was called
    verify(this.hashOperationsMock, times(1)).put(arg1.capture(), arg2.capture(), arg3.capture());

    // Assertions
    assertEquals(this.expectedKey.name(), arg1.getValue(), "Argument must match.");
    assertEquals(zone1id, arg2.getValue(), "Argument must match.");
    assertEquals(zone1, arg3.getValue(), "Argument must match.");
  }

  @Test
  @DisplayName("TEST update")
  void testUpdate() {
    UUID zone1id = UUID.fromString("4f895f64-0314-0111-032c-2b963f789af1");
    UUID zone2id = UUID.fromString("5b895f64-5147-4222-b3fc-48963f50ace2");
    ZoneInfo zone1 = new ZoneInfo().zoneId(zone1id);

    // Argument Captors
    ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ZoneInfo> arg3 = ArgumentCaptor.forClass(ZoneInfo.class);

    // Call method
    this.zoneRepository.update(zone2id, zone1);

    // Verify that appropriate Redis' method was called
    verify(this.hashOperationsMock, times(1)).put(arg1.capture(), arg2.capture(), arg3.capture());

    // Assertions
    assertEquals(this.expectedKey.name(), arg1.getValue(), "Argument must match.");
    assertEquals(zone2id, arg2.getValue(), "Argument must match.");
    assertEquals(zone1, arg3.getValue(), "Argument must match.");
  }

  @Test
  @DisplayName("TEST delete")
  void testDelete() {
    UUID zone1id = UUID.fromString("4f895f64-0314-0111-032c-2b963f789af1");

    // Argument Captors
    ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    this.zoneRepository.delete(zone1id);

    // Verify that appropriate Redis' method was called
    verify(this.hashOperationsMock, times(1)).delete(arg1.capture(), arg2.capture());

    // Assertions
    assertEquals(this.expectedKey.name(), arg1.getValue(), "Argument must match.");
    assertEquals(zone1id, arg2.getValue(), "Argument must match.");
  }
}
