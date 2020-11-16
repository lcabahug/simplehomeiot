package com.codingcuriosity.project.simplehomeiot.devices.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import com.codingcuriosity.project.simplehomeiot.controls.model.CommonControlType;
import com.codingcuriosity.project.simplehomeiot.devices.model.CommonDeviceState;
import com.codingcuriosity.project.simplehomeiot.devices.model.DeviceInfo;
import com.codingcuriosity.project.simplehomeiot.devices.model.DeviceInfoAvailableControls;
import com.codingcuriosity.project.simplehomeiot.devices.model.DeviceModification;
import com.codingcuriosity.project.simplehomeiot.devices.model.DeviceRegistration;
import com.codingcuriosity.project.simplehomeiot.devices.model.DeviceSwitchOnOff;
import com.codingcuriosity.project.simplehomeiot.devices.repository.DeviceRepository;
import com.codingcuriosity.project.simplehomeiot.logs.repository.ControllerLogRepository;
import com.codingcuriosity.project.simplehomeiot.zones.model.ZoneInfo;
import com.codingcuriosity.project.simplehomeiot.zones.repository.ZoneRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
public class DeviceServiceImplTest {

  @Autowired
  DeviceService deviceService;

  @MockBean
  DeviceRepository deviceRepository;

  @MockBean
  ZoneRepository zoneRepository;

  @MockBean
  ControllerLogRepository controllerLogRepository;

  @Test
  @DisplayName("TEST getLogRepo")
  void testGetLogRepo() {

    // Call Service
    ControllerLogRepository logRepository = this.deviceService.getLogRepo();

    // Assertions
    assertEquals(this.controllerLogRepository, logRepository, "LogRepository must be identical.");
  }

  @Test
  @DisplayName("TEST getDevices (all parameters = null)")
  void testGetDevicesNoParam() {
    Integer skip = null;
    Integer limit = null;
    Boolean includeRegistered = null;
    Boolean includeUnregistered = null;

    UUID device1id = UUID.fromString("3f985f64-0314-0361-032c-2b963f789af1");
    UUID device2id = UUID.fromString("4b785f64-5147-4013-b3fc-48963f50ace2");
    UUID device3id = UUID.fromString("1a085f64-842b-3102-9990-6c963f1b3bd3");
    String device1name = "Shark - Test";
    String device2name = "Samsung - Test";
    String device3name = "Apple -- Test";
    String device1manuf = "Shark Inc.";
    String device2manuf = "Samsung Inc.";
    String device3manuf = "The Apple Company";
    String device1comment = "An Old TV in the Living Room.";
    String device2comment = "Mini TV in the Bedroom";
    String device3comment = "A test device.";
    String device1address = "2607:f0d0:1002:31::1";
    String device2address = "2607:f0d0:1002:42::2";
    String device3address = "2607:f0d0:1002:53::3";
    CommonDeviceState device1state = CommonDeviceState.OFF;
    CommonDeviceState device2state = CommonDeviceState.ON;
    CommonDeviceState device3state = CommonDeviceState.OFF;
    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 1") //
        .description("Desc 1 -1") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(true));
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 2") //
        .description("Desc 1 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("3.337") //
        .isManaged(false));
    List<DeviceInfoAvailableControls> device2controls = new ArrayList<>();
    device2controls.add(new DeviceInfoAvailableControls() //
        .name("Dev2 Ctrl 1") //
        .description("Desc 2 -1") //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device2controls.add(new DeviceInfoAvailableControls() //
        .name("Dev2 Ctrl 2") //
        .description("Desc 2 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("1.029") //
        .isManaged(true));
    List<DeviceInfoAvailableControls> device3controls = new ArrayList<>();
    device3controls.add(new DeviceInfoAvailableControls() //
        .name("Dev3 Ctrl 1") //
        .description("Desc 3 -1") //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device3controls.add(new DeviceInfoAvailableControls() //
        .name("Dev3 Ctrl 2") //
        .description("Desc 3 -2") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(false));
    Boolean device1isReg = false;
    Boolean device2isReg = true;
    Boolean device3isReg = false;
    Boolean device1isGrp = true;
    Boolean device2isGrp = false;
    Boolean device3isGrp = true;
    UUID device1grpId = UUID.fromString("3f985f64-9999-0361-032c-2b96dd787d51");
    UUID device2grpId = null;
    UUID device3grpId = UUID.fromString("1a085f64-aaaa-3102-9990-6c963f000b03");
    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(device1id) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);
    DeviceInfo device2 = new DeviceInfo() //
        .deviceId(device2id) //
        .name(device2name) //
        .manufacturer(device2manuf) //
        .comment(device2comment) //
        .address(device2address) //
        .state(device2state) //
        .availableControls(device2controls) //
        .isRegistered(device2isReg) //
        .isGrouped(device2isGrp) //
        .groupId(device2grpId);
    DeviceInfo device3 = new DeviceInfo() //
        .deviceId(device3id) //
        .name(device3name) //
        .manufacturer(device3manuf) //
        .comment(device3comment) //
        .address(device3address) //
        .state(device3state) //
        .availableControls(device3controls) //
        .isRegistered(device3isReg) //
        .isGrouped(device3isGrp) //
        .groupId(device3grpId);

    Map<UUID, DeviceInfo> deviceMap = new HashMap<>();
    deviceMap.put(device1id, device1);
    deviceMap.put(device2id, device2);
    deviceMap.put(device3id, device3);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(deviceMap).when(this.deviceRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceInfo> retListResp =
        this.deviceService.getDevices(skip, limit, includeRegistered, includeUnregistered);

    // Verify that appropriate Repository method was called
    verify(this.deviceRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be sorted by device name ascending.",
        retListResp.getObjList(), contains(device3, device2, device1));
    assertEquals(device3id, retListResp.getObjList().get(0).getDeviceId(),
        "1st entry's UUID must match device 3's");
    assertEquals(device3name, retListResp.getObjList().get(0).getName(),
        "1st entry's name must match device 3's");
    assertEquals(device3manuf, retListResp.getObjList().get(0).getManufacturer(),
        "1st entry's manufacturer must match device 3's");
    assertEquals(device3comment, retListResp.getObjList().get(0).getComment(),
        "1st entry's comment must match device 3's");
    assertEquals(device3address, retListResp.getObjList().get(0).getAddress(),
        "1st entry's address must match device 3's");
    assertEquals(device3state, retListResp.getObjList().get(0).getState(),
        "1st entry's state must match device 3's");
    assertEquals(device3controls, retListResp.getObjList().get(0).getAvailableControls(),
        "1st entry's available controls must match device 3's");
    assertEquals(device3isReg, retListResp.getObjList().get(0).isIsRegistered(),
        "1st entry's isRegistered must match device 3's");
    assertEquals(device3isGrp, retListResp.getObjList().get(0).isIsGrouped(),
        "1st entry's isGrouped must match device 3's");
    assertEquals(device3grpId, retListResp.getObjList().get(0).getGroupId(),
        "1st entry's groupId must match device 3's");
    assertEquals(device2id, retListResp.getObjList().get(1).getDeviceId(),
        "2nd entry's UUID must match device 2's");
    assertEquals(device2name, retListResp.getObjList().get(1).getName(),
        "2nd entry's name must match device 2's");
    assertEquals(device2manuf, retListResp.getObjList().get(1).getManufacturer(),
        "2nd entry's manufacturer must match device 2's");
    assertEquals(device2comment, retListResp.getObjList().get(1).getComment(),
        "2nd entry's comment must match device 2's");
    assertEquals(device2address, retListResp.getObjList().get(1).getAddress(),
        "2nd entry's address must match device 2's");
    assertEquals(device2state, retListResp.getObjList().get(1).getState(),
        "2nd entry's state must match device 2's");
    assertEquals(device2controls, retListResp.getObjList().get(1).getAvailableControls(),
        "2nd entry's available controls must match device 2's");
    assertEquals(device2isReg, retListResp.getObjList().get(1).isIsRegistered(),
        "2nd entry's isRegistered must match device 2's");
    assertEquals(device2isGrp, retListResp.getObjList().get(1).isIsGrouped(),
        "2nd entry's isGrouped must match device 2's");
    assertNull(retListResp.getObjList().get(1).getGroupId(),
        "2nd entry's groupId must match device 2's (which is null)");
    assertEquals(device1id, retListResp.getObjList().get(2).getDeviceId(),
        "3rd entry's UUID must match device 1's");
    assertEquals(device1name, retListResp.getObjList().get(2).getName(),
        "3rd entry's name must match device 1's");
    assertEquals(device1manuf, retListResp.getObjList().get(2).getManufacturer(),
        "3rd entry's manufacturer must match device 1's");
    assertEquals(device1comment, retListResp.getObjList().get(2).getComment(),
        "3rd entry's comment must match device 1's");
    assertEquals(device1address, retListResp.getObjList().get(2).getAddress(),
        "3rd entry's address must match device 1's");
    assertEquals(device1state, retListResp.getObjList().get(2).getState(),
        "3rd entry's state must match device 1's");
    assertEquals(device1controls, retListResp.getObjList().get(2).getAvailableControls(),
        "3rd entry's available controls must match device 1's");
    assertEquals(device1isReg, retListResp.getObjList().get(2).isIsRegistered(),
        "3rd entry's isRegistered must match device 1's");
    assertEquals(device1isGrp, retListResp.getObjList().get(2).isIsGrouped(),
        "3rd entry's isGrouped must match device 1's");
    assertEquals(device1grpId, retListResp.getObjList().get(2).getGroupId(),
        "3rd entry's groupId must match device 1's");
  }

  @Test
  @DisplayName("TEST getDevices (skip = 0, limit = 0)")
  void testGetDevicesSkipMinLimitMin() {
    Integer skip = 0;
    Integer limit = 0;
    Boolean includeRegistered = null;
    Boolean includeUnregistered = null;

    UUID device1id = UUID.fromString("3f985f64-0314-0361-032c-2b963f789af1");
    UUID device2id = UUID.fromString("4b785f64-5147-4013-b3fc-48963f50ace2");
    UUID device3id = UUID.fromString("1a085f64-842b-3102-9990-6c963f1b3bd3");
    String device1name = "Shark - Test";
    String device2name = "Samsung - Test";
    String device3name = "Apple -- Test";
    String device1manuf = "Shark Inc.";
    String device2manuf = "Samsung Inc.";
    String device3manuf = "The Apple Company";
    String device1comment = "An Old TV in the Living Room.";
    String device2comment = "Mini TV in the Bedroom";
    String device3comment = "A test device.";
    String device1address = "2607:f0d0:1002:31::1";
    String device2address = "2607:f0d0:1002:42::2";
    String device3address = "2607:f0d0:1002:53::3";
    CommonDeviceState device1state = CommonDeviceState.OFF;
    CommonDeviceState device2state = CommonDeviceState.ON;
    CommonDeviceState device3state = CommonDeviceState.OFF;
    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 1") //
        .description("Desc 1 -1") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(true));
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 2") //
        .description("Desc 1 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("3.337") //
        .isManaged(false));
    List<DeviceInfoAvailableControls> device2controls = new ArrayList<>();
    device2controls.add(new DeviceInfoAvailableControls() //
        .name("Dev2 Ctrl 1") //
        .description("Desc 2 -1") //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device2controls.add(new DeviceInfoAvailableControls() //
        .name("Dev2 Ctrl 2") //
        .description("Desc 2 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("1.029") //
        .isManaged(true));
    List<DeviceInfoAvailableControls> device3controls = new ArrayList<>();
    device3controls.add(new DeviceInfoAvailableControls() //
        .name("Dev3 Ctrl 1") //
        .description("Desc 3 -1") //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device3controls.add(new DeviceInfoAvailableControls() //
        .name("Dev3 Ctrl 2") //
        .description("Desc 3 -2") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(false));
    Boolean device1isReg = false;
    Boolean device2isReg = true;
    Boolean device3isReg = false;
    Boolean device1isGrp = true;
    Boolean device2isGrp = false;
    Boolean device3isGrp = true;
    UUID device1grpId = UUID.fromString("3f985f64-9999-0361-032c-2b96dd787d51");
    UUID device2grpId = null;
    UUID device3grpId = UUID.fromString("1a085f64-aaaa-3102-9990-6c963f000b03");
    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(device1id) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);
    DeviceInfo device2 = new DeviceInfo() //
        .deviceId(device2id) //
        .name(device2name) //
        .manufacturer(device2manuf) //
        .comment(device2comment) //
        .address(device2address) //
        .state(device2state) //
        .availableControls(device2controls) //
        .isRegistered(device2isReg) //
        .isGrouped(device2isGrp) //
        .groupId(device2grpId);
    DeviceInfo device3 = new DeviceInfo() //
        .deviceId(device3id) //
        .name(device3name) //
        .manufacturer(device3manuf) //
        .comment(device3comment) //
        .address(device3address) //
        .state(device3state) //
        .availableControls(device3controls) //
        .isRegistered(device3isReg) //
        .isGrouped(device3isGrp) //
        .groupId(device3grpId);

