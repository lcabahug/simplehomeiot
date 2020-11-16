package com.codingcuriosity.project.simplehomeiot.timers.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonAddResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonGetListResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonGetResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonOperResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.ErrorType;
import com.codingcuriosity.project.simplehomeiot.controls.model.DeviceControlInfo;
import com.codingcuriosity.project.simplehomeiot.controls.repository.ControlRepository;
import com.codingcuriosity.project.simplehomeiot.logs.repository.ControllerLogRepository;
import com.codingcuriosity.project.simplehomeiot.timers.model.CommonTimerAction;
import com.codingcuriosity.project.simplehomeiot.timers.model.CommonTimerIntervalPattern;
import com.codingcuriosity.project.simplehomeiot.timers.model.CommonTimerType;
import com.codingcuriosity.project.simplehomeiot.timers.model.TimerInfo;
import com.codingcuriosity.project.simplehomeiot.timers.model.TimerInfoTimerTarget;
import com.codingcuriosity.project.simplehomeiot.timers.model.TimerModification;
import com.codingcuriosity.project.simplehomeiot.timers.model.TimerRegistration;
import com.codingcuriosity.project.simplehomeiot.timers.model.TimerSetAction;
import com.codingcuriosity.project.simplehomeiot.timers.repository.TimerRepository;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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
public class TimerServiceImplTest {

  @Autowired
  TimerService timerService;

  @MockBean
  TimerRepository timerRepository;

  @MockBean
  ControlRepository controlRepository;

  @MockBean
  ControllerLogRepository controllerLogRepository;

  @Test
  @DisplayName("TEST getLogRepo")
  void testGetLogRepo() {

    // Call Service
    ControllerLogRepository logRepository = this.timerService.getLogRepo();

    // Assertions
    assertEquals(this.controllerLogRepository, logRepository, "LogRepository must be identical.");
  }

  @Test
  @DisplayName("TEST getTimers (all parameters = null)")
  void testGetTimersNoParam() {
    Integer skip = null;
    Integer limit = null;

    UUID timer1id = UUID.fromString("06abcdef-1111-1111-1111-111111111101");
    UUID timer2id = UUID.fromString("06abcdef-2222-2222-2222-222222222202");
    UUID timer3id = UUID.fromString("06abcdef-3333-3333-3333-333333333303");
    UUID timer4id = UUID.fromString("06abcdef-4444-4444-4444-444444444404");
    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerType timer2type = CommonTimerType.ONCE;
    CommonTimerType timer3type = CommonTimerType.INTERVAL;
    CommonTimerType timer4type = CommonTimerType.ONCE;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    CommonTimerIntervalPattern timer2intPtrn = null;
    CommonTimerIntervalPattern timer3intPtrn = CommonTimerIntervalPattern.DAYS_PER_MONTH;
    CommonTimerIntervalPattern timer4intPtrn = null;
    OffsetDateTime timer1startDate = OffsetDateTime.now().plusDays(2);
    OffsetDateTime timer2startDate = OffsetDateTime.now().plusDays(4);
    OffsetDateTime timer3startDate = OffsetDateTime.now().plusDays(1);
    OffsetDateTime timer4startDate = OffsetDateTime.now().plusDays(3);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusWeeks(2);
    OffsetDateTime timer2endDate = timer2startDate;
    OffsetDateTime timer3endDate = OffsetDateTime.now().plusDays(9);
    OffsetDateTime timer4endDate = timer4startDate;
    Integer timer1intervalPeriod = 1;
    Integer timer2intervalPeriod = null;
    Integer timer3intervalPeriod = 2;
    Integer timer4intervalPeriod = null;
    OffsetDateTime timer1date1 = OffsetDateTime.now().plusDays(5).plusMinutes(5);
    OffsetDateTime timer1date2 = OffsetDateTime.now().plusDays(5).plusMinutes(10);
    OffsetDateTime timer2date1 = OffsetDateTime.now().plusDays(5).plusMinutes(15);
    OffsetDateTime timer2date2 = OffsetDateTime.now().plusDays(5).plusMinutes(20);
    OffsetDateTime timer3date1 = OffsetDateTime.now().plusDays(5).plusMinutes(25);
    OffsetDateTime timer3date2 = OffsetDateTime.now().plusDays(5).plusMinutes(30);
    OffsetDateTime timer4date1 = OffsetDateTime.now().plusDays(5).plusMinutes(35);
    OffsetDateTime timer4date2 = OffsetDateTime.now().plusDays(5).plusMinutes(40);

    // Timer Targets
    UUID timer1tar1contr1 = UUID.fromString("06abcdef-1111-cccc-ffff-888888888111");
    UUID timer1tar1contr2 = UUID.fromString("06abcdef-2222-cccc-ffff-888888888112");
    UUID timer2tar1contr1 = UUID.fromString("06abcdef-1111-cccc-ffff-888888888221");
    UUID timer2tar1contr2 = UUID.fromString("06abcdef-2222-cccc-ffff-888888888222");
    UUID timer3tar1contr1 = UUID.fromString("06abcdef-1111-cccc-ffff-888888888331");
    UUID timer3tar1contr2 = UUID.fromString("06abcdef-2222-cccc-ffff-888888888332");
    UUID timer4tar1contr1 = UUID.fromString("06abcdef-1111-cccc-ffff-888888888441");
    UUID timer4tar1contr2 = UUID.fromString("06abcdef-2222-cccc-ffff-888888888442");
    CommonTimerAction timer1tar1act1 = CommonTimerAction.DIRECT;
    CommonTimerAction timer1tar1act2 = CommonTimerAction.SLIDER_DOWN;
    CommonTimerAction timer2tar1act1 = CommonTimerAction.SLIDER_UP;
    CommonTimerAction timer2tar1act2 = CommonTimerAction.TOGGLE;
    CommonTimerAction timer3tar1act1 = CommonTimerAction.SWITCH_OFF;
    CommonTimerAction timer3tar1act2 = CommonTimerAction.SWITCH_ON;
    CommonTimerAction timer4tar1act1 = CommonTimerAction.DIRECT;
    CommonTimerAction timer4tar1act2 = CommonTimerAction.SLIDER_UP;
    String timer1tar1val1 = "5.55";
    String timer1tar1val2 = "test only - null a";
    String timer2tar1val1 = "test only - null b";
    String timer2tar1val2 = "test only - null c";
    String timer3tar1val1 = "test only - null d";
    String timer3tar1val2 = "test only - null e";
    String timer4tar1val1 = "test only - null f";
    String timer4tar1val2 = "test only - null g";

    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(timer1tar1contr1) //
        .action(timer1tar1act1) //
        .directValue(timer1tar1val1);
    TimerInfoTimerTarget timer1target2 = new TimerInfoTimerTarget() //
        .controlId(timer1tar1contr2) //
        .action(timer1tar1act2) //
        .directValue(timer1tar1val2);
    TimerInfoTimerTarget timer2target1 = new TimerInfoTimerTarget() //
        .controlId(timer2tar1contr1) //
        .action(timer2tar1act1) //
        .directValue(timer2tar1val1);
    TimerInfoTimerTarget timer2target2 = new TimerInfoTimerTarget() //
        .controlId(timer2tar1contr2) //
        .action(timer2tar1act2) //
        .directValue(timer2tar1val2);
    TimerInfoTimerTarget timer3target1 = new TimerInfoTimerTarget() //
        .controlId(timer3tar1contr1) //
        .action(timer3tar1act1) //
        .directValue(timer3tar1val1);
    TimerInfoTimerTarget timer3target2 = new TimerInfoTimerTarget() //
        .controlId(timer3tar1contr2) //
        .action(timer3tar1act2) //
        .directValue(timer3tar1val2);
    TimerInfoTimerTarget timer4target1 = new TimerInfoTimerTarget() //
        .controlId(timer4tar1contr1) //
        .action(timer4tar1act1) //
        .directValue(timer4tar1val1);
    TimerInfoTimerTarget timer4target2 = new TimerInfoTimerTarget() //
        .controlId(timer4tar1contr2) //
        .action(timer4tar1act2) //
        .directValue(timer4tar1val2);

    TimerInfo timer1 = new TimerInfo();
    timer1.setTimerId(timer1id);
    timer1.setTimerType(timer1type);
    timer1.setIntervalPattern(timer1intPtrn);
    timer1.setStartDateTime(timer1startDate);
    timer1.setEndDateTime(timer1endDate);
    timer1.setIntervalPeriod(timer1intervalPeriod);
    timer1.addSetDateTimesItem(timer1date2);
    timer1.addSetDateTimesItem(timer1date1);
    timer1.addTimerTargetItem(timer1target1);
    timer1.addTimerTargetItem(timer1target2);
    TimerInfo timer2 = new TimerInfo();
    timer2.setTimerId(timer2id);
    timer2.setTimerType(timer2type);
    timer2.setIntervalPattern(timer2intPtrn);
    timer2.setStartDateTime(timer2startDate);
    timer2.setEndDateTime(timer2endDate);
    timer2.setIntervalPeriod(timer2intervalPeriod);
    timer2.addSetDateTimesItem(timer2date1);
    timer2.addSetDateTimesItem(timer2date2);
    timer2.addTimerTargetItem(timer2target1);
    timer2.addTimerTargetItem(timer2target2);
    TimerInfo timer3 = new TimerInfo();
    timer3.setTimerId(timer3id);
    timer3.setTimerType(timer3type);
    timer3.setIntervalPattern(timer3intPtrn);
    timer3.setStartDateTime(timer3startDate);
    timer3.setEndDateTime(timer3endDate);
    timer3.setIntervalPeriod(timer3intervalPeriod);
    timer3.addSetDateTimesItem(timer3date2);
    timer3.addSetDateTimesItem(timer3date1);
    timer3.addTimerTargetItem(timer3target1);
    timer3.addTimerTargetItem(timer3target2);
    TimerInfo timer4 = new TimerInfo();
    timer4.setTimerId(timer4id);
    timer4.setTimerType(timer4type);
    timer4.setIntervalPattern(timer4intPtrn);
    timer4.setStartDateTime(timer4startDate);
    timer4.setEndDateTime(timer4endDate);
    timer4.setIntervalPeriod(timer4intervalPeriod);
    timer4.addSetDateTimesItem(timer4date1);
    timer4.addSetDateTimesItem(timer4date2);
    timer4.addTimerTargetItem(timer4target1);
    timer4.addTimerTargetItem(timer4target2);

    Map<UUID, TimerInfo> timerMap = new HashMap<>();
    timerMap.put(timer1id, timer1);
    timerMap.put(timer2id, timer2);
    timerMap.put(timer3id, timer3);
    timerMap.put(timer4id, timer4);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(timerMap).when(this.timerRepository).findAll();

    // Call service
    CommonGetListResponse<TimerInfo> retListResp = this.timerService.getTimers(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.timerRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be sorted by start date ascending.",
        retListResp.getObjList(), contains(timer3, timer1, timer4, timer2));
    assertEquals(timer3id, retListResp.getObjList().get(0).getTimerId(),
        "1st entry's UUID must match timer 3's");
    assertEquals(timer3type, retListResp.getObjList().get(0).getTimerType(),
        "1st entry's Type must match timer 3's");
    assertEquals(timer3intPtrn, retListResp.getObjList().get(0).getIntervalPattern(),
        "1st entry's Interval Pattern must match timer 3's");
    assertEquals(timer3startDate, retListResp.getObjList().get(0).getStartDateTime(),
        "1st entry's Start DateTime must match timer 3's");
    assertEquals(timer3endDate, retListResp.getObjList().get(0).getEndDateTime(),
        "1st entry's End DateTime must match timer 3's");
    assertEquals(timer3intervalPeriod, retListResp.getObjList().get(0).getIntervalPeriod(),
        "1st entry's Interval Period must match timer 3's");
    assertThat("The set DateTimes must be correct and sorted.",
        retListResp.getObjList().get(0).getSetDateTimes(), contains(timer3date1, timer3date2));
    assertThat("The Timer Targets must be correct (in any order).",
        retListResp.getObjList().get(0).getTimerTarget(),
        containsInAnyOrder(timer3target1, timer3target2));
    for (TimerInfoTimerTarget targetInfo : retListResp.getObjList().get(0).getTimerTarget()) {
      if (targetInfo.equals(timer3target1)) {
        assertEquals(timer3tar1contr1, targetInfo.getControlId(), "Control ID must match.");
        assertEquals(timer3tar1act1, targetInfo.getAction(), "Action must match.");
        assertEquals(timer3tar1val1, targetInfo.getDirectValue(), "Direct Value must match.");
        continue;
      } else if (targetInfo.equals(timer3target2)) {
        assertEquals(timer3tar1contr2, targetInfo.getControlId(), "Control ID must match.");
        assertEquals(timer3tar1act2, targetInfo.getAction(), "Action must match.");
        assertEquals(timer3tar1val2, targetInfo.getDirectValue(), "Direct Value must match.");
        continue;
      } else {
        fail("Some unrelated object was in the list.");
      }
    }
    assertEquals(timer1id, retListResp.getObjList().get(1).getTimerId(),
        "2nd entry's UUID must match timer 1's");
    assertEquals(timer1type, retListResp.getObjList().get(1).getTimerType(),
        "2nd entry's Type must match timer 1's");
    assertEquals(timer1intPtrn, retListResp.getObjList().get(1).getIntervalPattern(),
        "2nd entry's Interval Pattern must match timer 1's");
    assertEquals(timer1startDate, retListResp.getObjList().get(1).getStartDateTime(),
        "2nd entry's Start DateTime must match timer 1's");
    assertEquals(timer1endDate, retListResp.getObjList().get(1).getEndDateTime(),
        "2nd entry's End DateTime must match timer 1's");
    assertEquals(timer1intervalPeriod, retListResp.getObjList().get(1).getIntervalPeriod(),
        "2nd entry's Interval Period must match timer 1's");
    assertThat("The set DateTimes must be correct and sorted.",
        retListResp.getObjList().get(1).getSetDateTimes(), contains(timer1date1, timer1date2));
    assertThat("The Timer Targets must be correct (in any order).",
        retListResp.getObjList().get(1).getTimerTarget(),
        containsInAnyOrder(timer1target1, timer1target2));
    for (TimerInfoTimerTarget targetInfo : retListResp.getObjList().get(1).getTimerTarget()) {
      if (targetInfo.equals(timer1target1)) {
        assertEquals(timer1tar1contr1, targetInfo.getControlId(), "Control ID must match.");
        assertEquals(timer1tar1act1, targetInfo.getAction(), "Action must match.");
        assertEquals(timer1tar1val1, targetInfo.getDirectValue(), "Direct Value must match.");
        continue;
      } else if (targetInfo.equals(timer1target2)) {
        assertEquals(timer1tar1contr2, targetInfo.getControlId(), "Control ID must match.");
        assertEquals(timer1tar1act2, targetInfo.getAction(), "Action must match.");
        assertEquals(timer1tar1val2, targetInfo.getDirectValue(), "Direct Value must match.");
        continue;
      } else {
        fail("Some unrelated object was in the list.");
      }
    }
    assertEquals(timer4id, retListResp.getObjList().get(2).getTimerId(),
        "3rd entry's UUID must match timer 4's");
    assertEquals(timer4type, retListResp.getObjList().get(2).getTimerType(),
        "3rd entry's Type must match timer 4's");
    assertEquals(timer4intPtrn, retListResp.getObjList().get(2).getIntervalPattern(),
        "3rd entry's Interval Pattern must match timer 4's");
    assertEquals(timer4startDate, retListResp.getObjList().get(2).getStartDateTime(),
        "3rd entry's Start DateTime must match timer 4's");
    assertEquals(timer4endDate, retListResp.getObjList().get(2).getEndDateTime(),
        "3rd entry's End DateTime must match timer 4's");
    assertEquals(timer4intervalPeriod, retListResp.getObjList().get(2).getIntervalPeriod(),
        "3rd entry's Interval Period must match timer 4's");
    assertThat("The set DateTimes must be correct and sorted.",
        retListResp.getObjList().get(2).getSetDateTimes(), contains(timer4date1, timer4date2));
    assertThat("The Timer Targets must be correct (in any order).",
        retListResp.getObjList().get(2).getTimerTarget(),
        containsInAnyOrder(timer4target1, timer4target2));
    for (TimerInfoTimerTarget targetInfo : retListResp.getObjList().get(2).getTimerTarget()) {
      if (targetInfo.equals(timer4target1)) {
        assertEquals(timer4tar1contr1, targetInfo.getControlId(), "Control ID must match.");
        assertEquals(timer4tar1act1, targetInfo.getAction(), "Action must match.");
        assertEquals(timer4tar1val1, targetInfo.getDirectValue(), "Direct Value must match.");
        continue;
      } else if (targetInfo.equals(timer4target2)) {
        assertEquals(timer4tar1contr2, targetInfo.getControlId(), "Control ID must match.");
        assertEquals(timer4tar1act2, targetInfo.getAction(), "Action must match.");
        assertEquals(timer4tar1val2, targetInfo.getDirectValue(), "Direct Value must match.");
        continue;
      } else {
        fail("Some unrelated object was in the list.");
      }
    }
    assertEquals(timer2id, retListResp.getObjList().get(3).getTimerId(),
        "4th entry's UUID must match timer 2's");
    assertEquals(timer2type, retListResp.getObjList().get(3).getTimerType(),
        "4th entry's Type must match timer 2's");
    assertEquals(timer2intPtrn, retListResp.getObjList().get(3).getIntervalPattern(),
        "4th entry's Interval Pattern must match timer 2's");
    assertEquals(timer2startDate, retListResp.getObjList().get(3).getStartDateTime(),
        "4th entry's Start DateTime must match timer 2's");
    assertEquals(timer2endDate, retListResp.getObjList().get(3).getEndDateTime(),
        "4th entry's End DateTime must match timer 2's");
    assertEquals(timer2intervalPeriod, retListResp.getObjList().get(3).getIntervalPeriod(),
        "4th entry's Interval Period must match timer 2's");
    assertThat("The set DateTimes must be correct and sorted.",
        retListResp.getObjList().get(3).getSetDateTimes(), contains(timer2date1, timer2date2));
    assertThat("The Timer Targets must be correct (in any order).",
        retListResp.getObjList().get(3).getTimerTarget(),
        containsInAnyOrder(timer2target1, timer2target2));
    for (TimerInfoTimerTarget targetInfo : retListResp.getObjList().get(3).getTimerTarget()) {
      if (targetInfo.equals(timer2target1)) {
        assertEquals(timer2tar1contr1, targetInfo.getControlId(), "Control ID must match.");
        assertEquals(timer2tar1act1, targetInfo.getAction(), "Action must match.");
        assertEquals(timer2tar1val1, targetInfo.getDirectValue(), "Direct Value must match.");
        continue;
      } else if (targetInfo.equals(timer2target2)) {
        assertEquals(timer2tar1contr2, targetInfo.getControlId(), "Control ID must match.");
        assertEquals(timer2tar1act2, targetInfo.getAction(), "Action must match.");
        assertEquals(timer2tar1val2, targetInfo.getDirectValue(), "Direct Value must match.");
        continue;
      } else {
        fail("Some unrelated object was in the list.");
      }
    }
  }

