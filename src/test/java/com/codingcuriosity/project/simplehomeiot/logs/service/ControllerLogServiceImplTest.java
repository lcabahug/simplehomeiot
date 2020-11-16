package com.codingcuriosity.project.simplehomeiot.logs.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonGetListResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonOperResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.ErrorType;
import com.codingcuriosity.project.simplehomeiot.logs.model.CommonLogLevel;
import com.codingcuriosity.project.simplehomeiot.logs.model.ControllerLogInfo;
import com.codingcuriosity.project.simplehomeiot.logs.repository.ControllerLogRepository;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
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
public class ControllerLogServiceImplTest {

  @Autowired
  ControllerLogService controllerLogService;

  @MockBean
  ControllerLogRepository controllerLogRepository;

  @Test
  @DisplayName("TEST getLogRepo")
  void testGetLogRepo() {

    // Call Service
    ControllerLogRepository logRepository = this.controllerLogService.getLogRepo();

    // Assertions
    assertEquals(this.controllerLogRepository, logRepository, "LogRepository must be identical.");
  }

  @Test
  @DisplayName("TEST getLogInfo (all parameters = null)")
  void testGetLogInfo() {
    Integer skip = null;
    Integer limit = null;

    UUID contrLog1Id = UUID.fromString("4f985f64-0314-0361-032c-2b963f789af1");
    UUID contrLog2Id = UUID.fromString("5b785f64-5147-4013-b3fc-48963f50ace2");
    UUID contrLog3Id = UUID.fromString("2a085f64-842b-3102-9990-6c963f1b3bd3");
    OffsetDateTime contrLog1time = OffsetDateTime.now().minusDays(2);
    OffsetDateTime contrLog2time = OffsetDateTime.now().minusDays(3);
    OffsetDateTime contrLog3time = OffsetDateTime.now();
    CommonLogLevel contrLog1level = CommonLogLevel.CRITICAL;
    CommonLogLevel contrLog2level = CommonLogLevel.WARNING;
    CommonLogLevel contrLog3level = CommonLogLevel.DEBUG;
    String contrLog1title = "A fatal error occurred.";
    String contrLog2title = "Warning. Something is misconfigured.";
    String contrLog3title = "value check: deviceInfo = null";
    String contrLog1msg = "NullPointerException on ...test";
    String contrLog2msg = "Multiple port values are present.";
    String contrLog3msg = "deviceInfo = null";
    ControllerLogInfo contrLog1 = new ControllerLogInfo() //
        .logId(contrLog1Id) //
        .timestamp(contrLog1time) //
        .level(contrLog1level) //
        .title(contrLog1title) //
        .message(contrLog1msg);
    ControllerLogInfo contrLog2 = new ControllerLogInfo() //
        .logId(contrLog2Id) //
        .timestamp(contrLog2time) //
        .level(contrLog2level) //
        .title(contrLog2title) //
        .message(contrLog2msg);
    ControllerLogInfo contrLog3 = new ControllerLogInfo() //
        .logId(contrLog3Id) //
        .timestamp(contrLog3time) //
        .level(contrLog3level) //
        .title(contrLog3title) //
        .message(contrLog3msg);

    Map<UUID, ControllerLogInfo> logMap = new HashMap<>();
    logMap.put(contrLog1Id, contrLog1);
    logMap.put(contrLog2Id, contrLog2);
    logMap.put(contrLog3Id, contrLog3);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(logMap).when(controllerLogRepository).findAll();

    // Call service
    CommonGetListResponse<ControllerLogInfo> retListResp =
        controllerLogService.getLogInfo(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.controllerLogRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be sorted by timestamp ascending.", retListResp.getObjList(),
        contains(contrLog2, contrLog1, contrLog3));
    assertEquals(contrLog2Id, retListResp.getObjList().get(0).getLogId(),
        "1st entry's UUID must match log 2's");
    assertEquals(contrLog2time, retListResp.getObjList().get(0).getTimestamp(),
        "1st entry's timestamp must match log 2's");
    assertEquals(contrLog2level, retListResp.getObjList().get(0).getLevel(),
        "1st entry's level must match log 2's");
    assertEquals(contrLog2title, retListResp.getObjList().get(0).getTitle(),
        "1st entry's title must match log 2's");
    assertEquals(contrLog2msg, retListResp.getObjList().get(0).getMessage(),
        "1st entry's message must match log 2's");
    assertEquals(contrLog1Id, retListResp.getObjList().get(1).getLogId(),
        "2nd entry's UUID must match log 1's");
    assertEquals(contrLog1time, retListResp.getObjList().get(1).getTimestamp(),
        "2nd entry's timestamp must match log 1's");
    assertEquals(contrLog1level, retListResp.getObjList().get(1).getLevel(),
        "2nd entry's level must match log 1's");
    assertEquals(contrLog1title, retListResp.getObjList().get(1).getTitle(),
        "2nd entry's title must match log 1's");
    assertEquals(contrLog1msg, retListResp.getObjList().get(1).getMessage(),
        "2nd entry's message must match log 1's");
    assertEquals(contrLog3Id, retListResp.getObjList().get(2).getLogId(),
        "3rd entry's UUID must match log 3's");
    assertEquals(contrLog3time, retListResp.getObjList().get(2).getTimestamp(),
        "3rd entry's timestamp must match log 3's");
    assertEquals(contrLog3level, retListResp.getObjList().get(2).getLevel(),
        "3rd entry's level must match log 3's");
    assertEquals(contrLog3title, retListResp.getObjList().get(2).getTitle(),
        "3rd entry's title must match log 3's");
    assertEquals(contrLog3msg, retListResp.getObjList().get(2).getMessage(),
        "3rd entry's message must match log 3's");
  }