    Map<UUID, DeviceInfo> deviceMap = new HashMap<>();
    deviceMap.put(device1id, device1);
    deviceMap.put(device2id, device2);
    deviceMap.put(device3id, device3);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(deviceMap).when(this.deviceRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceInfo> retListResp =
        this.deviceService.getDevices(skip, limit, includeRegistered, includeUnregistered);

    // Verify that appropriate Repository method was called
    verify(this.deviceRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getDevices (skip = 0, limit = entry max)")
  void testGetDevicesSkipMinLimitMax() {
    Integer skip = 0;
    Integer limit = null; // Will be changed later
    Boolean includeRegistered = null;
    Boolean includeUnregistered = null;

    UUID device1id = UUID.fromString("3f985f64-0314-0361-032c-2b963f789af1");
    UUID device2id = UUID.fromString("4b785f64-5147-4013-b3fc-48963f50ace2");
    UUID device3id = UUID.fromString("1a085f64-842b-3102-9990-6c963f1b3bd3");
    String device1name = "Shark - Test";
    String device2name = "Samsung - Test";
    String device3name = "Apple -- Test";
    String device1manuf = "Shark Inc.";
    String device2manuf = "Samsung Inc.";
    String device3manuf = "The Apple Company";
    String device1comment = "An Old TV in the Living Room.";
    String device2comment = "Mini TV in the Bedroom";
    String device3comment = "A test device.";
    String device1address = "2607:f0d0:1002:31::1";
    String device2address = "2607:f0d0:1002:42::2";
    String device3address = "2607:f0d0:1002:53::3";
    CommonDeviceState device1state = CommonDeviceState.OFF;
    CommonDeviceState device2state = CommonDeviceState.ON;
    CommonDeviceState device3state = CommonDeviceState.OFF;
    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 1") //
        .description("Desc 1 -1") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(true));
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 2") //
        .description("Desc 1 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("3.337") //
        .isManaged(false));
    List<DeviceInfoAvailableControls> device2controls = new ArrayList<>();
    device2controls.add(new DeviceInfoAvailableControls() //
        .name("Dev2 Ctrl 1") //
        .description("Desc 2 -1") //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device2controls.add(new DeviceInfoAvailableControls() //
        .name("Dev2 Ctrl 2") //
        .description("Desc 2 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("1.029") //
        .isManaged(true));
    List<DeviceInfoAvailableControls> device3controls = new ArrayList<>();
    device3controls.add(new DeviceInfoAvailableControls() //
        .name("Dev3 Ctrl 1") //
        .description("Desc 3 -1") //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device3controls.add(new DeviceInfoAvailableControls() //
        .name("Dev3 Ctrl 2") //
        .description("Desc 3 -2") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(false));
    Boolean device1isReg = false;
    Boolean device2isReg = true;
    Boolean device3isReg = false;
    Boolean device1isGrp = true;
    Boolean device2isGrp = false;
    Boolean device3isGrp = true;
    UUID device1grpId = UUID.fromString("3f985f64-9999-0361-032c-2b96dd787d51");
    UUID device2grpId = null;
    UUID device3grpId = UUID.fromString("1a085f64-aaaa-3102-9990-6c963f000b03");
    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(device1id) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);
    DeviceInfo device2 = new DeviceInfo() //
        .deviceId(device2id) //
        .name(device2name) //
        .manufacturer(device2manuf) //
        .comment(device2comment) //
        .address(device2address) //
        .state(device2state) //
        .availableControls(device2controls) //
        .isRegistered(device2isReg) //
        .isGrouped(device2isGrp) //
        .groupId(device2grpId);
    DeviceInfo device3 = new DeviceInfo() //
        .deviceId(device3id) //
        .name(device3name) //
        .manufacturer(device3manuf) //
        .comment(device3comment) //
        .address(device3address) //
        .state(device3state) //
        .availableControls(device3controls) //
        .isRegistered(device3isReg) //
        .isGrouped(device3isGrp) //
        .groupId(device3grpId);

    Map<UUID, DeviceInfo> deviceMap = new HashMap<>();
    deviceMap.put(device1id, device1);
    deviceMap.put(device2id, device2);
    deviceMap.put(device3id, device3);

    HttpStatus expectedStatus = HttpStatus.OK;

    limit = deviceMap.size();

    // Mock(s)
    doReturn(deviceMap).when(this.deviceRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceInfo> retListResp =
        this.deviceService.getDevices(skip, limit, includeRegistered, includeUnregistered);

    // Verify that appropriate Repository method was called
    verify(this.deviceRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be sorted by device name ascending.",
        retListResp.getObjList(), contains(device3, device2, device1));
  }

  @Test
  @DisplayName("TEST getDevices (skip = entry max)")
  void testGetDevicesSkipMax() {
    Integer skip = null; // Will be changed later
    Integer limit = null;
    Boolean includeRegistered = null;
    Boolean includeUnregistered = null;

    UUID device1id = UUID.fromString("3f985f64-0314-0361-032c-2b963f789af1");
    UUID device2id = UUID.fromString("4b785f64-5147-4013-b3fc-48963f50ace2");
    UUID device3id = UUID.fromString("1a085f64-842b-3102-9990-6c963f1b3bd3");
    String device1name = "Shark - Test";
    String device2name = "Samsung - Test";
    String device3name = "Apple -- Test";
    String device1manuf = "Shark Inc.";
    String device2manuf = "Samsung Inc.";
    String device3manuf = "The Apple Company";
    String device1comment = "An Old TV in the Living Room.";
    String device2comment = "Mini TV in the Bedroom";
    String device3comment = "A test device.";
    String device1address = "2607:f0d0:1002:31::1";
    String device2address = "2607:f0d0:1002:42::2";
    String device3address = "2607:f0d0:1002:53::3";
    CommonDeviceState device1state = CommonDeviceState.OFF;
    CommonDeviceState device2state = CommonDeviceState.ON;
    CommonDeviceState device3state = CommonDeviceState.OFF;
    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 1") //
        .description("Desc 1 -1") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(true));
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 2") //
        .description("Desc 1 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("3.337") //
        .isManaged(false));
    List<DeviceInfoAvailableControls> device2controls = new ArrayList<>();
    device2controls.add(new DeviceInfoAvailableControls() //
        .name("Dev2 Ctrl 1") //
        .description("Desc 2 -1") //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device2controls.add(new DeviceInfoAvailableControls() //
        .name("Dev2 Ctrl 2") //
        .description("Desc 2 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("1.029") //
        .isManaged(true));
    List<DeviceInfoAvailableControls> device3controls = new ArrayList<>();
    device3controls.add(new DeviceInfoAvailableControls() //
        .name("Dev3 Ctrl 1") //
        .description("Desc 3 -1") //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device3controls.add(new DeviceInfoAvailableControls() //
        .name("Dev3 Ctrl 2") //
        .description("Desc 3 -2") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(false));
    Boolean device1isReg = false;
    Boolean device2isReg = true;
    Boolean device3isReg = false;
    Boolean device1isGrp = true;
    Boolean device2isGrp = false;
    Boolean device3isGrp = true;
    UUID device1grpId = UUID.fromString("3f985f64-9999-0361-032c-2b96dd787d51");
    UUID device2grpId = null;
    UUID device3grpId = UUID.fromString("1a085f64-aaaa-3102-9990-6c963f000b03");
    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(device1id) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);
    DeviceInfo device2 = new DeviceInfo() //
        .deviceId(device2id) //
        .name(device2name) //
        .manufacturer(device2manuf) //
        .comment(device2comment) //
        .address(device2address) //
        .state(device2state) //
        .availableControls(device2controls) //
        .isRegistered(device2isReg) //
        .isGrouped(device2isGrp) //
        .groupId(device2grpId);
    DeviceInfo device3 = new DeviceInfo() //
        .deviceId(device3id) //
        .name(device3name) //
        .manufacturer(device3manuf) //
        .comment(device3comment) //
        .address(device3address) //
        .state(device3state) //
        .availableControls(device3controls) //
        .isRegistered(device3isReg) //
        .isGrouped(device3isGrp) //
        .groupId(device3grpId);

    Map<UUID, DeviceInfo> deviceMap = new HashMap<>();
    deviceMap.put(device1id, device1);
    deviceMap.put(device2id, device2);
    deviceMap.put(device3id, device3);

    HttpStatus expectedStatus = HttpStatus.OK;

    skip = deviceMap.size();

    // Mock(s)
    doReturn(deviceMap).when(this.deviceRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceInfo> retListResp =
        this.deviceService.getDevices(skip, limit, includeRegistered, includeUnregistered);

    // Verify that appropriate Repository method was called
    verify(this.deviceRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getDevices (skip = -1)")
  void testGetDevicesSkipMinMinus1() {
    Integer skip = -1;
    Integer limit = null;
    Boolean includeRegistered = null;
    Boolean includeUnregistered = null;

    UUID device1id = UUID.fromString("3f985f64-0314-0361-032c-2b963f789af1");
    UUID device2id = UUID.fromString("4b785f64-5147-4013-b3fc-48963f50ace2");
    UUID device3id = UUID.fromString("1a085f64-842b-3102-9990-6c963f1b3bd3");
    String device1name = "Shark - Test";
    String device2name = "Samsung - Test";
    String device3name = "Apple -- Test";
    String device1manuf = "Shark Inc.";
    String device2manuf = "Samsung Inc.";
    String device3manuf = "The Apple Company";
    String device1comment = "An Old TV in the Living Room.";
    String device2comment = "Mini TV in the Bedroom";
    String device3comment = "A test device.";
    String device1address = "2607:f0d0:1002:31::1";
    String device2address = "2607:f0d0:1002:42::2";
    String device3address = "2607:f0d0:1002:53::3";
    CommonDeviceState device1state = CommonDeviceState.OFF;
    CommonDeviceState device2state = CommonDeviceState.ON;
    CommonDeviceState device3state = CommonDeviceState.OFF;
    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 1") //
        .description("Desc 1 -1") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(true));
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 2") //
        .description("Desc 1 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("3.337") //
        .isManaged(false));
    List<DeviceInfoAvailableControls> device2controls = new ArrayList<>();
    device2controls.add(new DeviceInfoAvailableControls() //
        .name("Dev2 Ctrl 1") //
        .description("Desc 2 -1") //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device2controls.add(new DeviceInfoAvailableControls() //
        .name("Dev2 Ctrl 2") //
        .description("Desc 2 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("1.029") //
        .isManaged(true));
    List<DeviceInfoAvailableControls> device3controls = new ArrayList<>();
    device3controls.add(new DeviceInfoAvailableControls() //
        .name("Dev3 Ctrl 1") //
        .description("Desc 3 -1") //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device3controls.add(new DeviceInfoAvailableControls() //
        .name("Dev3 Ctrl 2") //
        .description("Desc 3 -2") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(false));
    Boolean device1isReg = false;
    Boolean device2isReg = true;
    Boolean device3isReg = false;
    Boolean device1isGrp = true;
    Boolean device2isGrp = false;
    Boolean device3isGrp = true;
    UUID device1grpId = UUID.fromString("3f985f64-9999-0361-032c-2b96dd787d51");
    UUID device2grpId = null;
    UUID device3grpId = UUID.fromString("1a085f64-aaaa-3102-9990-6c963f000b03");
    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(device1id) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);
    DeviceInfo device2 = new DeviceInfo() //
        .deviceId(device2id) //
        .name(device2name) //
        .manufacturer(device2manuf) //
        .comment(device2comment) //
        .address(device2address) //
        .state(device2state) //
        .availableControls(device2controls) //
        .isRegistered(device2isReg) //
        .isGrouped(device2isGrp) //
        .groupId(device2grpId);
    DeviceInfo device3 = new DeviceInfo() //
        .deviceId(device3id) //
        .name(device3name) //
        .manufacturer(device3manuf) //
        .comment(device3comment) //
        .address(device3address) //
        .state(device3state) //
        .availableControls(device3controls) //
        .isRegistered(device3isReg) //
        .isGrouped(device3isGrp) //
        .groupId(device3grpId);

    Map<UUID, DeviceInfo> deviceMap = new HashMap<>();
    deviceMap.put(device1id, device1);
    deviceMap.put(device2id, device2);
    deviceMap.put(device3id, device3);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    doReturn(deviceMap).when(this.deviceRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceInfo> retListResp =
        this.deviceService.getDevices(skip, limit, includeRegistered, includeUnregistered);

    // Verify that appropriate Repository method was called
    verify(this.deviceRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 400.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getDevices (limit = -1)")
  void testGetDevicesLimitMinMinus1() {
    Integer skip = null;
    Integer limit = -1;
    Boolean includeRegistered = null;
    Boolean includeUnregistered = null;

    UUID device1id = UUID.fromString("3f985f64-0314-0361-032c-2b963f789af1");
    UUID device2id = UUID.fromString("4b785f64-5147-4013-b3fc-48963f50ace2");
    UUID device3id = UUID.fromString("1a085f64-842b-3102-9990-6c963f1b3bd3");
    String device1name = "Shark - Test";
    String device2name = "Samsung - Test";
    String device3name = "Apple -- Test";
    String device1manuf = "Shark Inc.";
    String device2manuf = "Samsung Inc.";
    String device3manuf = "The Apple Company";
    String device1comment = "An Old TV in the Living Room.";
    String device2comment = "Mini TV in the Bedroom";
    String device3comment = "A test device.";
    String device1address = "2607:f0d0:1002:31::1";
    String device2address = "2607:f0d0:1002:42::2";
    String device3address = "2607:f0d0:1002:53::3";
    CommonDeviceState device1state = CommonDeviceState.OFF;
    CommonDeviceState device2state = CommonDeviceState.ON;
    CommonDeviceState device3state = CommonDeviceState.OFF;
    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 1") //
        .description("Desc 1 -1") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(true));
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 2") //
        .description("Desc 1 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("3.337") //
        .isManaged(false));
    List<DeviceInfoAvailableControls> device2controls = new ArrayList<>();
    device2controls.add(new DeviceInfoAvailableControls() //
        .name("Dev2 Ctrl 1") //
        .description("Desc 2 -1") //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device2controls.add(new DeviceInfoAvailableControls() //
        .name("Dev2 Ctrl 2") //
        .description("Desc 2 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("1.029") //
        .isManaged(true));
    List<DeviceInfoAvailableControls> device3controls = new ArrayList<>();
    device3controls.add(new DeviceInfoAvailableControls() //
        .name("Dev3 Ctrl 1") //
        .description("Desc 3 -1") //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device3controls.add(new DeviceInfoAvailableControls() //
        .name("Dev3 Ctrl 2") //
        .description("Desc 3 -2") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(false));
    Boolean device1isReg = false;
    Boolean device2isReg = true;
    Boolean device3isReg = false;
    Boolean device1isGrp = true;
    Boolean device2isGrp = false;
    Boolean device3isGrp = true;
    UUID device1grpId = UUID.fromString("3f985f64-9999-0361-032c-2b96dd787d51");
    UUID device2grpId = null;
    UUID device3grpId = UUID.fromString("1a085f64-aaaa-3102-9990-6c963f000b03");
    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(device1id) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);
    DeviceInfo device2 = new DeviceInfo() //
        .deviceId(device2id) //
        .name(device2name) //
        .manufacturer(device2manuf) //
        .comment(device2comment) //
        .address(device2address) //
        .state(device2state) //
        .availableControls(device2controls) //
        .isRegistered(device2isReg) //
        .isGrouped(device2isGrp) //
        .groupId(device2grpId);
    DeviceInfo device3 = new DeviceInfo() //
        .deviceId(device3id) //
        .name(device3name) //
        .manufacturer(device3manuf) //
        .comment(device3comment) //
        .address(device3address) //
        .state(device3state) //
        .availableControls(device3controls) //
        .isRegistered(device3isReg) //
        .isGrouped(device3isGrp) //
        .groupId(device3grpId);

    Map<UUID, DeviceInfo> deviceMap = new HashMap<>();
    deviceMap.put(device1id, device1);
    deviceMap.put(device2id, device2);
    deviceMap.put(device3id, device3);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    doReturn(deviceMap).when(this.deviceRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceInfo> retListResp =
        this.deviceService.getDevices(skip, limit, includeRegistered, includeUnregistered);

    // Verify that appropriate Repository method was called
    verify(this.deviceRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 400.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getDevices (skip = entry max + 1)")
  void testGetDevicesSkipMaxPlus1() {
    Integer skip = null; // Will be changed later
    Integer limit = null;
    Boolean includeRegistered = null;
    Boolean includeUnregistered = null;

    UUID device1id = UUID.fromString("3f985f64-0314-0361-032c-2b963f789af1");
    UUID device2id = UUID.fromString("4b785f64-5147-4013-b3fc-48963f50ace2");
    UUID device3id = UUID.fromString("1a085f64-842b-3102-9990-6c963f1b3bd3");
    String device1name = "Shark - Test";
    String device2name = "Samsung - Test";
    String device3name = "Apple -- Test";
    String device1manuf = "Shark Inc.";
    String device2manuf = "Samsung Inc.";
    String device3manuf = "The Apple Company";
    String device1comment = "An Old TV in the Living Room.";
    String device2comment = "Mini TV in the Bedroom";
    String device3comment = "A test device.";
    String device1address = "2607:f0d0:1002:31::1";
    String device2address = "2607:f0d0:1002:42::2";
    String device3address = "2607:f0d0:1002:53::3";
    CommonDeviceState device1state = CommonDeviceState.OFF;
    CommonDeviceState device2state = CommonDeviceState.ON;
    CommonDeviceState device3state = CommonDeviceState.OFF;
    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 1") //
        .description("Desc 1 -1") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(true));
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 2") //
        .description("Desc 1 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("3.337") //
        .isManaged(false));
    List<DeviceInfoAvailableControls> device2controls = new ArrayList<>();
    device2controls.add(new DeviceInfoAvailableControls() //
        .name("Dev2 Ctrl 1") //
        .description("Desc 2 -1") //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device2controls.add(new DeviceInfoAvailableControls() //
        .name("Dev2 Ctrl 2") //
        .description("Desc 2 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("1.029") //
        .isManaged(true));
    List<DeviceInfoAvailableControls> device3controls = new ArrayList<>();
    device3controls.add(new DeviceInfoAvailableControls() //
        .name("Dev3 Ctrl 1") //
        .description("Desc 3 -1") //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device3controls.add(new DeviceInfoAvailableControls() //
        .name("Dev3 Ctrl 2") //
        .description("Desc 3 -2") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(false));
    Boolean device1isReg = false;
    Boolean device2isReg = true;
    Boolean device3isReg = false;
    Boolean device1isGrp = true;
    Boolean device2isGrp = false;
    Boolean device3isGrp = true;
    UUID device1grpId = UUID.fromString("3f985f64-9999-0361-032c-2b96dd787d51");
    UUID device2grpId = null;
    UUID device3grpId = UUID.fromString("1a085f64-aaaa-3102-9990-6c963f000b03");
    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(device1id) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);
    DeviceInfo device2 = new DeviceInfo() //
        .deviceId(device2id) //
        .name(device2name) //
        .manufacturer(device2manuf) //
        .comment(device2comment) //
        .address(device2address) //
        .state(device2state) //
        .availableControls(device2controls) //
        .isRegistered(device2isReg) //
        .isGrouped(device2isGrp) //
        .groupId(device2grpId);
    DeviceInfo device3 = new DeviceInfo() //
        .deviceId(device3id) //
        .name(device3name) //
        .manufacturer(device3manuf) //
        .comment(device3comment) //
        .address(device3address) //
        .state(device3state) //
        .availableControls(device3controls) //
        .isRegistered(device3isReg) //
        .isGrouped(device3isGrp) //
        .groupId(device3grpId);

    Map<UUID, DeviceInfo> deviceMap = new HashMap<>();
    deviceMap.put(device1id, device1);
    deviceMap.put(device2id, device2);
    deviceMap.put(device3id, device3);

    HttpStatus expectedStatus = HttpStatus.OK;

    skip = deviceMap.size() + 1;

    // Mock(s)
    doReturn(deviceMap).when(this.deviceRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceInfo> retListResp =
        this.deviceService.getDevices(skip, limit, includeRegistered, includeUnregistered);

    // Verify that appropriate Repository method was called
    verify(this.deviceRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getDevices (limit = entry max + 1)")
  void testGetDevicesLimitMaxPlus1() {
    Integer skip = null;
    Integer limit = null; // Will be changed later
    Boolean includeRegistered = null;
    Boolean includeUnregistered = null;

    UUID device1id = UUID.fromString("3f985f64-0314-0361-032c-2b963f789af1");
    UUID device2id = UUID.fromString("4b785f64-5147-4013-b3fc-48963f50ace2");
    UUID device3id = UUID.fromString("1a085f64-842b-3102-9990-6c963f1b3bd3");
    String device1name = "Shark - Test";
    String device2name = "Samsung - Test";
    String device3name = "Apple -- Test";
    String device1manuf = "Shark Inc.";
    String device2manuf = "Samsung Inc.";
    String device3manuf = "The Apple Company";
    String device1comment = "An Old TV in the Living Room.";
    String device2comment = "Mini TV in the Bedroom";
    String device3comment = "A test device.";
    String device1address = "2607:f0d0:1002:31::1";
    String device2address = "2607:f0d0:1002:42::2";
    String device3address = "2607:f0d0:1002:53::3";
    CommonDeviceState device1state = CommonDeviceState.OFF;
    CommonDeviceState device2state = CommonDeviceState.ON;
    CommonDeviceState device3state = CommonDeviceState.OFF;
    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 1") //
        .description("Desc 1 -1") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(true));
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 2") //
        .description("Desc 1 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("3.337") //
        .isManaged(false));
    List<DeviceInfoAvailableControls> device2controls = new ArrayList<>();
    device2controls.add(new DeviceInfoAvailableControls() //
        .name("Dev2 Ctrl 1") //
        .description("Desc 2 -1") //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device2controls.add(new DeviceInfoAvailableControls() //
        .name("Dev2 Ctrl 2") //
        .description("Desc 2 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("1.029") //
        .isManaged(true));
    List<DeviceInfoAvailableControls> device3controls = new ArrayList<>();
    device3controls.add(new DeviceInfoAvailableControls() //
        .name("Dev3 Ctrl 1") //
        .description("Desc 3 -1") //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device3controls.add(new DeviceInfoAvailableControls() //
        .name("Dev3 Ctrl 2") //
        .description("Desc 3 -2") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(false));
    Boolean device1isReg = false;
    Boolean device2isReg = true;
    Boolean device3isReg = false;
    Boolean device1isGrp = true;
    Boolean device2isGrp = false;
    Boolean device3isGrp = true;
    UUID device1grpId = UUID.fromString("3f985f64-9999-0361-032c-2b96dd787d51");
    UUID device2grpId = null;
    UUID device3grpId = UUID.fromString("1a085f64-aaaa-3102-9990-6c963f000b03");
    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(device1id) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);
    DeviceInfo device2 = new DeviceInfo() //
        .deviceId(device2id) //
        .name(device2name) //
        .manufacturer(device2manuf) //
        .comment(device2comment) //
        .address(device2address) //
        .state(device2state) //
        .availableControls(device2controls) //
        .isRegistered(device2isReg) //
        .isGrouped(device2isGrp) //
        .groupId(device2grpId);
    DeviceInfo device3 = new DeviceInfo() //
        .deviceId(device3id) //
        .name(device3name) //
        .manufacturer(device3manuf) //
        .comment(device3comment) //
        .address(device3address) //
        .state(device3state) //
        .availableControls(device3controls) //
        .isRegistered(device3isReg) //
        .isGrouped(device3isGrp) //
        .groupId(device3grpId);

    Map<UUID, DeviceInfo> deviceMap = new HashMap<>();
    deviceMap.put(device1id, device1);
    deviceMap.put(device2id, device2);
    deviceMap.put(device3id, device3);

    HttpStatus expectedStatus = HttpStatus.OK;

    limit = deviceMap.size() + 1;

    // Mock(s)
    doReturn(deviceMap).when(this.deviceRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceInfo> retListResp =
        this.deviceService.getDevices(skip, limit, includeRegistered, includeUnregistered);

    // Verify that appropriate Repository method was called
    verify(this.deviceRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be sorted by device name ascending.",
        retListResp.getObjList(), contains(device3, device2, device1));
  }

  @Test
  @DisplayName("TEST getDevices (skip = 1, limit = 1)")
  void testGetDevicesSkip1Limit1() {
    Integer skip = 1;
    Integer limit = 1;
    Boolean includeRegistered = null;
    Boolean includeUnregistered = null;

    UUID device1id = UUID.fromString("3f985f64-0314-0361-032c-2b963f789af1");
    UUID device2id = UUID.fromString("4b785f64-5147-4013-b3fc-48963f50ace2");
    UUID device3id = UUID.fromString("1a085f64-842b-3102-9990-6c963f1b3bd3");
    String device1name = "Shark - Test";
    String device2name = "Samsung - Test";
    String device3name = "Apple -- Test";
    String device1manuf = "Shark Inc.";
    String device2manuf = "Samsung Inc.";
    String device3manuf = "The Apple Company";
    String device1comment = "An Old TV in the Living Room.";
    String device2comment = "Mini TV in the Bedroom";
    String device3comment = "A test device.";
    String device1address = "2607:f0d0:1002:31::1";
    String device2address = "2607:f0d0:1002:42::2";
    String device3address = "2607:f0d0:1002:53::3";
    CommonDeviceState device1state = CommonDeviceState.OFF;
    CommonDeviceState device2state = CommonDeviceState.ON;
    CommonDeviceState device3state = CommonDeviceState.OFF;
    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 1") //
        .description("Desc 1 -1") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(true));
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 2") //
        .description("Desc 1 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("3.337") //
        .isManaged(false));
    List<DeviceInfoAvailableControls> device2controls = new ArrayList<>();
    device2controls.add(new DeviceInfoAvailableControls() //
        .name("Dev2 Ctrl 1") //
        .description("Desc 2 -1") //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device2controls.add(new DeviceInfoAvailableControls() //
        .name("Dev2 Ctrl 2") //
        .description("Desc 2 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("1.029") //
        .isManaged(true));
    List<DeviceInfoAvailableControls> device3controls = new ArrayList<>();
    device3controls.add(new DeviceInfoAvailableControls() //
        .name("Dev3 Ctrl 1") //
        .description("Desc 3 -1") //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device3controls.add(new DeviceInfoAvailableControls() //
        .name("Dev3 Ctrl 2") //
        .description("Desc 3 -2") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(false));
    Boolean device1isReg = false;
    Boolean device2isReg = true;
    Boolean device3isReg = false;
    Boolean device1isGrp = true;
    Boolean device2isGrp = false;
    Boolean device3isGrp = true;
    UUID device1grpId = UUID.fromString("3f985f64-9999-0361-032c-2b96dd787d51");
    UUID device2grpId = null;
    UUID device3grpId = UUID.fromString("1a085f64-aaaa-3102-9990-6c963f000b03");
    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(device1id) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);
    DeviceInfo device2 = new DeviceInfo() //
        .deviceId(device2id) //
        .name(device2name) //
        .manufacturer(device2manuf) //
        .comment(device2comment) //
        .address(device2address) //
        .state(device2state) //
        .availableControls(device2controls) //
        .isRegistered(device2isReg) //
        .isGrouped(device2isGrp) //
        .groupId(device2grpId);
    DeviceInfo device3 = new DeviceInfo() //
        .deviceId(device3id) //
        .name(device3name) //
        .manufacturer(device3manuf) //
        .comment(device3comment) //
        .address(device3address) //
        .state(device3state) //
        .availableControls(device3controls) //
        .isRegistered(device3isReg) //
        .isGrouped(device3isGrp) //
        .groupId(device3grpId);

    Map<UUID, DeviceInfo> deviceMap = new HashMap<>();
    deviceMap.put(device1id, device1);
    deviceMap.put(device2id, device2);
    deviceMap.put(device3id, device3);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(deviceMap).when(this.deviceRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceInfo> retListResp =
        this.deviceService.getDevices(skip, limit, includeRegistered, includeUnregistered);

    // Verify that appropriate Repository method was called
    verify(this.deviceRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must only be one, the second entry only.",
        retListResp.getObjList(), contains(device2));
  }

  @Test
  @DisplayName("TEST getDevices (skip = 2)")
  void testGetDevicesSkip2() {
    Integer skip = 2;
    Integer limit = null;
    Boolean includeRegistered = null;
    Boolean includeUnregistered = null;

    UUID device1id = UUID.fromString("3f985f64-0314-0361-032c-2b963f789af1");
    UUID device2id = UUID.fromString("4b785f64-5147-4013-b3fc-48963f50ace2");
    UUID device3id = UUID.fromString("1a085f64-842b-3102-9990-6c963f1b3bd3");
    String device1name = "Shark - Test";
    String device2name = "Samsung - Test";
    String device3name = "Apple -- Test";
    String device1manuf = "Shark Inc.";
    String device2manuf = "Samsung Inc.";
    String device3manuf = "The Apple Company";
    String device1comment = "An Old TV in the Living Room.";
    String device2comment = "Mini TV in the Bedroom";
    String device3comment = "A test device.";
    String device1address = "2607:f0d0:1002:31::1";
    String device2address = "2607:f0d0:1002:42::2";
    String device3address = "2607:f0d0:1002:53::3";
    CommonDeviceState device1state = CommonDeviceState.OFF;
    CommonDeviceState device2state = CommonDeviceState.ON;
    CommonDeviceState device3state = CommonDeviceState.OFF;
    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 1") //
        .description("Desc 1 -1") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(true));
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 2") //
        .description("Desc 1 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("3.337") //
        .isManaged(false));
    List<DeviceInfoAvailableControls> device2controls = new ArrayList<>();
    device2controls.add(new DeviceInfoAvailableControls() //
        .name("Dev2 Ctrl 1") //
        .description("Desc 2 -1") //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device2controls.add(new DeviceInfoAvailableControls() //
        .name("Dev2 Ctrl 2") //
        .description("Desc 2 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("1.029") //
        .isManaged(true));
    List<DeviceInfoAvailableControls> device3controls = new ArrayList<>();
    device3controls.add(new DeviceInfoAvailableControls() //
        .name("Dev3 Ctrl 1") //
        .description("Desc 3 -1") //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device3controls.add(new DeviceInfoAvailableControls() //
        .name("Dev3 Ctrl 2") //
        .description("Desc 3 -2") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(false));
    Boolean device1isReg = false;
    Boolean device2isReg = true;
    Boolean device3isReg = false;
    Boolean device1isGrp = true;
    Boolean device2isGrp = false;
    Boolean device3isGrp = true;
    UUID device1grpId = UUID.fromString("3f985f64-9999-0361-032c-2b96dd787d51");
    UUID device2grpId = null;
    UUID device3grpId = UUID.fromString("1a085f64-aaaa-3102-9990-6c963f000b03");
    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(device1id) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);
    DeviceInfo device2 = new DeviceInfo() //
        .deviceId(device2id) //
        .name(device2name) //
        .manufacturer(device2manuf) //
        .comment(device2comment) //
        .address(device2address) //
        .state(device2state) //
        .availableControls(device2controls) //
        .isRegistered(device2isReg) //
        .isGrouped(device2isGrp) //
        .groupId(device2grpId);
    DeviceInfo device3 = new DeviceInfo() //
        .deviceId(device3id) //
        .name(device3name) //
        .manufacturer(device3manuf) //
        .comment(device3comment) //
        .address(device3address) //
        .state(device3state) //
        .availableControls(device3controls) //
        .isRegistered(device3isReg) //
        .isGrouped(device3isGrp) //
        .groupId(device3grpId);

    Map<UUID, DeviceInfo> deviceMap = new HashMap<>();
    deviceMap.put(device1id, device1);
    deviceMap.put(device2id, device2);
    deviceMap.put(device3id, device3);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(deviceMap).when(this.deviceRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceInfo> retListResp =
        this.deviceService.getDevices(skip, limit, includeRegistered, includeUnregistered);

    // Verify that appropriate Repository method was called
    verify(this.deviceRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must only be one, the last entry only.", retListResp.getObjList(),
        contains(device1));
  }

  @Test
  @DisplayName("TEST getDevices (limit = 2)")
  void testGetDevicesLimit2() {
    Integer skip = null;
    Integer limit = 2;
    Boolean includeRegistered = null;
    Boolean includeUnregistered = null;

    UUID device1id = UUID.fromString("3f985f64-0314-0361-032c-2b963f789af1");
    UUID device2id = UUID.fromString("4b785f64-5147-4013-b3fc-48963f50ace2");
    UUID device3id = UUID.fromString("1a085f64-842b-3102-9990-6c963f1b3bd3");
    String device1name = "Shark - Test";
    String device2name = "Samsung - Test";
    String device3name = "Apple -- Test";
    String device1manuf = "Shark Inc.";
    String device2manuf = "Samsung Inc.";
    String device3manuf = "The Apple Company";
    String device1comment = "An Old TV in the Living Room.";
    String device2comment = "Mini TV in the Bedroom";
    String device3comment = "A test device.";
    String device1address = "2607:f0d0:1002:31::1";
    String device2address = "2607:f0d0:1002:42::2";
    String device3address = "2607:f0d0:1002:53::3";
    CommonDeviceState device1state = CommonDeviceState.OFF;
    CommonDeviceState device2state = CommonDeviceState.ON;
    CommonDeviceState device3state = CommonDeviceState.OFF;
    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 1") //
        .description("Desc 1 -1") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(true));
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 2") //
        .description("Desc 1 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("3.337") //
        .isManaged(false));
    List<DeviceInfoAvailableControls> device2controls = new ArrayList<>();
    device2controls.add(new DeviceInfoAvailableControls() //
        .name("Dev2 Ctrl 1") //
        .description("Desc 2 -1") //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device2controls.add(new DeviceInfoAvailableControls() //
        .name("Dev2 Ctrl 2") //
        .description("Desc 2 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("1.029") //
        .isManaged(true));
    List<DeviceInfoAvailableControls> device3controls = new ArrayList<>();
    device3controls.add(new DeviceInfoAvailableControls() //
        .name("Dev3 Ctrl 1") //
        .description("Desc 3 -1") //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device3controls.add(new DeviceInfoAvailableControls() //
        .name("Dev3 Ctrl 2") //
        .description("Desc 3 -2") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(false));
    Boolean device1isReg = false;
    Boolean device2isReg = true;
    Boolean device3isReg = false;
    Boolean device1isGrp = true;
    Boolean device2isGrp = false;
    Boolean device3isGrp = true;
    UUID device1grpId = UUID.fromString("3f985f64-9999-0361-032c-2b96dd787d51");
    UUID device2grpId = null;
    UUID device3grpId = UUID.fromString("1a085f64-aaaa-3102-9990-6c963f000b03");
    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(device1id) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);
    DeviceInfo device2 = new DeviceInfo() //
        .deviceId(device2id) //
        .name(device2name) //
        .manufacturer(device2manuf) //
        .comment(device2comment) //
        .address(device2address) //
        .state(device2state) //
        .availableControls(device2controls) //
        .isRegistered(device2isReg) //
        .isGrouped(device2isGrp) //
        .groupId(device2grpId);
    DeviceInfo device3 = new DeviceInfo() //
        .deviceId(device3id) //
        .name(device3name) //
        .manufacturer(device3manuf) //
        .comment(device3comment) //
        .address(device3address) //
        .state(device3state) //
        .availableControls(device3controls) //
        .isRegistered(device3isReg) //
        .isGrouped(device3isGrp) //
        .groupId(device3grpId);

    Map<UUID, DeviceInfo> deviceMap = new HashMap<>();
    deviceMap.put(device1id, device1);
    deviceMap.put(device2id, device2);
    deviceMap.put(device3id, device3);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(deviceMap).when(this.deviceRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceInfo> retListResp =
        this.deviceService.getDevices(skip, limit, includeRegistered, includeUnregistered);

    // Verify that appropriate Repository method was called
    verify(this.deviceRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must only be 2, sorted by device name ascending.",
        retListResp.getObjList(), contains(device3, device2));
  }

  @Test
  @DisplayName("TEST getDevices (includeRegistered = true)")
  void testGetDevicesIncludeRegisteredTrue() {
    Integer skip = null;
    Integer limit = null;
    Boolean includeRegistered = true;
    Boolean includeUnregistered = null;

    UUID device1id = UUID.fromString("3f985f64-0314-0361-032c-2b963f789af1");
    UUID device2id = UUID.fromString("4b785f64-5147-4013-b3fc-48963f50ace2");
    UUID device3id = UUID.fromString("1a085f64-842b-3102-9990-6c963f1b3bd3");
    String device1name = "Shark - Test";
    String device2name = "Samsung - Test";
    String device3name = "Apple -- Test";
    String device1manuf = "Shark Inc.";
    String device2manuf = "Samsung Inc.";
    String device3manuf = "The Apple Company";
    String device1comment = "An Old TV in the Living Room.";
    String device2comment = "Mini TV in the Bedroom";
    String device3comment = "A test device.";
    String device1address = "2607:f0d0:1002:31::1";
    String device2address = "2607:f0d0:1002:42::2";
    String device3address = "2607:f0d0:1002:53::3";
    CommonDeviceState device1state = CommonDeviceState.OFF;
    CommonDeviceState device2state = CommonDeviceState.ON;
    CommonDeviceState device3state = CommonDeviceState.OFF;
    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 1") //
        .description("Desc 1 -1") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(true));
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 2") //
        .description("Desc 1 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("3.337") //
        .isManaged(false));
    List<DeviceInfoAvailableControls> device2controls = new ArrayList<>();
    device2controls.add(new DeviceInfoAvailableControls() //
        .name("Dev2 Ctrl 1") //
        .description("Desc 2 -1") //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device2controls.add(new DeviceInfoAvailableControls() //
        .name("Dev2 Ctrl 2") //
        .description("Desc 2 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("1.029") //
        .isManaged(true));
    List<DeviceInfoAvailableControls> device3controls = new ArrayList<>();
    device3controls.add(new DeviceInfoAvailableControls() //
        .name("Dev3 Ctrl 1") //
        .description("Desc 3 -1") //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device3controls.add(new DeviceInfoAvailableControls() //
        .name("Dev3 Ctrl 2") //
        .description("Desc 3 -2") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(false));
    Boolean device1isReg = false;
    Boolean device2isReg = true;
    Boolean device3isReg = false;
    Boolean device1isGrp = true;
    Boolean device2isGrp = false;
    Boolean device3isGrp = true;
    UUID device1grpId = UUID.fromString("3f985f64-9999-0361-032c-2b96dd787d51");
    UUID device2grpId = null;
    UUID device3grpId = UUID.fromString("1a085f64-aaaa-3102-9990-6c963f000b03");
    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(device1id) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);
    DeviceInfo device2 = new DeviceInfo() //
        .deviceId(device2id) //
        .name(device2name) //
        .manufacturer(device2manuf) //
        .comment(device2comment) //
        .address(device2address) //
        .state(device2state) //
        .availableControls(device2controls) //
        .isRegistered(device2isReg) //
        .isGrouped(device2isGrp) //
        .groupId(device2grpId);
    DeviceInfo device3 = new DeviceInfo() //
        .deviceId(device3id) //
        .name(device3name) //
        .manufacturer(device3manuf) //
        .comment(device3comment) //
        .address(device3address) //
        .state(device3state) //
        .availableControls(device3controls) //
        .isRegistered(device3isReg) //
        .isGrouped(device3isGrp) //
        .groupId(device3grpId);

    Map<UUID, DeviceInfo> deviceMap = new HashMap<>();
    deviceMap.put(device1id, device1);
    deviceMap.put(device2id, device2);
    deviceMap.put(device3id, device3);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(deviceMap).when(this.deviceRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceInfo> retListResp =
        this.deviceService.getDevices(skip, limit, includeRegistered, includeUnregistered);

    // Verify that appropriate Repository method was called
    verify(this.deviceRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be sorted by device name ascending.",
        retListResp.getObjList(), contains(device3, device2, device1));
  }

  @Test
  @DisplayName("TEST getDevices (includeRegistered = false)")
  void testGetDevicesIncludeRegisteredFalse() {
    Integer skip = null;
    Integer limit = null;
    Boolean includeRegistered = false;
    Boolean includeUnregistered = null;

    UUID device1id = UUID.fromString("3f985f64-0314-0361-032c-2b963f789af1");
    UUID device2id = UUID.fromString("4b785f64-5147-4013-b3fc-48963f50ace2");
    UUID device3id = UUID.fromString("1a085f64-842b-3102-9990-6c963f1b3bd3");
    String device1name = "Shark - Test";
    String device2name = "Samsung - Test";
    String device3name = "Apple -- Test";
    String device1manuf = "Shark Inc.";
    String device2manuf = "Samsung Inc.";
    String device3manuf = "The Apple Company";
    String device1comment = "An Old TV in the Living Room.";
    String device2comment = "Mini TV in the Bedroom";
    String device3comment = "A test device.";
    String device1address = "2607:f0d0:1002:31::1";
    String device2address = "2607:f0d0:1002:42::2";
    String device3address = "2607:f0d0:1002:53::3";
    CommonDeviceState device1state = CommonDeviceState.OFF;
    CommonDeviceState device2state = CommonDeviceState.ON;
    CommonDeviceState device3state = CommonDeviceState.OFF;
    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 1") //
        .description("Desc 1 -1") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(true));
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 2") //
        .description("Desc 1 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("3.337") //
        .isManaged(false));
    List<DeviceInfoAvailableControls> device2controls = new ArrayList<>();
    device2controls.add(new DeviceInfoAvailableControls() //
        .name("Dev2 Ctrl 1") //
        .description("Desc 2 -1") //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device2controls.add(new DeviceInfoAvailableControls() //
        .name("Dev2 Ctrl 2") //
        .description("Desc 2 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("1.029") //
        .isManaged(true));
    List<DeviceInfoAvailableControls> device3controls = new ArrayList<>();
    device3controls.add(new DeviceInfoAvailableControls() //
        .name("Dev3 Ctrl 1") //
        .description("Desc 3 -1") //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device3controls.add(new DeviceInfoAvailableControls() //
        .name("Dev3 Ctrl 2") //
        .description("Desc 3 -2") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(false));
    Boolean device1isReg = false;
    Boolean device2isReg = true;
    Boolean device3isReg = false;
    Boolean device1isGrp = true;
    Boolean device2isGrp = false;
    Boolean device3isGrp = true;
    UUID device1grpId = UUID.fromString("3f985f64-9999-0361-032c-2b96dd787d51");
    UUID device2grpId = null;
    UUID device3grpId = UUID.fromString("1a085f64-aaaa-3102-9990-6c963f000b03");
    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(device1id) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);
    DeviceInfo device2 = new DeviceInfo() //
        .deviceId(device2id) //
        .name(device2name) //
        .manufacturer(device2manuf) //
        .comment(device2comment) //
        .address(device2address) //
        .state(device2state) //
        .availableControls(device2controls) //
        .isRegistered(device2isReg) //
        .isGrouped(device2isGrp) //
        .groupId(device2grpId);
    DeviceInfo device3 = new DeviceInfo() //
        .deviceId(device3id) //
        .name(device3name) //
        .manufacturer(device3manuf) //
        .comment(device3comment) //
        .address(device3address) //
        .state(device3state) //
        .availableControls(device3controls) //
        .isRegistered(device3isReg) //
        .isGrouped(device3isGrp) //
        .groupId(device3grpId);

    Map<UUID, DeviceInfo> deviceMap = new HashMap<>();
    deviceMap.put(device1id, device1);
    deviceMap.put(device2id, device2);
    deviceMap.put(device3id, device3);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(deviceMap).when(this.deviceRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceInfo> retListResp =
        this.deviceService.getDevices(skip, limit, includeRegistered, includeUnregistered);

    // Verify that appropriate Repository method was called
    verify(this.deviceRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must only be dev 1&3 (unregistered)", retListResp.getObjList(),
        contains(device3, device1));
  }

  @Test
  @DisplayName("TEST getDevices (includeUnregistered = true)")
  void testGetDevicesIncludeUnregisteredTrue() {
    Integer skip = null;
    Integer limit = null;
    Boolean includeRegistered = null;
    Boolean includeUnregistered = true;

    UUID device1id = UUID.fromString("3f985f64-0314-0361-032c-2b963f789af1");
    UUID device2id = UUID.fromString("4b785f64-5147-4013-b3fc-48963f50ace2");
    UUID device3id = UUID.fromString("1a085f64-842b-3102-9990-6c963f1b3bd3");
    String device1name = "Shark - Test";
    String device2name = "Samsung - Test";
    String device3name = "Apple -- Test";
    String device1manuf = "Shark Inc.";
    String device2manuf = "Samsung Inc.";
    String device3manuf = "The Apple Company";
    String device1comment = "An Old TV in the Living Room.";
    String device2comment = "Mini TV in the Bedroom";
    String device3comment = "A test device.";
    String device1address = "2607:f0d0:1002:31::1";
    String device2address = "2607:f0d0:1002:42::2";
    String device3address = "2607:f0d0:1002:53::3";
    CommonDeviceState device1state = CommonDeviceState.OFF;
    CommonDeviceState device2state = CommonDeviceState.ON;
    CommonDeviceState device3state = CommonDeviceState.OFF;
    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 1") //
        .description("Desc 1 -1") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(true));
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 2") //
        .description("Desc 1 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("3.337") //
        .isManaged(false));
    List<DeviceInfoAvailableControls> device2controls = new ArrayList<>();
    device2controls.add(new DeviceInfoAvailableControls() //
        .name("Dev2 Ctrl 1") //
        .description("Desc 2 -1") //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device2controls.add(new DeviceInfoAvailableControls() //
        .name("Dev2 Ctrl 2") //
        .description("Desc 2 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("1.029") //
        .isManaged(true));
    List<DeviceInfoAvailableControls> device3controls = new ArrayList<>();
    device3controls.add(new DeviceInfoAvailableControls() //
        .name("Dev3 Ctrl 1") //
        .description("Desc 3 -1") //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device3controls.add(new DeviceInfoAvailableControls() //
        .name("Dev3 Ctrl 2") //
        .description("Desc 3 -2") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(false));
    Boolean device1isReg = false;
    Boolean device2isReg = true;
    Boolean device3isReg = false;
    Boolean device1isGrp = true;
    Boolean device2isGrp = false;
    Boolean device3isGrp = true;
    UUID device1grpId = UUID.fromString("3f985f64-9999-0361-032c-2b96dd787d51");
    UUID device2grpId = null;
    UUID device3grpId = UUID.fromString("1a085f64-aaaa-3102-9990-6c963f000b03");
    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(device1id) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);
    DeviceInfo device2 = new DeviceInfo() //
        .deviceId(device2id) //
        .name(device2name) //
        .manufacturer(device2manuf) //
        .comment(device2comment) //
        .address(device2address) //
        .state(device2state) //
        .availableControls(device2controls) //
        .isRegistered(device2isReg) //
        .isGrouped(device2isGrp) //
        .groupId(device2grpId);
    DeviceInfo device3 = new DeviceInfo() //
        .deviceId(device3id) //
        .name(device3name) //
        .manufacturer(device3manuf) //
        .comment(device3comment) //
        .address(device3address) //
        .state(device3state) //
        .availableControls(device3controls) //
        .isRegistered(device3isReg) //
        .isGrouped(device3isGrp) //
        .groupId(device3grpId);

    Map<UUID, DeviceInfo> deviceMap = new HashMap<>();
    deviceMap.put(device1id, device1);
    deviceMap.put(device2id, device2);
    deviceMap.put(device3id, device3);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(deviceMap).when(this.deviceRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceInfo> retListResp =
        this.deviceService.getDevices(skip, limit, includeRegistered, includeUnregistered);

    // Verify that appropriate Repository method was called
    verify(this.deviceRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be sorted by device name ascending.",
        retListResp.getObjList(), contains(device3, device2, device1));
  }

  @Test
  @DisplayName("TEST getDevices (includeUnregistered = false)")
  void testGetDevicesIncludeUnregisteredFalse() {
    Integer skip = null;
    Integer limit = null;
    Boolean includeRegistered = null;
    Boolean includeUnregistered = false;

    UUID device1id = UUID.fromString("3f985f64-0314-0361-032c-2b963f789af1");
    UUID device2id = UUID.fromString("4b785f64-5147-4013-b3fc-48963f50ace2");
    UUID device3id = UUID.fromString("1a085f64-842b-3102-9990-6c963f1b3bd3");
    String device1name = "Shark - Test";
    String device2name = "Samsung - Test";
    String device3name = "Apple -- Test";
    String device1manuf = "Shark Inc.";
    String device2manuf = "Samsung Inc.";
    String device3manuf = "The Apple Company";
    String device1comment = "An Old TV in the Living Room.";
    String device2comment = "Mini TV in the Bedroom";
    String device3comment = "A test device.";
    String device1address = "2607:f0d0:1002:31::1";
    String device2address = "2607:f0d0:1002:42::2";
    String device3address = "2607:f0d0:1002:53::3";
    CommonDeviceState device1state = CommonDeviceState.OFF;
    CommonDeviceState device2state = CommonDeviceState.ON;
    CommonDeviceState device3state = CommonDeviceState.OFF;
    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 1") //
        .description("Desc 1 -1") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(true));
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 2") //
        .description("Desc 1 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("3.337") //
        .isManaged(false));
    List<DeviceInfoAvailableControls> device2controls = new ArrayList<>();
    device2controls.add(new DeviceInfoAvailableControls() //
        .name("Dev2 Ctrl 1") //
        .description("Desc 2 -1") //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device2controls.add(new DeviceInfoAvailableControls() //
        .name("Dev2 Ctrl 2") //
        .description("Desc 2 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("1.029") //
        .isManaged(true));
    List<DeviceInfoAvailableControls> device3controls = new ArrayList<>();
    device3controls.add(new DeviceInfoAvailableControls() //
        .name("Dev3 Ctrl 1") //
        .description("Desc 3 -1") //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device3controls.add(new DeviceInfoAvailableControls() //
        .name("Dev3 Ctrl 2") //
        .description("Desc 3 -2") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(false));
    Boolean device1isReg = false;
    Boolean device2isReg = true;
    Boolean device3isReg = false;
    Boolean device1isGrp = true;
    Boolean device2isGrp = false;
    Boolean device3isGrp = true;
    UUID device1grpId = UUID.fromString("3f985f64-9999-0361-032c-2b96dd787d51");
    UUID device2grpId = null;
    UUID device3grpId = UUID.fromString("1a085f64-aaaa-3102-9990-6c963f000b03");
    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(device1id) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);
    DeviceInfo device2 = new DeviceInfo() //
        .deviceId(device2id) //
        .name(device2name) //
        .manufacturer(device2manuf) //
        .comment(device2comment) //
        .address(device2address) //
        .state(device2state) //
        .availableControls(device2controls) //
        .isRegistered(device2isReg) //
        .isGrouped(device2isGrp) //
        .groupId(device2grpId);
    DeviceInfo device3 = new DeviceInfo() //
        .deviceId(device3id) //
        .name(device3name) //
        .manufacturer(device3manuf) //
        .comment(device3comment) //
        .address(device3address) //
        .state(device3state) //
        .availableControls(device3controls) //
        .isRegistered(device3isReg) //
        .isGrouped(device3isGrp) //
        .groupId(device3grpId);

    Map<UUID, DeviceInfo> deviceMap = new HashMap<>();
    deviceMap.put(device1id, device1);
    deviceMap.put(device2id, device2);
    deviceMap.put(device3id, device3);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(deviceMap).when(this.deviceRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceInfo> retListResp =
        this.deviceService.getDevices(skip, limit, includeRegistered, includeUnregistered);

    // Verify that appropriate Repository method was called
    verify(this.deviceRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must only be registered(device2)", retListResp.getObjList(),
        contains(device2));
  }

  @Test
  @DisplayName("TEST getDevices (includeRegistered = true, includeUnregistered = true)")
  void testGetDevicesIncludeRegisteredTrueIncludeUnregisteredTrue() {
    Integer skip = null;
    Integer limit = null;
    Boolean includeRegistered = true;
    Boolean includeUnregistered = true;

    UUID device1id = UUID.fromString("3f985f64-0314-0361-032c-2b963f789af1");
    UUID device2id = UUID.fromString("4b785f64-5147-4013-b3fc-48963f50ace2");
    UUID device3id = UUID.fromString("1a085f64-842b-3102-9990-6c963f1b3bd3");
    String device1name = "Shark - Test";
    String device2name = "Samsung - Test";
    String device3name = "Apple -- Test";
    String device1manuf = "Shark Inc.";
    String device2manuf = "Samsung Inc.";
    String device3manuf = "The Apple Company";
    String device1comment = "An Old TV in the Living Room.";
    String device2comment = "Mini TV in the Bedroom";
    String device3comment = "A test device.";
    String device1address = "2607:f0d0:1002:31::1";
    String device2address = "2607:f0d0:1002:42::2";
    String device3address = "2607:f0d0:1002:53::3";
    CommonDeviceState device1state = CommonDeviceState.OFF;
    CommonDeviceState device2state = CommonDeviceState.ON;
    CommonDeviceState device3state = CommonDeviceState.OFF;
    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 1") //
        .description("Desc 1 -1") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(true));
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 2") //
        .description("Desc 1 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("3.337") //
        .isManaged(false));
    List<DeviceInfoAvailableControls> device2controls = new ArrayList<>();
    device2controls.add(new DeviceInfoAvailableControls() //
        .name("Dev2 Ctrl 1") //
        .description("Desc 2 -1") //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device2controls.add(new DeviceInfoAvailableControls() //
        .name("Dev2 Ctrl 2") //
        .description("Desc 2 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("1.029") //
        .isManaged(true));
    List<DeviceInfoAvailableControls> device3controls = new ArrayList<>();
    device3controls.add(new DeviceInfoAvailableControls() //
        .name("Dev3 Ctrl 1") //
        .description("Desc 3 -1") //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device3controls.add(new DeviceInfoAvailableControls() //
        .name("Dev3 Ctrl 2") //
        .description("Desc 3 -2") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(false));
    Boolean device1isReg = false;
    Boolean device2isReg = true;
    Boolean device3isReg = false;
    Boolean device1isGrp = true;
    Boolean device2isGrp = false;
    Boolean device3isGrp = true;
    UUID device1grpId = UUID.fromString("3f985f64-9999-0361-032c-2b96dd787d51");
    UUID device2grpId = null;
    UUID device3grpId = UUID.fromString("1a085f64-aaaa-3102-9990-6c963f000b03");
    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(device1id) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);
    DeviceInfo device2 = new DeviceInfo() //
        .deviceId(device2id) //
        .name(device2name) //
        .manufacturer(device2manuf) //
        .comment(device2comment) //
        .address(device2address) //
        .state(device2state) //
        .availableControls(device2controls) //
        .isRegistered(device2isReg) //
        .isGrouped(device2isGrp) //
        .groupId(device2grpId);
    DeviceInfo device3 = new DeviceInfo() //
        .deviceId(device3id) //
        .name(device3name) //
        .manufacturer(device3manuf) //
        .comment(device3comment) //
        .address(device3address) //
        .state(device3state) //
        .availableControls(device3controls) //
        .isRegistered(device3isReg) //
        .isGrouped(device3isGrp) //
        .groupId(device3grpId);

    Map<UUID, DeviceInfo> deviceMap = new HashMap<>();
    deviceMap.put(device1id, device1);
    deviceMap.put(device2id, device2);
    deviceMap.put(device3id, device3);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(deviceMap).when(this.deviceRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceInfo> retListResp =
        this.deviceService.getDevices(skip, limit, includeRegistered, includeUnregistered);

    // Verify that appropriate Repository method was called
    verify(this.deviceRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be sorted by device name ascending.",
        retListResp.getObjList(), contains(device3, device2, device1));
  }

  @Test
  @DisplayName("TEST getDevices (includeRegistered = true, includeUnregistered = false)")
  void testGetDevicesIncludeRegisteredTrueIncludeUnregisteredFalse() {
    Integer skip = null;
    Integer limit = null;
    Boolean includeRegistered = true;
    Boolean includeUnregistered = false;

    UUID device1id = UUID.fromString("3f985f64-0314-0361-032c-2b963f789af1");
    UUID device2id = UUID.fromString("4b785f64-5147-4013-b3fc-48963f50ace2");
    UUID device3id = UUID.fromString("1a085f64-842b-3102-9990-6c963f1b3bd3");
    String device1name = "Shark - Test";
    String device2name = "Samsung - Test";
    String device3name = "Apple -- Test";
    String device1manuf = "Shark Inc.";
    String device2manuf = "Samsung Inc.";
    String device3manuf = "The Apple Company";
    String device1comment = "An Old TV in the Living Room.";
    String device2comment = "Mini TV in the Bedroom";
    String device3comment = "A test device.";
    String device1address = "2607:f0d0:1002:31::1";
    String device2address = "2607:f0d0:1002:42::2";
    String device3address = "2607:f0d0:1002:53::3";
    CommonDeviceState device1state = CommonDeviceState.OFF;
    CommonDeviceState device2state = CommonDeviceState.ON;
    CommonDeviceState device3state = CommonDeviceState.OFF;
    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 1") //
        .description("Desc 1 -1") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(true));
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 2") //
        .description("Desc 1 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("3.337") //
        .isManaged(false));
    List<DeviceInfoAvailableControls> device2controls = new ArrayList<>();
    device2controls.add(new DeviceInfoAvailableControls() //
        .name("Dev2 Ctrl 1") //
        .description("Desc 2 -1") //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device2controls.add(new DeviceInfoAvailableControls() //
        .name("Dev2 Ctrl 2") //
        .description("Desc 2 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("1.029") //
        .isManaged(true));
    List<DeviceInfoAvailableControls> device3controls = new ArrayList<>();
    device3controls.add(new DeviceInfoAvailableControls() //
        .name("Dev3 Ctrl 1") //
        .description("Desc 3 -1") //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device3controls.add(new DeviceInfoAvailableControls() //
        .name("Dev3 Ctrl 2") //
        .description("Desc 3 -2") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(false));
    Boolean device1isReg = false;
    Boolean device2isReg = true;
    Boolean device3isReg = false;
    Boolean device1isGrp = true;
    Boolean device2isGrp = false;
    Boolean device3isGrp = true;
    UUID device1grpId = UUID.fromString("3f985f64-9999-0361-032c-2b96dd787d51");
    UUID device2grpId = null;
    UUID device3grpId = UUID.fromString("1a085f64-aaaa-3102-9990-6c963f000b03");
    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(device1id) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);
    DeviceInfo device2 = new DeviceInfo() //
        .deviceId(device2id) //
        .name(device2name) //
        .manufacturer(device2manuf) //
        .comment(device2comment) //
        .address(device2address) //
        .state(device2state) //
        .availableControls(device2controls) //
        .isRegistered(device2isReg) //
        .isGrouped(device2isGrp) //
        .groupId(device2grpId);
    DeviceInfo device3 = new DeviceInfo() //
        .deviceId(device3id) //
        .name(device3name) //
        .manufacturer(device3manuf) //
        .comment(device3comment) //
        .address(device3address) //
        .state(device3state) //
        .availableControls(device3controls) //
        .isRegistered(device3isReg) //
        .isGrouped(device3isGrp) //
        .groupId(device3grpId);

    Map<UUID, DeviceInfo> deviceMap = new HashMap<>();
    deviceMap.put(device1id, device1);
    deviceMap.put(device2id, device2);
    deviceMap.put(device3id, device3);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(deviceMap).when(this.deviceRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceInfo> retListResp =
        this.deviceService.getDevices(skip, limit, includeRegistered, includeUnregistered);

    // Verify that appropriate Repository method was called
    verify(this.deviceRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must only be registered(device2)", retListResp.getObjList(),
        contains(device2));
  }

  @Test
  @DisplayName("TEST getDevices (includeRegistered = false, includeUnregistered = true)")
  void testGetDevicesIncludeRegisteredFalseIncludeUnregisteredTrue() {
    Integer skip = null;
    Integer limit = null;
    Boolean includeRegistered = false;
    Boolean includeUnregistered = true;

    UUID device1id = UUID.fromString("3f985f64-0314-0361-032c-2b963f789af1");
    UUID device2id = UUID.fromString("4b785f64-5147-4013-b3fc-48963f50ace2");
    UUID device3id = UUID.fromString("1a085f64-842b-3102-9990-6c963f1b3bd3");
    String device1name = "Shark - Test";
    String device2name = "Samsung - Test";
    String device3name = "Apple -- Test";
    String device1manuf = "Shark Inc.";
    String device2manuf = "Samsung Inc.";
    String device3manuf = "The Apple Company";
    String device1comment = "An Old TV in the Living Room.";
    String device2comment = "Mini TV in the Bedroom";
    String device3comment = "A test device.";
    String device1address = "2607:f0d0:1002:31::1";
    String device2address = "2607:f0d0:1002:42::2";
    String device3address = "2607:f0d0:1002:53::3";
    CommonDeviceState device1state = CommonDeviceState.OFF;
    CommonDeviceState device2state = CommonDeviceState.ON;
    CommonDeviceState device3state = CommonDeviceState.OFF;
    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 1") //
        .description("Desc 1 -1") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(true));
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 2") //
        .description("Desc 1 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("3.337") //
        .isManaged(false));
    List<DeviceInfoAvailableControls> device2controls = new ArrayList<>();
    device2controls.add(new DeviceInfoAvailableControls() //
        .name("Dev2 Ctrl 1") //
        .description("Desc 2 -1") //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device2controls.add(new DeviceInfoAvailableControls() //
        .name("Dev2 Ctrl 2") //
        .description("Desc 2 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("1.029") //
        .isManaged(true));
    List<DeviceInfoAvailableControls> device3controls = new ArrayList<>();
    device3controls.add(new DeviceInfoAvailableControls() //
        .name("Dev3 Ctrl 1") //
        .description("Desc 3 -1") //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device3controls.add(new DeviceInfoAvailableControls() //
        .name("Dev3 Ctrl 2") //
        .description("Desc 3 -2") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(false));
    Boolean device1isReg = false;
    Boolean device2isReg = true;
    Boolean device3isReg = false;
    Boolean device1isGrp = true;
    Boolean device2isGrp = false;
    Boolean device3isGrp = true;
    UUID device1grpId = UUID.fromString("3f985f64-9999-0361-032c-2b96dd787d51");
    UUID device2grpId = null;
    UUID device3grpId = UUID.fromString("1a085f64-aaaa-3102-9990-6c963f000b03");
    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(device1id) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);
    DeviceInfo device2 = new DeviceInfo() //
        .deviceId(device2id) //
        .name(device2name) //
        .manufacturer(device2manuf) //
        .comment(device2comment) //
        .address(device2address) //
        .state(device2state) //
        .availableControls(device2controls) //
        .isRegistered(device2isReg) //
        .isGrouped(device2isGrp) //
        .groupId(device2grpId);
    DeviceInfo device3 = new DeviceInfo() //
        .deviceId(device3id) //
        .name(device3name) //
        .manufacturer(device3manuf) //
        .comment(device3comment) //
        .address(device3address) //
        .state(device3state) //
        .availableControls(device3controls) //
        .isRegistered(device3isReg) //
        .isGrouped(device3isGrp) //
        .groupId(device3grpId);

    Map<UUID, DeviceInfo> deviceMap = new HashMap<>();
    deviceMap.put(device1id, device1);
    deviceMap.put(device2id, device2);
    deviceMap.put(device3id, device3);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(deviceMap).when(this.deviceRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceInfo> retListResp =
        this.deviceService.getDevices(skip, limit, includeRegistered, includeUnregistered);

    // Verify that appropriate Repository method was called
    verify(this.deviceRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must only be device1 & 3(unregistered)", retListResp.getObjList(),
        contains(device3, device1));
  }

  @Test
  @DisplayName("TEST getDevices (includeRegistered = false, includeUnregistered = false)")
  void testGetDevicesIncludeRegisteredFalseIncludeUnregisteredFalse() {
    Integer skip = null;
    Integer limit = null;
    Boolean includeRegistered = false;
    Boolean includeUnregistered = false;

    UUID device1id = UUID.fromString("3f985f64-0314-0361-032c-2b963f789af1");
    UUID device2id = UUID.fromString("4b785f64-5147-4013-b3fc-48963f50ace2");
    UUID device3id = UUID.fromString("1a085f64-842b-3102-9990-6c963f1b3bd3");
    String device1name = "Shark - Test";
    String device2name = "Samsung - Test";
    String device3name = "Apple -- Test";
    String device1manuf = "Shark Inc.";
    String device2manuf = "Samsung Inc.";
    String device3manuf = "The Apple Company";
    String device1comment = "An Old TV in the Living Room.";
    String device2comment = "Mini TV in the Bedroom";
    String device3comment = "A test device.";
    String device1address = "2607:f0d0:1002:31::1";
    String device2address = "2607:f0d0:1002:42::2";
    String device3address = "2607:f0d0:1002:53::3";
    CommonDeviceState device1state = CommonDeviceState.OFF;
    CommonDeviceState device2state = CommonDeviceState.ON;
    CommonDeviceState device3state = CommonDeviceState.OFF;
    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 1") //
        .description("Desc 1 -1") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(true));
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 2") //
        .description("Desc 1 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("3.337") //
        .isManaged(false));
    List<DeviceInfoAvailableControls> device2controls = new ArrayList<>();
    device2controls.add(new DeviceInfoAvailableControls() //
        .name("Dev2 Ctrl 1") //
        .description("Desc 2 -1") //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device2controls.add(new DeviceInfoAvailableControls() //
        .name("Dev2 Ctrl 2") //
        .description("Desc 2 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("1.029") //
        .isManaged(true));
    List<DeviceInfoAvailableControls> device3controls = new ArrayList<>();
    device3controls.add(new DeviceInfoAvailableControls() //
        .name("Dev3 Ctrl 1") //
        .description("Desc 3 -1") //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device3controls.add(new DeviceInfoAvailableControls() //
        .name("Dev3 Ctrl 2") //
        .description("Desc 3 -2") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(false));
    Boolean device1isReg = false;
    Boolean device2isReg = true;
    Boolean device3isReg = false;
    Boolean device1isGrp = true;
    Boolean device2isGrp = false;
    Boolean device3isGrp = true;
    UUID device1grpId = UUID.fromString("3f985f64-9999-0361-032c-2b96dd787d51");
    UUID device2grpId = null;
    UUID device3grpId = UUID.fromString("1a085f64-aaaa-3102-9990-6c963f000b03");
    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(device1id) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);
    DeviceInfo device2 = new DeviceInfo() //
        .deviceId(device2id) //
        .name(device2name) //
        .manufacturer(device2manuf) //
        .comment(device2comment) //
        .address(device2address) //
        .state(device2state) //
        .availableControls(device2controls) //
        .isRegistered(device2isReg) //
        .isGrouped(device2isGrp) //
        .groupId(device2grpId);
    DeviceInfo device3 = new DeviceInfo() //
        .deviceId(device3id) //
        .name(device3name) //
        .manufacturer(device3manuf) //
        .comment(device3comment) //
        .address(device3address) //
        .state(device3state) //
        .availableControls(device3controls) //
        .isRegistered(device3isReg) //
        .isGrouped(device3isGrp) //
        .groupId(device3grpId);

    Map<UUID, DeviceInfo> deviceMap = new HashMap<>();
    deviceMap.put(device1id, device1);
    deviceMap.put(device2id, device2);
    deviceMap.put(device3id, device3);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(deviceMap).when(this.deviceRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceInfo> retListResp =
        this.deviceService.getDevices(skip, limit, includeRegistered, includeUnregistered);

    // Verify that appropriate Repository method was called
    verify(this.deviceRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getDevices (No entries in the DB(null))")
  void testGetDevicesNoEntryNull() {
    Integer skip = null;
    Integer limit = null;
    Boolean includeRegistered = null;
    Boolean includeUnregistered = null;

    Map<UUID, DeviceInfo> deviceMap = null;

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(deviceMap).when(this.deviceRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceInfo> retListResp =
        this.deviceService.getDevices(skip, limit, includeRegistered, includeUnregistered);

    // Verify that appropriate Repository method was called
    verify(this.deviceRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getDevices (No entries in the DB)")
  void testGetDevicesNoEntry() {
    Integer skip = null;
    Integer limit = null;
    Boolean includeRegistered = null;
    Boolean includeUnregistered = null;

    Map<UUID, DeviceInfo> deviceMap = Collections.emptyMap();

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(deviceMap).when(this.deviceRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceInfo> retListResp =
        this.deviceService.getDevices(skip, limit, includeRegistered, includeUnregistered);

    // Verify that appropriate Repository method was called
    verify(this.deviceRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getDeviceById")
  void testGetDeviceById() {
    UUID device1id = UUID.fromString("3f985f64-0314-0361-032c-2b963f789af1");
    String device1name = "Shark - Test";
    String device1manuf = "Shark Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device1address = "2607:f0d0:1002:31::1";
    CommonDeviceState device1state = CommonDeviceState.OFF;
    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 1") //
        .description("Desc 1 -1") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(true));
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 2") //
        .description("Desc 1 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("3.337") //
        .isManaged(false));
    Boolean device1isReg = false;
    Boolean device1isGrp = true;
    UUID device1grpId = UUID.fromString("3f985f64-9999-0361-032c-2b96dd787d51");
    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(device1id) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.deviceRepository.findById(eq(device1id))).thenReturn(device1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call Service
    CommonGetResponse<DeviceInfo> operResp = this.deviceService.getDeviceById(device1id);

    // Verify that appropriate Repository method was called
    verify(this.deviceRepository, times(1)).findById(arg1.capture());

    // Assertions
    assertEquals(device1id, arg1.getValue(), "Argument must match.");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertEquals(device1, operResp.getObj(), "Device1 must be returned.");
    assertEquals(device1id, operResp.getObj().getDeviceId(), "Device1's id must be returned.");
  }

  @Test
  @DisplayName("TEST getDeviceById (Id not found)")
  void testGetDeviceByIdNotFound() {
    UUID device1id = UUID.fromString("3f985f64-0314-0361-032c-2b963f789af1");
    UUID othDevid = UUID.fromString("00000000-1111-0361-032c-2b963f789af1");
    String device1name = "Shark - Test";
    String device1manuf = "Shark Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device1address = "2607:f0d0:1002:31::1";
    CommonDeviceState device1state = CommonDeviceState.OFF;
    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 1") //
        .description("Desc 1 -1") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(true));
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 2") //
        .description("Desc 1 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("3.337") //
        .isManaged(false));
    Boolean device1isReg = false;
    Boolean device1isGrp = true;
    UUID device1grpId = UUID.fromString("3f985f64-9999-0361-032c-2b96dd787d51");
    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(device1id) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);

    HttpStatus expectedStatus = HttpStatus.NOT_FOUND;

    // Mock(s)
    when(this.deviceRepository.findById(eq(device1id))).thenReturn(device1);
    when(this.deviceRepository.findById(eq(othDevid))).thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call Service
    CommonGetResponse<DeviceInfo> operResp = this.deviceService.getDeviceById(othDevid);

    // Verify that appropriate Repository method was called
    verify(this.deviceRepository, times(1)).findById(arg1.capture());

    // Assertions
    assertEquals(othDevid, arg1.getValue(), "Argument must match.");
    assertNotNull(operResp, "Return object is not null.");
    assertNull(operResp.getObj(), "Inner object must be null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 404.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST registerDevice")
  void testRegisterDevice() {
    UUID device1id = UUID.fromString("3f985f64-0314-0361-032c-2b963f789af1");
    String device1name = "Shark - Test";
    String device1manuf = "Shark Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device1address = "2607:f0d0:1002:31::1";

    String device1name2 = "Shark - Test 2";
    String device1manuf2 = "Shark 2 Inc.";
    String device1comment2 = "Old TV in the Living Room 2.";

    DeviceRegistration devReg = new DeviceRegistration();
    devReg.setName(device1name2);
    devReg.setManufacturer(device1manuf2);
    devReg.setComment(device1comment2);
    devReg.setAddress(device1address);

    CommonDeviceState device1state = CommonDeviceState.OFF;
    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 1") //
        .description("Desc 1 -1") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(true));
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 2") //
        .description("Desc 1 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("3.337") //
        .isManaged(false));
    Boolean device1isReg = false;
    Boolean device1isGrp = true;
    UUID device1grpId = UUID.fromString("3f985f64-9999-0361-032c-2b96dd787d51");
    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(device1id) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);
    Map<UUID, DeviceInfo> deviceMap = new HashMap<>();
    deviceMap.put(device1id, device1);

    HttpStatus expectedStatus = HttpStatus.CREATED;

    // Mock(s)
    doReturn(deviceMap).when(this.deviceRepository).findAll();

    // Argument Captors
    ArgumentCaptor<DeviceInfo> arg1 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call Service
    CommonAddResponse operResp = this.deviceService.registerDevice(devReg);

    // Verify that appropriate Repository methods were called
    verify(this.deviceRepository, times(1)).findAll();
    verify(this.deviceRepository, times(1)).add(arg1.capture());

    // Assertions
    assertNotEquals(device1, arg1.getValue(), "Argument must NOT match (obj was updated)");
    assertEquals(device1id, arg1.getValue().getDeviceId(), "deviceId of Argument must match");
    assertEquals(device1name2, arg1.getValue().getName(), "name of Argument must match");
    assertEquals(device1manuf2, arg1.getValue().getManufacturer(),
        "manufacturer of Argument must match");
    assertEquals(device1comment2, arg1.getValue().getComment(), "comment of Argument must match");
    assertEquals(device1address, arg1.getValue().getAddress(), "address of Argument must match");
    assertEquals(device1state, arg1.getValue().getState(), "state of Argument must match");
    assertEquals(device1controls, arg1.getValue().getAvailableControls(),
        "availableControls of Argument must match");
    assertTrue(arg1.getValue().isIsRegistered(), "isRegistered of Argument must be true");
    assertEquals(device1isGrp, arg1.getValue().isIsGrouped(), "isGrouped of Argument must match");
    assertEquals(device1grpId, arg1.getValue().getGroupId(), "groupId of Argument must match");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 201.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertEquals(device1id, operResp.getId(), "Returned deviceId must match.");
  }

  @Test
  @DisplayName("TEST registerDevice (device address is not found in DB)")
  void testRegisterDeviceNotFoundInDB() {
    UUID device1id = UUID.fromString("3f985f64-0314-0361-032c-2b963f789af1");
    String device1name = "Shark - Test";
    String device1manuf = "Shark Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device1address = "2607:f0d0:1002:31::1";

    String device1name2 = "Shark - Test 2";
    String device1manuf2 = "Shark 2 Inc.";
    String device1comment2 = "Old TV in the Living Room 2.";
    String device1address2 = "2607:f0d0:1002:31::2";

    DeviceRegistration devReg = new DeviceRegistration();
    devReg.setName(device1name2);
    devReg.setManufacturer(device1manuf2);
    devReg.setComment(device1comment2);
    devReg.setAddress(device1address2);

    CommonDeviceState device1state = CommonDeviceState.OFF;
    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 1") //
        .description("Desc 1 -1") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(true));
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 2") //
        .description("Desc 1 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("3.337") //
        .isManaged(false));
    Boolean device1isReg = false;
    Boolean device1isGrp = true;
    UUID device1grpId = UUID.fromString("3f985f64-9999-0361-032c-2b96dd787d51");
    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(device1id) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);
    Map<UUID, DeviceInfo> deviceMap = new HashMap<>();
    deviceMap.put(device1id, device1);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot register device";
    String expectedDetail = "Only detected devices are allowed to be registered.";

    // Mock(s)
    doReturn(deviceMap).when(this.deviceRepository).findAll();

    // Argument Captors
    ArgumentCaptor<DeviceInfo> arg1 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call Service
    CommonAddResponse operResp = this.deviceService.registerDevice(devReg);

    // Verify that appropriate Repository method was called
    verify(this.deviceRepository, times(1)).findAll();

    // Verify that appropriate Repository method was NOT called
    verify(this.deviceRepository, times(0)).add(arg1.capture());

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
    assertNull(operResp.getId(), "Returned deviceId must be null.");
  }

  @Test
  @DisplayName("TEST registerDevice (device address is not found in DB (empty DB))")
  void testRegisterDeviceNotFoundInDbEmpty() {
    String device1name2 = "Shark - Test 2";
    String device1manuf2 = "Shark 2 Inc.";
    String device1comment2 = "Old TV in the Living Room 2.";
    String device1address2 = "2607:f0d0:1002:31::2";

    DeviceRegistration devReg = new DeviceRegistration();
    devReg.setName(device1name2);
    devReg.setManufacturer(device1manuf2);
    devReg.setComment(device1comment2);
    devReg.setAddress(device1address2);

    Map<UUID, DeviceInfo> deviceMap = Collections.emptyMap();

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot register device";
    String expectedDetail = "Only detected devices are allowed to be registered.";

    // Mock(s)
    doReturn(deviceMap).when(this.deviceRepository).findAll();

    // Argument Captors
    ArgumentCaptor<DeviceInfo> arg1 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call Service
    CommonAddResponse operResp = this.deviceService.registerDevice(devReg);

    // Verify that appropriate Repository method was called
    verify(this.deviceRepository, times(1)).findAll();

    // Verify that appropriate Repository method was NOT called
    verify(this.deviceRepository, times(0)).add(arg1.capture());

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
    assertNull(operResp.getId(), "Returned deviceId must be null.");
  }

  @Test
  @DisplayName("TEST registerDevice (device address is not found in DB (null DB))")
  void testRegisterDeviceNotFoundInDbNull() {
    String device1name2 = "Shark - Test 2";
    String device1manuf2 = "Shark 2 Inc.";
    String device1comment2 = "Old TV in the Living Room 2.";
    String device1address2 = "2607:f0d0:1002:31::2";

    DeviceRegistration devReg = new DeviceRegistration();
    devReg.setName(device1name2);
    devReg.setManufacturer(device1manuf2);
    devReg.setComment(device1comment2);
    devReg.setAddress(device1address2);

    Map<UUID, DeviceInfo> deviceMap = null;

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot register device";
    String expectedDetail = "Only detected devices are allowed to be registered.";

    // Mock(s)
    doReturn(deviceMap).when(this.deviceRepository).findAll();

    // Argument Captors
    ArgumentCaptor<DeviceInfo> arg1 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call Service
    CommonAddResponse operResp = this.deviceService.registerDevice(devReg);

    // Verify that appropriate Repository method was called
    verify(this.deviceRepository, times(1)).findAll();

    // Verify that appropriate Repository method was NOT called
    verify(this.deviceRepository, times(0)).add(arg1.capture());

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
    assertNull(operResp.getId(), "Returned deviceId must be null.");
  }

  @Test
  @DisplayName("TEST registerDevice (device was previously already registered)")
  void testRegisterDeviceAlreadyRegistered() {
    UUID device1id = UUID.fromString("3f985f64-0314-0361-032c-2b963f789af1");
    String device1name = "Shark - Test";
    String device1manuf = "Shark Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device1address = "2607:f0d0:1002:31::1";

    String device1name2 = "Shark - Test 2";
    String device1manuf2 = "Shark 2 Inc.";
    String device1comment2 = "Old TV in the Living Room 2.";

    DeviceRegistration devReg = new DeviceRegistration();
    devReg.setName(device1name2);
    devReg.setManufacturer(device1manuf2);
    devReg.setComment(device1comment2);
    devReg.setAddress(device1address);

    CommonDeviceState device1state = CommonDeviceState.OFF;
    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 1") //
        .description("Desc 1 -1") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(true));
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 2") //
        .description("Desc 1 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("3.337") //
        .isManaged(false));
    Boolean device1isReg = true;
    Boolean device1isGrp = true;
    UUID device1grpId = UUID.fromString("3f985f64-9999-0361-032c-2b96dd787d51");
    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(device1id) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);
    Map<UUID, DeviceInfo> deviceMap = new HashMap<>();
    deviceMap.put(device1id, device1);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    doReturn(deviceMap).when(this.deviceRepository).findAll();

    // Argument Captors
    ArgumentCaptor<DeviceInfo> arg1 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call Service
    CommonAddResponse operResp = this.deviceService.registerDevice(devReg);

    // Verify that appropriate Repository method was called
    verify(this.deviceRepository, times(1)).findAll();

    // Verify that appropriate Repository method was NOT called
    verify(this.deviceRepository, times(0)).add(arg1.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertNull(operResp.getId(), "Returned deviceId must be null.");
  }

  @Test
  @DisplayName("TEST modifyDevice")
  void testModifyDevice() {
    UUID device1id = UUID.fromString("3f985f64-0314-0361-032c-2b963f789af1");
    String device1name2 = "Shark - Test2";
    String device1manuf2 = "Shark Inc.2";
    String device1comment2 = "An Old TV in the Living Room.2";

    DeviceModification devMod = new DeviceModification();
    devMod.setName(device1name2);
    devMod.setManufacturer(device1manuf2);
    devMod.setComment(device1comment2);

    String device1name = "Shark - Test";
    String device1manuf = "Shark Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device1address = "2607:f0d0:1002:31::1";
    CommonDeviceState device1state = CommonDeviceState.OFF;
    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 1") //
        .description("Desc 1 -1") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(true));
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 2") //
        .description("Desc 1 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("3.337") //
        .isManaged(false));
    Boolean device1isReg = true;
    Boolean device1isGrp = true;
    UUID device1grpId = UUID.fromString("3f985f64-9999-0361-032c-2b96dd787d51");
    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(device1id) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.deviceRepository.findById(eq(device1id))).thenReturn(device1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg2 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call Service
    CommonOperResponse operResp = this.deviceService.modifyDevice(device1id, devMod);

    // Verify that appropriate Repository methods were called
    verify(this.deviceRepository, times(1)).findById(device1id);
    verify(this.deviceRepository, times(1)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertEquals(device1id, arg1.getValue(), "Argument must match");
    assertNotEquals(device1, arg2.getValue(), "Argument must NOT match (obj was updated)");
    assertEquals(device1id, arg2.getValue().getDeviceId(), "deviceId of Argument must match");
    assertEquals(device1name2, arg2.getValue().getName(), "name of Argument must match");
    assertEquals(device1manuf2, arg2.getValue().getManufacturer(),
        "manufacturer of Argument must match");
    assertEquals(device1comment2, arg2.getValue().getComment(), "comment of Argument must match");
    assertEquals(device1address, arg2.getValue().getAddress(), "address of Argument must match");
    assertEquals(device1state, arg2.getValue().getState(), "state of Argument must match");
    assertEquals(device1controls, arg2.getValue().getAvailableControls(),
        "availableControls of Argument must match");
    assertTrue(arg2.getValue().isIsRegistered(), "isRegistered of Argument must be true");
    assertEquals(device1isGrp, arg2.getValue().isIsGrouped(), "isGrouped of Argument must match");
    assertEquals(device1grpId, arg2.getValue().getGroupId(), "groupId of Argument must match");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyDevice (device is not found in DB)")
  void testModifyDeviceNotFoundInDB() {
    UUID device1id = UUID.fromString("3f985f64-0314-0361-032c-2b963f789af1");
    String device1name2 = "Shark - Test2";
    String device1manuf2 = "Shark Inc.2";
    String device1comment2 = "An Old TV in the Living Room.2";

    DeviceModification devMod = new DeviceModification();
    devMod.setName(device1name2);
    devMod.setManufacturer(device1manuf2);
    devMod.setComment(device1comment2);

    HttpStatus expectedStatus = HttpStatus.NOT_FOUND;

    // Mock(s)
    when(this.deviceRepository.findById(eq(device1id))).thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg2 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call Service
    CommonOperResponse operResp = this.deviceService.modifyDevice(device1id, devMod);

    // Verify that appropriate Repository method was called
    verify(this.deviceRepository, times(1)).findById(device1id);

    // Verify that appropriate Repository method was NOT called
    verify(this.deviceRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 404.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyDevice (device is unregistered)")
  void testModifyDeviceUnregistered() {
    UUID device1id = UUID.fromString("3f985f64-0314-0361-032c-2b963f789af1");
    String device1name2 = "Shark - Test2";
    String device1manuf2 = "Shark Inc.2";
    String device1comment2 = "An Old TV in the Living Room.2";

    DeviceModification devMod = new DeviceModification();
    devMod.setName(device1name2);
    devMod.setManufacturer(device1manuf2);
    devMod.setComment(device1comment2);

    String device1name = "Shark - Test";
    String device1manuf = "Shark Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device1address = "2607:f0d0:1002:31::1";
    CommonDeviceState device1state = CommonDeviceState.OFF;
    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 1") //
        .description("Desc 1 -1") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(true));
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 2") //
        .description("Desc 1 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("3.337") //
        .isManaged(false));
    Boolean device1isReg = false;
    Boolean device1isGrp = true;
    UUID device1grpId = UUID.fromString("3f985f64-9999-0361-032c-2b96dd787d51");
    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(device1id) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot update device";
    String expectedDetail = "Only registered devices are allowed to be modified.";

    // Mock(s)
    when(this.deviceRepository.findById(eq(device1id))).thenReturn(device1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg2 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call Service
    CommonOperResponse operResp = this.deviceService.modifyDevice(device1id, devMod);

    // Verify that appropriate Repository method was called
    verify(this.deviceRepository, times(1)).findById(device1id);

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
  @DisplayName("TEST switchDeviceOnOff")
  void testSwitchDeviceOnOff() {
    UUID device1id = UUID.fromString("3f985f64-0314-0361-032c-2b963f789af1");
    CommonDeviceState dev1newState = CommonDeviceState.ON;
    DeviceSwitchOnOff devMod = new DeviceSwitchOnOff();
    devMod.setState(dev1newState);

    String device1name = "Shark - Test";
    String device1manuf = "Shark Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device1address = "2607:f0d0:1002:31::1";
    CommonDeviceState device1state = CommonDeviceState.OFF;
    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 1") //
        .description("Desc 1 -1") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(true));
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 2") //
        .description("Desc 1 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("3.337") //
        .isManaged(false));
    Boolean device1isReg = true;
    Boolean device1isGrp = true;
    UUID device1grpId = UUID.fromString("3f985f64-9999-0361-032c-2b96dd787d51");
    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(device1id) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.deviceRepository.findById(eq(device1id))).thenReturn(device1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg2 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call Service
    CommonOperResponse operResp = this.deviceService.switchDeviceOnOff(device1id, devMod);

    // Verify that appropriate Repository methods were called
    verify(this.deviceRepository, times(1)).findById(device1id);
    verify(this.deviceRepository, times(1)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertEquals(device1id, arg1.getValue(), "Argument must match");
    assertNotEquals(device1, arg2.getValue(), "Argument must NOT match (obj was updated)");
    assertEquals(device1id, arg2.getValue().getDeviceId(), "deviceId of Argument must match");
    assertEquals(device1name, arg2.getValue().getName(), "name of Argument must match");
    assertEquals(device1manuf, arg2.getValue().getManufacturer(),
        "manufacturer of Argument must match");
    assertEquals(device1comment, arg2.getValue().getComment(), "comment of Argument must match");
    assertEquals(device1address, arg2.getValue().getAddress(), "address of Argument must match");
    assertEquals(dev1newState, arg2.getValue().getState(), "state of Argument must match");
    assertEquals(device1controls, arg2.getValue().getAvailableControls(),
        "availableControls of Argument must match");
    assertTrue(arg2.getValue().isIsRegistered(), "isRegistered of Argument must be true");
    assertEquals(device1isGrp, arg2.getValue().isIsGrouped(), "isGrouped of Argument must match");
    assertEquals(device1grpId, arg2.getValue().getGroupId(), "groupId of Argument must match");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST switchDeviceOnOff (device is not found in DB)")
  void testSwitchDeviceOnOffNotFoundInDB() {
    UUID device1id = UUID.fromString("3f985f64-0314-0361-032c-2b963f789af1");
    CommonDeviceState dev1newState = CommonDeviceState.ON;
    DeviceSwitchOnOff devMod = new DeviceSwitchOnOff();
    devMod.setState(dev1newState);

    HttpStatus expectedStatus = HttpStatus.NOT_FOUND;

    // Mock(s)
    when(this.deviceRepository.findById(eq(device1id))).thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg2 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call Service
    CommonOperResponse operResp = this.deviceService.switchDeviceOnOff(device1id, devMod);

    // Verify that appropriate Repository method was called
    verify(this.deviceRepository, times(1)).findById(device1id);

    // Verify that appropriate Repository method was NOT called
    verify(this.deviceRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 404.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST switchDeviceOnOff (device is unregistered (=ON))")
  void testSwitchDeviceOnOffUnregisteredOn() {
    UUID device1id = UUID.fromString("3f985f64-0314-0361-032c-2b963f789af1");
    CommonDeviceState dev1newState = CommonDeviceState.ON;
    DeviceSwitchOnOff devMod = new DeviceSwitchOnOff();
    devMod.setState(dev1newState);

    String device1name = "Shark - Test";
    String device1manuf = "Shark Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device1address = "2607:f0d0:1002:31::1";
    CommonDeviceState device1state = CommonDeviceState.OFF;
    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 1") //
        .description("Desc 1 -1") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(true));
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 2") //
        .description("Desc 1 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("3.337") //
        .isManaged(false));
    Boolean device1isReg = false;
    Boolean device1isGrp = true;
    UUID device1grpId = UUID.fromString("3f985f64-9999-0361-032c-2b96dd787d51");
    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(device1id) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot switch device to on";
    String expectedDetail = "Only registered devices are allowed to be operated upon.";

    // Mock(s)
    when(this.deviceRepository.findById(eq(device1id))).thenReturn(device1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg2 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call Service
    CommonOperResponse operResp = this.deviceService.switchDeviceOnOff(device1id, devMod);

    // Verify that appropriate Repository method was called
    verify(this.deviceRepository, times(1)).findById(device1id);

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
  @DisplayName("TEST switchDeviceOnOff (device is unregistered (=OFF))")
  void testSwitchDeviceOnOffUnregisteredOff() {
    UUID device1id = UUID.fromString("3f985f64-0314-0361-032c-2b963f789af1");
    CommonDeviceState dev1newState = CommonDeviceState.OFF;
    DeviceSwitchOnOff devMod = new DeviceSwitchOnOff();
    devMod.setState(dev1newState);

    String device1name = "Shark - Test";
    String device1manuf = "Shark Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device1address = "2607:f0d0:1002:31::1";
    CommonDeviceState device1state = CommonDeviceState.OFF;
    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 1") //
        .description("Desc 1 -1") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(true));
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 2") //
        .description("Desc 1 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("3.337") //
        .isManaged(false));
    Boolean device1isReg = false;
    Boolean device1isGrp = true;
    UUID device1grpId = UUID.fromString("3f985f64-9999-0361-032c-2b96dd787d51");
    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(device1id) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot switch device to off";
    String expectedDetail = "Only registered devices are allowed to be operated upon.";

    // Mock(s)
    when(this.deviceRepository.findById(eq(device1id))).thenReturn(device1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg2 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call Service
    CommonOperResponse operResp = this.deviceService.switchDeviceOnOff(device1id, devMod);

    // Verify that appropriate Repository method was called
    verify(this.deviceRepository, times(1)).findById(device1id);

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
  @DisplayName("TEST switchDeviceOnOff (setting to on when device is already on)")
  void testSwitchDeviceOnOffRedundantOn() {
    UUID device1id = UUID.fromString("3f985f64-0314-0361-032c-2b963f789af1");
    CommonDeviceState dev1newState = CommonDeviceState.ON;
    DeviceSwitchOnOff devMod = new DeviceSwitchOnOff();
    devMod.setState(dev1newState);

    String device1name = "Shark - Test";
    String device1manuf = "Shark Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device1address = "2607:f0d0:1002:31::1";
    CommonDeviceState device1state = CommonDeviceState.ON;
    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 1") //
        .description("Desc 1 -1") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(true));
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 2") //
        .description("Desc 1 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("3.337") //
        .isManaged(false));
    Boolean device1isReg = true;
    Boolean device1isGrp = true;
    UUID device1grpId = UUID.fromString("3f985f64-9999-0361-032c-2b96dd787d51");
    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(device1id) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot switch device to on";
    String expectedDetail = "Device is already on";

    // Mock(s)
    when(this.deviceRepository.findById(eq(device1id))).thenReturn(device1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg2 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call Service
    CommonOperResponse operResp = this.deviceService.switchDeviceOnOff(device1id, devMod);

    // Verify that appropriate Repository method was called
    verify(this.deviceRepository, times(1)).findById(device1id);

    // Verify that appropriate Repository method was NOT called
    verify(this.deviceRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
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
  @DisplayName("TEST switchDeviceOnOff (setting to on when device is already off)")
  void testSwitchDeviceOnOffRedundantOff() {
    UUID device1id = UUID.fromString("3f985f64-0314-0361-032c-2b963f789af1");
    CommonDeviceState dev1newState = CommonDeviceState.OFF;
    DeviceSwitchOnOff devMod = new DeviceSwitchOnOff();
    devMod.setState(dev1newState);

    String device1name = "Shark - Test";
    String device1manuf = "Shark Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device1address = "2607:f0d0:1002:31::1";
    CommonDeviceState device1state = CommonDeviceState.OFF;
    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 1") //
        .description("Desc 1 -1") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(true));
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 2") //
        .description("Desc 1 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("3.337") //
        .isManaged(false));
    Boolean device1isReg = true;
    Boolean device1isGrp = true;
    UUID device1grpId = UUID.fromString("3f985f64-9999-0361-032c-2b96dd787d51");
    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(device1id) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot switch device to off";
    String expectedDetail = "Device is already off";

    // Mock(s)
    when(this.deviceRepository.findById(eq(device1id))).thenReturn(device1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg2 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call Service
    CommonOperResponse operResp = this.deviceService.switchDeviceOnOff(device1id, devMod);

    // Verify that appropriate Repository method was called
    verify(this.deviceRepository, times(1)).findById(device1id);

    // Verify that appropriate Repository method was NOT called
    verify(this.deviceRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
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
  @DisplayName("TEST deleteDevice")
  void testDeleteDevice() {
    UUID device1id = UUID.fromString("3f985f64-0314-0361-032c-2b963f789af1");

    String device1name = "Shark - Test";
    String device1manuf = "Shark Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device1address = "2607:f0d0:1002:31::1";
    CommonDeviceState device1state = CommonDeviceState.OFF;
    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 1") //
        .description("Desc 1 -1") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(false));
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 2") //
        .description("Desc 1 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("3.337") //
        .isManaged(false));
    Boolean device1isReg = true;
    Boolean device1isGrp = true;
    UUID device1grpId = UUID.fromString("3f985f64-9999-0361-032c-2b96dd787d51");
    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(device1id) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);

    UUID zone1id = UUID.fromString("99999999-1111-0000-0000-000000000000");
    UUID zone2id = UUID.fromString("99999999-2222-0000-0000-000000000000");
    UUID zone3id = UUID.fromString("99999999-3333-0000-0000-000000000000");
    UUID zone4id = UUID.fromString("99999999-4444-0000-0000-000000000000");
    UUID dummyId1 = UUID.fromString("00000000-1111-0000-0000-000000000000");
    UUID dummyId2 = UUID.fromString("00000000-2222-0000-0000-000000000000");

    ZoneInfo zone1 = new ZoneInfo().zoneId(zone1id);
    zone1.addDeviceIdsItem(dummyId1);
    zone1.addDeviceIdsItem(device1id);
    zone1.addDeviceIdsItem(dummyId2);
    ZoneInfo zone2 = new ZoneInfo().zoneId(zone2id);
    zone2.setDeviceIds(Collections.emptyList());
    ZoneInfo zone3 = new ZoneInfo().zoneId(zone3id);
    zone3.setDeviceIds(null);
    Map<UUID, ZoneInfo> zoneMap = new HashMap<>();
    zoneMap.put(zone4id, null);
    zoneMap.put(zone3id, zone3);
    zoneMap.put(zone2id, zone2);
    zoneMap.put(zone1id, zone1);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.deviceRepository.findById(eq(device1id))).thenReturn(device1);
    doReturn(zoneMap).when(this.zoneRepository).findAll();

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg2 = ArgumentCaptor.forClass(DeviceInfo.class);
    ArgumentCaptor<UUID> arg3 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<UUID> arg4 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ZoneInfo> arg5 = ArgumentCaptor.forClass(ZoneInfo.class);

    // Call Service
    CommonOperResponse operResp = this.deviceService.deleteDevice(device1id);

    // Verify that appropriate Repository methods were called
    verify(this.deviceRepository, times(1)).findById(device1id);
    verify(this.deviceRepository, times(1)).update(arg1.capture(), arg2.capture());
    verify(this.zoneRepository, times(1)).update(arg4.capture(), arg5.capture());

    // Verify that appropriate Repository method was NOT called
    verify(this.deviceRepository, times(0)).delete(arg3.capture());

    // Assertions
    assertEquals(device1id, arg1.getValue(), "Argument must match");
    assertEquals(device1id, arg2.getValue().getDeviceId(), "deviceId of Argument must match");
    assertEquals(device1name, arg2.getValue().getName(), "name of Argument must match");
    assertEquals(device1manuf, arg2.getValue().getManufacturer(),
        "manufacturer of Argument must match");
    assertEquals(device1comment, arg2.getValue().getComment(), "comment of Argument must match");
    assertEquals(device1address, arg2.getValue().getAddress(), "address of Argument must match");
    assertEquals(device1state, arg2.getValue().getState(), "state of Argument must match");
    assertEquals(device1controls, arg2.getValue().getAvailableControls(),
        "availableControls of Argument must match");
    assertFalse(arg2.getValue().isIsRegistered(), "isRegistered of Argument must be false");
    assertFalse(arg2.getValue().isIsGrouped(), "isGrouped of Argument must be false");
    assertNull(arg2.getValue().getGroupId(), "groupId of Argument must be null");
    assertEquals(zone1id, arg4.getValue(), "Argument must match");
    assertNotNull(arg5.getValue(), "Argument (ZoneInfo) must not be null");
    assertThat("The device list inside the ZoneInfo passed must not include the deviceId",
        arg5.getValue().getDeviceIds(), contains(dummyId1, dummyId2));
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST deleteDevice (device is not found in DB)")
  void testDeleteDeviceNotFoundInDB() {
    UUID device1id = UUID.fromString("3f985f64-0314-0361-032c-2b963f789af1");

    HttpStatus expectedStatus = HttpStatus.NOT_FOUND;

    // Mock(s)
    when(this.deviceRepository.findById(eq(device1id))).thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg2 = ArgumentCaptor.forClass(DeviceInfo.class);
    ArgumentCaptor<UUID> arg3 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<UUID> arg4 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ZoneInfo> arg5 = ArgumentCaptor.forClass(ZoneInfo.class);

    // Call Service
    CommonOperResponse operResp = this.deviceService.deleteDevice(device1id);

    // Verify that appropriate Repository method was called
    verify(this.deviceRepository, times(1)).findById(device1id);

    // Verify that appropriate Repository methods were NOT called
    verify(this.deviceRepository, times(0)).update(arg1.capture(), arg2.capture());
    verify(this.deviceRepository, times(0)).delete(arg3.capture());
    verify(this.zoneRepository, times(0)).update(arg4.capture(), arg5.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 404.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST deleteDevice (device is unregistered)")
  void testDeleteDeviceUnregistered() {
    UUID device1id = UUID.fromString("3f985f64-0314-0361-032c-2b963f789af1");

    String device1name = "Shark - Test";
    String device1manuf = "Shark Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device1address = "2607:f0d0:1002:31::1";
    CommonDeviceState device1state = CommonDeviceState.OFF;
    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 1") //
        .description("Desc 1 -1") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(false));
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 2") //
        .description("Desc 1 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("3.337") //
        .isManaged(false));
    Boolean device1isReg = false;
    Boolean device1isGrp = true;
    UUID device1grpId = UUID.fromString("3f985f64-9999-0361-032c-2b96dd787d51");
    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(device1id) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot delete device";
    String expectedDetail = "Only registered devices are allowed to be deleted.";

    // Mock(s)
    when(this.deviceRepository.findById(eq(device1id))).thenReturn(device1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg2 = ArgumentCaptor.forClass(DeviceInfo.class);
    ArgumentCaptor<UUID> arg3 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<UUID> arg4 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ZoneInfo> arg5 = ArgumentCaptor.forClass(ZoneInfo.class);

    // Call Service
    CommonOperResponse operResp = this.deviceService.deleteDevice(device1id);

    // Verify that appropriate Repository method was called
    verify(this.deviceRepository, times(1)).findById(device1id);

    // Verify that appropriate Repository methods were NOT called
    verify(this.deviceRepository, times(0)).update(arg1.capture(), arg2.capture());
    verify(this.deviceRepository, times(0)).delete(arg3.capture());
    verify(this.zoneRepository, times(0)).update(arg4.capture(), arg5.capture());

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
  @DisplayName("TEST deleteDevice (device is switched on)")
  void testDeleteDeviceSwitchedOn() {
    UUID device1id = UUID.fromString("3f985f64-0314-0361-032c-2b963f789af1");

    String device1name = "Shark - Test";
    String device1manuf = "Shark Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device1address = "2607:f0d0:1002:31::1";
    CommonDeviceState device1state = CommonDeviceState.ON;
    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 1") //
        .description("Desc 1 -1") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(false));
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 2") //
        .description("Desc 1 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("3.337") //
        .isManaged(false));
    Boolean device1isReg = true;
    Boolean device1isGrp = true;
    UUID device1grpId = UUID.fromString("3f985f64-9999-0361-032c-2b96dd787d51");
    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(device1id) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot delete device";
    String expectedDetail = "Device is switched on.";

    // Mock(s)
    when(this.deviceRepository.findById(eq(device1id))).thenReturn(device1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg2 = ArgumentCaptor.forClass(DeviceInfo.class);
    ArgumentCaptor<UUID> arg3 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<UUID> arg4 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ZoneInfo> arg5 = ArgumentCaptor.forClass(ZoneInfo.class);

    // Call Service
    CommonOperResponse operResp = this.deviceService.deleteDevice(device1id);

    // Verify that appropriate Repository method was called
    verify(this.deviceRepository, times(1)).findById(device1id);

    // Verify that appropriate Repository methods were NOT called
    verify(this.deviceRepository, times(0)).update(arg1.capture(), arg2.capture());
    verify(this.deviceRepository, times(0)).delete(arg3.capture());
    verify(this.zoneRepository, times(0)).update(arg4.capture(), arg5.capture());

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
  @DisplayName("TEST deleteDevice (the device has a control which is registered)")
  void testDeleteDeviceHasRegisteredControl() {
    UUID device1id = UUID.fromString("3f985f64-0314-0361-032c-2b963f789af1");

    String device1name = "Shark - Test";
    String device1manuf = "Shark Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device1address = "2607:f0d0:1002:31::1";
    CommonDeviceState device1state = CommonDeviceState.OFF;
    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 1") //
        .description("Desc 1 -1") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(true));
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 2") //
        .description("Desc 1 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("3.337") //
        .isManaged(false));
    Boolean device1isReg = true;
    Boolean device1isGrp = true;
    UUID device1grpId = UUID.fromString("3f985f64-9999-0361-032c-2b96dd787d51");
    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(device1id) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot delete device";
    String expectedDetail = "One or more controls in this device are registered.";

    // Mock(s)
    when(this.deviceRepository.findById(eq(device1id))).thenReturn(device1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg2 = ArgumentCaptor.forClass(DeviceInfo.class);
    ArgumentCaptor<UUID> arg3 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<UUID> arg4 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ZoneInfo> arg5 = ArgumentCaptor.forClass(ZoneInfo.class);

    // Call Service
    CommonOperResponse operResp = this.deviceService.deleteDevice(device1id);

    // Verify that appropriate Repository method was called
    verify(this.deviceRepository, times(1)).findById(device1id);

    // Verify that appropriate Repository methods were NOT called
    verify(this.deviceRepository, times(0)).update(arg1.capture(), arg2.capture());
    verify(this.deviceRepository, times(0)).delete(arg3.capture());
    verify(this.zoneRepository, times(0)).update(arg4.capture(), arg5.capture());

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
  @DisplayName("TEST deleteDevice (device has no controls(null))")
  void testDeleteDeviceNoControlsNull() {
    UUID device1id = UUID.fromString("3f985f64-0314-0361-032c-2b963f789af1");

    String device1name = "Shark - Test";
    String device1manuf = "Shark Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device1address = "2607:f0d0:1002:31::1";
    CommonDeviceState device1state = CommonDeviceState.OFF;
    List<DeviceInfoAvailableControls> device1controls = null;
    Boolean device1isReg = true;
    Boolean device1isGrp = true;
    UUID device1grpId = UUID.fromString("3f985f64-9999-0361-032c-2b96dd787d51");
    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(device1id) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.deviceRepository.findById(eq(device1id))).thenReturn(device1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg2 = ArgumentCaptor.forClass(DeviceInfo.class);
    ArgumentCaptor<UUID> arg3 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<UUID> arg4 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ZoneInfo> arg5 = ArgumentCaptor.forClass(ZoneInfo.class);

    // Call Service
    CommonOperResponse operResp = this.deviceService.deleteDevice(device1id);

    // Verify that appropriate Repository methods were called
    verify(this.deviceRepository, times(1)).findById(device1id);
    verify(this.deviceRepository, times(1)).update(arg1.capture(), arg2.capture());

    // Verify that appropriate Repository methods were NOT called
    verify(this.deviceRepository, times(0)).delete(arg3.capture());
    verify(this.zoneRepository, times(0)).update(arg4.capture(), arg5.capture());

    // Assertions
    assertEquals(device1id, arg1.getValue(), "Argument must match");
    assertEquals(device1id, arg2.getValue().getDeviceId(), "deviceId of Argument must match");
    assertEquals(device1name, arg2.getValue().getName(), "name of Argument must match");
    assertEquals(device1manuf, arg2.getValue().getManufacturer(),
        "manufacturer of Argument must match");
    assertEquals(device1comment, arg2.getValue().getComment(), "comment of Argument must match");
    assertEquals(device1address, arg2.getValue().getAddress(), "address of Argument must match");
    assertEquals(device1state, arg2.getValue().getState(), "state of Argument must match");
    assertEquals(device1controls, arg2.getValue().getAvailableControls(),
        "availableControls of Argument must match");
    assertFalse(arg2.getValue().isIsRegistered(), "isRegistered of Argument must be false");
    assertFalse(arg2.getValue().isIsGrouped(), "isGrouped of Argument must be false");
    assertNull(arg2.getValue().getGroupId(), "groupId of Argument must be null");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST deleteDevice (device has no controls)")
  void testDeleteDeviceNoControls() {
    UUID device1id = UUID.fromString("3f985f64-0314-0361-032c-2b963f789af1");

    String device1name = "Shark - Test";
    String device1manuf = "Shark Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device1address = "2607:f0d0:1002:31::1";
    CommonDeviceState device1state = CommonDeviceState.OFF;
    List<DeviceInfoAvailableControls> device1controls = Collections.emptyList();
    Boolean device1isReg = true;
    Boolean device1isGrp = true;
    UUID device1grpId = UUID.fromString("3f985f64-9999-0361-032c-2b96dd787d51");
    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(device1id) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.deviceRepository.findById(eq(device1id))).thenReturn(device1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg2 = ArgumentCaptor.forClass(DeviceInfo.class);
    ArgumentCaptor<UUID> arg3 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<UUID> arg4 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ZoneInfo> arg5 = ArgumentCaptor.forClass(ZoneInfo.class);

    // Call Service
    CommonOperResponse operResp = this.deviceService.deleteDevice(device1id);

    // Verify that appropriate Repository methods were called
    verify(this.deviceRepository, times(1)).findById(device1id);
    verify(this.deviceRepository, times(1)).update(arg1.capture(), arg2.capture());

    // Verify that appropriate Repository methods were NOT called
    verify(this.deviceRepository, times(0)).delete(arg3.capture());
    verify(this.zoneRepository, times(0)).update(arg4.capture(), arg5.capture());

    // Assertions
    assertEquals(device1id, arg1.getValue(), "Argument must match");
    assertEquals(device1id, arg2.getValue().getDeviceId(), "deviceId of Argument must match");
    assertEquals(device1name, arg2.getValue().getName(), "name of Argument must match");
    assertEquals(device1manuf, arg2.getValue().getManufacturer(),
        "manufacturer of Argument must match");
    assertEquals(device1comment, arg2.getValue().getComment(), "comment of Argument must match");
    assertEquals(device1address, arg2.getValue().getAddress(), "address of Argument must match");
    assertEquals(device1state, arg2.getValue().getState(), "state of Argument must match");
    assertEquals(device1controls, arg2.getValue().getAvailableControls(),
        "availableControls of Argument must match");
    assertFalse(arg2.getValue().isIsRegistered(), "isRegistered of Argument must be false");
    assertFalse(arg2.getValue().isIsGrouped(), "isGrouped of Argument must be false");
    assertNull(arg2.getValue().getGroupId(), "groupId of Argument must be null");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST deleteDevice (zone DB is empty(null))")
  void testDeleteDeviceZoneDbEmptyNull() {
    UUID device1id = UUID.fromString("3f985f64-0314-0361-032c-2b963f789af1");

    String device1name = "Shark - Test";
    String device1manuf = "Shark Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device1address = "2607:f0d0:1002:31::1";
    CommonDeviceState device1state = CommonDeviceState.OFF;
    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 1") //
        .description("Desc 1 -1") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(false));
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 2") //
        .description("Desc 1 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("3.337") //
        .isManaged(false));
    Boolean device1isReg = true;
    Boolean device1isGrp = true;
    UUID device1grpId = UUID.fromString("3f985f64-9999-0361-032c-2b96dd787d51");
    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(device1id) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);

    Map<UUID, ZoneInfo> zoneMap = null;

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.deviceRepository.findById(eq(device1id))).thenReturn(device1);
    doReturn(zoneMap).when(this.zoneRepository).findAll();

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg2 = ArgumentCaptor.forClass(DeviceInfo.class);
    ArgumentCaptor<UUID> arg3 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<UUID> arg4 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ZoneInfo> arg5 = ArgumentCaptor.forClass(ZoneInfo.class);

    // Call Service
    CommonOperResponse operResp = this.deviceService.deleteDevice(device1id);

    // Verify that appropriate Repository methods were called
    verify(this.deviceRepository, times(1)).findById(device1id);
    verify(this.deviceRepository, times(1)).update(arg1.capture(), arg2.capture());

    // Verify that appropriate Repository methods were NOT called
    verify(this.deviceRepository, times(0)).delete(arg3.capture());
    verify(this.zoneRepository, times(0)).update(arg4.capture(), arg5.capture());

    // Assertions
    assertEquals(device1id, arg1.getValue(), "Argument must match");
    assertEquals(device1id, arg2.getValue().getDeviceId(), "deviceId of Argument must match");
    assertEquals(device1name, arg2.getValue().getName(), "name of Argument must match");
    assertEquals(device1manuf, arg2.getValue().getManufacturer(),
        "manufacturer of Argument must match");
    assertEquals(device1comment, arg2.getValue().getComment(), "comment of Argument must match");
    assertEquals(device1address, arg2.getValue().getAddress(), "address of Argument must match");
    assertEquals(device1state, arg2.getValue().getState(), "state of Argument must match");
    assertEquals(device1controls, arg2.getValue().getAvailableControls(),
        "availableControls of Argument must match");
    assertFalse(arg2.getValue().isIsRegistered(), "isRegistered of Argument must be false");
    assertFalse(arg2.getValue().isIsGrouped(), "isGrouped of Argument must be false");
    assertNull(arg2.getValue().getGroupId(), "groupId of Argument must be null");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST deleteDevice (zone DB is empty)")
  void testDeleteDeviceZoneDbEmpty() {
    UUID device1id = UUID.fromString("3f985f64-0314-0361-032c-2b963f789af1");

    String device1name = "Shark - Test";
    String device1manuf = "Shark Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device1address = "2607:f0d0:1002:31::1";
    CommonDeviceState device1state = CommonDeviceState.OFF;
    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 1") //
        .description("Desc 1 -1") //
        .controlType(CommonControlType.TOGGLE) //
        .isManaged(false));
    device1controls.add(new DeviceInfoAvailableControls() //
        .name("Dev1 Ctrl 2") //
        .description("Desc 1 -2") //
        .controlType(CommonControlType.DIRECT) //
        .value("3.337") //
        .isManaged(false));
    Boolean device1isReg = true;
    Boolean device1isGrp = true;
    UUID device1grpId = UUID.fromString("3f985f64-9999-0361-032c-2b96dd787d51");
    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(device1id) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);

    Map<UUID, ZoneInfo> zoneMap = Collections.emptyMap();

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.deviceRepository.findById(eq(device1id))).thenReturn(device1);
    doReturn(zoneMap).when(this.zoneRepository).findAll();

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg2 = ArgumentCaptor.forClass(DeviceInfo.class);
    ArgumentCaptor<UUID> arg3 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<UUID> arg4 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ZoneInfo> arg5 = ArgumentCaptor.forClass(ZoneInfo.class);

    // Call Service
    CommonOperResponse operResp = this.deviceService.deleteDevice(device1id);

    // Verify that appropriate Repository methods were called
    verify(this.deviceRepository, times(1)).findById(device1id);
    verify(this.deviceRepository, times(1)).update(arg1.capture(), arg2.capture());

    // Verify that appropriate Repository methods were NOT called
    verify(this.deviceRepository, times(0)).delete(arg3.capture());
    verify(this.zoneRepository, times(0)).update(arg4.capture(), arg5.capture());

    // Assertions
    assertEquals(device1id, arg1.getValue(), "Argument must match");
    assertEquals(device1id, arg2.getValue().getDeviceId(), "deviceId of Argument must match");
    assertEquals(device1name, arg2.getValue().getName(), "name of Argument must match");
    assertEquals(device1manuf, arg2.getValue().getManufacturer(),
        "manufacturer of Argument must match");
    assertEquals(device1comment, arg2.getValue().getComment(), "comment of Argument must match");
    assertEquals(device1address, arg2.getValue().getAddress(), "address of Argument must match");
    assertEquals(device1state, arg2.getValue().getState(), "state of Argument must match");
    assertEquals(device1controls, arg2.getValue().getAvailableControls(),
        "availableControls of Argument must match");
    assertFalse(arg2.getValue().isIsRegistered(), "isRegistered of Argument must be false");
    assertFalse(arg2.getValue().isIsGrouped(), "isGrouped of Argument must be false");
    assertNull(arg2.getValue().getGroupId(), "groupId of Argument must be null");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }
}
