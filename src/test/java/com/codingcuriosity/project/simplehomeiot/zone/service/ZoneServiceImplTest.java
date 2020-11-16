package com.codingcuriosity.project.simplehomeiot.zone.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import com.codingcuriosity.project.simplehomeiot.devices.model.DeviceInfo;
import com.codingcuriosity.project.simplehomeiot.devices.repository.DeviceRepository;
import com.codingcuriosity.project.simplehomeiot.logs.repository.ControllerLogRepository;
import com.codingcuriosity.project.simplehomeiot.zones.model.ZoneCreation;
import com.codingcuriosity.project.simplehomeiot.zones.model.ZoneInfo;
import com.codingcuriosity.project.simplehomeiot.zones.model.ZoneModification;
import com.codingcuriosity.project.simplehomeiot.zones.model.ZoneSetDevice;
import com.codingcuriosity.project.simplehomeiot.zones.repository.ZoneRepository;
import com.codingcuriosity.project.simplehomeiot.zones.service.ZoneService;
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
public class ZoneServiceImplTest {

  @Autowired
  ZoneService zoneService;

  @MockBean
  DeviceRepository deviceRepository;

  @MockBean
  ZoneRepository zoneRepository;

  @MockBean
  ControllerLogRepository logRepository;

  @Test
  @DisplayName("TEST getLogRepo")
  void testGetLogRepo() {

    // Call Service
    ControllerLogRepository logRepository = this.zoneService.getLogRepo();

    // Assertions
    assertEquals(this.logRepository, logRepository, "LogRepository must be identical.");
  }

  @Test
  @DisplayName("TEST getZones (all parameters = null)")
  void testGetZones() {
    Integer skip = null;
    Integer limit = null;

    UUID zone1id = UUID.fromString("08abcdef-1111-1111-1111-111111111101");
    UUID zone2id = UUID.fromString("08abcdef-2222-2222-2222-222222222202");
    UUID zone3id = UUID.fromString("08abcdef-3333-3333-3333-333333333303");
    UUID zone4id = UUID.fromString("08abcdef-4444-4444-4444-444444444404");
    String zone1name = "B zone name Test";
    String zone2name = "A zone name Test";
    String zone3name = "D zone name Test";
    String zone4name = "C zone name Test";
    String zone1comment = "B zone comment Test";
    String zone2comment = "A zone comment Test";
    String zone3comment = "D zone comment Test";
    String zone4comment = "C zone comment Test";
    UUID zone1dev1id = UUID.fromString("08abcdef-1111-aaaa-ffff-11111111af01");
    UUID zone1dev2id = UUID.fromString("08abcdef-1111-aaaa-ffff-22222222af01");
    UUID zone2dev1id = UUID.fromString("08abcdef-2222-aaaa-ffff-11111111af01");
    UUID zone2dev2id = UUID.fromString("08abcdef-2222-aaaa-ffff-22222222af01");
    UUID zone3dev1id = UUID.fromString("08abcdef-3333-aaaa-ffff-11111111af01");
    UUID zone3dev2id = UUID.fromString("08abcdef-3333-aaaa-ffff-22222222af01");
    UUID zone4dev1id = UUID.fromString("08abcdef-4444-aaaa-ffff-11111111af01");
    UUID zone4dev2id = UUID.fromString("08abcdef-4444-aaaa-ffff-22222222af01");

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.addDeviceIdsItem(zone1dev1id);
    zone1.addDeviceIdsItem(zone1dev2id);
    ZoneInfo zone2 = new ZoneInfo();
    zone2.setZoneId(zone2id);
    zone2.setName(zone2name);
    zone2.setComment(zone2comment);
    zone2.addDeviceIdsItem(zone2dev1id);
    zone2.addDeviceIdsItem(zone2dev2id);
    ZoneInfo zone3 = new ZoneInfo();
    zone3.setZoneId(zone3id);
    zone3.setName(zone3name);
    zone3.setComment(zone3comment);
    zone3.addDeviceIdsItem(zone3dev1id);
    zone3.addDeviceIdsItem(zone3dev2id);
    ZoneInfo zone4 = new ZoneInfo();
    zone4.setZoneId(zone4id);
    zone4.setName(zone4name);
    zone4.setComment(zone4comment);
    zone4.addDeviceIdsItem(zone4dev1id);
    zone4.addDeviceIdsItem(zone4dev2id);

    Map<UUID, ZoneInfo> zoneMap = new HashMap<>();
    zoneMap.put(zone1id, zone1);
    zoneMap.put(zone2id, zone2);
    zoneMap.put(zone3id, zone3);
    zoneMap.put(zone4id, zone4);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(zoneMap).when(this.zoneRepository).findAll();

    // Call service
    CommonGetListResponse<ZoneInfo> retListResp = this.zoneService.getZones(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.zoneRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be sorted name ascending.", retListResp.getObjList(),
        contains(zone2, zone1, zone4, zone3));
    assertEquals(zone2id, retListResp.getObjList().get(0).getZoneId(),
        "1st entry's UUID must match zone 2's");
    assertEquals(zone2name, retListResp.getObjList().get(0).getName(),
        "1st entry's name must match zone 2's");
    assertEquals(zone2comment, retListResp.getObjList().get(0).getComment(),
        "1st entry's comment must match zone 2's");
    assertThat("The device Ids must be listed in any order",
        retListResp.getObjList().get(0).getDeviceIds(),
        containsInAnyOrder(zone2dev1id, zone2dev2id));
    assertEquals(zone1id, retListResp.getObjList().get(1).getZoneId(),
        "2nd entry's UUID must match zone 1's");
    assertEquals(zone1name, retListResp.getObjList().get(1).getName(),
        "2nd entry's name must match zone 1's");
    assertEquals(zone1comment, retListResp.getObjList().get(1).getComment(),
        "2nd entry's comment must match zone 1's");
    assertThat("The device Ids must be listed in any order",
        retListResp.getObjList().get(1).getDeviceIds(),
        containsInAnyOrder(zone1dev1id, zone1dev2id));
    assertEquals(zone4id, retListResp.getObjList().get(2).getZoneId(),
        "3rd entry's UUID must match zone 4's");
    assertEquals(zone4name, retListResp.getObjList().get(2).getName(),
        "3rd entry's name must match zone 4's");
    assertEquals(zone4comment, retListResp.getObjList().get(2).getComment(),
        "3rd entry's comment must match zone 4's");
    assertThat("The device Ids must be listed in any order",
        retListResp.getObjList().get(2).getDeviceIds(),
        containsInAnyOrder(zone4dev1id, zone4dev2id));
    assertEquals(zone3id, retListResp.getObjList().get(3).getZoneId(),
        "4th entry's UUID must match zone 3's");
    assertEquals(zone3name, retListResp.getObjList().get(3).getName(),
        "4th entry's name must match zone 3's");
    assertEquals(zone3comment, retListResp.getObjList().get(3).getComment(),
        "4th entry's comment must match zone 3's");
    assertThat("The device Ids must be listed in any order",
        retListResp.getObjList().get(3).getDeviceIds(),
        containsInAnyOrder(zone3dev1id, zone3dev2id));
  }