  @Test
  @DisplayName("TEST getLogInfo (skip = 0, limit = 0)")
  void testGetLogInfoSkipMinLimitMin() {
    Integer skip = 0;
    Integer limit = 0;

    UUID contrLog1Id = UUID.fromString("4f985f64-0314-0361-032c-2b963f789af1");
    UUID contrLog2Id = UUID.fromString("5b785f64-5147-4013-b3fc-48963f50ace2");
    UUID contrLog3Id = UUID.fromString("2a085f64-842b-3102-9990-6c963f1b3bd3");
    OffsetDateTime contrLog1time = OffsetDateTime.now().minusDays(2);
    OffsetDateTime contrLog2time = OffsetDateTime.now().minusDays(3);
    OffsetDateTime contrLog3time = OffsetDateTime.now();
    CommonLogLevel contrLog1level = CommonLogLevel.CRITICAL;
    CommonLogLevel contrLog2level = CommonLogLevel.WARNING;
    CommonLogLevel contrLog3level = CommonLogLevel.DEBUG;
    String contrLog1title = "A fatal error occurred.";
    String contrLog2title = "Warning. Something is misconfigured.";
    String contrLog3title = "value check: deviceInfo = null";
    String contrLog1msg = "NullPointerException on ...test";
    String contrLog2msg = "Multiple port values are present.";
    String contrLog3msg = "deviceInfo = null";
    ControllerLogInfo contrLog1 = new ControllerLogInfo() //
        .logId(contrLog1Id) //
        .timestamp(contrLog1time) //
        .level(contrLog1level) //
        .title(contrLog1title) //
        .message(contrLog1msg);
    ControllerLogInfo contrLog2 = new ControllerLogInfo() //
        .logId(contrLog2Id) //
        .timestamp(contrLog2time) //
        .level(contrLog2level) //
        .title(contrLog2title) //
        .message(contrLog2msg);
    ControllerLogInfo contrLog3 = new ControllerLogInfo() //
        .logId(contrLog3Id) //
        .timestamp(contrLog3time) //
        .level(contrLog3level) //
        .title(contrLog3title) //
        .message(contrLog3msg);

    Map<UUID, ControllerLogInfo> logMap = new HashMap<>();
    logMap.put(contrLog1Id, contrLog1);
    logMap.put(contrLog2Id, contrLog2);
    logMap.put(contrLog3Id, contrLog3);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(logMap).when(controllerLogRepository).findAll();

    // Call service
    CommonGetListResponse<ControllerLogInfo> retListResp =
        controllerLogService.getLogInfo(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.controllerLogRepository, times(1)).findAll();

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
    Integer skip = null;
    Integer limit = null; // Will be changed later

    UUID contrLog1Id = UUID.fromString("4f985f64-0314-0361-032c-2b963f789af1");
    UUID contrLog2Id = UUID.fromString("5b785f64-5147-4013-b3fc-48963f50ace2");
    UUID contrLog3Id = UUID.fromString("2a085f64-842b-3102-9990-6c963f1b3bd3");
    OffsetDateTime contrLog1time = OffsetDateTime.now().minusDays(2);
    OffsetDateTime contrLog2time = OffsetDateTime.now().minusDays(3);
    OffsetDateTime contrLog3time = OffsetDateTime.now();
    CommonLogLevel contrLog1level = CommonLogLevel.CRITICAL;
    CommonLogLevel contrLog2level = CommonLogLevel.WARNING;
    CommonLogLevel contrLog3level = CommonLogLevel.DEBUG;
    String contrLog1title = "A fatal error occurred.";
    String contrLog2title = "Warning. Something is misconfigured.";
    String contrLog3title = "value check: deviceInfo = null";
    String contrLog1msg = "NullPointerException on ...test";
    String contrLog2msg = "Multiple port values are present.";
    String contrLog3msg = "deviceInfo = null";
    ControllerLogInfo contrLog1 = new ControllerLogInfo() //
        .logId(contrLog1Id) //
        .timestamp(contrLog1time) //
        .level(contrLog1level) //
        .title(contrLog1title) //
        .message(contrLog1msg);
    ControllerLogInfo contrLog2 = new ControllerLogInfo() //
        .logId(contrLog2Id) //
        .timestamp(contrLog2time) //
        .level(contrLog2level) //
        .title(contrLog2title) //
        .message(contrLog2msg);
    ControllerLogInfo contrLog3 = new ControllerLogInfo() //
        .logId(contrLog3Id) //
        .timestamp(contrLog3time) //
        .level(contrLog3level) //
        .title(contrLog3title) //
        .message(contrLog3msg);

    Map<UUID, ControllerLogInfo> logMap = new HashMap<>();
    logMap.put(contrLog1Id, contrLog1);
    logMap.put(contrLog2Id, contrLog2);
    logMap.put(contrLog3Id, contrLog3);

    limit = logMap.size();

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(logMap).when(controllerLogRepository).findAll();

    // Call service
    CommonGetListResponse<ControllerLogInfo> retListResp =
        controllerLogService.getLogInfo(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.controllerLogRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be sorted by timestamp ascending.", retListResp.getObjList(),
        contains(contrLog2, contrLog1, contrLog3));
  }