  @Test
  @DisplayName("TEST getTimers (skip = 0, limit = 0)")
  void testGetTimersSkipMinLimitMin() {
    Integer skip = 0;
    Integer limit = 0;

    UUID timer1id = UUID.fromString("06abcdef-1111-1111-1111-111111111101");
    UUID timer2id = UUID.fromString("06abcdef-2222-2222-2222-222222222202");
    UUID timer3id = UUID.fromString("06abcdef-3333-3333-3333-333333333303");
    UUID timer4id = UUID.fromString("06abcdef-4444-4444-4444-444444444404");
    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerType timer2type = CommonTimerType.ONCE;
    CommonTimerType timer3type = CommonTimerType.INTERVAL;
    CommonTimerType timer4type = CommonTimerType.ONCE;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    CommonTimerIntervalPattern timer2intPtrn = null;
    CommonTimerIntervalPattern timer3intPtrn = CommonTimerIntervalPattern.DAYS_PER_MONTH;
    CommonTimerIntervalPattern timer4intPtrn = null;
    OffsetDateTime timer1startDate = OffsetDateTime.now().plusDays(2);
    OffsetDateTime timer2startDate = OffsetDateTime.now().plusDays(4);
    OffsetDateTime timer3startDate = OffsetDateTime.now().plusDays(1);
    OffsetDateTime timer4startDate = OffsetDateTime.now().plusDays(3);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusWeeks(2);
    OffsetDateTime timer2endDate = timer2startDate;
    OffsetDateTime timer3endDate = OffsetDateTime.now().plusDays(9);
    OffsetDateTime timer4endDate = timer4startDate;
    Integer timer1intervalPeriod = 1;
    Integer timer2intervalPeriod = null;
    Integer timer3intervalPeriod = 2;
    Integer timer4intervalPeriod = null;
    OffsetDateTime timer1date1 = OffsetDateTime.now().plusDays(5).plusMinutes(5);
    OffsetDateTime timer1date2 = OffsetDateTime.now().plusDays(5).plusMinutes(10);
    OffsetDateTime timer2date1 = OffsetDateTime.now().plusDays(5).plusMinutes(15);
    OffsetDateTime timer2date2 = OffsetDateTime.now().plusDays(5).plusMinutes(20);
    OffsetDateTime timer3date1 = OffsetDateTime.now().plusDays(5).plusMinutes(25);
    OffsetDateTime timer3date2 = OffsetDateTime.now().plusDays(5).plusMinutes(30);
    OffsetDateTime timer4date1 = OffsetDateTime.now().plusDays(5).plusMinutes(35);
    OffsetDateTime timer4date2 = OffsetDateTime.now().plusDays(5).plusMinutes(40);

    // Timer Targets
    UUID timer1tar1contr1 = UUID.fromString("06abcdef-1111-cccc-ffff-888888888111");
    UUID timer1tar1contr2 = UUID.fromString("06abcdef-2222-cccc-ffff-888888888112");
    UUID timer2tar1contr1 = UUID.fromString("06abcdef-1111-cccc-ffff-888888888221");
    UUID timer2tar1contr2 = UUID.fromString("06abcdef-2222-cccc-ffff-888888888222");
    UUID timer3tar1contr1 = UUID.fromString("06abcdef-1111-cccc-ffff-888888888331");
    UUID timer3tar1contr2 = UUID.fromString("06abcdef-2222-cccc-ffff-888888888332");
    UUID timer4tar1contr1 = UUID.fromString("06abcdef-1111-cccc-ffff-888888888441");
    UUID timer4tar1contr2 = UUID.fromString("06abcdef-2222-cccc-ffff-888888888442");
    CommonTimerAction timer1tar1act1 = CommonTimerAction.DIRECT;
    CommonTimerAction timer1tar1act2 = CommonTimerAction.SLIDER_DOWN;
    CommonTimerAction timer2tar1act1 = CommonTimerAction.SLIDER_UP;
    CommonTimerAction timer2tar1act2 = CommonTimerAction.TOGGLE;
    CommonTimerAction timer3tar1act1 = CommonTimerAction.SWITCH_OFF;
    CommonTimerAction timer3tar1act2 = CommonTimerAction.SWITCH_ON;
    CommonTimerAction timer4tar1act1 = CommonTimerAction.DIRECT;
    CommonTimerAction timer4tar1act2 = CommonTimerAction.SLIDER_UP;
    String timer1tar1val1 = "5.55";
    String timer1tar1val2 = "test only - null a";
    String timer2tar1val1 = "test only - null b";
    String timer2tar1val2 = "test only - null c";
    String timer3tar1val1 = "test only - null d";
    String timer3tar1val2 = "test only - null e";
    String timer4tar1val1 = "test only - null f";
    String timer4tar1val2 = "test only - null g";

    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(timer1tar1contr1) //
        .action(timer1tar1act1) //
        .directValue(timer1tar1val1);
    TimerInfoTimerTarget timer1target2 = new TimerInfoTimerTarget() //
        .controlId(timer1tar1contr2) //
        .action(timer1tar1act2) //
        .directValue(timer1tar1val2);
    TimerInfoTimerTarget timer2target1 = new TimerInfoTimerTarget() //
        .controlId(timer2tar1contr1) //
        .action(timer2tar1act1) //
        .directValue(timer2tar1val1);
    TimerInfoTimerTarget timer2target2 = new TimerInfoTimerTarget() //
        .controlId(timer2tar1contr2) //
        .action(timer2tar1act2) //
        .directValue(timer2tar1val2);
    TimerInfoTimerTarget timer3target1 = new TimerInfoTimerTarget() //
        .controlId(timer3tar1contr1) //
        .action(timer3tar1act1) //
        .directValue(timer3tar1val1);
    TimerInfoTimerTarget timer3target2 = new TimerInfoTimerTarget() //
        .controlId(timer3tar1contr2) //
        .action(timer3tar1act2) //
        .directValue(timer3tar1val2);
    TimerInfoTimerTarget timer4target1 = new TimerInfoTimerTarget() //
        .controlId(timer4tar1contr1) //
        .action(timer4tar1act1) //
        .directValue(timer4tar1val1);
    TimerInfoTimerTarget timer4target2 = new TimerInfoTimerTarget() //
        .controlId(timer4tar1contr2) //
        .action(timer4tar1act2) //
        .directValue(timer4tar1val2);

    TimerInfo timer1 = new TimerInfo();
    timer1.setTimerId(timer1id);
    timer1.setTimerType(timer1type);
    timer1.setIntervalPattern(timer1intPtrn);
    timer1.setStartDateTime(timer1startDate);
    timer1.setEndDateTime(timer1endDate);
    timer1.setIntervalPeriod(timer1intervalPeriod);
    timer1.addSetDateTimesItem(timer1date1);
    timer1.addSetDateTimesItem(timer1date2);
    timer1.addTimerTargetItem(timer1target1);
    timer1.addTimerTargetItem(timer1target2);
    TimerInfo timer2 = new TimerInfo();
    timer2.setTimerId(timer2id);
    timer2.setTimerType(timer2type);
    timer2.setIntervalPattern(timer2intPtrn);
    timer2.setStartDateTime(timer2startDate);
    timer2.setEndDateTime(timer2endDate);
    timer2.setIntervalPeriod(timer2intervalPeriod);
    timer2.addSetDateTimesItem(timer2date1);
    timer2.addSetDateTimesItem(timer2date2);
    timer2.addTimerTargetItem(timer2target1);
    timer2.addTimerTargetItem(timer2target2);
    TimerInfo timer3 = new TimerInfo();
    timer3.setTimerId(timer3id);
    timer3.setTimerType(timer3type);
    timer3.setIntervalPattern(timer3intPtrn);
    timer3.setStartDateTime(timer3startDate);
    timer3.setEndDateTime(timer3endDate);
    timer3.setIntervalPeriod(timer3intervalPeriod);
    timer3.addSetDateTimesItem(timer3date1);
    timer3.addSetDateTimesItem(timer3date2);
    timer3.addTimerTargetItem(timer3target1);
    timer3.addTimerTargetItem(timer3target2);
    TimerInfo timer4 = new TimerInfo();
    timer4.setTimerId(timer4id);
    timer4.setTimerType(timer4type);
    timer4.setIntervalPattern(timer4intPtrn);
    timer4.setStartDateTime(timer4startDate);
    timer4.setEndDateTime(timer4endDate);
    timer4.setIntervalPeriod(timer4intervalPeriod);
    timer4.addSetDateTimesItem(timer4date1);
    timer4.addSetDateTimesItem(timer4date2);
    timer4.addTimerTargetItem(timer4target1);
    timer4.addTimerTargetItem(timer4target2);

    Map<UUID, TimerInfo> timerMap = new HashMap<>();
    timerMap.put(timer1id, timer1);
    timerMap.put(timer2id, timer2);
    timerMap.put(timer3id, timer3);
    timerMap.put(timer4id, timer4);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(timerMap).when(this.timerRepository).findAll();

    // Call service
    CommonGetListResponse<TimerInfo> retListResp = this.timerService.getTimers(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.timerRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getTimers (skip = 0, limit = entry max)")
  void testGetTimersSkipMinLimitMax() {
    Integer skip = 0;
    Integer limit = null; // Will be changed below

    UUID timer1id = UUID.fromString("06abcdef-1111-1111-1111-111111111101");
    UUID timer2id = UUID.fromString("06abcdef-2222-2222-2222-222222222202");
    UUID timer3id = UUID.fromString("06abcdef-3333-3333-3333-333333333303");
    UUID timer4id = UUID.fromString("06abcdef-4444-4444-4444-444444444404");
    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerType timer2type = CommonTimerType.ONCE;
    CommonTimerType timer3type = CommonTimerType.INTERVAL;
    CommonTimerType timer4type = CommonTimerType.ONCE;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    CommonTimerIntervalPattern timer2intPtrn = null;
    CommonTimerIntervalPattern timer3intPtrn = CommonTimerIntervalPattern.DAYS_PER_MONTH;
    CommonTimerIntervalPattern timer4intPtrn = null;
    OffsetDateTime timer1startDate = OffsetDateTime.now().plusDays(2);
    OffsetDateTime timer2startDate = OffsetDateTime.now().plusDays(4);
    OffsetDateTime timer3startDate = OffsetDateTime.now().plusDays(1);
    OffsetDateTime timer4startDate = OffsetDateTime.now().plusDays(3);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusWeeks(2);
    OffsetDateTime timer2endDate = timer2startDate;
    OffsetDateTime timer3endDate = OffsetDateTime.now().plusDays(9);
    OffsetDateTime timer4endDate = timer4startDate;
    Integer timer1intervalPeriod = 1;
    Integer timer2intervalPeriod = null;
    Integer timer3intervalPeriod = 2;
    Integer timer4intervalPeriod = null;
    OffsetDateTime timer1date1 = OffsetDateTime.now().plusDays(5).plusMinutes(5);
    OffsetDateTime timer1date2 = OffsetDateTime.now().plusDays(5).plusMinutes(10);
    OffsetDateTime timer2date1 = OffsetDateTime.now().plusDays(5).plusMinutes(15);
    OffsetDateTime timer2date2 = OffsetDateTime.now().plusDays(5).plusMinutes(20);
    OffsetDateTime timer3date1 = OffsetDateTime.now().plusDays(5).plusMinutes(25);
    OffsetDateTime timer3date2 = OffsetDateTime.now().plusDays(5).plusMinutes(30);
    OffsetDateTime timer4date1 = OffsetDateTime.now().plusDays(5).plusMinutes(35);
    OffsetDateTime timer4date2 = OffsetDateTime.now().plusDays(5).plusMinutes(40);

    // Timer Targets
    UUID timer1tar1contr1 = UUID.fromString("06abcdef-1111-cccc-ffff-888888888111");
    UUID timer1tar1contr2 = UUID.fromString("06abcdef-2222-cccc-ffff-888888888112");
    UUID timer2tar1contr1 = UUID.fromString("06abcdef-1111-cccc-ffff-888888888221");
    UUID timer2tar1contr2 = UUID.fromString("06abcdef-2222-cccc-ffff-888888888222");
    UUID timer3tar1contr1 = UUID.fromString("06abcdef-1111-cccc-ffff-888888888331");
    UUID timer3tar1contr2 = UUID.fromString("06abcdef-2222-cccc-ffff-888888888332");
    UUID timer4tar1contr1 = UUID.fromString("06abcdef-1111-cccc-ffff-888888888441");
    UUID timer4tar1contr2 = UUID.fromString("06abcdef-2222-cccc-ffff-888888888442");
    CommonTimerAction timer1tar1act1 = CommonTimerAction.DIRECT;
    CommonTimerAction timer1tar1act2 = CommonTimerAction.SLIDER_DOWN;
    CommonTimerAction timer2tar1act1 = CommonTimerAction.SLIDER_UP;
    CommonTimerAction timer2tar1act2 = CommonTimerAction.TOGGLE;
    CommonTimerAction timer3tar1act1 = CommonTimerAction.SWITCH_OFF;
    CommonTimerAction timer3tar1act2 = CommonTimerAction.SWITCH_ON;
    CommonTimerAction timer4tar1act1 = CommonTimerAction.DIRECT;
    CommonTimerAction timer4tar1act2 = CommonTimerAction.SLIDER_UP;
    String timer1tar1val1 = "5.55";
    String timer1tar1val2 = "test only - null a";
    String timer2tar1val1 = "test only - null b";
    String timer2tar1val2 = "test only - null c";
    String timer3tar1val1 = "test only - null d";
    String timer3tar1val2 = "test only - null e";
    String timer4tar1val1 = "test only - null f";
    String timer4tar1val2 = "test only - null g";

    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(timer1tar1contr1) //
        .action(timer1tar1act1) //
        .directValue(timer1tar1val1);
    TimerInfoTimerTarget timer1target2 = new TimerInfoTimerTarget() //
        .controlId(timer1tar1contr2) //
        .action(timer1tar1act2) //
        .directValue(timer1tar1val2);
    TimerInfoTimerTarget timer2target1 = new TimerInfoTimerTarget() //
        .controlId(timer2tar1contr1) //
        .action(timer2tar1act1) //
        .directValue(timer2tar1val1);
    TimerInfoTimerTarget timer2target2 = new TimerInfoTimerTarget() //
        .controlId(timer2tar1contr2) //
        .action(timer2tar1act2) //
        .directValue(timer2tar1val2);
    TimerInfoTimerTarget timer3target1 = new TimerInfoTimerTarget() //
        .controlId(timer3tar1contr1) //
        .action(timer3tar1act1) //
        .directValue(timer3tar1val1);
    TimerInfoTimerTarget timer3target2 = new TimerInfoTimerTarget() //
        .controlId(timer3tar1contr2) //
        .action(timer3tar1act2) //
        .directValue(timer3tar1val2);
    TimerInfoTimerTarget timer4target1 = new TimerInfoTimerTarget() //
        .controlId(timer4tar1contr1) //
        .action(timer4tar1act1) //
        .directValue(timer4tar1val1);
    TimerInfoTimerTarget timer4target2 = new TimerInfoTimerTarget() //
        .controlId(timer4tar1contr2) //
        .action(timer4tar1act2) //
        .directValue(timer4tar1val2);

    TimerInfo timer1 = new TimerInfo();
    timer1.setTimerId(timer1id);
    timer1.setTimerType(timer1type);
    timer1.setIntervalPattern(timer1intPtrn);
    timer1.setStartDateTime(timer1startDate);
    timer1.setEndDateTime(timer1endDate);
    timer1.setIntervalPeriod(timer1intervalPeriod);
    timer1.addSetDateTimesItem(timer1date1);
    timer1.addSetDateTimesItem(timer1date2);
    timer1.addTimerTargetItem(timer1target1);
    timer1.addTimerTargetItem(timer1target2);
    TimerInfo timer2 = new TimerInfo();
    timer2.setTimerId(timer2id);
    timer2.setTimerType(timer2type);
    timer2.setIntervalPattern(timer2intPtrn);
    timer2.setStartDateTime(timer2startDate);
    timer2.setEndDateTime(timer2endDate);
    timer2.setIntervalPeriod(timer2intervalPeriod);
    timer2.addSetDateTimesItem(timer2date1);
    timer2.addSetDateTimesItem(timer2date2);
    timer2.addTimerTargetItem(timer2target1);
    timer2.addTimerTargetItem(timer2target2);
    TimerInfo timer3 = new TimerInfo();
    timer3.setTimerId(timer3id);
    timer3.setTimerType(timer3type);
    timer3.setIntervalPattern(timer3intPtrn);
    timer3.setStartDateTime(timer3startDate);
    timer3.setEndDateTime(timer3endDate);
    timer3.setIntervalPeriod(timer3intervalPeriod);
    timer3.addSetDateTimesItem(timer3date1);
    timer3.addSetDateTimesItem(timer3date2);
    timer3.addTimerTargetItem(timer3target1);
    timer3.addTimerTargetItem(timer3target2);
    TimerInfo timer4 = new TimerInfo();
    timer4.setTimerId(timer4id);
    timer4.setTimerType(timer4type);
    timer4.setIntervalPattern(timer4intPtrn);
    timer4.setStartDateTime(timer4startDate);
    timer4.setEndDateTime(timer4endDate);
    timer4.setIntervalPeriod(timer4intervalPeriod);
    timer4.addSetDateTimesItem(timer4date1);
    timer4.addSetDateTimesItem(timer4date2);
    timer4.addTimerTargetItem(timer4target1);
    timer4.addTimerTargetItem(timer4target2);

    Map<UUID, TimerInfo> timerMap = new HashMap<>();
    timerMap.put(timer1id, timer1);
    timerMap.put(timer2id, timer2);
    timerMap.put(timer3id, timer3);
    timerMap.put(timer4id, timer4);

    limit = timerMap.size();

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(timerMap).when(this.timerRepository).findAll();

    // Call service
    CommonGetListResponse<TimerInfo> retListResp = this.timerService.getTimers(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.timerRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be sorted by start date ascending.",
        retListResp.getObjList(), contains(timer3, timer1, timer4, timer2));
  }

  @Test
  @DisplayName("TEST getTimers (skip = entry max)")
  void testGetTimersSkipMax() {
    Integer skip = null; // Will be changed below
    Integer limit = null;

    UUID timer1id = UUID.fromString("06abcdef-1111-1111-1111-111111111101");
    UUID timer2id = UUID.fromString("06abcdef-2222-2222-2222-222222222202");
    UUID timer3id = UUID.fromString("06abcdef-3333-3333-3333-333333333303");
    UUID timer4id = UUID.fromString("06abcdef-4444-4444-4444-444444444404");
    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerType timer2type = CommonTimerType.ONCE;
    CommonTimerType timer3type = CommonTimerType.INTERVAL;
    CommonTimerType timer4type = CommonTimerType.ONCE;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    CommonTimerIntervalPattern timer2intPtrn = null;
    CommonTimerIntervalPattern timer3intPtrn = CommonTimerIntervalPattern.DAYS_PER_MONTH;
    CommonTimerIntervalPattern timer4intPtrn = null;
    OffsetDateTime timer1startDate = OffsetDateTime.now().plusDays(2);
    OffsetDateTime timer2startDate = OffsetDateTime.now().plusDays(4);
    OffsetDateTime timer3startDate = OffsetDateTime.now().plusDays(1);
    OffsetDateTime timer4startDate = OffsetDateTime.now().plusDays(3);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusWeeks(2);
    OffsetDateTime timer2endDate = timer2startDate;
    OffsetDateTime timer3endDate = OffsetDateTime.now().plusDays(9);
    OffsetDateTime timer4endDate = timer4startDate;
    Integer timer1intervalPeriod = 1;
    Integer timer2intervalPeriod = null;
    Integer timer3intervalPeriod = 2;
    Integer timer4intervalPeriod = null;
    OffsetDateTime timer1date1 = OffsetDateTime.now().plusDays(5).plusMinutes(5);
    OffsetDateTime timer1date2 = OffsetDateTime.now().plusDays(5).plusMinutes(10);
    OffsetDateTime timer2date1 = OffsetDateTime.now().plusDays(5).plusMinutes(15);
    OffsetDateTime timer2date2 = OffsetDateTime.now().plusDays(5).plusMinutes(20);
    OffsetDateTime timer3date1 = OffsetDateTime.now().plusDays(5).plusMinutes(25);
    OffsetDateTime timer3date2 = OffsetDateTime.now().plusDays(5).plusMinutes(30);
    OffsetDateTime timer4date1 = OffsetDateTime.now().plusDays(5).plusMinutes(35);
    OffsetDateTime timer4date2 = OffsetDateTime.now().plusDays(5).plusMinutes(40);

    // Timer Targets
    UUID timer1tar1contr1 = UUID.fromString("06abcdef-1111-cccc-ffff-888888888111");
    UUID timer1tar1contr2 = UUID.fromString("06abcdef-2222-cccc-ffff-888888888112");
    UUID timer2tar1contr1 = UUID.fromString("06abcdef-1111-cccc-ffff-888888888221");
    UUID timer2tar1contr2 = UUID.fromString("06abcdef-2222-cccc-ffff-888888888222");
    UUID timer3tar1contr1 = UUID.fromString("06abcdef-1111-cccc-ffff-888888888331");
    UUID timer3tar1contr2 = UUID.fromString("06abcdef-2222-cccc-ffff-888888888332");
    UUID timer4tar1contr1 = UUID.fromString("06abcdef-1111-cccc-ffff-888888888441");
    UUID timer4tar1contr2 = UUID.fromString("06abcdef-2222-cccc-ffff-888888888442");
    CommonTimerAction timer1tar1act1 = CommonTimerAction.DIRECT;
    CommonTimerAction timer1tar1act2 = CommonTimerAction.SLIDER_DOWN;
    CommonTimerAction timer2tar1act1 = CommonTimerAction.SLIDER_UP;
    CommonTimerAction timer2tar1act2 = CommonTimerAction.TOGGLE;
    CommonTimerAction timer3tar1act1 = CommonTimerAction.SWITCH_OFF;
    CommonTimerAction timer3tar1act2 = CommonTimerAction.SWITCH_ON;
    CommonTimerAction timer4tar1act1 = CommonTimerAction.DIRECT;
    CommonTimerAction timer4tar1act2 = CommonTimerAction.SLIDER_UP;
    String timer1tar1val1 = "5.55";
    String timer1tar1val2 = "test only - null a";
    String timer2tar1val1 = "test only - null b";
    String timer2tar1val2 = "test only - null c";
    String timer3tar1val1 = "test only - null d";
    String timer3tar1val2 = "test only - null e";
    String timer4tar1val1 = "test only - null f";
    String timer4tar1val2 = "test only - null g";

    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(timer1tar1contr1) //
        .action(timer1tar1act1) //
        .directValue(timer1tar1val1);
    TimerInfoTimerTarget timer1target2 = new TimerInfoTimerTarget() //
        .controlId(timer1tar1contr2) //
        .action(timer1tar1act2) //
        .directValue(timer1tar1val2);
    TimerInfoTimerTarget timer2target1 = new TimerInfoTimerTarget() //
        .controlId(timer2tar1contr1) //
        .action(timer2tar1act1) //
        .directValue(timer2tar1val1);
    TimerInfoTimerTarget timer2target2 = new TimerInfoTimerTarget() //
        .controlId(timer2tar1contr2) //
        .action(timer2tar1act2) //
        .directValue(timer2tar1val2);
    TimerInfoTimerTarget timer3target1 = new TimerInfoTimerTarget() //
        .controlId(timer3tar1contr1) //
        .action(timer3tar1act1) //
        .directValue(timer3tar1val1);
    TimerInfoTimerTarget timer3target2 = new TimerInfoTimerTarget() //
        .controlId(timer3tar1contr2) //
        .action(timer3tar1act2) //
        .directValue(timer3tar1val2);
    TimerInfoTimerTarget timer4target1 = new TimerInfoTimerTarget() //
        .controlId(timer4tar1contr1) //
        .action(timer4tar1act1) //
        .directValue(timer4tar1val1);
    TimerInfoTimerTarget timer4target2 = new TimerInfoTimerTarget() //
        .controlId(timer4tar1contr2) //
        .action(timer4tar1act2) //
        .directValue(timer4tar1val2);

    TimerInfo timer1 = new TimerInfo();
    timer1.setTimerId(timer1id);
    timer1.setTimerType(timer1type);
    timer1.setIntervalPattern(timer1intPtrn);
    timer1.setStartDateTime(timer1startDate);
    timer1.setEndDateTime(timer1endDate);
    timer1.setIntervalPeriod(timer1intervalPeriod);
    timer1.addSetDateTimesItem(timer1date1);
    timer1.addSetDateTimesItem(timer1date2);
    timer1.addTimerTargetItem(timer1target1);
    timer1.addTimerTargetItem(timer1target2);
    TimerInfo timer2 = new TimerInfo();
    timer2.setTimerId(timer2id);
    timer2.setTimerType(timer2type);
    timer2.setIntervalPattern(timer2intPtrn);
    timer2.setStartDateTime(timer2startDate);
    timer2.setEndDateTime(timer2endDate);
    timer2.setIntervalPeriod(timer2intervalPeriod);
    timer2.addSetDateTimesItem(timer2date1);
    timer2.addSetDateTimesItem(timer2date2);
    timer2.addTimerTargetItem(timer2target1);
    timer2.addTimerTargetItem(timer2target2);
    TimerInfo timer3 = new TimerInfo();
    timer3.setTimerId(timer3id);
    timer3.setTimerType(timer3type);
    timer3.setIntervalPattern(timer3intPtrn);
    timer3.setStartDateTime(timer3startDate);
    timer3.setEndDateTime(timer3endDate);
    timer3.setIntervalPeriod(timer3intervalPeriod);
    timer3.addSetDateTimesItem(timer3date1);
    timer3.addSetDateTimesItem(timer3date2);
    timer3.addTimerTargetItem(timer3target1);
    timer3.addTimerTargetItem(timer3target2);
    TimerInfo timer4 = new TimerInfo();
    timer4.setTimerId(timer4id);
    timer4.setTimerType(timer4type);
    timer4.setIntervalPattern(timer4intPtrn);
    timer4.setStartDateTime(timer4startDate);
    timer4.setEndDateTime(timer4endDate);
    timer4.setIntervalPeriod(timer4intervalPeriod);
    timer4.addSetDateTimesItem(timer4date1);
    timer4.addSetDateTimesItem(timer4date2);
    timer4.addTimerTargetItem(timer4target1);
    timer4.addTimerTargetItem(timer4target2);

    Map<UUID, TimerInfo> timerMap = new HashMap<>();
    timerMap.put(timer1id, timer1);
    timerMap.put(timer2id, timer2);
    timerMap.put(timer3id, timer3);
    timerMap.put(timer4id, timer4);

    skip = timerMap.size();

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(timerMap).when(this.timerRepository).findAll();

    // Call service
    CommonGetListResponse<TimerInfo> retListResp = this.timerService.getTimers(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.timerRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getTimers (skip = -1)")
  void testGetTimersSkipMinMinus1() {
    Integer skip = -1;
    Integer limit = null;

    UUID timer1id = UUID.fromString("06abcdef-1111-1111-1111-111111111101");
    UUID timer2id = UUID.fromString("06abcdef-2222-2222-2222-222222222202");
    UUID timer3id = UUID.fromString("06abcdef-3333-3333-3333-333333333303");
    UUID timer4id = UUID.fromString("06abcdef-4444-4444-4444-444444444404");
    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerType timer2type = CommonTimerType.ONCE;
    CommonTimerType timer3type = CommonTimerType.INTERVAL;
    CommonTimerType timer4type = CommonTimerType.ONCE;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    CommonTimerIntervalPattern timer2intPtrn = null;
    CommonTimerIntervalPattern timer3intPtrn = CommonTimerIntervalPattern.DAYS_PER_MONTH;
    CommonTimerIntervalPattern timer4intPtrn = null;
    OffsetDateTime timer1startDate = OffsetDateTime.now().plusDays(2);
    OffsetDateTime timer2startDate = OffsetDateTime.now().plusDays(4);
    OffsetDateTime timer3startDate = OffsetDateTime.now().plusDays(1);
    OffsetDateTime timer4startDate = OffsetDateTime.now().plusDays(3);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusWeeks(2);
    OffsetDateTime timer2endDate = timer2startDate;
    OffsetDateTime timer3endDate = OffsetDateTime.now().plusDays(9);
    OffsetDateTime timer4endDate = timer4startDate;
    Integer timer1intervalPeriod = 1;
    Integer timer2intervalPeriod = null;
    Integer timer3intervalPeriod = 2;
    Integer timer4intervalPeriod = null;
    OffsetDateTime timer1date1 = OffsetDateTime.now().plusDays(5).plusMinutes(5);
    OffsetDateTime timer1date2 = OffsetDateTime.now().plusDays(5).plusMinutes(10);
    OffsetDateTime timer2date1 = OffsetDateTime.now().plusDays(5).plusMinutes(15);
    OffsetDateTime timer2date2 = OffsetDateTime.now().plusDays(5).plusMinutes(20);
    OffsetDateTime timer3date1 = OffsetDateTime.now().plusDays(5).plusMinutes(25);
    OffsetDateTime timer3date2 = OffsetDateTime.now().plusDays(5).plusMinutes(30);
    OffsetDateTime timer4date1 = OffsetDateTime.now().plusDays(5).plusMinutes(35);
    OffsetDateTime timer4date2 = OffsetDateTime.now().plusDays(5).plusMinutes(40);

    // Timer Targets
    UUID timer1tar1contr1 = UUID.fromString("06abcdef-1111-cccc-ffff-888888888111");
    UUID timer1tar1contr2 = UUID.fromString("06abcdef-2222-cccc-ffff-888888888112");
    UUID timer2tar1contr1 = UUID.fromString("06abcdef-1111-cccc-ffff-888888888221");
    UUID timer2tar1contr2 = UUID.fromString("06abcdef-2222-cccc-ffff-888888888222");
    UUID timer3tar1contr1 = UUID.fromString("06abcdef-1111-cccc-ffff-888888888331");
    UUID timer3tar1contr2 = UUID.fromString("06abcdef-2222-cccc-ffff-888888888332");
    UUID timer4tar1contr1 = UUID.fromString("06abcdef-1111-cccc-ffff-888888888441");
    UUID timer4tar1contr2 = UUID.fromString("06abcdef-2222-cccc-ffff-888888888442");
    CommonTimerAction timer1tar1act1 = CommonTimerAction.DIRECT;
    CommonTimerAction timer1tar1act2 = CommonTimerAction.SLIDER_DOWN;
    CommonTimerAction timer2tar1act1 = CommonTimerAction.SLIDER_UP;
    CommonTimerAction timer2tar1act2 = CommonTimerAction.TOGGLE;
    CommonTimerAction timer3tar1act1 = CommonTimerAction.SWITCH_OFF;
    CommonTimerAction timer3tar1act2 = CommonTimerAction.SWITCH_ON;
    CommonTimerAction timer4tar1act1 = CommonTimerAction.DIRECT;
    CommonTimerAction timer4tar1act2 = CommonTimerAction.SLIDER_UP;
    String timer1tar1val1 = "5.55";
    String timer1tar1val2 = "test only - null a";
    String timer2tar1val1 = "test only - null b";
    String timer2tar1val2 = "test only - null c";
    String timer3tar1val1 = "test only - null d";
    String timer3tar1val2 = "test only - null e";
    String timer4tar1val1 = "test only - null f";
    String timer4tar1val2 = "test only - null g";

    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(timer1tar1contr1) //
        .action(timer1tar1act1) //
        .directValue(timer1tar1val1);
    TimerInfoTimerTarget timer1target2 = new TimerInfoTimerTarget() //
        .controlId(timer1tar1contr2) //
        .action(timer1tar1act2) //
        .directValue(timer1tar1val2);
    TimerInfoTimerTarget timer2target1 = new TimerInfoTimerTarget() //
        .controlId(timer2tar1contr1) //
        .action(timer2tar1act1) //
        .directValue(timer2tar1val1);
    TimerInfoTimerTarget timer2target2 = new TimerInfoTimerTarget() //
        .controlId(timer2tar1contr2) //
        .action(timer2tar1act2) //
        .directValue(timer2tar1val2);
    TimerInfoTimerTarget timer3target1 = new TimerInfoTimerTarget() //
        .controlId(timer3tar1contr1) //
        .action(timer3tar1act1) //
        .directValue(timer3tar1val1);
    TimerInfoTimerTarget timer3target2 = new TimerInfoTimerTarget() //
        .controlId(timer3tar1contr2) //
        .action(timer3tar1act2) //
        .directValue(timer3tar1val2);
    TimerInfoTimerTarget timer4target1 = new TimerInfoTimerTarget() //
        .controlId(timer4tar1contr1) //
        .action(timer4tar1act1) //
        .directValue(timer4tar1val1);
    TimerInfoTimerTarget timer4target2 = new TimerInfoTimerTarget() //
        .controlId(timer4tar1contr2) //
        .action(timer4tar1act2) //
        .directValue(timer4tar1val2);

    TimerInfo timer1 = new TimerInfo();
    timer1.setTimerId(timer1id);
    timer1.setTimerType(timer1type);
    timer1.setIntervalPattern(timer1intPtrn);
    timer1.setStartDateTime(timer1startDate);
    timer1.setEndDateTime(timer1endDate);
    timer1.setIntervalPeriod(timer1intervalPeriod);
    timer1.addSetDateTimesItem(timer1date1);
    timer1.addSetDateTimesItem(timer1date2);
    timer1.addTimerTargetItem(timer1target1);
    timer1.addTimerTargetItem(timer1target2);
    TimerInfo timer2 = new TimerInfo();
    timer2.setTimerId(timer2id);
    timer2.setTimerType(timer2type);
    timer2.setIntervalPattern(timer2intPtrn);
    timer2.setStartDateTime(timer2startDate);
    timer2.setEndDateTime(timer2endDate);
    timer2.setIntervalPeriod(timer2intervalPeriod);
    timer2.addSetDateTimesItem(timer2date1);
    timer2.addSetDateTimesItem(timer2date2);
    timer2.addTimerTargetItem(timer2target1);
    timer2.addTimerTargetItem(timer2target2);
    TimerInfo timer3 = new TimerInfo();
    timer3.setTimerId(timer3id);
    timer3.setTimerType(timer3type);
    timer3.setIntervalPattern(timer3intPtrn);
    timer3.setStartDateTime(timer3startDate);
    timer3.setEndDateTime(timer3endDate);
    timer3.setIntervalPeriod(timer3intervalPeriod);
    timer3.addSetDateTimesItem(timer3date1);
    timer3.addSetDateTimesItem(timer3date2);
    timer3.addTimerTargetItem(timer3target1);
    timer3.addTimerTargetItem(timer3target2);
    TimerInfo timer4 = new TimerInfo();
    timer4.setTimerId(timer4id);
    timer4.setTimerType(timer4type);
    timer4.setIntervalPattern(timer4intPtrn);
    timer4.setStartDateTime(timer4startDate);
    timer4.setEndDateTime(timer4endDate);
    timer4.setIntervalPeriod(timer4intervalPeriod);
    timer4.addSetDateTimesItem(timer4date1);
    timer4.addSetDateTimesItem(timer4date2);
    timer4.addTimerTargetItem(timer4target1);
    timer4.addTimerTargetItem(timer4target2);

    Map<UUID, TimerInfo> timerMap = new HashMap<>();
    timerMap.put(timer1id, timer1);
    timerMap.put(timer2id, timer2);
    timerMap.put(timer3id, timer3);
    timerMap.put(timer4id, timer4);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    doReturn(timerMap).when(this.timerRepository).findAll();

    // Call service
    CommonGetListResponse<TimerInfo> retListResp = this.timerService.getTimers(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.timerRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 400.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getTimers (limit = -1)")
  void testGetTimersLimitMinMinus1() {
    Integer skip = null;
    Integer limit = -1;

    UUID timer1id = UUID.fromString("06abcdef-1111-1111-1111-111111111101");
    UUID timer2id = UUID.fromString("06abcdef-2222-2222-2222-222222222202");
    UUID timer3id = UUID.fromString("06abcdef-3333-3333-3333-333333333303");
    UUID timer4id = UUID.fromString("06abcdef-4444-4444-4444-444444444404");
    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerType timer2type = CommonTimerType.ONCE;
    CommonTimerType timer3type = CommonTimerType.INTERVAL;
    CommonTimerType timer4type = CommonTimerType.ONCE;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    CommonTimerIntervalPattern timer2intPtrn = null;
    CommonTimerIntervalPattern timer3intPtrn = CommonTimerIntervalPattern.DAYS_PER_MONTH;
    CommonTimerIntervalPattern timer4intPtrn = null;
    OffsetDateTime timer1startDate = OffsetDateTime.now().plusDays(2);
    OffsetDateTime timer2startDate = OffsetDateTime.now().plusDays(4);
    OffsetDateTime timer3startDate = OffsetDateTime.now().plusDays(1);
    OffsetDateTime timer4startDate = OffsetDateTime.now().plusDays(3);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusWeeks(2);
    OffsetDateTime timer2endDate = timer2startDate;
    OffsetDateTime timer3endDate = OffsetDateTime.now().plusDays(9);
    OffsetDateTime timer4endDate = timer4startDate;
    Integer timer1intervalPeriod = 1;
    Integer timer2intervalPeriod = null;
    Integer timer3intervalPeriod = 2;
    Integer timer4intervalPeriod = null;
    OffsetDateTime timer1date1 = OffsetDateTime.now().plusDays(5).plusMinutes(5);
    OffsetDateTime timer1date2 = OffsetDateTime.now().plusDays(5).plusMinutes(10);
    OffsetDateTime timer2date1 = OffsetDateTime.now().plusDays(5).plusMinutes(15);
    OffsetDateTime timer2date2 = OffsetDateTime.now().plusDays(5).plusMinutes(20);
    OffsetDateTime timer3date1 = OffsetDateTime.now().plusDays(5).plusMinutes(25);
    OffsetDateTime timer3date2 = OffsetDateTime.now().plusDays(5).plusMinutes(30);
    OffsetDateTime timer4date1 = OffsetDateTime.now().plusDays(5).plusMinutes(35);
    OffsetDateTime timer4date2 = OffsetDateTime.now().plusDays(5).plusMinutes(40);

    // Timer Targets
    UUID timer1tar1contr1 = UUID.fromString("06abcdef-1111-cccc-ffff-888888888111");
    UUID timer1tar1contr2 = UUID.fromString("06abcdef-2222-cccc-ffff-888888888112");
    UUID timer2tar1contr1 = UUID.fromString("06abcdef-1111-cccc-ffff-888888888221");
    UUID timer2tar1contr2 = UUID.fromString("06abcdef-2222-cccc-ffff-888888888222");
    UUID timer3tar1contr1 = UUID.fromString("06abcdef-1111-cccc-ffff-888888888331");
    UUID timer3tar1contr2 = UUID.fromString("06abcdef-2222-cccc-ffff-888888888332");
    UUID timer4tar1contr1 = UUID.fromString("06abcdef-1111-cccc-ffff-888888888441");
    UUID timer4tar1contr2 = UUID.fromString("06abcdef-2222-cccc-ffff-888888888442");
    CommonTimerAction timer1tar1act1 = CommonTimerAction.DIRECT;
    CommonTimerAction timer1tar1act2 = CommonTimerAction.SLIDER_DOWN;
    CommonTimerAction timer2tar1act1 = CommonTimerAction.SLIDER_UP;
    CommonTimerAction timer2tar1act2 = CommonTimerAction.TOGGLE;
    CommonTimerAction timer3tar1act1 = CommonTimerAction.SWITCH_OFF;
    CommonTimerAction timer3tar1act2 = CommonTimerAction.SWITCH_ON;
    CommonTimerAction timer4tar1act1 = CommonTimerAction.DIRECT;
    CommonTimerAction timer4tar1act2 = CommonTimerAction.SLIDER_UP;
    String timer1tar1val1 = "5.55";
    String timer1tar1val2 = "test only - null a";
    String timer2tar1val1 = "test only - null b";
    String timer2tar1val2 = "test only - null c";
    String timer3tar1val1 = "test only - null d";
    String timer3tar1val2 = "test only - null e";
    String timer4tar1val1 = "test only - null f";
    String timer4tar1val2 = "test only - null g";

    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(timer1tar1contr1) //
        .action(timer1tar1act1) //
        .directValue(timer1tar1val1);
    TimerInfoTimerTarget timer1target2 = new TimerInfoTimerTarget() //
        .controlId(timer1tar1contr2) //
        .action(timer1tar1act2) //
        .directValue(timer1tar1val2);
    TimerInfoTimerTarget timer2target1 = new TimerInfoTimerTarget() //
        .controlId(timer2tar1contr1) //
        .action(timer2tar1act1) //
        .directValue(timer2tar1val1);
    TimerInfoTimerTarget timer2target2 = new TimerInfoTimerTarget() //
        .controlId(timer2tar1contr2) //
        .action(timer2tar1act2) //
        .directValue(timer2tar1val2);
    TimerInfoTimerTarget timer3target1 = new TimerInfoTimerTarget() //
        .controlId(timer3tar1contr1) //
        .action(timer3tar1act1) //
        .directValue(timer3tar1val1);
    TimerInfoTimerTarget timer3target2 = new TimerInfoTimerTarget() //
        .controlId(timer3tar1contr2) //
        .action(timer3tar1act2) //
        .directValue(timer3tar1val2);
    TimerInfoTimerTarget timer4target1 = new TimerInfoTimerTarget() //
        .controlId(timer4tar1contr1) //
        .action(timer4tar1act1) //
        .directValue(timer4tar1val1);
    TimerInfoTimerTarget timer4target2 = new TimerInfoTimerTarget() //
        .controlId(timer4tar1contr2) //
        .action(timer4tar1act2) //
        .directValue(timer4tar1val2);

    TimerInfo timer1 = new TimerInfo();
    timer1.setTimerId(timer1id);
    timer1.setTimerType(timer1type);
    timer1.setIntervalPattern(timer1intPtrn);
    timer1.setStartDateTime(timer1startDate);
    timer1.setEndDateTime(timer1endDate);
    timer1.setIntervalPeriod(timer1intervalPeriod);
    timer1.addSetDateTimesItem(timer1date1);
    timer1.addSetDateTimesItem(timer1date2);
    timer1.addTimerTargetItem(timer1target1);
    timer1.addTimerTargetItem(timer1target2);
    TimerInfo timer2 = new TimerInfo();
    timer2.setTimerId(timer2id);
    timer2.setTimerType(timer2type);
    timer2.setIntervalPattern(timer2intPtrn);
    timer2.setStartDateTime(timer2startDate);
    timer2.setEndDateTime(timer2endDate);
    timer2.setIntervalPeriod(timer2intervalPeriod);
    timer2.addSetDateTimesItem(timer2date1);
    timer2.addSetDateTimesItem(timer2date2);
    timer2.addTimerTargetItem(timer2target1);
    timer2.addTimerTargetItem(timer2target2);
    TimerInfo timer3 = new TimerInfo();
    timer3.setTimerId(timer3id);
    timer3.setTimerType(timer3type);
    timer3.setIntervalPattern(timer3intPtrn);
    timer3.setStartDateTime(timer3startDate);
    timer3.setEndDateTime(timer3endDate);
    timer3.setIntervalPeriod(timer3intervalPeriod);
    timer3.addSetDateTimesItem(timer3date1);
    timer3.addSetDateTimesItem(timer3date2);
    timer3.addTimerTargetItem(timer3target1);
    timer3.addTimerTargetItem(timer3target2);
    TimerInfo timer4 = new TimerInfo();
    timer4.setTimerId(timer4id);
    timer4.setTimerType(timer4type);
    timer4.setIntervalPattern(timer4intPtrn);
    timer4.setStartDateTime(timer4startDate);
    timer4.setEndDateTime(timer4endDate);
    timer4.setIntervalPeriod(timer4intervalPeriod);
    timer4.addSetDateTimesItem(timer4date1);
    timer4.addSetDateTimesItem(timer4date2);
    timer4.addTimerTargetItem(timer4target1);
    timer4.addTimerTargetItem(timer4target2);

    Map<UUID, TimerInfo> timerMap = new HashMap<>();
    timerMap.put(timer1id, timer1);
    timerMap.put(timer2id, timer2);
    timerMap.put(timer3id, timer3);
    timerMap.put(timer4id, timer4);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    doReturn(timerMap).when(this.timerRepository).findAll();

    // Call service
    CommonGetListResponse<TimerInfo> retListResp = this.timerService.getTimers(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.timerRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 400.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getTimers (skip = entry max + 1)")
  void testGetTimersSkipMaxPlus1() {
    Integer skip = null; // Will be changed below
    Integer limit = null;

    UUID timer1id = UUID.fromString("06abcdef-1111-1111-1111-111111111101");
    UUID timer2id = UUID.fromString("06abcdef-2222-2222-2222-222222222202");
    UUID timer3id = UUID.fromString("06abcdef-3333-3333-3333-333333333303");
    UUID timer4id = UUID.fromString("06abcdef-4444-4444-4444-444444444404");
    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerType timer2type = CommonTimerType.ONCE;
    CommonTimerType timer3type = CommonTimerType.INTERVAL;
    CommonTimerType timer4type = CommonTimerType.ONCE;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    CommonTimerIntervalPattern timer2intPtrn = null;
    CommonTimerIntervalPattern timer3intPtrn = CommonTimerIntervalPattern.DAYS_PER_MONTH;
    CommonTimerIntervalPattern timer4intPtrn = null;
    OffsetDateTime timer1startDate = OffsetDateTime.now().plusDays(2);
    OffsetDateTime timer2startDate = OffsetDateTime.now().plusDays(4);
    OffsetDateTime timer3startDate = OffsetDateTime.now().plusDays(1);
    OffsetDateTime timer4startDate = OffsetDateTime.now().plusDays(3);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusWeeks(2);
    OffsetDateTime timer2endDate = timer2startDate;
    OffsetDateTime timer3endDate = OffsetDateTime.now().plusDays(9);
    OffsetDateTime timer4endDate = timer4startDate;
    Integer timer1intervalPeriod = 1;
    Integer timer2intervalPeriod = null;
    Integer timer3intervalPeriod = 2;
    Integer timer4intervalPeriod = null;
    OffsetDateTime timer1date1 = OffsetDateTime.now().plusDays(5).plusMinutes(5);
    OffsetDateTime timer1date2 = OffsetDateTime.now().plusDays(5).plusMinutes(10);
    OffsetDateTime timer2date1 = OffsetDateTime.now().plusDays(5).plusMinutes(15);
    OffsetDateTime timer2date2 = OffsetDateTime.now().plusDays(5).plusMinutes(20);
    OffsetDateTime timer3date1 = OffsetDateTime.now().plusDays(5).plusMinutes(25);
    OffsetDateTime timer3date2 = OffsetDateTime.now().plusDays(5).plusMinutes(30);
    OffsetDateTime timer4date1 = OffsetDateTime.now().plusDays(5).plusMinutes(35);
    OffsetDateTime timer4date2 = OffsetDateTime.now().plusDays(5).plusMinutes(40);

    // Timer Targets
    UUID timer1tar1contr1 = UUID.fromString("06abcdef-1111-cccc-ffff-888888888111");
    UUID timer1tar1contr2 = UUID.fromString("06abcdef-2222-cccc-ffff-888888888112");
    UUID timer2tar1contr1 = UUID.fromString("06abcdef-1111-cccc-ffff-888888888221");
    UUID timer2tar1contr2 = UUID.fromString("06abcdef-2222-cccc-ffff-888888888222");
    UUID timer3tar1contr1 = UUID.fromString("06abcdef-1111-cccc-ffff-888888888331");
    UUID timer3tar1contr2 = UUID.fromString("06abcdef-2222-cccc-ffff-888888888332");
    UUID timer4tar1contr1 = UUID.fromString("06abcdef-1111-cccc-ffff-888888888441");
    UUID timer4tar1contr2 = UUID.fromString("06abcdef-2222-cccc-ffff-888888888442");
    CommonTimerAction timer1tar1act1 = CommonTimerAction.DIRECT;
    CommonTimerAction timer1tar1act2 = CommonTimerAction.SLIDER_DOWN;
    CommonTimerAction timer2tar1act1 = CommonTimerAction.SLIDER_UP;
    CommonTimerAction timer2tar1act2 = CommonTimerAction.TOGGLE;
    CommonTimerAction timer3tar1act1 = CommonTimerAction.SWITCH_OFF;
    CommonTimerAction timer3tar1act2 = CommonTimerAction.SWITCH_ON;
    CommonTimerAction timer4tar1act1 = CommonTimerAction.DIRECT;
    CommonTimerAction timer4tar1act2 = CommonTimerAction.SLIDER_UP;
    String timer1tar1val1 = "5.55";
    String timer1tar1val2 = "test only - null a";
    String timer2tar1val1 = "test only - null b";
    String timer2tar1val2 = "test only - null c";
    String timer3tar1val1 = "test only - null d";
    String timer3tar1val2 = "test only - null e";
    String timer4tar1val1 = "test only - null f";
    String timer4tar1val2 = "test only - null g";

    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(timer1tar1contr1) //
        .action(timer1tar1act1) //
        .directValue(timer1tar1val1);
    TimerInfoTimerTarget timer1target2 = new TimerInfoTimerTarget() //
        .controlId(timer1tar1contr2) //
        .action(timer1tar1act2) //
        .directValue(timer1tar1val2);
    TimerInfoTimerTarget timer2target1 = new TimerInfoTimerTarget() //
        .controlId(timer2tar1contr1) //
        .action(timer2tar1act1) //
        .directValue(timer2tar1val1);
    TimerInfoTimerTarget timer2target2 = new TimerInfoTimerTarget() //
        .controlId(timer2tar1contr2) //
        .action(timer2tar1act2) //
        .directValue(timer2tar1val2);
    TimerInfoTimerTarget timer3target1 = new TimerInfoTimerTarget() //
        .controlId(timer3tar1contr1) //
        .action(timer3tar1act1) //
        .directValue(timer3tar1val1);
    TimerInfoTimerTarget timer3target2 = new TimerInfoTimerTarget() //
        .controlId(timer3tar1contr2) //
        .action(timer3tar1act2) //
        .directValue(timer3tar1val2);
    TimerInfoTimerTarget timer4target1 = new TimerInfoTimerTarget() //
        .controlId(timer4tar1contr1) //
        .action(timer4tar1act1) //
        .directValue(timer4tar1val1);
    TimerInfoTimerTarget timer4target2 = new TimerInfoTimerTarget() //
        .controlId(timer4tar1contr2) //
        .action(timer4tar1act2) //
        .directValue(timer4tar1val2);

    TimerInfo timer1 = new TimerInfo();
    timer1.setTimerId(timer1id);
    timer1.setTimerType(timer1type);
    timer1.setIntervalPattern(timer1intPtrn);
    timer1.setStartDateTime(timer1startDate);
    timer1.setEndDateTime(timer1endDate);
    timer1.setIntervalPeriod(timer1intervalPeriod);
    timer1.addSetDateTimesItem(timer1date1);
    timer1.addSetDateTimesItem(timer1date2);
    timer1.addTimerTargetItem(timer1target1);
    timer1.addTimerTargetItem(timer1target2);
    TimerInfo timer2 = new TimerInfo();
    timer2.setTimerId(timer2id);
    timer2.setTimerType(timer2type);
    timer2.setIntervalPattern(timer2intPtrn);
    timer2.setStartDateTime(timer2startDate);
    timer2.setEndDateTime(timer2endDate);
    timer2.setIntervalPeriod(timer2intervalPeriod);
    timer2.addSetDateTimesItem(timer2date1);
    timer2.addSetDateTimesItem(timer2date2);
    timer2.addTimerTargetItem(timer2target1);
    timer2.addTimerTargetItem(timer2target2);
    TimerInfo timer3 = new TimerInfo();
    timer3.setTimerId(timer3id);
    timer3.setTimerType(timer3type);
    timer3.setIntervalPattern(timer3intPtrn);
    timer3.setStartDateTime(timer3startDate);
    timer3.setEndDateTime(timer3endDate);
    timer3.setIntervalPeriod(timer3intervalPeriod);
    timer3.addSetDateTimesItem(timer3date1);
    timer3.addSetDateTimesItem(timer3date2);
    timer3.addTimerTargetItem(timer3target1);
    timer3.addTimerTargetItem(timer3target2);
    TimerInfo timer4 = new TimerInfo();
    timer4.setTimerId(timer4id);
    timer4.setTimerType(timer4type);
    timer4.setIntervalPattern(timer4intPtrn);
    timer4.setStartDateTime(timer4startDate);
    timer4.setEndDateTime(timer4endDate);
    timer4.setIntervalPeriod(timer4intervalPeriod);
    timer4.addSetDateTimesItem(timer4date1);
    timer4.addSetDateTimesItem(timer4date2);
    timer4.addTimerTargetItem(timer4target1);
    timer4.addTimerTargetItem(timer4target2);

    Map<UUID, TimerInfo> timerMap = new HashMap<>();
    timerMap.put(timer1id, timer1);
    timerMap.put(timer2id, timer2);
    timerMap.put(timer3id, timer3);
    timerMap.put(timer4id, timer4);

    skip = timerMap.size() + 1;

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(timerMap).when(this.timerRepository).findAll();

    // Call service
    CommonGetListResponse<TimerInfo> retListResp = this.timerService.getTimers(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.timerRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getTimers (limit = entry max + 1)")
  void testGetTimersLimitMaxPlus1() {
    Integer skip = null;
    Integer limit = null; // Will be changed below

    UUID timer1id = UUID.fromString("06abcdef-1111-1111-1111-111111111101");
    UUID timer2id = UUID.fromString("06abcdef-2222-2222-2222-222222222202");
    UUID timer3id = UUID.fromString("06abcdef-3333-3333-3333-333333333303");
    UUID timer4id = UUID.fromString("06abcdef-4444-4444-4444-444444444404");
    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerType timer2type = CommonTimerType.ONCE;
    CommonTimerType timer3type = CommonTimerType.INTERVAL;
    CommonTimerType timer4type = CommonTimerType.ONCE;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    CommonTimerIntervalPattern timer2intPtrn = null;
    CommonTimerIntervalPattern timer3intPtrn = CommonTimerIntervalPattern.DAYS_PER_MONTH;
    CommonTimerIntervalPattern timer4intPtrn = null;
    OffsetDateTime timer1startDate = OffsetDateTime.now().plusDays(2);
    OffsetDateTime timer2startDate = OffsetDateTime.now().plusDays(4);
    OffsetDateTime timer3startDate = OffsetDateTime.now().plusDays(1);
    OffsetDateTime timer4startDate = OffsetDateTime.now().plusDays(3);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusWeeks(2);
    OffsetDateTime timer2endDate = timer2startDate;
    OffsetDateTime timer3endDate = OffsetDateTime.now().plusDays(9);
    OffsetDateTime timer4endDate = timer4startDate;
    Integer timer1intervalPeriod = 1;
    Integer timer2intervalPeriod = null;
    Integer timer3intervalPeriod = 2;
    Integer timer4intervalPeriod = null;
    OffsetDateTime timer1date1 = OffsetDateTime.now().plusDays(5).plusMinutes(5);
    OffsetDateTime timer1date2 = OffsetDateTime.now().plusDays(5).plusMinutes(10);
    OffsetDateTime timer2date1 = OffsetDateTime.now().plusDays(5).plusMinutes(15);
    OffsetDateTime timer2date2 = OffsetDateTime.now().plusDays(5).plusMinutes(20);
    OffsetDateTime timer3date1 = OffsetDateTime.now().plusDays(5).plusMinutes(25);
    OffsetDateTime timer3date2 = OffsetDateTime.now().plusDays(5).plusMinutes(30);
    OffsetDateTime timer4date1 = OffsetDateTime.now().plusDays(5).plusMinutes(35);
    OffsetDateTime timer4date2 = OffsetDateTime.now().plusDays(5).plusMinutes(40);

    // Timer Targets
    UUID timer1tar1contr1 = UUID.fromString("06abcdef-1111-cccc-ffff-888888888111");
    UUID timer1tar1contr2 = UUID.fromString("06abcdef-2222-cccc-ffff-888888888112");
    UUID timer2tar1contr1 = UUID.fromString("06abcdef-1111-cccc-ffff-888888888221");
    UUID timer2tar1contr2 = UUID.fromString("06abcdef-2222-cccc-ffff-888888888222");
    UUID timer3tar1contr1 = UUID.fromString("06abcdef-1111-cccc-ffff-888888888331");
    UUID timer3tar1contr2 = UUID.fromString("06abcdef-2222-cccc-ffff-888888888332");
    UUID timer4tar1contr1 = UUID.fromString("06abcdef-1111-cccc-ffff-888888888441");
    UUID timer4tar1contr2 = UUID.fromString("06abcdef-2222-cccc-ffff-888888888442");
    CommonTimerAction timer1tar1act1 = CommonTimerAction.DIRECT;
    CommonTimerAction timer1tar1act2 = CommonTimerAction.SLIDER_DOWN;
    CommonTimerAction timer2tar1act1 = CommonTimerAction.SLIDER_UP;
    CommonTimerAction timer2tar1act2 = CommonTimerAction.TOGGLE;
    CommonTimerAction timer3tar1act1 = CommonTimerAction.SWITCH_OFF;
    CommonTimerAction timer3tar1act2 = CommonTimerAction.SWITCH_ON;
    CommonTimerAction timer4tar1act1 = CommonTimerAction.DIRECT;
    CommonTimerAction timer4tar1act2 = CommonTimerAction.SLIDER_UP;
    String timer1tar1val1 = "5.55";
    String timer1tar1val2 = "test only - null a";
    String timer2tar1val1 = "test only - null b";
    String timer2tar1val2 = "test only - null c";
    String timer3tar1val1 = "test only - null d";
    String timer3tar1val2 = "test only - null e";
    String timer4tar1val1 = "test only - null f";
    String timer4tar1val2 = "test only - null g";

    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(timer1tar1contr1) //
        .action(timer1tar1act1) //
        .directValue(timer1tar1val1);
    TimerInfoTimerTarget timer1target2 = new TimerInfoTimerTarget() //
        .controlId(timer1tar1contr2) //
        .action(timer1tar1act2) //
        .directValue(timer1tar1val2);
    TimerInfoTimerTarget timer2target1 = new TimerInfoTimerTarget() //
        .controlId(timer2tar1contr1) //
        .action(timer2tar1act1) //
        .directValue(timer2tar1val1);
    TimerInfoTimerTarget timer2target2 = new TimerInfoTimerTarget() //
        .controlId(timer2tar1contr2) //
        .action(timer2tar1act2) //
        .directValue(timer2tar1val2);
    TimerInfoTimerTarget timer3target1 = new TimerInfoTimerTarget() //
        .controlId(timer3tar1contr1) //
        .action(timer3tar1act1) //
        .directValue(timer3tar1val1);
    TimerInfoTimerTarget timer3target2 = new TimerInfoTimerTarget() //
        .controlId(timer3tar1contr2) //
        .action(timer3tar1act2) //
        .directValue(timer3tar1val2);
    TimerInfoTimerTarget timer4target1 = new TimerInfoTimerTarget() //
        .controlId(timer4tar1contr1) //
        .action(timer4tar1act1) //
        .directValue(timer4tar1val1);
    TimerInfoTimerTarget timer4target2 = new TimerInfoTimerTarget() //
        .controlId(timer4tar1contr2) //
        .action(timer4tar1act2) //
        .directValue(timer4tar1val2);

    TimerInfo timer1 = new TimerInfo();
    timer1.setTimerId(timer1id);
    timer1.setTimerType(timer1type);
    timer1.setIntervalPattern(timer1intPtrn);
    timer1.setStartDateTime(timer1startDate);
    timer1.setEndDateTime(timer1endDate);
    timer1.setIntervalPeriod(timer1intervalPeriod);
    timer1.addSetDateTimesItem(timer1date1);
    timer1.addSetDateTimesItem(timer1date2);
    timer1.addTimerTargetItem(timer1target1);
    timer1.addTimerTargetItem(timer1target2);
    TimerInfo timer2 = new TimerInfo();
    timer2.setTimerId(timer2id);
    timer2.setTimerType(timer2type);
    timer2.setIntervalPattern(timer2intPtrn);
    timer2.setStartDateTime(timer2startDate);
    timer2.setEndDateTime(timer2endDate);
    timer2.setIntervalPeriod(timer2intervalPeriod);
    timer2.addSetDateTimesItem(timer2date1);
    timer2.addSetDateTimesItem(timer2date2);
    timer2.addTimerTargetItem(timer2target1);
    timer2.addTimerTargetItem(timer2target2);
    TimerInfo timer3 = new TimerInfo();
    timer3.setTimerId(timer3id);
    timer3.setTimerType(timer3type);
    timer3.setIntervalPattern(timer3intPtrn);
    timer3.setStartDateTime(timer3startDate);
    timer3.setEndDateTime(timer3endDate);
    timer3.setIntervalPeriod(timer3intervalPeriod);
    timer3.addSetDateTimesItem(timer3date1);
    timer3.addSetDateTimesItem(timer3date2);
    timer3.addTimerTargetItem(timer3target1);
    timer3.addTimerTargetItem(timer3target2);
    TimerInfo timer4 = new TimerInfo();
    timer4.setTimerId(timer4id);
    timer4.setTimerType(timer4type);
    timer4.setIntervalPattern(timer4intPtrn);
    timer4.setStartDateTime(timer4startDate);
    timer4.setEndDateTime(timer4endDate);
    timer4.setIntervalPeriod(timer4intervalPeriod);
    timer4.addSetDateTimesItem(timer4date1);
    timer4.addSetDateTimesItem(timer4date2);
    timer4.addTimerTargetItem(timer4target1);
    timer4.addTimerTargetItem(timer4target2);

    Map<UUID, TimerInfo> timerMap = new HashMap<>();
    timerMap.put(timer1id, timer1);
    timerMap.put(timer2id, timer2);
    timerMap.put(timer3id, timer3);
    timerMap.put(timer4id, timer4);

    limit = timerMap.size() + 1;

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(timerMap).when(this.timerRepository).findAll();

    // Call service
    CommonGetListResponse<TimerInfo> retListResp = this.timerService.getTimers(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.timerRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be sorted by start date ascending.",
        retListResp.getObjList(), contains(timer3, timer1, timer4, timer2));
  }

  @Test
  @DisplayName("TEST getTimers (skip = 1, limit = 1)")
  void testGetTimersSkip1Limit1() {
    Integer skip = 1;
    Integer limit = 1;

    UUID timer1id = UUID.fromString("06abcdef-1111-1111-1111-111111111101");
    UUID timer2id = UUID.fromString("06abcdef-2222-2222-2222-222222222202");
    UUID timer3id = UUID.fromString("06abcdef-3333-3333-3333-333333333303");
    UUID timer4id = UUID.fromString("06abcdef-4444-4444-4444-444444444404");
    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerType timer2type = CommonTimerType.ONCE;
    CommonTimerType timer3type = CommonTimerType.INTERVAL;
    CommonTimerType timer4type = CommonTimerType.ONCE;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    CommonTimerIntervalPattern timer2intPtrn = null;
    CommonTimerIntervalPattern timer3intPtrn = CommonTimerIntervalPattern.DAYS_PER_MONTH;
    CommonTimerIntervalPattern timer4intPtrn = null;
    OffsetDateTime timer1startDate = OffsetDateTime.now().plusDays(2);
    OffsetDateTime timer2startDate = OffsetDateTime.now().plusDays(4);
    OffsetDateTime timer3startDate = OffsetDateTime.now().plusDays(1);
    OffsetDateTime timer4startDate = OffsetDateTime.now().plusDays(3);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusWeeks(2);
    OffsetDateTime timer2endDate = timer2startDate;
    OffsetDateTime timer3endDate = OffsetDateTime.now().plusDays(9);
    OffsetDateTime timer4endDate = timer4startDate;
    Integer timer1intervalPeriod = 1;
    Integer timer2intervalPeriod = null;
    Integer timer3intervalPeriod = 2;
    Integer timer4intervalPeriod = null;
    OffsetDateTime timer1date1 = OffsetDateTime.now().plusDays(5).plusMinutes(5);
    OffsetDateTime timer1date2 = OffsetDateTime.now().plusDays(5).plusMinutes(10);
    OffsetDateTime timer2date1 = OffsetDateTime.now().plusDays(5).plusMinutes(15);
    OffsetDateTime timer2date2 = OffsetDateTime.now().plusDays(5).plusMinutes(20);
    OffsetDateTime timer3date1 = OffsetDateTime.now().plusDays(5).plusMinutes(25);
    OffsetDateTime timer3date2 = OffsetDateTime.now().plusDays(5).plusMinutes(30);
    OffsetDateTime timer4date1 = OffsetDateTime.now().plusDays(5).plusMinutes(35);
    OffsetDateTime timer4date2 = OffsetDateTime.now().plusDays(5).plusMinutes(40);

    // Timer Targets
    UUID timer1tar1contr1 = UUID.fromString("06abcdef-1111-cccc-ffff-888888888111");
    UUID timer1tar1contr2 = UUID.fromString("06abcdef-2222-cccc-ffff-888888888112");
    UUID timer2tar1contr1 = UUID.fromString("06abcdef-1111-cccc-ffff-888888888221");
    UUID timer2tar1contr2 = UUID.fromString("06abcdef-2222-cccc-ffff-888888888222");
    UUID timer3tar1contr1 = UUID.fromString("06abcdef-1111-cccc-ffff-888888888331");
    UUID timer3tar1contr2 = UUID.fromString("06abcdef-2222-cccc-ffff-888888888332");
    UUID timer4tar1contr1 = UUID.fromString("06abcdef-1111-cccc-ffff-888888888441");
    UUID timer4tar1contr2 = UUID.fromString("06abcdef-2222-cccc-ffff-888888888442");
    CommonTimerAction timer1tar1act1 = CommonTimerAction.DIRECT;
    CommonTimerAction timer1tar1act2 = CommonTimerAction.SLIDER_DOWN;
    CommonTimerAction timer2tar1act1 = CommonTimerAction.SLIDER_UP;
    CommonTimerAction timer2tar1act2 = CommonTimerAction.TOGGLE;
    CommonTimerAction timer3tar1act1 = CommonTimerAction.SWITCH_OFF;
    CommonTimerAction timer3tar1act2 = CommonTimerAction.SWITCH_ON;
    CommonTimerAction timer4tar1act1 = CommonTimerAction.DIRECT;
    CommonTimerAction timer4tar1act2 = CommonTimerAction.SLIDER_UP;
    String timer1tar1val1 = "5.55";
    String timer1tar1val2 = "test only - null a";
    String timer2tar1val1 = "test only - null b";
    String timer2tar1val2 = "test only - null c";
    String timer3tar1val1 = "test only - null d";
    String timer3tar1val2 = "test only - null e";
    String timer4tar1val1 = "test only - null f";
    String timer4tar1val2 = "test only - null g";

    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(timer1tar1contr1) //
        .action(timer1tar1act1) //
        .directValue(timer1tar1val1);
    TimerInfoTimerTarget timer1target2 = new TimerInfoTimerTarget() //
        .controlId(timer1tar1contr2) //
        .action(timer1tar1act2) //
        .directValue(timer1tar1val2);
    TimerInfoTimerTarget timer2target1 = new TimerInfoTimerTarget() //
        .controlId(timer2tar1contr1) //
        .action(timer2tar1act1) //
        .directValue(timer2tar1val1);
    TimerInfoTimerTarget timer2target2 = new TimerInfoTimerTarget() //
        .controlId(timer2tar1contr2) //
        .action(timer2tar1act2) //
        .directValue(timer2tar1val2);
    TimerInfoTimerTarget timer3target1 = new TimerInfoTimerTarget() //
        .controlId(timer3tar1contr1) //
        .action(timer3tar1act1) //
        .directValue(timer3tar1val1);
    TimerInfoTimerTarget timer3target2 = new TimerInfoTimerTarget() //
        .controlId(timer3tar1contr2) //
        .action(timer3tar1act2) //
        .directValue(timer3tar1val2);
    TimerInfoTimerTarget timer4target1 = new TimerInfoTimerTarget() //
        .controlId(timer4tar1contr1) //
        .action(timer4tar1act1) //
        .directValue(timer4tar1val1);
    TimerInfoTimerTarget timer4target2 = new TimerInfoTimerTarget() //
        .controlId(timer4tar1contr2) //
        .action(timer4tar1act2) //
        .directValue(timer4tar1val2);

    TimerInfo timer1 = new TimerInfo();
    timer1.setTimerId(timer1id);
    timer1.setTimerType(timer1type);
    timer1.setIntervalPattern(timer1intPtrn);
    timer1.setStartDateTime(timer1startDate);
    timer1.setEndDateTime(timer1endDate);
    timer1.setIntervalPeriod(timer1intervalPeriod);
    timer1.addSetDateTimesItem(timer1date1);
    timer1.addSetDateTimesItem(timer1date2);
    timer1.addTimerTargetItem(timer1target1);
    timer1.addTimerTargetItem(timer1target2);
    TimerInfo timer2 = new TimerInfo();
    timer2.setTimerId(timer2id);
    timer2.setTimerType(timer2type);
    timer2.setIntervalPattern(timer2intPtrn);
    timer2.setStartDateTime(timer2startDate);
    timer2.setEndDateTime(timer2endDate);
    timer2.setIntervalPeriod(timer2intervalPeriod);
    timer2.addSetDateTimesItem(timer2date1);
    timer2.addSetDateTimesItem(timer2date2);
    timer2.addTimerTargetItem(timer2target1);
    timer2.addTimerTargetItem(timer2target2);
    TimerInfo timer3 = new TimerInfo();
    timer3.setTimerId(timer3id);
    timer3.setTimerType(timer3type);
    timer3.setIntervalPattern(timer3intPtrn);
    timer3.setStartDateTime(timer3startDate);
    timer3.setEndDateTime(timer3endDate);
    timer3.setIntervalPeriod(timer3intervalPeriod);
    timer3.addSetDateTimesItem(timer3date1);
    timer3.addSetDateTimesItem(timer3date2);
    timer3.addTimerTargetItem(timer3target1);
    timer3.addTimerTargetItem(timer3target2);
    TimerInfo timer4 = new TimerInfo();
    timer4.setTimerId(timer4id);
    timer4.setTimerType(timer4type);
    timer4.setIntervalPattern(timer4intPtrn);
    timer4.setStartDateTime(timer4startDate);
    timer4.setEndDateTime(timer4endDate);
    timer4.setIntervalPeriod(timer4intervalPeriod);
    timer4.addSetDateTimesItem(timer4date1);
    timer4.addSetDateTimesItem(timer4date2);
    timer4.addTimerTargetItem(timer4target1);
    timer4.addTimerTargetItem(timer4target2);

    Map<UUID, TimerInfo> timerMap = new HashMap<>();
    timerMap.put(timer1id, timer1);
    timerMap.put(timer2id, timer2);
    timerMap.put(timer3id, timer3);
    timerMap.put(timer4id, timer4);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(timerMap).when(this.timerRepository).findAll();

    // Call service
    CommonGetListResponse<TimerInfo> retListResp = this.timerService.getTimers(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.timerRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must only be one, the second entry only.",
        retListResp.getObjList(), contains(timer1));
  }

  @Test
  @DisplayName("TEST getTimers (skip = 2)")
  void testGetTimersSkip2() {
    Integer skip = 2;
    Integer limit = null;

    UUID timer1id = UUID.fromString("06abcdef-1111-1111-1111-111111111101");
    UUID timer2id = UUID.fromString("06abcdef-2222-2222-2222-222222222202");
    UUID timer3id = UUID.fromString("06abcdef-3333-3333-3333-333333333303");
    UUID timer4id = UUID.fromString("06abcdef-4444-4444-4444-444444444404");
    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerType timer2type = CommonTimerType.ONCE;
    CommonTimerType timer3type = CommonTimerType.INTERVAL;
    CommonTimerType timer4type = CommonTimerType.ONCE;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    CommonTimerIntervalPattern timer2intPtrn = null;
    CommonTimerIntervalPattern timer3intPtrn = CommonTimerIntervalPattern.DAYS_PER_MONTH;
    CommonTimerIntervalPattern timer4intPtrn = null;
    OffsetDateTime timer1startDate = OffsetDateTime.now().plusDays(2);
    OffsetDateTime timer2startDate = OffsetDateTime.now().plusDays(4);
    OffsetDateTime timer3startDate = OffsetDateTime.now().plusDays(1);
    OffsetDateTime timer4startDate = OffsetDateTime.now().plusDays(3);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusWeeks(2);
    OffsetDateTime timer2endDate = timer2startDate;
    OffsetDateTime timer3endDate = OffsetDateTime.now().plusDays(9);
    OffsetDateTime timer4endDate = timer4startDate;
    Integer timer1intervalPeriod = 1;
    Integer timer2intervalPeriod = null;
    Integer timer3intervalPeriod = 2;
    Integer timer4intervalPeriod = null;
    OffsetDateTime timer1date1 = OffsetDateTime.now().plusDays(5).plusMinutes(5);
    OffsetDateTime timer1date2 = OffsetDateTime.now().plusDays(5).plusMinutes(10);
    OffsetDateTime timer2date1 = OffsetDateTime.now().plusDays(5).plusMinutes(15);
    OffsetDateTime timer2date2 = OffsetDateTime.now().plusDays(5).plusMinutes(20);
    OffsetDateTime timer3date1 = OffsetDateTime.now().plusDays(5).plusMinutes(25);
    OffsetDateTime timer3date2 = OffsetDateTime.now().plusDays(5).plusMinutes(30);
    OffsetDateTime timer4date1 = OffsetDateTime.now().plusDays(5).plusMinutes(35);
    OffsetDateTime timer4date2 = OffsetDateTime.now().plusDays(5).plusMinutes(40);

    // Timer Targets
    UUID timer1tar1contr1 = UUID.fromString("06abcdef-1111-cccc-ffff-888888888111");
    UUID timer1tar1contr2 = UUID.fromString("06abcdef-2222-cccc-ffff-888888888112");
    UUID timer2tar1contr1 = UUID.fromString("06abcdef-1111-cccc-ffff-888888888221");
    UUID timer2tar1contr2 = UUID.fromString("06abcdef-2222-cccc-ffff-888888888222");
    UUID timer3tar1contr1 = UUID.fromString("06abcdef-1111-cccc-ffff-888888888331");
    UUID timer3tar1contr2 = UUID.fromString("06abcdef-2222-cccc-ffff-888888888332");
    UUID timer4tar1contr1 = UUID.fromString("06abcdef-1111-cccc-ffff-888888888441");
    UUID timer4tar1contr2 = UUID.fromString("06abcdef-2222-cccc-ffff-888888888442");
    CommonTimerAction timer1tar1act1 = CommonTimerAction.DIRECT;
    CommonTimerAction timer1tar1act2 = CommonTimerAction.SLIDER_DOWN;
    CommonTimerAction timer2tar1act1 = CommonTimerAction.SLIDER_UP;
    CommonTimerAction timer2tar1act2 = CommonTimerAction.TOGGLE;
    CommonTimerAction timer3tar1act1 = CommonTimerAction.SWITCH_OFF;
    CommonTimerAction timer3tar1act2 = CommonTimerAction.SWITCH_ON;
    CommonTimerAction timer4tar1act1 = CommonTimerAction.DIRECT;
    CommonTimerAction timer4tar1act2 = CommonTimerAction.SLIDER_UP;
    String timer1tar1val1 = "5.55";
    String timer1tar1val2 = "test only - null a";
    String timer2tar1val1 = "test only - null b";
    String timer2tar1val2 = "test only - null c";
    String timer3tar1val1 = "test only - null d";
    String timer3tar1val2 = "test only - null e";
    String timer4tar1val1 = "test only - null f";
    String timer4tar1val2 = "test only - null g";

    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(timer1tar1contr1) //
        .action(timer1tar1act1) //
        .directValue(timer1tar1val1);
    TimerInfoTimerTarget timer1target2 = new TimerInfoTimerTarget() //
        .controlId(timer1tar1contr2) //
        .action(timer1tar1act2) //
        .directValue(timer1tar1val2);
    TimerInfoTimerTarget timer2target1 = new TimerInfoTimerTarget() //
        .controlId(timer2tar1contr1) //
        .action(timer2tar1act1) //
        .directValue(timer2tar1val1);
    TimerInfoTimerTarget timer2target2 = new TimerInfoTimerTarget() //
        .controlId(timer2tar1contr2) //
        .action(timer2tar1act2) //
        .directValue(timer2tar1val2);
    TimerInfoTimerTarget timer3target1 = new TimerInfoTimerTarget() //
        .controlId(timer3tar1contr1) //
        .action(timer3tar1act1) //
        .directValue(timer3tar1val1);
    TimerInfoTimerTarget timer3target2 = new TimerInfoTimerTarget() //
        .controlId(timer3tar1contr2) //
        .action(timer3tar1act2) //
        .directValue(timer3tar1val2);
    TimerInfoTimerTarget timer4target1 = new TimerInfoTimerTarget() //
        .controlId(timer4tar1contr1) //
        .action(timer4tar1act1) //
        .directValue(timer4tar1val1);
    TimerInfoTimerTarget timer4target2 = new TimerInfoTimerTarget() //
        .controlId(timer4tar1contr2) //
        .action(timer4tar1act2) //
        .directValue(timer4tar1val2);

    TimerInfo timer1 = new TimerInfo();
    timer1.setTimerId(timer1id);
    timer1.setTimerType(timer1type);
    timer1.setIntervalPattern(timer1intPtrn);
    timer1.setStartDateTime(timer1startDate);
    timer1.setEndDateTime(timer1endDate);
    timer1.setIntervalPeriod(timer1intervalPeriod);
    timer1.addSetDateTimesItem(timer1date1);
    timer1.addSetDateTimesItem(timer1date2);
    timer1.addTimerTargetItem(timer1target1);
    timer1.addTimerTargetItem(timer1target2);
    TimerInfo timer2 = new TimerInfo();
    timer2.setTimerId(timer2id);
    timer2.setTimerType(timer2type);
    timer2.setIntervalPattern(timer2intPtrn);
    timer2.setStartDateTime(timer2startDate);
    timer2.setEndDateTime(timer2endDate);
    timer2.setIntervalPeriod(timer2intervalPeriod);
    timer2.addSetDateTimesItem(timer2date1);
    timer2.addSetDateTimesItem(timer2date2);
    timer2.addTimerTargetItem(timer2target1);
    timer2.addTimerTargetItem(timer2target2);
    TimerInfo timer3 = new TimerInfo();
    timer3.setTimerId(timer3id);
    timer3.setTimerType(timer3type);
    timer3.setIntervalPattern(timer3intPtrn);
    timer3.setStartDateTime(timer3startDate);
    timer3.setEndDateTime(timer3endDate);
    timer3.setIntervalPeriod(timer3intervalPeriod);
    timer3.addSetDateTimesItem(timer3date1);
    timer3.addSetDateTimesItem(timer3date2);
    timer3.addTimerTargetItem(timer3target1);
    timer3.addTimerTargetItem(timer3target2);
    TimerInfo timer4 = new TimerInfo();
    timer4.setTimerId(timer4id);
    timer4.setTimerType(timer4type);
    timer4.setIntervalPattern(timer4intPtrn);
    timer4.setStartDateTime(timer4startDate);
    timer4.setEndDateTime(timer4endDate);
    timer4.setIntervalPeriod(timer4intervalPeriod);
    timer4.addSetDateTimesItem(timer4date1);
    timer4.addSetDateTimesItem(timer4date2);
    timer4.addTimerTargetItem(timer4target1);
    timer4.addTimerTargetItem(timer4target2);

    Map<UUID, TimerInfo> timerMap = new HashMap<>();
    timerMap.put(timer1id, timer1);
    timerMap.put(timer2id, timer2);
    timerMap.put(timer3id, timer3);
    timerMap.put(timer4id, timer4);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(timerMap).when(this.timerRepository).findAll();

    // Call service
    CommonGetListResponse<TimerInfo> retListResp = this.timerService.getTimers(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.timerRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The first two entries must not be included.", retListResp.getObjList(),
        contains(timer4, timer2));
  }

  @Test
  @DisplayName("TEST getTimers (limit = 2)")
  void testGetTimersLimit2() {
    Integer skip = null;
    Integer limit = 2;

    UUID timer1id = UUID.fromString("06abcdef-1111-1111-1111-111111111101");
    UUID timer2id = UUID.fromString("06abcdef-2222-2222-2222-222222222202");
    UUID timer3id = UUID.fromString("06abcdef-3333-3333-3333-333333333303");
    UUID timer4id = UUID.fromString("06abcdef-4444-4444-4444-444444444404");
    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerType timer2type = CommonTimerType.ONCE;
    CommonTimerType timer3type = CommonTimerType.INTERVAL;
    CommonTimerType timer4type = CommonTimerType.ONCE;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    CommonTimerIntervalPattern timer2intPtrn = null;
    CommonTimerIntervalPattern timer3intPtrn = CommonTimerIntervalPattern.DAYS_PER_MONTH;
    CommonTimerIntervalPattern timer4intPtrn = null;
    OffsetDateTime timer1startDate = OffsetDateTime.now().plusDays(2);
    OffsetDateTime timer2startDate = OffsetDateTime.now().plusDays(4);
    OffsetDateTime timer3startDate = OffsetDateTime.now().plusDays(1);
    OffsetDateTime timer4startDate = OffsetDateTime.now().plusDays(3);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusWeeks(2);
    OffsetDateTime timer2endDate = timer2startDate;
    OffsetDateTime timer3endDate = OffsetDateTime.now().plusDays(9);
    OffsetDateTime timer4endDate = timer4startDate;
    Integer timer1intervalPeriod = 1;
    Integer timer2intervalPeriod = null;
    Integer timer3intervalPeriod = 2;
    Integer timer4intervalPeriod = null;
    OffsetDateTime timer1date1 = OffsetDateTime.now().plusDays(5).plusMinutes(5);
    OffsetDateTime timer1date2 = OffsetDateTime.now().plusDays(5).plusMinutes(10);
    OffsetDateTime timer2date1 = OffsetDateTime.now().plusDays(5).plusMinutes(15);
    OffsetDateTime timer2date2 = OffsetDateTime.now().plusDays(5).plusMinutes(20);
    OffsetDateTime timer3date1 = OffsetDateTime.now().plusDays(5).plusMinutes(25);
    OffsetDateTime timer3date2 = OffsetDateTime.now().plusDays(5).plusMinutes(30);
    OffsetDateTime timer4date1 = OffsetDateTime.now().plusDays(5).plusMinutes(35);
    OffsetDateTime timer4date2 = OffsetDateTime.now().plusDays(5).plusMinutes(40);

    // Timer Targets
    UUID timer1tar1contr1 = UUID.fromString("06abcdef-1111-cccc-ffff-888888888111");
    UUID timer1tar1contr2 = UUID.fromString("06abcdef-2222-cccc-ffff-888888888112");
    UUID timer2tar1contr1 = UUID.fromString("06abcdef-1111-cccc-ffff-888888888221");
    UUID timer2tar1contr2 = UUID.fromString("06abcdef-2222-cccc-ffff-888888888222");
    UUID timer3tar1contr1 = UUID.fromString("06abcdef-1111-cccc-ffff-888888888331");
    UUID timer3tar1contr2 = UUID.fromString("06abcdef-2222-cccc-ffff-888888888332");
    UUID timer4tar1contr1 = UUID.fromString("06abcdef-1111-cccc-ffff-888888888441");
    UUID timer4tar1contr2 = UUID.fromString("06abcdef-2222-cccc-ffff-888888888442");
    CommonTimerAction timer1tar1act1 = CommonTimerAction.DIRECT;
    CommonTimerAction timer1tar1act2 = CommonTimerAction.SLIDER_DOWN;
    CommonTimerAction timer2tar1act1 = CommonTimerAction.SLIDER_UP;
    CommonTimerAction timer2tar1act2 = CommonTimerAction.TOGGLE;
    CommonTimerAction timer3tar1act1 = CommonTimerAction.SWITCH_OFF;
    CommonTimerAction timer3tar1act2 = CommonTimerAction.SWITCH_ON;
    CommonTimerAction timer4tar1act1 = CommonTimerAction.DIRECT;
    CommonTimerAction timer4tar1act2 = CommonTimerAction.SLIDER_UP;
    String timer1tar1val1 = "5.55";
    String timer1tar1val2 = "test only - null a";
    String timer2tar1val1 = "test only - null b";
    String timer2tar1val2 = "test only - null c";
    String timer3tar1val1 = "test only - null d";
    String timer3tar1val2 = "test only - null e";
    String timer4tar1val1 = "test only - null f";
    String timer4tar1val2 = "test only - null g";

    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(timer1tar1contr1) //
        .action(timer1tar1act1) //
        .directValue(timer1tar1val1);
    TimerInfoTimerTarget timer1target2 = new TimerInfoTimerTarget() //
        .controlId(timer1tar1contr2) //
        .action(timer1tar1act2) //
        .directValue(timer1tar1val2);
    TimerInfoTimerTarget timer2target1 = new TimerInfoTimerTarget() //
        .controlId(timer2tar1contr1) //
        .action(timer2tar1act1) //
        .directValue(timer2tar1val1);
    TimerInfoTimerTarget timer2target2 = new TimerInfoTimerTarget() //
        .controlId(timer2tar1contr2) //
        .action(timer2tar1act2) //
        .directValue(timer2tar1val2);
    TimerInfoTimerTarget timer3target1 = new TimerInfoTimerTarget() //
        .controlId(timer3tar1contr1) //
        .action(timer3tar1act1) //
        .directValue(timer3tar1val1);
    TimerInfoTimerTarget timer3target2 = new TimerInfoTimerTarget() //
        .controlId(timer3tar1contr2) //
        .action(timer3tar1act2) //
        .directValue(timer3tar1val2);
    TimerInfoTimerTarget timer4target1 = new TimerInfoTimerTarget() //
        .controlId(timer4tar1contr1) //
        .action(timer4tar1act1) //
        .directValue(timer4tar1val1);
    TimerInfoTimerTarget timer4target2 = new TimerInfoTimerTarget() //
        .controlId(timer4tar1contr2) //
        .action(timer4tar1act2) //
        .directValue(timer4tar1val2);

    TimerInfo timer1 = new TimerInfo();
    timer1.setTimerId(timer1id);
    timer1.setTimerType(timer1type);
    timer1.setIntervalPattern(timer1intPtrn);
    timer1.setStartDateTime(timer1startDate);
    timer1.setEndDateTime(timer1endDate);
    timer1.setIntervalPeriod(timer1intervalPeriod);
    timer1.addSetDateTimesItem(timer1date1);
    timer1.addSetDateTimesItem(timer1date2);
    timer1.addTimerTargetItem(timer1target1);
    timer1.addTimerTargetItem(timer1target2);
    TimerInfo timer2 = new TimerInfo();
    timer2.setTimerId(timer2id);
    timer2.setTimerType(timer2type);
    timer2.setIntervalPattern(timer2intPtrn);
    timer2.setStartDateTime(timer2startDate);
    timer2.setEndDateTime(timer2endDate);
    timer2.setIntervalPeriod(timer2intervalPeriod);
    timer2.addSetDateTimesItem(timer2date1);
    timer2.addSetDateTimesItem(timer2date2);
    timer2.addTimerTargetItem(timer2target1);
    timer2.addTimerTargetItem(timer2target2);
    TimerInfo timer3 = new TimerInfo();
    timer3.setTimerId(timer3id);
    timer3.setTimerType(timer3type);
    timer3.setIntervalPattern(timer3intPtrn);
    timer3.setStartDateTime(timer3startDate);
    timer3.setEndDateTime(timer3endDate);
    timer3.setIntervalPeriod(timer3intervalPeriod);
    timer3.addSetDateTimesItem(timer3date1);
    timer3.addSetDateTimesItem(timer3date2);
    timer3.addTimerTargetItem(timer3target1);
    timer3.addTimerTargetItem(timer3target2);
    TimerInfo timer4 = new TimerInfo();
    timer4.setTimerId(timer4id);
    timer4.setTimerType(timer4type);
    timer4.setIntervalPattern(timer4intPtrn);
    timer4.setStartDateTime(timer4startDate);
    timer4.setEndDateTime(timer4endDate);
    timer4.setIntervalPeriod(timer4intervalPeriod);
    timer4.addSetDateTimesItem(timer4date1);
    timer4.addSetDateTimesItem(timer4date2);
    timer4.addTimerTargetItem(timer4target1);
    timer4.addTimerTargetItem(timer4target2);

    Map<UUID, TimerInfo> timerMap = new HashMap<>();
    timerMap.put(timer1id, timer1);
    timerMap.put(timer2id, timer2);
    timerMap.put(timer3id, timer3);
    timerMap.put(timer4id, timer4);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(timerMap).when(this.timerRepository).findAll();

    // Call service
    CommonGetListResponse<TimerInfo> retListResp = this.timerService.getTimers(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.timerRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must only be 2, sorted by start date ascending.",
        retListResp.getObjList(), contains(timer3, timer1));
  }

  @Test
  @DisplayName("TEST getTimers (No entries in the DB(null))")
  void testGetTimersNoEntryNull() {
    Integer skip = null;
    Integer limit = null;

    Map<UUID, TimerInfo> timerMap = null;

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(timerMap).when(this.timerRepository).findAll();

    // Call service
    CommonGetListResponse<TimerInfo> retListResp = this.timerService.getTimers(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.timerRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getTimers (No entries in the DB)")
  void testGetTimersNoEntry() {
    Integer skip = null;
    Integer limit = null;

    Map<UUID, TimerInfo> timerMap = Collections.emptyMap();

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(timerMap).when(this.timerRepository).findAll();

    // Call service
    CommonGetListResponse<TimerInfo> retListResp = this.timerService.getTimers(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.timerRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getTimerById")
  void testGetTimerById() {
    UUID timer1id = UUID.fromString("06abcdef-1111-1111-1111-111111111101");
    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    OffsetDateTime timer1startDate = OffsetDateTime.now().plusDays(2);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusWeeks(2);
    Integer timer1intervalPeriod = 1;
    OffsetDateTime timer1date1 = OffsetDateTime.now().plusDays(5).plusMinutes(5);
    OffsetDateTime timer1date2 = OffsetDateTime.now().plusDays(5).plusMinutes(10);

    // Timer Targets
    UUID timer1tar1contr1 = UUID.fromString("06abcdef-1111-cccc-ffff-888888888111");
    UUID timer1tar1contr2 = UUID.fromString("06abcdef-2222-cccc-ffff-888888888112");
    CommonTimerAction timer1tar1act1 = CommonTimerAction.DIRECT;
    CommonTimerAction timer1tar1act2 = CommonTimerAction.SLIDER_DOWN;
    String timer1tar1val1 = "5.55";
    String timer1tar1val2 = "test only - null a";

    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(timer1tar1contr1) //
        .action(timer1tar1act1) //
        .directValue(timer1tar1val1);
    TimerInfoTimerTarget timer1target2 = new TimerInfoTimerTarget() //
        .controlId(timer1tar1contr2) //
        .action(timer1tar1act2) //
        .directValue(timer1tar1val2);

    TimerInfo timer1 = new TimerInfo();
    timer1.setTimerId(timer1id);
    timer1.setTimerType(timer1type);
    timer1.setIntervalPattern(timer1intPtrn);
    timer1.setStartDateTime(timer1startDate);
    timer1.setEndDateTime(timer1endDate);
    timer1.setIntervalPeriod(timer1intervalPeriod);
    timer1.addSetDateTimesItem(timer1date1);
    timer1.addSetDateTimesItem(timer1date2);
    timer1.addTimerTargetItem(timer1target1);
    timer1.addTimerTargetItem(timer1target2);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.timerRepository.findById(eq(timer1id))).thenReturn(timer1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call service
    CommonGetResponse<TimerInfo> operResp = this.timerService.getTimerById(timer1id);

    // Verify that appropriate Repository method was called
    verify(this.timerRepository, times(1)).findById(arg1.capture());

    // Assertions
    assertEquals(timer1id, arg1.getValue(), "Argument must match.");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertEquals(timer1, operResp.getObj(), "Timer1 must be returned.");
    assertEquals(timer1id, operResp.getObj().getTimerId(), "Timer1's id must be returned.");
    assertEquals(timer1type, operResp.getObj().getTimerType(), "Timer1's type must be returned.");
    assertEquals(timer1intPtrn, operResp.getObj().getIntervalPattern(),
        "Timer1's interval pattern must be returned.");
    assertEquals(timer1startDate, operResp.getObj().getStartDateTime(),
        "Timer1's start DateTime must be returned.");
    assertEquals(timer1endDate, operResp.getObj().getEndDateTime(),
        "Timer1's end DateTime must be returned.");
    assertEquals(timer1intervalPeriod, operResp.getObj().getIntervalPeriod(),
        "Timer1's interval period must be returned.");
    assertThat("Timer1's targets must be returned in any order.",
        operResp.getObj().getSetDateTimes(), contains(timer1date1, timer1date2));
  }

  @Test
  @DisplayName("TEST getTimerById (Id not found)")
  void testGetTimerByIdNotFound() {
    UUID timer1id = UUID.fromString("06abcdef-1111-1111-1111-111111111101");
    UUID othDevid = UUID.fromString("00000000-1111-0361-032c-2b963f789af1");
    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    OffsetDateTime timer1startDate = OffsetDateTime.now().plusDays(2);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusWeeks(2);
    Integer timer1intervalPeriod = 1;
    OffsetDateTime timer1date1 = OffsetDateTime.now().plusDays(5).plusMinutes(5);
    OffsetDateTime timer1date2 = OffsetDateTime.now().plusDays(5).plusMinutes(10);

    // Timer Targets
    UUID timer1tar1contr1 = UUID.fromString("06abcdef-1111-cccc-ffff-888888888111");
    UUID timer1tar1contr2 = UUID.fromString("06abcdef-2222-cccc-ffff-888888888112");
    CommonTimerAction timer1tar1act1 = CommonTimerAction.DIRECT;
    CommonTimerAction timer1tar1act2 = CommonTimerAction.SLIDER_DOWN;
    String timer1tar1val1 = "5.55";
    String timer1tar1val2 = "test only - null a";

    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(timer1tar1contr1) //
        .action(timer1tar1act1) //
        .directValue(timer1tar1val1);
    TimerInfoTimerTarget timer1target2 = new TimerInfoTimerTarget() //
        .controlId(timer1tar1contr2) //
        .action(timer1tar1act2) //
        .directValue(timer1tar1val2);

    TimerInfo timer1 = new TimerInfo();
    timer1.setTimerId(timer1id);
    timer1.setTimerType(timer1type);
    timer1.setIntervalPattern(timer1intPtrn);
    timer1.setStartDateTime(timer1startDate);
    timer1.setEndDateTime(timer1endDate);
    timer1.setIntervalPeriod(timer1intervalPeriod);
    timer1.addSetDateTimesItem(timer1date1);
    timer1.addSetDateTimesItem(timer1date2);
    timer1.addTimerTargetItem(timer1target1);
    timer1.addTimerTargetItem(timer1target2);

    HttpStatus expectedStatus = HttpStatus.NOT_FOUND;

    // Mock(s)
    when(this.timerRepository.findById(eq(timer1id))).thenReturn(timer1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call service
    CommonGetResponse<TimerInfo> operResp = this.timerService.getTimerById(othDevid);

    // Verify that appropriate Repository method was called
    verify(this.timerRepository, times(1)).findById(arg1.capture());

    // Assertions
    assertEquals(othDevid, arg1.getValue(), "Argument must match.");
    assertNotNull(operResp, "Return object is not null.");
    assertNull(operResp.getObj(), "Inner object must be null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 404.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST registerTimer (type = ONCE (valid))")
  void testRegisterTimerTypeOnceValid() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.ONCE;
    CommonTimerIntervalPattern timer1intPtrn = null;
    OffsetDateTime timer1startDate = OffsetDateTime.now();
    OffsetDateTime timer1endDate = timer1startDate;
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = null;

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CREATED;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was called
    verify(this.timerRepository, times(1)).add(arg1.capture());

    // Assertions
    assertNotNull(arg1.getValue(), "Argument (TimerInfo) must not be null.");
    UUID newTimerId = arg1.getValue().getTimerId();
    assertNotNull(newTimerId, "Argument's timerId must not be null.");
    assertEquals(timer1type, arg1.getValue().getTimerType(), "Timer1's timerType must match.");
    assertEquals(timer1intPtrn, arg1.getValue().getIntervalPattern(),
        "Timer1's intervalPattern must match.");
    assertEquals(timer1startDate, arg1.getValue().getStartDateTime(),
        "Timer1's startDateTime must match.");
    assertEquals(timer1endDate, arg1.getValue().getEndDateTime(),
        "Timer1's endDateTime must match.");
    assertEquals(timer1intPeriod, arg1.getValue().getIntervalPeriod(),
        "Timer1's intervalPeriod must match.");
    assertEquals(timer1dateTimes, arg1.getValue().getSetDateTimes(),
        "Timer1's setDateTimes must match.");
    assertEquals(timer1targets, arg1.getValue().getTimerTarget(),
        "Timer1's timerTarget must match.");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 201.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNotNull(operResp.getId(), "Returned zoneId must not be null.");
    assertEquals(newTimerId, operResp.getId(),
        "Returned timerId must match the id passed to the repo.");
  }

  @Test
  @DisplayName("TEST registerTimer (type = ONCE (null start time))")
  void testRegisterTimerTypeOnceNullStartTime() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.ONCE;
    CommonTimerIntervalPattern timer1intPtrn = null;
    OffsetDateTime timer1startDate = null;
    OffsetDateTime timer1endDate = OffsetDateTime.now();
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = null;

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned timerId must be null.");
  }

  @Test
  @DisplayName("TEST registerTimer (type = ONCE (null end time))")
  void testRegisterTimerTypeOnceNullEndTime() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.ONCE;
    CommonTimerIntervalPattern timer1intPtrn = null;
    OffsetDateTime timer1startDate = OffsetDateTime.now();
    OffsetDateTime timer1endDate = null;
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = null;

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned timerId must be null.");
  }

  @Test
  @DisplayName("TEST registerTimer (type = ONCE (start time != end time))")
  void testRegisterTimerTypeOnceStartVsEndNotEqual() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.ONCE;
    CommonTimerIntervalPattern timer1intPtrn = null;
    OffsetDateTime timer1startDate = OffsetDateTime.now();
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusDays(3);
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = null;

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned timerId must be null.");
  }

  @Test
  @DisplayName("TEST registerTimer (type = ONCE (interval period = -1))")
  void testRegisterTimerTypeOnceIntervalPeriodNeg1() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.ONCE;
    CommonTimerIntervalPattern timer1intPtrn = null;
    OffsetDateTime timer1startDate = OffsetDateTime.now();
    OffsetDateTime timer1endDate = timer1startDate;
    Integer timer1intPeriod = -1;
    List<OffsetDateTime> timer1dateTimes = null;

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned timerId must be null.");
  }

  @Test
  @DisplayName("TEST registerTimer (type = ONCE (interval period = 0))")
  void testRegisterTimerTypeOnceIntervalPeriod0() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.ONCE;
    CommonTimerIntervalPattern timer1intPtrn = null;
    OffsetDateTime timer1startDate = OffsetDateTime.now();
    OffsetDateTime timer1endDate = timer1startDate;
    Integer timer1intPeriod = 0;
    List<OffsetDateTime> timer1dateTimes = null;

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned timerId must be null.");
  }

  @Test
  @DisplayName("TEST registerTimer (type = ONCE (interval period > 1))")
  void testRegisterTimerTypeOnceIntervalPeriodMoreThan1() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.ONCE;
    CommonTimerIntervalPattern timer1intPtrn = null;
    OffsetDateTime timer1startDate = OffsetDateTime.now();
    OffsetDateTime timer1endDate = timer1startDate;
    Integer timer1intPeriod = 2;
    List<OffsetDateTime> timer1dateTimes = null;

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned timerId must be null.");
  }

  @Test
  @DisplayName("TEST registerTimer (type = ONCE (interval pattern provided))")
  void testRegisterTimerTypeOnceIntervalPatternHave() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.ONCE;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    OffsetDateTime timer1startDate = OffsetDateTime.now();
    OffsetDateTime timer1endDate = timer1startDate;
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = null;

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned timerId must be null.");
  }

  @Test
  @DisplayName("TEST registerTimer (type = ONCE (null interval period))")
  void testRegisterTimerTypeOnceNullIntervalPeriod() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.ONCE;
    CommonTimerIntervalPattern timer1intPtrn = null;
    OffsetDateTime timer1startDate = OffsetDateTime.now();
    OffsetDateTime timer1endDate = timer1startDate;
    Integer timer1intPeriod = null;
    List<OffsetDateTime> timer1dateTimes = null;

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned timerId must be null.");
  }

  @Test
  @DisplayName("TEST registerTimer (type = INTERVAL, intervalPattern = timesPerDay (valid))")
  void testRegisterTimerTypeIntervalPtnTimesPerDayValid() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    UUID controlId2 = UUID.fromString("22888888-2222-2222-2222-111111111102");
    UUID controlId3 = UUID.fromString("22888888-3333-3333-3333-111111111103");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    String contr2name = "Name 2";
    String contr2descr = "Descr 2";
    String contr2val = null;
    String contr3name = "Name 3";
    String contr3descr = "Descr 3";
    String contr3val = null;
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);
    DeviceControlInfo contr2 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId2) //
        .name(contr2name) //
        .description(contr2descr) //
        .currentValue(contr2val);
    DeviceControlInfo contr3 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId3) //
        .name(contr3name) //
        .description(contr3descr) //
        .currentValue(contr3val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.TIMES_PER_DAY;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = (24 * 60);
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    CommonTimerAction timer2action = CommonTimerAction.SLIDER_UP;
    String timer2dirVal = null;
    CommonTimerAction timer3action = CommonTimerAction.SLIDER_DOWN;
    String timer3dirVal = "";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    TimerInfoTimerTarget timer1target2 = new TimerInfoTimerTarget() //
        .controlId(controlId2) //
        .action(timer2action) //
        .directValue(timer2dirVal);
    TimerInfoTimerTarget timer1target3 = new TimerInfoTimerTarget() //
        .controlId(controlId3) //
        .action(timer3action) //
        .directValue(timer3dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);
    timer1targets.add(timer1target2);
    timer1targets.add(timer1target3);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    inputContrSet.add(controlId2);
    inputContrSet.add(controlId3);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);
    contrList.add(contr2);
    contrList.add(contr3);

    HttpStatus expectedStatus = HttpStatus.CREATED;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was called
    verify(this.timerRepository, times(1)).add(arg1.capture());

    // Assertions
    assertNotNull(arg1.getValue(), "Argument (TimerInfo) must not be null.");
    UUID newTimerId = arg1.getValue().getTimerId();
    assertNotNull(newTimerId, "Argument's timerId must not be null.");
    assertEquals(timer1type, arg1.getValue().getTimerType(), "Timer1's timerType must match.");
    assertEquals(timer1intPtrn, arg1.getValue().getIntervalPattern(),
        "Timer1's intervalPattern must match.");
    assertEquals(timer1startDate, arg1.getValue().getStartDateTime(),
        "Timer1's startDateTime must match.");
    assertEquals(timer1endDate, arg1.getValue().getEndDateTime(),
        "Timer1's endDateTime must match.");
    assertEquals(timer1intPeriod, arg1.getValue().getIntervalPeriod(),
        "Timer1's intervalPeriod must match.");
    assertEquals(timer1dateTimes, arg1.getValue().getSetDateTimes(),
        "Timer1's setDateTimes must match.");
    assertEquals(timer1targets, arg1.getValue().getTimerTarget(),
        "Timer1's timerTarget must match.");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 201.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNotNull(operResp.getId(), "Returned timerId must not be null.");
    assertEquals(newTimerId, operResp.getId(),
        "Returned timerId must match the id passed to the repo.");
  }

  @Test
  @DisplayName("TEST registerTimer (type = INTERVAL, intervalPattern = daysPerWeek (valid))")
  void testRegisterTimerTypeIntervalPtnDaysPerWeekValid() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAYS_PER_WEEK;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 6;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CREATED;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was called
    verify(this.timerRepository, times(1)).add(arg1.capture());

    // Assertions
    assertNotNull(arg1.getValue(), "Argument (TimerInfo) must not be null.");
    UUID newTimerId = arg1.getValue().getTimerId();
    assertNotNull(newTimerId, "Argument's timerId must not be null.");
    assertEquals(timer1type, arg1.getValue().getTimerType(), "Timer1's timerType must match.");
    assertEquals(timer1intPtrn, arg1.getValue().getIntervalPattern(),
        "Timer1's intervalPattern must match.");
    assertEquals(timer1startDate, arg1.getValue().getStartDateTime(),
        "Timer1's startDateTime must match.");
    assertEquals(timer1endDate, arg1.getValue().getEndDateTime(),
        "Timer1's endDateTime must match.");
    assertEquals(timer1intPeriod, arg1.getValue().getIntervalPeriod(),
        "Timer1's intervalPeriod must match.");
    assertEquals(timer1dateTimes, arg1.getValue().getSetDateTimes(),
        "Timer1's setDateTimes must match.");
    assertEquals(timer1targets, arg1.getValue().getTimerTarget(),
        "Timer1's timerTarget must match.");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 201.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNotNull(operResp.getId(), "Returned timerId must not be null.");
    assertEquals(newTimerId, operResp.getId(),
        "Returned timerId must match the id passed to the repo.");
  }

  @Test
  @DisplayName("TEST registerTimer (type = INTERVAL, intervalPattern = daysPerMonth (valid))")
  void testRegisterTimerTypeIntervalPtnDaysPerMonthValid() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAYS_PER_MONTH;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 29;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CREATED;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was called
    verify(this.timerRepository, times(1)).add(arg1.capture());

    // Assertions
    assertNotNull(arg1.getValue(), "Argument (TimerInfo) must not be null.");
    UUID newTimerId = arg1.getValue().getTimerId();
    assertNotNull(newTimerId, "Argument's timerId must not be null.");
    assertEquals(timer1type, arg1.getValue().getTimerType(), "Timer1's timerType must match.");
    assertEquals(timer1intPtrn, arg1.getValue().getIntervalPattern(),
        "Timer1's intervalPattern must match.");
    assertEquals(timer1startDate, arg1.getValue().getStartDateTime(),
        "Timer1's startDateTime must match.");
    assertEquals(timer1endDate, arg1.getValue().getEndDateTime(),
        "Timer1's endDateTime must match.");
    assertEquals(timer1intPeriod, arg1.getValue().getIntervalPeriod(),
        "Timer1's intervalPeriod must match.");
    assertEquals(timer1dateTimes, arg1.getValue().getSetDateTimes(),
        "Timer1's setDateTimes must match.");
    assertEquals(timer1targets, arg1.getValue().getTimerTarget(),
        "Timer1's timerTarget must match.");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 201.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNotNull(operResp.getId(), "Returned timerId must not be null.");
    assertEquals(newTimerId, operResp.getId(),
        "Returned timerId must match the id passed to the repo.");
  }

  @Test
  @DisplayName("TEST registerTimer (type = INTERVAL, intervalPattern = daysPerYear (valid))")
  void testRegisterTimerTypeIntervalPtnDaysPerYearValid() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAYS_PER_YEAR;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 364;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CREATED;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was called
    verify(this.timerRepository, times(1)).add(arg1.capture());

    // Assertions
    assertNotNull(arg1.getValue(), "Argument (TimerInfo) must not be null.");
    UUID newTimerId = arg1.getValue().getTimerId();
    assertNotNull(newTimerId, "Argument's timerId must not be null.");
    assertEquals(timer1type, arg1.getValue().getTimerType(), "Timer1's timerType must match.");
    assertEquals(timer1intPtrn, arg1.getValue().getIntervalPattern(),
        "Timer1's intervalPattern must match.");
    assertEquals(timer1startDate, arg1.getValue().getStartDateTime(),
        "Timer1's startDateTime must match.");
    assertEquals(timer1endDate, arg1.getValue().getEndDateTime(),
        "Timer1's endDateTime must match.");
    assertEquals(timer1intPeriod, arg1.getValue().getIntervalPeriod(),
        "Timer1's intervalPeriod must match.");
    assertEquals(timer1dateTimes, arg1.getValue().getSetDateTimes(),
        "Timer1's setDateTimes must match.");
    assertEquals(timer1targets, arg1.getValue().getTimerTarget(),
        "Timer1's timerTarget must match.");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 201.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNotNull(operResp.getId(), "Returned timerId must not be null.");
    assertEquals(newTimerId, operResp.getId(),
        "Returned timerId must match the id passed to the repo.");
  }