  @Test
  @DisplayName("TEST getZones (skip = 0, limit = 0)")
  void testGetZonesSkipMinLimitMin() {
    Integer skip = 0;
    Integer limit = 0;

    UUID zone1id = UUID.fromString("08abcdef-1111-1111-1111-111111111101");
    UUID zone2id = UUID.fromString("08abcdef-2222-2222-2222-222222222202");
    UUID zone3id = UUID.fromString("08abcdef-3333-3333-3333-333333333303");
    UUID zone4id = UUID.fromString("08abcdef-4444-4444-4444-444444444404");
    String zone1name = "B zone name Test";
    String zone2name = "A zone name Test";
    String zone3name = "D zone name Test";
    String zone4name = "C zone name Test";
    String zone1comment = "B zone comment Test";
    String zone2comment = "A zone comment Test";
    String zone3comment = "D zone comment Test";
    String zone4comment = "C zone comment Test";
    UUID zone1dev1id = UUID.fromString("08abcdef-1111-aaaa-ffff-11111111af01");
    UUID zone1dev2id = UUID.fromString("08abcdef-1111-aaaa-ffff-22222222af01");
    UUID zone2dev1id = UUID.fromString("08abcdef-2222-aaaa-ffff-11111111af01");
    UUID zone2dev2id = UUID.fromString("08abcdef-2222-aaaa-ffff-22222222af01");
    UUID zone3dev1id = UUID.fromString("08abcdef-3333-aaaa-ffff-11111111af01");
    UUID zone3dev2id = UUID.fromString("08abcdef-3333-aaaa-ffff-22222222af01");
    UUID zone4dev1id = UUID.fromString("08abcdef-4444-aaaa-ffff-11111111af01");
    UUID zone4dev2id = UUID.fromString("08abcdef-4444-aaaa-ffff-22222222af01");

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.addDeviceIdsItem(zone1dev1id);
    zone1.addDeviceIdsItem(zone1dev2id);
    ZoneInfo zone2 = new ZoneInfo();
    zone2.setZoneId(zone2id);
    zone2.setName(zone2name);
    zone2.setComment(zone2comment);
    zone2.addDeviceIdsItem(zone2dev1id);
    zone2.addDeviceIdsItem(zone2dev2id);
    ZoneInfo zone3 = new ZoneInfo();
    zone3.setZoneId(zone3id);
    zone3.setName(zone3name);
    zone3.setComment(zone3comment);
    zone3.addDeviceIdsItem(zone3dev1id);
    zone3.addDeviceIdsItem(zone3dev2id);
    ZoneInfo zone4 = new ZoneInfo();
    zone4.setZoneId(zone4id);
    zone4.setName(zone4name);
    zone4.setComment(zone4comment);
    zone4.addDeviceIdsItem(zone4dev1id);
    zone4.addDeviceIdsItem(zone4dev2id);

    Map<UUID, ZoneInfo> zoneMap = new HashMap<>();
    zoneMap.put(zone1id, zone1);
    zoneMap.put(zone2id, zone2);
    zoneMap.put(zone3id, zone3);
    zoneMap.put(zone4id, zone4);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(zoneMap).when(this.zoneRepository).findAll();

    // Call service
    CommonGetListResponse<ZoneInfo> retListResp = this.zoneService.getZones(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.zoneRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getZones (skip = 0, limit = entry max)")
  void testGetZonesSkipMinLimitMax() {
    Integer skip = 0;
    Integer limit = null; // Will be changed below

    UUID zone1id = UUID.fromString("08abcdef-1111-1111-1111-111111111101");
    UUID zone2id = UUID.fromString("08abcdef-2222-2222-2222-222222222202");
    UUID zone3id = UUID.fromString("08abcdef-3333-3333-3333-333333333303");
    UUID zone4id = UUID.fromString("08abcdef-4444-4444-4444-444444444404");
    String zone1name = "B zone name Test";
    String zone2name = "A zone name Test";
    String zone3name = "D zone name Test";
    String zone4name = "C zone name Test";
    String zone1comment = "B zone comment Test";
    String zone2comment = "A zone comment Test";
    String zone3comment = "D zone comment Test";
    String zone4comment = "C zone comment Test";
    UUID zone1dev1id = UUID.fromString("08abcdef-1111-aaaa-ffff-11111111af01");
    UUID zone1dev2id = UUID.fromString("08abcdef-1111-aaaa-ffff-22222222af01");
    UUID zone2dev1id = UUID.fromString("08abcdef-2222-aaaa-ffff-11111111af01");
    UUID zone2dev2id = UUID.fromString("08abcdef-2222-aaaa-ffff-22222222af01");
    UUID zone3dev1id = UUID.fromString("08abcdef-3333-aaaa-ffff-11111111af01");
    UUID zone3dev2id = UUID.fromString("08abcdef-3333-aaaa-ffff-22222222af01");
    UUID zone4dev1id = UUID.fromString("08abcdef-4444-aaaa-ffff-11111111af01");
    UUID zone4dev2id = UUID.fromString("08abcdef-4444-aaaa-ffff-22222222af01");

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.addDeviceIdsItem(zone1dev1id);
    zone1.addDeviceIdsItem(zone1dev2id);
    ZoneInfo zone2 = new ZoneInfo();
    zone2.setZoneId(zone2id);
    zone2.setName(zone2name);
    zone2.setComment(zone2comment);
    zone2.addDeviceIdsItem(zone2dev1id);
    zone2.addDeviceIdsItem(zone2dev2id);
    ZoneInfo zone3 = new ZoneInfo();
    zone3.setZoneId(zone3id);
    zone3.setName(zone3name);
    zone3.setComment(zone3comment);
    zone3.addDeviceIdsItem(zone3dev1id);
    zone3.addDeviceIdsItem(zone3dev2id);
    ZoneInfo zone4 = new ZoneInfo();
    zone4.setZoneId(zone4id);
    zone4.setName(zone4name);
    zone4.setComment(zone4comment);
    zone4.addDeviceIdsItem(zone4dev1id);
    zone4.addDeviceIdsItem(zone4dev2id);

    Map<UUID, ZoneInfo> zoneMap = new HashMap<>();
    zoneMap.put(zone1id, zone1);
    zoneMap.put(zone2id, zone2);
    zoneMap.put(zone3id, zone3);
    zoneMap.put(zone4id, zone4);

    limit = zoneMap.size();

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(zoneMap).when(this.zoneRepository).findAll();

    // Call service
    CommonGetListResponse<ZoneInfo> retListResp = this.zoneService.getZones(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.zoneRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be sorted name ascending.", retListResp.getObjList(),
        contains(zone2, zone1, zone4, zone3));
  }

  @Test
  @DisplayName("TEST getZones (skip = entry max)")
  void testGetZonesSkipMax() {
    Integer skip = null; // Will be changed below
    Integer limit = null;

    UUID zone1id = UUID.fromString("08abcdef-1111-1111-1111-111111111101");
    UUID zone2id = UUID.fromString("08abcdef-2222-2222-2222-222222222202");
    UUID zone3id = UUID.fromString("08abcdef-3333-3333-3333-333333333303");
    UUID zone4id = UUID.fromString("08abcdef-4444-4444-4444-444444444404");
    String zone1name = "B zone name Test";
    String zone2name = "A zone name Test";
    String zone3name = "D zone name Test";
    String zone4name = "C zone name Test";
    String zone1comment = "B zone comment Test";
    String zone2comment = "A zone comment Test";
    String zone3comment = "D zone comment Test";
    String zone4comment = "C zone comment Test";
    UUID zone1dev1id = UUID.fromString("08abcdef-1111-aaaa-ffff-11111111af01");
    UUID zone1dev2id = UUID.fromString("08abcdef-1111-aaaa-ffff-22222222af01");
    UUID zone2dev1id = UUID.fromString("08abcdef-2222-aaaa-ffff-11111111af01");
    UUID zone2dev2id = UUID.fromString("08abcdef-2222-aaaa-ffff-22222222af01");
    UUID zone3dev1id = UUID.fromString("08abcdef-3333-aaaa-ffff-11111111af01");
    UUID zone3dev2id = UUID.fromString("08abcdef-3333-aaaa-ffff-22222222af01");
    UUID zone4dev1id = UUID.fromString("08abcdef-4444-aaaa-ffff-11111111af01");
    UUID zone4dev2id = UUID.fromString("08abcdef-4444-aaaa-ffff-22222222af01");

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.addDeviceIdsItem(zone1dev1id);
    zone1.addDeviceIdsItem(zone1dev2id);
    ZoneInfo zone2 = new ZoneInfo();
    zone2.setZoneId(zone2id);
    zone2.setName(zone2name);
    zone2.setComment(zone2comment);
    zone2.addDeviceIdsItem(zone2dev1id);
    zone2.addDeviceIdsItem(zone2dev2id);
    ZoneInfo zone3 = new ZoneInfo();
    zone3.setZoneId(zone3id);
    zone3.setName(zone3name);
    zone3.setComment(zone3comment);
    zone3.addDeviceIdsItem(zone3dev1id);
    zone3.addDeviceIdsItem(zone3dev2id);
    ZoneInfo zone4 = new ZoneInfo();
    zone4.setZoneId(zone4id);
    zone4.setName(zone4name);
    zone4.setComment(zone4comment);
    zone4.addDeviceIdsItem(zone4dev1id);
    zone4.addDeviceIdsItem(zone4dev2id);

    Map<UUID, ZoneInfo> zoneMap = new HashMap<>();
    zoneMap.put(zone1id, zone1);
    zoneMap.put(zone2id, zone2);
    zoneMap.put(zone3id, zone3);
    zoneMap.put(zone4id, zone4);

    skip = zoneMap.size();

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(zoneMap).when(this.zoneRepository).findAll();



    // Call service
    CommonGetListResponse<ZoneInfo> retListResp = this.zoneService.getZones(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.zoneRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getZones (skip = -1)")
  void testGetZonesSkipMinMinus1() {
    Integer skip = -1;
    Integer limit = null;

    UUID zone1id = UUID.fromString("08abcdef-1111-1111-1111-111111111101");
    UUID zone2id = UUID.fromString("08abcdef-2222-2222-2222-222222222202");
    UUID zone3id = UUID.fromString("08abcdef-3333-3333-3333-333333333303");
    UUID zone4id = UUID.fromString("08abcdef-4444-4444-4444-444444444404");
    String zone1name = "B zone name Test";
    String zone2name = "A zone name Test";
    String zone3name = "D zone name Test";
    String zone4name = "C zone name Test";
    String zone1comment = "B zone comment Test";
    String zone2comment = "A zone comment Test";
    String zone3comment = "D zone comment Test";
    String zone4comment = "C zone comment Test";
    UUID zone1dev1id = UUID.fromString("08abcdef-1111-aaaa-ffff-11111111af01");
    UUID zone1dev2id = UUID.fromString("08abcdef-1111-aaaa-ffff-22222222af01");
    UUID zone2dev1id = UUID.fromString("08abcdef-2222-aaaa-ffff-11111111af01");
    UUID zone2dev2id = UUID.fromString("08abcdef-2222-aaaa-ffff-22222222af01");
    UUID zone3dev1id = UUID.fromString("08abcdef-3333-aaaa-ffff-11111111af01");
    UUID zone3dev2id = UUID.fromString("08abcdef-3333-aaaa-ffff-22222222af01");
    UUID zone4dev1id = UUID.fromString("08abcdef-4444-aaaa-ffff-11111111af01");
    UUID zone4dev2id = UUID.fromString("08abcdef-4444-aaaa-ffff-22222222af01");

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.addDeviceIdsItem(zone1dev1id);
    zone1.addDeviceIdsItem(zone1dev2id);
    ZoneInfo zone2 = new ZoneInfo();
    zone2.setZoneId(zone2id);
    zone2.setName(zone2name);
    zone2.setComment(zone2comment);
    zone2.addDeviceIdsItem(zone2dev1id);
    zone2.addDeviceIdsItem(zone2dev2id);
    ZoneInfo zone3 = new ZoneInfo();
    zone3.setZoneId(zone3id);
    zone3.setName(zone3name);
    zone3.setComment(zone3comment);
    zone3.addDeviceIdsItem(zone3dev1id);
    zone3.addDeviceIdsItem(zone3dev2id);
    ZoneInfo zone4 = new ZoneInfo();
    zone4.setZoneId(zone4id);
    zone4.setName(zone4name);
    zone4.setComment(zone4comment);
    zone4.addDeviceIdsItem(zone4dev1id);
    zone4.addDeviceIdsItem(zone4dev2id);

    Map<UUID, ZoneInfo> zoneMap = new HashMap<>();
    zoneMap.put(zone1id, zone1);
    zoneMap.put(zone2id, zone2);
    zoneMap.put(zone3id, zone3);
    zoneMap.put(zone4id, zone4);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    doReturn(zoneMap).when(this.zoneRepository).findAll();

    // Call service
    CommonGetListResponse<ZoneInfo> retListResp = this.zoneService.getZones(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.zoneRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 400.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getZones (limit = -1)")
  void testGetZonesLimitMinMinus1() {
    Integer skip = null;
    Integer limit = -1;

    UUID zone1id = UUID.fromString("08abcdef-1111-1111-1111-111111111101");
    UUID zone2id = UUID.fromString("08abcdef-2222-2222-2222-222222222202");
    UUID zone3id = UUID.fromString("08abcdef-3333-3333-3333-333333333303");
    UUID zone4id = UUID.fromString("08abcdef-4444-4444-4444-444444444404");
    String zone1name = "B zone name Test";
    String zone2name = "A zone name Test";
    String zone3name = "D zone name Test";
    String zone4name = "C zone name Test";
    String zone1comment = "B zone comment Test";
    String zone2comment = "A zone comment Test";
    String zone3comment = "D zone comment Test";
    String zone4comment = "C zone comment Test";
    UUID zone1dev1id = UUID.fromString("08abcdef-1111-aaaa-ffff-11111111af01");
    UUID zone1dev2id = UUID.fromString("08abcdef-1111-aaaa-ffff-22222222af01");
    UUID zone2dev1id = UUID.fromString("08abcdef-2222-aaaa-ffff-11111111af01");
    UUID zone2dev2id = UUID.fromString("08abcdef-2222-aaaa-ffff-22222222af01");
    UUID zone3dev1id = UUID.fromString("08abcdef-3333-aaaa-ffff-11111111af01");
    UUID zone3dev2id = UUID.fromString("08abcdef-3333-aaaa-ffff-22222222af01");
    UUID zone4dev1id = UUID.fromString("08abcdef-4444-aaaa-ffff-11111111af01");
    UUID zone4dev2id = UUID.fromString("08abcdef-4444-aaaa-ffff-22222222af01");

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.addDeviceIdsItem(zone1dev1id);
    zone1.addDeviceIdsItem(zone1dev2id);
    ZoneInfo zone2 = new ZoneInfo();
    zone2.setZoneId(zone2id);
    zone2.setName(zone2name);
    zone2.setComment(zone2comment);
    zone2.addDeviceIdsItem(zone2dev1id);
    zone2.addDeviceIdsItem(zone2dev2id);
    ZoneInfo zone3 = new ZoneInfo();
    zone3.setZoneId(zone3id);
    zone3.setName(zone3name);
    zone3.setComment(zone3comment);
    zone3.addDeviceIdsItem(zone3dev1id);
    zone3.addDeviceIdsItem(zone3dev2id);
    ZoneInfo zone4 = new ZoneInfo();
    zone4.setZoneId(zone4id);
    zone4.setName(zone4name);
    zone4.setComment(zone4comment);
    zone4.addDeviceIdsItem(zone4dev1id);
    zone4.addDeviceIdsItem(zone4dev2id);

    Map<UUID, ZoneInfo> zoneMap = new HashMap<>();
    zoneMap.put(zone1id, zone1);
    zoneMap.put(zone2id, zone2);
    zoneMap.put(zone3id, zone3);
    zoneMap.put(zone4id, zone4);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    doReturn(zoneMap).when(this.zoneRepository).findAll();

    // Call service
    CommonGetListResponse<ZoneInfo> retListResp = this.zoneService.getZones(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.zoneRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 400.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getZones (skip = entry max + 1)")
  void testGetZonesSkipMaxPlus1() {
    Integer skip = null; // Will be changed below
    Integer limit = null;

    UUID zone1id = UUID.fromString("08abcdef-1111-1111-1111-111111111101");
    UUID zone2id = UUID.fromString("08abcdef-2222-2222-2222-222222222202");
    UUID zone3id = UUID.fromString("08abcdef-3333-3333-3333-333333333303");
    UUID zone4id = UUID.fromString("08abcdef-4444-4444-4444-444444444404");
    String zone1name = "B zone name Test";
    String zone2name = "A zone name Test";
    String zone3name = "D zone name Test";
    String zone4name = "C zone name Test";
    String zone1comment = "B zone comment Test";
    String zone2comment = "A zone comment Test";
    String zone3comment = "D zone comment Test";
    String zone4comment = "C zone comment Test";
    UUID zone1dev1id = UUID.fromString("08abcdef-1111-aaaa-ffff-11111111af01");
    UUID zone1dev2id = UUID.fromString("08abcdef-1111-aaaa-ffff-22222222af01");
    UUID zone2dev1id = UUID.fromString("08abcdef-2222-aaaa-ffff-11111111af01");
    UUID zone2dev2id = UUID.fromString("08abcdef-2222-aaaa-ffff-22222222af01");
    UUID zone3dev1id = UUID.fromString("08abcdef-3333-aaaa-ffff-11111111af01");
    UUID zone3dev2id = UUID.fromString("08abcdef-3333-aaaa-ffff-22222222af01");
    UUID zone4dev1id = UUID.fromString("08abcdef-4444-aaaa-ffff-11111111af01");
    UUID zone4dev2id = UUID.fromString("08abcdef-4444-aaaa-ffff-22222222af01");

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.addDeviceIdsItem(zone1dev1id);
    zone1.addDeviceIdsItem(zone1dev2id);
    ZoneInfo zone2 = new ZoneInfo();
    zone2.setZoneId(zone2id);
    zone2.setName(zone2name);
    zone2.setComment(zone2comment);
    zone2.addDeviceIdsItem(zone2dev1id);
    zone2.addDeviceIdsItem(zone2dev2id);
    ZoneInfo zone3 = new ZoneInfo();
    zone3.setZoneId(zone3id);
    zone3.setName(zone3name);
    zone3.setComment(zone3comment);
    zone3.addDeviceIdsItem(zone3dev1id);
    zone3.addDeviceIdsItem(zone3dev2id);
    ZoneInfo zone4 = new ZoneInfo();
    zone4.setZoneId(zone4id);
    zone4.setName(zone4name);
    zone4.setComment(zone4comment);
    zone4.addDeviceIdsItem(zone4dev1id);
    zone4.addDeviceIdsItem(zone4dev2id);

    Map<UUID, ZoneInfo> zoneMap = new HashMap<>();
    zoneMap.put(zone1id, zone1);
    zoneMap.put(zone2id, zone2);
    zoneMap.put(zone3id, zone3);
    zoneMap.put(zone4id, zone4);

    skip = zoneMap.size() + 1;

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(zoneMap).when(this.zoneRepository).findAll();

    // Call service
    CommonGetListResponse<ZoneInfo> retListResp = this.zoneService.getZones(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.zoneRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getZones (limit = entry max + 1)")
  void testGetZonesLimitMaxPlus1() {
    Integer skip = null;
    Integer limit = null; // Will be changed below

    UUID zone1id = UUID.fromString("08abcdef-1111-1111-1111-111111111101");
    UUID zone2id = UUID.fromString("08abcdef-2222-2222-2222-222222222202");
    UUID zone3id = UUID.fromString("08abcdef-3333-3333-3333-333333333303");
    UUID zone4id = UUID.fromString("08abcdef-4444-4444-4444-444444444404");
    String zone1name = "B zone name Test";
    String zone2name = "A zone name Test";
    String zone3name = "D zone name Test";
    String zone4name = "C zone name Test";
    String zone1comment = "B zone comment Test";
    String zone2comment = "A zone comment Test";
    String zone3comment = "D zone comment Test";
    String zone4comment = "C zone comment Test";
    UUID zone1dev1id = UUID.fromString("08abcdef-1111-aaaa-ffff-11111111af01");
    UUID zone1dev2id = UUID.fromString("08abcdef-1111-aaaa-ffff-22222222af01");
    UUID zone2dev1id = UUID.fromString("08abcdef-2222-aaaa-ffff-11111111af01");
    UUID zone2dev2id = UUID.fromString("08abcdef-2222-aaaa-ffff-22222222af01");
    UUID zone3dev1id = UUID.fromString("08abcdef-3333-aaaa-ffff-11111111af01");
    UUID zone3dev2id = UUID.fromString("08abcdef-3333-aaaa-ffff-22222222af01");
    UUID zone4dev1id = UUID.fromString("08abcdef-4444-aaaa-ffff-11111111af01");
    UUID zone4dev2id = UUID.fromString("08abcdef-4444-aaaa-ffff-22222222af01");

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.addDeviceIdsItem(zone1dev1id);
    zone1.addDeviceIdsItem(zone1dev2id);
    ZoneInfo zone2 = new ZoneInfo();
    zone2.setZoneId(zone2id);
    zone2.setName(zone2name);
    zone2.setComment(zone2comment);
    zone2.addDeviceIdsItem(zone2dev1id);
    zone2.addDeviceIdsItem(zone2dev2id);
    ZoneInfo zone3 = new ZoneInfo();
    zone3.setZoneId(zone3id);
    zone3.setName(zone3name);
    zone3.setComment(zone3comment);
    zone3.addDeviceIdsItem(zone3dev1id);
    zone3.addDeviceIdsItem(zone3dev2id);
    ZoneInfo zone4 = new ZoneInfo();
    zone4.setZoneId(zone4id);
    zone4.setName(zone4name);
    zone4.setComment(zone4comment);
    zone4.addDeviceIdsItem(zone4dev1id);
    zone4.addDeviceIdsItem(zone4dev2id);

    Map<UUID, ZoneInfo> zoneMap = new HashMap<>();
    zoneMap.put(zone1id, zone1);
    zoneMap.put(zone2id, zone2);
    zoneMap.put(zone3id, zone3);
    zoneMap.put(zone4id, zone4);

    limit = zoneMap.size() + 1;

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(zoneMap).when(this.zoneRepository).findAll();

    // Call service
    CommonGetListResponse<ZoneInfo> retListResp = this.zoneService.getZones(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.zoneRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be sorted name ascending.", retListResp.getObjList(),
        contains(zone2, zone1, zone4, zone3));
  }

  @Test
  @DisplayName("TEST getZones (skip = 1, limit = 1)")
  void testGetZonesSkip1Limit1() {
    Integer skip = 1;
    Integer limit = 1;

    UUID zone1id = UUID.fromString("08abcdef-1111-1111-1111-111111111101");
    UUID zone2id = UUID.fromString("08abcdef-2222-2222-2222-222222222202");
    UUID zone3id = UUID.fromString("08abcdef-3333-3333-3333-333333333303");
    UUID zone4id = UUID.fromString("08abcdef-4444-4444-4444-444444444404");
    String zone1name = "B zone name Test";
    String zone2name = "A zone name Test";
    String zone3name = "D zone name Test";
    String zone4name = "C zone name Test";
    String zone1comment = "B zone comment Test";
    String zone2comment = "A zone comment Test";
    String zone3comment = "D zone comment Test";
    String zone4comment = "C zone comment Test";
    UUID zone1dev1id = UUID.fromString("08abcdef-1111-aaaa-ffff-11111111af01");
    UUID zone1dev2id = UUID.fromString("08abcdef-1111-aaaa-ffff-22222222af01");
    UUID zone2dev1id = UUID.fromString("08abcdef-2222-aaaa-ffff-11111111af01");
    UUID zone2dev2id = UUID.fromString("08abcdef-2222-aaaa-ffff-22222222af01");
    UUID zone3dev1id = UUID.fromString("08abcdef-3333-aaaa-ffff-11111111af01");
    UUID zone3dev2id = UUID.fromString("08abcdef-3333-aaaa-ffff-22222222af01");
    UUID zone4dev1id = UUID.fromString("08abcdef-4444-aaaa-ffff-11111111af01");
    UUID zone4dev2id = UUID.fromString("08abcdef-4444-aaaa-ffff-22222222af01");

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.addDeviceIdsItem(zone1dev1id);
    zone1.addDeviceIdsItem(zone1dev2id);
    ZoneInfo zone2 = new ZoneInfo();
    zone2.setZoneId(zone2id);
    zone2.setName(zone2name);
    zone2.setComment(zone2comment);
    zone2.addDeviceIdsItem(zone2dev1id);
    zone2.addDeviceIdsItem(zone2dev2id);
    ZoneInfo zone3 = new ZoneInfo();
    zone3.setZoneId(zone3id);
    zone3.setName(zone3name);
    zone3.setComment(zone3comment);
    zone3.addDeviceIdsItem(zone3dev1id);
    zone3.addDeviceIdsItem(zone3dev2id);
    ZoneInfo zone4 = new ZoneInfo();
    zone4.setZoneId(zone4id);
    zone4.setName(zone4name);
    zone4.setComment(zone4comment);
    zone4.addDeviceIdsItem(zone4dev1id);
    zone4.addDeviceIdsItem(zone4dev2id);

    Map<UUID, ZoneInfo> zoneMap = new HashMap<>();
    zoneMap.put(zone1id, zone1);
    zoneMap.put(zone2id, zone2);
    zoneMap.put(zone3id, zone3);
    zoneMap.put(zone4id, zone4);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(zoneMap).when(this.zoneRepository).findAll();

    // Call service
    CommonGetListResponse<ZoneInfo> retListResp = this.zoneService.getZones(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.zoneRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must only be one, the second entry only.",
        retListResp.getObjList(), contains(zone1));
  }

  @Test
  @DisplayName("TEST getZones (skip = 2)")
  void testGetZonesSkip2() {
    Integer skip = 2;
    Integer limit = null;

    UUID zone1id = UUID.fromString("08abcdef-1111-1111-1111-111111111101");
    UUID zone2id = UUID.fromString("08abcdef-2222-2222-2222-222222222202");
    UUID zone3id = UUID.fromString("08abcdef-3333-3333-3333-333333333303");
    UUID zone4id = UUID.fromString("08abcdef-4444-4444-4444-444444444404");
    String zone1name = "B zone name Test";
    String zone2name = "A zone name Test";
    String zone3name = "D zone name Test";
    String zone4name = "C zone name Test";
    String zone1comment = "B zone comment Test";
    String zone2comment = "A zone comment Test";
    String zone3comment = "D zone comment Test";
    String zone4comment = "C zone comment Test";
    UUID zone1dev1id = UUID.fromString("08abcdef-1111-aaaa-ffff-11111111af01");
    UUID zone1dev2id = UUID.fromString("08abcdef-1111-aaaa-ffff-22222222af01");
    UUID zone2dev1id = UUID.fromString("08abcdef-2222-aaaa-ffff-11111111af01");
    UUID zone2dev2id = UUID.fromString("08abcdef-2222-aaaa-ffff-22222222af01");
    UUID zone3dev1id = UUID.fromString("08abcdef-3333-aaaa-ffff-11111111af01");
    UUID zone3dev2id = UUID.fromString("08abcdef-3333-aaaa-ffff-22222222af01");
    UUID zone4dev1id = UUID.fromString("08abcdef-4444-aaaa-ffff-11111111af01");
    UUID zone4dev2id = UUID.fromString("08abcdef-4444-aaaa-ffff-22222222af01");

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.addDeviceIdsItem(zone1dev1id);
    zone1.addDeviceIdsItem(zone1dev2id);
    ZoneInfo zone2 = new ZoneInfo();
    zone2.setZoneId(zone2id);
    zone2.setName(zone2name);
    zone2.setComment(zone2comment);
    zone2.addDeviceIdsItem(zone2dev1id);
    zone2.addDeviceIdsItem(zone2dev2id);
    ZoneInfo zone3 = new ZoneInfo();
    zone3.setZoneId(zone3id);
    zone3.setName(zone3name);
    zone3.setComment(zone3comment);
    zone3.addDeviceIdsItem(zone3dev1id);
    zone3.addDeviceIdsItem(zone3dev2id);
    ZoneInfo zone4 = new ZoneInfo();
    zone4.setZoneId(zone4id);
    zone4.setName(zone4name);
    zone4.setComment(zone4comment);
    zone4.addDeviceIdsItem(zone4dev1id);
    zone4.addDeviceIdsItem(zone4dev2id);

    Map<UUID, ZoneInfo> zoneMap = new HashMap<>();
    zoneMap.put(zone1id, zone1);
    zoneMap.put(zone2id, zone2);
    zoneMap.put(zone3id, zone3);
    zoneMap.put(zone4id, zone4);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(zoneMap).when(this.zoneRepository).findAll();

    // Call service
    CommonGetListResponse<ZoneInfo> retListResp = this.zoneService.getZones(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.zoneRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The first two entries must not be included.", retListResp.getObjList(),
        contains(zone4, zone3));
  }

  @Test
  @DisplayName("TEST getZones (limit = 2)")
  void testGetZonesLimit2() {
    Integer skip = null;
    Integer limit = 2;

    UUID zone1id = UUID.fromString("08abcdef-1111-1111-1111-111111111101");
    UUID zone2id = UUID.fromString("08abcdef-2222-2222-2222-222222222202");
    UUID zone3id = UUID.fromString("08abcdef-3333-3333-3333-333333333303");
    UUID zone4id = UUID.fromString("08abcdef-4444-4444-4444-444444444404");
    String zone1name = "B zone name Test";
    String zone2name = "A zone name Test";
    String zone3name = "D zone name Test";
    String zone4name = "C zone name Test";
    String zone1comment = "B zone comment Test";
    String zone2comment = "A zone comment Test";
    String zone3comment = "D zone comment Test";
    String zone4comment = "C zone comment Test";
    UUID zone1dev1id = UUID.fromString("08abcdef-1111-aaaa-ffff-11111111af01");
    UUID zone1dev2id = UUID.fromString("08abcdef-1111-aaaa-ffff-22222222af01");
    UUID zone2dev1id = UUID.fromString("08abcdef-2222-aaaa-ffff-11111111af01");
    UUID zone2dev2id = UUID.fromString("08abcdef-2222-aaaa-ffff-22222222af01");
    UUID zone3dev1id = UUID.fromString("08abcdef-3333-aaaa-ffff-11111111af01");
    UUID zone3dev2id = UUID.fromString("08abcdef-3333-aaaa-ffff-22222222af01");
    UUID zone4dev1id = UUID.fromString("08abcdef-4444-aaaa-ffff-11111111af01");
    UUID zone4dev2id = UUID.fromString("08abcdef-4444-aaaa-ffff-22222222af01");

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.addDeviceIdsItem(zone1dev1id);
    zone1.addDeviceIdsItem(zone1dev2id);
    ZoneInfo zone2 = new ZoneInfo();
    zone2.setZoneId(zone2id);
    zone2.setName(zone2name);
    zone2.setComment(zone2comment);
    zone2.addDeviceIdsItem(zone2dev1id);
    zone2.addDeviceIdsItem(zone2dev2id);
    ZoneInfo zone3 = new ZoneInfo();
    zone3.setZoneId(zone3id);
    zone3.setName(zone3name);
    zone3.setComment(zone3comment);
    zone3.addDeviceIdsItem(zone3dev1id);
    zone3.addDeviceIdsItem(zone3dev2id);
    ZoneInfo zone4 = new ZoneInfo();
    zone4.setZoneId(zone4id);
    zone4.setName(zone4name);
    zone4.setComment(zone4comment);
    zone4.addDeviceIdsItem(zone4dev1id);
    zone4.addDeviceIdsItem(zone4dev2id);

    Map<UUID, ZoneInfo> zoneMap = new HashMap<>();
    zoneMap.put(zone1id, zone1);
    zoneMap.put(zone2id, zone2);
    zoneMap.put(zone3id, zone3);
    zoneMap.put(zone4id, zone4);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(zoneMap).when(this.zoneRepository).findAll();

    // Call service
    CommonGetListResponse<ZoneInfo> retListResp = this.zoneService.getZones(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.zoneRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must only be 2, sorted by name ascending.",
        retListResp.getObjList(), contains(zone2, zone1));
  }

  @Test
  @DisplayName("TEST getZones (No entries in the DB(null))")
  void testGetZonesNoEntryNull() {
    Integer skip = null;
    Integer limit = null;

    Map<UUID, ZoneInfo> zoneMap = null;

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(zoneMap).when(this.zoneRepository).findAll();

    // Call service
    CommonGetListResponse<ZoneInfo> retListResp = this.zoneService.getZones(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.zoneRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getZones (No entries in the DB)")
  void testGetZonesNoEntry() {
    Integer skip = null;
    Integer limit = null;

    Map<UUID, ZoneInfo> zoneMap = Collections.emptyMap();

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(zoneMap).when(this.zoneRepository).findAll();

    // Call service
    CommonGetListResponse<ZoneInfo> retListResp = this.zoneService.getZones(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.zoneRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getZoneById")
  void getZoneById() {
    UUID zone1id = UUID.fromString("08abcdef-1111-1111-1111-111111111101");
    String zone1name = "B zone name Test";
    String zone1comment = "B zone comment Test";
    UUID zone1dev1id = UUID.fromString("08abcdef-1111-aaaa-ffff-11111111af01");
    UUID zone1dev2id = UUID.fromString("08abcdef-1111-aaaa-ffff-22222222af01");

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.addDeviceIdsItem(zone1dev1id);
    zone1.addDeviceIdsItem(zone1dev2id);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.zoneRepository.findById(eq(zone1id))).thenReturn(zone1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call service
    CommonGetResponse<ZoneInfo> operResp = this.zoneService.getZoneById(zone1id);

    // Verify that appropriate Repository method was called
    verify(this.zoneRepository, times(1)).findById(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertEquals(zone1, operResp.getObj(), "Zone1 must be returned.");
    assertEquals(zone1id, operResp.getObj().getZoneId(), "Zone1's id must be returned.");
    assertEquals(zone1name, operResp.getObj().getName(), "Zone1's name must be returned.");
    assertEquals(zone1comment, operResp.getObj().getComment(), "Zone1's comment must be returned.");
    assertThat("The device List must be returned (in any order)", operResp.getObj().getDeviceIds(),
        containsInAnyOrder(zone1dev1id, zone1dev2id));
  }

  @Test
  @DisplayName("TEST getZoneById (zoneId not found in DB)")
  void getZoneByIdNotFound() {
    UUID zone1id = UUID.fromString("08abcdef-1111-1111-1111-111111111101");
    String zone1name = "B zone name Test";
    String zone1comment = "B zone comment Test";
    UUID zone1dev1id = UUID.fromString("08abcdef-1111-aaaa-ffff-11111111af01");
    UUID zone1dev2id = UUID.fromString("08abcdef-1111-aaaa-ffff-22222222af01");

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.addDeviceIdsItem(zone1dev1id);
    zone1.addDeviceIdsItem(zone1dev2id);

    HttpStatus expectedStatus = HttpStatus.NOT_FOUND;

    // Mock(s)
    when(this.zoneRepository.findById(eq(zone1id))).thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call service
    CommonGetResponse<ZoneInfo> operResp = this.zoneService.getZoneById(zone1id);

    // Verify that appropriate Repository method was called
    verify(this.zoneRepository, times(1)).findById(arg1.capture());

    // Assertions
    assertEquals(zone1id, arg1.getValue(), "Argument must match.");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 404.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getObj(), "The returned object must be empty.");
  }

  @Test
  @DisplayName("TEST registerZone")
  void getRegisterZone() {
    String zoneNewName = "a Zone Name";
    String zoneNewCommn = "a Zone Comment";

    ZoneCreation zoneAdd = new ZoneCreation();
    zoneAdd.setName(zoneNewName);
    zoneAdd.setComment(zoneNewCommn);
    zoneAdd.setDeviceIds(Collections.emptyList());

    UUID zone1id = UUID.fromString("55abcdef-1111-1111-1111-111111111101");
    String zone1name = "B zone name Test";
    String zone1comment = "B zone comment Test";

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.setDeviceIds(Collections.emptyList());

    Map<UUID, ZoneInfo> zoneMap = new HashMap<>();
    zoneMap.put(zone1id, zone1);

    HttpStatus expectedStatus = HttpStatus.CREATED;

    // Mock(s)
    doReturn(zoneMap).when(this.zoneRepository).findAll();
    when(this.zoneRepository.findById(any(UUID.class))).thenReturn(new ZoneInfo()).thenReturn(null);

    // Argument Captors
    ArgumentCaptor<ZoneInfo> arg1 = ArgumentCaptor.forClass(ZoneInfo.class);

    // Call service
    CommonAddResponse operResp = this.zoneService.registerZone(zoneAdd);

    // Verify that appropriate Repository method was called
    verify(this.zoneRepository, times(1)).add(arg1.capture());

    // Assertions
    assertNotNull(arg1.getValue(), "Argument (ZoneInfo) must not be null.");
    UUID newZoneId = arg1.getValue().getZoneId();
    assertNotNull(newZoneId, "Argument's zoneId must not be null.");
    assertEquals(zoneNewName, arg1.getValue().getName(), "Argument's name must match.");
    assertEquals(zoneNewCommn, arg1.getValue().getComment(), "Argument's comment must match.");
    assertThat("Argument's deviceId List must be empty.", arg1.getValue().getDeviceIds(),
        IsEmptyCollection.empty());
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 201.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNotNull(operResp.getId(), "Returned zoneId must not be null.");
    assertEquals(newZoneId, operResp.getId(),
        "Returned zoneId must match the id passed to the repo.");
  }

  @Test
  @DisplayName("TEST registerZone (deviceIds is null)")
  void getRegisterZoneDeviceIdListNull() {
    String zoneNewName = "a Zone Name";
    String zoneNewCommn = "a Zone Comment";

    ZoneCreation zoneAdd = new ZoneCreation();
    zoneAdd.setName(zoneNewName);
    zoneAdd.setComment(zoneNewCommn);
    zoneAdd.setDeviceIds(null);

    UUID zone1id = UUID.fromString("55abcdef-1111-1111-1111-111111111101");
    String zone1name = "B zone name Test";
    String zone1comment = "B zone comment Test";

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.setDeviceIds(Collections.emptyList());

    Map<UUID, ZoneInfo> zoneMap = new HashMap<>();
    zoneMap.put(zone1id, zone1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    doReturn(zoneMap).when(this.zoneRepository).findAll();

    // Argument Captors
    ArgumentCaptor<ZoneInfo> arg1 = ArgumentCaptor.forClass(ZoneInfo.class);

    // Call service
    CommonAddResponse operResp = this.zoneService.registerZone(zoneAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.zoneRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned zoneId must be null.");
  }

  @Test
  @DisplayName("TEST registerZone (deviceIds contains null)")
  void getRegisterZoneDeviceIdListContainsNull() {
    UUID device1id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111101");
    UUID device2id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111102");
    UUID device3id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111103");

    List<UUID> deviceIds = new ArrayList<>();
    deviceIds.add(device1id);
    deviceIds.add(device2id);
    deviceIds.add(null);
    deviceIds.add(device3id);

    String zoneNewName = "a Zone Name";
    String zoneNewCommn = "a Zone Comment";

    ZoneCreation zoneAdd = new ZoneCreation();
    zoneAdd.setName(zoneNewName);
    zoneAdd.setComment(zoneNewCommn);
    zoneAdd.setDeviceIds(deviceIds);

    UUID zone1id = UUID.fromString("55abcdef-1111-1111-1111-111111111101");
    String zone1name = "B zone name Test";
    String zone1comment = "B zone comment Test";

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.setDeviceIds(Collections.emptyList());

    Map<UUID, ZoneInfo> zoneMap = new HashMap<>();
    zoneMap.put(zone1id, zone1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    doReturn(zoneMap).when(this.zoneRepository).findAll();

    // Argument Captors
    ArgumentCaptor<ZoneInfo> arg1 = ArgumentCaptor.forClass(ZoneInfo.class);

    // Call service
    CommonAddResponse operResp = this.zoneService.registerZone(zoneAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.zoneRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned zoneId must be null.");
  }

  @Test
  @DisplayName("TEST registerZone (deviceIds contains duplicate entry)")
  void getRegisterZoneDeviceIdListContainsDuplicate() {
    UUID device1id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111101");
    UUID device2id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111102");
    UUID device3id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111103");

    List<UUID> deviceIds = new ArrayList<>();
    deviceIds.add(device1id);
    deviceIds.add(device2id);
    deviceIds.add(device1id);
    deviceIds.add(device3id);

    String zoneNewName = "a Zone Name";
    String zoneNewCommn = "a Zone Comment";

    ZoneCreation zoneAdd = new ZoneCreation();
    zoneAdd.setName(zoneNewName);
    zoneAdd.setComment(zoneNewCommn);
    zoneAdd.setDeviceIds(deviceIds);

    UUID zone1id = UUID.fromString("55abcdef-1111-1111-1111-111111111101");
    String zone1name = "B zone name Test";
    String zone1comment = "B zone comment Test";

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.setDeviceIds(Collections.emptyList());

    Map<UUID, ZoneInfo> zoneMap = new HashMap<>();
    zoneMap.put(zone1id, zone1);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    doReturn(zoneMap).when(this.zoneRepository).findAll();

    // Argument Captors
    ArgumentCaptor<ZoneInfo> arg1 = ArgumentCaptor.forClass(ZoneInfo.class);

    // Call service
    CommonAddResponse operResp = this.zoneService.registerZone(zoneAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.zoneRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned zoneId must be null.");
  }

  @SuppressWarnings("unchecked")
  @Test
  @DisplayName("TEST registerZone (device multi-lookup returns null)")
  void getRegisterZoneDeviceIdListMultiLookupReturnsNull() {
    UUID device1id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111101");
    UUID device2id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111102");
    UUID device3id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111103");

    List<UUID> deviceIds = new ArrayList<>();
    deviceIds.add(device1id);
    deviceIds.add(device2id);
    deviceIds.add(device3id);

    String zoneNewName = "a Zone Name";
    String zoneNewCommn = "a Zone Comment";

    ZoneCreation zoneAdd = new ZoneCreation();
    zoneAdd.setName(zoneNewName);
    zoneAdd.setComment(zoneNewCommn);
    zoneAdd.setDeviceIds(deviceIds);

    UUID zone1id = UUID.fromString("55abcdef-1111-1111-1111-111111111101");
    String zone1name = "B zone name Test";
    String zone1comment = "B zone comment Test";

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.setDeviceIds(Collections.emptyList());

    Map<UUID, ZoneInfo> zoneMap = new HashMap<>();
    zoneMap.put(zone1id, zone1);

    List<DeviceInfo> deviceList = null;

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot register zone";
    String expectedDetail = "There are no existing devices for this zone.";

    // Mock(s)
    doReturn(zoneMap).when(this.zoneRepository).findAll();
    when(this.deviceRepository.findManyById(any(Set.class))).thenReturn(deviceList);

    // Argument Captors
    ArgumentCaptor<ZoneInfo> arg1 = ArgumentCaptor.forClass(ZoneInfo.class);

    // Call service
    CommonAddResponse operResp = this.zoneService.registerZone(zoneAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.zoneRepository, times(0)).add(arg1.capture());

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
    assertNull(operResp.getId(), "There should be no zoneId returned.");
  }

  @SuppressWarnings("unchecked")
  @Test
  @DisplayName("TEST registerZone (device multi-lookup returns empty)")
  void getRegisterZoneDeviceIdListMultiLookupReturnsEmpty() {
    UUID device1id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111101");
    UUID device2id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111102");
    UUID device3id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111103");

    List<UUID> deviceIds = new ArrayList<>();
    deviceIds.add(device1id);
    deviceIds.add(device2id);
    deviceIds.add(device3id);

    String zoneNewName = "a Zone Name";
    String zoneNewCommn = "a Zone Comment";

    ZoneCreation zoneAdd = new ZoneCreation();
    zoneAdd.setName(zoneNewName);
    zoneAdd.setComment(zoneNewCommn);
    zoneAdd.setDeviceIds(deviceIds);

    UUID zone1id = UUID.fromString("55abcdef-1111-1111-1111-111111111101");
    String zone1name = "B zone name Test";
    String zone1comment = "B zone comment Test";

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.setDeviceIds(Collections.emptyList());

    Map<UUID, ZoneInfo> zoneMap = new HashMap<>();
    zoneMap.put(zone1id, zone1);

    List<DeviceInfo> deviceList = Collections.emptyList();

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot register zone";
    String expectedDetail = "There are no existing devices for this zone.";

    // Mock(s)
    doReturn(zoneMap).when(this.zoneRepository).findAll();
    when(this.deviceRepository.findManyById(any(Set.class))).thenReturn(deviceList);

    // Argument Captors
    ArgumentCaptor<ZoneInfo> arg1 = ArgumentCaptor.forClass(ZoneInfo.class);

    // Call service
    CommonAddResponse operResp = this.zoneService.registerZone(zoneAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.zoneRepository, times(0)).add(arg1.capture());

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
    assertNull(operResp.getId(), "There should be no zoneId returned.");
  }

  @SuppressWarnings("unchecked")
  @Test
  @DisplayName("TEST registerZone (device multi-lookup returns a list containing null)")
  void getRegisterZoneDeviceIdListMultiLookupReturnsContainingNull() {
    UUID device1id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111101");
    UUID device2id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111102");
    UUID device3id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111103");
    String dev1name = "Device 1";
    String dev2name = "Device 2";
    String dev3name = "Device 3";
    boolean dev1isReg = true;
    boolean dev2isReg = true;
    boolean dev3isReg = true;

    List<UUID> deviceIds = new ArrayList<>();
    deviceIds.add(device1id);
    deviceIds.add(device2id);
    deviceIds.add(device3id);

    String zoneNewName = "a Zone Name";
    String zoneNewCommn = "a Zone Comment";

    ZoneCreation zoneAdd = new ZoneCreation();
    zoneAdd.setName(zoneNewName);
    zoneAdd.setComment(zoneNewCommn);
    zoneAdd.setDeviceIds(deviceIds);

    UUID zone1id = UUID.fromString("55abcdef-1111-1111-1111-111111111101");
    String zone1name = "B zone name Test";
    String zone1comment = "B zone comment Test";

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.setDeviceIds(Collections.emptyList());

    Map<UUID, ZoneInfo> zoneMap = new HashMap<>();
    zoneMap.put(zone1id, zone1);

    DeviceInfo dev1 = new DeviceInfo().deviceId(device1id).name(dev1name).isRegistered(dev1isReg);
    DeviceInfo dev2 = new DeviceInfo().deviceId(device2id).name(dev2name).isRegistered(dev2isReg);
    DeviceInfo dev3 = new DeviceInfo().deviceId(device3id).name(dev3name).isRegistered(dev3isReg);
    List<DeviceInfo> deviceList = new ArrayList<>();
    deviceList.add(dev1);
    deviceList.add(null);
    deviceList.add(dev2);
    deviceList.add(dev3);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot register zone";
    String expectedDetail = "One or more devices in this zone do not exist.";

    // Mock(s)
    doReturn(zoneMap).when(this.zoneRepository).findAll();
    when(this.deviceRepository.findManyById(any(Set.class))).thenReturn(deviceList);

    // Argument Captors
    ArgumentCaptor<ZoneInfo> arg1 = ArgumentCaptor.forClass(ZoneInfo.class);

    // Call service
    CommonAddResponse operResp = this.zoneService.registerZone(zoneAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.zoneRepository, times(0)).add(arg1.capture());

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
    assertNull(operResp.getId(), "There should be no zoneId returned.");
  }

  @SuppressWarnings("unchecked")
  @Test
  @DisplayName("TEST registerZone (deviceIds has some unregistered devices)")
  void getRegisterZoneDeviceIdListContainsUnregistered() {
    UUID device1id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111101");
    UUID device2id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111102");
    UUID device3id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111103");
    String dev1name = "Device 1";
    String dev2name = "Device 2";
    String dev3name = "Device 3";
    boolean dev1isReg = true;
    boolean dev2isReg = false;
    boolean dev3isReg = true;

    List<UUID> deviceIds = new ArrayList<>();
    deviceIds.add(device1id);
    deviceIds.add(device2id);
    deviceIds.add(device3id);

    String zoneNewName = "a Zone Name";
    String zoneNewCommn = "a Zone Comment";

    ZoneCreation zoneAdd = new ZoneCreation();
    zoneAdd.setName(zoneNewName);
    zoneAdd.setComment(zoneNewCommn);
    zoneAdd.setDeviceIds(deviceIds);

    UUID zone1id = UUID.fromString("55abcdef-1111-1111-1111-111111111101");
    String zone1name = "B zone name Test";
    String zone1comment = "B zone comment Test";

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.setDeviceIds(Collections.emptyList());

    Map<UUID, ZoneInfo> zoneMap = new HashMap<>();
    zoneMap.put(zone1id, zone1);

    DeviceInfo dev1 = new DeviceInfo().deviceId(device1id).name(dev1name).isRegistered(dev1isReg);
    DeviceInfo dev2 = new DeviceInfo().deviceId(device2id).name(dev2name).isRegistered(dev2isReg);
    DeviceInfo dev3 = new DeviceInfo().deviceId(device3id).name(dev3name).isRegistered(dev3isReg);
    List<DeviceInfo> deviceList = new ArrayList<>();
    deviceList.add(dev1);
    deviceList.add(dev2);
    deviceList.add(dev3);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot register zone";
    String expectedDetail = "One or more devices in this zone are not registered.";

    // Mock(s)
    doReturn(zoneMap).when(this.zoneRepository).findAll();
    when(this.deviceRepository.findManyById(any(Set.class))).thenReturn(deviceList);

    // Argument Captors
    ArgumentCaptor<ZoneInfo> arg1 = ArgumentCaptor.forClass(ZoneInfo.class);

    // Call service
    CommonAddResponse operResp = this.zoneService.registerZone(zoneAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.zoneRepository, times(0)).add(arg1.capture());

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
    assertNull(operResp.getId(), "There should be no zoneId returned.");
  }

  @SuppressWarnings("unchecked")
  @Test
  @DisplayName("TEST registerZone (deviceIds are all valid and registered)")
  void getRegisterZoneDeviceIdListRegistered() {
    UUID device1id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111101");
    UUID device2id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111102");
    UUID device3id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111103");
    String dev1name = "Device 1";
    String dev2name = "Device 2";
    String dev3name = "Device 3";
    boolean dev1isReg = true;
    boolean dev2isReg = true;
    boolean dev3isReg = true;

    List<UUID> deviceIds = new ArrayList<>();
    deviceIds.add(device1id);
    deviceIds.add(device2id);
    deviceIds.add(device3id);

    String zoneNewName = "a Zone Name";
    String zoneNewCommn = "a Zone Comment";

    ZoneCreation zoneAdd = new ZoneCreation();
    zoneAdd.setName(zoneNewName);
    zoneAdd.setComment(zoneNewCommn);
    zoneAdd.setDeviceIds(deviceIds);

    UUID zone1id = UUID.fromString("55abcdef-1111-1111-1111-111111111101");
    String zone1name = "B zone name Test";
    String zone1comment = "B zone comment Test";

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.setDeviceIds(Collections.emptyList());

    Map<UUID, ZoneInfo> zoneMap = new HashMap<>();
    zoneMap.put(zone1id, zone1);

    DeviceInfo dev1 = new DeviceInfo().deviceId(device1id).name(dev1name).isRegistered(dev1isReg);
    DeviceInfo dev2 = new DeviceInfo().deviceId(device2id).name(dev2name).isRegistered(dev2isReg);
    DeviceInfo dev3 = new DeviceInfo().deviceId(device3id).name(dev3name).isRegistered(dev3isReg);
    List<DeviceInfo> deviceList = new ArrayList<>();
    deviceList.add(dev1);
    deviceList.add(dev2);
    deviceList.add(dev3);

    HttpStatus expectedStatus = HttpStatus.CREATED;

    // Mock(s)
    doReturn(zoneMap).when(this.zoneRepository).findAll();
    when(this.deviceRepository.findManyById(any(Set.class))).thenReturn(deviceList);

    // Argument Captors
    ArgumentCaptor<ZoneInfo> arg1 = ArgumentCaptor.forClass(ZoneInfo.class);

    // Call service
    CommonAddResponse operResp = this.zoneService.registerZone(zoneAdd);

    // Verify that appropriate Repository method was called
    verify(this.zoneRepository, times(1)).add(arg1.capture());

    // Assertions
    assertNotNull(arg1.getValue(), "Argument (ZoneInfo) must not be null.");
    UUID newZoneId = arg1.getValue().getZoneId();
    assertNotNull(newZoneId, "Argument's zoneId must not be null.");
    assertEquals(zoneNewName, arg1.getValue().getName(), "Argument's name must match.");
    assertEquals(zoneNewCommn, arg1.getValue().getComment(), "Argument's comment must match.");
    assertThat("Argument's deviceId List must match.", arg1.getValue().getDeviceIds(),
        contains(device1id, device2id, device3id));
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 201.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNotNull(operResp.getId(), "Returned zoneId must not be null.");
    assertEquals(newZoneId, operResp.getId(),
        "Returned zoneId must match the id passed to the repo.");
  }

  @Test
  @DisplayName("TEST registerZone (there are no existing zones (null))")
  void getRegisterZoneNoEntryNull() {
    String zoneNewName = "a Zone Name";
    String zoneNewCommn = "a Zone Comment";

    ZoneCreation zoneAdd = new ZoneCreation();
    zoneAdd.setName(zoneNewName);
    zoneAdd.setComment(zoneNewCommn);
    zoneAdd.setDeviceIds(Collections.emptyList());

    UUID zone1id = UUID.fromString("55abcdef-1111-1111-1111-111111111101");
    String zone1name = "B zone name Test";
    String zone1comment = "B zone comment Test";

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.setDeviceIds(Collections.emptyList());

    Map<UUID, ZoneInfo> zoneMap = null;

    HttpStatus expectedStatus = HttpStatus.CREATED;

    // Mock(s)
    doReturn(zoneMap).when(this.zoneRepository).findAll();

    // Argument Captors
    ArgumentCaptor<ZoneInfo> arg1 = ArgumentCaptor.forClass(ZoneInfo.class);

    // Call service
    CommonAddResponse operResp = this.zoneService.registerZone(zoneAdd);

    // Verify that appropriate Repository method was called
    verify(this.zoneRepository, times(1)).add(arg1.capture());

    // Assertions
    assertNotNull(arg1.getValue(), "Argument (ZoneInfo) must not be null.");
    UUID newZoneId = arg1.getValue().getZoneId();
    assertNotNull(newZoneId, "Argument's zoneId must not be null.");
    assertEquals(zoneNewName, arg1.getValue().getName(), "Argument's name must match.");
    assertEquals(zoneNewCommn, arg1.getValue().getComment(), "Argument's comment must match.");
    assertThat("Argument's deviceId List must be empty.", arg1.getValue().getDeviceIds(),
        IsEmptyCollection.empty());
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 201.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNotNull(operResp.getId(), "Returned zoneId must not be null.");
    assertEquals(newZoneId, operResp.getId(),
        "Returned zoneId must match the id passed to the repo.");
  }

  @Test
  @DisplayName("TEST registerZone (there are no existing zones)")
  void getRegisterZoneNoEntry() {
    String zoneNewName = "a Zone Name";
    String zoneNewCommn = "a Zone Comment";

    ZoneCreation zoneAdd = new ZoneCreation();
    zoneAdd.setName(zoneNewName);
    zoneAdd.setComment(zoneNewCommn);
    zoneAdd.setDeviceIds(Collections.emptyList());

    UUID zone1id = UUID.fromString("55abcdef-1111-1111-1111-111111111101");
    String zone1name = "B zone name Test";
    String zone1comment = "B zone comment Test";

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.setDeviceIds(Collections.emptyList());

    Map<UUID, ZoneInfo> zoneMap = Collections.emptyMap();

    HttpStatus expectedStatus = HttpStatus.CREATED;

    // Mock(s)
    doReturn(zoneMap).when(this.zoneRepository).findAll();

    // Argument Captors
    ArgumentCaptor<ZoneInfo> arg1 = ArgumentCaptor.forClass(ZoneInfo.class);

    // Call service
    CommonAddResponse operResp = this.zoneService.registerZone(zoneAdd);

    // Verify that appropriate Repository method was called
    verify(this.zoneRepository, times(1)).add(arg1.capture());

    // Assertions
    assertNotNull(arg1.getValue(), "Argument (ZoneInfo) must not be null.");
    UUID newZoneId = arg1.getValue().getZoneId();
    assertNotNull(newZoneId, "Argument's zoneId must not be null.");
    assertEquals(zoneNewName, arg1.getValue().getName(), "Argument's name must match.");
    assertEquals(zoneNewCommn, arg1.getValue().getComment(), "Argument's comment must match.");
    assertThat("Argument's deviceId List must be empty.", arg1.getValue().getDeviceIds(),
        IsEmptyCollection.empty());
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 201.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNotNull(operResp.getId(), "Returned zoneId must not be null.");
    assertEquals(newZoneId, operResp.getId(),
        "Returned zoneId must match the id passed to the repo.");
  }

  @Test
  @DisplayName("TEST registerZone (zone name already exists in a different zone)")
  void getRegisterZoneZoneNameExists() {
    String zoneNewCommn = "a Zone Comment";

    UUID zone1id = UUID.fromString("55abcdef-1111-1111-1111-111111111101");
    String zone1name = "B zone name Test";
    String zone1comment = "B zone comment Test";

    ZoneCreation zoneAdd = new ZoneCreation();
    zoneAdd.setName(zone1name);
    zoneAdd.setComment(zoneNewCommn);
    zoneAdd.setDeviceIds(Collections.emptyList());

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.setDeviceIds(Collections.emptyList());

    Map<UUID, ZoneInfo> zoneMap = new HashMap<>();
    zoneMap.put(zone1id, zone1);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    doReturn(zoneMap).when(this.zoneRepository).findAll();

    // Argument Captors
    ArgumentCaptor<ZoneInfo> arg1 = ArgumentCaptor.forClass(ZoneInfo.class);

    // Call service
    CommonAddResponse operResp = this.zoneService.registerZone(zoneAdd);

    // Verify that appropriate Repository method was NOT called
    verify(this.zoneRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned zoneId must be null.");
  }

  @Test
  @DisplayName("TEST modifyZone")
  void getModifyZone() {
    UUID device1id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111101");
    UUID device2id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111102");
    UUID device3id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111103");

    String zoneNewName = "an updated Zone Name";
    String zoneNewCommn = "an updated Zone Comment";

    ZoneModification zoneMod = new ZoneModification();
    zoneMod.setName(zoneNewName);
    zoneMod.setComment(zoneNewCommn);
    zoneMod.setDeviceIds(Collections.emptyList());

    UUID zone1id = UUID.fromString("55abcdef-1111-1111-1111-111111111101");
    UUID zone2id = UUID.fromString("55abcdef-1111-1111-2222-111111111102");
    String zone1name = "B zone name Test";
    String zone1comment = "B zone comment Test";
    String zone2name = "A zone name Test";
    String zone2comment = "A zone comment Test";
    List<UUID> zone1deviceIds = new ArrayList<>();
    zone1deviceIds.add(device1id);
    zone1deviceIds.add(device2id);
    zone1deviceIds.add(device3id);

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.setDeviceIds(zone1deviceIds);
    ZoneInfo zone2 = new ZoneInfo();
    zone2.setZoneId(zone2id);
    zone2.setName(zone2name);
    zone2.setComment(zone2comment);
    zone2.setDeviceIds(Collections.emptyList());

    Map<UUID, ZoneInfo> zoneMap = new HashMap<>();
    zoneMap.put(zone1id, zone1);
    zoneMap.put(zone2id, zone2);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(zoneMap).when(this.zoneRepository).findAll();

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ZoneInfo> arg2 = ArgumentCaptor.forClass(ZoneInfo.class);

    // Call service
    CommonOperResponse operResp = this.zoneService.modifyZone(zone1id, zoneMod);

    // Verify that appropriate Repository method was called
    verify(this.zoneRepository, times(1)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertEquals(zone1id, arg1.getValue(), "Argument must match.");
    assertNotNull(arg2.getValue(), "Argument (ZoneInfo) must not be null.");
    assertEquals(zone1id, arg2.getValue().getZoneId(), "Argument's zoneId must match.");
    assertEquals(zoneNewName, arg2.getValue().getName(), "Argument's name must match.");
    assertEquals(zoneNewCommn, arg2.getValue().getComment(), "Argument's comment must match.");
    assertThat("Argument's deviceId List must be empty.", arg2.getValue().getDeviceIds(),
        IsEmptyCollection.empty());
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyZone (deviceIds is null)")
  void getModifyZoneDeviceIdListNull() {
    UUID device1id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111101");
    UUID device2id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111102");
    UUID device3id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111103");

    String zoneNewName = "an updated Zone Name";
    String zoneNewCommn = "an updated Zone Comment";

    ZoneModification zoneMod = new ZoneModification();
    zoneMod.setName(zoneNewName);
    zoneMod.setComment(zoneNewCommn);
    zoneMod.setDeviceIds(null);

    UUID zone1id = UUID.fromString("55abcdef-1111-1111-1111-111111111101");
    UUID zone2id = UUID.fromString("55abcdef-1111-1111-2222-111111111102");
    String zone1name = "B zone name Test";
    String zone1comment = "B zone comment Test";
    String zone2name = "A zone name Test";
    String zone2comment = "A zone comment Test";
    List<UUID> zone1deviceIds = new ArrayList<>();
    zone1deviceIds.add(device1id);
    zone1deviceIds.add(device2id);
    zone1deviceIds.add(device3id);

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.setDeviceIds(zone1deviceIds);
    ZoneInfo zone2 = new ZoneInfo();
    zone2.setZoneId(zone2id);
    zone2.setName(zone2name);
    zone2.setComment(zone2comment);
    zone2.setDeviceIds(Collections.emptyList());

    Map<UUID, ZoneInfo> zoneMap = new HashMap<>();
    zoneMap.put(zone1id, zone1);
    zoneMap.put(zone2id, zone2);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    doReturn(zoneMap).when(this.zoneRepository).findAll();

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ZoneInfo> arg2 = ArgumentCaptor.forClass(ZoneInfo.class);

    // Call service
    CommonOperResponse operResp = this.zoneService.modifyZone(zone1id, zoneMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.zoneRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyZone (deviceIds contains null)")
  void getModifyZoneDeviceIdListContainsNull() {
    UUID device1id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111101");
    UUID device2id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111102");
    UUID device3id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111103");

    String zoneNewName = "an updated Zone Name";
    String zoneNewCommn = "an updated Zone Comment";

    List<UUID> deviceIds = new ArrayList<>();
    deviceIds.add(device1id);
    deviceIds.add(device2id);
    deviceIds.add(null);
    deviceIds.add(device3id);

    ZoneModification zoneMod = new ZoneModification();
    zoneMod.setName(zoneNewName);
    zoneMod.setComment(zoneNewCommn);
    zoneMod.setDeviceIds(deviceIds);

    UUID zone1id = UUID.fromString("55abcdef-1111-1111-1111-111111111101");
    UUID zone2id = UUID.fromString("55abcdef-1111-1111-2222-111111111102");
    String zone1name = "B zone name Test";
    String zone1comment = "B zone comment Test";
    String zone2name = "A zone name Test";
    String zone2comment = "A zone comment Test";
    List<UUID> zone1deviceIds = new ArrayList<>();
    zone1deviceIds.add(device1id);
    zone1deviceIds.add(device2id);
    zone1deviceIds.add(device3id);

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.setDeviceIds(zone1deviceIds);
    ZoneInfo zone2 = new ZoneInfo();
    zone2.setZoneId(zone2id);
    zone2.setName(zone2name);
    zone2.setComment(zone2comment);
    zone2.setDeviceIds(Collections.emptyList());

    Map<UUID, ZoneInfo> zoneMap = new HashMap<>();
    zoneMap.put(zone1id, zone1);
    zoneMap.put(zone2id, zone2);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    doReturn(zoneMap).when(this.zoneRepository).findAll();

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ZoneInfo> arg2 = ArgumentCaptor.forClass(ZoneInfo.class);

    // Call service
    CommonOperResponse operResp = this.zoneService.modifyZone(zone1id, zoneMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.zoneRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyZone (deviceIds contains duplicate entry)")
  void getModifyZoneDeviceIdListContainsDuplicate() {
    UUID device1id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111101");
    UUID device2id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111102");
    UUID device3id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111103");

    String zoneNewName = "an updated Zone Name";
    String zoneNewCommn = "an updated Zone Comment";

    List<UUID> deviceIds = new ArrayList<>();
    deviceIds.add(device1id);
    deviceIds.add(device2id);
    deviceIds.add(device1id);
    deviceIds.add(device3id);

    ZoneModification zoneMod = new ZoneModification();
    zoneMod.setName(zoneNewName);
    zoneMod.setComment(zoneNewCommn);
    zoneMod.setDeviceIds(deviceIds);

    UUID zone1id = UUID.fromString("55abcdef-1111-1111-1111-111111111101");
    UUID zone2id = UUID.fromString("55abcdef-1111-1111-2222-111111111102");
    String zone1name = "B zone name Test";
    String zone1comment = "B zone comment Test";
    String zone2name = "A zone name Test";
    String zone2comment = "A zone comment Test";
    List<UUID> zone1deviceIds = new ArrayList<>();
    zone1deviceIds.add(device1id);
    zone1deviceIds.add(device2id);
    zone1deviceIds.add(device3id);

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.setDeviceIds(zone1deviceIds);
    ZoneInfo zone2 = new ZoneInfo();
    zone2.setZoneId(zone2id);
    zone2.setName(zone2name);
    zone2.setComment(zone2comment);
    zone2.setDeviceIds(Collections.emptyList());

    Map<UUID, ZoneInfo> zoneMap = new HashMap<>();
    zoneMap.put(zone1id, zone1);
    zoneMap.put(zone2id, zone2);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    doReturn(zoneMap).when(this.zoneRepository).findAll();

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ZoneInfo> arg2 = ArgumentCaptor.forClass(ZoneInfo.class);

    // Call service
    CommonOperResponse operResp = this.zoneService.modifyZone(zone1id, zoneMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.zoneRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @SuppressWarnings("unchecked")
  @Test
  @DisplayName("TEST modifyZone (device multi-lookup returns null)")
  void getModifyZoneDeviceIdListMultiLookupReturnsNull() {
    UUID device1id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111101");
    UUID device2id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111102");
    UUID device3id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111103");

    String zoneNewName = "an updated Zone Name";
    String zoneNewCommn = "an updated Zone Comment";

    List<UUID> deviceIds = new ArrayList<>();
    deviceIds.add(device1id);
    deviceIds.add(device2id);
    deviceIds.add(device3id);

    ZoneModification zoneMod = new ZoneModification();
    zoneMod.setName(zoneNewName);
    zoneMod.setComment(zoneNewCommn);
    zoneMod.setDeviceIds(deviceIds);

    UUID zone1id = UUID.fromString("55abcdef-1111-1111-1111-111111111101");
    UUID zone2id = UUID.fromString("55abcdef-1111-1111-2222-111111111102");
    String zone1name = "B zone name Test";
    String zone1comment = "B zone comment Test";
    String zone2name = "A zone name Test";
    String zone2comment = "A zone comment Test";
    List<UUID> zone1deviceIds = new ArrayList<>();
    zone1deviceIds.add(device1id);
    zone1deviceIds.add(device2id);
    zone1deviceIds.add(device3id);

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.setDeviceIds(zone1deviceIds);
    ZoneInfo zone2 = new ZoneInfo();
    zone2.setZoneId(zone2id);
    zone2.setName(zone2name);
    zone2.setComment(zone2comment);
    zone2.setDeviceIds(Collections.emptyList());

    Map<UUID, ZoneInfo> zoneMap = new HashMap<>();
    zoneMap.put(zone1id, zone1);
    zoneMap.put(zone2id, zone2);

    List<DeviceInfo> deviceList = null;

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot modify zone";
    String expectedDetail = "There are no existing devices for this zone.";

    // Mock(s)
    doReturn(zoneMap).when(this.zoneRepository).findAll();
    when(this.deviceRepository.findManyById(any(Set.class))).thenReturn(deviceList);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ZoneInfo> arg2 = ArgumentCaptor.forClass(ZoneInfo.class);

    // Call service
    CommonOperResponse operResp = this.zoneService.modifyZone(zone1id, zoneMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.zoneRepository, times(0)).update(arg1.capture(), arg2.capture());

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

  @SuppressWarnings("unchecked")
  @Test
  @DisplayName("TEST modifyZone (device multi-lookup returns empty)")
  void getModifyZoneDeviceIdListMultiLookupReturnsEmpty() {
    UUID device1id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111101");
    UUID device2id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111102");
    UUID device3id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111103");

    String zoneNewName = "an updated Zone Name";
    String zoneNewCommn = "an updated Zone Comment";

    List<UUID> deviceIds = new ArrayList<>();
    deviceIds.add(device1id);
    deviceIds.add(device2id);
    deviceIds.add(device3id);

    ZoneModification zoneMod = new ZoneModification();
    zoneMod.setName(zoneNewName);
    zoneMod.setComment(zoneNewCommn);
    zoneMod.setDeviceIds(deviceIds);

    UUID zone1id = UUID.fromString("55abcdef-1111-1111-1111-111111111101");
    UUID zone2id = UUID.fromString("55abcdef-1111-1111-2222-111111111102");
    String zone1name = "B zone name Test";
    String zone1comment = "B zone comment Test";
    String zone2name = "A zone name Test";
    String zone2comment = "A zone comment Test";
    List<UUID> zone1deviceIds = new ArrayList<>();
    zone1deviceIds.add(device1id);
    zone1deviceIds.add(device2id);
    zone1deviceIds.add(device3id);

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.setDeviceIds(zone1deviceIds);
    ZoneInfo zone2 = new ZoneInfo();
    zone2.setZoneId(zone2id);
    zone2.setName(zone2name);
    zone2.setComment(zone2comment);
    zone2.setDeviceIds(Collections.emptyList());

    Map<UUID, ZoneInfo> zoneMap = new HashMap<>();
    zoneMap.put(zone1id, zone1);
    zoneMap.put(zone2id, zone2);

    List<DeviceInfo> deviceList = Collections.emptyList();

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot modify zone";
    String expectedDetail = "There are no existing devices for this zone.";

    // Mock(s)
    doReturn(zoneMap).when(this.zoneRepository).findAll();
    when(this.deviceRepository.findManyById(any(Set.class))).thenReturn(deviceList);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ZoneInfo> arg2 = ArgumentCaptor.forClass(ZoneInfo.class);

    // Call service
    CommonOperResponse operResp = this.zoneService.modifyZone(zone1id, zoneMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.zoneRepository, times(0)).update(arg1.capture(), arg2.capture());

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

  @SuppressWarnings("unchecked")
  @Test
  @DisplayName("TEST modifyZone (device multi-lookup returns a list containing null)")
  void getModifyZoneDeviceIdListMultiLookupReturnsContainingNull() {
    UUID device1id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111101");
    UUID device2id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111102");
    UUID device3id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111103");
    String dev1name = "Device 1";
    String dev2name = "Device 2";
    String dev3name = "Device 3";
    boolean dev1isReg = true;
    boolean dev2isReg = true;
    boolean dev3isReg = true;

    String zoneNewName = "an updated Zone Name";
    String zoneNewCommn = "an updated Zone Comment";

    List<UUID> deviceIds = new ArrayList<>();
    deviceIds.add(device1id);

    ZoneModification zoneMod = new ZoneModification();
    zoneMod.setName(zoneNewName);
    zoneMod.setComment(zoneNewCommn);
    zoneMod.setDeviceIds(deviceIds);

    UUID zone1id = UUID.fromString("55abcdef-1111-1111-1111-111111111101");
    UUID zone2id = UUID.fromString("55abcdef-1111-1111-2222-111111111102");
    String zone1name = "B zone name Test";
    String zone1comment = "B zone comment Test";
    String zone2name = "A zone name Test";
    String zone2comment = "A zone comment Test";
    List<UUID> zone1deviceIds = new ArrayList<>();
    zone1deviceIds.add(device1id);
    zone1deviceIds.add(device2id);
    zone1deviceIds.add(device3id);

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.setDeviceIds(zone1deviceIds);
    ZoneInfo zone2 = new ZoneInfo();
    zone2.setZoneId(zone2id);
    zone2.setName(zone2name);
    zone2.setComment(zone2comment);
    zone2.setDeviceIds(Collections.emptyList());

    Map<UUID, ZoneInfo> zoneMap = new HashMap<>();
    zoneMap.put(zone1id, zone1);
    zoneMap.put(zone2id, zone2);

    DeviceInfo dev1 = new DeviceInfo().deviceId(device1id).name(dev1name).isRegistered(dev1isReg);
    DeviceInfo dev2 = new DeviceInfo().deviceId(device2id).name(dev2name).isRegistered(dev2isReg);
    DeviceInfo dev3 = new DeviceInfo().deviceId(device3id).name(dev3name).isRegistered(dev3isReg);
    List<DeviceInfo> deviceList = new ArrayList<>();
    deviceList.add(dev1);
    deviceList.add(null);
    deviceList.add(dev2);
    deviceList.add(dev3);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot modify zone";
    String expectedDetail = "One or more devices in this zone do not exist.";

    // Mock(s)
    doReturn(zoneMap).when(this.zoneRepository).findAll();
    when(this.deviceRepository.findManyById(any(Set.class))).thenReturn(deviceList);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ZoneInfo> arg2 = ArgumentCaptor.forClass(ZoneInfo.class);

    // Call service
    CommonOperResponse operResp = this.zoneService.modifyZone(zone1id, zoneMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.zoneRepository, times(0)).update(arg1.capture(), arg2.capture());

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

  @SuppressWarnings("unchecked")
  @Test
  @DisplayName("TEST modifyZone (deviceIds has some unregistered devices)")
  void getModifyZoneDeviceIdListContainsUnregistered() {
    UUID device1id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111101");
    UUID device2id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111102");
    UUID device3id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111103");
    String dev1name = "Device 1";
    String dev2name = "Device 2";
    String dev3name = "Device 3";
    boolean dev1isReg = true;
    boolean dev2isReg = false;
    boolean dev3isReg = true;

    String zoneNewName = "an updated Zone Name";
    String zoneNewCommn = "an updated Zone Comment";

    List<UUID> deviceIds = new ArrayList<>();
    deviceIds.add(device1id);
    deviceIds.add(device2id);
    deviceIds.add(device3id);

    ZoneModification zoneMod = new ZoneModification();
    zoneMod.setName(zoneNewName);
    zoneMod.setComment(zoneNewCommn);
    zoneMod.setDeviceIds(deviceIds);

    UUID zone1id = UUID.fromString("55abcdef-1111-1111-1111-111111111101");
    UUID zone2id = UUID.fromString("55abcdef-1111-1111-2222-111111111102");
    String zone1name = "B zone name Test";
    String zone1comment = "B zone comment Test";
    String zone2name = "A zone name Test";
    String zone2comment = "A zone comment Test";
    List<UUID> zone1deviceIds = new ArrayList<>();
    zone1deviceIds.add(device2id);

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.setDeviceIds(zone1deviceIds);
    ZoneInfo zone2 = new ZoneInfo();
    zone2.setZoneId(zone2id);
    zone2.setName(zone2name);
    zone2.setComment(zone2comment);
    zone2.setDeviceIds(Collections.emptyList());

    Map<UUID, ZoneInfo> zoneMap = new HashMap<>();
    zoneMap.put(zone1id, zone1);
    zoneMap.put(zone2id, zone2);

    DeviceInfo dev1 = new DeviceInfo().deviceId(device1id).name(dev1name).isRegistered(dev1isReg);
    DeviceInfo dev2 = new DeviceInfo().deviceId(device2id).name(dev2name).isRegistered(dev2isReg);
    DeviceInfo dev3 = new DeviceInfo().deviceId(device3id).name(dev3name).isRegistered(dev3isReg);
    List<DeviceInfo> deviceList = new ArrayList<>();
    deviceList.add(dev1);
    deviceList.add(dev2);
    deviceList.add(dev3);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot modify zone";
    String expectedDetail = "One or more devices in this zone are not registered.";

    // Mock(s)
    doReturn(zoneMap).when(this.zoneRepository).findAll();
    when(this.deviceRepository.findManyById(any(Set.class))).thenReturn(deviceList);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ZoneInfo> arg2 = ArgumentCaptor.forClass(ZoneInfo.class);

    // Call service
    CommonOperResponse operResp = this.zoneService.modifyZone(zone1id, zoneMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.zoneRepository, times(0)).update(arg1.capture(), arg2.capture());

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

  @SuppressWarnings("unchecked")
  @Test
  @DisplayName("TEST modifyZone (deviceIds are all valid and registered)")
  void getModifyZoneDeviceIdListRegistered() {
    UUID device1id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111101");
    UUID device2id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111102");
    UUID device3id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111103");
    String dev1name = "Device 1";
    String dev2name = "Device 2";
    String dev3name = "Device 3";
    boolean dev1isReg = true;
    boolean dev2isReg = true;
    boolean dev3isReg = true;

    String zoneNewName = "an updated Zone Name";
    String zoneNewCommn = "an updated Zone Comment";

    List<UUID> deviceIds = new ArrayList<>();
    deviceIds.add(device1id);
    deviceIds.add(device2id);
    deviceIds.add(device3id);

    ZoneModification zoneMod = new ZoneModification();
    zoneMod.setName(zoneNewName);
    zoneMod.setComment(zoneNewCommn);
    zoneMod.setDeviceIds(deviceIds);

    UUID zone1id = UUID.fromString("55abcdef-1111-1111-1111-111111111101");
    UUID zone2id = UUID.fromString("55abcdef-1111-1111-2222-111111111102");
    String zone1name = "B zone name Test";
    String zone1comment = "B zone comment Test";
    String zone2name = "A zone name Test";
    String zone2comment = "A zone comment Test";
    List<UUID> zone1deviceIds = new ArrayList<>();
    zone1deviceIds.add(device2id);

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.setDeviceIds(zone1deviceIds);
    ZoneInfo zone2 = new ZoneInfo();
    zone2.setZoneId(zone2id);
    zone2.setName(zone2name);
    zone2.setComment(zone2comment);
    zone2.setDeviceIds(Collections.emptyList());

    Map<UUID, ZoneInfo> zoneMap = new HashMap<>();
    zoneMap.put(zone1id, zone1);
    zoneMap.put(zone2id, zone2);

    DeviceInfo dev1 = new DeviceInfo().deviceId(device1id).name(dev1name).isRegistered(dev1isReg);
    DeviceInfo dev2 = new DeviceInfo().deviceId(device2id).name(dev2name).isRegistered(dev2isReg);
    DeviceInfo dev3 = new DeviceInfo().deviceId(device3id).name(dev3name).isRegistered(dev3isReg);
    List<DeviceInfo> deviceList = new ArrayList<>();
    deviceList.add(dev1);
    deviceList.add(dev2);
    deviceList.add(dev3);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(zoneMap).when(this.zoneRepository).findAll();
    when(this.deviceRepository.findManyById(any(Set.class))).thenReturn(deviceList);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ZoneInfo> arg2 = ArgumentCaptor.forClass(ZoneInfo.class);

    // Call service
    CommonOperResponse operResp = this.zoneService.modifyZone(zone1id, zoneMod);

    // Verify that appropriate Repository method was called
    verify(this.zoneRepository, times(1)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertEquals(zone1id, arg1.getValue(), "Argument must match.");
    assertNotNull(arg2.getValue(), "Argument (ZoneInfo) must not be null.");
    assertEquals(zone1id, arg2.getValue().getZoneId(), "Argument's zoneId must match.");
    assertEquals(zoneNewName, arg2.getValue().getName(), "Argument's name must match.");
    assertEquals(zoneNewCommn, arg2.getValue().getComment(), "Argument's comment must match.");
    assertThat("Argument's deviceId List must match.", arg2.getValue().getDeviceIds(),
        contains(device1id, device2id, device3id));
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyZone (there are no existing zones (null))")
  void getModifyZoneNoEntryNull() {
    UUID device1id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111101");
    UUID device2id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111102");
    UUID device3id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111103");

    String zoneNewName = "an updated Zone Name";
    String zoneNewCommn = "an updated Zone Comment";

    ZoneModification zoneMod = new ZoneModification();
    zoneMod.setName(zoneNewName);
    zoneMod.setComment(zoneNewCommn);
    zoneMod.setDeviceIds(Collections.emptyList());

    UUID zone1id = UUID.fromString("55abcdef-1111-1111-1111-111111111101");
    UUID zone2id = UUID.fromString("55abcdef-1111-1111-2222-111111111102");
    String zone1name = "B zone name Test";
    String zone1comment = "B zone comment Test";
    String zone2name = "A zone name Test";
    String zone2comment = "A zone comment Test";
    List<UUID> zone1deviceIds = new ArrayList<>();
    zone1deviceIds.add(device1id);
    zone1deviceIds.add(device2id);
    zone1deviceIds.add(device3id);

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.setDeviceIds(zone1deviceIds);
    ZoneInfo zone2 = new ZoneInfo();
    zone2.setZoneId(zone2id);
    zone2.setName(zone2name);
    zone2.setComment(zone2comment);
    zone2.setDeviceIds(Collections.emptyList());

    Map<UUID, ZoneInfo> zoneMap = null;

    HttpStatus expectedStatus = HttpStatus.NOT_FOUND;

    // Mock(s)
    doReturn(zoneMap).when(this.zoneRepository).findAll();

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ZoneInfo> arg2 = ArgumentCaptor.forClass(ZoneInfo.class);

    // Call service
    CommonOperResponse operResp = this.zoneService.modifyZone(zone1id, zoneMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.zoneRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 404.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyZone (there are no existing zones)")
  void getModifyZoneNoEntry() {
    UUID device1id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111101");
    UUID device2id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111102");
    UUID device3id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111103");

    String zoneNewName = "an updated Zone Name";
    String zoneNewCommn = "an updated Zone Comment";

    ZoneModification zoneMod = new ZoneModification();
    zoneMod.setName(zoneNewName);
    zoneMod.setComment(zoneNewCommn);
    zoneMod.setDeviceIds(Collections.emptyList());

    UUID zone1id = UUID.fromString("55abcdef-1111-1111-1111-111111111101");
    UUID zone2id = UUID.fromString("55abcdef-1111-1111-2222-111111111102");
    String zone1name = "B zone name Test";
    String zone1comment = "B zone comment Test";
    String zone2name = "A zone name Test";
    String zone2comment = "A zone comment Test";
    List<UUID> zone1deviceIds = new ArrayList<>();
    zone1deviceIds.add(device1id);
    zone1deviceIds.add(device2id);
    zone1deviceIds.add(device3id);

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.setDeviceIds(zone1deviceIds);
    ZoneInfo zone2 = new ZoneInfo();
    zone2.setZoneId(zone2id);
    zone2.setName(zone2name);
    zone2.setComment(zone2comment);
    zone2.setDeviceIds(Collections.emptyList());

    Map<UUID, ZoneInfo> zoneMap = Collections.emptyMap();

    HttpStatus expectedStatus = HttpStatus.NOT_FOUND;

    // Mock(s)
    doReturn(zoneMap).when(this.zoneRepository).findAll();

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ZoneInfo> arg2 = ArgumentCaptor.forClass(ZoneInfo.class);

    // Call service
    CommonOperResponse operResp = this.zoneService.modifyZone(zone1id, zoneMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.zoneRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 404.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyZone (zoneId not found in DB)")
  void getModifyZoneNotFound() {
    UUID device1id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111101");
    UUID device2id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111102");
    UUID device3id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111103");

    String zoneNewName = "an updated Zone Name";
    String zoneNewCommn = "an updated Zone Comment";

    ZoneModification zoneMod = new ZoneModification();
    zoneMod.setName(zoneNewName);
    zoneMod.setComment(zoneNewCommn);
    zoneMod.setDeviceIds(Collections.emptyList());

    UUID zone1id = UUID.fromString("55abcdef-1111-1111-1111-111111111101");
    UUID zone2id = UUID.fromString("55abcdef-1111-1111-2222-111111111102");
    UUID otherZoneId = UUID.fromString("55abcdef-ffff-cccc-bbbb-11111111110f");
    String zone1name = "B zone name Test";
    String zone1comment = "B zone comment Test";
    String zone2name = "A zone name Test";
    String zone2comment = "A zone comment Test";
    List<UUID> zone1deviceIds = new ArrayList<>();
    zone1deviceIds.add(device1id);
    zone1deviceIds.add(device2id);
    zone1deviceIds.add(device3id);

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.setDeviceIds(zone1deviceIds);
    ZoneInfo zone2 = new ZoneInfo();
    zone2.setZoneId(zone2id);
    zone2.setName(zone2name);
    zone2.setComment(zone2comment);
    zone2.setDeviceIds(Collections.emptyList());

    Map<UUID, ZoneInfo> zoneMap = new HashMap<>();
    zoneMap.put(zone1id, zone1);
    zoneMap.put(zone2id, zone2);

    HttpStatus expectedStatus = HttpStatus.NOT_FOUND;

    // Mock(s)
    doReturn(zoneMap).when(this.zoneRepository).findAll();

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ZoneInfo> arg2 = ArgumentCaptor.forClass(ZoneInfo.class);

    // Call service
    CommonOperResponse operResp = this.zoneService.modifyZone(otherZoneId, zoneMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.zoneRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 404.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyZone (name is already used by same zone)")
  void getModifyZoneSameNameSameEntry() {
    UUID device1id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111101");
    UUID device2id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111102");
    UUID device3id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111103");

    String zoneNewCommn = "an updated Zone Comment";

    UUID zone1id = UUID.fromString("55abcdef-1111-1111-1111-111111111101");
    UUID zone2id = UUID.fromString("55abcdef-1111-1111-2222-111111111102");
    String zone1name = "B zone name Test";
    String zone1comment = "B zone comment Test";
    String zone2name = "A zone name Test";
    String zone2comment = "A zone comment Test";

    ZoneModification zoneMod = new ZoneModification();
    zoneMod.setName(zone1name);
    zoneMod.setComment(zoneNewCommn);
    zoneMod.setDeviceIds(Collections.emptyList());

    List<UUID> zone1deviceIds = new ArrayList<>();
    zone1deviceIds.add(device1id);
    zone1deviceIds.add(device2id);
    zone1deviceIds.add(device3id);

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.setDeviceIds(zone1deviceIds);
    ZoneInfo zone2 = new ZoneInfo();
    zone2.setZoneId(zone2id);
    zone2.setName(zone2name);
    zone2.setComment(zone2comment);
    zone2.setDeviceIds(Collections.emptyList());

    Map<UUID, ZoneInfo> zoneMap = new HashMap<>();
    zoneMap.put(zone1id, zone1);
    zoneMap.put(zone2id, zone2);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(zoneMap).when(this.zoneRepository).findAll();

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ZoneInfo> arg2 = ArgumentCaptor.forClass(ZoneInfo.class);

    // Call service
    CommonOperResponse operResp = this.zoneService.modifyZone(zone1id, zoneMod);

    // Verify that appropriate Repository method was called
    verify(this.zoneRepository, times(1)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertEquals(zone1id, arg1.getValue(), "Argument must match.");
    assertNotNull(arg2.getValue(), "Argument (ZoneInfo) must not be null.");
    assertEquals(zone1id, arg2.getValue().getZoneId(), "Argument's zoneId must match.");
    assertEquals(zone1name, arg2.getValue().getName(), "Argument's name must match.");
    assertEquals(zoneNewCommn, arg2.getValue().getComment(), "Argument's comment must match.");
    assertThat("Argument's deviceId List must be empty.", arg2.getValue().getDeviceIds(),
        IsEmptyCollection.empty());
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyZone (name is already used by another zone)")
  void getModifyZoneSameNameOtherEntry() {
    UUID device1id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111101");
    UUID device2id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111102");
    UUID device3id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111103");

    String zoneNewCommn = "an updated Zone Comment";

    UUID zone1id = UUID.fromString("55abcdef-1111-1111-1111-111111111101");
    UUID zone2id = UUID.fromString("55abcdef-1111-1111-2222-111111111102");
    String zone1name = "B zone name Test";
    String zone1comment = "B zone comment Test";
    String zone2name = "A zone name Test";
    String zone2comment = "A zone comment Test";

    ZoneModification zoneMod = new ZoneModification();
    zoneMod.setName(zone2name);
    zoneMod.setComment(zoneNewCommn);
    zoneMod.setDeviceIds(Collections.emptyList());

    List<UUID> zone1deviceIds = new ArrayList<>();
    zone1deviceIds.add(device1id);
    zone1deviceIds.add(device2id);
    zone1deviceIds.add(device3id);

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.setDeviceIds(zone1deviceIds);
    ZoneInfo zone2 = new ZoneInfo();
    zone2.setZoneId(zone2id);
    zone2.setName(zone2name);
    zone2.setComment(zone2comment);
    zone2.setDeviceIds(Collections.emptyList());

    Map<UUID, ZoneInfo> zoneMap = new HashMap<>();
    zoneMap.put(zone1id, zone1);
    zoneMap.put(zone2id, zone2);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    doReturn(zoneMap).when(this.zoneRepository).findAll();

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ZoneInfo> arg2 = ArgumentCaptor.forClass(ZoneInfo.class);

    // Call service
    CommonOperResponse operResp = this.zoneService.modifyZone(zone1id, zoneMod);

    // Verify that appropriate Repository method was NOT called
    verify(this.zoneRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST setDevicesToZone")
  void getSetDevicesToZone() {
    UUID device1id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111101");
    UUID device2id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111102");
    UUID device3id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111103");

    ZoneSetDevice zoneSet = new ZoneSetDevice();
    zoneSet.setDeviceIds(Collections.emptyList());

    UUID zone1id = UUID.fromString("55abcdef-1111-1111-1111-111111111101");
    UUID zone2id = UUID.fromString("55abcdef-1111-1111-2222-111111111102");
    String zone1name = "B zone name Test";
    String zone1comment = "B zone comment Test";
    String zone2name = "A zone name Test";
    String zone2comment = "A zone comment Test";
    List<UUID> zone1deviceIds = new ArrayList<>();
    zone1deviceIds.add(device1id);
    zone1deviceIds.add(device2id);
    zone1deviceIds.add(device3id);

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.setDeviceIds(zone1deviceIds);
    ZoneInfo zone2 = new ZoneInfo();
    zone2.setZoneId(zone2id);
    zone2.setName(zone2name);
    zone2.setComment(zone2comment);
    zone2.setDeviceIds(Collections.emptyList());

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.zoneRepository.findById(eq(zone1id))).thenReturn(zone1);
    when(this.zoneRepository.findById(eq(zone2id))).thenReturn(zone2);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ZoneInfo> arg2 = ArgumentCaptor.forClass(ZoneInfo.class);

    // Call service
    CommonOperResponse operResp = this.zoneService.setDevicesToZone(zone1id, zoneSet);

    // Verify that appropriate Repository method was called
    verify(this.zoneRepository, times(1)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertEquals(zone1id, arg1.getValue(), "Argument must match.");
    assertNotNull(arg2.getValue(), "Argument (ZoneInfo) must not be null.");
    assertEquals(zone1id, arg2.getValue().getZoneId(), "Argument's zoneId must match.");
    assertThat("Argument's deviceId List must be empty.", arg2.getValue().getDeviceIds(),
        IsEmptyCollection.empty());
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST setDevicesToZone (zoneId not found in DB)")
  void getSetDevicesToZoneNotFound() {
    UUID device1id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111101");
    UUID device2id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111102");
    UUID device3id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111103");

    ZoneSetDevice zoneSet = new ZoneSetDevice();
    zoneSet.setDeviceIds(Collections.emptyList());

    UUID zone1id = UUID.fromString("55abcdef-1111-1111-1111-111111111101");
    UUID zone2id = UUID.fromString("55abcdef-1111-1111-2222-111111111102");
    UUID otherZoneId = UUID.fromString("55abcdef-ffff-cccc-bbbb-11111111110f");
    String zone1name = "B zone name Test";
    String zone1comment = "B zone comment Test";
    String zone2name = "A zone name Test";
    String zone2comment = "A zone comment Test";
    List<UUID> zone1deviceIds = new ArrayList<>();
    zone1deviceIds.add(device1id);
    zone1deviceIds.add(device2id);
    zone1deviceIds.add(device3id);

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.setDeviceIds(zone1deviceIds);
    ZoneInfo zone2 = new ZoneInfo();
    zone2.setZoneId(zone2id);
    zone2.setName(zone2name);
    zone2.setComment(zone2comment);
    zone2.setDeviceIds(Collections.emptyList());

    HttpStatus expectedStatus = HttpStatus.NOT_FOUND;

    // Mock(s)
    when(this.zoneRepository.findById(eq(zone1id))).thenReturn(zone1);
    when(this.zoneRepository.findById(eq(zone2id))).thenReturn(zone2);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ZoneInfo> arg2 = ArgumentCaptor.forClass(ZoneInfo.class);

    // Call service
    CommonOperResponse operResp = this.zoneService.setDevicesToZone(otherZoneId, zoneSet);

    // Verify that appropriate Repository method was NOT called
    verify(this.zoneRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 404.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST setDevicesToZone (deviceIds is null)")
  void getSetDevicesToZoneDeviceIdListNull() {
    UUID device1id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111101");
    UUID device2id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111102");
    UUID device3id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111103");

    ZoneSetDevice zoneSet = new ZoneSetDevice();
    zoneSet.setDeviceIds(null);

    UUID zone1id = UUID.fromString("55abcdef-1111-1111-1111-111111111101");
    UUID zone2id = UUID.fromString("55abcdef-1111-1111-2222-111111111102");
    String zone1name = "B zone name Test";
    String zone1comment = "B zone comment Test";
    String zone2name = "A zone name Test";
    String zone2comment = "A zone comment Test";
    List<UUID> zone1deviceIds = new ArrayList<>();
    zone1deviceIds.add(device1id);
    zone1deviceIds.add(device2id);
    zone1deviceIds.add(device3id);

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.setDeviceIds(zone1deviceIds);
    ZoneInfo zone2 = new ZoneInfo();
    zone2.setZoneId(zone2id);
    zone2.setName(zone2name);
    zone2.setComment(zone2comment);
    zone2.setDeviceIds(Collections.emptyList());

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.zoneRepository.findById(eq(zone1id))).thenReturn(zone1);
    when(this.zoneRepository.findById(eq(zone2id))).thenReturn(zone2);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ZoneInfo> arg2 = ArgumentCaptor.forClass(ZoneInfo.class);

    // Call service
    CommonOperResponse operResp = this.zoneService.setDevicesToZone(zone1id, zoneSet);

    // Verify that appropriate Repository method was NOT called
    verify(this.zoneRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST setDevicesToZone (deviceIds contains null)")
  void getSetDevicesToZoneDeviceIdListContainsNull() {
    UUID device1id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111101");
    UUID device2id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111102");
    UUID device3id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111103");

    List<UUID> deviceIds = new ArrayList<>();
    deviceIds.add(device1id);
    deviceIds.add(device2id);
    deviceIds.add(null);
    deviceIds.add(device3id);

    ZoneSetDevice zoneSet = new ZoneSetDevice();
    zoneSet.setDeviceIds(deviceIds);

    UUID zone1id = UUID.fromString("55abcdef-1111-1111-1111-111111111101");
    UUID zone2id = UUID.fromString("55abcdef-1111-1111-2222-111111111102");
    String zone1name = "B zone name Test";
    String zone1comment = "B zone comment Test";
    String zone2name = "A zone name Test";
    String zone2comment = "A zone comment Test";
    List<UUID> zone1deviceIds = new ArrayList<>();
    zone1deviceIds.add(device1id);
    zone1deviceIds.add(device2id);
    zone1deviceIds.add(device3id);

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.setDeviceIds(zone1deviceIds);
    ZoneInfo zone2 = new ZoneInfo();
    zone2.setZoneId(zone2id);
    zone2.setName(zone2name);
    zone2.setComment(zone2comment);
    zone2.setDeviceIds(Collections.emptyList());

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.zoneRepository.findById(eq(zone1id))).thenReturn(zone1);
    when(this.zoneRepository.findById(eq(zone2id))).thenReturn(zone2);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ZoneInfo> arg2 = ArgumentCaptor.forClass(ZoneInfo.class);

    // Call service
    CommonOperResponse operResp = this.zoneService.setDevicesToZone(zone1id, zoneSet);

    // Verify that appropriate Repository method was NOT called
    verify(this.zoneRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST setDevicesToZone (deviceIds contains duplicate entry)")
  void getSetDevicesToZoneDeviceIdListContainsDuplicate() {
    UUID device1id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111101");
    UUID device2id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111102");
    UUID device3id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111103");

    List<UUID> deviceIds = new ArrayList<>();
    deviceIds.add(device1id);
    deviceIds.add(device2id);
    deviceIds.add(device1id);
    deviceIds.add(device3id);

    ZoneSetDevice zoneSet = new ZoneSetDevice();
    zoneSet.setDeviceIds(deviceIds);

    UUID zone1id = UUID.fromString("55abcdef-1111-1111-1111-111111111101");
    UUID zone2id = UUID.fromString("55abcdef-1111-1111-2222-111111111102");
    String zone1name = "B zone name Test";
    String zone1comment = "B zone comment Test";
    String zone2name = "A zone name Test";
    String zone2comment = "A zone comment Test";
    List<UUID> zone1deviceIds = new ArrayList<>();
    zone1deviceIds.add(device1id);
    zone1deviceIds.add(device2id);
    zone1deviceIds.add(device3id);

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.setDeviceIds(zone1deviceIds);
    ZoneInfo zone2 = new ZoneInfo();
    zone2.setZoneId(zone2id);
    zone2.setName(zone2name);
    zone2.setComment(zone2comment);
    zone2.setDeviceIds(Collections.emptyList());

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.zoneRepository.findById(eq(zone1id))).thenReturn(zone1);
    when(this.zoneRepository.findById(eq(zone2id))).thenReturn(zone2);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ZoneInfo> arg2 = ArgumentCaptor.forClass(ZoneInfo.class);

    // Call service
    CommonOperResponse operResp = this.zoneService.setDevicesToZone(zone1id, zoneSet);

    // Verify that appropriate Repository method was NOT called
    verify(this.zoneRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 400.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @SuppressWarnings("unchecked")
  @Test
  @DisplayName("TEST setDevicesToZone (device multi-lookup returns null)")
  void getSetDevicesToZoneDeviceIdListMultiLookupReturnsNull() {
    UUID device1id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111101");
    UUID device2id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111102");
    UUID device3id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111103");

    List<UUID> deviceIds = new ArrayList<>();
    deviceIds.add(device1id);
    deviceIds.add(device2id);
    deviceIds.add(device3id);

    ZoneSetDevice zoneSet = new ZoneSetDevice();
    zoneSet.setDeviceIds(deviceIds);

    UUID zone1id = UUID.fromString("55abcdef-1111-1111-1111-111111111101");
    UUID zone2id = UUID.fromString("55abcdef-1111-1111-2222-111111111102");
    String zone1name = "B zone name Test";
    String zone1comment = "B zone comment Test";
    String zone2name = "A zone name Test";
    String zone2comment = "A zone comment Test";
    List<UUID> zone1deviceIds = new ArrayList<>();
    zone1deviceIds.add(device1id);
    zone1deviceIds.add(device2id);
    zone1deviceIds.add(device3id);

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.setDeviceIds(zone1deviceIds);
    ZoneInfo zone2 = new ZoneInfo();
    zone2.setZoneId(zone2id);
    zone2.setName(zone2name);
    zone2.setComment(zone2comment);
    zone2.setDeviceIds(Collections.emptyList());

    List<DeviceInfo> deviceList = null;

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot set devices in zone";
    String expectedDetail = "There are no existing devices for this zone.";

    // Mock(s)
    when(this.zoneRepository.findById(eq(zone1id))).thenReturn(zone1);
    when(this.zoneRepository.findById(eq(zone2id))).thenReturn(zone2);
    when(this.deviceRepository.findManyById(any(Set.class))).thenReturn(deviceList);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ZoneInfo> arg2 = ArgumentCaptor.forClass(ZoneInfo.class);

    // Call service
    CommonOperResponse operResp = this.zoneService.setDevicesToZone(zone1id, zoneSet);

    // Verify that appropriate Repository method was NOT called
    verify(this.zoneRepository, times(0)).update(arg1.capture(), arg2.capture());

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

  @SuppressWarnings("unchecked")
  @Test
  @DisplayName("TEST setDevicesToZone (device multi-lookup returns empty)")
  void getSetDevicesToZoneDeviceIdListMultiLookupReturnsEmpty() {
    UUID device1id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111101");
    UUID device2id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111102");
    UUID device3id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111103");

    List<UUID> deviceIds = new ArrayList<>();
    deviceIds.add(device1id);
    deviceIds.add(device2id);
    deviceIds.add(device3id);

    ZoneSetDevice zoneSet = new ZoneSetDevice();
    zoneSet.setDeviceIds(deviceIds);

    UUID zone1id = UUID.fromString("55abcdef-1111-1111-1111-111111111101");
    UUID zone2id = UUID.fromString("55abcdef-1111-1111-2222-111111111102");
    String zone1name = "B zone name Test";
    String zone1comment = "B zone comment Test";
    String zone2name = "A zone name Test";
    String zone2comment = "A zone comment Test";
    List<UUID> zone1deviceIds = new ArrayList<>();
    zone1deviceIds.add(device1id);
    zone1deviceIds.add(device2id);
    zone1deviceIds.add(device3id);

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.setDeviceIds(zone1deviceIds);
    ZoneInfo zone2 = new ZoneInfo();
    zone2.setZoneId(zone2id);
    zone2.setName(zone2name);
    zone2.setComment(zone2comment);
    zone2.setDeviceIds(Collections.emptyList());

    List<DeviceInfo> deviceList = Collections.emptyList();

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot set devices in zone";
    String expectedDetail = "There are no existing devices for this zone.";

    // Mock(s)
    when(this.zoneRepository.findById(eq(zone1id))).thenReturn(zone1);
    when(this.zoneRepository.findById(eq(zone2id))).thenReturn(zone2);
    when(this.deviceRepository.findManyById(any(Set.class))).thenReturn(deviceList);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ZoneInfo> arg2 = ArgumentCaptor.forClass(ZoneInfo.class);

    // Call service
    CommonOperResponse operResp = this.zoneService.setDevicesToZone(zone1id, zoneSet);

    // Verify that appropriate Repository method was NOT called
    verify(this.zoneRepository, times(0)).update(arg1.capture(), arg2.capture());

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

  @SuppressWarnings("unchecked")
  @Test
  @DisplayName("TEST setDevicesToZone (device multi-lookup returns a list containing null)")
  void getSetDevicesToZoneDeviceIdListMultiLookupReturnsContainingNull() {
    UUID device1id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111101");
    UUID device2id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111102");
    UUID device3id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111103");
    String dev1name = "Device 1";
    String dev2name = "Device 2";
    String dev3name = "Device 3";
    boolean dev1isReg = true;
    boolean dev2isReg = true;
    boolean dev3isReg = true;

    List<UUID> deviceIds = new ArrayList<>();
    deviceIds.add(device1id);
    deviceIds.add(device2id);
    deviceIds.add(device3id);

    ZoneSetDevice zoneSet = new ZoneSetDevice();
    zoneSet.setDeviceIds(deviceIds);

    UUID zone1id = UUID.fromString("55abcdef-1111-1111-1111-111111111101");
    UUID zone2id = UUID.fromString("55abcdef-1111-1111-2222-111111111102");
    String zone1name = "B zone name Test";
    String zone1comment = "B zone comment Test";
    String zone2name = "A zone name Test";
    String zone2comment = "A zone comment Test";
    List<UUID> zone1deviceIds = new ArrayList<>();
    zone1deviceIds.add(device1id);
    zone1deviceIds.add(device2id);
    zone1deviceIds.add(device3id);

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.setDeviceIds(zone1deviceIds);
    ZoneInfo zone2 = new ZoneInfo();
    zone2.setZoneId(zone2id);
    zone2.setName(zone2name);
    zone2.setComment(zone2comment);
    zone2.setDeviceIds(Collections.emptyList());

    DeviceInfo dev1 = new DeviceInfo().deviceId(device1id).name(dev1name).isRegistered(dev1isReg);
    DeviceInfo dev2 = new DeviceInfo().deviceId(device2id).name(dev2name).isRegistered(dev2isReg);
    DeviceInfo dev3 = new DeviceInfo().deviceId(device3id).name(dev3name).isRegistered(dev3isReg);
    List<DeviceInfo> deviceList = new ArrayList<>();
    deviceList.add(dev1);
    deviceList.add(null);
    deviceList.add(dev2);
    deviceList.add(dev3);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot set devices in zone";
    String expectedDetail = "One or more devices in this zone do not exist.";

    // Mock(s)
    when(this.zoneRepository.findById(eq(zone1id))).thenReturn(zone1);
    when(this.zoneRepository.findById(eq(zone2id))).thenReturn(zone2);
    when(this.deviceRepository.findManyById(any(Set.class))).thenReturn(deviceList);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ZoneInfo> arg2 = ArgumentCaptor.forClass(ZoneInfo.class);

    // Call service
    CommonOperResponse operResp = this.zoneService.setDevicesToZone(zone1id, zoneSet);

    // Verify that appropriate Repository method was NOT called
    verify(this.zoneRepository, times(0)).update(arg1.capture(), arg2.capture());

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

  @SuppressWarnings("unchecked")
  @Test
  @DisplayName("TEST setDevicesToZone (deviceIds has some unregistered devices)")
  void getSetDevicesToZoneDeviceIdListContainsUnregistered() {
    UUID device1id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111101");
    UUID device2id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111102");
    UUID device3id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111103");
    String dev1name = "Device 1";
    String dev2name = "Device 2";
    String dev3name = "Device 3";
    boolean dev1isReg = true;
    boolean dev2isReg = false;
    boolean dev3isReg = true;

    List<UUID> deviceIds = new ArrayList<>();
    deviceIds.add(device1id);
    deviceIds.add(device2id);
    deviceIds.add(device3id);

    ZoneSetDevice zoneSet = new ZoneSetDevice();
    zoneSet.setDeviceIds(deviceIds);

    UUID zone1id = UUID.fromString("55abcdef-1111-1111-1111-111111111101");
    UUID zone2id = UUID.fromString("55abcdef-1111-1111-2222-111111111102");
    String zone1name = "B zone name Test";
    String zone1comment = "B zone comment Test";
    String zone2name = "A zone name Test";
    String zone2comment = "A zone comment Test";
    List<UUID> zone1deviceIds = new ArrayList<>();
    zone1deviceIds.add(device1id);
    zone1deviceIds.add(device2id);
    zone1deviceIds.add(device3id);

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.setDeviceIds(zone1deviceIds);
    ZoneInfo zone2 = new ZoneInfo();
    zone2.setZoneId(zone2id);
    zone2.setName(zone2name);
    zone2.setComment(zone2comment);
    zone2.setDeviceIds(Collections.emptyList());

    DeviceInfo dev1 = new DeviceInfo().deviceId(device1id).name(dev1name).isRegistered(dev1isReg);
    DeviceInfo dev2 = new DeviceInfo().deviceId(device2id).name(dev2name).isRegistered(dev2isReg);
    DeviceInfo dev3 = new DeviceInfo().deviceId(device3id).name(dev3name).isRegistered(dev3isReg);
    List<DeviceInfo> deviceList = new ArrayList<>();
    deviceList.add(dev1);
    deviceList.add(dev2);
    deviceList.add(dev3);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot set devices in zone";
    String expectedDetail = "One or more devices in this zone are not registered.";

    // Mock(s)
    when(this.zoneRepository.findById(eq(zone1id))).thenReturn(zone1);
    when(this.zoneRepository.findById(eq(zone2id))).thenReturn(zone2);
    when(this.deviceRepository.findManyById(any(Set.class))).thenReturn(deviceList);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ZoneInfo> arg2 = ArgumentCaptor.forClass(ZoneInfo.class);

    // Call service
    CommonOperResponse operResp = this.zoneService.setDevicesToZone(zone1id, zoneSet);

    // Verify that appropriate Repository method was NOT called
    verify(this.zoneRepository, times(0)).update(arg1.capture(), arg2.capture());

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

  @SuppressWarnings("unchecked")
  @Test
  @DisplayName("TEST setDevicesToZone (deviceIds are all valid and registered)")
  void getSetDevicesToZoneDeviceIdListRegistered() {
    UUID device1id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111101");
    UUID device2id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111102");
    UUID device3id = UUID.fromString("05abcdef-9999-aaaa-cccc-111111111103");
    String dev1name = "Device 1";
    String dev2name = "Device 2";
    String dev3name = "Device 3";
    boolean dev1isReg = true;
    boolean dev2isReg = true;
    boolean dev3isReg = true;

    List<UUID> deviceIds = new ArrayList<>();
    deviceIds.add(device1id);
    deviceIds.add(device2id);
    deviceIds.add(device3id);

    ZoneSetDevice zoneSet = new ZoneSetDevice();
    zoneSet.setDeviceIds(deviceIds);

    UUID zone1id = UUID.fromString("55abcdef-1111-1111-1111-111111111101");
    UUID zone2id = UUID.fromString("55abcdef-1111-1111-2222-111111111102");
    String zone1name = "B zone name Test";
    String zone1comment = "B zone comment Test";
    String zone2name = "A zone name Test";
    String zone2comment = "A zone comment Test";
    List<UUID> zone1deviceIds = new ArrayList<>();
    zone1deviceIds.add(device1id);
    zone1deviceIds.add(device2id);
    zone1deviceIds.add(device3id);

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.setDeviceIds(zone1deviceIds);
    ZoneInfo zone2 = new ZoneInfo();
    zone2.setZoneId(zone2id);
    zone2.setName(zone2name);
    zone2.setComment(zone2comment);
    zone2.setDeviceIds(Collections.emptyList());

    DeviceInfo dev1 = new DeviceInfo().deviceId(device1id).name(dev1name).isRegistered(dev1isReg);
    DeviceInfo dev2 = new DeviceInfo().deviceId(device2id).name(dev2name).isRegistered(dev2isReg);
    DeviceInfo dev3 = new DeviceInfo().deviceId(device3id).name(dev3name).isRegistered(dev3isReg);
    List<DeviceInfo> deviceList = new ArrayList<>();
    deviceList.add(dev1);
    deviceList.add(dev2);
    deviceList.add(dev3);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.zoneRepository.findById(eq(zone1id))).thenReturn(zone1);
    when(this.zoneRepository.findById(eq(zone2id))).thenReturn(zone2);
    when(this.deviceRepository.findManyById(any(Set.class))).thenReturn(deviceList);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ZoneInfo> arg2 = ArgumentCaptor.forClass(ZoneInfo.class);

    // Call service
    CommonOperResponse operResp = this.zoneService.setDevicesToZone(zone1id, zoneSet);

    // Verify that appropriate Repository method was called
    verify(this.zoneRepository, times(1)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertEquals(zone1id, arg1.getValue(), "Argument must match.");
    assertNotNull(arg2.getValue(), "Argument (ZoneInfo) must not be null.");
    assertEquals(zone1id, arg2.getValue().getZoneId(), "Argument's zoneId must match.");
    assertThat("Argument's deviceId List must match.", arg2.getValue().getDeviceIds(),
        contains(device1id, device2id, device3id));
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST deleteZone")
  void testDeleteZone() {
    UUID zone1id = UUID.fromString("55abcdef-1111-1111-1111-111111111101");
    String zone1name = "B zone name Test";
    String zone1comment = "B zone comment Test";

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.setDeviceIds(Collections.emptyList());

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.zoneRepository.findById(eq(zone1id))).thenReturn(zone1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call service
    CommonOperResponse operResp = this.zoneService.deleteZone(zone1id);

    // Verify that appropriate Repository method was called
    verify(this.zoneRepository, times(1)).delete(arg1.capture());

    // Assertions
    assertEquals(zone1id, arg1.getValue(), "Argument must match.");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST deleteZone (zoneId not found in DB)")
  void testDeleteZoneNotFound() {
    UUID zone1id = UUID.fromString("55abcdef-1111-1111-1111-111111111101");
    UUID otherZoneId = UUID.fromString("55abcdef-ffff-cccc-bbbb-11111111110f");
    String zone1name = "B zone name Test";
    String zone1comment = "B zone comment Test";

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zone1id);
    zone1.setName(zone1name);
    zone1.setComment(zone1comment);
    zone1.setDeviceIds(Collections.emptyList());

    HttpStatus expectedStatus = HttpStatus.NOT_FOUND;

    // Mock(s)
    when(this.zoneRepository.findById(eq(zone1id))).thenReturn(zone1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call service
    CommonOperResponse operResp = this.zoneService.deleteZone(otherZoneId);

    // Verify that appropriate Repository method was NOT called
    verify(this.zoneRepository, times(0)).delete(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 404.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }
}
