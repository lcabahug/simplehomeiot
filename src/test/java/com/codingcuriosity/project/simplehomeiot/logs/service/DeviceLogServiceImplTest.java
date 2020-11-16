package com.codingcuriosity.project.simplehomeiot.logs.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonGetListResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonOperResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.ErrorType;
import com.codingcuriosity.project.simplehomeiot.devices.model.DeviceInfo;
import com.codingcuriosity.project.simplehomeiot.devices.repository.DeviceRepository;
import com.codingcuriosity.project.simplehomeiot.logs.model.CommonLogLevel;
import com.codingcuriosity.project.simplehomeiot.logs.model.DeviceLogInfo;
import com.codingcuriosity.project.simplehomeiot.logs.repository.ControllerLogRepository;
import com.codingcuriosity.project.simplehomeiot.logs.repository.DeviceLogRepository;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class DeviceLogServiceImplTest {

  @Autowired
  DeviceLogService deviceLogService;

  @MockBean
  DeviceLogRepository deviceLogRepository;

  @MockBean
  DeviceRepository deviceRepository;

  @MockBean
  ControllerLogRepository controllerLogRepository;

  @Test
  @DisplayName("TEST getLogRepo")
  void testGetLogRepo() {

    // Call Service
    ControllerLogRepository logRepository = this.deviceLogService.getLogRepo();

    // Assertions
    assertEquals(this.controllerLogRepository, logRepository, "LogRepository must be identical.");
  }

  @Test
  @DisplayName("TEST getLogInfo (all parameters = null)")
  void testGetLogInfo() {
    Integer skip = null;
    Integer limit = null;

    UUID deviceId1 = UUID.fromString("77777777-7777-1111-032c-2b963f789af1");
    UUID deviceId2 = UUID.fromString("77777777-7777-2222-032c-2b963f789af1");
    UUID devLog1Id = UUID.fromString("4f985f64-0314-0361-032c-2b963f789af1");
    UUID devLog2Id = UUID.fromString("5b785f64-5147-4013-b3fc-48963f50ace2");
    UUID devLog3Id = UUID.fromString("2a085f64-842b-3102-9990-6c963f1b3bd3");
    OffsetDateTime devLog1time = OffsetDateTime.now().minusDays(2);
    OffsetDateTime devLog2time = OffsetDateTime.now().minusDays(3);
    OffsetDateTime devLog3time = OffsetDateTime.now();
    CommonLogLevel devLog1level = CommonLogLevel.CRITICAL;
    CommonLogLevel devLog2level = CommonLogLevel.WARNING;
    CommonLogLevel devLog3level = CommonLogLevel.DEBUG;
    String devLog1title = "A fatal error occurred.";
    String devLog2title = "Warning. Something is misconfigured.";
    String devLog3title = "value check: deviceInfo = null";
    String devLog1msg = "NullPointerException on ...test";
    String devLog2msg = "Multiple port values are present.";
    String devLog3msg = "deviceInfo = null";
    DeviceLogInfo devLog1 = new DeviceLogInfo() //
        .logId(devLog1Id) //
        .deviceId(deviceId1) //
        .timestamp(devLog1time) //
        .level(devLog1level) //
        .title(devLog1title) //
        .message(devLog1msg);
    DeviceLogInfo devLog2 = new DeviceLogInfo() //
        .logId(devLog2Id) //
        .deviceId(deviceId1) //
        .timestamp(devLog2time) //
        .level(devLog2level) //
        .title(devLog2title) //
        .message(devLog2msg);
    DeviceLogInfo devLog3 = new DeviceLogInfo() //
        .logId(devLog3Id) //
        .deviceId(deviceId2) //
        .timestamp(devLog3time) //
        .level(devLog3level) //
        .title(devLog3title) //
        .message(devLog3msg);

    Map<UUID, DeviceLogInfo> logMap = new HashMap<>();
    logMap.put(devLog1Id, devLog1);
    logMap.put(devLog2Id, devLog2);
    logMap.put(devLog3Id, devLog3);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(logMap).when(this.deviceLogRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceLogInfo> retListResp = deviceLogService.getLogInfo(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.deviceLogRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be sorted by timestamp ascending.", retListResp.getObjList(),
        contains(devLog2, devLog1, devLog3));
    assertEquals(devLog2Id, retListResp.getObjList().get(0).getLogId(),
        "1st entry's UUID must match log 2's");
    assertEquals(deviceId1, retListResp.getObjList().get(0).getDeviceId(),
        "1st entry's deviceId must match device 1's");
    assertEquals(devLog2time, retListResp.getObjList().get(0).getTimestamp(),
        "1st entry's timestamp must match log 2's");
    assertEquals(devLog2level, retListResp.getObjList().get(0).getLevel(),
        "1st entry's level must match log 2's");
    assertEquals(devLog2title, retListResp.getObjList().get(0).getTitle(),
        "1st entry's title must match log 2's");
    assertEquals(devLog2msg, retListResp.getObjList().get(0).getMessage(),
        "1st entry's message must match log 2's");
    assertEquals(devLog1Id, retListResp.getObjList().get(1).getLogId(),
        "2nd entry's UUID must match log 1's");
    assertEquals(deviceId1, retListResp.getObjList().get(1).getDeviceId(),
        "2nd entry's deviceId must match device 1's");
    assertEquals(devLog1time, retListResp.getObjList().get(1).getTimestamp(),
        "2nd entry's timestamp must match log 1's");
    assertEquals(devLog1level, retListResp.getObjList().get(1).getLevel(),
        "2nd entry's level must match log 1's");
    assertEquals(devLog1title, retListResp.getObjList().get(1).getTitle(),
        "2nd entry's title must match log 1's");
    assertEquals(devLog1msg, retListResp.getObjList().get(1).getMessage(),
        "2nd entry's message must match log 1's");
    assertEquals(devLog3Id, retListResp.getObjList().get(2).getLogId(),
        "3rd entry's UUID must match log 3's");
    assertEquals(deviceId2, retListResp.getObjList().get(2).getDeviceId(),
        "3rd entry's deviceId must match device 2's");
    assertEquals(devLog3time, retListResp.getObjList().get(2).getTimestamp(),
        "3rd entry's timestamp must match log 3's");
    assertEquals(devLog3level, retListResp.getObjList().get(2).getLevel(),
        "3rd entry's level must match log 3's");
    assertEquals(devLog3title, retListResp.getObjList().get(2).getTitle(),
        "3rd entry's title must match log 3's");
    assertEquals(devLog3msg, retListResp.getObjList().get(2).getMessage(),
        "3rd entry's message must match log 3's");
  }

  @Test
  @DisplayName("TEST getLogInfo (skip = 0, limit = 0)")
  void testGetLogInfoSkipMinLimitMin() {
    Integer skip = 0;
    Integer limit = 0;

    UUID deviceId1 = UUID.fromString("77777777-7777-1111-032c-2b963f789af1");
    UUID deviceId2 = UUID.fromString("77777777-7777-2222-032c-2b963f789af1");
    UUID devLog1Id = UUID.fromString("4f985f64-0314-0361-032c-2b963f789af1");
    UUID devLog2Id = UUID.fromString("5b785f64-5147-4013-b3fc-48963f50ace2");
    UUID devLog3Id = UUID.fromString("2a085f64-842b-3102-9990-6c963f1b3bd3");
    OffsetDateTime devLog1time = OffsetDateTime.now().minusDays(2);
    OffsetDateTime devLog2time = OffsetDateTime.now().minusDays(3);
    OffsetDateTime devLog3time = OffsetDateTime.now();
    CommonLogLevel devLog1level = CommonLogLevel.CRITICAL;
    CommonLogLevel devLog2level = CommonLogLevel.WARNING;
    CommonLogLevel devLog3level = CommonLogLevel.DEBUG;
    String devLog1title = "A fatal error occurred.";
    String devLog2title = "Warning. Something is misconfigured.";
    String devLog3title = "value check: deviceInfo = null";
    String devLog1msg = "NullPointerException on ...test";
    String devLog2msg = "Multiple port values are present.";
    String devLog3msg = "deviceInfo = null";
    DeviceLogInfo devLog1 = new DeviceLogInfo() //
        .logId(devLog1Id) //
        .deviceId(deviceId1) //
        .timestamp(devLog1time) //
        .level(devLog1level) //
        .title(devLog1title) //
        .message(devLog1msg);
    DeviceLogInfo devLog2 = new DeviceLogInfo() //
        .logId(devLog2Id) //
        .deviceId(deviceId1) //
        .timestamp(devLog2time) //
        .level(devLog2level) //
        .title(devLog2title) //
        .message(devLog2msg);
    DeviceLogInfo devLog3 = new DeviceLogInfo() //
        .logId(devLog3Id) //
        .deviceId(deviceId2) //
        .timestamp(devLog3time) //
        .level(devLog3level) //
        .title(devLog3title) //
        .message(devLog3msg);

    Map<UUID, DeviceLogInfo> logMap = new HashMap<>();
    logMap.put(devLog1Id, devLog1);
    logMap.put(devLog2Id, devLog2);
    logMap.put(devLog3Id, devLog3);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(logMap).when(this.deviceLogRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceLogInfo> retListResp = deviceLogService.getLogInfo(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.deviceLogRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getLogInfo (skip = 0, limit = entry max)")
  void testGetLogInfoSkipMinLimitMax() {
    Integer skip = 0;
    Integer limit = null; // Will be changed later

    UUID deviceId1 = UUID.fromString("77777777-7777-1111-032c-2b963f789af1");
    UUID deviceId2 = UUID.fromString("77777777-7777-2222-032c-2b963f789af1");
    UUID devLog1Id = UUID.fromString("4f985f64-0314-0361-032c-2b963f789af1");
    UUID devLog2Id = UUID.fromString("5b785f64-5147-4013-b3fc-48963f50ace2");
    UUID devLog3Id = UUID.fromString("2a085f64-842b-3102-9990-6c963f1b3bd3");
    OffsetDateTime devLog1time = OffsetDateTime.now().minusDays(2);
    OffsetDateTime devLog2time = OffsetDateTime.now().minusDays(3);
    OffsetDateTime devLog3time = OffsetDateTime.now();
    CommonLogLevel devLog1level = CommonLogLevel.CRITICAL;
    CommonLogLevel devLog2level = CommonLogLevel.WARNING;
    CommonLogLevel devLog3level = CommonLogLevel.DEBUG;
    String devLog1title = "A fatal error occurred.";
    String devLog2title = "Warning. Something is misconfigured.";
    String devLog3title = "value check: deviceInfo = null";
    String devLog1msg = "NullPointerException on ...test";
    String devLog2msg = "Multiple port values are present.";
    String devLog3msg = "deviceInfo = null";
    DeviceLogInfo devLog1 = new DeviceLogInfo() //
        .logId(devLog1Id) //
        .deviceId(deviceId1) //
        .timestamp(devLog1time) //
        .level(devLog1level) //
        .title(devLog1title) //
        .message(devLog1msg);
    DeviceLogInfo devLog2 = new DeviceLogInfo() //
        .logId(devLog2Id) //
        .deviceId(deviceId1) //
        .timestamp(devLog2time) //
        .level(devLog2level) //
        .title(devLog2title) //
        .message(devLog2msg);
    DeviceLogInfo devLog3 = new DeviceLogInfo() //
        .logId(devLog3Id) //
        .deviceId(deviceId2) //
        .timestamp(devLog3time) //
        .level(devLog3level) //
        .title(devLog3title) //
        .message(devLog3msg);

    Map<UUID, DeviceLogInfo> logMap = new HashMap<>();
    logMap.put(devLog1Id, devLog1);
    logMap.put(devLog2Id, devLog2);
    logMap.put(devLog3Id, devLog3);

    limit = logMap.size();

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(logMap).when(this.deviceLogRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceLogInfo> retListResp = deviceLogService.getLogInfo(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.deviceLogRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be sorted by timestamp ascending.", retListResp.getObjList(),
        contains(devLog2, devLog1, devLog3));
  }

  @Test
  @DisplayName("TEST getLogInfo (skip = entry max)")
  void testGetLogInfoSkipMax() {
    Integer skip = null; // Will be changed later
    Integer limit = null;

    UUID deviceId1 = UUID.fromString("77777777-7777-1111-032c-2b963f789af1");
    UUID deviceId2 = UUID.fromString("77777777-7777-2222-032c-2b963f789af1");
    UUID devLog1Id = UUID.fromString("4f985f64-0314-0361-032c-2b963f789af1");
    UUID devLog2Id = UUID.fromString("5b785f64-5147-4013-b3fc-48963f50ace2");
    UUID devLog3Id = UUID.fromString("2a085f64-842b-3102-9990-6c963f1b3bd3");
    OffsetDateTime devLog1time = OffsetDateTime.now().minusDays(2);
    OffsetDateTime devLog2time = OffsetDateTime.now().minusDays(3);
    OffsetDateTime devLog3time = OffsetDateTime.now();
    CommonLogLevel devLog1level = CommonLogLevel.CRITICAL;
    CommonLogLevel devLog2level = CommonLogLevel.WARNING;
    CommonLogLevel devLog3level = CommonLogLevel.DEBUG;
    String devLog1title = "A fatal error occurred.";
    String devLog2title = "Warning. Something is misconfigured.";
    String devLog3title = "value check: deviceInfo = null";
    String devLog1msg = "NullPointerException on ...test";
    String devLog2msg = "Multiple port values are present.";
    String devLog3msg = "deviceInfo = null";
    DeviceLogInfo devLog1 = new DeviceLogInfo() //
        .logId(devLog1Id) //
        .deviceId(deviceId1) //
        .timestamp(devLog1time) //
        .level(devLog1level) //
        .title(devLog1title) //
        .message(devLog1msg);
    DeviceLogInfo devLog2 = new DeviceLogInfo() //
        .logId(devLog2Id) //
        .deviceId(deviceId1) //
        .timestamp(devLog2time) //
        .level(devLog2level) //
        .title(devLog2title) //
        .message(devLog2msg);
    DeviceLogInfo devLog3 = new DeviceLogInfo() //
        .logId(devLog3Id) //
        .deviceId(deviceId2) //
        .timestamp(devLog3time) //
        .level(devLog3level) //
        .title(devLog3title) //
        .message(devLog3msg);

    Map<UUID, DeviceLogInfo> logMap = new HashMap<>();
    logMap.put(devLog1Id, devLog1);
    logMap.put(devLog2Id, devLog2);
    logMap.put(devLog3Id, devLog3);

    skip = logMap.size();

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(logMap).when(this.deviceLogRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceLogInfo> retListResp = deviceLogService.getLogInfo(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.deviceLogRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getLogInfo (skip = -1)")
  void testGetLogInfoSkipMinMinus1() {
    Integer skip = -1;
    Integer limit = null;

    UUID deviceId1 = UUID.fromString("77777777-7777-1111-032c-2b963f789af1");
    UUID deviceId2 = UUID.fromString("77777777-7777-2222-032c-2b963f789af1");
    UUID devLog1Id = UUID.fromString("4f985f64-0314-0361-032c-2b963f789af1");
    UUID devLog2Id = UUID.fromString("5b785f64-5147-4013-b3fc-48963f50ace2");
    UUID devLog3Id = UUID.fromString("2a085f64-842b-3102-9990-6c963f1b3bd3");
    OffsetDateTime devLog1time = OffsetDateTime.now().minusDays(2);
    OffsetDateTime devLog2time = OffsetDateTime.now().minusDays(3);
    OffsetDateTime devLog3time = OffsetDateTime.now();
    CommonLogLevel devLog1level = CommonLogLevel.CRITICAL;
    CommonLogLevel devLog2level = CommonLogLevel.WARNING;
    CommonLogLevel devLog3level = CommonLogLevel.DEBUG;
    String devLog1title = "A fatal error occurred.";
    String devLog2title = "Warning. Something is misconfigured.";
    String devLog3title = "value check: deviceInfo = null";
    String devLog1msg = "NullPointerException on ...test";
    String devLog2msg = "Multiple port values are present.";
    String devLog3msg = "deviceInfo = null";
    DeviceLogInfo devLog1 = new DeviceLogInfo() //
        .logId(devLog1Id) //
        .deviceId(deviceId1) //
        .timestamp(devLog1time) //
        .level(devLog1level) //
        .title(devLog1title) //
        .message(devLog1msg);
    DeviceLogInfo devLog2 = new DeviceLogInfo() //
        .logId(devLog2Id) //
        .deviceId(deviceId1) //
        .timestamp(devLog2time) //
        .level(devLog2level) //
        .title(devLog2title) //
        .message(devLog2msg);
    DeviceLogInfo devLog3 = new DeviceLogInfo() //
        .logId(devLog3Id) //
        .deviceId(deviceId2) //
        .timestamp(devLog3time) //
        .level(devLog3level) //
        .title(devLog3title) //
        .message(devLog3msg);

    Map<UUID, DeviceLogInfo> logMap = new HashMap<>();
    logMap.put(devLog1Id, devLog1);
    logMap.put(devLog2Id, devLog2);
    logMap.put(devLog3Id, devLog3);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    doReturn(logMap).when(this.deviceLogRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceLogInfo> retListResp = deviceLogService.getLogInfo(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.deviceLogRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 400.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getLogInfo (limit = -1)")
  void testGetLogInfoLimitMinMinus1() {
    Integer skip = null;
    Integer limit = -1;

    UUID deviceId1 = UUID.fromString("77777777-7777-1111-032c-2b963f789af1");
    UUID deviceId2 = UUID.fromString("77777777-7777-2222-032c-2b963f789af1");
    UUID devLog1Id = UUID.fromString("4f985f64-0314-0361-032c-2b963f789af1");
    UUID devLog2Id = UUID.fromString("5b785f64-5147-4013-b3fc-48963f50ace2");
    UUID devLog3Id = UUID.fromString("2a085f64-842b-3102-9990-6c963f1b3bd3");
    OffsetDateTime devLog1time = OffsetDateTime.now().minusDays(2);
    OffsetDateTime devLog2time = OffsetDateTime.now().minusDays(3);
    OffsetDateTime devLog3time = OffsetDateTime.now();
    CommonLogLevel devLog1level = CommonLogLevel.CRITICAL;
    CommonLogLevel devLog2level = CommonLogLevel.WARNING;
    CommonLogLevel devLog3level = CommonLogLevel.DEBUG;
    String devLog1title = "A fatal error occurred.";
    String devLog2title = "Warning. Something is misconfigured.";
    String devLog3title = "value check: deviceInfo = null";
    String devLog1msg = "NullPointerException on ...test";
    String devLog2msg = "Multiple port values are present.";
    String devLog3msg = "deviceInfo = null";
    DeviceLogInfo devLog1 = new DeviceLogInfo() //
        .logId(devLog1Id) //
        .deviceId(deviceId1) //
        .timestamp(devLog1time) //
        .level(devLog1level) //
        .title(devLog1title) //
        .message(devLog1msg);
    DeviceLogInfo devLog2 = new DeviceLogInfo() //
        .logId(devLog2Id) //
        .deviceId(deviceId1) //
        .timestamp(devLog2time) //
        .level(devLog2level) //
        .title(devLog2title) //
        .message(devLog2msg);
    DeviceLogInfo devLog3 = new DeviceLogInfo() //
        .logId(devLog3Id) //
        .deviceId(deviceId2) //
        .timestamp(devLog3time) //
        .level(devLog3level) //
        .title(devLog3title) //
        .message(devLog3msg);

    Map<UUID, DeviceLogInfo> logMap = new HashMap<>();
    logMap.put(devLog1Id, devLog1);
    logMap.put(devLog2Id, devLog2);
    logMap.put(devLog3Id, devLog3);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    doReturn(logMap).when(this.deviceLogRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceLogInfo> retListResp = deviceLogService.getLogInfo(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.deviceLogRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 400.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getLogInfo (skip = entry max + 1)")
  void testGetLogInfoSkipMaxPlus1() {
    Integer skip = null; // Will be changed later
    Integer limit = null;

    UUID deviceId1 = UUID.fromString("77777777-7777-1111-032c-2b963f789af1");
    UUID deviceId2 = UUID.fromString("77777777-7777-2222-032c-2b963f789af1");
    UUID devLog1Id = UUID.fromString("4f985f64-0314-0361-032c-2b963f789af1");
    UUID devLog2Id = UUID.fromString("5b785f64-5147-4013-b3fc-48963f50ace2");
    UUID devLog3Id = UUID.fromString("2a085f64-842b-3102-9990-6c963f1b3bd3");
    OffsetDateTime devLog1time = OffsetDateTime.now().minusDays(2);
    OffsetDateTime devLog2time = OffsetDateTime.now().minusDays(3);
    OffsetDateTime devLog3time = OffsetDateTime.now();
    CommonLogLevel devLog1level = CommonLogLevel.CRITICAL;
    CommonLogLevel devLog2level = CommonLogLevel.WARNING;
    CommonLogLevel devLog3level = CommonLogLevel.DEBUG;
    String devLog1title = "A fatal error occurred.";
    String devLog2title = "Warning. Something is misconfigured.";
    String devLog3title = "value check: deviceInfo = null";
    String devLog1msg = "NullPointerException on ...test";
    String devLog2msg = "Multiple port values are present.";
    String devLog3msg = "deviceInfo = null";
    DeviceLogInfo devLog1 = new DeviceLogInfo() //
        .logId(devLog1Id) //
        .deviceId(deviceId1) //
        .timestamp(devLog1time) //
        .level(devLog1level) //
        .title(devLog1title) //
        .message(devLog1msg);
    DeviceLogInfo devLog2 = new DeviceLogInfo() //
        .logId(devLog2Id) //
        .deviceId(deviceId1) //
        .timestamp(devLog2time) //
        .level(devLog2level) //
        .title(devLog2title) //
        .message(devLog2msg);
    DeviceLogInfo devLog3 = new DeviceLogInfo() //
        .logId(devLog3Id) //
        .deviceId(deviceId2) //
        .timestamp(devLog3time) //
        .level(devLog3level) //
        .title(devLog3title) //
        .message(devLog3msg);

    Map<UUID, DeviceLogInfo> logMap = new HashMap<>();
    logMap.put(devLog1Id, devLog1);
    logMap.put(devLog2Id, devLog2);
    logMap.put(devLog3Id, devLog3);

    skip = logMap.size() + 1;

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(logMap).when(this.deviceLogRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceLogInfo> retListResp = deviceLogService.getLogInfo(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.deviceLogRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getLogInfo (skip = 1, limit = 1)")
  void testGetLogInfoSkip1Limit1() {
    Integer skip = 1;
    Integer limit = 1;

    UUID deviceId1 = UUID.fromString("77777777-7777-1111-032c-2b963f789af1");
    UUID deviceId2 = UUID.fromString("77777777-7777-2222-032c-2b963f789af1");
    UUID devLog1Id = UUID.fromString("4f985f64-0314-0361-032c-2b963f789af1");
    UUID devLog2Id = UUID.fromString("5b785f64-5147-4013-b3fc-48963f50ace2");
    UUID devLog3Id = UUID.fromString("2a085f64-842b-3102-9990-6c963f1b3bd3");
    OffsetDateTime devLog1time = OffsetDateTime.now().minusDays(2);
    OffsetDateTime devLog2time = OffsetDateTime.now().minusDays(3);
    OffsetDateTime devLog3time = OffsetDateTime.now();
    CommonLogLevel devLog1level = CommonLogLevel.CRITICAL;
    CommonLogLevel devLog2level = CommonLogLevel.WARNING;
    CommonLogLevel devLog3level = CommonLogLevel.DEBUG;
    String devLog1title = "A fatal error occurred.";
    String devLog2title = "Warning. Something is misconfigured.";
    String devLog3title = "value check: deviceInfo = null";
    String devLog1msg = "NullPointerException on ...test";
    String devLog2msg = "Multiple port values are present.";
    String devLog3msg = "deviceInfo = null";
    DeviceLogInfo devLog1 = new DeviceLogInfo() //
        .logId(devLog1Id) //
        .deviceId(deviceId1) //
        .timestamp(devLog1time) //
        .level(devLog1level) //
        .title(devLog1title) //
        .message(devLog1msg);
    DeviceLogInfo devLog2 = new DeviceLogInfo() //
        .logId(devLog2Id) //
        .deviceId(deviceId1) //
        .timestamp(devLog2time) //
        .level(devLog2level) //
        .title(devLog2title) //
        .message(devLog2msg);
    DeviceLogInfo devLog3 = new DeviceLogInfo() //
        .logId(devLog3Id) //
        .deviceId(deviceId2) //
        .timestamp(devLog3time) //
        .level(devLog3level) //
        .title(devLog3title) //
        .message(devLog3msg);

    Map<UUID, DeviceLogInfo> logMap = new HashMap<>();
    logMap.put(devLog1Id, devLog1);
    logMap.put(devLog2Id, devLog2);
    logMap.put(devLog3Id, devLog3);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(logMap).when(this.deviceLogRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceLogInfo> retListResp = deviceLogService.getLogInfo(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.deviceLogRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must only be one, the second entry only.",
        retListResp.getObjList(), contains(devLog1));
  }

  @Test
  @DisplayName("TEST getLogInfo (skip = 2)")
  void testGetLogInfoSkip2() {
    Integer skip = 2;
    Integer limit = null;

    UUID deviceId1 = UUID.fromString("77777777-7777-1111-032c-2b963f789af1");
    UUID deviceId2 = UUID.fromString("77777777-7777-2222-032c-2b963f789af1");
    UUID devLog1Id = UUID.fromString("4f985f64-0314-0361-032c-2b963f789af1");
    UUID devLog2Id = UUID.fromString("5b785f64-5147-4013-b3fc-48963f50ace2");
    UUID devLog3Id = UUID.fromString("2a085f64-842b-3102-9990-6c963f1b3bd3");
    OffsetDateTime devLog1time = OffsetDateTime.now().minusDays(2);
    OffsetDateTime devLog2time = OffsetDateTime.now().minusDays(3);
    OffsetDateTime devLog3time = OffsetDateTime.now();
    CommonLogLevel devLog1level = CommonLogLevel.CRITICAL;
    CommonLogLevel devLog2level = CommonLogLevel.WARNING;
    CommonLogLevel devLog3level = CommonLogLevel.DEBUG;
    String devLog1title = "A fatal error occurred.";
    String devLog2title = "Warning. Something is misconfigured.";
    String devLog3title = "value check: deviceInfo = null";
    String devLog1msg = "NullPointerException on ...test";
    String devLog2msg = "Multiple port values are present.";
    String devLog3msg = "deviceInfo = null";
    DeviceLogInfo devLog1 = new DeviceLogInfo() //
        .logId(devLog1Id) //
        .deviceId(deviceId1) //
        .timestamp(devLog1time) //
        .level(devLog1level) //
        .title(devLog1title) //
        .message(devLog1msg);
    DeviceLogInfo devLog2 = new DeviceLogInfo() //
        .logId(devLog2Id) //
        .deviceId(deviceId1) //
        .timestamp(devLog2time) //
        .level(devLog2level) //
        .title(devLog2title) //
        .message(devLog2msg);
    DeviceLogInfo devLog3 = new DeviceLogInfo() //
        .logId(devLog3Id) //
        .deviceId(deviceId2) //
        .timestamp(devLog3time) //
        .level(devLog3level) //
        .title(devLog3title) //
        .message(devLog3msg);

    Map<UUID, DeviceLogInfo> logMap = new HashMap<>();
    logMap.put(devLog1Id, devLog1);
    logMap.put(devLog2Id, devLog2);
    logMap.put(devLog3Id, devLog3);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(logMap).when(this.deviceLogRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceLogInfo> retListResp = deviceLogService.getLogInfo(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.deviceLogRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must only be one, the last entry only.", retListResp.getObjList(),
        contains(devLog3));
  }

  @Test
  @DisplayName("TEST getLogInfo (limit = 2)")
  void testGetLogInfoLimit2() {
    Integer skip = null;
    Integer limit = 2;

    UUID deviceId1 = UUID.fromString("77777777-7777-1111-032c-2b963f789af1");
    UUID deviceId2 = UUID.fromString("77777777-7777-2222-032c-2b963f789af1");
    UUID devLog1Id = UUID.fromString("4f985f64-0314-0361-032c-2b963f789af1");
    UUID devLog2Id = UUID.fromString("5b785f64-5147-4013-b3fc-48963f50ace2");
    UUID devLog3Id = UUID.fromString("2a085f64-842b-3102-9990-6c963f1b3bd3");
    OffsetDateTime devLog1time = OffsetDateTime.now().minusDays(2);
    OffsetDateTime devLog2time = OffsetDateTime.now().minusDays(3);
    OffsetDateTime devLog3time = OffsetDateTime.now();
    CommonLogLevel devLog1level = CommonLogLevel.CRITICAL;
    CommonLogLevel devLog2level = CommonLogLevel.WARNING;
    CommonLogLevel devLog3level = CommonLogLevel.DEBUG;
    String devLog1title = "A fatal error occurred.";
    String devLog2title = "Warning. Something is misconfigured.";
    String devLog3title = "value check: deviceInfo = null";
    String devLog1msg = "NullPointerException on ...test";
    String devLog2msg = "Multiple port values are present.";
    String devLog3msg = "deviceInfo = null";
    DeviceLogInfo devLog1 = new DeviceLogInfo() //
        .logId(devLog1Id) //
        .deviceId(deviceId1) //
        .timestamp(devLog1time) //
        .level(devLog1level) //
        .title(devLog1title) //
        .message(devLog1msg);
    DeviceLogInfo devLog2 = new DeviceLogInfo() //
        .logId(devLog2Id) //
        .deviceId(deviceId1) //
        .timestamp(devLog2time) //
        .level(devLog2level) //
        .title(devLog2title) //
        .message(devLog2msg);
    DeviceLogInfo devLog3 = new DeviceLogInfo() //
        .logId(devLog3Id) //
        .deviceId(deviceId2) //
        .timestamp(devLog3time) //
        .level(devLog3level) //
        .title(devLog3title) //
        .message(devLog3msg);

    Map<UUID, DeviceLogInfo> logMap = new HashMap<>();
    logMap.put(devLog1Id, devLog1);
    logMap.put(devLog2Id, devLog2);
    logMap.put(devLog3Id, devLog3);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(logMap).when(this.deviceLogRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceLogInfo> retListResp = deviceLogService.getLogInfo(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.deviceLogRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must only be 2, sorted by timestamp ascending.",
        retListResp.getObjList(), contains(devLog2, devLog1));
  }

  @Test
  @DisplayName("TEST getLogInfo (No entries in the DB(null))")
  void testGetLogInfoNoEntryNull() {
    Integer skip = null;
    Integer limit = null;

    Map<UUID, DeviceLogInfo> logMap = null;

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(logMap).when(this.deviceLogRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceLogInfo> retListResp = deviceLogService.getLogInfo(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.deviceLogRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 500.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getLogInfo (No entries in the DB)")
  void testGetLogInfoNoEntry() {
    Integer skip = null;
    Integer limit = null;

    Map<UUID, DeviceLogInfo> logMap = Collections.emptyMap();

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(logMap).when(this.deviceLogRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceLogInfo> retListResp = deviceLogService.getLogInfo(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.deviceLogRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 500.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getFilteredLogInfo (all parameters = null)")
  void testGetFilteredLogInfo() {
    Integer skip = null;
    Integer limit = null;

    UUID deviceId = UUID.fromString("11111111-1111-1111-1111-111111111111");
    UUID deviceId2 = UUID.fromString("22222222-2222-2222-2222-222222222222");

    DeviceInfo dev1 = new DeviceInfo().deviceId(deviceId);
    DeviceInfo dev2 = new DeviceInfo().deviceId(deviceId2);

    UUID devLog1Id = UUID.fromString("61985f64-0314-0361-032c-2b963f789af1");
    UUID devLog2Id = UUID.fromString("82785f64-5147-4013-b3fc-48963f50ace2");
    UUID devLog3Id = UUID.fromString("93085f64-842b-3102-9990-6c963f1b3bd3");
    UUID devLog4Id = UUID.fromString("0bb85f64-062c-0199-98c6-6c963f0c3de4");
    OffsetDateTime devLog1time = OffsetDateTime.now();
    OffsetDateTime devLog2time = OffsetDateTime.now().minusDays(3);
    OffsetDateTime devLog3time = OffsetDateTime.now().minusDays(1);
    OffsetDateTime devLog4time = OffsetDateTime.now().minusDays(2);
    CommonLogLevel devLog1level = CommonLogLevel.ERROR;
    CommonLogLevel devLog2level = CommonLogLevel.INFO;
    CommonLogLevel devLog3level = CommonLogLevel.TRACE;
    CommonLogLevel devLog4level = CommonLogLevel.DEBUG;
    String devLog1title = "An error occurred.";
    String devLog2title = "Device successfully registered.";
    String devLog3title = "Trace: getFilteredLogInfo() start";
    String devLog4title = "controlInfo = 1";
    String devLog1msg = "Test";
    String devLog2msg = "Device successfully registered. Test";
    String devLog3msg = "getFilteredLogInfo() start";
    String devLog4msg = "Test: controlInfo = 1";
    DeviceLogInfo devLog1 = new DeviceLogInfo() //
        .deviceId(deviceId) //
        .logId(devLog1Id) //
        .timestamp(devLog1time) //
        .level(devLog1level) //
        .title(devLog1title) //
        .message(devLog1msg);
    DeviceLogInfo devLog2 = new DeviceLogInfo() //
        .deviceId(deviceId2) //
        .logId(devLog2Id) //
        .timestamp(devLog2time) //
        .level(devLog2level) //
        .title(devLog2title) //
        .message(devLog2msg);
    DeviceLogInfo devLog3 = new DeviceLogInfo() //
        .deviceId(deviceId) //
        .logId(devLog3Id) //
        .timestamp(devLog3time) //
        .level(devLog3level) //
        .title(devLog3title) //
        .message(devLog3msg);
    DeviceLogInfo devLog4 = new DeviceLogInfo() //
        .deviceId(deviceId) //
        .logId(devLog4Id) //
        .timestamp(devLog4time) //
        .level(devLog4level) //
        .title(devLog4title) //
        .message(devLog4msg);

    Map<UUID, DeviceLogInfo> logMap = new HashMap<>();
    logMap.put(devLog1Id, devLog1);
    logMap.put(devLog2Id, devLog2);
    logMap.put(devLog3Id, devLog3);
    logMap.put(devLog4Id, devLog4);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.deviceRepository.findById(deviceId)).thenReturn(dev1);
    when(this.deviceRepository.findById(deviceId2)).thenReturn(dev2);
    doReturn(logMap).when(this.deviceLogRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceLogInfo> retListResp =
        deviceLogService.getFilteredLogInfo(deviceId, skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.deviceLogRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be sorted by timestamp ascending.", retListResp.getObjList(),
        contains(devLog4, devLog3, devLog1));
    assertEquals(devLog4Id, retListResp.getObjList().get(0).getLogId(),
        "1st entry's UUID must match log 4's");
    assertEquals(devLog4time, retListResp.getObjList().get(0).getTimestamp(),
        "1st entry's timestamp must match log 4's");
    assertEquals(devLog4level, retListResp.getObjList().get(0).getLevel(),
        "1st entry's level must match log 4's");
    assertEquals(devLog4title, retListResp.getObjList().get(0).getTitle(),
        "1st entry's title must match log 4's");
    assertEquals(devLog4msg, retListResp.getObjList().get(0).getMessage(),
        "1st entry's message must match log 4's");
    assertEquals(devLog3Id, retListResp.getObjList().get(1).getLogId(),
        "2nd entry's UUID must match log 3's");
    assertEquals(devLog3time, retListResp.getObjList().get(1).getTimestamp(),
        "2nd entry's timestamp must match log 3's");
    assertEquals(devLog3level, retListResp.getObjList().get(1).getLevel(),
        "2nd entry's level must match log 3's");
    assertEquals(devLog3title, retListResp.getObjList().get(1).getTitle(),
        "2nd entry's title must match log 3's");
    assertEquals(devLog3msg, retListResp.getObjList().get(1).getMessage(),
        "2nd entry's message must match log 1's");
    assertEquals(devLog1Id, retListResp.getObjList().get(2).getLogId(),
        "3rd entry's UUID must match log 1's");
    assertEquals(devLog1time, retListResp.getObjList().get(2).getTimestamp(),
        "3rd entry's timestamp must match log 1's");
    assertEquals(devLog1level, retListResp.getObjList().get(2).getLevel(),
        "3rd entry's level must match log 1's");
    assertEquals(devLog1title, retListResp.getObjList().get(2).getTitle(),
        "3rd entry's title must match log 1's");
    assertEquals(devLog1msg, retListResp.getObjList().get(2).getMessage(),
        "3rd entry's message must match log 1's");
  }

  @Test
  @DisplayName("TEST getFilteredLogInfo (skip = 0, limit = entry max)")
  void testGetFilteredLogInfoSkipMinLimitMax() {
    Integer skip = 0;
    Integer limit = null; // Will be changed later

    UUID deviceId = UUID.fromString("11111111-1111-1111-1111-111111111111");
    UUID deviceId2 = UUID.fromString("22222222-2222-2222-2222-222222222222");

    DeviceInfo dev1 = new DeviceInfo().deviceId(deviceId);
    DeviceInfo dev2 = new DeviceInfo().deviceId(deviceId2);

    UUID devLog1Id = UUID.fromString("61985f64-0314-0361-032c-2b963f789af1");
    UUID devLog2Id = UUID.fromString("82785f64-5147-4013-b3fc-48963f50ace2");
    UUID devLog3Id = UUID.fromString("93085f64-842b-3102-9990-6c963f1b3bd3");
    UUID devLog4Id = UUID.fromString("0bb85f64-062c-0199-98c6-6c963f0c3de4");
    OffsetDateTime devLog1time = OffsetDateTime.now();
    OffsetDateTime devLog2time = OffsetDateTime.now().minusDays(3);
    OffsetDateTime devLog3time = OffsetDateTime.now().minusDays(1);
    OffsetDateTime devLog4time = OffsetDateTime.now().minusDays(2);
    CommonLogLevel devLog1level = CommonLogLevel.ERROR;
    CommonLogLevel devLog2level = CommonLogLevel.INFO;
    CommonLogLevel devLog3level = CommonLogLevel.TRACE;
    CommonLogLevel devLog4level = CommonLogLevel.DEBUG;
    String devLog1title = "An error occurred.";
    String devLog2title = "Device successfully registered.";
    String devLog3title = "Trace: getFilteredLogInfo() start";
    String devLog4title = "controlInfo = 1";
    String devLog1msg = "Test";
    String devLog2msg = "Device successfully registered. Test";
    String devLog3msg = "getFilteredLogInfo() start";
    String devLog4msg = "Test: controlInfo = 1";
    DeviceLogInfo devLog1 = new DeviceLogInfo() //
        .deviceId(deviceId) //
        .logId(devLog1Id) //
        .timestamp(devLog1time) //
        .level(devLog1level) //
        .title(devLog1title) //
        .message(devLog1msg);
    DeviceLogInfo devLog2 = new DeviceLogInfo() //
        .deviceId(deviceId2) //
        .logId(devLog2Id) //
        .timestamp(devLog2time) //
        .level(devLog2level) //
        .title(devLog2title) //
        .message(devLog2msg);
    DeviceLogInfo devLog3 = new DeviceLogInfo() //
        .deviceId(deviceId) //
        .logId(devLog3Id) //
        .timestamp(devLog3time) //
        .level(devLog3level) //
        .title(devLog3title) //
        .message(devLog3msg);
    DeviceLogInfo devLog4 = new DeviceLogInfo() //
        .deviceId(deviceId) //
        .logId(devLog4Id) //
        .timestamp(devLog4time) //
        .level(devLog4level) //
        .title(devLog4title) //
        .message(devLog4msg);

    Map<UUID, DeviceLogInfo> logMap = new HashMap<>();
    logMap.put(devLog1Id, devLog1);
    logMap.put(devLog2Id, devLog2);
    logMap.put(devLog3Id, devLog3);
    logMap.put(devLog4Id, devLog4);

    limit = logMap.size();

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.deviceRepository.findById(deviceId)).thenReturn(dev1);
    when(this.deviceRepository.findById(deviceId2)).thenReturn(dev2);
    doReturn(logMap).when(this.deviceLogRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceLogInfo> retListResp =
        deviceLogService.getFilteredLogInfo(deviceId, skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.deviceLogRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be sorted by timestamp ascending.", retListResp.getObjList(),
        contains(devLog4, devLog3, devLog1));
  }

  @Test
  @DisplayName("TEST getFilteredLogInfo (skip = entry max)")
  void testGetFilteredLogInfoSkipMax() {
    Integer skip = null; // Will be changed later
    Integer limit = null;

    UUID deviceId = UUID.fromString("11111111-1111-1111-1111-111111111111");
    UUID deviceId2 = UUID.fromString("22222222-2222-2222-2222-222222222222");

    DeviceInfo dev1 = new DeviceInfo().deviceId(deviceId);
    DeviceInfo dev2 = new DeviceInfo().deviceId(deviceId2);

    UUID devLog1Id = UUID.fromString("61985f64-0314-0361-032c-2b963f789af1");
    UUID devLog2Id = UUID.fromString("82785f64-5147-4013-b3fc-48963f50ace2");
    UUID devLog3Id = UUID.fromString("93085f64-842b-3102-9990-6c963f1b3bd3");
    UUID devLog4Id = UUID.fromString("0bb85f64-062c-0199-98c6-6c963f0c3de4");
    OffsetDateTime devLog1time = OffsetDateTime.now();
    OffsetDateTime devLog2time = OffsetDateTime.now().minusDays(3);
    OffsetDateTime devLog3time = OffsetDateTime.now().minusDays(1);
    OffsetDateTime devLog4time = OffsetDateTime.now().minusDays(2);
    CommonLogLevel devLog1level = CommonLogLevel.ERROR;
    CommonLogLevel devLog2level = CommonLogLevel.INFO;
    CommonLogLevel devLog3level = CommonLogLevel.TRACE;
    CommonLogLevel devLog4level = CommonLogLevel.DEBUG;
    String devLog1title = "An error occurred.";
    String devLog2title = "Device successfully registered.";
    String devLog3title = "Trace: getFilteredLogInfo() start";
    String devLog4title = "controlInfo = 1";
    String devLog1msg = "Test";
    String devLog2msg = "Device successfully registered. Test";
    String devLog3msg = "getFilteredLogInfo() start";
    String devLog4msg = "Test: controlInfo = 1";
    DeviceLogInfo devLog1 = new DeviceLogInfo() //
        .deviceId(deviceId) //
        .logId(devLog1Id) //
        .timestamp(devLog1time) //
        .level(devLog1level) //
        .title(devLog1title) //
        .message(devLog1msg);
    DeviceLogInfo devLog2 = new DeviceLogInfo() //
        .deviceId(deviceId2) //
        .logId(devLog2Id) //
        .timestamp(devLog2time) //
        .level(devLog2level) //
        .title(devLog2title) //
        .message(devLog2msg);
    DeviceLogInfo devLog3 = new DeviceLogInfo() //
        .deviceId(deviceId) //
        .logId(devLog3Id) //
        .timestamp(devLog3time) //
        .level(devLog3level) //
        .title(devLog3title) //
        .message(devLog3msg);
    DeviceLogInfo devLog4 = new DeviceLogInfo() //
        .deviceId(deviceId) //
        .logId(devLog4Id) //
        .timestamp(devLog4time) //
        .level(devLog4level) //
        .title(devLog4title) //
        .message(devLog4msg);

    Map<UUID, DeviceLogInfo> logMap = new HashMap<>();
    logMap.put(devLog1Id, devLog1);
    logMap.put(devLog2Id, devLog2);
    logMap.put(devLog3Id, devLog3);
    logMap.put(devLog4Id, devLog4);

    skip = logMap.size();

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.deviceRepository.findById(deviceId)).thenReturn(dev1);
    when(this.deviceRepository.findById(deviceId2)).thenReturn(dev2);
    doReturn(logMap).when(this.deviceLogRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceLogInfo> retListResp =
        deviceLogService.getFilteredLogInfo(deviceId, skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.deviceLogRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getFilteredLogInfo (skip = -1)")
  void testGetFilteredLogInfoSkipMinMinus1() {
    Integer skip = -1;
    Integer limit = null;

    UUID deviceId = UUID.fromString("11111111-1111-1111-1111-111111111111");
    UUID deviceId2 = UUID.fromString("22222222-2222-2222-2222-222222222222");

    DeviceInfo dev1 = new DeviceInfo().deviceId(deviceId);
    DeviceInfo dev2 = new DeviceInfo().deviceId(deviceId2);

    UUID devLog1Id = UUID.fromString("61985f64-0314-0361-032c-2b963f789af1");
    UUID devLog2Id = UUID.fromString("82785f64-5147-4013-b3fc-48963f50ace2");
    UUID devLog3Id = UUID.fromString("93085f64-842b-3102-9990-6c963f1b3bd3");
    UUID devLog4Id = UUID.fromString("0bb85f64-062c-0199-98c6-6c963f0c3de4");
    OffsetDateTime devLog1time = OffsetDateTime.now();
    OffsetDateTime devLog2time = OffsetDateTime.now().minusDays(3);
    OffsetDateTime devLog3time = OffsetDateTime.now().minusDays(1);
    OffsetDateTime devLog4time = OffsetDateTime.now().minusDays(2);
    CommonLogLevel devLog1level = CommonLogLevel.ERROR;
    CommonLogLevel devLog2level = CommonLogLevel.INFO;
    CommonLogLevel devLog3level = CommonLogLevel.TRACE;
    CommonLogLevel devLog4level = CommonLogLevel.DEBUG;
    String devLog1title = "An error occurred.";
    String devLog2title = "Device successfully registered.";
    String devLog3title = "Trace: getFilteredLogInfo() start";
    String devLog4title = "controlInfo = 1";
    String devLog1msg = "Test";
    String devLog2msg = "Device successfully registered. Test";
    String devLog3msg = "getFilteredLogInfo() start";
    String devLog4msg = "Test: controlInfo = 1";
    DeviceLogInfo devLog1 = new DeviceLogInfo() //
        .deviceId(deviceId) //
        .logId(devLog1Id) //
        .timestamp(devLog1time) //
        .level(devLog1level) //
        .title(devLog1title) //
        .message(devLog1msg);
    DeviceLogInfo devLog2 = new DeviceLogInfo() //
        .deviceId(deviceId2) //
        .logId(devLog2Id) //
        .timestamp(devLog2time) //
        .level(devLog2level) //
        .title(devLog2title) //
        .message(devLog2msg);
    DeviceLogInfo devLog3 = new DeviceLogInfo() //
        .deviceId(deviceId) //
        .logId(devLog3Id) //
        .timestamp(devLog3time) //
        .level(devLog3level) //
        .title(devLog3title) //
        .message(devLog3msg);
    DeviceLogInfo devLog4 = new DeviceLogInfo() //
        .deviceId(deviceId) //
        .logId(devLog4Id) //
        .timestamp(devLog4time) //
        .level(devLog4level) //
        .title(devLog4title) //
        .message(devLog4msg);

    Map<UUID, DeviceLogInfo> logMap = new HashMap<>();
    logMap.put(devLog1Id, devLog1);
    logMap.put(devLog2Id, devLog2);
    logMap.put(devLog3Id, devLog3);
    logMap.put(devLog4Id, devLog4);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.deviceRepository.findById(deviceId)).thenReturn(dev1);
    when(this.deviceRepository.findById(deviceId2)).thenReturn(dev2);
    doReturn(logMap).when(this.deviceLogRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceLogInfo> retListResp =
        deviceLogService.getFilteredLogInfo(deviceId, skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.deviceLogRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 400.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getFilteredLogInfo (limit = -1)")
  void testGetFilteredLogInfoLimitMinMinus1() {
    Integer skip = null;
    Integer limit = -1;

    UUID deviceId = UUID.fromString("11111111-1111-1111-1111-111111111111");
    UUID deviceId2 = UUID.fromString("22222222-2222-2222-2222-222222222222");

    DeviceInfo dev1 = new DeviceInfo().deviceId(deviceId);
    DeviceInfo dev2 = new DeviceInfo().deviceId(deviceId2);

    UUID devLog1Id = UUID.fromString("61985f64-0314-0361-032c-2b963f789af1");
    UUID devLog2Id = UUID.fromString("82785f64-5147-4013-b3fc-48963f50ace2");
    UUID devLog3Id = UUID.fromString("93085f64-842b-3102-9990-6c963f1b3bd3");
    UUID devLog4Id = UUID.fromString("0bb85f64-062c-0199-98c6-6c963f0c3de4");
    OffsetDateTime devLog1time = OffsetDateTime.now();
    OffsetDateTime devLog2time = OffsetDateTime.now().minusDays(3);
    OffsetDateTime devLog3time = OffsetDateTime.now().minusDays(1);
    OffsetDateTime devLog4time = OffsetDateTime.now().minusDays(2);
    CommonLogLevel devLog1level = CommonLogLevel.ERROR;
    CommonLogLevel devLog2level = CommonLogLevel.INFO;
    CommonLogLevel devLog3level = CommonLogLevel.TRACE;
    CommonLogLevel devLog4level = CommonLogLevel.DEBUG;
    String devLog1title = "An error occurred.";
    String devLog2title = "Device successfully registered.";
    String devLog3title = "Trace: getFilteredLogInfo() start";
    String devLog4title = "controlInfo = 1";
    String devLog1msg = "Test";
    String devLog2msg = "Device successfully registered. Test";
    String devLog3msg = "getFilteredLogInfo() start";
    String devLog4msg = "Test: controlInfo = 1";
    DeviceLogInfo devLog1 = new DeviceLogInfo() //
        .deviceId(deviceId) //
        .logId(devLog1Id) //
        .timestamp(devLog1time) //
        .level(devLog1level) //
        .title(devLog1title) //
        .message(devLog1msg);
    DeviceLogInfo devLog2 = new DeviceLogInfo() //
        .deviceId(deviceId2) //
        .logId(devLog2Id) //
        .timestamp(devLog2time) //
        .level(devLog2level) //
        .title(devLog2title) //
        .message(devLog2msg);
    DeviceLogInfo devLog3 = new DeviceLogInfo() //
        .deviceId(deviceId) //
        .logId(devLog3Id) //
        .timestamp(devLog3time) //
        .level(devLog3level) //
        .title(devLog3title) //
        .message(devLog3msg);
    DeviceLogInfo devLog4 = new DeviceLogInfo() //
        .deviceId(deviceId) //
        .logId(devLog4Id) //
        .timestamp(devLog4time) //
        .level(devLog4level) //
        .title(devLog4title) //
        .message(devLog4msg);

    Map<UUID, DeviceLogInfo> logMap = new HashMap<>();
    logMap.put(devLog1Id, devLog1);
    logMap.put(devLog2Id, devLog2);
    logMap.put(devLog3Id, devLog3);
    logMap.put(devLog4Id, devLog4);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.deviceRepository.findById(deviceId)).thenReturn(dev1);
    when(this.deviceRepository.findById(deviceId2)).thenReturn(dev2);
    doReturn(logMap).when(this.deviceLogRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceLogInfo> retListResp =
        deviceLogService.getFilteredLogInfo(deviceId, skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.deviceLogRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 400.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getFilteredLogInfo (skip = entry max + 1)")
  void testGetFilteredLogInfoSkipMaxPlus1() {
    Integer skip = null; // Will be changed later
    Integer limit = null;

    UUID deviceId = UUID.fromString("11111111-1111-1111-1111-111111111111");
    UUID deviceId2 = UUID.fromString("22222222-2222-2222-2222-222222222222");

    DeviceInfo dev1 = new DeviceInfo().deviceId(deviceId);
    DeviceInfo dev2 = new DeviceInfo().deviceId(deviceId2);

    UUID devLog1Id = UUID.fromString("61985f64-0314-0361-032c-2b963f789af1");
    UUID devLog2Id = UUID.fromString("82785f64-5147-4013-b3fc-48963f50ace2");
    UUID devLog3Id = UUID.fromString("93085f64-842b-3102-9990-6c963f1b3bd3");
    UUID devLog4Id = UUID.fromString("0bb85f64-062c-0199-98c6-6c963f0c3de4");
    OffsetDateTime devLog1time = OffsetDateTime.now();
    OffsetDateTime devLog2time = OffsetDateTime.now().minusDays(3);
    OffsetDateTime devLog3time = OffsetDateTime.now().minusDays(1);
    OffsetDateTime devLog4time = OffsetDateTime.now().minusDays(2);
    CommonLogLevel devLog1level = CommonLogLevel.ERROR;
    CommonLogLevel devLog2level = CommonLogLevel.INFO;
    CommonLogLevel devLog3level = CommonLogLevel.TRACE;
    CommonLogLevel devLog4level = CommonLogLevel.DEBUG;
    String devLog1title = "An error occurred.";
    String devLog2title = "Device successfully registered.";
    String devLog3title = "Trace: getFilteredLogInfo() start";
    String devLog4title = "controlInfo = 1";
    String devLog1msg = "Test";
    String devLog2msg = "Device successfully registered. Test";
    String devLog3msg = "getFilteredLogInfo() start";
    String devLog4msg = "Test: controlInfo = 1";
    DeviceLogInfo devLog1 = new DeviceLogInfo() //
        .deviceId(deviceId) //
        .logId(devLog1Id) //
        .timestamp(devLog1time) //
        .level(devLog1level) //
        .title(devLog1title) //
        .message(devLog1msg);
    DeviceLogInfo devLog2 = new DeviceLogInfo() //
        .deviceId(deviceId2) //
        .logId(devLog2Id) //
        .timestamp(devLog2time) //
        .level(devLog2level) //
        .title(devLog2title) //
        .message(devLog2msg);
    DeviceLogInfo devLog3 = new DeviceLogInfo() //
        .deviceId(deviceId) //
        .logId(devLog3Id) //
        .timestamp(devLog3time) //
        .level(devLog3level) //
        .title(devLog3title) //
        .message(devLog3msg);
    DeviceLogInfo devLog4 = new DeviceLogInfo() //
        .deviceId(deviceId) //
        .logId(devLog4Id) //
        .timestamp(devLog4time) //
        .level(devLog4level) //
        .title(devLog4title) //
        .message(devLog4msg);

    Map<UUID, DeviceLogInfo> logMap = new HashMap<>();
    logMap.put(devLog1Id, devLog1);
    logMap.put(devLog2Id, devLog2);
    logMap.put(devLog3Id, devLog3);
    logMap.put(devLog4Id, devLog4);

    skip = logMap.size() + 1;

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.deviceRepository.findById(deviceId)).thenReturn(dev1);
    when(this.deviceRepository.findById(deviceId2)).thenReturn(dev2);
    doReturn(logMap).when(this.deviceLogRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceLogInfo> retListResp =
        deviceLogService.getFilteredLogInfo(deviceId, skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.deviceLogRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getFilteredLogInfo (limit = entry max + 1)")
  void testGetFilteredLogInfoLimitMaxPlus1() {
    Integer skip = null;
    Integer limit = null; // Will be changed later

    UUID deviceId = UUID.fromString("11111111-1111-1111-1111-111111111111");
    UUID deviceId2 = UUID.fromString("22222222-2222-2222-2222-222222222222");

    DeviceInfo dev1 = new DeviceInfo().deviceId(deviceId);
    DeviceInfo dev2 = new DeviceInfo().deviceId(deviceId2);

    UUID devLog1Id = UUID.fromString("61985f64-0314-0361-032c-2b963f789af1");
    UUID devLog2Id = UUID.fromString("82785f64-5147-4013-b3fc-48963f50ace2");
    UUID devLog3Id = UUID.fromString("93085f64-842b-3102-9990-6c963f1b3bd3");
    UUID devLog4Id = UUID.fromString("0bb85f64-062c-0199-98c6-6c963f0c3de4");
    OffsetDateTime devLog1time = OffsetDateTime.now();
    OffsetDateTime devLog2time = OffsetDateTime.now().minusDays(3);
    OffsetDateTime devLog3time = OffsetDateTime.now().minusDays(1);
    OffsetDateTime devLog4time = OffsetDateTime.now().minusDays(2);
    CommonLogLevel devLog1level = CommonLogLevel.ERROR;
    CommonLogLevel devLog2level = CommonLogLevel.INFO;
    CommonLogLevel devLog3level = CommonLogLevel.TRACE;
    CommonLogLevel devLog4level = CommonLogLevel.DEBUG;
    String devLog1title = "An error occurred.";
    String devLog2title = "Device successfully registered.";
    String devLog3title = "Trace: getFilteredLogInfo() start";
    String devLog4title = "controlInfo = 1";
    String devLog1msg = "Test";
    String devLog2msg = "Device successfully registered. Test";
    String devLog3msg = "getFilteredLogInfo() start";
    String devLog4msg = "Test: controlInfo = 1";
    DeviceLogInfo devLog1 = new DeviceLogInfo() //
        .deviceId(deviceId) //
        .logId(devLog1Id) //
        .timestamp(devLog1time) //
        .level(devLog1level) //
        .title(devLog1title) //
        .message(devLog1msg);
    DeviceLogInfo devLog2 = new DeviceLogInfo() //
        .deviceId(deviceId2) //
        .logId(devLog2Id) //
        .timestamp(devLog2time) //
        .level(devLog2level) //
        .title(devLog2title) //
        .message(devLog2msg);
    DeviceLogInfo devLog3 = new DeviceLogInfo() //
        .deviceId(deviceId) //
        .logId(devLog3Id) //
        .timestamp(devLog3time) //
        .level(devLog3level) //
        .title(devLog3title) //
        .message(devLog3msg);
    DeviceLogInfo devLog4 = new DeviceLogInfo() //
        .deviceId(deviceId) //
        .logId(devLog4Id) //
        .timestamp(devLog4time) //
        .level(devLog4level) //
        .title(devLog4title) //
        .message(devLog4msg);

    Map<UUID, DeviceLogInfo> logMap = new HashMap<>();
    logMap.put(devLog1Id, devLog1);
    logMap.put(devLog2Id, devLog2);
    logMap.put(devLog3Id, devLog3);
    logMap.put(devLog4Id, devLog4);

    limit = logMap.size() + 1;

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.deviceRepository.findById(deviceId)).thenReturn(dev1);
    when(this.deviceRepository.findById(deviceId2)).thenReturn(dev2);
    doReturn(logMap).when(this.deviceLogRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceLogInfo> retListResp =
        deviceLogService.getFilteredLogInfo(deviceId, skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.deviceLogRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be sorted by timestamp ascending.", retListResp.getObjList(),
        contains(devLog4, devLog3, devLog1));
  }

  @Test
  @DisplayName("TEST getFilteredLogInfo (skip = 1, limit = 1)")
  void testGetFilteredLogInfoSkip1Limit1() {
    Integer skip = 1;
    Integer limit = 1;

    UUID deviceId = UUID.fromString("11111111-1111-1111-1111-111111111111");
    UUID deviceId2 = UUID.fromString("22222222-2222-2222-2222-222222222222");

    DeviceInfo dev1 = new DeviceInfo().deviceId(deviceId);
    DeviceInfo dev2 = new DeviceInfo().deviceId(deviceId2);

    UUID devLog1Id = UUID.fromString("61985f64-0314-0361-032c-2b963f789af1");
    UUID devLog2Id = UUID.fromString("82785f64-5147-4013-b3fc-48963f50ace2");
    UUID devLog3Id = UUID.fromString("93085f64-842b-3102-9990-6c963f1b3bd3");
    UUID devLog4Id = UUID.fromString("0bb85f64-062c-0199-98c6-6c963f0c3de4");
    OffsetDateTime devLog1time = OffsetDateTime.now();
    OffsetDateTime devLog2time = OffsetDateTime.now().minusDays(3);
    OffsetDateTime devLog3time = OffsetDateTime.now().minusDays(1);
    OffsetDateTime devLog4time = OffsetDateTime.now().minusDays(2);
    CommonLogLevel devLog1level = CommonLogLevel.ERROR;
    CommonLogLevel devLog2level = CommonLogLevel.INFO;
    CommonLogLevel devLog3level = CommonLogLevel.TRACE;
    CommonLogLevel devLog4level = CommonLogLevel.DEBUG;
    String devLog1title = "An error occurred.";
    String devLog2title = "Device successfully registered.";
    String devLog3title = "Trace: getFilteredLogInfo() start";
    String devLog4title = "controlInfo = 1";
    String devLog1msg = "Test";
    String devLog2msg = "Device successfully registered. Test";
    String devLog3msg = "getFilteredLogInfo() start";
    String devLog4msg = "Test: controlInfo = 1";
    DeviceLogInfo devLog1 = new DeviceLogInfo() //
        .deviceId(deviceId) //
        .logId(devLog1Id) //
        .timestamp(devLog1time) //
        .level(devLog1level) //
        .title(devLog1title) //
        .message(devLog1msg);
    DeviceLogInfo devLog2 = new DeviceLogInfo() //
        .deviceId(deviceId2) //
        .logId(devLog2Id) //
        .timestamp(devLog2time) //
        .level(devLog2level) //
        .title(devLog2title) //
        .message(devLog2msg);
    DeviceLogInfo devLog3 = new DeviceLogInfo() //
        .deviceId(deviceId) //
        .logId(devLog3Id) //
        .timestamp(devLog3time) //
        .level(devLog3level) //
        .title(devLog3title) //
        .message(devLog3msg);
    DeviceLogInfo devLog4 = new DeviceLogInfo() //
        .deviceId(deviceId) //
        .logId(devLog4Id) //
        .timestamp(devLog4time) //
        .level(devLog4level) //
        .title(devLog4title) //
        .message(devLog4msg);

    Map<UUID, DeviceLogInfo> logMap = new HashMap<>();
    logMap.put(devLog1Id, devLog1);
    logMap.put(devLog2Id, devLog2);
    logMap.put(devLog3Id, devLog3);
    logMap.put(devLog4Id, devLog4);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.deviceRepository.findById(deviceId)).thenReturn(dev1);
    when(this.deviceRepository.findById(deviceId2)).thenReturn(dev2);
    doReturn(logMap).when(this.deviceLogRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceLogInfo> retListResp =
        deviceLogService.getFilteredLogInfo(deviceId, skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.deviceLogRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must only be one, the second entry only.",
        retListResp.getObjList(), contains(devLog3));
  }

  @Test
  @DisplayName("TEST getFilteredLogInfo (skip = 2)")
  void testGetFilteredLogInfoSkip2() {
    Integer skip = 2;
    Integer limit = null;

    UUID deviceId = UUID.fromString("11111111-1111-1111-1111-111111111111");
    UUID deviceId2 = UUID.fromString("22222222-2222-2222-2222-222222222222");

    DeviceInfo dev1 = new DeviceInfo().deviceId(deviceId);
    DeviceInfo dev2 = new DeviceInfo().deviceId(deviceId2);

    UUID devLog1Id = UUID.fromString("61985f64-0314-0361-032c-2b963f789af1");
    UUID devLog2Id = UUID.fromString("82785f64-5147-4013-b3fc-48963f50ace2");
    UUID devLog3Id = UUID.fromString("93085f64-842b-3102-9990-6c963f1b3bd3");
    UUID devLog4Id = UUID.fromString("0bb85f64-062c-0199-98c6-6c963f0c3de4");
    OffsetDateTime devLog1time = OffsetDateTime.now();
    OffsetDateTime devLog2time = OffsetDateTime.now().minusDays(3);
    OffsetDateTime devLog3time = OffsetDateTime.now().minusDays(1);
    OffsetDateTime devLog4time = OffsetDateTime.now().minusDays(2);
    CommonLogLevel devLog1level = CommonLogLevel.ERROR;
    CommonLogLevel devLog2level = CommonLogLevel.INFO;
    CommonLogLevel devLog3level = CommonLogLevel.TRACE;
    CommonLogLevel devLog4level = CommonLogLevel.DEBUG;
    String devLog1title = "An error occurred.";
    String devLog2title = "Device successfully registered.";
    String devLog3title = "Trace: getFilteredLogInfo() start";
    String devLog4title = "controlInfo = 1";
    String devLog1msg = "Test";
    String devLog2msg = "Device successfully registered. Test";
    String devLog3msg = "getFilteredLogInfo() start";
    String devLog4msg = "Test: controlInfo = 1";
    DeviceLogInfo devLog1 = new DeviceLogInfo() //
        .deviceId(deviceId) //
        .logId(devLog1Id) //
        .timestamp(devLog1time) //
        .level(devLog1level) //
        .title(devLog1title) //
        .message(devLog1msg);
    DeviceLogInfo devLog2 = new DeviceLogInfo() //
        .deviceId(deviceId2) //
        .logId(devLog2Id) //
        .timestamp(devLog2time) //
        .level(devLog2level) //
        .title(devLog2title) //
        .message(devLog2msg);
    DeviceLogInfo devLog3 = new DeviceLogInfo() //
        .deviceId(deviceId) //
        .logId(devLog3Id) //
        .timestamp(devLog3time) //
        .level(devLog3level) //
        .title(devLog3title) //
        .message(devLog3msg);
    DeviceLogInfo devLog4 = new DeviceLogInfo() //
        .deviceId(deviceId) //
        .logId(devLog4Id) //
        .timestamp(devLog4time) //
        .level(devLog4level) //
        .title(devLog4title) //
        .message(devLog4msg);

    Map<UUID, DeviceLogInfo> logMap = new HashMap<>();
    logMap.put(devLog1Id, devLog1);
    logMap.put(devLog2Id, devLog2);
    logMap.put(devLog3Id, devLog3);
    logMap.put(devLog4Id, devLog4);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.deviceRepository.findById(deviceId)).thenReturn(dev1);
    when(this.deviceRepository.findById(deviceId2)).thenReturn(dev2);
    doReturn(logMap).when(this.deviceLogRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceLogInfo> retListResp =
        deviceLogService.getFilteredLogInfo(deviceId, skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.deviceLogRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must only be one, the last entry only.", retListResp.getObjList(),
        contains(devLog1));
  }

  @Test
  @DisplayName("TEST getFilteredLogInfo (limit = 2)")
  void testGetFilteredLogInfoLimit2() {
    Integer skip = null;
    Integer limit = 2;

    UUID deviceId = UUID.fromString("11111111-1111-1111-1111-111111111111");
    UUID deviceId2 = UUID.fromString("22222222-2222-2222-2222-222222222222");

    DeviceInfo dev1 = new DeviceInfo().deviceId(deviceId);
    DeviceInfo dev2 = new DeviceInfo().deviceId(deviceId2);

    UUID devLog1Id = UUID.fromString("61985f64-0314-0361-032c-2b963f789af1");
    UUID devLog2Id = UUID.fromString("82785f64-5147-4013-b3fc-48963f50ace2");
    UUID devLog3Id = UUID.fromString("93085f64-842b-3102-9990-6c963f1b3bd3");
    UUID devLog4Id = UUID.fromString("0bb85f64-062c-0199-98c6-6c963f0c3de4");
    OffsetDateTime devLog1time = OffsetDateTime.now();
    OffsetDateTime devLog2time = OffsetDateTime.now().minusDays(3);
    OffsetDateTime devLog3time = OffsetDateTime.now().minusDays(1);
    OffsetDateTime devLog4time = OffsetDateTime.now().minusDays(2);
    CommonLogLevel devLog1level = CommonLogLevel.ERROR;
    CommonLogLevel devLog2level = CommonLogLevel.INFO;
    CommonLogLevel devLog3level = CommonLogLevel.TRACE;
    CommonLogLevel devLog4level = CommonLogLevel.DEBUG;
    String devLog1title = "An error occurred.";
    String devLog2title = "Device successfully registered.";
    String devLog3title = "Trace: getFilteredLogInfo() start";
    String devLog4title = "controlInfo = 1";
    String devLog1msg = "Test";
    String devLog2msg = "Device successfully registered. Test";
    String devLog3msg = "getFilteredLogInfo() start";
    String devLog4msg = "Test: controlInfo = 1";
    DeviceLogInfo devLog1 = new DeviceLogInfo() //
        .deviceId(deviceId) //
        .logId(devLog1Id) //
        .timestamp(devLog1time) //
        .level(devLog1level) //
        .title(devLog1title) //
        .message(devLog1msg);
    DeviceLogInfo devLog2 = new DeviceLogInfo() //
        .deviceId(deviceId2) //
        .logId(devLog2Id) //
        .timestamp(devLog2time) //
        .level(devLog2level) //
        .title(devLog2title) //
        .message(devLog2msg);
    DeviceLogInfo devLog3 = new DeviceLogInfo() //
        .deviceId(deviceId) //
        .logId(devLog3Id) //
        .timestamp(devLog3time) //
        .level(devLog3level) //
        .title(devLog3title) //
        .message(devLog3msg);
    DeviceLogInfo devLog4 = new DeviceLogInfo() //
        .deviceId(deviceId) //
        .logId(devLog4Id) //
        .timestamp(devLog4time) //
        .level(devLog4level) //
        .title(devLog4title) //
        .message(devLog4msg);

    Map<UUID, DeviceLogInfo> logMap = new HashMap<>();
    logMap.put(devLog1Id, devLog1);
    logMap.put(devLog2Id, devLog2);
    logMap.put(devLog3Id, devLog3);
    logMap.put(devLog4Id, devLog4);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.deviceRepository.findById(deviceId)).thenReturn(dev1);
    when(this.deviceRepository.findById(deviceId2)).thenReturn(dev2);
    doReturn(logMap).when(this.deviceLogRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceLogInfo> retListResp =
        deviceLogService.getFilteredLogInfo(deviceId, skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.deviceLogRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must only be 2, sorted by timestamp ascending.",
        retListResp.getObjList(), contains(devLog4, devLog3));
  }

  @Test
  @DisplayName("TEST getFilteredLogInfo (No entries in the DB(null))")
  void testGetFilteredLogInfoNoEntryNull() {
    Integer skip = null;
    Integer limit = null;

    UUID deviceId = UUID.fromString("11111111-1111-1111-1111-111111111111");
    UUID deviceId2 = UUID.fromString("22222222-2222-2222-2222-222222222222");

    DeviceInfo dev1 = new DeviceInfo().deviceId(deviceId);
    DeviceInfo dev2 = new DeviceInfo().deviceId(deviceId2);

    Map<UUID, DeviceLogInfo> logMap = null;

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.deviceRepository.findById(deviceId)).thenReturn(dev1);
    when(this.deviceRepository.findById(deviceId2)).thenReturn(dev2);
    doReturn(logMap).when(this.deviceLogRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceLogInfo> retListResp =
        deviceLogService.getFilteredLogInfo(deviceId, skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.deviceLogRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 500.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getFilteredLogInfo (No entries in the DB)")
  void testGetFilteredLogInfoNoEntry() {
    Integer skip = null;
    Integer limit = null;

    UUID deviceId = UUID.fromString("11111111-1111-1111-1111-111111111111");
    UUID deviceId2 = UUID.fromString("22222222-2222-2222-2222-222222222222");

    DeviceInfo dev1 = new DeviceInfo().deviceId(deviceId);
    DeviceInfo dev2 = new DeviceInfo().deviceId(deviceId2);

    Map<UUID, DeviceLogInfo> logMap = Collections.emptyMap();

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.deviceRepository.findById(deviceId)).thenReturn(dev1);
    when(this.deviceRepository.findById(deviceId2)).thenReturn(dev2);
    doReturn(logMap).when(this.deviceLogRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceLogInfo> retListResp =
        deviceLogService.getFilteredLogInfo(deviceId, skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.deviceLogRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 500.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getFilteredLogInfo (deviceId = null)")
  void testGetFilteredLogInfoDeviceIdNull() {
    Integer skip = null;
    Integer limit = null;

    UUID deviceId = UUID.fromString("11111111-1111-1111-1111-111111111111");
    UUID deviceId2 = UUID.fromString("22222222-2222-2222-2222-222222222222");

    DeviceInfo dev1 = new DeviceInfo().deviceId(deviceId);
    DeviceInfo dev2 = new DeviceInfo().deviceId(deviceId2);

    UUID devLog1Id = UUID.fromString("61985f64-0314-0361-032c-2b963f789af1");
    UUID devLog2Id = UUID.fromString("82785f64-5147-4013-b3fc-48963f50ace2");
    UUID devLog3Id = UUID.fromString("93085f64-842b-3102-9990-6c963f1b3bd3");
    UUID devLog4Id = UUID.fromString("0bb85f64-062c-0199-98c6-6c963f0c3de4");
    OffsetDateTime devLog1time = OffsetDateTime.now();
    OffsetDateTime devLog2time = OffsetDateTime.now().minusDays(3);
    OffsetDateTime devLog3time = OffsetDateTime.now().minusDays(1);
    OffsetDateTime devLog4time = OffsetDateTime.now().minusDays(2);
    CommonLogLevel devLog1level = CommonLogLevel.ERROR;
    CommonLogLevel devLog2level = CommonLogLevel.INFO;
    CommonLogLevel devLog3level = CommonLogLevel.TRACE;
    CommonLogLevel devLog4level = CommonLogLevel.DEBUG;
    String devLog1title = "An error occurred.";
    String devLog2title = "Device successfully registered.";
    String devLog3title = "Trace: getFilteredLogInfo() start";
    String devLog4title = "controlInfo = 1";
    String devLog1msg = "Test";
    String devLog2msg = "Device successfully registered. Test";
    String devLog3msg = "getFilteredLogInfo() start";
    String devLog4msg = "Test: controlInfo = 1";
    DeviceLogInfo devLog1 = new DeviceLogInfo() //
        .deviceId(deviceId) //
        .logId(devLog1Id) //
        .timestamp(devLog1time) //
        .level(devLog1level) //
        .title(devLog1title) //
        .message(devLog1msg);
    DeviceLogInfo devLog2 = new DeviceLogInfo() //
        .deviceId(deviceId2) //
        .logId(devLog2Id) //
        .timestamp(devLog2time) //
        .level(devLog2level) //
        .title(devLog2title) //
        .message(devLog2msg);
    DeviceLogInfo devLog3 = new DeviceLogInfo() //
        .deviceId(deviceId) //
        .logId(devLog3Id) //
        .timestamp(devLog3time) //
        .level(devLog3level) //
        .title(devLog3title) //
        .message(devLog3msg);
    DeviceLogInfo devLog4 = new DeviceLogInfo() //
        .deviceId(deviceId) //
        .logId(devLog4Id) //
        .timestamp(devLog4time) //
        .level(devLog4level) //
        .title(devLog4title) //
        .message(devLog4msg);

    Map<UUID, DeviceLogInfo> logMap = new HashMap<>();
    logMap.put(devLog1Id, devLog1);
    logMap.put(devLog2Id, devLog2);
    logMap.put(devLog3Id, devLog3);
    logMap.put(devLog4Id, devLog4);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.deviceRepository.findById(deviceId)).thenReturn(dev1);
    when(this.deviceRepository.findById(deviceId2)).thenReturn(dev2);
    doReturn(logMap).when(this.deviceLogRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceLogInfo> retListResp =
        deviceLogService.getFilteredLogInfo(null, skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.deviceLogRepository, times(0)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 400.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getFilteredLogInfo (device is not found)")
  void testGetFilteredLogInfoDeviceNotFound() {
    Integer skip = null;
    Integer limit = null;

    UUID deviceId = UUID.fromString("11111111-1111-1111-1111-111111111111");
    UUID deviceId2 = UUID.fromString("22222222-2222-2222-2222-222222222222");
    UUID deviceIdOther = UUID.fromString("11111111-0000-0000-1111-111111111111");

    DeviceInfo dev1 = new DeviceInfo().deviceId(deviceId);
    DeviceInfo dev2 = new DeviceInfo().deviceId(deviceId2);

    UUID devLog1Id = UUID.fromString("61985f64-0314-0361-032c-2b963f789af1");
    UUID devLog2Id = UUID.fromString("82785f64-5147-4013-b3fc-48963f50ace2");
    UUID devLog3Id = UUID.fromString("93085f64-842b-3102-9990-6c963f1b3bd3");
    UUID devLog4Id = UUID.fromString("0bb85f64-062c-0199-98c6-6c963f0c3de4");
    OffsetDateTime devLog1time = OffsetDateTime.now();
    OffsetDateTime devLog2time = OffsetDateTime.now().minusDays(3);
    OffsetDateTime devLog3time = OffsetDateTime.now().minusDays(1);
    OffsetDateTime devLog4time = OffsetDateTime.now().minusDays(2);
    CommonLogLevel devLog1level = CommonLogLevel.ERROR;
    CommonLogLevel devLog2level = CommonLogLevel.INFO;
    CommonLogLevel devLog3level = CommonLogLevel.TRACE;
    CommonLogLevel devLog4level = CommonLogLevel.DEBUG;
    String devLog1title = "An error occurred.";
    String devLog2title = "Device successfully registered.";
    String devLog3title = "Trace: getFilteredLogInfo() start";
    String devLog4title = "controlInfo = 1";
    String devLog1msg = "Test";
    String devLog2msg = "Device successfully registered. Test";
    String devLog3msg = "getFilteredLogInfo() start";
    String devLog4msg = "Test: controlInfo = 1";
    DeviceLogInfo devLog1 = new DeviceLogInfo() //
        .deviceId(deviceId) //
        .logId(devLog1Id) //
        .timestamp(devLog1time) //
        .level(devLog1level) //
        .title(devLog1title) //
        .message(devLog1msg);
    DeviceLogInfo devLog2 = new DeviceLogInfo() //
        .deviceId(deviceId2) //
        .logId(devLog2Id) //
        .timestamp(devLog2time) //
        .level(devLog2level) //
        .title(devLog2title) //
        .message(devLog2msg);
    DeviceLogInfo devLog3 = new DeviceLogInfo() //
        .deviceId(deviceId) //
        .logId(devLog3Id) //
        .timestamp(devLog3time) //
        .level(devLog3level) //
        .title(devLog3title) //
        .message(devLog3msg);
    DeviceLogInfo devLog4 = new DeviceLogInfo() //
        .deviceId(deviceId) //
        .logId(devLog4Id) //
        .timestamp(devLog4time) //
        .level(devLog4level) //
        .title(devLog4title) //
        .message(devLog4msg);

    Map<UUID, DeviceLogInfo> logMap = new HashMap<>();
    logMap.put(devLog1Id, devLog1);
    logMap.put(devLog2Id, devLog2);
    logMap.put(devLog3Id, devLog3);
    logMap.put(devLog4Id, devLog4);

    HttpStatus expectedStatus = HttpStatus.NOT_FOUND;

    // Mock(s)
    when(this.deviceRepository.findById(deviceId)).thenReturn(dev1);
    when(this.deviceRepository.findById(deviceId2)).thenReturn(dev2);
    doReturn(logMap).when(this.deviceLogRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceLogInfo> retListResp =
        deviceLogService.getFilteredLogInfo(deviceIdOther, skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.deviceLogRepository, times(0)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 404.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST deleteLogs")
  void testDeleteLogs() {
    UUID deviceId = UUID.fromString("11111111-1111-1111-1111-111111111111");
    UUID deviceId2 = UUID.fromString("22222222-2222-2222-2222-222222222222");
    UUID devLog1Id = UUID.fromString("4f985f64-0314-0361-032c-2b963f789af1");
    UUID devLog2Id = UUID.fromString("5b785f64-5147-4013-b3fc-48963f50ace2");
    UUID devLog3Id = UUID.fromString("2a085f64-842b-3102-9990-6c963f1b3bd3");
    OffsetDateTime devLog1time = OffsetDateTime.now().minusDays(2);
    OffsetDateTime devLog2time = OffsetDateTime.now().minusDays(3);
    OffsetDateTime devLog3time = OffsetDateTime.now();
    CommonLogLevel devLog1level = CommonLogLevel.CRITICAL;
    CommonLogLevel devLog2level = CommonLogLevel.WARNING;
    CommonLogLevel devLog3level = CommonLogLevel.DEBUG;
    String devLog1title = "A fatal error occurred.";
    String devLog2title = "Warning. Something is misconfigured.";
    String devLog3title = "value check: deviceInfo = null";
    String devLog1msg = "NullPointerException on ...test";
    String devLog2msg = "Multiple port values are present.";
    String devLog3msg = "deviceInfo = null";
    DeviceLogInfo devLog1 = new DeviceLogInfo() //
        .logId(devLog1Id) //
        .deviceId(deviceId) //
        .timestamp(devLog1time) //
        .level(devLog1level) //
        .title(devLog1title) //
        .message(devLog1msg);
    DeviceLogInfo devLog2 = new DeviceLogInfo() //
        .logId(devLog2Id) //
        .deviceId(deviceId) //
        .timestamp(devLog2time) //
        .level(devLog2level) //
        .title(devLog2title) //
        .message(devLog2msg);
    DeviceLogInfo devLog3 = new DeviceLogInfo() //
        .logId(devLog3Id) //
        .deviceId(deviceId2) //
        .timestamp(devLog3time) //
        .level(devLog3level) //
        .title(devLog3title) //
        .message(devLog3msg);

    Map<UUID, DeviceLogInfo> logMap = new HashMap<>();
    logMap.put(devLog1Id, devLog1);
    logMap.put(devLog2Id, devLog2);
    logMap.put(devLog3Id, devLog3);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(true).when(deviceLogRepository).deleteAll();
    doReturn(logMap).when(deviceLogRepository).findAll();

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call Service
    CommonOperResponse operResp = deviceLogService.deleteAllLogs();

    // Verify that appropriate Repository method was called
    verify(this.deviceLogRepository, times(1)).deleteAll();

    // Verify that appropriate Repository method was NOT called
    verify(this.deviceLogRepository, times(0)).delete(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST deleteLogs (some logs were not deleted)")
  void testDeleteLogsSomeLogsNotDeleted() {
    UUID deviceId = UUID.fromString("11111111-1111-1111-1111-111111111111");
    UUID deviceId2 = UUID.fromString("22222222-2222-2222-2222-222222222222");
    UUID devLog1Id = UUID.fromString("4f985f64-0314-0361-032c-2b963f789af1");
    UUID devLog2Id = UUID.fromString("5b785f64-5147-4013-b3fc-48963f50ace2");
    UUID devLog3Id = UUID.fromString("2a085f64-842b-3102-9990-6c963f1b3bd3");
    OffsetDateTime devLog1time = OffsetDateTime.now().minusDays(2);
    OffsetDateTime devLog2time = OffsetDateTime.now().minusDays(3);
    OffsetDateTime devLog3time = OffsetDateTime.now();
    CommonLogLevel devLog1level = CommonLogLevel.CRITICAL;
    CommonLogLevel devLog2level = CommonLogLevel.WARNING;
    CommonLogLevel devLog3level = CommonLogLevel.DEBUG;
    String devLog1title = "A fatal error occurred.";
    String devLog2title = "Warning. Something is misconfigured.";
    String devLog3title = "value check: deviceInfo = null";
    String devLog1msg = "NullPointerException on ...test";
    String devLog2msg = "Multiple port values are present.";
    String devLog3msg = "deviceInfo = null";
    DeviceLogInfo devLog1 = new DeviceLogInfo() //
        .logId(devLog1Id) //
        .deviceId(deviceId) //
        .timestamp(devLog1time) //
        .level(devLog1level) //
        .title(devLog1title) //
        .message(devLog1msg);
    DeviceLogInfo devLog2 = new DeviceLogInfo() //
        .logId(devLog2Id) //
        .deviceId(deviceId) //
        .timestamp(devLog2time) //
        .level(devLog2level) //
        .title(devLog2title) //
        .message(devLog2msg);
    DeviceLogInfo devLog3 = new DeviceLogInfo() //
        .logId(devLog3Id) //
        .deviceId(deviceId2) //
        .timestamp(devLog3time) //
        .level(devLog3level) //
        .title(devLog3title) //
        .message(devLog3msg);

    Map<UUID, DeviceLogInfo> logMap = new HashMap<>();
    logMap.put(devLog1Id, devLog1);
    logMap.put(devLog2Id, devLog2);
    logMap.put(devLog3Id, devLog3);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot delete all log entries.";
    String expectedDetail = "Some or all device log entries may not have been deleted.";

    // Mock(s)
    doReturn(false).when(deviceLogRepository).deleteAll();
    doReturn(logMap).when(deviceLogRepository).findAll();

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call Service
    CommonOperResponse operResp = deviceLogService.deleteAllLogs();

    // Verify that appropriate Repository method was called
    verify(this.deviceLogRepository, times(1)).deleteAll();

    // Verify that appropriate Repository method was NOT called
    verify(this.deviceLogRepository, times(0)).delete(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 500.");
    assertNotNull(operResp.getErrorResult(), "There should be an error returned.");
    assertNotNull(operResp.getErrorResult().getLogId(), "Error object must have logId provided.");
    assertNotNull(operResp.getErrorResult().getTimestamp(),
        "Error object must have timestamp provided.");
    assertEquals(ErrorType.ERROR, operResp.getErrorResult().getType(),
        "Error object must have type = Error");
    assertNotNull(operResp.getErrorResult().getType(), "Error object must have logId provided.");
    assertEquals(expectedErrMsg, operResp.getErrorResult().getMessage(),
        "Error Message must match.");
    assertEquals(expectedDetail, operResp.getErrorResult().getDetails(),
        "Error Details must match.");
  }

  @Test
  @DisplayName("TEST deleteSpecificLogs")
  void testDeleteSpecificLogs() {
    UUID deviceId = UUID.fromString("11111111-1111-1111-1111-111111111111");
    UUID deviceId2 = UUID.fromString("22222222-2222-2222-2222-222222222222");
    UUID devLog1Id = UUID.fromString("4f985f64-0314-0361-032c-2b963f789af1");
    UUID devLog2Id = UUID.fromString("5b785f64-5147-4013-b3fc-48963f50ace2");
    UUID devLog3Id = UUID.fromString("2a085f64-842b-3102-9990-6c963f1b3bd3");
    OffsetDateTime devLog1time = OffsetDateTime.now().minusDays(2);
    OffsetDateTime devLog2time = OffsetDateTime.now().minusDays(3);
    OffsetDateTime devLog3time = OffsetDateTime.now();
    CommonLogLevel devLog1level = CommonLogLevel.CRITICAL;
    CommonLogLevel devLog2level = CommonLogLevel.WARNING;
    CommonLogLevel devLog3level = CommonLogLevel.DEBUG;
    String devLog1title = "A fatal error occurred.";
    String devLog2title = "Warning. Something is misconfigured.";
    String devLog3title = "value check: deviceInfo = null";
    String devLog1msg = "NullPointerException on ...test";
    String devLog2msg = "Multiple port values are present.";
    String devLog3msg = "deviceInfo = null";
    DeviceLogInfo devLog1 = new DeviceLogInfo() //
        .logId(devLog1Id) //
        .deviceId(deviceId) //
        .timestamp(devLog1time) //
        .level(devLog1level) //
        .title(devLog1title) //
        .message(devLog1msg);
    DeviceLogInfo devLog2 = new DeviceLogInfo() //
        .logId(devLog2Id) //
        .deviceId(deviceId) //
        .timestamp(devLog2time) //
        .level(devLog2level) //
        .title(devLog2title) //
        .message(devLog2msg);
    DeviceLogInfo devLog3 = new DeviceLogInfo() //
        .logId(devLog3Id) //
        .deviceId(deviceId2) //
        .timestamp(devLog3time) //
        .level(devLog3level) //
        .title(devLog3title) //
        .message(devLog3msg);

    Map<UUID, DeviceLogInfo> logMap = new HashMap<>();
    logMap.put(devLog1Id, devLog1);
    logMap.put(devLog2Id, devLog2);
    logMap.put(devLog3Id, devLog3);

    List<DeviceLogInfo> remainingLogs = Collections.emptyList();

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(deviceLogRepository.findManyById(any(Set.class))).thenReturn(remainingLogs);
    doReturn(logMap).when(deviceLogRepository).findAll();

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call Service
    CommonOperResponse operResp = deviceLogService.deleteSpecificLogs(deviceId);

    // Verify that appropriate Repository method was called
    verify(this.deviceLogRepository, times(2)).delete(arg1.capture());

    // Verify that appropriate Repository method was NOT called
    verify(this.deviceLogRepository, times(0)).deleteAll();

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertThat("The logIds passed are the only ones associated with the deviceId",
        arg1.getAllValues(), containsInAnyOrder(devLog1Id, devLog2Id));
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST deleteSpecificLogs (device log DB is empty(null))")
  void testDeleteSpecificLogsNoEntryNull() {
    UUID deviceId = UUID.fromString("11111111-1111-1111-1111-111111111111");

    Map<UUID, DeviceLogInfo> logMap = null;

    List<DeviceLogInfo> remainingLogs = Collections.emptyList();

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(deviceLogRepository.findManyById(any(Set.class))).thenReturn(remainingLogs);
    doReturn(logMap).when(deviceLogRepository).findAll();

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call Service
    CommonOperResponse operResp = deviceLogService.deleteSpecificLogs(deviceId);

    // Verify that appropriate Repository methods were NOT called
    verify(this.deviceLogRepository, times(0)).delete(arg1.capture());
    verify(this.deviceLogRepository, times(0)).deleteAll();

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST deleteSpecificLogs (device log DB is empty)")
  void testDeleteSpecificLogsNoEntry() {
    UUID deviceId = UUID.fromString("11111111-1111-1111-1111-111111111111");

    Map<UUID, DeviceLogInfo> logMap = Collections.emptyMap();

    List<DeviceLogInfo> remainingLogs = Collections.emptyList();

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(deviceLogRepository.findManyById(any(Set.class))).thenReturn(remainingLogs);
    doReturn(logMap).when(deviceLogRepository).findAll();

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call Service
    CommonOperResponse operResp = deviceLogService.deleteSpecificLogs(deviceId);

    // Verify that appropriate Repository methods were NOT called
    verify(this.deviceLogRepository, times(0)).delete(arg1.capture());
    verify(this.deviceLogRepository, times(0)).deleteAll();

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST deleteSpecificLogs (searching for the devices again returned null)")
  void testDeleteSpecificLogsRecheckReturnedNull() {
    UUID deviceId = UUID.fromString("11111111-1111-1111-1111-111111111111");
    UUID deviceId2 = UUID.fromString("22222222-2222-2222-2222-222222222222");
    UUID devLog1Id = UUID.fromString("4f985f64-0314-0361-032c-2b963f789af1");
    UUID devLog2Id = UUID.fromString("5b785f64-5147-4013-b3fc-48963f50ace2");
    UUID devLog3Id = UUID.fromString("2a085f64-842b-3102-9990-6c963f1b3bd3");
    OffsetDateTime devLog1time = OffsetDateTime.now().minusDays(2);
    OffsetDateTime devLog2time = OffsetDateTime.now().minusDays(3);
    OffsetDateTime devLog3time = OffsetDateTime.now();
    CommonLogLevel devLog1level = CommonLogLevel.CRITICAL;
    CommonLogLevel devLog2level = CommonLogLevel.WARNING;
    CommonLogLevel devLog3level = CommonLogLevel.DEBUG;
    String devLog1title = "A fatal error occurred.";
    String devLog2title = "Warning. Something is misconfigured.";
    String devLog3title = "value check: deviceInfo = null";
    String devLog1msg = "NullPointerException on ...test";
    String devLog2msg = "Multiple port values are present.";
    String devLog3msg = "deviceInfo = null";
    DeviceLogInfo devLog1 = new DeviceLogInfo() //
        .logId(devLog1Id) //
        .deviceId(deviceId) //
        .timestamp(devLog1time) //
        .level(devLog1level) //
        .title(devLog1title) //
        .message(devLog1msg);
    DeviceLogInfo devLog2 = new DeviceLogInfo() //
        .logId(devLog2Id) //
        .deviceId(deviceId) //
        .timestamp(devLog2time) //
        .level(devLog2level) //
        .title(devLog2title) //
        .message(devLog2msg);
    DeviceLogInfo devLog3 = new DeviceLogInfo() //
        .logId(devLog3Id) //
        .deviceId(deviceId2) //
        .timestamp(devLog3time) //
        .level(devLog3level) //
        .title(devLog3title) //
        .message(devLog3msg);

    Map<UUID, DeviceLogInfo> logMap = new HashMap<>();
    logMap.put(devLog1Id, devLog1);
    logMap.put(devLog2Id, devLog2);
    logMap.put(devLog3Id, devLog3);

    List<DeviceLogInfo> remainingLogs = null;

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(deviceLogRepository.findManyById(any(Set.class))).thenReturn(remainingLogs);
    doReturn(logMap).when(deviceLogRepository).findAll();

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call Service
    CommonOperResponse operResp = deviceLogService.deleteSpecificLogs(deviceId);

    // Verify that appropriate Repository method was called
    verify(this.deviceLogRepository, times(2)).delete(arg1.capture());

    // Verify that appropriate Repository method was NOT called
    verify(this.deviceLogRepository, times(0)).deleteAll();

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertThat("The logIds passed are the only ones associated with the deviceId",
        arg1.getAllValues(), containsInAnyOrder(devLog1Id, devLog2Id));
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST deleteSpecificLogs (there are remaining logs not deleted)")
  void testDeleteSpecificLogsRecheckReturnedSomething() {
    UUID deviceId = UUID.fromString("11111111-1111-1111-1111-111111111111");
    UUID deviceId2 = UUID.fromString("22222222-2222-2222-2222-222222222222");
    UUID devLog1Id = UUID.fromString("4f985f64-0314-0361-032c-2b963f789af1");
    UUID devLog2Id = UUID.fromString("5b785f64-5147-4013-b3fc-48963f50ace2");
    UUID devLog3Id = UUID.fromString("2a085f64-842b-3102-9990-6c963f1b3bd3");
    OffsetDateTime devLog1time = OffsetDateTime.now().minusDays(2);
    OffsetDateTime devLog2time = OffsetDateTime.now().minusDays(3);
    OffsetDateTime devLog3time = OffsetDateTime.now();
    CommonLogLevel devLog1level = CommonLogLevel.CRITICAL;
    CommonLogLevel devLog2level = CommonLogLevel.WARNING;
    CommonLogLevel devLog3level = CommonLogLevel.DEBUG;
    String devLog1title = "A fatal error occurred.";
    String devLog2title = "Warning. Something is misconfigured.";
    String devLog3title = "value check: deviceInfo = null";
    String devLog1msg = "NullPointerException on ...test";
    String devLog2msg = "Multiple port values are present.";
    String devLog3msg = "deviceInfo = null";
    DeviceLogInfo devLog1 = new DeviceLogInfo() //
        .logId(devLog1Id) //
        .deviceId(deviceId) //
        .timestamp(devLog1time) //
        .level(devLog1level) //
        .title(devLog1title) //
        .message(devLog1msg);
    DeviceLogInfo devLog2 = new DeviceLogInfo() //
        .logId(devLog2Id) //
        .deviceId(deviceId) //
        .timestamp(devLog2time) //
        .level(devLog2level) //
        .title(devLog2title) //
        .message(devLog2msg);
    DeviceLogInfo devLog3 = new DeviceLogInfo() //
        .logId(devLog3Id) //
        .deviceId(deviceId2) //
        .timestamp(devLog3time) //
        .level(devLog3level) //
        .title(devLog3title) //
        .message(devLog3msg);

    Map<UUID, DeviceLogInfo> logMap = new HashMap<>();
    logMap.put(devLog1Id, devLog1);
    logMap.put(devLog2Id, devLog2);
    logMap.put(devLog3Id, devLog3);

    List<DeviceLogInfo> remainingLogs = new ArrayList<>();
    remainingLogs.add(devLog2);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = String.format("Cannot delete all log entries for device %s.", deviceId);
    String expectedDetail =
        "Some or all device log entries for this device may not have been deleted.";

    // Mock(s)
    when(deviceLogRepository.findManyById(any(Set.class))).thenReturn(remainingLogs);
    doReturn(logMap).when(deviceLogRepository).findAll();

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call Service
    CommonOperResponse operResp = deviceLogService.deleteSpecificLogs(deviceId);

    // Verify that appropriate Repository method was called
    verify(this.deviceLogRepository, times(2)).delete(arg1.capture());

    // Verify that appropriate Repository method was NOT called
    verify(this.deviceLogRepository, times(0)).deleteAll();

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertThat("The logIds passed are the only ones associated with the deviceId",
        arg1.getAllValues(), containsInAnyOrder(devLog1Id, devLog2Id));
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 500.");
    assertNotNull(operResp.getErrorResult(), "There should be an error returned.");
    assertNotNull(operResp.getErrorResult().getLogId(), "Error object must have logId provided.");
    assertNotNull(operResp.getErrorResult().getTimestamp(),
        "Error object must have timestamp provided.");
    assertEquals(ErrorType.ERROR, operResp.getErrorResult().getType(),
        "Error object must have type = Error");
    assertNotNull(operResp.getErrorResult().getType(), "Error object must have logId provided.");
    assertEquals(expectedErrMsg, operResp.getErrorResult().getMessage(),
        "Error Message must match.");
    assertEquals(expectedDetail, operResp.getErrorResult().getDetails(),
        "Error Details must match.");
  }

  @Test
  @DisplayName("TEST deleteSpecificLogs (recheck returned all nulls)")
  void testDeleteSpecificLogsRecheckReturnedAllNull() {
    UUID deviceId = UUID.fromString("11111111-1111-1111-1111-111111111111");
    UUID deviceId2 = UUID.fromString("22222222-2222-2222-2222-222222222222");
    UUID devLog1Id = UUID.fromString("4f985f64-0314-0361-032c-2b963f789af1");
    UUID devLog2Id = UUID.fromString("5b785f64-5147-4013-b3fc-48963f50ace2");
    UUID devLog3Id = UUID.fromString("2a085f64-842b-3102-9990-6c963f1b3bd3");
    OffsetDateTime devLog1time = OffsetDateTime.now().minusDays(2);
    OffsetDateTime devLog2time = OffsetDateTime.now().minusDays(3);
    OffsetDateTime devLog3time = OffsetDateTime.now();
    CommonLogLevel devLog1level = CommonLogLevel.CRITICAL;
    CommonLogLevel devLog2level = CommonLogLevel.WARNING;
    CommonLogLevel devLog3level = CommonLogLevel.DEBUG;
    String devLog1title = "A fatal error occurred.";
    String devLog2title = "Warning. Something is misconfigured.";
    String devLog3title = "value check: deviceInfo = null";
    String devLog1msg = "NullPointerException on ...test";
    String devLog2msg = "Multiple port values are present.";
    String devLog3msg = "deviceInfo = null";
    DeviceLogInfo devLog1 = new DeviceLogInfo() //
        .logId(devLog1Id) //
        .deviceId(deviceId) //
        .timestamp(devLog1time) //
        .level(devLog1level) //
        .title(devLog1title) //
        .message(devLog1msg);
    DeviceLogInfo devLog2 = new DeviceLogInfo() //
        .logId(devLog2Id) //
        .deviceId(deviceId) //
        .timestamp(devLog2time) //
        .level(devLog2level) //
        .title(devLog2title) //
        .message(devLog2msg);
    DeviceLogInfo devLog3 = new DeviceLogInfo() //
        .logId(devLog3Id) //
        .deviceId(deviceId2) //
        .timestamp(devLog3time) //
        .level(devLog3level) //
        .title(devLog3title) //
        .message(devLog3msg);

    Map<UUID, DeviceLogInfo> logMap = new HashMap<>();
    logMap.put(devLog1Id, devLog1);
    logMap.put(devLog2Id, devLog2);
    logMap.put(devLog3Id, devLog3);

    List<DeviceLogInfo> remainingLogs = new ArrayList<>();
    remainingLogs.add(null);
    remainingLogs.add(null);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(deviceLogRepository.findManyById(any(Set.class))).thenReturn(remainingLogs);
    doReturn(logMap).when(deviceLogRepository).findAll();

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call Service
    CommonOperResponse operResp = deviceLogService.deleteSpecificLogs(deviceId);

    // Verify that appropriate Repository method was called
    verify(this.deviceLogRepository, times(2)).delete(arg1.capture());

    // Verify that appropriate Repository method was NOT called
    verify(this.deviceLogRepository, times(0)).deleteAll();

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertThat("The logIds passed are the only ones associated with the deviceId",
        arg1.getAllValues(), containsInAnyOrder(devLog1Id, devLog2Id));
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }
}