  @Test
  @DisplayName("TEST getLogInfo (skip = entry max)")
  void testGetLogInfoSkipMax() {
    Integer skip = null; // Will be changed later
    Integer limit = null;

    UUID contrLog1Id = UUID.fromString("4f985f64-0314-0361-032c-2b963f789af1");
    UUID contrLog2Id = UUID.fromString("5b785f64-5147-4013-b3fc-48963f50ace2");
    UUID contrLog3Id = UUID.fromString("2a085f64-842b-3102-9990-6c963f1b3bd3");
    OffsetDateTime contrLog1time = OffsetDateTime.now().minusDays(2);
    OffsetDateTime contrLog2time = OffsetDateTime.now().minusDays(3);
    OffsetDateTime contrLog3time = OffsetDateTime.now();
    CommonLogLevel contrLog1level = CommonLogLevel.CRITICAL;
    CommonLogLevel contrLog2level = CommonLogLevel.WARNING;
    CommonLogLevel contrLog3level = CommonLogLevel.DEBUG;
    String contrLog1title = "A fatal error occurred.";
    String contrLog2title = "Warning. Something is misconfigured.";
    String contrLog3title = "value check: deviceInfo = null";
    String contrLog1msg = "NullPointerException on ...test";
    String contrLog2msg = "Multiple port values are present.";
    String contrLog3msg = "deviceInfo = null";
    ControllerLogInfo contrLog1 = new ControllerLogInfo() //
        .logId(contrLog1Id) //
        .timestamp(contrLog1time) //
        .level(contrLog1level) //
        .title(contrLog1title) //
        .message(contrLog1msg);
    ControllerLogInfo contrLog2 = new ControllerLogInfo() //
        .logId(contrLog2Id) //
        .timestamp(contrLog2time) //
        .level(contrLog2level) //
        .title(contrLog2title) //
        .message(contrLog2msg);
    ControllerLogInfo contrLog3 = new ControllerLogInfo() //
        .logId(contrLog3Id) //
        .timestamp(contrLog3time) //
        .level(contrLog3level) //
        .title(contrLog3title) //
        .message(contrLog3msg);

    Map<UUID, ControllerLogInfo> logMap = new HashMap<>();
    logMap.put(contrLog1Id, contrLog1);
    logMap.put(contrLog2Id, contrLog2);
    logMap.put(contrLog3Id, contrLog3);

    skip = logMap.size();

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(logMap).when(controllerLogRepository).findAll();

    // Call service
    CommonGetListResponse<ControllerLogInfo> retListResp =
        controllerLogService.getLogInfo(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.controllerLogRepository, times(1)).findAll();

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

    UUID contrLog1Id = UUID.fromString("4f985f64-0314-0361-032c-2b963f789af1");
    UUID contrLog2Id = UUID.fromString("5b785f64-5147-4013-b3fc-48963f50ace2");
    UUID contrLog3Id = UUID.fromString("2a085f64-842b-3102-9990-6c963f1b3bd3");
    OffsetDateTime contrLog1time = OffsetDateTime.now().minusDays(2);
    OffsetDateTime contrLog2time = OffsetDateTime.now().minusDays(3);
    OffsetDateTime contrLog3time = OffsetDateTime.now();
    CommonLogLevel contrLog1level = CommonLogLevel.CRITICAL;
    CommonLogLevel contrLog2level = CommonLogLevel.WARNING;
    CommonLogLevel contrLog3level = CommonLogLevel.DEBUG;
    String contrLog1title = "A fatal error occurred.";
    String contrLog2title = "Warning. Something is misconfigured.";
    String contrLog3title = "value check: deviceInfo = null";
    String contrLog1msg = "NullPointerException on ...test";
    String contrLog2msg = "Multiple port values are present.";
    String contrLog3msg = "deviceInfo = null";
    ControllerLogInfo contrLog1 = new ControllerLogInfo() //
        .logId(contrLog1Id) //
        .timestamp(contrLog1time) //
        .level(contrLog1level) //
        .title(contrLog1title) //
        .message(contrLog1msg);
    ControllerLogInfo contrLog2 = new ControllerLogInfo() //
        .logId(contrLog2Id) //
        .timestamp(contrLog2time) //
        .level(contrLog2level) //
        .title(contrLog2title) //
        .message(contrLog2msg);
    ControllerLogInfo contrLog3 = new ControllerLogInfo() //
        .logId(contrLog3Id) //
        .timestamp(contrLog3time) //
        .level(contrLog3level) //
        .title(contrLog3title) //
        .message(contrLog3msg);

    Map<UUID, ControllerLogInfo> logMap = new HashMap<>();
    logMap.put(contrLog1Id, contrLog1);
    logMap.put(contrLog2Id, contrLog2);
    logMap.put(contrLog3Id, contrLog3);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    doReturn(logMap).when(controllerLogRepository).findAll();

    // Call service
    CommonGetListResponse<ControllerLogInfo> retListResp =
        controllerLogService.getLogInfo(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.controllerLogRepository, times(1)).findAll();

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

    UUID contrLog1Id = UUID.fromString("4f985f64-0314-0361-032c-2b963f789af1");
    UUID contrLog2Id = UUID.fromString("5b785f64-5147-4013-b3fc-48963f50ace2");
    UUID contrLog3Id = UUID.fromString("2a085f64-842b-3102-9990-6c963f1b3bd3");
    OffsetDateTime contrLog1time = OffsetDateTime.now().minusDays(2);
    OffsetDateTime contrLog2time = OffsetDateTime.now().minusDays(3);
    OffsetDateTime contrLog3time = OffsetDateTime.now();
    CommonLogLevel contrLog1level = CommonLogLevel.CRITICAL;
    CommonLogLevel contrLog2level = CommonLogLevel.WARNING;
    CommonLogLevel contrLog3level = CommonLogLevel.DEBUG;
    String contrLog1title = "A fatal error occurred.";
    String contrLog2title = "Warning. Something is misconfigured.";
    String contrLog3title = "value check: deviceInfo = null";
    String contrLog1msg = "NullPointerException on ...test";
    String contrLog2msg = "Multiple port values are present.";
    String contrLog3msg = "deviceInfo = null";
    ControllerLogInfo contrLog1 = new ControllerLogInfo() //
        .logId(contrLog1Id) //
        .timestamp(contrLog1time) //
        .level(contrLog1level) //
        .title(contrLog1title) //
        .message(contrLog1msg);
    ControllerLogInfo contrLog2 = new ControllerLogInfo() //
        .logId(contrLog2Id) //
        .timestamp(contrLog2time) //
        .level(contrLog2level) //
        .title(contrLog2title) //
        .message(contrLog2msg);
    ControllerLogInfo contrLog3 = new ControllerLogInfo() //
        .logId(contrLog3Id) //
        .timestamp(contrLog3time) //
        .level(contrLog3level) //
        .title(contrLog3title) //
        .message(contrLog3msg);

    Map<UUID, ControllerLogInfo> logMap = new HashMap<>();
    logMap.put(contrLog1Id, contrLog1);
    logMap.put(contrLog2Id, contrLog2);
    logMap.put(contrLog3Id, contrLog3);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    doReturn(logMap).when(controllerLogRepository).findAll();

    // Call service
    CommonGetListResponse<ControllerLogInfo> retListResp =
        controllerLogService.getLogInfo(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.controllerLogRepository, times(1)).findAll();

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

    UUID contrLog1Id = UUID.fromString("4f985f64-0314-0361-032c-2b963f789af1");
    UUID contrLog2Id = UUID.fromString("5b785f64-5147-4013-b3fc-48963f50ace2");
    UUID contrLog3Id = UUID.fromString("2a085f64-842b-3102-9990-6c963f1b3bd3");
    OffsetDateTime contrLog1time = OffsetDateTime.now().minusDays(2);
    OffsetDateTime contrLog2time = OffsetDateTime.now().minusDays(3);
    OffsetDateTime contrLog3time = OffsetDateTime.now();
    CommonLogLevel contrLog1level = CommonLogLevel.CRITICAL;
    CommonLogLevel contrLog2level = CommonLogLevel.WARNING;
    CommonLogLevel contrLog3level = CommonLogLevel.DEBUG;
    String contrLog1title = "A fatal error occurred.";
    String contrLog2title = "Warning. Something is misconfigured.";
    String contrLog3title = "value check: deviceInfo = null";
    String contrLog1msg = "NullPointerException on ...test";
    String contrLog2msg = "Multiple port values are present.";
    String contrLog3msg = "deviceInfo = null";
    ControllerLogInfo contrLog1 = new ControllerLogInfo() //
        .logId(contrLog1Id) //
        .timestamp(contrLog1time) //
        .level(contrLog1level) //
        .title(contrLog1title) //
        .message(contrLog1msg);
    ControllerLogInfo contrLog2 = new ControllerLogInfo() //
        .logId(contrLog2Id) //
        .timestamp(contrLog2time) //
        .level(contrLog2level) //
        .title(contrLog2title) //
        .message(contrLog2msg);
    ControllerLogInfo contrLog3 = new ControllerLogInfo() //
        .logId(contrLog3Id) //
        .timestamp(contrLog3time) //
        .level(contrLog3level) //
        .title(contrLog3title) //
        .message(contrLog3msg);

    Map<UUID, ControllerLogInfo> logMap = new HashMap<>();
    logMap.put(contrLog1Id, contrLog1);
    logMap.put(contrLog2Id, contrLog2);
    logMap.put(contrLog3Id, contrLog3);

    skip = logMap.size() + 1;

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(logMap).when(controllerLogRepository).findAll();

    // Call service
    CommonGetListResponse<ControllerLogInfo> retListResp =
        controllerLogService.getLogInfo(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.controllerLogRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getLogInfo (limit = entry max + 1)")
  void testGetLogInfoLimitMaxPlus1() {
    Integer skip = null;
    Integer limit = null; // Will be changed later

    UUID contrLog1Id = UUID.fromString("4f985f64-0314-0361-032c-2b963f789af1");
    UUID contrLog2Id = UUID.fromString("5b785f64-5147-4013-b3fc-48963f50ace2");
    UUID contrLog3Id = UUID.fromString("2a085f64-842b-3102-9990-6c963f1b3bd3");
    OffsetDateTime contrLog1time = OffsetDateTime.now().minusDays(2);
    OffsetDateTime contrLog2time = OffsetDateTime.now().minusDays(3);
    OffsetDateTime contrLog3time = OffsetDateTime.now();
    CommonLogLevel contrLog1level = CommonLogLevel.CRITICAL;
    CommonLogLevel contrLog2level = CommonLogLevel.WARNING;
    CommonLogLevel contrLog3level = CommonLogLevel.DEBUG;
    String contrLog1title = "A fatal error occurred.";
    String contrLog2title = "Warning. Something is misconfigured.";
    String contrLog3title = "value check: deviceInfo = null";
    String contrLog1msg = "NullPointerException on ...test";
    String contrLog2msg = "Multiple port values are present.";
    String contrLog3msg = "deviceInfo = null";
    ControllerLogInfo contrLog1 = new ControllerLogInfo() //
        .logId(contrLog1Id) //
        .timestamp(contrLog1time) //
        .level(contrLog1level) //
        .title(contrLog1title) //
        .message(contrLog1msg);
    ControllerLogInfo contrLog2 = new ControllerLogInfo() //
        .logId(contrLog2Id) //
        .timestamp(contrLog2time) //
        .level(contrLog2level) //
        .title(contrLog2title) //
        .message(contrLog2msg);
    ControllerLogInfo contrLog3 = new ControllerLogInfo() //
        .logId(contrLog3Id) //
        .timestamp(contrLog3time) //
        .level(contrLog3level) //
        .title(contrLog3title) //
        .message(contrLog3msg);

    Map<UUID, ControllerLogInfo> logMap = new HashMap<>();
    logMap.put(contrLog1Id, contrLog1);
    logMap.put(contrLog2Id, contrLog2);
    logMap.put(contrLog3Id, contrLog3);

    limit = logMap.size() + 1;

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(logMap).when(controllerLogRepository).findAll();

    // Call service
    CommonGetListResponse<ControllerLogInfo> retListResp =
        controllerLogService.getLogInfo(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.controllerLogRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be sorted by timestamp ascending.", retListResp.getObjList(),
        contains(contrLog2, contrLog1, contrLog3));
  }

  @Test
  @DisplayName("TEST getLogInfo (skip = 1, limit = 1)")
  void testGetLogInfoSkip1Limit1() {
    Integer skip = 1;
    Integer limit = 1;

    UUID contrLog1Id = UUID.fromString("4f985f64-0314-0361-032c-2b963f789af1");
    UUID contrLog2Id = UUID.fromString("5b785f64-5147-4013-b3fc-48963f50ace2");
    UUID contrLog3Id = UUID.fromString("2a085f64-842b-3102-9990-6c963f1b3bd3");
    OffsetDateTime contrLog1time = OffsetDateTime.now().minusDays(2);
    OffsetDateTime contrLog2time = OffsetDateTime.now().minusDays(3);
    OffsetDateTime contrLog3time = OffsetDateTime.now();
    CommonLogLevel contrLog1level = CommonLogLevel.CRITICAL;
    CommonLogLevel contrLog2level = CommonLogLevel.WARNING;
    CommonLogLevel contrLog3level = CommonLogLevel.DEBUG;
    String contrLog1title = "A fatal error occurred.";
    String contrLog2title = "Warning. Something is misconfigured.";
    String contrLog3title = "value check: deviceInfo = null";
    String contrLog1msg = "NullPointerException on ...test";
    String contrLog2msg = "Multiple port values are present.";
    String contrLog3msg = "deviceInfo = null";
    ControllerLogInfo contrLog1 = new ControllerLogInfo() //
        .logId(contrLog1Id) //
        .timestamp(contrLog1time) //
        .level(contrLog1level) //
        .title(contrLog1title) //
        .message(contrLog1msg);
    ControllerLogInfo contrLog2 = new ControllerLogInfo() //
        .logId(contrLog2Id) //
        .timestamp(contrLog2time) //
        .level(contrLog2level) //
        .title(contrLog2title) //
        .message(contrLog2msg);
    ControllerLogInfo contrLog3 = new ControllerLogInfo() //
        .logId(contrLog3Id) //
        .timestamp(contrLog3time) //
        .level(contrLog3level) //
        .title(contrLog3title) //
        .message(contrLog3msg);

    Map<UUID, ControllerLogInfo> logMap = new HashMap<>();
    logMap.put(contrLog1Id, contrLog1);
    logMap.put(contrLog2Id, contrLog2);
    logMap.put(contrLog3Id, contrLog3);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(logMap).when(controllerLogRepository).findAll();

    // Call service
    CommonGetListResponse<ControllerLogInfo> retListResp =
        controllerLogService.getLogInfo(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.controllerLogRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must only be one, the second entry only.",
        retListResp.getObjList(), contains(contrLog1));
  }

  @Test
  @DisplayName("TEST getLogInfo (skip = 2)")
  void testGetLogInfoSkip2() {
    Integer skip = 2;
    Integer limit = null;

    UUID contrLog1Id = UUID.fromString("4f985f64-0314-0361-032c-2b963f789af1");
    UUID contrLog2Id = UUID.fromString("5b785f64-5147-4013-b3fc-48963f50ace2");
    UUID contrLog3Id = UUID.fromString("2a085f64-842b-3102-9990-6c963f1b3bd3");
    OffsetDateTime contrLog1time = OffsetDateTime.now().minusDays(2);
    OffsetDateTime contrLog2time = OffsetDateTime.now().minusDays(3);
    OffsetDateTime contrLog3time = OffsetDateTime.now();
    CommonLogLevel contrLog1level = CommonLogLevel.CRITICAL;
    CommonLogLevel contrLog2level = CommonLogLevel.WARNING;
    CommonLogLevel contrLog3level = CommonLogLevel.DEBUG;
    String contrLog1title = "A fatal error occurred.";
    String contrLog2title = "Warning. Something is misconfigured.";
    String contrLog3title = "value check: deviceInfo = null";
    String contrLog1msg = "NullPointerException on ...test";
    String contrLog2msg = "Multiple port values are present.";
    String contrLog3msg = "deviceInfo = null";
    ControllerLogInfo contrLog1 = new ControllerLogInfo() //
        .logId(contrLog1Id) //
        .timestamp(contrLog1time) //
        .level(contrLog1level) //
        .title(contrLog1title) //
        .message(contrLog1msg);
    ControllerLogInfo contrLog2 = new ControllerLogInfo() //
        .logId(contrLog2Id) //
        .timestamp(contrLog2time) //
        .level(contrLog2level) //
        .title(contrLog2title) //
        .message(contrLog2msg);
    ControllerLogInfo contrLog3 = new ControllerLogInfo() //
        .logId(contrLog3Id) //
        .timestamp(contrLog3time) //
        .level(contrLog3level) //
        .title(contrLog3title) //
        .message(contrLog3msg);

    Map<UUID, ControllerLogInfo> logMap = new HashMap<>();
    logMap.put(contrLog1Id, contrLog1);
    logMap.put(contrLog2Id, contrLog2);
    logMap.put(contrLog3Id, contrLog3);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(logMap).when(controllerLogRepository).findAll();

    // Call service
    CommonGetListResponse<ControllerLogInfo> retListResp =
        controllerLogService.getLogInfo(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.controllerLogRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must only be one, the last entry only.", retListResp.getObjList(),
        contains(contrLog3));
  }

  @Test
  @DisplayName("TEST getLogInfo (limit = 2)")
  void testGetLogInfoLimit2() {
    Integer skip = null;
    Integer limit = 2;

    UUID contrLog1Id = UUID.fromString("4f985f64-0314-0361-032c-2b963f789af1");
    UUID contrLog2Id = UUID.fromString("5b785f64-5147-4013-b3fc-48963f50ace2");
    UUID contrLog3Id = UUID.fromString("2a085f64-842b-3102-9990-6c963f1b3bd3");
    OffsetDateTime contrLog1time = OffsetDateTime.now().minusDays(2);
    OffsetDateTime contrLog2time = OffsetDateTime.now().minusDays(3);
    OffsetDateTime contrLog3time = OffsetDateTime.now();
    CommonLogLevel contrLog1level = CommonLogLevel.CRITICAL;
    CommonLogLevel contrLog2level = CommonLogLevel.WARNING;
    CommonLogLevel contrLog3level = CommonLogLevel.DEBUG;
    String contrLog1title = "A fatal error occurred.";
    String contrLog2title = "Warning. Something is misconfigured.";
    String contrLog3title = "value check: deviceInfo = null";
    String contrLog1msg = "NullPointerException on ...test";
    String contrLog2msg = "Multiple port values are present.";
    String contrLog3msg = "deviceInfo = null";
    ControllerLogInfo contrLog1 = new ControllerLogInfo() //
        .logId(contrLog1Id) //
        .timestamp(contrLog1time) //
        .level(contrLog1level) //
        .title(contrLog1title) //
        .message(contrLog1msg);
    ControllerLogInfo contrLog2 = new ControllerLogInfo() //
        .logId(contrLog2Id) //
        .timestamp(contrLog2time) //
        .level(contrLog2level) //
        .title(contrLog2title) //
        .message(contrLog2msg);
    ControllerLogInfo contrLog3 = new ControllerLogInfo() //
        .logId(contrLog3Id) //
        .timestamp(contrLog3time) //
        .level(contrLog3level) //
        .title(contrLog3title) //
        .message(contrLog3msg);

    Map<UUID, ControllerLogInfo> logMap = new HashMap<>();
    logMap.put(contrLog1Id, contrLog1);
    logMap.put(contrLog2Id, contrLog2);
    logMap.put(contrLog3Id, contrLog3);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(logMap).when(controllerLogRepository).findAll();

    // Call service
    CommonGetListResponse<ControllerLogInfo> retListResp =
        controllerLogService.getLogInfo(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.controllerLogRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must only be 2, sorted by timestamp ascending.",
        retListResp.getObjList(), contains(contrLog2, contrLog1));
  }

  @Test
  @DisplayName("TEST getLogInfo (No entries in the DB(null))")
  void testGetLogInfoNoEntryNull() {
    Integer skip = null;
    Integer limit = null;

    Map<UUID, ControllerLogInfo> logMap = null;

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(logMap).when(controllerLogRepository).findAll();

    // Call service
    CommonGetListResponse<ControllerLogInfo> retListResp =
        controllerLogService.getLogInfo(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.controllerLogRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getLogInfo (No entries in the DB)")
  void testGetLogInfoNoEntry() {
    Integer skip = null;
    Integer limit = null;

    Map<UUID, ControllerLogInfo> logMap = Collections.emptyMap();

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(logMap).when(controllerLogRepository).findAll();

    // Call service
    CommonGetListResponse<ControllerLogInfo> retListResp =
        controllerLogService.getLogInfo(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.controllerLogRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST deleteLogs")
  void testDeleteLogs() {
    UUID contrLog1Id = UUID.fromString("4f985f64-0314-0361-032c-2b963f789af1");
    UUID contrLog2Id = UUID.fromString("5b785f64-5147-4013-b3fc-48963f50ace2");
    UUID contrLog3Id = UUID.fromString("2a085f64-842b-3102-9990-6c963f1b3bd3");
    OffsetDateTime contrLog1time = OffsetDateTime.now().minusDays(2);
    OffsetDateTime contrLog2time = OffsetDateTime.now().minusDays(3);
    OffsetDateTime contrLog3time = OffsetDateTime.now();
    CommonLogLevel contrLog1level = CommonLogLevel.CRITICAL;
    CommonLogLevel contrLog2level = CommonLogLevel.WARNING;
    CommonLogLevel contrLog3level = CommonLogLevel.DEBUG;
    String contrLog1title = "A fatal error occurred.";
    String contrLog2title = "Warning. Something is misconfigured.";
    String contrLog3title = "value check: deviceInfo = null";
    String contrLog1msg = "NullPointerException on ...test";
    String contrLog2msg = "Multiple port values are present.";
    String contrLog3msg = "deviceInfo = null";
    ControllerLogInfo contrLog1 = new ControllerLogInfo() //
        .logId(contrLog1Id) //
        .timestamp(contrLog1time) //
        .level(contrLog1level) //
        .title(contrLog1title) //
        .message(contrLog1msg);
    ControllerLogInfo contrLog2 = new ControllerLogInfo() //
        .logId(contrLog2Id) //
        .timestamp(contrLog2time) //
        .level(contrLog2level) //
        .title(contrLog2title) //
        .message(contrLog2msg);
    ControllerLogInfo contrLog3 = new ControllerLogInfo() //
        .logId(contrLog3Id) //
        .timestamp(contrLog3time) //
        .level(contrLog3level) //
        .title(contrLog3title) //
        .message(contrLog3msg);

    Map<UUID, ControllerLogInfo> logMap = new HashMap<>();
    logMap.put(contrLog1Id, contrLog1);
    logMap.put(contrLog2Id, contrLog2);
    logMap.put(contrLog3Id, contrLog3);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(true).when(controllerLogRepository).deleteAll();
    doReturn(logMap).when(controllerLogRepository).findAll();

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call Service
    CommonOperResponse operResp = controllerLogService.deleteLogs();

    // Verify that appropriate Repository method was called
    verify(this.controllerLogRepository, times(1)).deleteAll();

    // Verify that appropriate Repository method was NOT called
    verify(this.controllerLogRepository, times(0)).delete(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST deleteLogs (some logs were not deleted)")
  void testDeleteLogsSomeLogsNotDeleted() {
    UUID contrLog1Id = UUID.fromString("4f985f64-0314-0361-032c-2b963f789af1");
    UUID contrLog2Id = UUID.fromString("5b785f64-5147-4013-b3fc-48963f50ace2");
    UUID contrLog3Id = UUID.fromString("2a085f64-842b-3102-9990-6c963f1b3bd3");
    OffsetDateTime contrLog1time = OffsetDateTime.now().minusDays(2);
    OffsetDateTime contrLog2time = OffsetDateTime.now().minusDays(3);
    OffsetDateTime contrLog3time = OffsetDateTime.now();
    CommonLogLevel contrLog1level = CommonLogLevel.CRITICAL;
    CommonLogLevel contrLog2level = CommonLogLevel.WARNING;
    CommonLogLevel contrLog3level = CommonLogLevel.DEBUG;
    String contrLog1title = "A fatal error occurred.";
    String contrLog2title = "Warning. Something is misconfigured.";
    String contrLog3title = "value check: deviceInfo = null";
    String contrLog1msg = "NullPointerException on ...test";
    String contrLog2msg = "Multiple port values are present.";
    String contrLog3msg = "deviceInfo = null";
    ControllerLogInfo contrLog1 = new ControllerLogInfo() //
        .logId(contrLog1Id) //
        .timestamp(contrLog1time) //
        .level(contrLog1level) //
        .title(contrLog1title) //
        .message(contrLog1msg);
    ControllerLogInfo contrLog2 = new ControllerLogInfo() //
        .logId(contrLog2Id) //
        .timestamp(contrLog2time) //
        .level(contrLog2level) //
        .title(contrLog2title) //
        .message(contrLog2msg);
    ControllerLogInfo contrLog3 = new ControllerLogInfo() //
        .logId(contrLog3Id) //
        .timestamp(contrLog3time) //
        .level(contrLog3level) //
        .title(contrLog3title) //
        .message(contrLog3msg);

    Map<UUID, ControllerLogInfo> logMap = new HashMap<>();
    logMap.put(contrLog1Id, contrLog1);
    logMap.put(contrLog2Id, contrLog2);
    logMap.put(contrLog3Id, contrLog3);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot delete all log entries.";
    String expectedDetail = "Some or all controller log entries may not have been deleted.";

    // Mock(s)
    doReturn(false).when(controllerLogRepository).deleteAll();
    doReturn(logMap).when(controllerLogRepository).findAll();

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call Service
    CommonOperResponse operResp = controllerLogService.deleteLogs();

    // Verify that appropriate Repository method was called
    verify(this.controllerLogRepository, times(1)).deleteAll();

    // Verify that appropriate Repository method was NOT called
    verify(this.controllerLogRepository, times(0)).delete(arg1.capture());

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
}
