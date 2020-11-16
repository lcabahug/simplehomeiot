package com.codingcuriosity.project.simplehomeiot.timers.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.codingcuriosity.project.simplehomeiot.common.repository.RepoKey;
import com.codingcuriosity.project.simplehomeiot.timers.model.TimerInfo;
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
public class TimerRepositoryImplTest {

  RedisTemplate<String, TimerInfo> redisTemplate;

  HashOperations<String, UUID, TimerInfo> hashOperationsMock;

  TimerRepository timerRepository;

  RepoKey expectedKey = RepoKey.TIMER;

  private Map<UUID, TimerInfo> createObjMap(TimerInfo... timerInfos) {
    Map<UUID, TimerInfo> timerMap = new HashMap<>();
    for (TimerInfo timerInfo : timerInfos) {
      timerMap.put(timerInfo.getTimerId(), timerInfo);
    }
    return timerMap;
  }

  @SuppressWarnings("unchecked")
  @BeforeEach
  void setMocksAndInitRepo() {
    this.redisTemplate = mock(RedisTemplate.class);
    this.hashOperationsMock = mock(HashOperations.class);
    doReturn(this.hashOperationsMock).when(this.redisTemplate).opsForHash();

    this.timerRepository = new TimerRepositoryImpl(this.redisTemplate);
  }

  @Test
  @DisplayName("TEST getKey")
  void testGetKey() {
    // Call method
    RepoKey key = this.timerRepository.getKey();

    // Validate
    assertEquals(this.expectedKey, key, "Repo Key must match");
  }

  @Test
  @DisplayName("TEST findAll")
  void testFindAll() {
    UUID timer1id = UUID.fromString("4f895f64-0314-0111-032c-2b963f789af1");
    UUID timer2id = UUID.fromString("5b895f64-5147-4222-b3fc-48963f50ace2");
    TimerInfo timer1 = new TimerInfo().timerId(timer1id);
    TimerInfo timer2 = new TimerInfo().timerId(timer2id);
    Map<UUID, TimerInfo> timerMap = createObjMap(timer1, timer2);

    // Mock(s)
    when(this.hashOperationsMock.entries(eq(this.expectedKey.name()))).thenReturn(timerMap);

    // Argument Captors
    ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);

    // Call method
    Map<UUID, TimerInfo> resp = this.timerRepository.findAll();

    // Verify that appropriate Redis' method was called
    verify(this.hashOperationsMock, times(1)).entries(arg1.capture());

    // Assertions
    assertEquals(this.expectedKey.name(), arg1.getValue(), "Argument must match.");
    assertNotNull(resp, "Return object is not null.");
    assertEquals(timerMap, resp, "Returned object must match.");
  }

  @Test
  @DisplayName("TEST findById")
  void testFindById() {
    UUID timer1id = UUID.fromString("4f895f64-0314-0111-032c-2b963f789af1");
    UUID timer2id = UUID.fromString("5b895f64-5147-4222-b3fc-48963f50ace2");
    UUID timer3id = UUID.fromString("2a895f64-842b-3333-9990-6c963f1b3bd3");
    TimerInfo timer1 = new TimerInfo().timerId(timer1id);
    TimerInfo timer2 = new TimerInfo().timerId(timer2id);
    TimerInfo timer3 = new TimerInfo().timerId(timer3id);

    // Mock(s)
    when(this.hashOperationsMock.get(eq(this.expectedKey.name()), eq(timer1id))).thenReturn(timer1);
    when(this.hashOperationsMock.get(eq(this.expectedKey.name()), eq(timer2id))).thenReturn(timer2);
    when(this.hashOperationsMock.get(eq(this.expectedKey.name()), eq(timer3id))).thenReturn(timer3);

    // Argument Captors
    ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);

    // Call method (round 1)
    TimerInfo retObj = this.timerRepository.findById(timer1id);

    // Verify that appropriate Redis' method was called
    verify(this.hashOperationsMock, times(1)).get(arg1.capture(), arg2.capture());

    // Assertions (round 1)
    assertEquals(this.expectedKey.name(), arg1.getValue(), "Argument must match.");
    assertEquals(timer1id, arg2.getValue(), "Argument must match.");
    assertNotNull(retObj, "Response must not be null.");
    assertEquals(timer1, retObj, "Returned object must match.");

    // Call method (round 2)
    retObj = this.timerRepository.findById(timer2id);

    // Assertions (round 2)
    assertNotNull(retObj, "Response must not be null.");
    assertEquals(timer2, retObj, "Returned object must match.");

    // Call method (round 3)
    retObj = this.timerRepository.findById(timer3id);

    // Assertions (round 3)
    assertNotNull(retObj, "Response must not be null.");
    assertEquals(timer3, retObj, "Returned object must match.");
  }

  @Test
  @DisplayName("TEST add")
  void testAdd() {
    UUID timer1id = UUID.fromString("4f895f64-0314-0111-032c-2b963f789af1");
    TimerInfo timer1 = new TimerInfo().timerId(timer1id);

    // Argument Captors
    ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg3 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call method
    this.timerRepository.add(timer1);

    // Verify that appropriate Redis' method was called
    verify(this.hashOperationsMock, times(1)).put(arg1.capture(), arg2.capture(), arg3.capture());

    // Assertions
    assertEquals(this.expectedKey.name(), arg1.getValue(), "Argument must match.");
    assertEquals(timer1id, arg2.getValue(), "Argument must match.");
    assertEquals(timer1, arg3.getValue(), "Argument must match.");
  }

  @Test
  @DisplayName("TEST update")
  void testUpdate() {
    UUID timer1id = UUID.fromString("4f895f64-0314-0111-032c-2b963f789af1");
    UUID timer2id = UUID.fromString("5b895f64-5147-4222-b3fc-48963f50ace2");
    TimerInfo timer1 = new TimerInfo().timerId(timer1id);

    // Argument Captors
    ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg3 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call method
    this.timerRepository.update(timer2id, timer1);

    // Verify that appropriate Redis' method was called
    verify(this.hashOperationsMock, times(1)).put(arg1.capture(), arg2.capture(), arg3.capture());

    // Assertions
    assertEquals(this.expectedKey.name(), arg1.getValue(), "Argument must match.");
    assertEquals(timer2id, arg2.getValue(), "Argument must match.");
    assertEquals(timer1, arg3.getValue(), "Argument must match.");
  }

  @Test
  @DisplayName("TEST delete")
  void testDelete() {
    UUID timer1id = UUID.fromString("4f895f64-0314-0111-032c-2b963f789af1");

    // Argument Captors
    ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    this.timerRepository.delete(timer1id);

    // Verify that appropriate Redis' method was called
    verify(this.hashOperationsMock, times(1)).delete(arg1.capture(), arg2.capture());

    // Assertions
    assertEquals(this.expectedKey.name(), arg1.getValue(), "Argument must match.");
    assertEquals(timer1id, arg2.getValue(), "Argument must match.");
  }
}
