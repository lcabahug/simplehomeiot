package com.codingcuriosity.project.simplehomeiot.groups.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import com.codingcuriosity.project.simplehomeiot.groups.model.EnableDisableGroup;
import com.codingcuriosity.project.simplehomeiot.groups.model.GroupInfo;
import com.codingcuriosity.project.simplehomeiot.groups.model.GroupModification;
import com.codingcuriosity.project.simplehomeiot.groups.model.GroupRawInfo;
import com.codingcuriosity.project.simplehomeiot.groups.model.GroupRegistration;
import com.codingcuriosity.project.simplehomeiot.groups.repository.GroupRepository;
import com.codingcuriosity.project.simplehomeiot.logs.repository.ControllerLogRepository;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
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
public class GroupServiceImplTest {

  @Autowired
  GroupService groupService;

  @MockBean
  DeviceRepository deviceRepository;

  @MockBean
  GroupRepository groupRepository;

  @MockBean
  ControllerLogRepository logRepository;

  @Test
  @DisplayName("TEST getLogRepo")
  void testGetLogRepo() {

    // Call Service
    ControllerLogRepository logRepository = this.groupService.getLogRepo();

    // Assertions
    assertEquals(this.logRepository, logRepository, "LogRepository must be identical.");
  }

  @Test
  @DisplayName("TEST getGroups (all parameters = null)")
  void testGetGroupsNoParam() {
    Integer skip = null;
    Integer limit = null;

    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");
    UUID group2id = UUID.fromString("05abcdef-2222-2222-2222-222222222202");
    UUID group3id = UUID.fromString("05abcdef-3333-3333-3333-333333333303");
    UUID group4id = UUID.fromString("05abcdef-4444-4444-4444-444444444404");
    String group1name = "D a Group Name 1";
    String group2name = "C a Group Name 2";
    String group3name = "A a Group Name 3";
    String group4name = "B a Group Name 4";
    String group1descr = "a Group Description 1";
    String group2descr = "a Group Description 2";
    String group3descr = "a Group Description 3";
    String group4descr = "a Group Description 4";
    String group1commn = "a Group Comment 1";
    String group2commn = "a Group Comment 2";
    String group3commn = "a Group Comment 3";
    String group4commn = "a Group Comment 4";
    Boolean group1isEnabled = true;
    Boolean group2isEnabled = true;
    Boolean group3isEnabled = false;
    Boolean group4isEnabled = true;

    UUID group1dev1 = UUID.fromString("15abcdef-9999-1111-1111-111111111101");
    UUID group1dev2 = UUID.fromString("15abcdef-9999-2222-1111-111111111101");
    UUID group2dev1 = UUID.fromString("15abcdef-9999-1111-2222-222222222202");
    UUID group2dev2 = UUID.fromString("15abcdef-9999-2222-2222-222222222202");
    UUID group3dev1 = UUID.fromString("15abcdef-9999-1111-3333-333333333303");
    UUID group3dev2 = UUID.fromString("15abcdef-9999-2222-3333-333333333303");
    UUID group4dev1 = UUID.fromString("15abcdef-9999-1111-4444-444444444404");
    UUID group4dev2 = UUID.fromString("15abcdef-9999-2222-4444-444444444404");

    UUID unrelatedDeviceId = UUID.fromString("99999999-9999-9999-9999-999999999999");
    DeviceInfo device1 = new DeviceInfo().deviceId(group1dev1).groupId(group1id);
    DeviceInfo device2 = new DeviceInfo().deviceId(group1dev2).groupId(group1id);
    DeviceInfo device3 = new DeviceInfo().deviceId(group2dev1).groupId(group2id);
    DeviceInfo device4 = new DeviceInfo().deviceId(group2dev2).groupId(group2id);
    DeviceInfo device5 = new DeviceInfo().deviceId(group3dev1).groupId(group3id);
    DeviceInfo device6 = new DeviceInfo().deviceId(group3dev2).groupId(group3id);
    DeviceInfo device7 = new DeviceInfo().deviceId(group4dev1).groupId(group4id);
    DeviceInfo device8 = new DeviceInfo().deviceId(group4dev2).groupId(group4id);
    DeviceInfo device9 = new DeviceInfo().deviceId(unrelatedDeviceId);

    Map<UUID, DeviceInfo> deviceMap = new HashMap<>();
    deviceMap.put(group1dev1, device1);
    deviceMap.put(group1dev2, device2);
    deviceMap.put(group2dev1, device3);
    deviceMap.put(group2dev2, device4);
    deviceMap.put(group3dev1, device5);
    deviceMap.put(group3dev2, device6);
    deviceMap.put(group4dev1, device7);
    deviceMap.put(group4dev2, device8);
    deviceMap.put(unrelatedDeviceId, device9);

    GroupRawInfo group1Raw = new GroupRawInfo();
    group1Raw.setGroupId(group1id);
    group1Raw.setName(group1name);
    group1Raw.setDescription(group1descr);
    group1Raw.setComment(group1commn);
    group1Raw.setIsEnabled(group1isEnabled);
    GroupRawInfo group2Raw = new GroupRawInfo();
    group2Raw.setGroupId(group2id);
    group2Raw.setName(group2name);
    group2Raw.setDescription(group2descr);
    group2Raw.setComment(group2commn);
    group2Raw.setIsEnabled(group2isEnabled);
    GroupRawInfo group3Raw = new GroupRawInfo();
    group3Raw.setGroupId(group3id);
    group3Raw.setName(group3name);
    group3Raw.setDescription(group3descr);
    group3Raw.setComment(group3commn);
    group3Raw.setIsEnabled(group3isEnabled);
    GroupRawInfo group4Raw = new GroupRawInfo();
    group4Raw.setGroupId(group4id);
    group4Raw.setName(group4name);
    group4Raw.setDescription(group4descr);
    group4Raw.setComment(group4commn);
    group4Raw.setIsEnabled(group4isEnabled);

    Map<UUID, GroupRawInfo> groupMap = new HashMap<>();
    groupMap.put(group1id, group1Raw);
    groupMap.put(group2id, group2Raw);
    groupMap.put(group3id, group3Raw);
    groupMap.put(group4id, group4Raw);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(groupMap).when(this.groupRepository).findAll();
    doReturn(deviceMap).when(deviceRepository).findAll();

    // Call service
    CommonGetListResponse<GroupInfo> retListResp = groupService.getGroups(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.groupRepository, times(1)).findAll();

    // Expected Values
    GroupInfo group1 = new GroupInfo();
    group1.setGroupId(group1id);
    group1.setName(group1name);
    group1.setDescription(group1descr);
    group1.setComment(group1commn);
    group1.setIsEnabled(group1isEnabled);
    group1.addDevicesItem(group1dev1);
    group1.addDevicesItem(group1dev2);
    GroupInfo group2 = new GroupInfo();
    group2.setGroupId(group2id);
    group2.setName(group2name);
    group2.setDescription(group2descr);
    group2.setComment(group2commn);
    group2.setIsEnabled(group2isEnabled);
    group2.addDevicesItem(group2dev1);
    group2.addDevicesItem(group2dev2);
    GroupInfo group3 = new GroupInfo();
    group3.setGroupId(group3id);
    group3.setName(group3name);
    group3.setDescription(group3descr);
    group3.setComment(group3commn);
    group3.setIsEnabled(group3isEnabled);
    group3.addDevicesItem(group3dev1);
    group3.addDevicesItem(group3dev2);
    GroupInfo group4 = new GroupInfo();
    group4.setGroupId(group4id);
    group4.setName(group4name);
    group4.setDescription(group4descr);
    group4.setComment(group4commn);
    group4.setIsEnabled(group4isEnabled);
    group4.addDevicesItem(group4dev1);
    group4.addDevicesItem(group4dev2);

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be sorted by group name ascending.",
        retListResp.getObjList(), contains(group3, group4, group2, group1));
    assertEquals(group3id, retListResp.getObjList().get(0).getGroupId(),
        "1st entry's UUID must match group 3's");
    assertEquals(group3name, retListResp.getObjList().get(0).getName(),
        "1st entry's name must match group 3's");
    assertEquals(group3descr, retListResp.getObjList().get(0).getDescription(),
        "1st entry's manufacturer must match group 3's");
    assertEquals(group3commn, retListResp.getObjList().get(0).getComment(),
        "1st entry's comment must match group 3's");
    assertEquals(group3isEnabled, retListResp.getObjList().get(0).isIsEnabled(),
        "1st entry's isEnabled must match group 3's");
    assertThat("The device List must be correct (any order).",
        retListResp.getObjList().get(0).getDevices(), containsInAnyOrder(group3dev1, group3dev2));
    assertEquals(group4id, retListResp.getObjList().get(1).getGroupId(),
        "2nd entry's UUID must match group 4's");
    assertEquals(group4name, retListResp.getObjList().get(1).getName(),
        "2nd entry's name must match group 4's");
    assertEquals(group4descr, retListResp.getObjList().get(1).getDescription(),
        "2nd entry's manufacturer must match group 4's");
    assertEquals(group4commn, retListResp.getObjList().get(1).getComment(),
        "2nd entry's comment must match group 4's");
    assertEquals(group4isEnabled, retListResp.getObjList().get(1).isIsEnabled(),
        "2nd entry's isEnabled must match group 4's");
    assertThat("The device List must be correct (any order).",
        retListResp.getObjList().get(1).getDevices(), containsInAnyOrder(group4dev1, group4dev2));
    assertEquals(group2id, retListResp.getObjList().get(2).getGroupId(),
        "3rd entry's UUID must match group 2's");
    assertEquals(group2name, retListResp.getObjList().get(2).getName(),
        "3rd entry's name must match group 2's");
    assertEquals(group2descr, retListResp.getObjList().get(2).getDescription(),
        "3rd entry's manufacturer must match group 2's");
    assertEquals(group2commn, retListResp.getObjList().get(2).getComment(),
        "3rd entry's comment must match group 2's");
    assertEquals(group2isEnabled, retListResp.getObjList().get(2).isIsEnabled(),
        "3rd entry's isEnabled must match group 2's");
    assertThat("The device List must be correct (any order).",
        retListResp.getObjList().get(2).getDevices(), containsInAnyOrder(group2dev1, group2dev2));
    assertEquals(group1id, retListResp.getObjList().get(3).getGroupId(),
        "4th entry's UUID must match group 1's");
    assertEquals(group1name, retListResp.getObjList().get(3).getName(),
        "4th entry's name must match group 1's");
    assertEquals(group1descr, retListResp.getObjList().get(3).getDescription(),
        "4th entry's manufacturer must match group 1's");
    assertEquals(group1commn, retListResp.getObjList().get(3).getComment(),
        "4th entry's comment must match group 1's");
    assertEquals(group1isEnabled, retListResp.getObjList().get(3).isIsEnabled(),
        "4th entry's isEnabled must match group 1's");
    assertThat("The device List must be correct (any order).",
        retListResp.getObjList().get(3).getDevices(), containsInAnyOrder(group1dev1, group1dev2));
  }

  @Test
  @DisplayName("TEST getGroups (skip = 0, limit = 0)")
  void testGetGroupsSkipMinLimitMin() {
    Integer skip = 0;
    Integer limit = 0;

    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");
    UUID group2id = UUID.fromString("05abcdef-2222-2222-2222-222222222202");
    UUID group3id = UUID.fromString("05abcdef-3333-3333-3333-333333333303");
    UUID group4id = UUID.fromString("05abcdef-4444-4444-4444-444444444404");
    String group1name = "D a Group Name 1";
    String group2name = "C a Group Name 2";
    String group3name = "A a Group Name 3";
    String group4name = "B a Group Name 4";
    String group1descr = "a Group Description 1";
    String group2descr = "a Group Description 2";
    String group3descr = "a Group Description 3";
    String group4descr = "a Group Description 4";
    String group1commn = "a Group Comment 1";
    String group2commn = "a Group Comment 2";
    String group3commn = "a Group Comment 3";
    String group4commn = "a Group Comment 4";
    Boolean group1isEnabled = true;
    Boolean group2isEnabled = true;
    Boolean group3isEnabled = false;
    Boolean group4isEnabled = true;

    UUID group1dev1 = UUID.fromString("15abcdef-9999-1111-1111-111111111101");
    UUID group1dev2 = UUID.fromString("15abcdef-9999-2222-1111-111111111101");
    UUID group2dev1 = UUID.fromString("15abcdef-9999-1111-2222-222222222202");
    UUID group2dev2 = UUID.fromString("15abcdef-9999-2222-2222-222222222202");
    UUID group3dev1 = UUID.fromString("15abcdef-9999-1111-3333-333333333303");
    UUID group3dev2 = UUID.fromString("15abcdef-9999-2222-3333-333333333303");
    UUID group4dev1 = UUID.fromString("15abcdef-9999-1111-4444-444444444404");
    UUID group4dev2 = UUID.fromString("15abcdef-9999-2222-4444-444444444404");

    UUID unrelatedDeviceId = UUID.fromString("99999999-9999-9999-9999-999999999999");
    DeviceInfo device1 = new DeviceInfo().deviceId(group1dev1).groupId(group1id);
    DeviceInfo device2 = new DeviceInfo().deviceId(group1dev2).groupId(group1id);
    DeviceInfo device3 = new DeviceInfo().deviceId(group2dev1).groupId(group2id);
    DeviceInfo device4 = new DeviceInfo().deviceId(group2dev2).groupId(group2id);
    DeviceInfo device5 = new DeviceInfo().deviceId(group3dev1).groupId(group3id);
    DeviceInfo device6 = new DeviceInfo().deviceId(group3dev2).groupId(group3id);
    DeviceInfo device7 = new DeviceInfo().deviceId(group4dev1).groupId(group4id);
    DeviceInfo device8 = new DeviceInfo().deviceId(group4dev2).groupId(group4id);
    DeviceInfo device9 = new DeviceInfo().deviceId(unrelatedDeviceId);

    Map<UUID, DeviceInfo> deviceMap = new HashMap<>();
    deviceMap.put(group1dev1, device1);
    deviceMap.put(group1dev2, device2);
    deviceMap.put(group2dev1, device3);
    deviceMap.put(group2dev2, device4);
    deviceMap.put(group3dev1, device5);
    deviceMap.put(group3dev2, device6);
    deviceMap.put(group4dev1, device7);
    deviceMap.put(group4dev2, device8);
    deviceMap.put(unrelatedDeviceId, device9);

    GroupRawInfo group1Raw = new GroupRawInfo();
    group1Raw.setGroupId(group1id);
    group1Raw.setName(group1name);
    group1Raw.setDescription(group1descr);
    group1Raw.setComment(group1commn);
    group1Raw.setIsEnabled(group1isEnabled);
    GroupRawInfo group2Raw = new GroupRawInfo();
    group2Raw.setGroupId(group2id);
    group2Raw.setName(group2name);
    group2Raw.setDescription(group2descr);
    group2Raw.setComment(group2commn);
    group2Raw.setIsEnabled(group2isEnabled);
    GroupRawInfo group3Raw = new GroupRawInfo();
    group3Raw.setGroupId(group3id);
    group3Raw.setName(group3name);
    group3Raw.setDescription(group3descr);
    group3Raw.setComment(group3commn);
    group3Raw.setIsEnabled(group3isEnabled);
    GroupRawInfo group4Raw = new GroupRawInfo();
    group4Raw.setGroupId(group4id);
    group4Raw.setName(group4name);
    group4Raw.setDescription(group4descr);
    group4Raw.setComment(group4commn);
    group4Raw.setIsEnabled(group4isEnabled);

    Map<UUID, GroupRawInfo> groupMap = new HashMap<>();
    groupMap.put(group1id, group1Raw);
    groupMap.put(group2id, group2Raw);
    groupMap.put(group3id, group3Raw);
    groupMap.put(group4id, group4Raw);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(groupMap).when(this.groupRepository).findAll();
    doReturn(deviceMap).when(deviceRepository).findAll();

    // Call service
    CommonGetListResponse<GroupInfo> retListResp = groupService.getGroups(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.groupRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getGroups (skip = 0, limit = entry max)")
  void testGetGroupsSkipMinLimitMax() {
    Integer skip = 0;
    Integer limit = null; // Will be changed later

    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");
    UUID group2id = UUID.fromString("05abcdef-2222-2222-2222-222222222202");
    UUID group3id = UUID.fromString("05abcdef-3333-3333-3333-333333333303");
    UUID group4id = UUID.fromString("05abcdef-4444-4444-4444-444444444404");
    String group1name = "D a Group Name 1";
    String group2name = "C a Group Name 2";
    String group3name = "A a Group Name 3";
    String group4name = "B a Group Name 4";
    String group1descr = "a Group Description 1";
    String group2descr = "a Group Description 2";
    String group3descr = "a Group Description 3";
    String group4descr = "a Group Description 4";
    String group1commn = "a Group Comment 1";
    String group2commn = "a Group Comment 2";
    String group3commn = "a Group Comment 3";
    String group4commn = "a Group Comment 4";
    Boolean group1isEnabled = true;
    Boolean group2isEnabled = true;
    Boolean group3isEnabled = false;
    Boolean group4isEnabled = true;

    UUID group1dev1 = UUID.fromString("15abcdef-9999-1111-1111-111111111101");
    UUID group1dev2 = UUID.fromString("15abcdef-9999-2222-1111-111111111101");
    UUID group2dev1 = UUID.fromString("15abcdef-9999-1111-2222-222222222202");
    UUID group2dev2 = UUID.fromString("15abcdef-9999-2222-2222-222222222202");
    UUID group3dev1 = UUID.fromString("15abcdef-9999-1111-3333-333333333303");
    UUID group3dev2 = UUID.fromString("15abcdef-9999-2222-3333-333333333303");
    UUID group4dev1 = UUID.fromString("15abcdef-9999-1111-4444-444444444404");
    UUID group4dev2 = UUID.fromString("15abcdef-9999-2222-4444-444444444404");

    UUID unrelatedDeviceId = UUID.fromString("99999999-9999-9999-9999-999999999999");
    DeviceInfo device1 = new DeviceInfo().deviceId(group1dev1).groupId(group1id);
    DeviceInfo device2 = new DeviceInfo().deviceId(group1dev2).groupId(group1id);
    DeviceInfo device3 = new DeviceInfo().deviceId(group2dev1).groupId(group2id);
    DeviceInfo device4 = new DeviceInfo().deviceId(group2dev2).groupId(group2id);
    DeviceInfo device5 = new DeviceInfo().deviceId(group3dev1).groupId(group3id);
    DeviceInfo device6 = new DeviceInfo().deviceId(group3dev2).groupId(group3id);
    DeviceInfo device7 = new DeviceInfo().deviceId(group4dev1).groupId(group4id);
    DeviceInfo device8 = new DeviceInfo().deviceId(group4dev2).groupId(group4id);
    DeviceInfo device9 = new DeviceInfo().deviceId(unrelatedDeviceId);

    Map<UUID, DeviceInfo> deviceMap = new HashMap<>();
    deviceMap.put(group1dev1, device1);
    deviceMap.put(group1dev2, device2);
    deviceMap.put(group2dev1, device3);
    deviceMap.put(group2dev2, device4);
    deviceMap.put(group3dev1, device5);
    deviceMap.put(group3dev2, device6);
    deviceMap.put(group4dev1, device7);
    deviceMap.put(group4dev2, device8);
    deviceMap.put(unrelatedDeviceId, device9);

    GroupRawInfo group1Raw = new GroupRawInfo();
    group1Raw.setGroupId(group1id);
    group1Raw.setName(group1name);
    group1Raw.setDescription(group1descr);
    group1Raw.setComment(group1commn);
    group1Raw.setIsEnabled(group1isEnabled);
    GroupRawInfo group2Raw = new GroupRawInfo();
    group2Raw.setGroupId(group2id);
    group2Raw.setName(group2name);
    group2Raw.setDescription(group2descr);
    group2Raw.setComment(group2commn);
    group2Raw.setIsEnabled(group2isEnabled);
    GroupRawInfo group3Raw = new GroupRawInfo();
    group3Raw.setGroupId(group3id);
    group3Raw.setName(group3name);
    group3Raw.setDescription(group3descr);
    group3Raw.setComment(group3commn);
    group3Raw.setIsEnabled(group3isEnabled);
    GroupRawInfo group4Raw = new GroupRawInfo();
    group4Raw.setGroupId(group4id);
    group4Raw.setName(group4name);
    group4Raw.setDescription(group4descr);
    group4Raw.setComment(group4commn);
    group4Raw.setIsEnabled(group4isEnabled);

    Map<UUID, GroupRawInfo> groupMap = new HashMap<>();
    groupMap.put(group1id, group1Raw);
    groupMap.put(group2id, group2Raw);
    groupMap.put(group3id, group3Raw);
    groupMap.put(group4id, group4Raw);

    limit = groupMap.size();

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(groupMap).when(this.groupRepository).findAll();
    doReturn(deviceMap).when(deviceRepository).findAll();

    // Call service
    CommonGetListResponse<GroupInfo> retListResp = groupService.getGroups(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.groupRepository, times(1)).findAll();

    // Expected Values
    GroupInfo group1 = new GroupInfo();
    group1.setGroupId(group1id);
    group1.setName(group1name);
    group1.setDescription(group1descr);
    group1.setComment(group1commn);
    group1.setIsEnabled(group1isEnabled);
    group1.addDevicesItem(group1dev1);
    group1.addDevicesItem(group1dev2);
    GroupInfo group2 = new GroupInfo();
    group2.setGroupId(group2id);
    group2.setName(group2name);
    group2.setDescription(group2descr);
    group2.setComment(group2commn);
    group2.setIsEnabled(group2isEnabled);
    group2.addDevicesItem(group2dev1);
    group2.addDevicesItem(group2dev2);
    GroupInfo group3 = new GroupInfo();
    group3.setGroupId(group3id);
    group3.setName(group3name);
    group3.setDescription(group3descr);
    group3.setComment(group3commn);
    group3.setIsEnabled(group3isEnabled);
    group3.addDevicesItem(group3dev1);
    group3.addDevicesItem(group3dev2);
    GroupInfo group4 = new GroupInfo();
    group4.setGroupId(group4id);
    group4.setName(group4name);
    group4.setDescription(group4descr);
    group4.setComment(group4commn);
    group4.setIsEnabled(group4isEnabled);
    group4.addDevicesItem(group4dev1);
    group4.addDevicesItem(group4dev2);

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be sorted by group name ascending.",
        retListResp.getObjList(), contains(group3, group4, group2, group1));
  }

  @Test
  @DisplayName("TEST getGroups (skip = entry max)")
  void testGetGroupsSkipMax() {
    Integer skip = null; // Will be changed later
    Integer limit = null;

    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");
    UUID group2id = UUID.fromString("05abcdef-2222-2222-2222-222222222202");
    UUID group3id = UUID.fromString("05abcdef-3333-3333-3333-333333333303");
    UUID group4id = UUID.fromString("05abcdef-4444-4444-4444-444444444404");
    String group1name = "D a Group Name 1";
    String group2name = "C a Group Name 2";
    String group3name = "A a Group Name 3";
    String group4name = "B a Group Name 4";
    String group1descr = "a Group Description 1";
    String group2descr = "a Group Description 2";
    String group3descr = "a Group Description 3";
    String group4descr = "a Group Description 4";
    String group1commn = "a Group Comment 1";
    String group2commn = "a Group Comment 2";
    String group3commn = "a Group Comment 3";
    String group4commn = "a Group Comment 4";
    Boolean group1isEnabled = true;
    Boolean group2isEnabled = true;
    Boolean group3isEnabled = false;
    Boolean group4isEnabled = true;

    UUID group1dev1 = UUID.fromString("15abcdef-9999-1111-1111-111111111101");
    UUID group1dev2 = UUID.fromString("15abcdef-9999-2222-1111-111111111101");
    UUID group2dev1 = UUID.fromString("15abcdef-9999-1111-2222-222222222202");
    UUID group2dev2 = UUID.fromString("15abcdef-9999-2222-2222-222222222202");
    UUID group3dev1 = UUID.fromString("15abcdef-9999-1111-3333-333333333303");
    UUID group3dev2 = UUID.fromString("15abcdef-9999-2222-3333-333333333303");
    UUID group4dev1 = UUID.fromString("15abcdef-9999-1111-4444-444444444404");
    UUID group4dev2 = UUID.fromString("15abcdef-9999-2222-4444-444444444404");

    UUID unrelatedDeviceId = UUID.fromString("99999999-9999-9999-9999-999999999999");
    DeviceInfo device1 = new DeviceInfo().deviceId(group1dev1).groupId(group1id);
    DeviceInfo device2 = new DeviceInfo().deviceId(group1dev2).groupId(group1id);
    DeviceInfo device3 = new DeviceInfo().deviceId(group2dev1).groupId(group2id);
    DeviceInfo device4 = new DeviceInfo().deviceId(group2dev2).groupId(group2id);
    DeviceInfo device5 = new DeviceInfo().deviceId(group3dev1).groupId(group3id);
    DeviceInfo device6 = new DeviceInfo().deviceId(group3dev2).groupId(group3id);
    DeviceInfo device7 = new DeviceInfo().deviceId(group4dev1).groupId(group4id);
    DeviceInfo device8 = new DeviceInfo().deviceId(group4dev2).groupId(group4id);
    DeviceInfo device9 = new DeviceInfo().deviceId(unrelatedDeviceId);

    Map<UUID, DeviceInfo> deviceMap = new HashMap<>();
    deviceMap.put(group1dev1, device1);
    deviceMap.put(group1dev2, device2);
    deviceMap.put(group2dev1, device3);
    deviceMap.put(group2dev2, device4);
    deviceMap.put(group3dev1, device5);
    deviceMap.put(group3dev2, device6);
    deviceMap.put(group4dev1, device7);
    deviceMap.put(group4dev2, device8);
    deviceMap.put(unrelatedDeviceId, device9);

    GroupRawInfo group1Raw = new GroupRawInfo();
    group1Raw.setGroupId(group1id);
    group1Raw.setName(group1name);
    group1Raw.setDescription(group1descr);
    group1Raw.setComment(group1commn);
    group1Raw.setIsEnabled(group1isEnabled);
    GroupRawInfo group2Raw = new GroupRawInfo();
    group2Raw.setGroupId(group2id);
    group2Raw.setName(group2name);
    group2Raw.setDescription(group2descr);
    group2Raw.setComment(group2commn);
    group2Raw.setIsEnabled(group2isEnabled);
    GroupRawInfo group3Raw = new GroupRawInfo();
    group3Raw.setGroupId(group3id);
    group3Raw.setName(group3name);
    group3Raw.setDescription(group3descr);
    group3Raw.setComment(group3commn);
    group3Raw.setIsEnabled(group3isEnabled);
    GroupRawInfo group4Raw = new GroupRawInfo();
    group4Raw.setGroupId(group4id);
    group4Raw.setName(group4name);
    group4Raw.setDescription(group4descr);
    group4Raw.setComment(group4commn);
    group4Raw.setIsEnabled(group4isEnabled);

    Map<UUID, GroupRawInfo> groupMap = new HashMap<>();
    groupMap.put(group1id, group1Raw);
    groupMap.put(group2id, group2Raw);
    groupMap.put(group3id, group3Raw);
    groupMap.put(group4id, group4Raw);

    skip = groupMap.size();

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(groupMap).when(this.groupRepository).findAll();
    doReturn(deviceMap).when(deviceRepository).findAll();

    // Call service
    CommonGetListResponse<GroupInfo> retListResp = groupService.getGroups(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.groupRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getGroups (skip = -1)")
  void testGetGroupsSkipMinMinus1() {
    Integer skip = -1;
    Integer limit = null;

    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");
    UUID group2id = UUID.fromString("05abcdef-2222-2222-2222-222222222202");
    UUID group3id = UUID.fromString("05abcdef-3333-3333-3333-333333333303");
    UUID group4id = UUID.fromString("05abcdef-4444-4444-4444-444444444404");
    String group1name = "D a Group Name 1";
    String group2name = "C a Group Name 2";
    String group3name = "A a Group Name 3";
    String group4name = "B a Group Name 4";
    String group1descr = "a Group Description 1";
    String group2descr = "a Group Description 2";
    String group3descr = "a Group Description 3";
    String group4descr = "a Group Description 4";
    String group1commn = "a Group Comment 1";
    String group2commn = "a Group Comment 2";
    String group3commn = "a Group Comment 3";
    String group4commn = "a Group Comment 4";
    Boolean group1isEnabled = true;
    Boolean group2isEnabled = true;
    Boolean group3isEnabled = false;
    Boolean group4isEnabled = true;

    UUID group1dev1 = UUID.fromString("15abcdef-9999-1111-1111-111111111101");
    UUID group1dev2 = UUID.fromString("15abcdef-9999-2222-1111-111111111101");
    UUID group2dev1 = UUID.fromString("15abcdef-9999-1111-2222-222222222202");
    UUID group2dev2 = UUID.fromString("15abcdef-9999-2222-2222-222222222202");
    UUID group3dev1 = UUID.fromString("15abcdef-9999-1111-3333-333333333303");
    UUID group3dev2 = UUID.fromString("15abcdef-9999-2222-3333-333333333303");
    UUID group4dev1 = UUID.fromString("15abcdef-9999-1111-4444-444444444404");
    UUID group4dev2 = UUID.fromString("15abcdef-9999-2222-4444-444444444404");

    UUID unrelatedDeviceId = UUID.fromString("99999999-9999-9999-9999-999999999999");
    DeviceInfo device1 = new DeviceInfo().deviceId(group1dev1).groupId(group1id);
    DeviceInfo device2 = new DeviceInfo().deviceId(group1dev2).groupId(group1id);
    DeviceInfo device3 = new DeviceInfo().deviceId(group2dev1).groupId(group2id);
    DeviceInfo device4 = new DeviceInfo().deviceId(group2dev2).groupId(group2id);
    DeviceInfo device5 = new DeviceInfo().deviceId(group3dev1).groupId(group3id);
    DeviceInfo device6 = new DeviceInfo().deviceId(group3dev2).groupId(group3id);
    DeviceInfo device7 = new DeviceInfo().deviceId(group4dev1).groupId(group4id);
    DeviceInfo device8 = new DeviceInfo().deviceId(group4dev2).groupId(group4id);
    DeviceInfo device9 = new DeviceInfo().deviceId(unrelatedDeviceId);

    Map<UUID, DeviceInfo> deviceMap = new HashMap<>();
    deviceMap.put(group1dev1, device1);
    deviceMap.put(group1dev2, device2);
    deviceMap.put(group2dev1, device3);
    deviceMap.put(group2dev2, device4);
    deviceMap.put(group3dev1, device5);
    deviceMap.put(group3dev2, device6);
    deviceMap.put(group4dev1, device7);
    deviceMap.put(group4dev2, device8);
    deviceMap.put(unrelatedDeviceId, device9);

    GroupRawInfo group1Raw = new GroupRawInfo();
    group1Raw.setGroupId(group1id);
    group1Raw.setName(group1name);
    group1Raw.setDescription(group1descr);
    group1Raw.setComment(group1commn);
    group1Raw.setIsEnabled(group1isEnabled);
    GroupRawInfo group2Raw = new GroupRawInfo();
    group2Raw.setGroupId(group2id);
    group2Raw.setName(group2name);
    group2Raw.setDescription(group2descr);
    group2Raw.setComment(group2commn);
    group2Raw.setIsEnabled(group2isEnabled);
    GroupRawInfo group3Raw = new GroupRawInfo();
    group3Raw.setGroupId(group3id);
    group3Raw.setName(group3name);
    group3Raw.setDescription(group3descr);
    group3Raw.setComment(group3commn);
    group3Raw.setIsEnabled(group3isEnabled);
    GroupRawInfo group4Raw = new GroupRawInfo();
    group4Raw.setGroupId(group4id);
    group4Raw.setName(group4name);
    group4Raw.setDescription(group4descr);
    group4Raw.setComment(group4commn);
    group4Raw.setIsEnabled(group4isEnabled);

    Map<UUID, GroupRawInfo> groupMap = new HashMap<>();
    groupMap.put(group1id, group1Raw);
    groupMap.put(group2id, group2Raw);
    groupMap.put(group3id, group3Raw);
    groupMap.put(group4id, group4Raw);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    doReturn(groupMap).when(this.groupRepository).findAll();
    doReturn(deviceMap).when(deviceRepository).findAll();

    // Call service
    CommonGetListResponse<GroupInfo> retListResp = groupService.getGroups(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.groupRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 400.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getGroups (limit = -1)")
  void testGetGroupsLimitMinMinus1() {
    Integer skip = null;
    Integer limit = -1;

    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");
    UUID group2id = UUID.fromString("05abcdef-2222-2222-2222-222222222202");
    UUID group3id = UUID.fromString("05abcdef-3333-3333-3333-333333333303");
    UUID group4id = UUID.fromString("05abcdef-4444-4444-4444-444444444404");
    String group1name = "D a Group Name 1";
    String group2name = "C a Group Name 2";
    String group3name = "A a Group Name 3";
    String group4name = "B a Group Name 4";
    String group1descr = "a Group Description 1";
    String group2descr = "a Group Description 2";
    String group3descr = "a Group Description 3";
    String group4descr = "a Group Description 4";
    String group1commn = "a Group Comment 1";
    String group2commn = "a Group Comment 2";
    String group3commn = "a Group Comment 3";
    String group4commn = "a Group Comment 4";
    Boolean group1isEnabled = true;
    Boolean group2isEnabled = true;
    Boolean group3isEnabled = false;
    Boolean group4isEnabled = true;

    UUID group1dev1 = UUID.fromString("15abcdef-9999-1111-1111-111111111101");
    UUID group1dev2 = UUID.fromString("15abcdef-9999-2222-1111-111111111101");
    UUID group2dev1 = UUID.fromString("15abcdef-9999-1111-2222-222222222202");
    UUID group2dev2 = UUID.fromString("15abcdef-9999-2222-2222-222222222202");
    UUID group3dev1 = UUID.fromString("15abcdef-9999-1111-3333-333333333303");
    UUID group3dev2 = UUID.fromString("15abcdef-9999-2222-3333-333333333303");
    UUID group4dev1 = UUID.fromString("15abcdef-9999-1111-4444-444444444404");
    UUID group4dev2 = UUID.fromString("15abcdef-9999-2222-4444-444444444404");

    UUID unrelatedDeviceId = UUID.fromString("99999999-9999-9999-9999-999999999999");
    DeviceInfo device1 = new DeviceInfo().deviceId(group1dev1).groupId(group1id);
    DeviceInfo device2 = new DeviceInfo().deviceId(group1dev2).groupId(group1id);
    DeviceInfo device3 = new DeviceInfo().deviceId(group2dev1).groupId(group2id);
    DeviceInfo device4 = new DeviceInfo().deviceId(group2dev2).groupId(group2id);
    DeviceInfo device5 = new DeviceInfo().deviceId(group3dev1).groupId(group3id);
    DeviceInfo device6 = new DeviceInfo().deviceId(group3dev2).groupId(group3id);
    DeviceInfo device7 = new DeviceInfo().deviceId(group4dev1).groupId(group4id);
    DeviceInfo device8 = new DeviceInfo().deviceId(group4dev2).groupId(group4id);
    DeviceInfo device9 = new DeviceInfo().deviceId(unrelatedDeviceId);

    Map<UUID, DeviceInfo> deviceMap = new HashMap<>();
    deviceMap.put(group1dev1, device1);
    deviceMap.put(group1dev2, device2);
    deviceMap.put(group2dev1, device3);
    deviceMap.put(group2dev2, device4);
    deviceMap.put(group3dev1, device5);
    deviceMap.put(group3dev2, device6);
    deviceMap.put(group4dev1, device7);
    deviceMap.put(group4dev2, device8);
    deviceMap.put(unrelatedDeviceId, device9);

    GroupRawInfo group1Raw = new GroupRawInfo();
    group1Raw.setGroupId(group1id);
    group1Raw.setName(group1name);
    group1Raw.setDescription(group1descr);
    group1Raw.setComment(group1commn);
    group1Raw.setIsEnabled(group1isEnabled);
    GroupRawInfo group2Raw = new GroupRawInfo();
    group2Raw.setGroupId(group2id);
    group2Raw.setName(group2name);
    group2Raw.setDescription(group2descr);
    group2Raw.setComment(group2commn);
    group2Raw.setIsEnabled(group2isEnabled);
    GroupRawInfo group3Raw = new GroupRawInfo();
    group3Raw.setGroupId(group3id);
    group3Raw.setName(group3name);
    group3Raw.setDescription(group3descr);
    group3Raw.setComment(group3commn);
    group3Raw.setIsEnabled(group3isEnabled);
    GroupRawInfo group4Raw = new GroupRawInfo();
    group4Raw.setGroupId(group4id);
    group4Raw.setName(group4name);
    group4Raw.setDescription(group4descr);
    group4Raw.setComment(group4commn);
    group4Raw.setIsEnabled(group4isEnabled);

    Map<UUID, GroupRawInfo> groupMap = new HashMap<>();
    groupMap.put(group1id, group1Raw);
    groupMap.put(group2id, group2Raw);
    groupMap.put(group3id, group3Raw);
    groupMap.put(group4id, group4Raw);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    doReturn(groupMap).when(this.groupRepository).findAll();
    doReturn(deviceMap).when(deviceRepository).findAll();

    // Call service
    CommonGetListResponse<GroupInfo> retListResp = groupService.getGroups(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.groupRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 400.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getGroups (skip = entry max + 1)")
  void testGetGroupsSkipMaxPlus1() {
    Integer skip = null; // Will be changed later
    Integer limit = null;

    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");
    UUID group2id = UUID.fromString("05abcdef-2222-2222-2222-222222222202");
    UUID group3id = UUID.fromString("05abcdef-3333-3333-3333-333333333303");
    UUID group4id = UUID.fromString("05abcdef-4444-4444-4444-444444444404");
    String group1name = "D a Group Name 1";
    String group2name = "C a Group Name 2";
    String group3name = "A a Group Name 3";
    String group4name = "B a Group Name 4";
    String group1descr = "a Group Description 1";
    String group2descr = "a Group Description 2";
    String group3descr = "a Group Description 3";
    String group4descr = "a Group Description 4";
    String group1commn = "a Group Comment 1";
    String group2commn = "a Group Comment 2";
    String group3commn = "a Group Comment 3";
    String group4commn = "a Group Comment 4";
    Boolean group1isEnabled = true;
    Boolean group2isEnabled = true;
    Boolean group3isEnabled = false;
    Boolean group4isEnabled = true;

    UUID group1dev1 = UUID.fromString("15abcdef-9999-1111-1111-111111111101");
    UUID group1dev2 = UUID.fromString("15abcdef-9999-2222-1111-111111111101");
    UUID group2dev1 = UUID.fromString("15abcdef-9999-1111-2222-222222222202");
    UUID group2dev2 = UUID.fromString("15abcdef-9999-2222-2222-222222222202");
    UUID group3dev1 = UUID.fromString("15abcdef-9999-1111-3333-333333333303");
    UUID group3dev2 = UUID.fromString("15abcdef-9999-2222-3333-333333333303");
    UUID group4dev1 = UUID.fromString("15abcdef-9999-1111-4444-444444444404");
    UUID group4dev2 = UUID.fromString("15abcdef-9999-2222-4444-444444444404");

    UUID unrelatedDeviceId = UUID.fromString("99999999-9999-9999-9999-999999999999");
    DeviceInfo device1 = new DeviceInfo().deviceId(group1dev1).groupId(group1id);
    DeviceInfo device2 = new DeviceInfo().deviceId(group1dev2).groupId(group1id);
    DeviceInfo device3 = new DeviceInfo().deviceId(group2dev1).groupId(group2id);
    DeviceInfo device4 = new DeviceInfo().deviceId(group2dev2).groupId(group2id);
    DeviceInfo device5 = new DeviceInfo().deviceId(group3dev1).groupId(group3id);
    DeviceInfo device6 = new DeviceInfo().deviceId(group3dev2).groupId(group3id);
    DeviceInfo device7 = new DeviceInfo().deviceId(group4dev1).groupId(group4id);
    DeviceInfo device8 = new DeviceInfo().deviceId(group4dev2).groupId(group4id);
    DeviceInfo device9 = new DeviceInfo().deviceId(unrelatedDeviceId);

    Map<UUID, DeviceInfo> deviceMap = new HashMap<>();
    deviceMap.put(group1dev1, device1);
    deviceMap.put(group1dev2, device2);
    deviceMap.put(group2dev1, device3);
    deviceMap.put(group2dev2, device4);
    deviceMap.put(group3dev1, device5);
    deviceMap.put(group3dev2, device6);
    deviceMap.put(group4dev1, device7);
    deviceMap.put(group4dev2, device8);
    deviceMap.put(unrelatedDeviceId, device9);

    GroupRawInfo group1Raw = new GroupRawInfo();
    group1Raw.setGroupId(group1id);
    group1Raw.setName(group1name);
    group1Raw.setDescription(group1descr);
    group1Raw.setComment(group1commn);
    group1Raw.setIsEnabled(group1isEnabled);
    GroupRawInfo group2Raw = new GroupRawInfo();
    group2Raw.setGroupId(group2id);
    group2Raw.setName(group2name);
    group2Raw.setDescription(group2descr);
    group2Raw.setComment(group2commn);
    group2Raw.setIsEnabled(group2isEnabled);
    GroupRawInfo group3Raw = new GroupRawInfo();
    group3Raw.setGroupId(group3id);
    group3Raw.setName(group3name);
    group3Raw.setDescription(group3descr);
    group3Raw.setComment(group3commn);
    group3Raw.setIsEnabled(group3isEnabled);
    GroupRawInfo group4Raw = new GroupRawInfo();
    group4Raw.setGroupId(group4id);
    group4Raw.setName(group4name);
    group4Raw.setDescription(group4descr);
    group4Raw.setComment(group4commn);
    group4Raw.setIsEnabled(group4isEnabled);

    Map<UUID, GroupRawInfo> groupMap = new HashMap<>();
    groupMap.put(group1id, group1Raw);
    groupMap.put(group2id, group2Raw);
    groupMap.put(group3id, group3Raw);
    groupMap.put(group4id, group4Raw);

    skip = groupMap.size() + 1;

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(groupMap).when(this.groupRepository).findAll();
    doReturn(deviceMap).when(deviceRepository).findAll();

    // Call service
    CommonGetListResponse<GroupInfo> retListResp = groupService.getGroups(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.groupRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getGroups (limit = entry max + 1)")
  void testGetGroupsLimitMaxPlus1() {
    Integer skip = null;
    Integer limit = null; // Will be changed later

    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");
    UUID group2id = UUID.fromString("05abcdef-2222-2222-2222-222222222202");
    UUID group3id = UUID.fromString("05abcdef-3333-3333-3333-333333333303");
    UUID group4id = UUID.fromString("05abcdef-4444-4444-4444-444444444404");
    String group1name = "D a Group Name 1";
    String group2name = "C a Group Name 2";
    String group3name = "A a Group Name 3";
    String group4name = "B a Group Name 4";
    String group1descr = "a Group Description 1";
    String group2descr = "a Group Description 2";
    String group3descr = "a Group Description 3";
    String group4descr = "a Group Description 4";
    String group1commn = "a Group Comment 1";
    String group2commn = "a Group Comment 2";
    String group3commn = "a Group Comment 3";
    String group4commn = "a Group Comment 4";
    Boolean group1isEnabled = true;
    Boolean group2isEnabled = true;
    Boolean group3isEnabled = false;
    Boolean group4isEnabled = true;

    UUID group1dev1 = UUID.fromString("15abcdef-9999-1111-1111-111111111101");
    UUID group1dev2 = UUID.fromString("15abcdef-9999-2222-1111-111111111101");
    UUID group2dev1 = UUID.fromString("15abcdef-9999-1111-2222-222222222202");
    UUID group2dev2 = UUID.fromString("15abcdef-9999-2222-2222-222222222202");
    UUID group3dev1 = UUID.fromString("15abcdef-9999-1111-3333-333333333303");
    UUID group3dev2 = UUID.fromString("15abcdef-9999-2222-3333-333333333303");
    UUID group4dev1 = UUID.fromString("15abcdef-9999-1111-4444-444444444404");
    UUID group4dev2 = UUID.fromString("15abcdef-9999-2222-4444-444444444404");

    UUID unrelatedDeviceId = UUID.fromString("99999999-9999-9999-9999-999999999999");
    DeviceInfo device1 = new DeviceInfo().deviceId(group1dev1).groupId(group1id);
    DeviceInfo device2 = new DeviceInfo().deviceId(group1dev2).groupId(group1id);
    DeviceInfo device3 = new DeviceInfo().deviceId(group2dev1).groupId(group2id);
    DeviceInfo device4 = new DeviceInfo().deviceId(group2dev2).groupId(group2id);
    DeviceInfo device5 = new DeviceInfo().deviceId(group3dev1).groupId(group3id);
    DeviceInfo device6 = new DeviceInfo().deviceId(group3dev2).groupId(group3id);
    DeviceInfo device7 = new DeviceInfo().deviceId(group4dev1).groupId(group4id);
    DeviceInfo device8 = new DeviceInfo().deviceId(group4dev2).groupId(group4id);
    DeviceInfo device9 = new DeviceInfo().deviceId(unrelatedDeviceId);

    Map<UUID, DeviceInfo> deviceMap = new HashMap<>();
    deviceMap.put(group1dev1, device1);
    deviceMap.put(group1dev2, device2);
    deviceMap.put(group2dev1, device3);
    deviceMap.put(group2dev2, device4);
    deviceMap.put(group3dev1, device5);
    deviceMap.put(group3dev2, device6);
    deviceMap.put(group4dev1, device7);
    deviceMap.put(group4dev2, device8);
    deviceMap.put(unrelatedDeviceId, device9);

    GroupRawInfo group1Raw = new GroupRawInfo();
    group1Raw.setGroupId(group1id);
    group1Raw.setName(group1name);
    group1Raw.setDescription(group1descr);
    group1Raw.setComment(group1commn);
    group1Raw.setIsEnabled(group1isEnabled);
    GroupRawInfo group2Raw = new GroupRawInfo();
    group2Raw.setGroupId(group2id);
    group2Raw.setName(group2name);
    group2Raw.setDescription(group2descr);
    group2Raw.setComment(group2commn);
    group2Raw.setIsEnabled(group2isEnabled);
    GroupRawInfo group3Raw = new GroupRawInfo();
    group3Raw.setGroupId(group3id);
    group3Raw.setName(group3name);
    group3Raw.setDescription(group3descr);
    group3Raw.setComment(group3commn);
    group3Raw.setIsEnabled(group3isEnabled);
    GroupRawInfo group4Raw = new GroupRawInfo();
    group4Raw.setGroupId(group4id);
    group4Raw.setName(group4name);
    group4Raw.setDescription(group4descr);
    group4Raw.setComment(group4commn);
    group4Raw.setIsEnabled(group4isEnabled);

    Map<UUID, GroupRawInfo> groupMap = new HashMap<>();
    groupMap.put(group1id, group1Raw);
    groupMap.put(group2id, group2Raw);
    groupMap.put(group3id, group3Raw);
    groupMap.put(group4id, group4Raw);

    limit = groupMap.size() + 1;

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(groupMap).when(this.groupRepository).findAll();
    doReturn(deviceMap).when(deviceRepository).findAll();

    // Call service
    CommonGetListResponse<GroupInfo> retListResp = groupService.getGroups(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.groupRepository, times(1)).findAll();

    // Expected Values
    GroupInfo group1 = new GroupInfo();
    group1.setGroupId(group1id);
    group1.setName(group1name);
    group1.setDescription(group1descr);
    group1.setComment(group1commn);
    group1.setIsEnabled(group1isEnabled);
    group1.addDevicesItem(group1dev1);
    group1.addDevicesItem(group1dev2);
    GroupInfo group2 = new GroupInfo();
    group2.setGroupId(group2id);
    group2.setName(group2name);
    group2.setDescription(group2descr);
    group2.setComment(group2commn);
    group2.setIsEnabled(group2isEnabled);
    group2.addDevicesItem(group2dev1);
    group2.addDevicesItem(group2dev2);
    GroupInfo group3 = new GroupInfo();
    group3.setGroupId(group3id);
    group3.setName(group3name);
    group3.setDescription(group3descr);
    group3.setComment(group3commn);
    group3.setIsEnabled(group3isEnabled);
    group3.addDevicesItem(group3dev1);
    group3.addDevicesItem(group3dev2);
    GroupInfo group4 = new GroupInfo();
    group4.setGroupId(group4id);
    group4.setName(group4name);
    group4.setDescription(group4descr);
    group4.setComment(group4commn);
    group4.setIsEnabled(group4isEnabled);
    group4.addDevicesItem(group4dev1);
    group4.addDevicesItem(group4dev2);

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be sorted by group name ascending.",
        retListResp.getObjList(), contains(group3, group4, group2, group1));
  }

  @Test
  @DisplayName("TEST getGroups (skip = 1, limit = 1)")
  void testGetGroupsSkip1Limit1() {
    Integer skip = 1;
    Integer limit = 1;

    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");
    UUID group2id = UUID.fromString("05abcdef-2222-2222-2222-222222222202");
    UUID group3id = UUID.fromString("05abcdef-3333-3333-3333-333333333303");
    UUID group4id = UUID.fromString("05abcdef-4444-4444-4444-444444444404");
    String group1name = "D a Group Name 1";
    String group2name = "C a Group Name 2";
    String group3name = "A a Group Name 3";
    String group4name = "B a Group Name 4";
    String group1descr = "a Group Description 1";
    String group2descr = "a Group Description 2";
    String group3descr = "a Group Description 3";
    String group4descr = "a Group Description 4";
    String group1commn = "a Group Comment 1";
    String group2commn = "a Group Comment 2";
    String group3commn = "a Group Comment 3";
    String group4commn = "a Group Comment 4";
    Boolean group1isEnabled = true;
    Boolean group2isEnabled = true;
    Boolean group3isEnabled = false;
    Boolean group4isEnabled = true;

    UUID group1dev1 = UUID.fromString("15abcdef-9999-1111-1111-111111111101");
    UUID group1dev2 = UUID.fromString("15abcdef-9999-2222-1111-111111111101");
    UUID group2dev1 = UUID.fromString("15abcdef-9999-1111-2222-222222222202");
    UUID group2dev2 = UUID.fromString("15abcdef-9999-2222-2222-222222222202");
    UUID group3dev1 = UUID.fromString("15abcdef-9999-1111-3333-333333333303");
    UUID group3dev2 = UUID.fromString("15abcdef-9999-2222-3333-333333333303");
    UUID group4dev1 = UUID.fromString("15abcdef-9999-1111-4444-444444444404");
    UUID group4dev2 = UUID.fromString("15abcdef-9999-2222-4444-444444444404");

    UUID unrelatedDeviceId = UUID.fromString("99999999-9999-9999-9999-999999999999");
    DeviceInfo device1 = new DeviceInfo().deviceId(group1dev1).groupId(group1id);
    DeviceInfo device2 = new DeviceInfo().deviceId(group1dev2).groupId(group1id);
    DeviceInfo device3 = new DeviceInfo().deviceId(group2dev1).groupId(group2id);
    DeviceInfo device4 = new DeviceInfo().deviceId(group2dev2).groupId(group2id);
    DeviceInfo device5 = new DeviceInfo().deviceId(group3dev1).groupId(group3id);
    DeviceInfo device6 = new DeviceInfo().deviceId(group3dev2).groupId(group3id);
    DeviceInfo device7 = new DeviceInfo().deviceId(group4dev1).groupId(group4id);
    DeviceInfo device8 = new DeviceInfo().deviceId(group4dev2).groupId(group4id);
    DeviceInfo device9 = new DeviceInfo().deviceId(unrelatedDeviceId);

    Map<UUID, DeviceInfo> deviceMap = new HashMap<>();
    deviceMap.put(group1dev1, device1);
    deviceMap.put(group1dev2, device2);
    deviceMap.put(group2dev1, device3);
    deviceMap.put(group2dev2, device4);
    deviceMap.put(group3dev1, device5);
    deviceMap.put(group3dev2, device6);
    deviceMap.put(group4dev1, device7);
    deviceMap.put(group4dev2, device8);
    deviceMap.put(unrelatedDeviceId, device9);

    GroupRawInfo group1Raw = new GroupRawInfo();
    group1Raw.setGroupId(group1id);
    group1Raw.setName(group1name);
    group1Raw.setDescription(group1descr);
    group1Raw.setComment(group1commn);
    group1Raw.setIsEnabled(group1isEnabled);
    GroupRawInfo group2Raw = new GroupRawInfo();
    group2Raw.setGroupId(group2id);
    group2Raw.setName(group2name);
    group2Raw.setDescription(group2descr);
    group2Raw.setComment(group2commn);
    group2Raw.setIsEnabled(group2isEnabled);
    GroupRawInfo group3Raw = new GroupRawInfo();
    group3Raw.setGroupId(group3id);
    group3Raw.setName(group3name);
    group3Raw.setDescription(group3descr);
    group3Raw.setComment(group3commn);
    group3Raw.setIsEnabled(group3isEnabled);
    GroupRawInfo group4Raw = new GroupRawInfo();
    group4Raw.setGroupId(group4id);
    group4Raw.setName(group4name);
    group4Raw.setDescription(group4descr);
    group4Raw.setComment(group4commn);
    group4Raw.setIsEnabled(group4isEnabled);

    Map<UUID, GroupRawInfo> groupMap = new HashMap<>();
    groupMap.put(group1id, group1Raw);
    groupMap.put(group2id, group2Raw);
    groupMap.put(group3id, group3Raw);
    groupMap.put(group4id, group4Raw);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(groupMap).when(this.groupRepository).findAll();
    doReturn(deviceMap).when(deviceRepository).findAll();

    // Call service
    CommonGetListResponse<GroupInfo> retListResp = groupService.getGroups(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.groupRepository, times(1)).findAll();

    // Expected Values
    GroupInfo group1 = new GroupInfo();
    group1.setGroupId(group1id);
    group1.setName(group1name);
    group1.setDescription(group1descr);
    group1.setComment(group1commn);
    group1.setIsEnabled(group1isEnabled);
    group1.addDevicesItem(group1dev1);
    group1.addDevicesItem(group1dev2);
    GroupInfo group2 = new GroupInfo();
    group2.setGroupId(group2id);
    group2.setName(group2name);
    group2.setDescription(group2descr);
    group2.setComment(group2commn);
    group2.setIsEnabled(group2isEnabled);
    group2.addDevicesItem(group2dev1);
    group2.addDevicesItem(group2dev2);
    GroupInfo group3 = new GroupInfo();
    group3.setGroupId(group3id);
    group3.setName(group3name);
    group3.setDescription(group3descr);
    group3.setComment(group3commn);
    group3.setIsEnabled(group3isEnabled);
    group3.addDevicesItem(group3dev1);
    group3.addDevicesItem(group3dev2);
    GroupInfo group4 = new GroupInfo();
    group4.setGroupId(group4id);
    group4.setName(group4name);
    group4.setDescription(group4descr);
    group4.setComment(group4commn);
    group4.setIsEnabled(group4isEnabled);
    group4.addDevicesItem(group4dev1);
    group4.addDevicesItem(group4dev2);

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must only be one, the second entry only.",
        retListResp.getObjList(), contains(group4));
  }

  @Test
  @DisplayName("TEST getGroups (skip = 2)")
  void testGetGroupsSkip2() {
    Integer skip = 2;
    Integer limit = null;

    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");
    UUID group2id = UUID.fromString("05abcdef-2222-2222-2222-222222222202");
    UUID group3id = UUID.fromString("05abcdef-3333-3333-3333-333333333303");
    UUID group4id = UUID.fromString("05abcdef-4444-4444-4444-444444444404");
    String group1name = "D a Group Name 1";
    String group2name = "C a Group Name 2";
    String group3name = "A a Group Name 3";
    String group4name = "B a Group Name 4";
    String group1descr = "a Group Description 1";
    String group2descr = "a Group Description 2";
    String group3descr = "a Group Description 3";
    String group4descr = "a Group Description 4";
    String group1commn = "a Group Comment 1";
    String group2commn = "a Group Comment 2";
    String group3commn = "a Group Comment 3";
    String group4commn = "a Group Comment 4";
    Boolean group1isEnabled = true;
    Boolean group2isEnabled = true;
    Boolean group3isEnabled = false;
    Boolean group4isEnabled = true;

    UUID group1dev1 = UUID.fromString("15abcdef-9999-1111-1111-111111111101");
    UUID group1dev2 = UUID.fromString("15abcdef-9999-2222-1111-111111111101");
    UUID group2dev1 = UUID.fromString("15abcdef-9999-1111-2222-222222222202");
    UUID group2dev2 = UUID.fromString("15abcdef-9999-2222-2222-222222222202");
    UUID group3dev1 = UUID.fromString("15abcdef-9999-1111-3333-333333333303");
    UUID group3dev2 = UUID.fromString("15abcdef-9999-2222-3333-333333333303");
    UUID group4dev1 = UUID.fromString("15abcdef-9999-1111-4444-444444444404");
    UUID group4dev2 = UUID.fromString("15abcdef-9999-2222-4444-444444444404");

    UUID unrelatedDeviceId = UUID.fromString("99999999-9999-9999-9999-999999999999");
    DeviceInfo device1 = new DeviceInfo().deviceId(group1dev1).groupId(group1id);
    DeviceInfo device2 = new DeviceInfo().deviceId(group1dev2).groupId(group1id);
    DeviceInfo device3 = new DeviceInfo().deviceId(group2dev1).groupId(group2id);
    DeviceInfo device4 = new DeviceInfo().deviceId(group2dev2).groupId(group2id);
    DeviceInfo device5 = new DeviceInfo().deviceId(group3dev1).groupId(group3id);
    DeviceInfo device6 = new DeviceInfo().deviceId(group3dev2).groupId(group3id);
    DeviceInfo device7 = new DeviceInfo().deviceId(group4dev1).groupId(group4id);
    DeviceInfo device8 = new DeviceInfo().deviceId(group4dev2).groupId(group4id);
    DeviceInfo device9 = new DeviceInfo().deviceId(unrelatedDeviceId);

    Map<UUID, DeviceInfo> deviceMap = new HashMap<>();
    deviceMap.put(group1dev1, device1);
    deviceMap.put(group1dev2, device2);
    deviceMap.put(group2dev1, device3);
    deviceMap.put(group2dev2, device4);
    deviceMap.put(group3dev1, device5);
    deviceMap.put(group3dev2, device6);
    deviceMap.put(group4dev1, device7);
    deviceMap.put(group4dev2, device8);
    deviceMap.put(unrelatedDeviceId, device9);

    GroupRawInfo group1Raw = new GroupRawInfo();
    group1Raw.setGroupId(group1id);
    group1Raw.setName(group1name);
    group1Raw.setDescription(group1descr);
    group1Raw.setComment(group1commn);
    group1Raw.setIsEnabled(group1isEnabled);
    GroupRawInfo group2Raw = new GroupRawInfo();
    group2Raw.setGroupId(group2id);
    group2Raw.setName(group2name);
    group2Raw.setDescription(group2descr);
    group2Raw.setComment(group2commn);
    group2Raw.setIsEnabled(group2isEnabled);
    GroupRawInfo group3Raw = new GroupRawInfo();
    group3Raw.setGroupId(group3id);
    group3Raw.setName(group3name);
    group3Raw.setDescription(group3descr);
    group3Raw.setComment(group3commn);
    group3Raw.setIsEnabled(group3isEnabled);
    GroupRawInfo group4Raw = new GroupRawInfo();
    group4Raw.setGroupId(group4id);
    group4Raw.setName(group4name);
    group4Raw.setDescription(group4descr);
    group4Raw.setComment(group4commn);
    group4Raw.setIsEnabled(group4isEnabled);

    Map<UUID, GroupRawInfo> groupMap = new HashMap<>();
    groupMap.put(group1id, group1Raw);
    groupMap.put(group2id, group2Raw);
    groupMap.put(group3id, group3Raw);
    groupMap.put(group4id, group4Raw);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(groupMap).when(this.groupRepository).findAll();
    doReturn(deviceMap).when(deviceRepository).findAll();

    // Call service
    CommonGetListResponse<GroupInfo> retListResp = groupService.getGroups(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.groupRepository, times(1)).findAll();

    // Expected Values
    GroupInfo group1 = new GroupInfo();
    group1.setGroupId(group1id);
    group1.setName(group1name);
    group1.setDescription(group1descr);
    group1.setComment(group1commn);
    group1.setIsEnabled(group1isEnabled);
    group1.addDevicesItem(group1dev1);
    group1.addDevicesItem(group1dev2);
    GroupInfo group2 = new GroupInfo();
    group2.setGroupId(group2id);
    group2.setName(group2name);
    group2.setDescription(group2descr);
    group2.setComment(group2commn);
    group2.setIsEnabled(group2isEnabled);
    group2.addDevicesItem(group2dev1);
    group2.addDevicesItem(group2dev2);
    GroupInfo group3 = new GroupInfo();
    group3.setGroupId(group3id);
    group3.setName(group3name);
    group3.setDescription(group3descr);
    group3.setComment(group3commn);
    group3.setIsEnabled(group3isEnabled);
    group3.addDevicesItem(group3dev1);
    group3.addDevicesItem(group3dev2);
    GroupInfo group4 = new GroupInfo();
    group4.setGroupId(group4id);
    group4.setName(group4name);
    group4.setDescription(group4descr);
    group4.setComment(group4commn);
    group4.setIsEnabled(group4isEnabled);
    group4.addDevicesItem(group4dev1);
    group4.addDevicesItem(group4dev2);

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The first two entries must not be included.", retListResp.getObjList(),
        contains(group2, group1));
  }

  @Test
  @DisplayName("TEST getGroups (limit = 2)")
  void testGetGroupsLimit2() {
    Integer skip = null;
    Integer limit = 2;

    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");
    UUID group2id = UUID.fromString("05abcdef-2222-2222-2222-222222222202");
    UUID group3id = UUID.fromString("05abcdef-3333-3333-3333-333333333303");
    UUID group4id = UUID.fromString("05abcdef-4444-4444-4444-444444444404");
    String group1name = "D a Group Name 1";
    String group2name = "C a Group Name 2";
    String group3name = "A a Group Name 3";
    String group4name = "B a Group Name 4";
    String group1descr = "a Group Description 1";
    String group2descr = "a Group Description 2";
    String group3descr = "a Group Description 3";
    String group4descr = "a Group Description 4";
    String group1commn = "a Group Comment 1";
    String group2commn = "a Group Comment 2";
    String group3commn = "a Group Comment 3";
    String group4commn = "a Group Comment 4";
    Boolean group1isEnabled = true;
    Boolean group2isEnabled = true;
    Boolean group3isEnabled = false;
    Boolean group4isEnabled = true;

    UUID group1dev1 = UUID.fromString("15abcdef-9999-1111-1111-111111111101");
    UUID group1dev2 = UUID.fromString("15abcdef-9999-2222-1111-111111111101");
    UUID group2dev1 = UUID.fromString("15abcdef-9999-1111-2222-222222222202");
    UUID group2dev2 = UUID.fromString("15abcdef-9999-2222-2222-222222222202");
    UUID group3dev1 = UUID.fromString("15abcdef-9999-1111-3333-333333333303");
    UUID group3dev2 = UUID.fromString("15abcdef-9999-2222-3333-333333333303");
    UUID group4dev1 = UUID.fromString("15abcdef-9999-1111-4444-444444444404");
    UUID group4dev2 = UUID.fromString("15abcdef-9999-2222-4444-444444444404");

    UUID unrelatedDeviceId = UUID.fromString("99999999-9999-9999-9999-999999999999");
    DeviceInfo device1 = new DeviceInfo().deviceId(group1dev1).groupId(group1id);
    DeviceInfo device2 = new DeviceInfo().deviceId(group1dev2).groupId(group1id);
    DeviceInfo device3 = new DeviceInfo().deviceId(group2dev1).groupId(group2id);
    DeviceInfo device4 = new DeviceInfo().deviceId(group2dev2).groupId(group2id);
    DeviceInfo device5 = new DeviceInfo().deviceId(group3dev1).groupId(group3id);
    DeviceInfo device6 = new DeviceInfo().deviceId(group3dev2).groupId(group3id);
    DeviceInfo device7 = new DeviceInfo().deviceId(group4dev1).groupId(group4id);
    DeviceInfo device8 = new DeviceInfo().deviceId(group4dev2).groupId(group4id);
    DeviceInfo device9 = new DeviceInfo().deviceId(unrelatedDeviceId);

    Map<UUID, DeviceInfo> deviceMap = new HashMap<>();
    deviceMap.put(group1dev1, device1);
    deviceMap.put(group1dev2, device2);
    deviceMap.put(group2dev1, device3);
    deviceMap.put(group2dev2, device4);
    deviceMap.put(group3dev1, device5);
    deviceMap.put(group3dev2, device6);
    deviceMap.put(group4dev1, device7);
    deviceMap.put(group4dev2, device8);
    deviceMap.put(unrelatedDeviceId, device9);

    GroupRawInfo group1Raw = new GroupRawInfo();
    group1Raw.setGroupId(group1id);
    group1Raw.setName(group1name);
    group1Raw.setDescription(group1descr);
    group1Raw.setComment(group1commn);
    group1Raw.setIsEnabled(group1isEnabled);
    GroupRawInfo group2Raw = new GroupRawInfo();
    group2Raw.setGroupId(group2id);
    group2Raw.setName(group2name);
    group2Raw.setDescription(group2descr);
    group2Raw.setComment(group2commn);
    group2Raw.setIsEnabled(group2isEnabled);
    GroupRawInfo group3Raw = new GroupRawInfo();
    group3Raw.setGroupId(group3id);
    group3Raw.setName(group3name);
    group3Raw.setDescription(group3descr);
    group3Raw.setComment(group3commn);
    group3Raw.setIsEnabled(group3isEnabled);
    GroupRawInfo group4Raw = new GroupRawInfo();
    group4Raw.setGroupId(group4id);
    group4Raw.setName(group4name);
    group4Raw.setDescription(group4descr);
    group4Raw.setComment(group4commn);
    group4Raw.setIsEnabled(group4isEnabled);

    Map<UUID, GroupRawInfo> groupMap = new HashMap<>();
    groupMap.put(group1id, group1Raw);
    groupMap.put(group2id, group2Raw);
    groupMap.put(group3id, group3Raw);
    groupMap.put(group4id, group4Raw);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(groupMap).when(this.groupRepository).findAll();
    doReturn(deviceMap).when(deviceRepository).findAll();

    // Call service
    CommonGetListResponse<GroupInfo> retListResp = groupService.getGroups(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.groupRepository, times(1)).findAll();

    // Expected Values
    GroupInfo group1 = new GroupInfo();
    group1.setGroupId(group1id);
    group1.setName(group1name);
    group1.setDescription(group1descr);
    group1.setComment(group1commn);
    group1.setIsEnabled(group1isEnabled);
    group1.addDevicesItem(group1dev1);
    group1.addDevicesItem(group1dev2);
    GroupInfo group2 = new GroupInfo();
    group2.setGroupId(group2id);
    group2.setName(group2name);
    group2.setDescription(group2descr);
    group2.setComment(group2commn);
    group2.setIsEnabled(group2isEnabled);
    group2.addDevicesItem(group2dev1);
    group2.addDevicesItem(group2dev2);
    GroupInfo group3 = new GroupInfo();
    group3.setGroupId(group3id);
    group3.setName(group3name);
    group3.setDescription(group3descr);
    group3.setComment(group3commn);
    group3.setIsEnabled(group3isEnabled);
    group3.addDevicesItem(group3dev1);
    group3.addDevicesItem(group3dev2);
    GroupInfo group4 = new GroupInfo();
    group4.setGroupId(group4id);
    group4.setName(group4name);
    group4.setDescription(group4descr);
    group4.setComment(group4commn);
    group4.setIsEnabled(group4isEnabled);
    group4.addDevicesItem(group4dev1);
    group4.addDevicesItem(group4dev2);

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must only be 2, sorted by group name ascending.",
        retListResp.getObjList(), contains(group3, group4));
  }

  @Test
  @DisplayName("TEST getGroups (No entries in the group DB(null))")
  void testGetGroupsNoGroupEntryNull() {
    Integer skip = null;
    Integer limit = null;

    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");
    UUID group2id = UUID.fromString("05abcdef-2222-2222-2222-222222222202");
    UUID group3id = UUID.fromString("05abcdef-3333-3333-3333-333333333303");
    UUID group4id = UUID.fromString("05abcdef-4444-4444-4444-444444444404");

    UUID group1dev1 = UUID.fromString("15abcdef-9999-1111-1111-111111111101");
    UUID group1dev2 = UUID.fromString("15abcdef-9999-2222-1111-111111111101");
    UUID group2dev1 = UUID.fromString("15abcdef-9999-1111-2222-222222222202");
    UUID group2dev2 = UUID.fromString("15abcdef-9999-2222-2222-222222222202");
    UUID group3dev1 = UUID.fromString("15abcdef-9999-1111-3333-333333333303");
    UUID group3dev2 = UUID.fromString("15abcdef-9999-2222-3333-333333333303");
    UUID group4dev1 = UUID.fromString("15abcdef-9999-1111-4444-444444444404");
    UUID group4dev2 = UUID.fromString("15abcdef-9999-2222-4444-444444444404");

    UUID unrelatedDeviceId = UUID.fromString("99999999-9999-9999-9999-999999999999");
    DeviceInfo device1 = new DeviceInfo().deviceId(group1dev1).groupId(group1id);
    DeviceInfo device2 = new DeviceInfo().deviceId(group1dev2).groupId(group1id);
    DeviceInfo device3 = new DeviceInfo().deviceId(group2dev1).groupId(group2id);
    DeviceInfo device4 = new DeviceInfo().deviceId(group2dev2).groupId(group2id);
    DeviceInfo device5 = new DeviceInfo().deviceId(group3dev1).groupId(group3id);
    DeviceInfo device6 = new DeviceInfo().deviceId(group3dev2).groupId(group3id);
    DeviceInfo device7 = new DeviceInfo().deviceId(group4dev1).groupId(group4id);
    DeviceInfo device8 = new DeviceInfo().deviceId(group4dev2).groupId(group4id);
    DeviceInfo device9 = new DeviceInfo().deviceId(unrelatedDeviceId);

    Map<UUID, DeviceInfo> deviceMap = new HashMap<>();
    deviceMap.put(group1dev1, device1);
    deviceMap.put(group1dev2, device2);
    deviceMap.put(group2dev1, device3);
    deviceMap.put(group2dev2, device4);
    deviceMap.put(group3dev1, device5);
    deviceMap.put(group3dev2, device6);
    deviceMap.put(group4dev1, device7);
    deviceMap.put(group4dev2, device8);
    deviceMap.put(unrelatedDeviceId, device9);

    Map<UUID, GroupRawInfo> groupMap = null;

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(groupMap).when(this.groupRepository).findAll();
    doReturn(deviceMap).when(deviceRepository).findAll();

    // Call service
    CommonGetListResponse<GroupInfo> retListResp = groupService.getGroups(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.groupRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getGroups (No entries in the group DB)")
  void testGetGroupsNoGroupEntry() {
    Integer skip = null;
    Integer limit = null;

    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");
    UUID group2id = UUID.fromString("05abcdef-2222-2222-2222-222222222202");
    UUID group3id = UUID.fromString("05abcdef-3333-3333-3333-333333333303");
    UUID group4id = UUID.fromString("05abcdef-4444-4444-4444-444444444404");

    UUID group1dev1 = UUID.fromString("15abcdef-9999-1111-1111-111111111101");
    UUID group1dev2 = UUID.fromString("15abcdef-9999-2222-1111-111111111101");
    UUID group2dev1 = UUID.fromString("15abcdef-9999-1111-2222-222222222202");
    UUID group2dev2 = UUID.fromString("15abcdef-9999-2222-2222-222222222202");
    UUID group3dev1 = UUID.fromString("15abcdef-9999-1111-3333-333333333303");
    UUID group3dev2 = UUID.fromString("15abcdef-9999-2222-3333-333333333303");
    UUID group4dev1 = UUID.fromString("15abcdef-9999-1111-4444-444444444404");
    UUID group4dev2 = UUID.fromString("15abcdef-9999-2222-4444-444444444404");

    UUID unrelatedDeviceId = UUID.fromString("99999999-9999-9999-9999-999999999999");
    DeviceInfo device1 = new DeviceInfo().deviceId(group1dev1).groupId(group1id);
    DeviceInfo device2 = new DeviceInfo().deviceId(group1dev2).groupId(group1id);
    DeviceInfo device3 = new DeviceInfo().deviceId(group2dev1).groupId(group2id);
    DeviceInfo device4 = new DeviceInfo().deviceId(group2dev2).groupId(group2id);
    DeviceInfo device5 = new DeviceInfo().deviceId(group3dev1).groupId(group3id);
    DeviceInfo device6 = new DeviceInfo().deviceId(group3dev2).groupId(group3id);
    DeviceInfo device7 = new DeviceInfo().deviceId(group4dev1).groupId(group4id);
    DeviceInfo device8 = new DeviceInfo().deviceId(group4dev2).groupId(group4id);
    DeviceInfo device9 = new DeviceInfo().deviceId(unrelatedDeviceId);

    Map<UUID, DeviceInfo> deviceMap = new HashMap<>();
    deviceMap.put(group1dev1, device1);
    deviceMap.put(group1dev2, device2);
    deviceMap.put(group2dev1, device3);
    deviceMap.put(group2dev2, device4);
    deviceMap.put(group3dev1, device5);
    deviceMap.put(group3dev2, device6);
    deviceMap.put(group4dev1, device7);
    deviceMap.put(group4dev2, device8);
    deviceMap.put(unrelatedDeviceId, device9);

    Map<UUID, GroupRawInfo> groupMap = Collections.emptyMap();

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(groupMap).when(this.groupRepository).findAll();
    doReturn(deviceMap).when(deviceRepository).findAll();

    // Call service
    CommonGetListResponse<GroupInfo> retListResp = groupService.getGroups(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.groupRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getGroups (No entries in the device DB(null))")
  void testGetGroupsNoDeviceEntryNull() {
    Integer skip = null;
    Integer limit = null;

    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");
    UUID group2id = UUID.fromString("05abcdef-2222-2222-2222-222222222202");
    UUID group3id = UUID.fromString("05abcdef-3333-3333-3333-333333333303");
    UUID group4id = UUID.fromString("05abcdef-4444-4444-4444-444444444404");
    String group1name = "D a Group Name 1";
    String group2name = "C a Group Name 2";
    String group3name = "A a Group Name 3";
    String group4name = "B a Group Name 4";
    String group1descr = "a Group Description 1";
    String group2descr = "a Group Description 2";
    String group3descr = "a Group Description 3";
    String group4descr = "a Group Description 4";
    String group1commn = "a Group Comment 1";
    String group2commn = "a Group Comment 2";
    String group3commn = "a Group Comment 3";
    String group4commn = "a Group Comment 4";
    Boolean group1isEnabled = true;
    Boolean group2isEnabled = true;
    Boolean group3isEnabled = false;
    Boolean group4isEnabled = true;

    Map<UUID, DeviceInfo> deviceMap = null;

    GroupRawInfo group1Raw = new GroupRawInfo();
    group1Raw.setGroupId(group1id);
    group1Raw.setName(group1name);
    group1Raw.setDescription(group1descr);
    group1Raw.setComment(group1commn);
    group1Raw.setIsEnabled(group1isEnabled);
    GroupRawInfo group2Raw = new GroupRawInfo();
    group2Raw.setGroupId(group2id);
    group2Raw.setName(group2name);
    group2Raw.setDescription(group2descr);
    group2Raw.setComment(group2commn);
    group2Raw.setIsEnabled(group2isEnabled);
    GroupRawInfo group3Raw = new GroupRawInfo();
    group3Raw.setGroupId(group3id);
    group3Raw.setName(group3name);
    group3Raw.setDescription(group3descr);
    group3Raw.setComment(group3commn);
    group3Raw.setIsEnabled(group3isEnabled);
    GroupRawInfo group4Raw = new GroupRawInfo();
    group4Raw.setGroupId(group4id);
    group4Raw.setName(group4name);
    group4Raw.setDescription(group4descr);
    group4Raw.setComment(group4commn);
    group4Raw.setIsEnabled(group4isEnabled);

    Map<UUID, GroupRawInfo> groupMap = new HashMap<>();
    groupMap.put(group1id, group1Raw);
    groupMap.put(group2id, group2Raw);
    groupMap.put(group3id, group3Raw);
    groupMap.put(group4id, group4Raw);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(groupMap).when(this.groupRepository).findAll();
    doReturn(deviceMap).when(deviceRepository).findAll();

    // Call service
    CommonGetListResponse<GroupInfo> retListResp = groupService.getGroups(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.groupRepository, times(1)).findAll();

    // Expected Values
    GroupInfo group1 = new GroupInfo();
    group1.setGroupId(group1id);
    group1.setName(group1name);
    group1.setDescription(group1descr);
    group1.setComment(group1commn);
    group1.setIsEnabled(group1isEnabled);
    group1.setDevices(Collections.emptyList());
    GroupInfo group2 = new GroupInfo();
    group2.setGroupId(group2id);
    group2.setName(group2name);
    group2.setDescription(group2descr);
    group2.setComment(group2commn);
    group2.setIsEnabled(group2isEnabled);
    group2.setDevices(Collections.emptyList());
    GroupInfo group3 = new GroupInfo();
    group3.setGroupId(group3id);
    group3.setName(group3name);
    group3.setDescription(group3descr);
    group3.setComment(group3commn);
    group3.setIsEnabled(group3isEnabled);
    group3.setDevices(Collections.emptyList());
    GroupInfo group4 = new GroupInfo();
    group4.setGroupId(group4id);
    group4.setName(group4name);
    group4.setDescription(group4descr);
    group4.setComment(group4commn);
    group4.setIsEnabled(group4isEnabled);
    group4.setDevices(Collections.emptyList());

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be sorted by group name ascending.",
        retListResp.getObjList(), contains(group3, group4, group2, group1));
    assertThat("The device List for group3 must be empty.",
        retListResp.getObjList().get(0).getDevices(), IsEmptyCollection.empty());
    assertThat("The device List for group4 must be empty.",
        retListResp.getObjList().get(1).getDevices(), IsEmptyCollection.empty());
    assertThat("The device List for group2 must be empty.",
        retListResp.getObjList().get(2).getDevices(), IsEmptyCollection.empty());
    assertThat("The device List for group1 must be empty.",
        retListResp.getObjList().get(3).getDevices(), IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getGroups (No entries in the device DB)")
  void testGetGroupsNoDeviceEntry() {
    Integer skip = null;
    Integer limit = null;

    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");
    UUID group2id = UUID.fromString("05abcdef-2222-2222-2222-222222222202");
    UUID group3id = UUID.fromString("05abcdef-3333-3333-3333-333333333303");
    UUID group4id = UUID.fromString("05abcdef-4444-4444-4444-444444444404");
    String group1name = "D a Group Name 1";
    String group2name = "C a Group Name 2";
    String group3name = "A a Group Name 3";
    String group4name = "B a Group Name 4";
    String group1descr = "a Group Description 1";
    String group2descr = "a Group Description 2";
    String group3descr = "a Group Description 3";
    String group4descr = "a Group Description 4";
    String group1commn = "a Group Comment 1";
    String group2commn = "a Group Comment 2";
    String group3commn = "a Group Comment 3";
    String group4commn = "a Group Comment 4";
    Boolean group1isEnabled = true;
    Boolean group2isEnabled = true;
    Boolean group3isEnabled = false;
    Boolean group4isEnabled = true;

    Map<UUID, DeviceInfo> deviceMap = Collections.emptyMap();

    GroupRawInfo group1Raw = new GroupRawInfo();
    group1Raw.setGroupId(group1id);
    group1Raw.setName(group1name);
    group1Raw.setDescription(group1descr);
    group1Raw.setComment(group1commn);
    group1Raw.setIsEnabled(group1isEnabled);
    GroupRawInfo group2Raw = new GroupRawInfo();
    group2Raw.setGroupId(group2id);
    group2Raw.setName(group2name);
    group2Raw.setDescription(group2descr);
    group2Raw.setComment(group2commn);
    group2Raw.setIsEnabled(group2isEnabled);
    GroupRawInfo group3Raw = new GroupRawInfo();
    group3Raw.setGroupId(group3id);
    group3Raw.setName(group3name);
    group3Raw.setDescription(group3descr);
    group3Raw.setComment(group3commn);
    group3Raw.setIsEnabled(group3isEnabled);
    GroupRawInfo group4Raw = new GroupRawInfo();
    group4Raw.setGroupId(group4id);
    group4Raw.setName(group4name);
    group4Raw.setDescription(group4descr);
    group4Raw.setComment(group4commn);
    group4Raw.setIsEnabled(group4isEnabled);

    Map<UUID, GroupRawInfo> groupMap = new HashMap<>();
    groupMap.put(group1id, group1Raw);
    groupMap.put(group2id, group2Raw);
    groupMap.put(group3id, group3Raw);
    groupMap.put(group4id, group4Raw);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(groupMap).when(this.groupRepository).findAll();
    doReturn(deviceMap).when(deviceRepository).findAll();

    // Call service
    CommonGetListResponse<GroupInfo> retListResp = groupService.getGroups(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.groupRepository, times(1)).findAll();

    // Expected Values
    GroupInfo group1 = new GroupInfo();
    group1.setGroupId(group1id);
    group1.setName(group1name);
    group1.setDescription(group1descr);
    group1.setComment(group1commn);
    group1.setIsEnabled(group1isEnabled);
    group1.setDevices(Collections.emptyList());
    GroupInfo group2 = new GroupInfo();
    group2.setGroupId(group2id);
    group2.setName(group2name);
    group2.setDescription(group2descr);
    group2.setComment(group2commn);
    group2.setIsEnabled(group2isEnabled);
    group2.setDevices(Collections.emptyList());
    GroupInfo group3 = new GroupInfo();
    group3.setGroupId(group3id);
    group3.setName(group3name);
    group3.setDescription(group3descr);
    group3.setComment(group3commn);
    group3.setIsEnabled(group3isEnabled);
    group3.setDevices(Collections.emptyList());
    GroupInfo group4 = new GroupInfo();
    group4.setGroupId(group4id);
    group4.setName(group4name);
    group4.setDescription(group4descr);
    group4.setComment(group4commn);
    group4.setIsEnabled(group4isEnabled);
    group4.setDevices(Collections.emptyList());

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be sorted by group name ascending.",
        retListResp.getObjList(), contains(group3, group4, group2, group1));
    assertThat("The device List for group3 must be empty.",
        retListResp.getObjList().get(0).getDevices(), IsEmptyCollection.empty());
    assertThat("The device List for group4 must be empty.",
        retListResp.getObjList().get(1).getDevices(), IsEmptyCollection.empty());
    assertThat("The device List for group2 must be empty.",
        retListResp.getObjList().get(2).getDevices(), IsEmptyCollection.empty());
    assertThat("The device List for group1 must be empty.",
        retListResp.getObjList().get(3).getDevices(), IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getGroups (Entries in the device DB have group Ids not in the group list)")
  void testGetGroupsMismatchEntriesGroupVsDevice() {
    Integer skip = null;
    Integer limit = null;

    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");
    UUID group2id = UUID.fromString("05abcdef-2222-2222-2222-222222222202");
    UUID group3id = UUID.fromString("05abcdef-3333-3333-3333-333333333303");
    UUID group4id = UUID.fromString("05abcdef-4444-4444-4444-444444444404");
    String group1name = "D a Group Name 1";
    String group2name = "C a Group Name 2";
    String group3name = "A a Group Name 3";
    String group4name = "B a Group Name 4";
    String group1descr = "a Group Description 1";
    String group2descr = "a Group Description 2";
    String group3descr = "a Group Description 3";
    String group4descr = "a Group Description 4";
    String group1commn = "a Group Comment 1";
    String group2commn = "a Group Comment 2";
    String group3commn = "a Group Comment 3";
    String group4commn = "a Group Comment 4";
    Boolean group1isEnabled = true;
    Boolean group2isEnabled = true;
    Boolean group3isEnabled = false;
    Boolean group4isEnabled = true;

    UUID group1dev1 = UUID.fromString("15abcdef-9999-1111-1111-111111111101");
    UUID group1dev2 = UUID.fromString("15abcdef-9999-2222-1111-111111111101");
    UUID group2dev1 = UUID.fromString("15abcdef-9999-1111-2222-222222222202");
    UUID group2dev2 = UUID.fromString("15abcdef-9999-2222-2222-222222222202");
    UUID group3dev1 = UUID.fromString("15abcdef-9999-1111-3333-333333333303");
    UUID group3dev2 = UUID.fromString("15abcdef-9999-2222-3333-333333333303");
    UUID group4dev1 = UUID.fromString("15abcdef-9999-1111-4444-444444444404");
    UUID group4dev2 = UUID.fromString("15abcdef-9999-2222-4444-444444444404");

    UUID deviceId1 = UUID.fromString("99999999-9999-9999-9999-999999999991");
    UUID deviceId2 = UUID.fromString("99999999-9999-9999-9999-999999999992");
    UUID deviceId3 = UUID.fromString("99999999-9999-9999-9999-999999999993");
    UUID deviceId4 = UUID.fromString("99999999-9999-9999-9999-999999999994");
    UUID deviceId5 = UUID.fromString("99999999-9999-9999-9999-999999999995");
    UUID deviceId6 = UUID.fromString("99999999-9999-9999-9999-999999999996");
    UUID deviceId7 = UUID.fromString("99999999-9999-9999-9999-999999999997");
    UUID deviceId8 = UUID.fromString("99999999-9999-9999-9999-999999999998");
    DeviceInfo device1 = new DeviceInfo().deviceId(group1dev1).groupId(deviceId1);
    DeviceInfo device2 = new DeviceInfo().deviceId(group1dev2).groupId(deviceId2);
    DeviceInfo device3 = new DeviceInfo().deviceId(group2dev1).groupId(deviceId3);
    DeviceInfo device4 = new DeviceInfo().deviceId(group2dev2).groupId(deviceId4);
    DeviceInfo device5 = new DeviceInfo().deviceId(group3dev1).groupId(deviceId5);
    DeviceInfo device6 = new DeviceInfo().deviceId(group3dev2).groupId(deviceId6);
    DeviceInfo device7 = new DeviceInfo().deviceId(group4dev1).groupId(deviceId7);
    DeviceInfo device8 = new DeviceInfo().deviceId(group4dev2).groupId(deviceId8);

    Map<UUID, DeviceInfo> deviceMap = new HashMap<>();
    deviceMap.put(deviceId1, device1);
    deviceMap.put(deviceId2, device2);
    deviceMap.put(deviceId3, device3);
    deviceMap.put(deviceId4, device4);
    deviceMap.put(deviceId5, device5);
    deviceMap.put(deviceId6, device6);
    deviceMap.put(deviceId7, device7);
    deviceMap.put(deviceId8, device8);

    GroupRawInfo group1Raw = new GroupRawInfo();
    group1Raw.setGroupId(group1id);
    group1Raw.setName(group1name);
    group1Raw.setDescription(group1descr);
    group1Raw.setComment(group1commn);
    group1Raw.setIsEnabled(group1isEnabled);
    GroupRawInfo group2Raw = new GroupRawInfo();
    group2Raw.setGroupId(group2id);
    group2Raw.setName(group2name);
    group2Raw.setDescription(group2descr);
    group2Raw.setComment(group2commn);
    group2Raw.setIsEnabled(group2isEnabled);
    GroupRawInfo group3Raw = new GroupRawInfo();
    group3Raw.setGroupId(group3id);
    group3Raw.setName(group3name);
    group3Raw.setDescription(group3descr);
    group3Raw.setComment(group3commn);
    group3Raw.setIsEnabled(group3isEnabled);
    GroupRawInfo group4Raw = new GroupRawInfo();
    group4Raw.setGroupId(group4id);
    group4Raw.setName(group4name);
    group4Raw.setDescription(group4descr);
    group4Raw.setComment(group4commn);
    group4Raw.setIsEnabled(group4isEnabled);

    Map<UUID, GroupRawInfo> groupMap = new HashMap<>();
    groupMap.put(group1id, group1Raw);
    groupMap.put(group2id, group2Raw);
    groupMap.put(group3id, group3Raw);
    groupMap.put(group4id, group4Raw);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(groupMap).when(this.groupRepository).findAll();
    doReturn(deviceMap).when(deviceRepository).findAll();

    // Call service
    CommonGetListResponse<GroupInfo> retListResp = groupService.getGroups(skip, limit);

    // Verify that appropriate Repository method was called
    verify(this.groupRepository, times(1)).findAll();

    // Expected Values
    GroupInfo group1 = new GroupInfo();
    group1.setGroupId(group1id);
    group1.setName(group1name);
    group1.setDescription(group1descr);
    group1.setComment(group1commn);
    group1.setIsEnabled(group1isEnabled);
    group1.setDevices(Collections.emptyList());
    GroupInfo group2 = new GroupInfo();
    group2.setGroupId(group2id);
    group2.setName(group2name);
    group2.setDescription(group2descr);
    group2.setComment(group2commn);
    group2.setIsEnabled(group2isEnabled);
    group2.setDevices(Collections.emptyList());
    GroupInfo group3 = new GroupInfo();
    group3.setGroupId(group3id);
    group3.setName(group3name);
    group3.setDescription(group3descr);
    group3.setComment(group3commn);
    group3.setIsEnabled(group3isEnabled);
    group3.setDevices(Collections.emptyList());
    GroupInfo group4 = new GroupInfo();
    group4.setGroupId(group4id);
    group4.setName(group4name);
    group4.setDescription(group4descr);
    group4.setComment(group4commn);
    group4.setIsEnabled(group4isEnabled);
    group4.setDevices(Collections.emptyList());

    // Validate
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be sorted by group name ascending.",
        retListResp.getObjList(), contains(group3, group4, group2, group1));
    assertThat("The device List for group3 must be empty.",
        retListResp.getObjList().get(0).getDevices(), IsEmptyCollection.empty());
    assertThat("The device List for group4 must be empty.",
        retListResp.getObjList().get(1).getDevices(), IsEmptyCollection.empty());
    assertThat("The device List for group2 must be empty.",
        retListResp.getObjList().get(2).getDevices(), IsEmptyCollection.empty());
    assertThat("The device List for group1 must be empty.",
        retListResp.getObjList().get(3).getDevices(), IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getGroupById")
  void testGetGroupById() {
    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");
    UUID group2id = UUID.fromString("05abcdef-2222-2222-2222-222222222202");
    UUID group3id = UUID.fromString("05abcdef-3333-3333-3333-333333333303");
    UUID group4id = UUID.fromString("05abcdef-4444-4444-4444-444444444404");
    String group1name = "D a Group Name 1";
    String group2name = "C a Group Name 2";
    String group3name = "A a Group Name 3";
    String group4name = "B a Group Name 4";
    String group1descr = "a Group Description 1";
    String group2descr = "a Group Description 2";
    String group3descr = "a Group Description 3";
    String group4descr = "a Group Description 4";
    String group1commn = "a Group Comment 1";
    String group2commn = "a Group Comment 2";
    String group3commn = "a Group Comment 3";
    String group4commn = "a Group Comment 4";
    Boolean group1isEnabled = true;
    Boolean group2isEnabled = true;
    Boolean group3isEnabled = false;
    Boolean group4isEnabled = true;

    UUID group1dev1 = UUID.fromString("15abcdef-9999-1111-1111-111111111101");
    UUID group1dev2 = UUID.fromString("15abcdef-9999-2222-1111-111111111101");
    UUID group2dev1 = UUID.fromString("15abcdef-9999-1111-2222-222222222202");
    UUID group2dev2 = UUID.fromString("15abcdef-9999-2222-2222-222222222202");
    UUID group3dev1 = UUID.fromString("15abcdef-9999-1111-3333-333333333303");
    UUID group3dev2 = UUID.fromString("15abcdef-9999-2222-3333-333333333303");
    UUID group4dev1 = UUID.fromString("15abcdef-9999-1111-4444-444444444404");
    UUID group4dev2 = UUID.fromString("15abcdef-9999-2222-4444-444444444404");

    GroupRawInfo group1Raw = new GroupRawInfo();
    group1Raw.setGroupId(group1id);
    group1Raw.setName(group1name);
    group1Raw.setDescription(group1descr);
    group1Raw.setComment(group1commn);
    group1Raw.setIsEnabled(group1isEnabled);
    GroupRawInfo group2Raw = new GroupRawInfo();
    group2Raw.setGroupId(group2id);
    group2Raw.setName(group2name);
    group2Raw.setDescription(group2descr);
    group2Raw.setComment(group2commn);
    group2Raw.setIsEnabled(group2isEnabled);
    GroupRawInfo group3Raw = new GroupRawInfo();
    group3Raw.setGroupId(group3id);
    group3Raw.setName(group3name);
    group3Raw.setDescription(group3descr);
    group3Raw.setComment(group3commn);
    group3Raw.setIsEnabled(group3isEnabled);
    GroupRawInfo group4Raw = new GroupRawInfo();
    group4Raw.setGroupId(group4id);
    group4Raw.setName(group4name);
    group4Raw.setDescription(group4descr);
    group4Raw.setComment(group4commn);
    group4Raw.setIsEnabled(group4isEnabled);

    UUID unrelatedDeviceId = UUID.fromString("99999999-9999-9999-9999-999999999999");
    DeviceInfo device1 = new DeviceInfo().deviceId(group1dev1).groupId(group1id);
    DeviceInfo device2 = new DeviceInfo().deviceId(group1dev2).groupId(group1id);
    DeviceInfo device3 = new DeviceInfo().deviceId(group2dev1).groupId(group2id);
    DeviceInfo device4 = new DeviceInfo().deviceId(group2dev2).groupId(group2id);
    DeviceInfo device5 = new DeviceInfo().deviceId(group3dev1).groupId(group3id);
    DeviceInfo device6 = new DeviceInfo().deviceId(group3dev2).groupId(group3id);
    DeviceInfo device7 = new DeviceInfo().deviceId(group4dev1).groupId(group4id);
    DeviceInfo device8 = new DeviceInfo().deviceId(group4dev2).groupId(group4id);
    DeviceInfo device9 = new DeviceInfo().deviceId(unrelatedDeviceId);

    Map<UUID, DeviceInfo> deviceMap = new HashMap<>();
    deviceMap.put(group1dev1, device1);
    deviceMap.put(group1dev2, device2);
    deviceMap.put(group2dev1, device3);
    deviceMap.put(group2dev2, device4);
    deviceMap.put(group3dev1, device5);
    deviceMap.put(group3dev2, device6);
    deviceMap.put(group4dev1, device7);
    deviceMap.put(group4dev2, device8);
    deviceMap.put(unrelatedDeviceId, device9);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.groupRepository.findById(eq(group1id))).thenReturn(group1Raw);
    when(this.groupRepository.findById(eq(group2id))).thenReturn(group2Raw);
    when(this.groupRepository.findById(eq(group3id))).thenReturn(group3Raw);
    when(this.groupRepository.findById(eq(group4id))).thenReturn(group4Raw);
    doReturn(deviceMap).when(deviceRepository).findAll();

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call service
    CommonGetResponse<GroupInfo> retObjResp = groupService.getGroupById(group1id);

    // Verify that appropriate Repository method was called
    verify(this.groupRepository, times(1)).findById(arg1.capture());

    // Expected Values
    GroupInfo group1 = new GroupInfo();
    group1.setGroupId(group1id);
    group1.setName(group1name);
    group1.setDescription(group1descr);
    group1.setComment(group1commn);
    group1.setIsEnabled(group1isEnabled);
    group1.addDevicesItem(group1dev1);
    group1.addDevicesItem(group1dev2);
    GroupInfo group2 = new GroupInfo();
    group2.setGroupId(group2id);
    group2.setName(group2name);
    group2.setDescription(group2descr);
    group2.setComment(group2commn);
    group2.setIsEnabled(group2isEnabled);
    group2.addDevicesItem(group2dev1);
    group2.addDevicesItem(group2dev2);
    GroupInfo group3 = new GroupInfo();
    group3.setGroupId(group3id);
    group3.setName(group3name);
    group3.setDescription(group3descr);
    group3.setComment(group3commn);
    group3.setIsEnabled(group3isEnabled);
    group3.addDevicesItem(group3dev1);
    group3.addDevicesItem(group3dev2);
    GroupInfo group4 = new GroupInfo();
    group4.setGroupId(group4id);
    group4.setName(group4name);
    group4.setDescription(group4descr);
    group4.setComment(group4commn);
    group4.setIsEnabled(group4isEnabled);
    group4.addDevicesItem(group4dev1);
    group4.addDevicesItem(group4dev2);

    // Assertions
    assertEquals(group1id, arg1.getValue(), "Argument must match.");
    assertNotNull(retObjResp, "Return object is not null.");
    assertEquals(expectedStatus, retObjResp.getResponse(), "Response code must be 200.");
    assertNull(retObjResp.getErrorResult(), "There should be no error returned.");
    assertEquals(group1, retObjResp.getObj(), "The returned object must match group 1");
    assertEquals(group1id, retObjResp.getObj().getGroupId(),
        "Returned entry's UUID must match group 1's");
    assertEquals(group1name, retObjResp.getObj().getName(),
        "Returned entry's name must match group 1's");
    assertEquals(group1descr, retObjResp.getObj().getDescription(),
        "Returned entry's manufacturer must match group 1's");
    assertEquals(group1commn, retObjResp.getObj().getComment(),
        "Returned entry's comment must match group 1's");
    assertEquals(group1isEnabled, retObjResp.getObj().isIsEnabled(),
        "Returned entry's isEnabled must match group 1's");
    assertThat("The device List must be correct (any order).", retObjResp.getObj().getDevices(),
        containsInAnyOrder(group1dev1, group1dev2));
  }

  @Test
  @DisplayName("TEST getGroupById (groupId not found in DB)")
  void testGetGroupByIdNotFound() {
    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");
    UUID group2id = UUID.fromString("05abcdef-2222-2222-2222-222222222202");
    UUID group3id = UUID.fromString("05abcdef-3333-3333-3333-333333333303");
    UUID group4id = UUID.fromString("05abcdef-4444-4444-4444-444444444404");
    String group1name = "D a Group Name 1";
    String group2name = "C a Group Name 2";
    String group3name = "A a Group Name 3";
    String group4name = "B a Group Name 4";
    String group1descr = "a Group Description 1";
    String group2descr = "a Group Description 2";
    String group3descr = "a Group Description 3";
    String group4descr = "a Group Description 4";
    String group1commn = "a Group Comment 1";
    String group2commn = "a Group Comment 2";
    String group3commn = "a Group Comment 3";
    String group4commn = "a Group Comment 4";
    Boolean group1isEnabled = true;
    Boolean group2isEnabled = true;
    Boolean group3isEnabled = false;
    Boolean group4isEnabled = true;

    UUID group1dev1 = UUID.fromString("15abcdef-9999-1111-1111-111111111101");
    UUID group1dev2 = UUID.fromString("15abcdef-9999-2222-1111-111111111101");
    UUID group2dev1 = UUID.fromString("15abcdef-9999-1111-2222-222222222202");
    UUID group2dev2 = UUID.fromString("15abcdef-9999-2222-2222-222222222202");
    UUID group3dev1 = UUID.fromString("15abcdef-9999-1111-3333-333333333303");
    UUID group3dev2 = UUID.fromString("15abcdef-9999-2222-3333-333333333303");
    UUID group4dev1 = UUID.fromString("15abcdef-9999-1111-4444-444444444404");
    UUID group4dev2 = UUID.fromString("15abcdef-9999-2222-4444-444444444404");

    GroupRawInfo group1Raw = new GroupRawInfo();
    group1Raw.setGroupId(group1id);
    group1Raw.setName(group1name);
    group1Raw.setDescription(group1descr);
    group1Raw.setComment(group1commn);
    group1Raw.setIsEnabled(group1isEnabled);
    GroupRawInfo group2Raw = new GroupRawInfo();
    group2Raw.setGroupId(group2id);
    group2Raw.setName(group2name);
    group2Raw.setDescription(group2descr);
    group2Raw.setComment(group2commn);
    group2Raw.setIsEnabled(group2isEnabled);
    GroupRawInfo group3Raw = new GroupRawInfo();
    group3Raw.setGroupId(group3id);
    group3Raw.setName(group3name);
    group3Raw.setDescription(group3descr);
    group3Raw.setComment(group3commn);
    group3Raw.setIsEnabled(group3isEnabled);
    GroupRawInfo group4Raw = new GroupRawInfo();
    group4Raw.setGroupId(group4id);
    group4Raw.setName(group4name);
    group4Raw.setDescription(group4descr);
    group4Raw.setComment(group4commn);
    group4Raw.setIsEnabled(group4isEnabled);

    UUID unrelatedDeviceId = UUID.fromString("99999999-9999-9999-9999-999999999999");
    DeviceInfo device1 = new DeviceInfo().deviceId(group1dev1).groupId(group1id);
    DeviceInfo device2 = new DeviceInfo().deviceId(group1dev2).groupId(group1id);
    DeviceInfo device3 = new DeviceInfo().deviceId(group2dev1).groupId(group2id);
    DeviceInfo device4 = new DeviceInfo().deviceId(group2dev2).groupId(group2id);
    DeviceInfo device5 = new DeviceInfo().deviceId(group3dev1).groupId(group3id);
    DeviceInfo device6 = new DeviceInfo().deviceId(group3dev2).groupId(group3id);
    DeviceInfo device7 = new DeviceInfo().deviceId(group4dev1).groupId(group4id);
    DeviceInfo device8 = new DeviceInfo().deviceId(group4dev2).groupId(group4id);
    DeviceInfo device9 = new DeviceInfo().deviceId(unrelatedDeviceId);

    Map<UUID, DeviceInfo> deviceMap = new HashMap<>();
    deviceMap.put(group1dev1, device1);
    deviceMap.put(group1dev2, device2);
    deviceMap.put(group2dev1, device3);
    deviceMap.put(group2dev2, device4);
    deviceMap.put(group3dev1, device5);
    deviceMap.put(group3dev2, device6);
    deviceMap.put(group4dev1, device7);
    deviceMap.put(group4dev2, device8);
    deviceMap.put(unrelatedDeviceId, device9);

    HttpStatus expectedStatus = HttpStatus.NOT_FOUND;

    // Mock(s)
    when(this.groupRepository.findById(eq(group1id))).thenReturn(null);
    when(this.groupRepository.findById(eq(group2id))).thenReturn(group2Raw);
    when(this.groupRepository.findById(eq(group3id))).thenReturn(group3Raw);
    when(this.groupRepository.findById(eq(group4id))).thenReturn(group4Raw);
    doReturn(deviceMap).when(deviceRepository).findAll();

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call service
    CommonGetResponse<GroupInfo> retObjResp = groupService.getGroupById(group1id);

    // Verify that appropriate Repository method was called
    verify(this.groupRepository, times(1)).findById(arg1.capture());

    // Assertions
    assertEquals(group1id, arg1.getValue(), "Argument must match.");
    assertNotNull(retObjResp, "Return object is not null.");
    assertEquals(expectedStatus, retObjResp.getResponse(), "Response code must be 404.");
    assertNull(retObjResp.getErrorResult(), "There should be no error returned.");
    assertNull(retObjResp.getObj(), "The returned object must be empty.");
  }

  @Test
  @DisplayName("TEST getGroupById (No entries in the device DB(null))")
  void testGetGroupByIdNoDeviceEntryNull() {
    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");
    UUID group2id = UUID.fromString("05abcdef-2222-2222-2222-222222222202");
    UUID group3id = UUID.fromString("05abcdef-3333-3333-3333-333333333303");
    UUID group4id = UUID.fromString("05abcdef-4444-4444-4444-444444444404");
    String group1name = "D a Group Name 1";
    String group2name = "C a Group Name 2";
    String group3name = "A a Group Name 3";
    String group4name = "B a Group Name 4";
    String group1descr = "a Group Description 1";
    String group2descr = "a Group Description 2";
    String group3descr = "a Group Description 3";
    String group4descr = "a Group Description 4";
    String group1commn = "a Group Comment 1";
    String group2commn = "a Group Comment 2";
    String group3commn = "a Group Comment 3";
    String group4commn = "a Group Comment 4";
    Boolean group1isEnabled = true;
    Boolean group2isEnabled = true;
    Boolean group3isEnabled = false;
    Boolean group4isEnabled = true;

    GroupRawInfo group1Raw = new GroupRawInfo();
    group1Raw.setGroupId(group1id);
    group1Raw.setName(group1name);
    group1Raw.setDescription(group1descr);
    group1Raw.setComment(group1commn);
    group1Raw.setIsEnabled(group1isEnabled);
    GroupRawInfo group2Raw = new GroupRawInfo();
    group2Raw.setGroupId(group2id);
    group2Raw.setName(group2name);
    group2Raw.setDescription(group2descr);
    group2Raw.setComment(group2commn);
    group2Raw.setIsEnabled(group2isEnabled);
    GroupRawInfo group3Raw = new GroupRawInfo();
    group3Raw.setGroupId(group3id);
    group3Raw.setName(group3name);
    group3Raw.setDescription(group3descr);
    group3Raw.setComment(group3commn);
    group3Raw.setIsEnabled(group3isEnabled);
    GroupRawInfo group4Raw = new GroupRawInfo();
    group4Raw.setGroupId(group4id);
    group4Raw.setName(group4name);
    group4Raw.setDescription(group4descr);
    group4Raw.setComment(group4commn);
    group4Raw.setIsEnabled(group4isEnabled);

    Map<UUID, DeviceInfo> deviceMap = null;

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.groupRepository.findById(eq(group1id))).thenReturn(group1Raw);
    when(this.groupRepository.findById(eq(group2id))).thenReturn(group2Raw);
    when(this.groupRepository.findById(eq(group3id))).thenReturn(group3Raw);
    when(this.groupRepository.findById(eq(group4id))).thenReturn(group4Raw);
    doReturn(deviceMap).when(deviceRepository).findAll();

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call service
    CommonGetResponse<GroupInfo> retObjResp = groupService.getGroupById(group1id);

    // Verify that appropriate Repository method was called
    verify(this.groupRepository, times(1)).findById(arg1.capture());

    // Assertions
    assertEquals(group1id, arg1.getValue(), "Argument must match.");
    assertNotNull(retObjResp, "Return object is not null.");
    assertEquals(expectedStatus, retObjResp.getResponse(), "Response code must be 200.");
    assertNull(retObjResp.getErrorResult(), "There should be no error returned.");
    assertNotNull(retObjResp.getObj(), "There is a return object.");
    assertEquals(group1id, retObjResp.getObj().getGroupId(),
        "Returned entry's UUID must match group 1's");
    assertEquals(group1name, retObjResp.getObj().getName(),
        "Returned entry's name must match group 1's");
    assertEquals(group1descr, retObjResp.getObj().getDescription(),
        "Returned entry's manufacturer must match group 1's");
    assertEquals(group1commn, retObjResp.getObj().getComment(),
        "Returned entry's comment must match group 1's");
    assertEquals(group1isEnabled, retObjResp.getObj().isIsEnabled(),
        "Returned entry's isEnabled must match group 1's");
    assertThat("The device List must be empty.", retObjResp.getObj().getDevices(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getGroupById (No entries in the device DB)")
  void testGetGroupByIdNoDeviceEntry() {
    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");
    UUID group2id = UUID.fromString("05abcdef-2222-2222-2222-222222222202");
    UUID group3id = UUID.fromString("05abcdef-3333-3333-3333-333333333303");
    UUID group4id = UUID.fromString("05abcdef-4444-4444-4444-444444444404");
    String group1name = "D a Group Name 1";
    String group2name = "C a Group Name 2";
    String group3name = "A a Group Name 3";
    String group4name = "B a Group Name 4";
    String group1descr = "a Group Description 1";
    String group2descr = "a Group Description 2";
    String group3descr = "a Group Description 3";
    String group4descr = "a Group Description 4";
    String group1commn = "a Group Comment 1";
    String group2commn = "a Group Comment 2";
    String group3commn = "a Group Comment 3";
    String group4commn = "a Group Comment 4";
    Boolean group1isEnabled = true;
    Boolean group2isEnabled = true;
    Boolean group3isEnabled = false;
    Boolean group4isEnabled = true;

    GroupRawInfo group1Raw = new GroupRawInfo();
    group1Raw.setGroupId(group1id);
    group1Raw.setName(group1name);
    group1Raw.setDescription(group1descr);
    group1Raw.setComment(group1commn);
    group1Raw.setIsEnabled(group1isEnabled);
    GroupRawInfo group2Raw = new GroupRawInfo();
    group2Raw.setGroupId(group2id);
    group2Raw.setName(group2name);
    group2Raw.setDescription(group2descr);
    group2Raw.setComment(group2commn);
    group2Raw.setIsEnabled(group2isEnabled);
    GroupRawInfo group3Raw = new GroupRawInfo();
    group3Raw.setGroupId(group3id);
    group3Raw.setName(group3name);
    group3Raw.setDescription(group3descr);
    group3Raw.setComment(group3commn);
    group3Raw.setIsEnabled(group3isEnabled);
    GroupRawInfo group4Raw = new GroupRawInfo();
    group4Raw.setGroupId(group4id);
    group4Raw.setName(group4name);
    group4Raw.setDescription(group4descr);
    group4Raw.setComment(group4commn);
    group4Raw.setIsEnabled(group4isEnabled);

    Map<UUID, DeviceInfo> deviceMap = Collections.emptyMap();

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.groupRepository.findById(eq(group1id))).thenReturn(group1Raw);
    when(this.groupRepository.findById(eq(group2id))).thenReturn(group2Raw);
    when(this.groupRepository.findById(eq(group3id))).thenReturn(group3Raw);
    when(this.groupRepository.findById(eq(group4id))).thenReturn(group4Raw);
    doReturn(deviceMap).when(deviceRepository).findAll();

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call service
    CommonGetResponse<GroupInfo> retObjResp = groupService.getGroupById(group1id);

    // Verify that appropriate Repository method was called
    verify(this.groupRepository, times(1)).findById(arg1.capture());

    // Assertions
    assertNotNull(retObjResp, "Return object is not null.");
    assertEquals(expectedStatus, retObjResp.getResponse(), "Response code must be 200.");
    assertNull(retObjResp.getErrorResult(), "There should be no error returned.");
    assertNotNull(retObjResp.getObj(), "There is a return object.");
    assertEquals(group1id, retObjResp.getObj().getGroupId(),
        "Returned entry's UUID must match group 1's");
    assertEquals(group1name, retObjResp.getObj().getName(),
        "Returned entry's name must match group 1's");
    assertEquals(group1descr, retObjResp.getObj().getDescription(),
        "Returned entry's manufacturer must match group 1's");
    assertEquals(group1commn, retObjResp.getObj().getComment(),
        "Returned entry's comment must match group 1's");
    assertEquals(group1isEnabled, retObjResp.getObj().isIsEnabled(),
        "Returned entry's isEnabled must match group 1's");
    assertThat("The device List must be empty.", retObjResp.getObj().getDevices(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getGroupById (Entries in the device DB have group Ids not specified by groupId param)")
  void testGetGroupByIdMismatchEntriesGroupVsDevice() {
    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");
    UUID group2id = UUID.fromString("05abcdef-2222-2222-2222-222222222202");
    UUID group3id = UUID.fromString("05abcdef-3333-3333-3333-333333333303");
    UUID group4id = UUID.fromString("05abcdef-4444-4444-4444-444444444404");
    String group1name = "D a Group Name 1";
    String group2name = "C a Group Name 2";
    String group3name = "A a Group Name 3";
    String group4name = "B a Group Name 4";
    String group1descr = "a Group Description 1";
    String group2descr = "a Group Description 2";
    String group3descr = "a Group Description 3";
    String group4descr = "a Group Description 4";
    String group1commn = "a Group Comment 1";
    String group2commn = "a Group Comment 2";
    String group3commn = "a Group Comment 3";
    String group4commn = "a Group Comment 4";
    Boolean group1isEnabled = true;
    Boolean group2isEnabled = true;
    Boolean group3isEnabled = false;
    Boolean group4isEnabled = true;

    UUID group1dev1 = UUID.fromString("15abcdef-9999-1111-1111-111111111101");
    UUID group1dev2 = UUID.fromString("15abcdef-9999-2222-1111-111111111101");
    UUID group2dev1 = UUID.fromString("15abcdef-9999-1111-2222-222222222202");
    UUID group2dev2 = UUID.fromString("15abcdef-9999-2222-2222-222222222202");
    UUID group3dev1 = UUID.fromString("15abcdef-9999-1111-3333-333333333303");
    UUID group3dev2 = UUID.fromString("15abcdef-9999-2222-3333-333333333303");
    UUID group4dev1 = UUID.fromString("15abcdef-9999-1111-4444-444444444404");
    UUID group4dev2 = UUID.fromString("15abcdef-9999-2222-4444-444444444404");

    GroupRawInfo group1Raw = new GroupRawInfo();
    group1Raw.setGroupId(group1id);
    group1Raw.setName(group1name);
    group1Raw.setDescription(group1descr);
    group1Raw.setComment(group1commn);
    group1Raw.setIsEnabled(group1isEnabled);
    GroupRawInfo group2Raw = new GroupRawInfo();
    group2Raw.setGroupId(group2id);
    group2Raw.setName(group2name);
    group2Raw.setDescription(group2descr);
    group2Raw.setComment(group2commn);
    group2Raw.setIsEnabled(group2isEnabled);
    GroupRawInfo group3Raw = new GroupRawInfo();
    group3Raw.setGroupId(group3id);
    group3Raw.setName(group3name);
    group3Raw.setDescription(group3descr);
    group3Raw.setComment(group3commn);
    group3Raw.setIsEnabled(group3isEnabled);
    GroupRawInfo group4Raw = new GroupRawInfo();
    group4Raw.setGroupId(group4id);
    group4Raw.setName(group4name);
    group4Raw.setDescription(group4descr);
    group4Raw.setComment(group4commn);
    group4Raw.setIsEnabled(group4isEnabled);

    UUID deviceId1 = UUID.fromString("99999999-9999-9999-9999-999999999991");
    UUID deviceId2 = UUID.fromString("99999999-9999-9999-9999-999999999992");
    UUID deviceId3 = UUID.fromString("99999999-9999-9999-9999-999999999993");
    UUID deviceId4 = UUID.fromString("99999999-9999-9999-9999-999999999994");
    UUID deviceId5 = UUID.fromString("99999999-9999-9999-9999-999999999995");
    UUID deviceId6 = UUID.fromString("99999999-9999-9999-9999-999999999996");
    UUID deviceId7 = UUID.fromString("99999999-9999-9999-9999-999999999997");
    UUID deviceId8 = UUID.fromString("99999999-9999-9999-9999-999999999998");
    DeviceInfo device1 = new DeviceInfo().deviceId(group1dev1).groupId(deviceId1);
    DeviceInfo device2 = new DeviceInfo().deviceId(group1dev2).groupId(deviceId2);
    DeviceInfo device3 = new DeviceInfo().deviceId(group2dev1).groupId(deviceId3);
    DeviceInfo device4 = new DeviceInfo().deviceId(group2dev2).groupId(deviceId4);
    DeviceInfo device5 = new DeviceInfo().deviceId(group3dev1).groupId(deviceId5);
    DeviceInfo device6 = new DeviceInfo().deviceId(group3dev2).groupId(deviceId6);
    DeviceInfo device7 = new DeviceInfo().deviceId(group4dev1).groupId(deviceId7);
    DeviceInfo device8 = new DeviceInfo().deviceId(group4dev2).groupId(deviceId8);

    Map<UUID, DeviceInfo> deviceMap = new HashMap<>();
    deviceMap.put(deviceId1, device1);
    deviceMap.put(deviceId2, device2);
    deviceMap.put(deviceId3, device3);
    deviceMap.put(deviceId4, device4);
    deviceMap.put(deviceId5, device5);
    deviceMap.put(deviceId6, device6);
    deviceMap.put(deviceId7, device7);
    deviceMap.put(deviceId8, device8);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.groupRepository.findById(eq(group1id))).thenReturn(group1Raw);
    when(this.groupRepository.findById(eq(group2id))).thenReturn(group2Raw);
    when(this.groupRepository.findById(eq(group3id))).thenReturn(group3Raw);
    when(this.groupRepository.findById(eq(group4id))).thenReturn(group4Raw);
    doReturn(deviceMap).when(deviceRepository).findAll();

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call service
    CommonGetResponse<GroupInfo> retObjResp = groupService.getGroupById(group1id);

    // Verify that appropriate Repository method was called
    verify(this.groupRepository, times(1)).findById(arg1.capture());

    // Assertions
    assertEquals(group1id, arg1.getValue(), "Argument must match.");
    assertNotNull(retObjResp, "Return object is not null.");
    assertEquals(expectedStatus, retObjResp.getResponse(), "Response code must be 200.");
    assertNull(retObjResp.getErrorResult(), "There should be no error returned.");
    assertNotNull(retObjResp.getObj(), "There is a return object.");
    assertEquals(group1id, retObjResp.getObj().getGroupId(),
        "Returned entry's UUID must match group 1's");
    assertEquals(group1name, retObjResp.getObj().getName(),
        "Returned entry's name must match group 1's");
    assertEquals(group1descr, retObjResp.getObj().getDescription(),
        "Returned entry's manufacturer must match group 1's");
    assertEquals(group1commn, retObjResp.getObj().getComment(),
        "Returned entry's comment must match group 1's");
    assertEquals(group1isEnabled, retObjResp.getObj().isIsEnabled(),
        "Returned entry's isEnabled must match group 1's");
    assertThat("The device List must be empty.", retObjResp.getObj().getDevices(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST registerGroup")
  void testRegisterGroup() {
    String group1name = "New Name";
    String group1descr = "New Description";
    String group1comment = "New Comment";

    GroupRegistration newGroup = new GroupRegistration();
    newGroup.setName(group1name);
    newGroup.setDescription(group1descr);
    newGroup.setComment(group1comment);

    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");
    UUID group2id = UUID.fromString("05abcdef-2222-2222-2222-222222222202");
    String xGroup1name = "D a Group Name 1";
    String xGroup2name = "C a Group Name 2";
    String xGroup1descr = "a Group Description 1";
    String xGroup2descr = "a Group Description 2";
    String xGroup1commn = "a Group Comment 1";
    String xGroup2commn = "a Group Comment 2";
    Boolean xGroup1isEnabled = true;
    Boolean xGroup2isEnabled = true;

    GroupRawInfo group1Raw = new GroupRawInfo();
    group1Raw.setGroupId(group1id);
    group1Raw.setName(xGroup1name);
    group1Raw.setDescription(xGroup1descr);
    group1Raw.setComment(xGroup1commn);
    group1Raw.setIsEnabled(xGroup1isEnabled);
    GroupRawInfo group2Raw = new GroupRawInfo();
    group2Raw.setGroupId(group2id);
    group2Raw.setName(xGroup2name);
    group2Raw.setDescription(xGroup2descr);
    group2Raw.setComment(xGroup2commn);
    group2Raw.setIsEnabled(xGroup2isEnabled);

    Map<UUID, GroupRawInfo> groupMap = new HashMap<>();
    groupMap.put(group1id, group1Raw);
    groupMap.put(group2id, group2Raw);

    HttpStatus expectedStatus = HttpStatus.CREATED;

    // Mock(s)
    doReturn(groupMap).when(this.groupRepository).findAll();
    when(this.groupRepository.findById(any(UUID.class))).thenReturn(new GroupRawInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<GroupRawInfo> arg1 = ArgumentCaptor.forClass(GroupRawInfo.class);

    // Call service
    CommonAddResponse operResp = this.groupService.registerGroup(newGroup);

    // Verify that appropriate Repository method was called
    verify(this.groupRepository, times(1)).add(arg1.capture());

    // Assertions
    assertNotNull(arg1.getValue(), "Passed Argument must not be null");

    UUID group1Id = arg1.getValue().getGroupId();
    assertNotNull(group1Id, "groupId of Argument must not be null");
    assertEquals(group1name, arg1.getValue().getName(), "name of Argument must match");
    assertEquals(group1descr, arg1.getValue().getDescription(),
        "description of Argument must match");
    assertEquals(group1comment, arg1.getValue().getComment(), "comment of Argument must match");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertEquals(group1Id, operResp.getId(), "returned groupId must match");
  }

  @Test
  @DisplayName("TEST registerGroup (there are no existing groups (null))")
  void testRegisterGroupNoEntryNull() {
    String group1name = "New Name";
    String group1descr = "New Description";
    String group1comment = "New Comment";

    GroupRegistration newGroup = new GroupRegistration();
    newGroup.setName(group1name);
    newGroup.setDescription(group1descr);
    newGroup.setComment(group1comment);

    Map<UUID, GroupRawInfo> groupMap = null;

    HttpStatus expectedStatus = HttpStatus.CREATED;

    // Mock(s)
    doReturn(groupMap).when(this.groupRepository).findAll();

    // Argument Captors
    ArgumentCaptor<GroupRawInfo> arg1 = ArgumentCaptor.forClass(GroupRawInfo.class);

    // Call service
    CommonAddResponse operResp = this.groupService.registerGroup(newGroup);

    // Verify that appropriate Repository method was called
    verify(this.groupRepository, times(1)).add(arg1.capture());

    // Assertions
    assertNotNull(arg1.getValue(), "Passed Argument must not be null");

    UUID group1Id = arg1.getValue().getGroupId();
    assertNotNull(group1Id, "groupId of Argument must not be null");
    assertEquals(group1name, arg1.getValue().getName(), "name of Argument must match");
    assertEquals(group1descr, arg1.getValue().getDescription(),
        "description of Argument must match");
    assertEquals(group1comment, arg1.getValue().getComment(), "comment of Argument must match");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertEquals(group1Id, operResp.getId(), "returned groupId must match");
  }

  @Test
  @DisplayName("TEST registerGroup (there are no existing groups)")
  void testRegisterGroupNoEntry() {
    String group1name = "New Name";
    String group1descr = "New Description";
    String group1comment = "New Comment";

    GroupRegistration newGroup = new GroupRegistration();
    newGroup.setName(group1name);
    newGroup.setDescription(group1descr);
    newGroup.setComment(group1comment);

    Map<UUID, GroupRawInfo> groupMap = Collections.emptyMap();

    HttpStatus expectedStatus = HttpStatus.CREATED;

    // Mock(s)
    doReturn(groupMap).when(this.groupRepository).findAll();

    // Argument Captors
    ArgumentCaptor<GroupRawInfo> arg1 = ArgumentCaptor.forClass(GroupRawInfo.class);

    // Call service
    CommonAddResponse operResp = this.groupService.registerGroup(newGroup);

    // Verify that appropriate Repository method was called
    verify(this.groupRepository, times(1)).add(arg1.capture());

    // Assertions
    assertNotNull(arg1.getValue(), "Passed Argument must not be null");

    UUID group1Id = arg1.getValue().getGroupId();
    assertNotNull(group1Id, "groupId of Argument must not be null");
    assertEquals(group1name, arg1.getValue().getName(), "name of Argument must match");
    assertEquals(group1descr, arg1.getValue().getDescription(),
        "description of Argument must match");
    assertEquals(group1comment, arg1.getValue().getComment(), "comment of Argument must match");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertEquals(group1Id, operResp.getId(), "returned groupId must match");
  }

  @Test
  @DisplayName("TEST registerGroup (a group with the same name already exists)")
  void testRegisterGroupExistingName() {
    String group1name = "New Name";
    String group1descr = "New Description";
    String group1comment = "New Comment";

    GroupRegistration newGroup = new GroupRegistration();
    newGroup.setName(group1name);
    newGroup.setDescription(group1descr);
    newGroup.setComment(group1comment);

    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");
    UUID group2id = UUID.fromString("05abcdef-2222-2222-2222-222222222202");
    String xGroup1name = "D a Group Name 1";
    String xGroup1descr = "a Group Description 1";
    String xGroup2descr = "a Group Description 2";
    String xGroup1commn = "a Group Comment 1";
    String xGroup2commn = "a Group Comment 2";
    Boolean xGroup1isEnabled = true;
    Boolean xGroup2isEnabled = true;

    GroupRawInfo group1Raw = new GroupRawInfo();
    group1Raw.setGroupId(group1id);
    group1Raw.setName(xGroup1name);
    group1Raw.setDescription(xGroup1descr);
    group1Raw.setComment(xGroup1commn);
    group1Raw.setIsEnabled(xGroup1isEnabled);
    GroupRawInfo group2Raw = new GroupRawInfo();
    group2Raw.setGroupId(group2id);
    group2Raw.setName(group1name);
    group2Raw.setDescription(xGroup2descr);
    group2Raw.setComment(xGroup2commn);
    group2Raw.setIsEnabled(xGroup2isEnabled);

    Map<UUID, GroupRawInfo> groupMap = new HashMap<>();
    groupMap.put(group1id, group1Raw);
    groupMap.put(group2id, group2Raw);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    doReturn(groupMap).when(this.groupRepository).findAll();

    // Argument Captors
    ArgumentCaptor<GroupRawInfo> arg1 = ArgumentCaptor.forClass(GroupRawInfo.class);

    // Call service
    CommonAddResponse operResp = this.groupService.registerGroup(newGroup);

    // Verify that appropriate Repository method was NOT called
    verify(this.groupRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "returned groupId must be null");
  }

  @Test
  @DisplayName("TEST modifyGroup)")
  void testModifyGroup() {
    String group1nameUpd = "Updated Name";
    String group1descrUpd = "Updated Description";
    String group1commentUpd = "Updated Comment";

    GroupModification updatedGroup = new GroupModification();
    updatedGroup.setName(group1nameUpd);
    updatedGroup.setDescription(group1descrUpd);
    updatedGroup.setComment(group1commentUpd);

    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");
    UUID group2id = UUID.fromString("05abcdef-2222-2222-2222-222222222202");
    String group1name = "Existing Name 1";
    String group2name = "Existing Name 2";
    String group1descr = "Existing Description 1";
    String group2descr = "Existing Description 2";
    String group1comment = "Existing Comment 1";
    String group2comment = "Existing Comment 2";

    GroupRawInfo group1 = new GroupRawInfo();
    group1.setGroupId(group1id);
    group1.setName(group1name);
    group1.setDescription(group1descr);
    group1.setComment(group1comment);
    GroupRawInfo group2 = new GroupRawInfo();
    group2.setGroupId(group2id);
    group2.setName(group2name);
    group2.setDescription(group2descr);
    group2.setComment(group2comment);

    Map<UUID, GroupRawInfo> groupMap = new HashMap<>();
    groupMap.put(group1id, group1);
    groupMap.put(group2id, group2);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(groupMap).when(this.groupRepository).findAll();

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<GroupRawInfo> arg2 = ArgumentCaptor.forClass(GroupRawInfo.class);

    // Call service
    CommonOperResponse operResp = this.groupService.modifyGroup(group1id, updatedGroup);

    // Verify that appropriate Repository method was called
    verify(this.groupRepository, times(1)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertEquals(group1id, arg1.getValue(), "Argument must match.");
    assertNotNull(arg2.getValue(), "Argument (GroupRawInfo) must not be null.");
    assertEquals(group1nameUpd, arg2.getValue().getName(), "Argument's name must match.");
    assertEquals(group1descrUpd, arg2.getValue().getDescription(),
        "Argument's description must match.");
    assertEquals(group1commentUpd, arg2.getValue().getComment(), "Argument's comment must match.");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyGroup (No entries in the group DB(null))")
  void testModifyGroupNoGroupEntryNull() {
    String group1nameUpd = "Updated Name";
    String group1descrUpd = "Updated Description";
    String group1commentUpd = "Updated Comment";

    GroupModification updatedGroup = new GroupModification();
    updatedGroup.setName(group1nameUpd);
    updatedGroup.setDescription(group1descrUpd);
    updatedGroup.setComment(group1commentUpd);

    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");

    Map<UUID, GroupRawInfo> groupMap = null;

    HttpStatus expectedStatus = HttpStatus.NOT_FOUND;

    // Mock(s)
    doReturn(groupMap).when(this.groupRepository).findAll();

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<GroupRawInfo> arg2 = ArgumentCaptor.forClass(GroupRawInfo.class);

    // Call service
    CommonOperResponse operResp = this.groupService.modifyGroup(group1id, updatedGroup);

    // Verify that appropriate Repository method was NOT called
    verify(this.groupRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 404.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyGroup (No entries in the group DB)")
  void testModifyGroupNoGroupEntry() {
    String group1nameUpd = "Updated Name";
    String group1descrUpd = "Updated Description";
    String group1commentUpd = "Updated Comment";

    GroupModification updatedGroup = new GroupModification();
    updatedGroup.setName(group1nameUpd);
    updatedGroup.setDescription(group1descrUpd);
    updatedGroup.setComment(group1commentUpd);

    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");

    Map<UUID, GroupRawInfo> groupMap = Collections.emptyMap();

    HttpStatus expectedStatus = HttpStatus.NOT_FOUND;

    // Mock(s)
    doReturn(groupMap).when(this.groupRepository).findAll();

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<GroupRawInfo> arg2 = ArgumentCaptor.forClass(GroupRawInfo.class);

    // Call service
    CommonOperResponse operResp = this.groupService.modifyGroup(group1id, updatedGroup);

    // Verify that appropriate Repository method was NOT called
    verify(this.groupRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 404.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyGroup (groupId is not found))")
  void testModifyGroupGroupIdNotFound() {
    String group1nameUpd = "Updated Name";
    String group1descrUpd = "Updated Description";
    String group1commentUpd = "Updated Comment";

    GroupModification updatedGroup = new GroupModification();
    updatedGroup.setName(group1nameUpd);
    updatedGroup.setDescription(group1descrUpd);
    updatedGroup.setComment(group1commentUpd);

    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");
    UUID group2id = UUID.fromString("05abcdef-2222-2222-2222-222222222202");
    UUID othergrpId = UUID.fromString("99999999-ffff-aaaa-cccc-eeeeeeeeeeee");
    String group1name = "Existing Name 1";
    String group2name = "Existing Name 2";
    String group1descr = "Existing Description 1";
    String group2descr = "Existing Description 2";
    String group1comment = "Existing Comment 1";
    String group2comment = "Existing Comment 2";

    GroupRawInfo group1 = new GroupRawInfo();
    group1.setGroupId(group1id);
    group1.setName(group1name);
    group1.setDescription(group1descr);
    group1.setComment(group1comment);
    GroupRawInfo group2 = new GroupRawInfo();
    group2.setGroupId(group2id);
    group2.setName(group2name);
    group2.setDescription(group2descr);
    group2.setComment(group2comment);

    Map<UUID, GroupRawInfo> groupMap = new HashMap<>();
    groupMap.put(group1id, group1);
    groupMap.put(group2id, group2);

    HttpStatus expectedStatus = HttpStatus.NOT_FOUND;

    // Mock(s)
    doReturn(groupMap).when(this.groupRepository).findAll();

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<GroupRawInfo> arg2 = ArgumentCaptor.forClass(GroupRawInfo.class);

    // Call service
    CommonOperResponse operResp = this.groupService.modifyGroup(othergrpId, updatedGroup);

    // Verify that appropriate Repository method was NOT called
    verify(this.groupRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 404.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyGroup (name is already used by same group))")
  void testModifyGroupSameNameSameEntry() {
    String group1nameUpd = "Existing Name 1";
    String group1descrUpd = "Updated Description";
    String group1commentUpd = "Updated Comment";

    GroupModification updatedGroup = new GroupModification();
    updatedGroup.setName(group1nameUpd);
    updatedGroup.setDescription(group1descrUpd);
    updatedGroup.setComment(group1commentUpd);

    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");
    UUID group2id = UUID.fromString("05abcdef-2222-2222-2222-222222222202");
    String group1name = "Existing Name 1";
    String group2name = "Existing Name 2";
    String group1descr = "Existing Description 1";
    String group2descr = "Existing Description 2";
    String group1comment = "Existing Comment 1";
    String group2comment = "Existing Comment 2";

    GroupRawInfo group1 = new GroupRawInfo();
    group1.setGroupId(group1id);
    group1.setName(group1name);
    group1.setDescription(group1descr);
    group1.setComment(group1comment);
    GroupRawInfo group2 = new GroupRawInfo();
    group2.setGroupId(group2id);
    group2.setName(group2name);
    group2.setDescription(group2descr);
    group2.setComment(group2comment);

    Map<UUID, GroupRawInfo> groupMap = new HashMap<>();
    groupMap.put(group1id, group1);
    groupMap.put(group2id, group2);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(groupMap).when(this.groupRepository).findAll();

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<GroupRawInfo> arg2 = ArgumentCaptor.forClass(GroupRawInfo.class);

    // Call service
    CommonOperResponse operResp = this.groupService.modifyGroup(group1id, updatedGroup);

    // Verify that appropriate Repository method was called
    verify(this.groupRepository, times(1)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertEquals(group1id, arg1.getValue(), "Argument must match.");
    assertNotNull(arg2.getValue(), "Argument (GroupRawInfo) must not be null.");
    assertEquals(group1nameUpd, arg2.getValue().getName(), "Argument's name must match.");
    assertEquals(group1descrUpd, arg2.getValue().getDescription(),
        "Argument's description must match.");
    assertEquals(group1commentUpd, arg2.getValue().getComment(), "Argument's comment must match.");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyGroup (name is already used by another group))")
  void testModifyGroupSameNameOtherEntry() {
    String group1nameUpd = "Existing Name 2";
    String group1descrUpd = "Updated Description";
    String group1commentUpd = "Updated Comment";

    GroupModification updatedGroup = new GroupModification();
    updatedGroup.setName(group1nameUpd);
    updatedGroup.setDescription(group1descrUpd);
    updatedGroup.setComment(group1commentUpd);

    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");
    UUID group2id = UUID.fromString("05abcdef-2222-2222-2222-222222222202");
    String group1name = "Existing Name 1";
    String group2name = "Existing Name 2";
    String group1descr = "Existing Description 1";
    String group2descr = "Existing Description 2";
    String group1comment = "Existing Comment 1";
    String group2comment = "Existing Comment 2";

    GroupRawInfo group1 = new GroupRawInfo();
    group1.setGroupId(group1id);
    group1.setName(group1name);
    group1.setDescription(group1descr);
    group1.setComment(group1comment);
    GroupRawInfo group2 = new GroupRawInfo();
    group2.setGroupId(group2id);
    group2.setName(group2name);
    group2.setDescription(group2descr);
    group2.setComment(group2comment);

    Map<UUID, GroupRawInfo> groupMap = new HashMap<>();
    groupMap.put(group1id, group1);
    groupMap.put(group2id, group2);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    doReturn(groupMap).when(this.groupRepository).findAll();

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<GroupRawInfo> arg2 = ArgumentCaptor.forClass(GroupRawInfo.class);

    // Call service
    CommonOperResponse operResp = this.groupService.modifyGroup(group1id, updatedGroup);

    // Verify that appropriate Repository method was NOT called
    verify(this.groupRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST enableDisableGroup")
  void testEnableDisableGroup() {
    boolean isEnabled = true;

    EnableDisableGroup setGrpParam = new EnableDisableGroup();
    setGrpParam.setIsEnabled(isEnabled);

    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");
    UUID group2id = UUID.fromString("05abcdef-2222-2222-2222-222222222202");
    String group1name = "Existing Name 1";
    String group2name = "Existing Name 2";
    String group1descr = "Existing Description 1";
    String group2descr = "Existing Description 2";
    String group1comment = "Existing Comment 1";
    String group2comment = "Existing Comment 2";

    GroupRawInfo group1 = new GroupRawInfo();
    group1.setGroupId(group1id);
    group1.setName(group1name);
    group1.setDescription(group1descr);
    group1.setComment(group1comment);
    group1.setIsEnabled(false);
    GroupRawInfo group2 = new GroupRawInfo();
    group2.setGroupId(group2id);
    group2.setName(group2name);
    group2.setDescription(group2descr);
    group2.setComment(group2comment);
    group2.setIsEnabled(false);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.groupRepository.findById(eq(group1id))).thenReturn(group1);
    when(this.groupRepository.findById(eq(group2id))).thenReturn(group2);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<GroupRawInfo> arg2 = ArgumentCaptor.forClass(GroupRawInfo.class);

    // Call service
    CommonOperResponse operResp = this.groupService.enableDisableGroup(group1id, setGrpParam);

    // Verify that appropriate Repository method was called
    verify(this.groupRepository, times(1)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertEquals(group1id, arg1.getValue(), "Argument must match.");
    assertNotNull(arg2.getValue(), "Argument (GroupRawInfo) must not be null.");
    assertEquals(group1name, arg2.getValue().getName(), "Argument's name must match.");
    assertEquals(group1descr, arg2.getValue().getDescription(),
        "Argument's description must match.");
    assertEquals(group1comment, arg2.getValue().getComment(), "Argument's comment must match.");
    assertEquals(isEnabled, arg2.getValue().isIsEnabled(), "Argument's isEnabled must match.");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST enableDisableGroup (groupId is not found)")
  void testEnableDisableGroupGroupIdNotFound() {
    boolean isEnabled = true;

    EnableDisableGroup setGrpParam = new EnableDisableGroup();
    setGrpParam.setIsEnabled(isEnabled);

    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");
    UUID group2id = UUID.fromString("05abcdef-2222-2222-2222-222222222202");
    UUID othergrpId = UUID.fromString("99999999-ffff-aaaa-cccc-eeeeeeeeeeee");
    String group1name = "Existing Name 1";
    String group2name = "Existing Name 2";
    String group1descr = "Existing Description 1";
    String group2descr = "Existing Description 2";
    String group1comment = "Existing Comment 1";
    String group2comment = "Existing Comment 2";

    GroupRawInfo group1 = new GroupRawInfo();
    group1.setGroupId(group1id);
    group1.setName(group1name);
    group1.setDescription(group1descr);
    group1.setComment(group1comment);
    group1.setIsEnabled(false);
    GroupRawInfo group2 = new GroupRawInfo();
    group2.setGroupId(group2id);
    group2.setName(group2name);
    group2.setDescription(group2descr);
    group2.setComment(group2comment);
    group2.setIsEnabled(false);

    HttpStatus expectedStatus = HttpStatus.NOT_FOUND;

    // Mock(s)
    when(this.groupRepository.findById(eq(group1id))).thenReturn(group1);
    when(this.groupRepository.findById(eq(group2id))).thenReturn(group2);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<GroupRawInfo> arg2 = ArgumentCaptor.forClass(GroupRawInfo.class);

    // Call service
    CommonOperResponse operResp = this.groupService.enableDisableGroup(othergrpId, setGrpParam);

    // Verify that appropriate Repository method was NOT called
    verify(this.groupRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 404.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST enableDisableGroup (group is already enabled (set to enable))")
  void testEnableDisableGroupAlreadyEnabled() {
    boolean isEnabled = true;

    EnableDisableGroup setGrpParam = new EnableDisableGroup();
    setGrpParam.setIsEnabled(isEnabled);

    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");
    UUID group2id = UUID.fromString("05abcdef-2222-2222-2222-222222222202");
    String group1name = "Existing Name 1";
    String group2name = "Existing Name 2";
    String group1descr = "Existing Description 1";
    String group2descr = "Existing Description 2";
    String group1comment = "Existing Comment 1";
    String group2comment = "Existing Comment 2";

    GroupRawInfo group1 = new GroupRawInfo();
    group1.setGroupId(group1id);
    group1.setName(group1name);
    group1.setDescription(group1descr);
    group1.setComment(group1comment);
    group1.setIsEnabled(true);
    GroupRawInfo group2 = new GroupRawInfo();
    group2.setGroupId(group2id);
    group2.setName(group2name);
    group2.setDescription(group2descr);
    group2.setComment(group2comment);
    group2.setIsEnabled(false);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    when(this.groupRepository.findById(eq(group1id))).thenReturn(group1);
    when(this.groupRepository.findById(eq(group2id))).thenReturn(group2);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<GroupRawInfo> arg2 = ArgumentCaptor.forClass(GroupRawInfo.class);

    // Call service
    CommonOperResponse operResp = this.groupService.enableDisableGroup(group1id, setGrpParam);

    // Verify that appropriate Repository method was NOT called
    verify(this.groupRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST enableDisableGroup (group is already disabled (set to disable))")
  void testEnableDisableGroupAlreadyDisabled() {
    boolean isEnabled = false;

    EnableDisableGroup setGrpParam = new EnableDisableGroup();
    setGrpParam.setIsEnabled(isEnabled);

    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");
    UUID group2id = UUID.fromString("05abcdef-2222-2222-2222-222222222202");
    String group1name = "Existing Name 1";
    String group2name = "Existing Name 2";
    String group1descr = "Existing Description 1";
    String group2descr = "Existing Description 2";
    String group1comment = "Existing Comment 1";
    String group2comment = "Existing Comment 2";

    GroupRawInfo group1 = new GroupRawInfo();
    group1.setGroupId(group1id);
    group1.setName(group1name);
    group1.setDescription(group1descr);
    group1.setComment(group1comment);
    group1.setIsEnabled(false);
    GroupRawInfo group2 = new GroupRawInfo();
    group2.setGroupId(group2id);
    group2.setName(group2name);
    group2.setDescription(group2descr);
    group2.setComment(group2comment);
    group2.setIsEnabled(false);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    when(this.groupRepository.findById(eq(group1id))).thenReturn(group1);
    when(this.groupRepository.findById(eq(group2id))).thenReturn(group2);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<GroupRawInfo> arg2 = ArgumentCaptor.forClass(GroupRawInfo.class);

    // Call service
    CommonOperResponse operResp = this.groupService.enableDisableGroup(group1id, setGrpParam);

    // Verify that appropriate Repository method was NOT called
    verify(this.groupRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST deleteGroup")
  void testDeleteGroup() {
    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");
    String group1name = "Existing Name 1";
    String group1descr = "Existing Description 1";
    String group1comment = "Existing Comment 1";

    GroupRawInfo group1 = new GroupRawInfo();
    group1.setGroupId(group1id);
    group1.setName(group1name);
    group1.setDescription(group1descr);
    group1.setComment(group1comment);
    group1.setIsEnabled(false);

    UUID dev1id = UUID.fromString("3f985f64-0314-ffff-eeee-2111af789af1");
    UUID dev2id = UUID.fromString("3f985f64-0314-ffff-eeee-2222bf789af1");
    UUID dev3id = UUID.fromString("3f985f64-0314-ffff-eeee-3333cf789af1");
    String dev1name = "Device 1";
    String dev2name = "Device 2";
    String dev3name = "Device 3";
    DeviceInfo device1 =
        new DeviceInfo().deviceId(dev1id).name(dev1name).isGrouped(true).groupId(group1id);
    DeviceInfo device2 =
        new DeviceInfo().deviceId(dev2id).name(dev2name).isGrouped(true).groupId(group1id);
    DeviceInfo device3 =
        new DeviceInfo().deviceId(dev3id).name(dev3name).isGrouped(true).groupId(group1id);

    Map<UUID, DeviceInfo> deviceMap = new HashMap<>();
    deviceMap.put(dev1id, device1);
    deviceMap.put(dev2id, device2);
    deviceMap.put(dev3id, device3);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.groupRepository.findById(eq(group1id))).thenReturn(group1);
    doReturn(deviceMap).when(this.deviceRepository).findAll();

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg3 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call service
    CommonOperResponse operResp = this.groupService.deleteGroup(group1id);

    // Verify that appropriate Repository methods were called
    verify(this.groupRepository, times(1)).delete(arg1.capture());
    verify(this.deviceRepository, times(3)).update(arg2.capture(), arg3.capture());

    // Assertions
    assertEquals(group1id, arg1.getValue(), "Argument must match.");
    assertThat("All deviceId Arguments must match", arg2.getAllValues(),
        containsInAnyOrder(dev1id, dev2id, dev3id));
    List<DeviceInfo> devArguments = arg3.getAllValues();
    assertThat("All DeviceInfo Arguments must match", devArguments,
        containsInAnyOrder(device1, device2, device3));
    List<Boolean> isGroupedList =
        devArguments.stream().map(DeviceInfo::isIsGrouped).collect(Collectors.toList());
    List<UUID> groupIdList =
        devArguments.stream().map(DeviceInfo::getGroupId).collect(Collectors.toList());
    assertThat("All isGrouped DeviceInfos must be false", isGroupedList,
        contains(false, false, false));
    assertThat("All groupId DeviceInfos must be null", groupIdList, contains(null, null, null));
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST deleteGroup (groupId is not found)")
  void testDeleteGroupGroupIdNotFound() {
    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");
    UUID otherId = UUID.fromString("ffffffff-0000-1111-1111-111111111101");
    String group1name = "Existing Name 1";
    String group1descr = "Existing Description 1";
    String group1comment = "Existing Comment 1";

    GroupRawInfo group1 = new GroupRawInfo();
    group1.setGroupId(group1id);
    group1.setName(group1name);
    group1.setDescription(group1descr);
    group1.setComment(group1comment);
    group1.setIsEnabled(false);

    UUID dev1id = UUID.fromString("3f985f64-0314-ffff-eeee-2111af789af1");
    UUID dev2id = UUID.fromString("3f985f64-0314-ffff-eeee-2222bf789af1");
    UUID dev3id = UUID.fromString("3f985f64-0314-ffff-eeee-3333cf789af1");
    String dev1name = "Device 1";
    String dev2name = "Device 2";
    String dev3name = "Device 3";
    DeviceInfo device1 =
        new DeviceInfo().deviceId(dev1id).name(dev1name).isGrouped(true).groupId(group1id);
    DeviceInfo device2 =
        new DeviceInfo().deviceId(dev2id).name(dev2name).isGrouped(true).groupId(group1id);
    DeviceInfo device3 =
        new DeviceInfo().deviceId(dev3id).name(dev3name).isGrouped(true).groupId(group1id);

    Map<UUID, DeviceInfo> deviceMap = new HashMap<>();
    deviceMap.put(dev1id, device1);
    deviceMap.put(dev2id, device2);
    deviceMap.put(dev3id, device3);

    HttpStatus expectedStatus = HttpStatus.NOT_FOUND;

    // Mock(s)
    when(this.groupRepository.findById(eq(group1id))).thenReturn(group1);
    doReturn(deviceMap).when(this.deviceRepository).findAll();

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg3 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call service
    CommonOperResponse operResp = this.groupService.deleteGroup(otherId);

    // Verify that appropriate Repository methods were called
    verify(this.groupRepository, times(0)).delete(arg1.capture());
    verify(this.deviceRepository, times(0)).update(arg2.capture(), arg3.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 404.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST deleteGroup (group is enabled)")
  void testDeleteGroupGroupEnabled() {
    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");
    String group1name = "Existing Name 1";
    String group1descr = "Existing Description 1";
    String group1comment = "Existing Comment 1";

    GroupRawInfo group1 = new GroupRawInfo();
    group1.setGroupId(group1id);
    group1.setName(group1name);
    group1.setDescription(group1descr);
    group1.setComment(group1comment);
    group1.setIsEnabled(true);

    UUID dev1id = UUID.fromString("3f985f64-0314-ffff-eeee-2111af789af1");
    UUID dev2id = UUID.fromString("3f985f64-0314-ffff-eeee-2222bf789af1");
    UUID dev3id = UUID.fromString("3f985f64-0314-ffff-eeee-3333cf789af1");
    String dev1name = "Device 1";
    String dev2name = "Device 2";
    String dev3name = "Device 3";
    DeviceInfo device1 =
        new DeviceInfo().deviceId(dev1id).name(dev1name).isGrouped(true).groupId(group1id);
    DeviceInfo device2 =
        new DeviceInfo().deviceId(dev2id).name(dev2name).isGrouped(true).groupId(group1id);
    DeviceInfo device3 =
        new DeviceInfo().deviceId(dev3id).name(dev3name).isGrouped(true).groupId(group1id);

    Map<UUID, DeviceInfo> deviceMap = new HashMap<>();
    deviceMap.put(dev1id, device1);
    deviceMap.put(dev2id, device2);
    deviceMap.put(dev3id, device3);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot delete group";
    String expectedDetail = "Enabled groups canot be deleted.";

    // Mock(s)
    when(this.groupRepository.findById(eq(group1id))).thenReturn(group1);
    doReturn(deviceMap).when(this.deviceRepository).findAll();

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg3 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call service
    CommonOperResponse operResp = this.groupService.deleteGroup(group1id);

    // Verify that appropriate Repository methods were called
    verify(this.groupRepository, times(0)).delete(arg1.capture());
    verify(this.deviceRepository, times(0)).update(arg2.capture(), arg3.capture());

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
  @DisplayName("TEST deleteGroup (group is not associated with any device)")
  void testDeleteGroupIsolatedGroup() {
    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");
    UUID otherId = UUID.fromString("05abcdef-8888-cccc-1111-111111111101");
    String group1name = "Existing Name 1";
    String group1descr = "Existing Description 1";
    String group1comment = "Existing Comment 1";

    GroupRawInfo group1 = new GroupRawInfo();
    group1.setGroupId(group1id);
    group1.setName(group1name);
    group1.setDescription(group1descr);
    group1.setComment(group1comment);
    group1.setIsEnabled(false);

    UUID dev1id = UUID.fromString("3f985f64-0314-ffff-eeee-2111af789af1");
    UUID dev2id = UUID.fromString("3f985f64-0314-ffff-eeee-2222bf789af1");
    UUID dev3id = UUID.fromString("3f985f64-0314-ffff-eeee-3333cf789af1");
    String dev1name = "Device 1";
    String dev2name = "Device 2";
    String dev3name = "Device 3";
    DeviceInfo device1 =
        new DeviceInfo().deviceId(dev1id).name(dev1name).isGrouped(true).groupId(otherId);
    DeviceInfo device2 =
        new DeviceInfo().deviceId(dev2id).name(dev2name).isGrouped(true).groupId(otherId);
    DeviceInfo device3 = new DeviceInfo().deviceId(dev3id).name(dev3name).isGrouped(false);

    Map<UUID, DeviceInfo> deviceMap = new HashMap<>();
    deviceMap.put(dev1id, device1);
    deviceMap.put(dev2id, device2);
    deviceMap.put(dev3id, device3);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.groupRepository.findById(eq(group1id))).thenReturn(group1);
    doReturn(deviceMap).when(this.deviceRepository).findAll();

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg3 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call service
    CommonOperResponse operResp = this.groupService.deleteGroup(group1id);

    // Verify that appropriate Repository method was called
    verify(this.groupRepository, times(1)).delete(arg1.capture());

    // Verify that appropriate Repository method was NOT called
    verify(this.deviceRepository, times(0)).update(arg2.capture(), arg3.capture());

    // Assertions
    assertEquals(group1id, arg1.getValue(), "Argument must match.");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST deleteGroup (no entries in the device DB(Null))")
  void testDeleteGroupNoDeviceEntryNull() {
    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");
    String group1name = "Existing Name 1";
    String group1descr = "Existing Description 1";
    String group1comment = "Existing Comment 1";

    GroupRawInfo group1 = new GroupRawInfo();
    group1.setGroupId(group1id);
    group1.setName(group1name);
    group1.setDescription(group1descr);
    group1.setComment(group1comment);
    group1.setIsEnabled(false);

    Map<UUID, DeviceInfo> deviceMap = null;

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.groupRepository.findById(eq(group1id))).thenReturn(group1);
    doReturn(deviceMap).when(this.deviceRepository).findAll();

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg3 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call service
    CommonOperResponse operResp = this.groupService.deleteGroup(group1id);

    // Verify that appropriate Repository method was called
    verify(this.groupRepository, times(1)).delete(arg1.capture());

    // Verify that appropriate Repository method was NOT called
    verify(this.deviceRepository, times(0)).update(arg2.capture(), arg3.capture());

    // Assertions
    assertEquals(group1id, arg1.getValue(), "Argument must match.");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST deleteGroup (no entries in the device DB)")
  void testDeleteGroupNoDeviceEntry() {
    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");
    String group1name = "Existing Name 1";
    String group1descr = "Existing Description 1";
    String group1comment = "Existing Comment 1";

    GroupRawInfo group1 = new GroupRawInfo();
    group1.setGroupId(group1id);
    group1.setName(group1name);
    group1.setDescription(group1descr);
    group1.setComment(group1comment);
    group1.setIsEnabled(false);

    Map<UUID, DeviceInfo> deviceMap = Collections.emptyMap();

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.groupRepository.findById(eq(group1id))).thenReturn(group1);
    doReturn(deviceMap).when(this.deviceRepository).findAll();

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg3 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call service
    CommonOperResponse operResp = this.groupService.deleteGroup(group1id);

    // Verify that appropriate Repository method was called
    verify(this.groupRepository, times(1)).delete(arg1.capture());

    // Verify that appropriate Repository method was NOT called
    verify(this.deviceRepository, times(0)).update(arg2.capture(), arg3.capture());

    // Assertions
    assertEquals(group1id, arg1.getValue(), "Argument must match.");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST addDeviceToGroup")
  void testAddDeviceToGroup() {
    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");
    String group1name = "Existing Name 1";
    String group1descr = "Existing Description 1";
    String group1comment = "Existing Comment 1";

    GroupRawInfo group1 = new GroupRawInfo();
    group1.setGroupId(group1id);
    group1.setName(group1name);
    group1.setDescription(group1descr);
    group1.setComment(group1comment);
    group1.setIsEnabled(false);

    UUID dev1id = UUID.fromString("3f985f64-0314-ffff-eeee-2111af789af1");
    String dev1name = "Device 1";
    String dev1manuf = "Device 1 Manufacturer";
    DeviceInfo device1 = new DeviceInfo().deviceId(dev1id).name(dev1name).manufacturer(dev1manuf)
        .isRegistered(true).isGrouped(false);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.groupRepository.findById(eq(group1id))).thenReturn(group1);
    when(this.deviceRepository.findById(eq(dev1id))).thenReturn(device1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg2 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call service
    CommonOperResponse operResp = this.groupService.addDeviceToGroup(group1id, dev1id);

    // Verify that appropriate Repository method was called
    verify(this.deviceRepository, times(1)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertEquals(dev1id, arg1.getValue(), "Argument must match.");
    assertNotNull(arg2.getValue(), "Argument (DeviceInfo) is not null.");
    assertEquals(dev1id, arg2.getValue().getDeviceId(), "Argument's deviceId must match.");
    assertEquals(dev1name, arg2.getValue().getName(), "Argument's name must match.");
    assertEquals(dev1manuf, arg2.getValue().getManufacturer(),
        "Argument's manufacturer must match.");
    assertTrue(arg2.getValue().isIsGrouped(), "Argument's isGrouped must be true.");
    assertEquals(group1id, arg2.getValue().getGroupId(), "Argument's groupId must match.");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST addDeviceToGroup (groupId not found in DB)")
  void testAddDeviceToGroupGroupIdNotFound() {
    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");

    UUID dev1id = UUID.fromString("3f985f64-0314-ffff-eeee-2111af789af1");
    String dev1name = "Device 1";
    String dev1manuf = "Device 1 Manufacturer";
    DeviceInfo device1 = new DeviceInfo().deviceId(dev1id).name(dev1name).manufacturer(dev1manuf)
        .isRegistered(true).isGrouped(false);

    HttpStatus expectedStatus = HttpStatus.NOT_FOUND;

    // Mock(s)
    when(this.groupRepository.findById(eq(group1id))).thenReturn(null);
    when(this.deviceRepository.findById(eq(dev1id))).thenReturn(device1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg2 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call service
    CommonOperResponse operResp = this.groupService.addDeviceToGroup(group1id, dev1id);

    // Verify that appropriate Repository method was NOT called
    verify(this.deviceRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 404.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST addDeviceToGroup (deviceId not found in DB)")
  void testAddDeviceToGroupDeviceIdNotFound() {
    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");
    String group1name = "Existing Name 1";
    String group1descr = "Existing Description 1";
    String group1comment = "Existing Comment 1";

    GroupRawInfo group1 = new GroupRawInfo();
    group1.setGroupId(group1id);
    group1.setName(group1name);
    group1.setDescription(group1descr);
    group1.setComment(group1comment);
    group1.setIsEnabled(false);

    UUID dev1id = UUID.fromString("3f985f64-0314-ffff-eeee-2111af789af1");

    HttpStatus expectedStatus = HttpStatus.NOT_FOUND;

    // Mock(s)
    when(this.groupRepository.findById(eq(group1id))).thenReturn(group1);
    when(this.deviceRepository.findById(eq(dev1id))).thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg2 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call service
    CommonOperResponse operResp = this.groupService.addDeviceToGroup(group1id, dev1id);

    // Verify that appropriate Repository method was NOT called
    verify(this.deviceRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 404.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST addDeviceToGroup (device is not registered)")
  void testAddDeviceToGroupUnregisteredDevice() {
    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");
    String group1name = "Existing Name 1";
    String group1descr = "Existing Description 1";
    String group1comment = "Existing Comment 1";

    GroupRawInfo group1 = new GroupRawInfo();
    group1.setGroupId(group1id);
    group1.setName(group1name);
    group1.setDescription(group1descr);
    group1.setComment(group1comment);
    group1.setIsEnabled(false);

    UUID dev1id = UUID.fromString("3f985f64-0314-ffff-eeee-2111af789af1");
    String dev1name = "Device 1";
    String dev1manuf = "Device 1 Manufacturer";
    DeviceInfo device1 = new DeviceInfo().deviceId(dev1id).name(dev1name).manufacturer(dev1manuf)
        .isRegistered(false).isGrouped(false);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot add Device to Group";
    String expectedDetail = "Device is not registered.";

    // Mock(s)
    when(this.groupRepository.findById(eq(group1id))).thenReturn(group1);
    when(this.deviceRepository.findById(eq(dev1id))).thenReturn(device1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg2 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call service
    CommonOperResponse operResp = this.groupService.addDeviceToGroup(group1id, dev1id);

    // Verify that appropriate Repository method was NOT called
    verify(this.deviceRepository, times(0)).update(arg1.capture(), arg2.capture());

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
  @DisplayName("TEST addDeviceToGroup (device belongs to a different group)")
  void testAddDeviceToGroupDeviceGroupMismatch() {
    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");
    UUID otherGrpId = UUID.fromString("eeeeeeee-1111-1111-1111-111111111101");
    String group1name = "Existing Name 1";
    String group1descr = "Existing Description 1";
    String group1comment = "Existing Comment 1";

    GroupRawInfo group1 = new GroupRawInfo();
    group1.setGroupId(group1id);
    group1.setName(group1name);
    group1.setDescription(group1descr);
    group1.setComment(group1comment);
    group1.setIsEnabled(false);

    UUID dev1id = UUID.fromString("3f985f64-0314-ffff-eeee-2111af789af1");
    String dev1name = "Device 1";
    String dev1manuf = "Device 1 Manufacturer";
    DeviceInfo device1 = new DeviceInfo().deviceId(dev1id).name(dev1name).manufacturer(dev1manuf)
        .isRegistered(true).isGrouped(true).groupId(otherGrpId);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot add Device to Group";
    String expectedDetail =
        String.format("Device with deviceId = %s already belongs in a group.", dev1id);

    // Mock(s)
    when(this.groupRepository.findById(eq(group1id))).thenReturn(group1);
    when(this.deviceRepository.findById(eq(dev1id))).thenReturn(device1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg2 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call service
    CommonOperResponse operResp = this.groupService.addDeviceToGroup(group1id, dev1id);

    // Verify that appropriate Repository method was NOT called
    verify(this.deviceRepository, times(0)).update(arg1.capture(), arg2.capture());

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
  @DisplayName("TEST deleteDeviceFromGroup")
  void testDeleteDeviceFromGroup() {
    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");
    String group1name = "Existing Name 1";
    String group1descr = "Existing Description 1";
    String group1comment = "Existing Comment 1";

    GroupRawInfo group1 = new GroupRawInfo();
    group1.setGroupId(group1id);
    group1.setName(group1name);
    group1.setDescription(group1descr);
    group1.setComment(group1comment);
    group1.setIsEnabled(false);

    UUID dev1id = UUID.fromString("3f985f64-0314-ffff-eeee-2111af789af1");
    String dev1name = "Device 1";
    String dev1manuf = "Device 1 Manufacturer";
    DeviceInfo device1 = new DeviceInfo().deviceId(dev1id).name(dev1name).manufacturer(dev1manuf)
        .isRegistered(true).isGrouped(true).groupId(group1id);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.groupRepository.findById(eq(group1id))).thenReturn(group1);
    when(this.deviceRepository.findById(eq(dev1id))).thenReturn(device1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg2 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call service
    CommonOperResponse operResp = this.groupService.deleteDeviceFromGroup(group1id, dev1id);

    // Verify that appropriate Repository method was called
    verify(this.deviceRepository, times(1)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertEquals(dev1id, arg1.getValue(), "Argument must match.");
    assertNotNull(arg2.getValue(), "Argument (DeviceInfo) is not null.");
    assertEquals(dev1id, arg2.getValue().getDeviceId(), "Argument's deviceId must match.");
    assertEquals(dev1name, arg2.getValue().getName(), "Argument's name must match.");
    assertEquals(dev1manuf, arg2.getValue().getManufacturer(),
        "Argument's manufacturer must match.");
    assertFalse(arg2.getValue().isIsGrouped(), "Argument's isGrouped must be false.");
    assertNull(arg2.getValue().getGroupId(), "Argument's groupId must be null.");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST deleteDeviceFromGroup (groupId not found in DB)")
  void testDeleteDeviceFromGroupGroupIdNotFound() {
    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");

    UUID dev1id = UUID.fromString("3f985f64-0314-ffff-eeee-2111af789af1");
    String dev1name = "Device 1";
    String dev1manuf = "Device 1 Manufacturer";
    DeviceInfo device1 = new DeviceInfo().deviceId(dev1id).name(dev1name).manufacturer(dev1manuf)
        .isRegistered(true).isGrouped(false);

    HttpStatus expectedStatus = HttpStatus.NOT_FOUND;

    // Mock(s)
    when(this.groupRepository.findById(eq(group1id))).thenReturn(null);
    when(this.deviceRepository.findById(eq(dev1id))).thenReturn(device1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg2 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call service
    CommonOperResponse operResp = this.groupService.deleteDeviceFromGroup(group1id, dev1id);

    // Verify that appropriate Repository method was NOT called
    verify(this.deviceRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 404.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST deleteDeviceFromGroup (deviceId not found in DB)")
  void testDeleteDeviceFromGroupDeviceIdNotFound() {
    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");
    String group1name = "Existing Name 1";
    String group1descr = "Existing Description 1";
    String group1comment = "Existing Comment 1";

    GroupRawInfo group1 = new GroupRawInfo();
    group1.setGroupId(group1id);
    group1.setName(group1name);
    group1.setDescription(group1descr);
    group1.setComment(group1comment);
    group1.setIsEnabled(false);

    UUID dev1id = UUID.fromString("3f985f64-0314-ffff-eeee-2111af789af1");

    HttpStatus expectedStatus = HttpStatus.NOT_FOUND;

    // Mock(s)
    when(this.groupRepository.findById(eq(group1id))).thenReturn(group1);
    when(this.deviceRepository.findById(eq(dev1id))).thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg2 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call service
    CommonOperResponse operResp = this.groupService.deleteDeviceFromGroup(group1id, dev1id);

    // Verify that appropriate Repository method was NOT called
    verify(this.deviceRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 404.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST deleteDeviceFromGroup (device is not registered)")
  void testDeleteDeviceFromGroupUnregisteredDevice() {
    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");
    String group1name = "Existing Name 1";
    String group1descr = "Existing Description 1";
    String group1comment = "Existing Comment 1";

    GroupRawInfo group1 = new GroupRawInfo();
    group1.setGroupId(group1id);
    group1.setName(group1name);
    group1.setDescription(group1descr);
    group1.setComment(group1comment);
    group1.setIsEnabled(false);

    UUID dev1id = UUID.fromString("3f985f64-0314-ffff-eeee-2111af789af1");
    String dev1name = "Device 1";
    String dev1manuf = "Device 1 Manufacturer";
    DeviceInfo device1 = new DeviceInfo().deviceId(dev1id).name(dev1name).manufacturer(dev1manuf)
        .isRegistered(false).isGrouped(false);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot remove Device from Group";
    String expectedDetail = "Device is not registered.";

    // Mock(s)
    when(this.groupRepository.findById(eq(group1id))).thenReturn(group1);
    when(this.deviceRepository.findById(eq(dev1id))).thenReturn(device1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg2 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call service
    CommonOperResponse operResp = this.groupService.deleteDeviceFromGroup(group1id, dev1id);

    // Verify that appropriate Repository method was NOT called
    verify(this.deviceRepository, times(0)).update(arg1.capture(), arg2.capture());

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
  @DisplayName("TEST deleteDeviceFromGroup (device does not have a group)")
  void testDeleteDeviceFromGroupDeviceGroupNone() {
    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");
    String group1name = "Existing Name 1";
    String group1descr = "Existing Description 1";
    String group1comment = "Existing Comment 1";

    GroupRawInfo group1 = new GroupRawInfo();
    group1.setGroupId(group1id);
    group1.setName(group1name);
    group1.setDescription(group1descr);
    group1.setComment(group1comment);
    group1.setIsEnabled(false);

    UUID dev1id = UUID.fromString("3f985f64-0314-ffff-eeee-2111af789af1");
    String dev1name = "Device 1";
    String dev1manuf = "Device 1 Manufacturer";
    DeviceInfo device1 = new DeviceInfo().deviceId(dev1id).name(dev1name).manufacturer(dev1manuf)
        .isRegistered(true).isGrouped(false);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot remove Device from Group";
    String expectedDetail = "Device does not belong to a Group.";

    // Mock(s)
    when(this.groupRepository.findById(eq(group1id))).thenReturn(group1);
    when(this.deviceRepository.findById(eq(dev1id))).thenReturn(device1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg2 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call service
    CommonOperResponse operResp = this.groupService.deleteDeviceFromGroup(group1id, dev1id);

    // Verify that appropriate Repository method was NOT called
    verify(this.deviceRepository, times(0)).update(arg1.capture(), arg2.capture());

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
  @DisplayName("TEST deleteDeviceFromGroup (device has an invalid group (null))")
  void testDeleteDeviceFromGroupDeviceGroupNull() {
    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");
    String group1name = "Existing Name 1";
    String group1descr = "Existing Description 1";
    String group1comment = "Existing Comment 1";

    GroupRawInfo group1 = new GroupRawInfo();
    group1.setGroupId(group1id);
    group1.setName(group1name);
    group1.setDescription(group1descr);
    group1.setComment(group1comment);
    group1.setIsEnabled(false);

    UUID dev1id = UUID.fromString("3f985f64-0314-ffff-eeee-2111af789af1");
    String dev1name = "Device 1";
    String dev1manuf = "Device 1 Manufacturer";
    DeviceInfo device1 = new DeviceInfo().deviceId(dev1id).name(dev1name).manufacturer(dev1manuf)
        .isRegistered(true).isGrouped(true).groupId(null);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot remove Device from Group";
    String expectedDetail = "Device does not belong to a Group.";

    // Mock(s)
    when(this.groupRepository.findById(eq(group1id))).thenReturn(group1);
    when(this.deviceRepository.findById(eq(dev1id))).thenReturn(device1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg2 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call service
    CommonOperResponse operResp = this.groupService.deleteDeviceFromGroup(group1id, dev1id);

    // Verify that appropriate Repository method was NOT called
    verify(this.deviceRepository, times(0)).update(arg1.capture(), arg2.capture());

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
  @DisplayName("TEST deleteDeviceFromGroup (device belongs to a different group)")
  void testDeleteDeviceFromGroupDeviceGroupMismatch() {
    UUID group1id = UUID.fromString("05abcdef-1111-1111-1111-111111111101");
    UUID otherGrpId = UUID.fromString("eeeeeeee-1111-1111-1111-111111111101");
    String group1name = "Existing Name 1";
    String group1descr = "Existing Description 1";
    String group1comment = "Existing Comment 1";

    GroupRawInfo group1 = new GroupRawInfo();
    group1.setGroupId(group1id);
    group1.setName(group1name);
    group1.setDescription(group1descr);
    group1.setComment(group1comment);
    group1.setIsEnabled(false);

    UUID dev1id = UUID.fromString("3f985f64-0314-ffff-eeee-2111af789af1");
    String dev1name = "Device 1";
    String dev1manuf = "Device 1 Manufacturer";
    DeviceInfo device1 = new DeviceInfo().deviceId(dev1id).name(dev1name).manufacturer(dev1manuf)
        .isRegistered(true).isGrouped(true).groupId(otherGrpId);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot remove Device from Group";
    String expectedDetail = "Device does not belong to the specified Group.";

    // Mock(s)
    when(this.groupRepository.findById(eq(group1id))).thenReturn(group1);
    when(this.deviceRepository.findById(eq(dev1id))).thenReturn(device1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg2 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call service
    CommonOperResponse operResp = this.groupService.deleteDeviceFromGroup(group1id, dev1id);

    // Verify that appropriate Repository method was NOT called
    verify(this.deviceRepository, times(0)).update(arg1.capture(), arg2.capture());

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