  @Test
  @DisplayName("TEST registerTimer (type = INTERVAL, intervalPattern = daily (valid))")
  void testRegisterTimerTypeIntervalPtnDailyValid() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CREATED;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was called
    verify(this.timerRepository, times(1)).add(arg1.capture());

    // Assertions
    assertNotNull(arg1.getValue(), "Argument (TimerInfo) must not be null.");
    UUID newTimerId = arg1.getValue().getTimerId();
    assertNotNull(newTimerId, "Argument's timerId must not be null.");
    assertEquals(timer1type, arg1.getValue().getTimerType(), "Timer1's timerType must match.");
    assertEquals(timer1intPtrn, arg1.getValue().getIntervalPattern(),
        "Timer1's intervalPattern must match.");
    assertEquals(timer1startDate, arg1.getValue().getStartDateTime(),
        "Timer1's startDateTime must match.");
    assertEquals(timer1endDate, arg1.getValue().getEndDateTime(),
        "Timer1's endDateTime must match.");
    assertEquals(timer1intPeriod, arg1.getValue().getIntervalPeriod(),
        "Timer1's intervalPeriod must match.");
    assertEquals(timer1dateTimes, arg1.getValue().getSetDateTimes(),
        "Timer1's setDateTimes must match.");
    assertEquals(timer1targets, arg1.getValue().getTimerTarget(),
        "Timer1's timerTarget must match.");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 201.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNotNull(operResp.getId(), "Returned timerId must not be null.");
    assertEquals(newTimerId, operResp.getId(),
        "Returned timerId must match the id passed to the repo.");
  }

  @Test
  @DisplayName("TEST registerTimer (type = INTERVAL, intervalPattern = weekly (valid))")
  void testRegisterTimerTypeIntervalPtnWeeklyValid() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.WEEKLY;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CREATED;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was called
    verify(this.timerRepository, times(1)).add(arg1.capture());

    // Assertions
    assertNotNull(arg1.getValue(), "Argument (TimerInfo) must not be null.");
    UUID newTimerId = arg1.getValue().getTimerId();
    assertNotNull(newTimerId, "Argument's timerId must not be null.");
    assertEquals(timer1type, arg1.getValue().getTimerType(), "Timer1's timerType must match.");
    assertEquals(timer1intPtrn, arg1.getValue().getIntervalPattern(),
        "Timer1's intervalPattern must match.");
    assertEquals(timer1startDate, arg1.getValue().getStartDateTime(),
        "Timer1's startDateTime must match.");
    assertEquals(timer1endDate, arg1.getValue().getEndDateTime(),
        "Timer1's endDateTime must match.");
    assertEquals(timer1intPeriod, arg1.getValue().getIntervalPeriod(),
        "Timer1's intervalPeriod must match.");
    assertEquals(timer1dateTimes, arg1.getValue().getSetDateTimes(),
        "Timer1's setDateTimes must match.");
    assertEquals(timer1targets, arg1.getValue().getTimerTarget(),
        "Timer1's timerTarget must match.");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 201.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNotNull(operResp.getId(), "Returned timerId must not be null.");
    assertEquals(newTimerId, operResp.getId(),
        "Returned timerId must match the id passed to the repo.");
  }

  @Test
  @DisplayName("TEST registerTimer (type = INTERVAL, intervalPattern = monthly (valid))")
  void testRegisterTimerTypeIntervalPtnMonthlyValid() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.MONTHLY;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CREATED;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was called
    verify(this.timerRepository, times(1)).add(arg1.capture());

    // Assertions
    assertNotNull(arg1.getValue(), "Argument (TimerInfo) must not be null.");
    UUID newTimerId = arg1.getValue().getTimerId();
    assertNotNull(newTimerId, "Argument's timerId must not be null.");
    assertEquals(timer1type, arg1.getValue().getTimerType(), "Timer1's timerType must match.");
    assertEquals(timer1intPtrn, arg1.getValue().getIntervalPattern(),
        "Timer1's intervalPattern must match.");
    assertEquals(timer1startDate, arg1.getValue().getStartDateTime(),
        "Timer1's startDateTime must match.");
    assertEquals(timer1endDate, arg1.getValue().getEndDateTime(),
        "Timer1's endDateTime must match.");
    assertEquals(timer1intPeriod, arg1.getValue().getIntervalPeriod(),
        "Timer1's intervalPeriod must match.");
    assertEquals(timer1dateTimes, arg1.getValue().getSetDateTimes(),
        "Timer1's setDateTimes must match.");
    assertEquals(timer1targets, arg1.getValue().getTimerTarget(),
        "Timer1's timerTarget must match.");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 201.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNotNull(operResp.getId(), "Returned timerId must not be null.");
    assertEquals(newTimerId, operResp.getId(),
        "Returned timerId must match the id passed to the repo.");
  }

  @Test
  @DisplayName("TEST registerTimer (type = INTERVAL, intervalPattern = yearly (valid))")
  void testRegisterTimerTypeIntervalPtnYearlyValid() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.YEARLY;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CREATED;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was called
    verify(this.timerRepository, times(1)).add(arg1.capture());

    // Assertions
    assertNotNull(arg1.getValue(), "Argument (TimerInfo) must not be null.");
    UUID newTimerId = arg1.getValue().getTimerId();
    assertNotNull(newTimerId, "Argument's timerId must not be null.");
    assertEquals(timer1type, arg1.getValue().getTimerType(), "Timer1's timerType must match.");
    assertEquals(timer1intPtrn, arg1.getValue().getIntervalPattern(),
        "Timer1's intervalPattern must match.");
    assertEquals(timer1startDate, arg1.getValue().getStartDateTime(),
        "Timer1's startDateTime must match.");
    assertEquals(timer1endDate, arg1.getValue().getEndDateTime(),
        "Timer1's endDateTime must match.");
    assertEquals(timer1intPeriod, arg1.getValue().getIntervalPeriod(),
        "Timer1's intervalPeriod must match.");
    assertEquals(timer1dateTimes, arg1.getValue().getSetDateTimes(),
        "Timer1's setDateTimes must match.");
    assertEquals(timer1targets, arg1.getValue().getTimerTarget(),
        "Timer1's timerTarget must match.");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 201.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNotNull(operResp.getId(), "Returned timerId must not be null.");
    assertEquals(newTimerId, operResp.getId(),
        "Returned timerId must match the id passed to the repo.");
  }

  @Test
  @DisplayName("TEST registerTimer (type = INTERVAL, intervalPattern = custom (valid))")
  void testRegisterTimerTypeIntervalPtnCustomValid() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.CUSTOM;
    OffsetDateTime timer1startDate = null;
    OffsetDateTime timer1endDate = null;
    Integer timer1intPeriod = null;
    List<OffsetDateTime> timer1dateTimes = new ArrayList<>();
    timer1dateTimes.add(OffsetDateTime.now().plusDays(1));
    timer1dateTimes.add(OffsetDateTime.now().plusMonths(1));
    timer1dateTimes.add(OffsetDateTime.now().plusYears(1));

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CREATED;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was called
    verify(this.timerRepository, times(1)).add(arg1.capture());

    // Assertions
    assertNotNull(arg1.getValue(), "Argument (TimerInfo) must not be null.");
    UUID newTimerId = arg1.getValue().getTimerId();
    assertNotNull(newTimerId, "Argument's timerId must not be null.");
    assertEquals(timer1type, arg1.getValue().getTimerType(), "Timer1's timerType must match.");
    assertEquals(timer1intPtrn, arg1.getValue().getIntervalPattern(),
        "Timer1's intervalPattern must match.");
    assertEquals(timer1startDate, arg1.getValue().getStartDateTime(),
        "Timer1's startDateTime must match.");
    assertEquals(timer1endDate, arg1.getValue().getEndDateTime(),
        "Timer1's endDateTime must match.");
    assertEquals(timer1intPeriod, arg1.getValue().getIntervalPeriod(),
        "Timer1's intervalPeriod must match.");
    assertEquals(timer1dateTimes, arg1.getValue().getSetDateTimes(),
        "Timer1's setDateTimes must match.");
    assertEquals(timer1targets, arg1.getValue().getTimerTarget(),
        "Timer1's timerTarget must match.");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 201.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNotNull(operResp.getId(), "Returned timerId must not be null.");
    assertEquals(newTimerId, operResp.getId(),
        "Returned timerId must match the id passed to the repo.");
  }

  @Test
  @DisplayName("TEST registerTimer (type = INTERVAL, intervalPattern = timesPerDay (null start time))")
  void testRegisterTimerTypeIntervalPtnTimesPerDayNullStartTime() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.TIMES_PER_DAY;
    OffsetDateTime timer1startDate = null;
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 3;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned timerId must be null.");
  }

  @Test
  @DisplayName("TEST registerTimer (type = INTERVAL, intervalPattern = timesPerDay (null end time))")
  void testRegisterTimerTypeIntervalPtnTimesPerDayNullEndTime() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.TIMES_PER_DAY;
    OffsetDateTime timer1startDate = OffsetDateTime.now();
    OffsetDateTime timer1endDate = null;
    Integer timer1intPeriod = 3;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned timerId must be null.");
  }

  @Test
  @DisplayName("TEST registerTimer (type = INTERVAL, intervalPattern = timesPerDay (start time == end time))")
  void testRegisterTimerTypeIntervalPtnTimesPerDayStartVsEndEqual() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.TIMES_PER_DAY;
    OffsetDateTime timer1startDate = OffsetDateTime.now();
    OffsetDateTime timer1endDate = timer1startDate;
    Integer timer1intPeriod = 3;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned timerId must be null.");
  }

  @Test
  @DisplayName("TEST registerTimer (type = INTERVAL, intervalPattern = timesPerDay (start time > end time))")
  void testRegisterTimerTypeIntervalPtnTimesPerDayStartVsEndGreater() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.TIMES_PER_DAY;
    OffsetDateTime timer1startDate = OffsetDateTime.now().plusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now();
    Integer timer1intPeriod = 3;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned timerId must be null.");
  }

  @Test
  @DisplayName("TEST registerTimer (type = INTERVAL, intervalPattern = timesPerDay (interval > max allowed))")
  void testRegisterTimerTypeIntervalPtnTimesPerDayIntervalMaxAllowed() {
    // Max allowed for timesPerDay = 24 * 60
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.TIMES_PER_DAY;
    OffsetDateTime timer1startDate = OffsetDateTime.now();
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusDays(1);
    Integer timer1intPeriod = (24 * 60) + 1;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned timerId must be null.");
  }

  @Test
  @DisplayName("TEST registerTimer (type = INTERVAL, intervalPattern = daysPerWeek (interval > max allowed))")
  void testRegisterTimerTypeIntervalPtnDaysPerWeekIntervalMaxAllowed() {
    // Max allowed for daysPerWeek = 6
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAYS_PER_WEEK;
    OffsetDateTime timer1startDate = OffsetDateTime.now();
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusDays(1);
    Integer timer1intPeriod = 7;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned timerId must be null.");
  }

  @Test
  @DisplayName("TEST registerTimer (type = INTERVAL, intervalPattern = daysPerMonth (interval > max allowed))")
  void testRegisterTimerTypeIntervalPtnDaysPerMonthIntervalMaxAllowed() {
    // Max allowed for daysPerMonth = 29
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAYS_PER_MONTH;
    OffsetDateTime timer1startDate = OffsetDateTime.now();
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusDays(1);
    Integer timer1intPeriod = 30;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned timerId must be null.");
  }

  @Test
  @DisplayName("TEST registerTimer (type = INTERVAL, intervalPattern = daysPerYear (interval > max allowed))")
  void testRegisterTimerTypeIntervalPtnDaysPerYearIntervalMaxAllowed() {
    // Max allowed for daysPerYear = 364
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAYS_PER_YEAR;
    OffsetDateTime timer1startDate = OffsetDateTime.now();
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusDays(1);
    Integer timer1intPeriod = 365;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned timerId must be null.");
  }

  @Test
  @DisplayName("TEST registerTimer (type = INTERVAL, intervalPattern = timesPerDay (setDateTime contains something))")
  void testRegisterTimerTypeIntervalPtnTimesPerDayHasSetDateTime() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.TIMES_PER_DAY;
    OffsetDateTime timer1startDate = OffsetDateTime.now();
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusDays(1);
    Integer timer1intPeriod = (24 * 60);
    List<OffsetDateTime> timer1dateTimes = new ArrayList<>();
    timer1dateTimes.add(OffsetDateTime.now().plusDays(1));
    timer1dateTimes.add(OffsetDateTime.now().plusMonths(1));
    timer1dateTimes.add(OffsetDateTime.now().plusYears(1));

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned timerId must be null.");
  }

  @Test
  @DisplayName("TEST registerTimer (type = INTERVAL, intervalPattern = daily (interval > max allowed))")
  void testRegisterTimerTypeIntervalPtnDailyIntervalMaxPlus1() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 2;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned timerId must be null.");
  }

  @Test
  @DisplayName("TEST registerTimer (type = INTERVAL, intervalPattern = weekly (interval > max allowed))")
  void testRegisterTimerTypeIntervalPtnWeeklyIntervalMaxPlus1() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.WEEKLY;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 2;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned timerId must be null.");
  }

  @Test
  @DisplayName("TEST registerTimer (type = INTERVAL, intervalPattern = monthly (interval > max allowed))")
  void testRegisterTimerTypeIntervalPtnMonthlyIntervalMaxPlus1() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.MONTHLY;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 2;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned timerId must be null.");
  }

  @Test
  @DisplayName("TEST registerTimer (type = INTERVAL, intervalPattern = yearly (interval > max allowed))")
  void testRegisterTimerTypeIntervalPtnYearlyIntervalMaxPlus1() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.YEARLY;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 2;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned timerId must be null.");
  }

  @Test
  @DisplayName("TEST registerTimer (type = INTERVAL, intervalPattern = daily (null start time))")
  void testRegisterTimerTypeIntervalPtnDailyNullStartTime() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    OffsetDateTime timer1startDate = null;
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned timerId must be null.");
  }

  @Test
  @DisplayName("TEST registerTimer (type = INTERVAL, intervalPattern = daily (null end time))")
  void testRegisterTimerTypeIntervalPtnDailyNullEndTime() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = null;
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned timerId must be null.");
  }

  @Test
  @DisplayName("TEST registerTimer (type = INTERVAL, intervalPattern = daily (start time == end time))")
  void testRegisterTimerTypeIntervalPtnDailyStartVsEndEqual() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = timer1startDate;
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned timerId must be null.");
  }

  @Test
  @DisplayName("TEST registerTimer (type = INTERVAL, intervalPattern = daily (start time > end time))")
  void testRegisterTimerTypeIntervalPtnDailyStartVsEndGreater() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    OffsetDateTime timer1startDate = OffsetDateTime.now();
    OffsetDateTime timer1endDate = OffsetDateTime.now().minusDays(1);
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned timerId must be null.");
  }

  @Test
  @DisplayName("TEST registerTimer (type = INTERVAL, intervalPattern = daily (setDateTime contains something))")
  void testRegisterTimerTypeIntervalPtnDailyHasSetDateTime() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    OffsetDateTime timer1startDate = OffsetDateTime.now();
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusDays(1);
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = new ArrayList<>();
    timer1dateTimes.add(OffsetDateTime.now().plusDays(1));
    timer1dateTimes.add(OffsetDateTime.now().plusMonths(1));
    timer1dateTimes.add(OffsetDateTime.now().plusYears(1));

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned timerId must be null.");
  }

  @Test
  @DisplayName("TEST registerTimer (type = INTERVAL, intervalPattern = custom (intervalPeriod != null))")
  void testRegisterTimerTypeIntervalPtnCustomHasIntervalPeriod() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.CUSTOM;
    OffsetDateTime timer1startDate = null;
    OffsetDateTime timer1endDate = null;
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = new ArrayList<>();
    timer1dateTimes.add(OffsetDateTime.now().plusDays(1));
    timer1dateTimes.add(OffsetDateTime.now().plusMonths(1));
    timer1dateTimes.add(OffsetDateTime.now().plusYears(1));

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned timerId must be null.");
  }

  @Test
  @DisplayName("TEST registerTimer (type = INTERVAL, intervalPattern = custom (start time != null))")
  void testRegisterTimerTypeIntervalPtnCustomHasStartTime() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.CUSTOM;
    OffsetDateTime timer1startDate = OffsetDateTime.now();
    OffsetDateTime timer1endDate = null;
    Integer timer1intPeriod = null;
    List<OffsetDateTime> timer1dateTimes = new ArrayList<>();
    timer1dateTimes.add(OffsetDateTime.now().plusDays(1));
    timer1dateTimes.add(OffsetDateTime.now().plusMonths(1));
    timer1dateTimes.add(OffsetDateTime.now().plusYears(1));

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned timerId must be null.");
  }

  @Test
  @DisplayName("TEST registerTimer (type = INTERVAL, intervalPattern = custom (end time != null))")
  void testRegisterTimerTypeIntervalPtnCustomHasEndTime() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.CUSTOM;
    OffsetDateTime timer1startDate = null;
    OffsetDateTime timer1endDate = OffsetDateTime.now();
    Integer timer1intPeriod = null;
    List<OffsetDateTime> timer1dateTimes = new ArrayList<>();
    timer1dateTimes.add(OffsetDateTime.now().plusDays(1));
    timer1dateTimes.add(OffsetDateTime.now().plusMonths(1));
    timer1dateTimes.add(OffsetDateTime.now().plusYears(1));

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned timerId must be null.");
  }

  @Test
  @DisplayName("TEST registerTimer (type = INTERVAL, intervalPattern = custom (null setDateTimes))")
  void testRegisterTimerTypeIntervalPtnCustomNullSetDateTime() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.CUSTOM;
    OffsetDateTime timer1startDate = null;
    OffsetDateTime timer1endDate = null;
    Integer timer1intPeriod = null;
    List<OffsetDateTime> timer1dateTimes = null;

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned timerId must be null.");
  }

  @Test
  @DisplayName("TEST registerTimer (type = INTERVAL, intervalPattern = custom (empty setDateTimes))")
  void testRegisterTimerTypeIntervalPtnCustomEmptySetDateTime() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.CUSTOM;
    OffsetDateTime timer1startDate = null;
    OffsetDateTime timer1endDate = null;
    Integer timer1intPeriod = null;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned timerId must be null.");
  }

  @Test
  @DisplayName("TEST registerTimer (type = INTERVAL, intervalPattern = custom (setDateTimes contains null))")
  void testRegisterTimerTypeIntervalPtnCustomSetDateTimeContainsNull() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.CUSTOM;
    OffsetDateTime timer1startDate = null;
    OffsetDateTime timer1endDate = null;
    Integer timer1intPeriod = null;
    List<OffsetDateTime> timer1dateTimes = new ArrayList<>();
    timer1dateTimes.add(OffsetDateTime.now().plusDays(1));
    timer1dateTimes.add(null);
    timer1dateTimes.add(OffsetDateTime.now().plusMonths(1));
    timer1dateTimes.add(OffsetDateTime.now().plusYears(1));

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned timerId must be null.");
  }

  @Test
  @DisplayName("TEST registerTimer (type = INTERVAL (interval period = -1))")
  void testRegisterTimerTypeIntervalIntervalPeriodNeg1() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.TIMES_PER_DAY;
    OffsetDateTime timer1startDate = OffsetDateTime.now();
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = -1;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned timerId must be null.");
  }

  @Test
  @DisplayName("TEST registerTimer (type = INTERVAL (interval period = 0))")
  void testRegisterTimerTypeIntervalIntervalPeriod0() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.TIMES_PER_DAY;
    OffsetDateTime timer1startDate = OffsetDateTime.now();
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 0;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned timerId must be null.");
  }

  @Test
  @DisplayName("TEST registerTimer (type = INTERVAL (not custom) (null interval period))")
  void testRegisterTimerTypeIntervalIntervalNull() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.TIMES_PER_DAY;
    OffsetDateTime timer1startDate = OffsetDateTime.now();
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = null;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned timerId must be null.");
  }

  @Test
  @DisplayName("TEST registerTimer (type = INTERVAL, intervalPattern = null)")
  void testRegisterTimerTypeIntervalIntervalPtnNull() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = null;
    OffsetDateTime timer1startDate = OffsetDateTime.now();
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 3;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned timerId must be null.");
  }

  @Test
  @DisplayName("TEST registerTimer (type = null)")
  void testRegisterTimerTypeNull() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = null;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.TIMES_PER_DAY;
    OffsetDateTime timer1startDate = OffsetDateTime.now();
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 3;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned timerId must be null.");
  }

  @Test
  @DisplayName("TEST registerTimer (timerTarget is null)")
  void testRegisterTimerTimerTargetNull() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    List<TimerInfoTimerTarget> timer1targets = null;

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned timerId must be null.");
  }

  @Test
  @DisplayName("TEST registerTimer (timerTarget is empty)")
  void testRegisterTimerTimerTargetEmpty() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    List<TimerInfoTimerTarget> timer1targets = Collections.emptyList();

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned timerId must be null.");
  }

  @Test
  @DisplayName("TEST registerTimer (timerTarget contains null)")
  void testRegisterTimerTimerTargetContainsNull() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(null);
    timer1targets.add(timer1target1);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned timerId must be null.");
  }

  @Test
  @DisplayName("TEST registerTimer (timerTarget contains null action)")
  void testRegisterTimerTimerTargetContainsNullAction() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    UUID controlId2 = UUID.fromString("22888888-2222-2222-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action1 = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    CommonTimerAction timer1action2 = null;
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action1) //
        .directValue(timer1dirVal);
    TimerInfoTimerTarget timer1target2 = new TimerInfoTimerTarget() //
        .controlId(controlId2) //
        .action(timer1action2) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);
    timer1targets.add(timer1target2);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned timerId must be null.");
  }

  @Test
  @DisplayName("TEST registerTimer (timerTarget contains null direct value)")
  void testRegisterTimerTimerTargetContainsNullDirectValue() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    UUID controlId2 = UUID.fromString("22888888-2222-2222-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action1 = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    CommonTimerAction timer1action2 = CommonTimerAction.DIRECT;
    String timer2dirVal = null;
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action1) //
        .directValue(timer1dirVal);
    TimerInfoTimerTarget timer1target2 = new TimerInfoTimerTarget() //
        .controlId(controlId2) //
        .action(timer1action2) //
        .directValue(timer2dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);
    timer1targets.add(timer1target2);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned timerId must be null.");
  }

  @Test
  @DisplayName("TEST registerTimer (timerTarget contains blank direct value)")
  void testRegisterTimerTimerTargetContainsBlankDirectValue() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    UUID controlId2 = UUID.fromString("22888888-2222-2222-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action1 = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    CommonTimerAction timer1action2 = CommonTimerAction.DIRECT;
    String timer2dirVal = "";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action1) //
        .directValue(timer1dirVal);
    TimerInfoTimerTarget timer1target2 = new TimerInfoTimerTarget() //
        .controlId(controlId2) //
        .action(timer1action2) //
        .directValue(timer2dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);
    timer1targets.add(timer1target2);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned timerId must be null.");
  }

  @Test
  @DisplayName("TEST registerTimer (timerTarget contains (non-direct) non-null direct value)")
  void testRegisterTimerTimerTargetContainsNonDirectDirectValue() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    UUID controlId2 = UUID.fromString("22888888-2222-2222-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action1 = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    CommonTimerAction timer1action2 = CommonTimerAction.SLIDER_DOWN;
    String timer2dirVal = "5";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action1) //
        .directValue(timer1dirVal);
    TimerInfoTimerTarget timer1target2 = new TimerInfoTimerTarget() //
        .controlId(controlId2) //
        .action(timer1action2) //
        .directValue(timer2dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);
    timer1targets.add(timer1target2);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned timerId must be null.");
  }

  @Test
  @DisplayName("TEST registerTimer (timerTarget contains duplicate controlId)")
  void testRegisterTimerTimerTargetContainsDuplicateControlId() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action1 = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    CommonTimerAction timer1action2 = CommonTimerAction.SLIDER_DOWN;
    String timer2dirVal = null;
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action1) //
        .directValue(timer1dirVal);
    TimerInfoTimerTarget timer1target2 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action2) //
        .directValue(timer2dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);
    timer1targets.add(timer1target2);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned timerId must be null.");
  }

  @Test
  @DisplayName("TEST registerTimer (some controlId in the timerTarget do not exist(null))")
  void testRegisterTimerTimerTargetContainsNonExistentControlIdNull() {
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    UUID controlId2 = UUID.fromString("22888888-2222-2222-2222-111111111101");

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action1 = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    CommonTimerAction timer1action2 = CommonTimerAction.SLIDER_DOWN;
    String timer2dirVal = null;
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action1) //
        .directValue(timer1dirVal);
    TimerInfoTimerTarget timer1target2 = new TimerInfoTimerTarget() //
        .controlId(controlId2) //
        .action(timer1action2) //
        .directValue(timer2dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);
    timer1targets.add(timer1target2);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    inputContrSet.add(controlId2);
    List<DeviceControlInfo> contrList = null;

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot add timer";
    String expectedDetail = "DB did not return a valid response for the controlId multi-query.";

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).add(arg1.capture());

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
    assertNull(operResp.getId(), "There should be no timerId returned.");
  }

  @Test
  @DisplayName("TEST registerTimer (some controlId in the timerTarget do not exist(empty))")
  void testRegisterTimerTimerTargetContainsNonExistentControlIdEmpty() {
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    UUID controlId2 = UUID.fromString("22888888-2222-2222-2222-111111111101");

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action1 = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    CommonTimerAction timer1action2 = CommonTimerAction.SLIDER_DOWN;
    String timer2dirVal = null;
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action1) //
        .directValue(timer1dirVal);
    TimerInfoTimerTarget timer1target2 = new TimerInfoTimerTarget() //
        .controlId(controlId2) //
        .action(timer1action2) //
        .directValue(timer2dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);
    timer1targets.add(timer1target2);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    inputContrSet.add(controlId2);
    List<DeviceControlInfo> contrList = Collections.emptyList();

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot add timer";
    String expectedDetail = "DB did not return a valid response for the controlId multi-query.";

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).add(arg1.capture());

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
    assertNull(operResp.getId(), "There should be no timerId returned.");
  }

  @Test
  @DisplayName("TEST registerTimer (some controlId in the timerTarget do not exist)")
  void testRegisterTimerTimerTargetContainsNonExistentControlId() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    UUID controlId2 = UUID.fromString("22888888-2222-2222-2222-111111111102");
    UUID controlId3 = UUID.fromString("22888888-3333-3333-3333-111111111103");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    String contr2name = "Name 2";
    String contr2descr = "Descr 2";
    String contr2val = null;
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);
    DeviceControlInfo contr2 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId2) //
        .name(contr2name) //
        .description(contr2descr) //
        .currentValue(contr2val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    CommonTimerAction timer2action = CommonTimerAction.SLIDER_UP;
    String timer2dirVal = null;
    CommonTimerAction timer3action = CommonTimerAction.SLIDER_DOWN;
    String timer3dirVal = "";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    TimerInfoTimerTarget timer1target2 = new TimerInfoTimerTarget() //
        .controlId(controlId2) //
        .action(timer2action) //
        .directValue(timer2dirVal);
    TimerInfoTimerTarget timer1target3 = new TimerInfoTimerTarget() //
        .controlId(controlId3) //
        .action(timer3action) //
        .directValue(timer3dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);
    timer1targets.add(timer1target2);
    timer1targets.add(timer1target3);

    TimerRegistration timerAdd = new TimerRegistration();
    timerAdd.setTimerType(timer1type);
    timerAdd.setIntervalPattern(timer1intPtrn);
    timerAdd.setStartDateTime(timer1startDate);
    timerAdd.setEndDateTime(timer1endDate);
    timerAdd.setIntervalPeriod(timer1intPeriod);
    timerAdd.setSetDateTimes(timer1dateTimes);
    timerAdd.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    inputContrSet.add(controlId2);
    inputContrSet.add(controlId3);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);
    contrList.add(contr2);
    contrList.add(null);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot add timer";
    String expectedDetail = "Some controlIds provided in the action parameter do not exist.";

    // Mock(s)
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<TimerInfo> arg1 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonAddResponse operResp = this.timerService.registerTimer(timerAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).add(arg1.capture());

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
    assertNull(operResp.getId(), "There should be no timerId returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (type = ONCE (valid))")
  void testModifyTimerTypeOnceValid() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.ONCE;
    CommonTimerIntervalPattern timer1intPtrn = null;
    OffsetDateTime timer1startDate = OffsetDateTime.now();
    OffsetDateTime timer1endDate = timer1startDate;
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = null;

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was called
    verify(this.timerRepository, times(1)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertEquals(timerId, arg1.getValue(), "Argument must match.");
    assertNotNull(arg2.getValue(), "Argument (TimerInfo) must not be null.");
    assertEquals(timer1type, arg2.getValue().getTimerType(), "Timer1's timerType must match.");
    assertEquals(timer1intPtrn, arg2.getValue().getIntervalPattern(),
        "Timer1's intervalPattern must match.");
    assertEquals(timer1startDate, arg2.getValue().getStartDateTime(),
        "Timer1's startDateTime must match.");
    assertEquals(timer1endDate, arg2.getValue().getEndDateTime(),
        "Timer1's endDateTime must match.");
    assertEquals(timer1intPeriod, arg2.getValue().getIntervalPeriod(),
        "Timer1's intervalPeriod must match.");
    assertEquals(timer1dateTimes, arg2.getValue().getSetDateTimes(),
        "Timer1's setDateTimes must match.");
    assertEquals(timer1targets, arg2.getValue().getTimerTarget(),
        "Timer1's timerTarget must match.");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (type = ONCE (null start time))")
  void testModifyTimerTypeOnceNullStartTime() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.ONCE;
    CommonTimerIntervalPattern timer1intPtrn = null;
    OffsetDateTime timer1startDate = null;
    OffsetDateTime timer1endDate = OffsetDateTime.now();
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = null;

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (type = ONCE (null end time))")
  void testModifyTimerTypeOnceNullEndTime() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.ONCE;
    CommonTimerIntervalPattern timer1intPtrn = null;
    OffsetDateTime timer1startDate = OffsetDateTime.now();
    OffsetDateTime timer1endDate = null;
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = null;

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (type = ONCE (start time != end time))")
  void testModifyTimerTypeOnceStartVsEndNotEqual() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.ONCE;
    CommonTimerIntervalPattern timer1intPtrn = null;
    OffsetDateTime timer1startDate = OffsetDateTime.now();
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusDays(3);
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = null;

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (type = ONCE (interval period = -1))")
  void testModifyTimerTypeOnceIntervalPeriodNeg1() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.ONCE;
    CommonTimerIntervalPattern timer1intPtrn = null;
    OffsetDateTime timer1startDate = OffsetDateTime.now();
    OffsetDateTime timer1endDate = timer1startDate;
    Integer timer1intPeriod = -1;
    List<OffsetDateTime> timer1dateTimes = null;

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (type = ONCE (interval period = 0))")
  void testModifyTimerTypeOnceIntervalPeriod0() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.ONCE;
    CommonTimerIntervalPattern timer1intPtrn = null;
    OffsetDateTime timer1startDate = OffsetDateTime.now();
    OffsetDateTime timer1endDate = timer1startDate;
    Integer timer1intPeriod = 0;
    List<OffsetDateTime> timer1dateTimes = null;

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (type = ONCE (interval period > 1))")
  void testModifyTimerTypeOnceIntervalPeriodMoreThan1() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.ONCE;
    CommonTimerIntervalPattern timer1intPtrn = null;
    OffsetDateTime timer1startDate = OffsetDateTime.now();
    OffsetDateTime timer1endDate = timer1startDate;
    Integer timer1intPeriod = 2;
    List<OffsetDateTime> timer1dateTimes = null;

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (type = ONCE (interval pattern provided))")
  void testModifyTimerTypeOnceIntervalPatternHave() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.ONCE;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    OffsetDateTime timer1startDate = OffsetDateTime.now();
    OffsetDateTime timer1endDate = timer1startDate;
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = null;

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (type = ONCE (null interval period))")
  void testModifyTimerTypeOnceNullIntervalPeriod() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.ONCE;
    CommonTimerIntervalPattern timer1intPtrn = null;
    OffsetDateTime timer1startDate = OffsetDateTime.now();
    OffsetDateTime timer1endDate = timer1startDate;
    Integer timer1intPeriod = null;
    List<OffsetDateTime> timer1dateTimes = null;

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (type = INTERVAL, intervalPattern = timesPerDay (valid))")
  void testModifyTimerTypeIntervalPtnTimesPerDayValid() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    UUID controlId2 = UUID.fromString("22888888-2222-2222-2222-111111111102");
    UUID controlId3 = UUID.fromString("22888888-3333-3333-3333-111111111103");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    String contr2name = "Name 2";
    String contr2descr = "Descr 2";
    String contr2val = null;
    String contr3name = "Name 3";
    String contr3descr = "Descr 3";
    String contr3val = null;
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);
    DeviceControlInfo contr2 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId2) //
        .name(contr2name) //
        .description(contr2descr) //
        .currentValue(contr2val);
    DeviceControlInfo contr3 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId3) //
        .name(contr3name) //
        .description(contr3descr) //
        .currentValue(contr3val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.TIMES_PER_DAY;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = (24 * 60);
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    CommonTimerAction timer2action = CommonTimerAction.SLIDER_UP;
    String timer2dirVal = null;
    CommonTimerAction timer3action = CommonTimerAction.SLIDER_DOWN;
    String timer3dirVal = "";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    TimerInfoTimerTarget timer1target2 = new TimerInfoTimerTarget() //
        .controlId(controlId2) //
        .action(timer2action) //
        .directValue(timer2dirVal);
    TimerInfoTimerTarget timer1target3 = new TimerInfoTimerTarget() //
        .controlId(controlId3) //
        .action(timer3action) //
        .directValue(timer3dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);
    timer1targets.add(timer1target2);
    timer1targets.add(timer1target3);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    inputContrSet.add(controlId2);
    inputContrSet.add(controlId3);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);
    contrList.add(contr2);
    contrList.add(contr3);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was called
    verify(this.timerRepository, times(1)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertEquals(timerId, arg1.getValue(), "Argument must match.");
    assertNotNull(arg2.getValue(), "Argument (TimerInfo) must not be null.");
    assertEquals(timer1type, arg2.getValue().getTimerType(), "Timer1's timerType must match.");
    assertEquals(timer1intPtrn, arg2.getValue().getIntervalPattern(),
        "Timer1's intervalPattern must match.");
    assertEquals(timer1startDate, arg2.getValue().getStartDateTime(),
        "Timer1's startDateTime must match.");
    assertEquals(timer1endDate, arg2.getValue().getEndDateTime(),
        "Timer1's endDateTime must match.");
    assertEquals(timer1intPeriod, arg2.getValue().getIntervalPeriod(),
        "Timer1's intervalPeriod must match.");
    assertEquals(timer1dateTimes, arg2.getValue().getSetDateTimes(),
        "Timer1's setDateTimes must match.");
    assertEquals(timer1targets, arg2.getValue().getTimerTarget(),
        "Timer1's timerTarget must match.");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (type = INTERVAL, intervalPattern = daysPerWeek (valid))")
  void testModifyTimerTypeIntervalPtnDaysPerWeekValid() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAYS_PER_WEEK;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 6;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was called
    verify(this.timerRepository, times(1)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertEquals(timerId, arg1.getValue(), "Argument must match.");
    assertNotNull(arg2.getValue(), "Argument (TimerInfo) must not be null.");
    assertEquals(timer1type, arg2.getValue().getTimerType(), "Timer1's timerType must match.");
    assertEquals(timer1intPtrn, arg2.getValue().getIntervalPattern(),
        "Timer1's intervalPattern must match.");
    assertEquals(timer1startDate, arg2.getValue().getStartDateTime(),
        "Timer1's startDateTime must match.");
    assertEquals(timer1endDate, arg2.getValue().getEndDateTime(),
        "Timer1's endDateTime must match.");
    assertEquals(timer1intPeriod, arg2.getValue().getIntervalPeriod(),
        "Timer1's intervalPeriod must match.");
    assertEquals(timer1dateTimes, arg2.getValue().getSetDateTimes(),
        "Timer1's setDateTimes must match.");
    assertEquals(timer1targets, arg2.getValue().getTimerTarget(),
        "Timer1's timerTarget must match.");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (type = INTERVAL, intervalPattern = daysPerMonth (valid))")
  void testModifyTimerTypeIntervalPtnDaysPerMonthValid() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAYS_PER_MONTH;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 29;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was called
    verify(this.timerRepository, times(1)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertEquals(timerId, arg1.getValue(), "Argument must match.");
    assertNotNull(arg2.getValue(), "Argument (TimerInfo) must not be null.");
    assertEquals(timer1type, arg2.getValue().getTimerType(), "Timer1's timerType must match.");
    assertEquals(timer1intPtrn, arg2.getValue().getIntervalPattern(),
        "Timer1's intervalPattern must match.");
    assertEquals(timer1startDate, arg2.getValue().getStartDateTime(),
        "Timer1's startDateTime must match.");
    assertEquals(timer1endDate, arg2.getValue().getEndDateTime(),
        "Timer1's endDateTime must match.");
    assertEquals(timer1intPeriod, arg2.getValue().getIntervalPeriod(),
        "Timer1's intervalPeriod must match.");
    assertEquals(timer1dateTimes, arg2.getValue().getSetDateTimes(),
        "Timer1's setDateTimes must match.");
    assertEquals(timer1targets, arg2.getValue().getTimerTarget(),
        "Timer1's timerTarget must match.");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (type = INTERVAL, intervalPattern = daysPerYear (valid))")
  void testModifyTimerTypeIntervalPtnDaysPerYearValid() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAYS_PER_YEAR;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 364;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was called
    verify(this.timerRepository, times(1)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertEquals(timerId, arg1.getValue(), "Argument must match.");
    assertNotNull(arg2.getValue(), "Argument (TimerInfo) must not be null.");
    assertEquals(timer1type, arg2.getValue().getTimerType(), "Timer1's timerType must match.");
    assertEquals(timer1intPtrn, arg2.getValue().getIntervalPattern(),
        "Timer1's intervalPattern must match.");
    assertEquals(timer1startDate, arg2.getValue().getStartDateTime(),
        "Timer1's startDateTime must match.");
    assertEquals(timer1endDate, arg2.getValue().getEndDateTime(),
        "Timer1's endDateTime must match.");
    assertEquals(timer1intPeriod, arg2.getValue().getIntervalPeriod(),
        "Timer1's intervalPeriod must match.");
    assertEquals(timer1dateTimes, arg2.getValue().getSetDateTimes(),
        "Timer1's setDateTimes must match.");
    assertEquals(timer1targets, arg2.getValue().getTimerTarget(),
        "Timer1's timerTarget must match.");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (type = INTERVAL, intervalPattern = daily (valid))")
  void testModifyTimerTypeIntervalPtnDailyValid() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was called
    verify(this.timerRepository, times(1)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertEquals(timerId, arg1.getValue(), "Argument must match.");
    assertNotNull(arg2.getValue(), "Argument (TimerInfo) must not be null.");
    assertEquals(timer1type, arg2.getValue().getTimerType(), "Timer1's timerType must match.");
    assertEquals(timer1intPtrn, arg2.getValue().getIntervalPattern(),
        "Timer1's intervalPattern must match.");
    assertEquals(timer1startDate, arg2.getValue().getStartDateTime(),
        "Timer1's startDateTime must match.");
    assertEquals(timer1endDate, arg2.getValue().getEndDateTime(),
        "Timer1's endDateTime must match.");
    assertEquals(timer1intPeriod, arg2.getValue().getIntervalPeriod(),
        "Timer1's intervalPeriod must match.");
    assertEquals(timer1dateTimes, arg2.getValue().getSetDateTimes(),
        "Timer1's setDateTimes must match.");
    assertEquals(timer1targets, arg2.getValue().getTimerTarget(),
        "Timer1's timerTarget must match.");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (type = INTERVAL, intervalPattern = weekly (valid))")
  void testModifyTimerTypeIntervalPtnWeeklyValid() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.WEEKLY;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was called
    verify(this.timerRepository, times(1)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertEquals(timerId, arg1.getValue(), "Argument must match.");
    assertNotNull(arg2.getValue(), "Argument (TimerInfo) must not be null.");
    assertEquals(timer1type, arg2.getValue().getTimerType(), "Timer1's timerType must match.");
    assertEquals(timer1intPtrn, arg2.getValue().getIntervalPattern(),
        "Timer1's intervalPattern must match.");
    assertEquals(timer1startDate, arg2.getValue().getStartDateTime(),
        "Timer1's startDateTime must match.");
    assertEquals(timer1endDate, arg2.getValue().getEndDateTime(),
        "Timer1's endDateTime must match.");
    assertEquals(timer1intPeriod, arg2.getValue().getIntervalPeriod(),
        "Timer1's intervalPeriod must match.");
    assertEquals(timer1dateTimes, arg2.getValue().getSetDateTimes(),
        "Timer1's setDateTimes must match.");
    assertEquals(timer1targets, arg2.getValue().getTimerTarget(),
        "Timer1's timerTarget must match.");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (type = INTERVAL, intervalPattern = monthly (valid))")
  void testModifyTimerTypeIntervalPtnMonthlyValid() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.MONTHLY;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was called
    verify(this.timerRepository, times(1)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertEquals(timerId, arg1.getValue(), "Argument must match.");
    assertNotNull(arg2.getValue(), "Argument (TimerInfo) must not be null.");
    assertEquals(timer1type, arg2.getValue().getTimerType(), "Timer1's timerType must match.");
    assertEquals(timer1intPtrn, arg2.getValue().getIntervalPattern(),
        "Timer1's intervalPattern must match.");
    assertEquals(timer1startDate, arg2.getValue().getStartDateTime(),
        "Timer1's startDateTime must match.");
    assertEquals(timer1endDate, arg2.getValue().getEndDateTime(),
        "Timer1's endDateTime must match.");
    assertEquals(timer1intPeriod, arg2.getValue().getIntervalPeriod(),
        "Timer1's intervalPeriod must match.");
    assertEquals(timer1dateTimes, arg2.getValue().getSetDateTimes(),
        "Timer1's setDateTimes must match.");
    assertEquals(timer1targets, arg2.getValue().getTimerTarget(),
        "Timer1's timerTarget must match.");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (type = INTERVAL, intervalPattern = yearly (valid))")
  void testModifyTimerTypeIntervalPtnYearlyValid() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.YEARLY;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was called
    verify(this.timerRepository, times(1)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertEquals(timerId, arg1.getValue(), "Argument must match.");
    assertNotNull(arg2.getValue(), "Argument (TimerInfo) must not be null.");
    assertEquals(timer1type, arg2.getValue().getTimerType(), "Timer1's timerType must match.");
    assertEquals(timer1intPtrn, arg2.getValue().getIntervalPattern(),
        "Timer1's intervalPattern must match.");
    assertEquals(timer1startDate, arg2.getValue().getStartDateTime(),
        "Timer1's startDateTime must match.");
    assertEquals(timer1endDate, arg2.getValue().getEndDateTime(),
        "Timer1's endDateTime must match.");
    assertEquals(timer1intPeriod, arg2.getValue().getIntervalPeriod(),
        "Timer1's intervalPeriod must match.");
    assertEquals(timer1dateTimes, arg2.getValue().getSetDateTimes(),
        "Timer1's setDateTimes must match.");
    assertEquals(timer1targets, arg2.getValue().getTimerTarget(),
        "Timer1's timerTarget must match.");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (type = INTERVAL, intervalPattern = custom (valid))")
  void testModifyTimerTypeIntervalPtnCustomValid() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.CUSTOM;
    OffsetDateTime timer1startDate = null;
    OffsetDateTime timer1endDate = null;
    Integer timer1intPeriod = null;
    List<OffsetDateTime> timer1dateTimes = new ArrayList<>();
    timer1dateTimes.add(OffsetDateTime.now().plusDays(1));
    timer1dateTimes.add(OffsetDateTime.now().plusMonths(1));
    timer1dateTimes.add(OffsetDateTime.now().plusYears(1));

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was called
    verify(this.timerRepository, times(1)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertEquals(timerId, arg1.getValue(), "Argument must match.");
    assertNotNull(arg2.getValue(), "Argument (TimerInfo) must not be null.");
    assertEquals(timer1type, arg2.getValue().getTimerType(), "Timer1's timerType must match.");
    assertEquals(timer1intPtrn, arg2.getValue().getIntervalPattern(),
        "Timer1's intervalPattern must match.");
    assertEquals(timer1startDate, arg2.getValue().getStartDateTime(),
        "Timer1's startDateTime must match.");
    assertEquals(timer1endDate, arg2.getValue().getEndDateTime(),
        "Timer1's endDateTime must match.");
    assertEquals(timer1intPeriod, arg2.getValue().getIntervalPeriod(),
        "Timer1's intervalPeriod must match.");
    assertEquals(timer1dateTimes, arg2.getValue().getSetDateTimes(),
        "Timer1's setDateTimes must match.");
    assertEquals(timer1targets, arg2.getValue().getTimerTarget(),
        "Timer1's timerTarget must match.");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (type = INTERVAL, intervalPattern = timesPerDay (null start time))")
  void testModifyTimerTypeIntervalPtnTimesPerDayNullStartTime() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.TIMES_PER_DAY;
    OffsetDateTime timer1startDate = null;
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 3;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (type = INTERVAL, intervalPattern = timesPerDay (null end time))")
  void testModifyTimerTypeIntervalPtnTimesPerDayNullEndTime() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.TIMES_PER_DAY;
    OffsetDateTime timer1startDate = OffsetDateTime.now();
    OffsetDateTime timer1endDate = null;
    Integer timer1intPeriod = 3;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (type = INTERVAL, intervalPattern = timesPerDay (start time == end time))")
  void testModifyTimerTypeIntervalPtnTimesPerDayStartVsEndEqual() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.TIMES_PER_DAY;
    OffsetDateTime timer1startDate = OffsetDateTime.now();
    OffsetDateTime timer1endDate = timer1startDate;
    Integer timer1intPeriod = 3;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (type = INTERVAL, intervalPattern = timesPerDay (start time > end time))")
  void testModifyTimerTypeIntervalPtnTimesPerDayStartVsEndGreater() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.TIMES_PER_DAY;
    OffsetDateTime timer1startDate = OffsetDateTime.now().plusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now();
    Integer timer1intPeriod = 3;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (type = INTERVAL, intervalPattern = timesPerDay (interval > max allowed))")
  void testModifyTimerTypeIntervalPtnTimesPerDayIntervalMaxAllowed() {
    // Max allowed for timesPerDay = 24 * 60
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.TIMES_PER_DAY;
    OffsetDateTime timer1startDate = OffsetDateTime.now();
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusDays(1);
    Integer timer1intPeriod = (24 * 60) + 1;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (type = INTERVAL, intervalPattern = daysPerWeek (interval > max allowed))")
  void testModifyTimerTypeIntervalPtnDaysPerWeekIntervalMaxAllowed() {
    // Max allowed for daysPerWeek = 6
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAYS_PER_WEEK;
    OffsetDateTime timer1startDate = OffsetDateTime.now();
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusDays(1);
    Integer timer1intPeriod = 7;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (type = INTERVAL, intervalPattern = daysPerMonth (interval > max allowed))")
  void testModifyTimerTypeIntervalPtnDaysPerMonthIntervalMaxAllowed() {
    // Max allowed for daysPerMonth = 29
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAYS_PER_MONTH;
    OffsetDateTime timer1startDate = OffsetDateTime.now();
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusDays(1);
    Integer timer1intPeriod = 30;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (type = INTERVAL, intervalPattern = daysPerYear (interval > max allowed))")
  void testModifyTimerTypeIntervalPtnDaysPerYearIntervalMaxAllowed() {
    // Max allowed for daysPerYear = 364
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAYS_PER_YEAR;
    OffsetDateTime timer1startDate = OffsetDateTime.now();
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusDays(1);
    Integer timer1intPeriod = 365;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (type = INTERVAL, intervalPattern = timesPerDay (setDateTime contains something))")
  void testModifyTimerTypeIntervalPtnTimesPerDayHasSetDateTime() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.TIMES_PER_DAY;
    OffsetDateTime timer1startDate = OffsetDateTime.now();
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusDays(1);
    Integer timer1intPeriod = (24 * 60);
    List<OffsetDateTime> timer1dateTimes = new ArrayList<>();
    timer1dateTimes.add(OffsetDateTime.now().plusDays(1));
    timer1dateTimes.add(OffsetDateTime.now().plusMonths(1));
    timer1dateTimes.add(OffsetDateTime.now().plusYears(1));

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (type = INTERVAL, intervalPattern = daily (interval > max allowed))")
  void testModifyTimerTypeIntervalPtnDailyIntervalMaxPlus1() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 2;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (type = INTERVAL, intervalPattern = weekly (interval > max allowed))")
  void testModifyTimerTypeIntervalPtnWeeklyIntervalMaxPlus1() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.WEEKLY;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 2;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (type = INTERVAL, intervalPattern = monthly (interval > max allowed))")
  void testModifyTimerTypeIntervalPtnMonthlyIntervalMaxPlus1() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.MONTHLY;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 2;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (type = INTERVAL, intervalPattern = yearly (interval > max allowed))")
  void testModifyTimerTypeIntervalPtnYearlyIntervalMaxPlus1() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.YEARLY;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 2;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (type = INTERVAL, intervalPattern = daily (null start time))")
  void testModifyTimerTypeIntervalPtnDailyNullStartTime() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    OffsetDateTime timer1startDate = null;
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (type = INTERVAL, intervalPattern = daily (null end time))")
  void testModifyTimerTypeIntervalPtnDailyNullEndTime() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = null;
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (type = INTERVAL, intervalPattern = daily (start time == end time))")
  void testModifyTimerTypeIntervalPtnDailyStartVsEndEqual() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = timer1startDate;
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (type = INTERVAL, intervalPattern = daily (start time > end time))")
  void testModifyTimerTypeIntervalPtnDailyStartVsEndGreater() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    OffsetDateTime timer1startDate = OffsetDateTime.now();
    OffsetDateTime timer1endDate = OffsetDateTime.now().minusDays(1);
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (type = INTERVAL, intervalPattern = daily (setDateTime contains something))")
  void testModifyTimerTypeIntervalPtnDailyHasSetDateTime() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    OffsetDateTime timer1startDate = OffsetDateTime.now();
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusDays(1);
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = new ArrayList<>();
    timer1dateTimes.add(OffsetDateTime.now().plusDays(1));
    timer1dateTimes.add(OffsetDateTime.now().plusMonths(1));
    timer1dateTimes.add(OffsetDateTime.now().plusYears(1));

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (type = INTERVAL, intervalPattern = custom (intervalPeriod != null))")
  void testModifyTimerTypeIntervalPtnCustomHasIntervalPeriod() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.CUSTOM;
    OffsetDateTime timer1startDate = null;
    OffsetDateTime timer1endDate = null;
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = new ArrayList<>();
    timer1dateTimes.add(OffsetDateTime.now().plusDays(1));
    timer1dateTimes.add(OffsetDateTime.now().plusMonths(1));
    timer1dateTimes.add(OffsetDateTime.now().plusYears(1));

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (type = INTERVAL, intervalPattern = custom (start time != null))")
  void testModifyTimerTypeIntervalPtnCustomHasStartTime() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.CUSTOM;
    OffsetDateTime timer1startDate = OffsetDateTime.now();
    OffsetDateTime timer1endDate = null;
    Integer timer1intPeriod = null;
    List<OffsetDateTime> timer1dateTimes = new ArrayList<>();
    timer1dateTimes.add(OffsetDateTime.now().plusDays(1));
    timer1dateTimes.add(OffsetDateTime.now().plusMonths(1));
    timer1dateTimes.add(OffsetDateTime.now().plusYears(1));

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (type = INTERVAL, intervalPattern = custom (end time != null))")
  void testModifyTimerTypeIntervalPtnCustomHasEndTime() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.CUSTOM;
    OffsetDateTime timer1startDate = null;
    OffsetDateTime timer1endDate = OffsetDateTime.now();
    Integer timer1intPeriod = null;
    List<OffsetDateTime> timer1dateTimes = new ArrayList<>();
    timer1dateTimes.add(OffsetDateTime.now().plusDays(1));
    timer1dateTimes.add(OffsetDateTime.now().plusMonths(1));
    timer1dateTimes.add(OffsetDateTime.now().plusYears(1));

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (type = INTERVAL, intervalPattern = custom (null setDateTimes))")
  void testModifyTimerTypeIntervalPtnCustomNullSetDateTime() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.CUSTOM;
    OffsetDateTime timer1startDate = null;
    OffsetDateTime timer1endDate = null;
    Integer timer1intPeriod = null;
    List<OffsetDateTime> timer1dateTimes = null;

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (type = INTERVAL, intervalPattern = custom (empty setDateTimes))")
  void testModifyTimerTypeIntervalPtnCustomEmptySetDateTime() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.CUSTOM;
    OffsetDateTime timer1startDate = null;
    OffsetDateTime timer1endDate = null;
    Integer timer1intPeriod = null;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (type = INTERVAL, intervalPattern = custom (setDateTimes contains null))")
  void testModifyTimerTypeIntervalPtnCustomSetDateTimeContainsNull() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.CUSTOM;
    OffsetDateTime timer1startDate = null;
    OffsetDateTime timer1endDate = null;
    Integer timer1intPeriod = null;
    List<OffsetDateTime> timer1dateTimes = new ArrayList<>();
    timer1dateTimes.add(OffsetDateTime.now().plusDays(1));
    timer1dateTimes.add(null);
    timer1dateTimes.add(OffsetDateTime.now().plusMonths(1));
    timer1dateTimes.add(OffsetDateTime.now().plusYears(1));

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (type = INTERVAL (interval period = -1))")
  void testModifyTimerTypeIntervalIntervalPeriodNeg1() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.TIMES_PER_DAY;
    OffsetDateTime timer1startDate = OffsetDateTime.now();
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = -1;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (type = INTERVAL (interval period = 0))")
  void testModifyTimerTypeIntervalIntervalPeriod0() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.TIMES_PER_DAY;
    OffsetDateTime timer1startDate = OffsetDateTime.now();
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 0;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (type = INTERVAL (not custom) (null interval period))")
  void testModifyTimerTypeIntervalIntervalNull() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.TIMES_PER_DAY;
    OffsetDateTime timer1startDate = OffsetDateTime.now();
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = null;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (type = INTERVAL, intervalPattern = null)")
  void testModifyTimerTypeIntervalIntervalPtnNull() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = null;
    OffsetDateTime timer1startDate = OffsetDateTime.now();
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 3;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (type = null)")
  void testModifyTimerTypeNull() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = null;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.TIMES_PER_DAY;
    OffsetDateTime timer1startDate = OffsetDateTime.now();
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 3;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (timerTarget is null)")
  void testModifyTimerTimerTargetNull() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    List<TimerInfoTimerTarget> timer1targets = null;

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (timerTarget is empty)")
  void testModifyTimerTimerTargetEmpty() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    List<TimerInfoTimerTarget> timer1targets = Collections.emptyList();

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (timerTarget contains null)")
  void testModifyTimerTimerTargetContainsNull() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(null);
    timer1targets.add(timer1target1);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (timerTarget contains null action)")
  void testModifyTimerTimerTargetContainsNullAction() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    UUID controlId2 = UUID.fromString("22888888-2222-2222-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action1 = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    CommonTimerAction timer1action2 = null;
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action1) //
        .directValue(timer1dirVal);
    TimerInfoTimerTarget timer1target2 = new TimerInfoTimerTarget() //
        .controlId(controlId2) //
        .action(timer1action2) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);
    timer1targets.add(timer1target2);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (timerTarget contains null direct value)")
  void testModifyTimerTimerTargetContainsNullDirectValue() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    UUID controlId2 = UUID.fromString("22888888-2222-2222-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action1 = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    CommonTimerAction timer1action2 = CommonTimerAction.DIRECT;
    String timer2dirVal = null;
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action1) //
        .directValue(timer1dirVal);
    TimerInfoTimerTarget timer1target2 = new TimerInfoTimerTarget() //
        .controlId(controlId2) //
        .action(timer1action2) //
        .directValue(timer2dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);
    timer1targets.add(timer1target2);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (timerTarget contains blank direct value)")
  void testModifyTimerTimerTargetContainsBlankDirectValue() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    UUID controlId2 = UUID.fromString("22888888-2222-2222-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action1 = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    CommonTimerAction timer1action2 = CommonTimerAction.DIRECT;
    String timer2dirVal = "";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action1) //
        .directValue(timer1dirVal);
    TimerInfoTimerTarget timer1target2 = new TimerInfoTimerTarget() //
        .controlId(controlId2) //
        .action(timer1action2) //
        .directValue(timer2dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);
    timer1targets.add(timer1target2);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (timerTarget contains (non-direct) non-null direct value)")
  void testModifyTimerTimerTargetContainsNonDirectDirectValue() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    UUID controlId2 = UUID.fromString("22888888-2222-2222-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action1 = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    CommonTimerAction timer1action2 = CommonTimerAction.SLIDER_DOWN;
    String timer2dirVal = "5";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action1) //
        .directValue(timer1dirVal);
    TimerInfoTimerTarget timer1target2 = new TimerInfoTimerTarget() //
        .controlId(controlId2) //
        .action(timer1action2) //
        .directValue(timer2dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);
    timer1targets.add(timer1target2);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (timerTarget contains duplicate controlId)")
  void testModifyTimerTimerTargetContainsDuplicateControlId() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action1 = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    CommonTimerAction timer1action2 = CommonTimerAction.SLIDER_DOWN;
    String timer2dirVal = null;
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action1) //
        .directValue(timer1dirVal);
    TimerInfoTimerTarget timer1target2 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action2) //
        .directValue(timer2dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);
    timer1targets.add(timer1target2);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyTimer (some controlId in the timerTarget do not exist(null))")
  void testModifyTimerTimerTargetContainsNonExistentControlIdNull() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    UUID controlId2 = UUID.fromString("22888888-2222-2222-2222-111111111101");

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action1 = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    CommonTimerAction timer1action2 = CommonTimerAction.SLIDER_DOWN;
    String timer2dirVal = null;
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action1) //
        .directValue(timer1dirVal);
    TimerInfoTimerTarget timer1target2 = new TimerInfoTimerTarget() //
        .controlId(controlId2) //
        .action(timer1action2) //
        .directValue(timer2dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);
    timer1targets.add(timer1target2);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    inputContrSet.add(controlId2);
    List<DeviceControlInfo> contrList = null;

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot modify timer";
    String expectedDetail = "DB did not return a valid response for the controlId multi-query.";

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

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
  @DisplayName("TEST modifyTimer (some controlId in the timerTarget do not exist(empty))")
  void testModifyTimerTimerTargetContainsNonExistentControlIdEmpty() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    UUID controlId2 = UUID.fromString("22888888-2222-2222-2222-111111111101");

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action1 = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    CommonTimerAction timer1action2 = CommonTimerAction.SLIDER_DOWN;
    String timer2dirVal = null;
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action1) //
        .directValue(timer1dirVal);
    TimerInfoTimerTarget timer1target2 = new TimerInfoTimerTarget() //
        .controlId(controlId2) //
        .action(timer1action2) //
        .directValue(timer2dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);
    timer1targets.add(timer1target2);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    inputContrSet.add(controlId2);
    List<DeviceControlInfo> contrList = Collections.emptyList();

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot modify timer";
    String expectedDetail = "DB did not return a valid response for the controlId multi-query.";

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

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
  @DisplayName("TEST modifyTimer (some controlId in the timerTarget do not exist)")
  void testModifyTimerTimerTargetContainsNonExistentControlId() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    UUID controlId2 = UUID.fromString("22888888-2222-2222-2222-111111111102");
    UUID controlId3 = UUID.fromString("22888888-3333-3333-3333-111111111103");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    String contr2name = "Name 2";
    String contr2descr = "Descr 2";
    String contr2val = null;
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);
    DeviceControlInfo contr2 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId2) //
        .name(contr2name) //
        .description(contr2descr) //
        .currentValue(contr2val);

    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAILY;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 1;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    CommonTimerAction timer2action = CommonTimerAction.SLIDER_UP;
    String timer2dirVal = null;
    CommonTimerAction timer3action = CommonTimerAction.SLIDER_DOWN;
    String timer3dirVal = "";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    TimerInfoTimerTarget timer1target2 = new TimerInfoTimerTarget() //
        .controlId(controlId2) //
        .action(timer2action) //
        .directValue(timer2dirVal);
    TimerInfoTimerTarget timer1target3 = new TimerInfoTimerTarget() //
        .controlId(controlId3) //
        .action(timer3action) //
        .directValue(timer3dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);
    timer1targets.add(timer1target2);
    timer1targets.add(timer1target3);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    inputContrSet.add(controlId2);
    inputContrSet.add(controlId3);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);
    contrList.add(contr2);
    contrList.add(null);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot modify timer";
    String expectedDetail = "Some controlIds provided in the action parameter do not exist.";

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

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
  @DisplayName("TEST modifyTimer (timerId not found)")
  void testModifyTimerNotFound() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    UUID timer1id = UUID.fromString("22abcdef-1111-1111-1111-111111111101");
    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAYS_PER_WEEK;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 3;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);

    TimerModification timerMod = new TimerModification();
    timerMod.setTimerType(timer1type);
    timerMod.setIntervalPattern(timer1intPtrn);
    timerMod.setStartDateTime(timer1startDate);
    timerMod.setEndDateTime(timer1endDate);
    timerMod.setIntervalPeriod(timer1intPeriod);
    timerMod.setSetDateTimes(timer1dateTimes);
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.NOT_FOUND;

    // Mock(s)
    when(this.timerRepository.findById(eq(timer1id))).thenReturn(null);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.modifyTimer(timer1id, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 404.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST setActionToTimer")
  void testSetActionToTimer() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    UUID timer1id = UUID.fromString("22abcdef-1111-1111-1111-111111111101");
    CommonTimerType timer1type = CommonTimerType.INTERVAL;
    CommonTimerIntervalPattern timer1intPtrn = CommonTimerIntervalPattern.DAYS_PER_WEEK;
    OffsetDateTime timer1startDate = OffsetDateTime.now().minusDays(1);
    OffsetDateTime timer1endDate = OffsetDateTime.now().plusMonths(3);
    Integer timer1intPeriod = 3;
    List<OffsetDateTime> timer1dateTimes = Collections.emptyList();
    TimerInfoTimerTarget timer1Extarget1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(CommonTimerAction.SLIDER_DOWN) //
        .directValue(null);
    List<TimerInfoTimerTarget> timer1Extargets = new ArrayList<>();
    timer1Extargets.add(timer1Extarget1);

    TimerInfo timer1 = new TimerInfo();
    timer1.setTimerId(timer1id);
    timer1.setTimerType(timer1type);
    timer1.setIntervalPattern(timer1intPtrn);
    timer1.setStartDateTime(timer1startDate);
    timer1.setEndDateTime(timer1endDate);
    timer1.setIntervalPeriod(timer1intPeriod);
    timer1.setSetDateTimes(timer1dateTimes);
    timer1.setTimerTarget(timer1Extargets);

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "6.915";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targetList = new ArrayList<>();
    timer1targetList.add(timer1target1);

    TimerSetAction timerPatch = new TimerSetAction();
    timerPatch.setTimerTarget(timer1targetList);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.timerRepository.findById(eq(timer1id))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.setActionToTimer(timer1id, timerPatch);

    // Verify that appropriate Repository method was called
    verify(this.timerRepository, times(1)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertEquals(timer1id, arg1.getValue(), "Argument must match.");
    assertNotNull(arg2.getValue(), "Argument (TimerInfo) must not be null.");
    assertEquals(timer1id, arg2.getValue().getTimerId(), "Argument's timerId must match.");
    assertEquals(timer1type, arg2.getValue().getTimerType(), "Timer1's timerType must match.");
    assertEquals(timer1intPtrn, arg2.getValue().getIntervalPattern(),
        "Timer1's intervalPattern must match.");
    assertEquals(timer1startDate, arg2.getValue().getStartDateTime(),
        "Timer1's startDateTime must match.");
    assertEquals(timer1endDate, arg2.getValue().getEndDateTime(),
        "Timer1's endDateTime must match.");
    assertEquals(timer1intPeriod, arg2.getValue().getIntervalPeriod(),
        "Timer1's intervalPeriod must match.");
    assertEquals(timer1dateTimes, arg2.getValue().getSetDateTimes(),
        "Timer1's setDateTimes must match.");
    assertEquals(timer1targetList, arg2.getValue().getTimerTarget(),
        "Timer1's timerTarget must match.");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST setActionToTimer (timerTarget is null)")
  void testSetActionToTimerTimerTargetNull() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    List<TimerInfoTimerTarget> timer1targets = null;

    TimerSetAction timerMod = new TimerSetAction();
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.setActionToTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST setActionToTimer (timerTarget is empty)")
  void testSetActionToTimerTimerTargetEmpty() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    List<TimerInfoTimerTarget> timer1targets = Collections.emptyList();

    TimerSetAction timerMod = new TimerSetAction();
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.setActionToTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST setActionToTimer (timerTarget contains null)")
  void testSetActionToTimerTimerTargetContainsNull() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(null);
    timer1targets.add(timer1target1);

    TimerSetAction timerMod = new TimerSetAction();
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.setActionToTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST setActionToTimer (timerTarget contains null action)")
  void testSetActionToTimerTimerTargetContainsNullAction() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    UUID controlId2 = UUID.fromString("22888888-2222-2222-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerAction timer1action1 = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    CommonTimerAction timer1action2 = null;
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action1) //
        .directValue(timer1dirVal);
    TimerInfoTimerTarget timer1target2 = new TimerInfoTimerTarget() //
        .controlId(controlId2) //
        .action(timer1action2) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);
    timer1targets.add(timer1target2);

    TimerSetAction timerMod = new TimerSetAction();
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.setActionToTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST setActionToTimer (timerTarget contains null direct value)")
  void testSetActionToTimerTimerTargetContainsNullDirectValue() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    UUID controlId2 = UUID.fromString("22888888-2222-2222-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerAction timer1action1 = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    CommonTimerAction timer1action2 = CommonTimerAction.DIRECT;
    String timer2dirVal = null;
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action1) //
        .directValue(timer1dirVal);
    TimerInfoTimerTarget timer1target2 = new TimerInfoTimerTarget() //
        .controlId(controlId2) //
        .action(timer1action2) //
        .directValue(timer2dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);
    timer1targets.add(timer1target2);

    TimerSetAction timerMod = new TimerSetAction();
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.setActionToTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST setActionToTimer (timerTarget contains blank direct value)")
  void testSetActionToTimerTimerTargetContainsBlankDirectValue() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    UUID controlId2 = UUID.fromString("22888888-2222-2222-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerAction timer1action1 = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    CommonTimerAction timer1action2 = CommonTimerAction.DIRECT;
    String timer2dirVal = "";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action1) //
        .directValue(timer1dirVal);
    TimerInfoTimerTarget timer1target2 = new TimerInfoTimerTarget() //
        .controlId(controlId2) //
        .action(timer1action2) //
        .directValue(timer2dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);
    timer1targets.add(timer1target2);

    TimerSetAction timerMod = new TimerSetAction();
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.setActionToTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST setActionToTimer (timerTarget contains (non-direct) non-null direct value)")
  void testSetActionToTimerTimerTargetContainsNonDirectDirectValue() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    UUID controlId2 = UUID.fromString("22888888-2222-2222-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerAction timer1action1 = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    CommonTimerAction timer1action2 = CommonTimerAction.SLIDER_DOWN;
    String timer2dirVal = "5";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action1) //
        .directValue(timer1dirVal);
    TimerInfoTimerTarget timer1target2 = new TimerInfoTimerTarget() //
        .controlId(controlId2) //
        .action(timer1action2) //
        .directValue(timer2dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);
    timer1targets.add(timer1target2);

    TimerSetAction timerMod = new TimerSetAction();
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.setActionToTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST setActionToTimer (timerTarget contains duplicate controlId)")
  void testSetActionToTimerTimerTargetContainsDuplicateControlId() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonTimerAction timer1action1 = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    CommonTimerAction timer1action2 = CommonTimerAction.SLIDER_DOWN;
    String timer2dirVal = null;
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action1) //
        .directValue(timer1dirVal);
    TimerInfoTimerTarget timer1target2 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action2) //
        .directValue(timer2dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);
    timer1targets.add(timer1target2);

    TimerSetAction timerMod = new TimerSetAction();
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.setActionToTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST setActionToTimer (some controlId in the timerTarget do not exist(null))")
  void testSetActionToTimerTimerTargetContainsNonExistentControlIdNull() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    UUID controlId2 = UUID.fromString("22888888-2222-2222-2222-111111111101");

    CommonTimerAction timer1action1 = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    CommonTimerAction timer1action2 = CommonTimerAction.SLIDER_DOWN;
    String timer2dirVal = null;
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action1) //
        .directValue(timer1dirVal);
    TimerInfoTimerTarget timer1target2 = new TimerInfoTimerTarget() //
        .controlId(controlId2) //
        .action(timer1action2) //
        .directValue(timer2dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);
    timer1targets.add(timer1target2);

    TimerSetAction timerMod = new TimerSetAction();
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    inputContrSet.add(controlId2);
    List<DeviceControlInfo> contrList = null;

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot set action to timer";
    String expectedDetail = "DB did not return a valid response for the controlId multi-query.";

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.setActionToTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

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
  @DisplayName("TEST setActionToTimer (some controlId in the timerTarget do not exist(empty))")
  void testSetActionToTimerTimerTargetContainsNonExistentControlIdEmpty() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    UUID controlId2 = UUID.fromString("22888888-2222-2222-2222-111111111101");

    CommonTimerAction timer1action1 = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    CommonTimerAction timer1action2 = CommonTimerAction.SLIDER_DOWN;
    String timer2dirVal = null;
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action1) //
        .directValue(timer1dirVal);
    TimerInfoTimerTarget timer1target2 = new TimerInfoTimerTarget() //
        .controlId(controlId2) //
        .action(timer1action2) //
        .directValue(timer2dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);
    timer1targets.add(timer1target2);

    TimerSetAction timerMod = new TimerSetAction();
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    inputContrSet.add(controlId2);
    List<DeviceControlInfo> contrList = Collections.emptyList();

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot set action to timer";
    String expectedDetail = "DB did not return a valid response for the controlId multi-query.";

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.setActionToTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

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
  @DisplayName("TEST setActionToTimer (some controlId in the timerTarget do not exist)")
  void testSetActionToTimerTimerTargetContainsNonExistentControlId() {
    UUID timerId = UUID.fromString("afcb3e09-775c-1111-1111-9170abcdef01");
    TimerInfo timer1 = new TimerInfo().timerId(timerId);
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    UUID controlId2 = UUID.fromString("22888888-2222-2222-2222-111111111102");
    UUID controlId3 = UUID.fromString("22888888-3333-3333-3333-111111111103");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    String contr2name = "Name 2";
    String contr2descr = "Descr 2";
    String contr2val = null;
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);
    DeviceControlInfo contr2 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId2) //
        .name(contr2name) //
        .description(contr2descr) //
        .currentValue(contr2val);

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "8.227";
    CommonTimerAction timer2action = CommonTimerAction.SLIDER_UP;
    String timer2dirVal = null;
    CommonTimerAction timer3action = CommonTimerAction.SLIDER_DOWN;
    String timer3dirVal = "";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    TimerInfoTimerTarget timer1target2 = new TimerInfoTimerTarget() //
        .controlId(controlId2) //
        .action(timer2action) //
        .directValue(timer2dirVal);
    TimerInfoTimerTarget timer1target3 = new TimerInfoTimerTarget() //
        .controlId(controlId3) //
        .action(timer3action) //
        .directValue(timer3dirVal);
    List<TimerInfoTimerTarget> timer1targets = new ArrayList<>();
    timer1targets.add(timer1target1);
    timer1targets.add(timer1target2);
    timer1targets.add(timer1target3);

    TimerSetAction timerMod = new TimerSetAction();
    timerMod.setTimerTarget(timer1targets);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    inputContrSet.add(controlId2);
    inputContrSet.add(controlId3);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);
    contrList.add(contr2);
    contrList.add(null);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot set action to timer";
    String expectedDetail = "Some controlIds provided in the action parameter do not exist.";

    // Mock(s)
    when(this.timerRepository.findById(eq(timerId))).thenReturn(timer1);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);
    when(this.timerRepository.findById(any(UUID.class))).thenReturn(new TimerInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.setActionToTimer(timerId, timerMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

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
  @DisplayName("TEST setActionToTimer (timerId not found)")
  void testSetActionToTimerNotFound() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID controlId1 = UUID.fromString("22888888-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";
    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(controlId1) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    Set<UUID> inputContrSet = new HashSet<>();
    inputContrSet.add(controlId1);
    List<DeviceControlInfo> contrList = new ArrayList<>();
    contrList.add(contr1);

    UUID timer1id = UUID.fromString("22abcdef-1111-1111-1111-111111111101");

    CommonTimerAction timer1action = CommonTimerAction.DIRECT;
    String timer1dirVal = "6.915";
    TimerInfoTimerTarget timer1target1 = new TimerInfoTimerTarget() //
        .controlId(controlId1) //
        .action(timer1action) //
        .directValue(timer1dirVal);
    List<TimerInfoTimerTarget> timer1targetList = new ArrayList<>();
    timer1targetList.add(timer1target1);

    TimerSetAction timerPatch = new TimerSetAction();
    timerPatch.setTimerTarget(timer1targetList);

    HttpStatus expectedStatus = HttpStatus.NOT_FOUND;

    // Mock(s)
    when(this.timerRepository.findById(eq(timer1id))).thenReturn(null);
    when(this.controlRepository.findManyById(eq(inputContrSet))).thenReturn(contrList);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerInfo> arg2 = ArgumentCaptor.forClass(TimerInfo.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.setActionToTimer(timer1id, timerPatch);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 404.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST deleteTimer")
  void testDeleteTimer() {
    UUID timer1id = UUID.fromString("22abcdef-1111-1111-1111-111111111101");

    TimerInfo timer1 = new TimerInfo().timerId(timer1id);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.timerRepository.findById(eq(timer1id))).thenReturn(timer1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.deleteTimer(timer1id);

    // Verify that appropriate Repository method was called
    verify(this.timerRepository, times(1)).delete(arg1.capture());

    // Assertions
    assertEquals(timer1id, arg1.getValue(), "Argument must match.");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST deleteTimer (timerId not found in DB)")
  void testDeleteTimerNotFound() {
    UUID timer1id = UUID.fromString("22abcdef-1111-1111-1111-111111111101");
    UUID otherZoneId = UUID.fromString("55abcdef-ffff-cccc-bbbb-11111111110f");

    TimerInfo timer1 = new TimerInfo().timerId(timer1id);

    HttpStatus expectedStatus = HttpStatus.NOT_FOUND;

    // Mock(s)
    when(this.timerRepository.findById(eq(timer1id))).thenReturn(timer1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call Service
    CommonOperResponse operResp = this.timerService.deleteTimer(otherZoneId);

    // Verify that appropriate Repository method was NOT called
    verify(this.timerRepository, times(0)).delete(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 404.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }
}
