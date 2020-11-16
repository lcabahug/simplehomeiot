package com.codingcuriosity.project.simplehomeiot.controls.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonAddResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonGetListResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonOperResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.ErrorType;
import com.codingcuriosity.project.simplehomeiot.controls.model.CommonControlType;
import com.codingcuriosity.project.simplehomeiot.controls.model.CommonSliderDirection;
import com.codingcuriosity.project.simplehomeiot.controls.model.ControlModification;
import com.codingcuriosity.project.simplehomeiot.controls.model.ControlRegistration;
import com.codingcuriosity.project.simplehomeiot.controls.model.DeviceControlInfo;
import com.codingcuriosity.project.simplehomeiot.controls.model.DirectControlValue;
import com.codingcuriosity.project.simplehomeiot.controls.model.SliderControlValue;
import com.codingcuriosity.project.simplehomeiot.controls.repository.ControlRepository;
import com.codingcuriosity.project.simplehomeiot.controls.southbound.ControlSoloOperation;
import com.codingcuriosity.project.simplehomeiot.devices.model.CommonDeviceState;
import com.codingcuriosity.project.simplehomeiot.devices.model.DeviceInfo;
import com.codingcuriosity.project.simplehomeiot.devices.model.DeviceInfoAvailableControls;
import com.codingcuriosity.project.simplehomeiot.devices.repository.DeviceRepository;
import com.codingcuriosity.project.simplehomeiot.logs.repository.ControllerLogRepository;
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
public class ControlServiceImplTest {

  @Autowired
  ControlService controlService;

  @MockBean
  ControlRepository controlRepository;

  @MockBean
  DeviceRepository deviceRepository;

  @MockBean
  ControllerLogRepository controllerLogRepository;

  @MockBean
  ControlSoloOperation controlOperation;

  @Test
  @DisplayName("TEST getLogRepo")
  void testGetLogRepo() {

    // Call Service
    ControllerLogRepository logRepository = controlService.getLogRepo();

    // Validate
    assertEquals(this.controllerLogRepository, logRepository, "LogRepository must be identical.");
  }

  @Test
  @DisplayName("TEST getDeviceControls")
  void testGetDeviceControls() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID deviceId2 = UUID.fromString("00222222-1111-1111-1111-1111abcdef02");

    UUID contr1id = UUID.fromString("00abcdef-1111-1111-1111-111111111101");
    UUID contr2id = UUID.fromString("00abcdef-2222-2222-2222-222222222202");
    UUID contr3id = UUID.fromString("00abcdef-3333-3333-3333-333333333303");
    UUID contr4id = UUID.fromString("00abcdef-4444-4444-4444-444444444404");
    String contr1name = "Name 1";
    String contr2name = "Name 2";
    String contr3name = "Name 3";
    String contr4name = "Name 4";
    String contr1descr = "Descr 1";
    String contr2descr = "Descr 2";
    String contr3descr = "Descr 3";
    String contr4descr = "Descr 4";
    String contr1val = "12.05";
    String contr2val = null;
    String contr3val = null;
    String contr4val = "15";

    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(contr1id) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);
    DeviceControlInfo contr2 = new DeviceControlInfo() //
        .deviceId(deviceId2) //
        .controlId(contr2id) //
        .name(contr2name) //
        .description(contr2descr) //
        .currentValue(contr2val);
    DeviceControlInfo contr3 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(contr3id) //
        .name(contr3name) //
        .description(contr3descr) //
        .currentValue(contr3val);
    DeviceControlInfo contr4 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(contr4id) //
        .name(contr4name) //
        .description(contr4descr) //
        .currentValue(contr4val);

    Map<UUID, DeviceControlInfo> contrMap = new HashMap<>();
    contrMap.put(contr1id, contr1);
    contrMap.put(contr2id, contr2);
    contrMap.put(contr3id, contr3);
    contrMap.put(contr4id, contr4);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(contrMap).when(this.controlRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceControlInfo> retListResp = this.controlService.getDeviceControls();

    // Verify that appropriate Repository method was called
    verify(this.controlRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must contain 4 entries in any order", retListResp.getObjList(),
        containsInAnyOrder(contr1, contr2, contr3, contr4));
    for (DeviceControlInfo contrInfo : retListResp.getObjList()) {
      if (contrInfo.equals(contr1)) {
        assertEquals(deviceId1, contrInfo.getDeviceId(), "Device Ids should match for device1.");
        assertEquals(contr1id, contrInfo.getControlId(), "Control Ids should match for device1.");
        assertEquals(contr1name, contrInfo.getName(), "Names should match for device1.");
        assertEquals(contr1descr, contrInfo.getDescription(),
            "Descriptions should match for device1.");
        assertEquals(contr1val, contrInfo.getCurrentValue(),
            "Current Values should match for device1.");
        continue;
      } else if (contrInfo.equals(contr2)) {
        assertEquals(deviceId2, contrInfo.getDeviceId(), "Device Ids should match for device2.");
        assertEquals(contr2id, contrInfo.getControlId(), "Control Ids should match for device2.");
        assertEquals(contr2name, contrInfo.getName(), "Names should match for device2.");
        assertEquals(contr2descr, contrInfo.getDescription(),
            "Descriptions should match for device2.");
        assertNull(contrInfo.getCurrentValue(),
            "There should be no value for device 2's currentValue");
        continue;
      } else if (contrInfo.equals(contr3)) {
        assertEquals(deviceId1, contrInfo.getDeviceId(), "Device Ids should match for device3.");
        assertEquals(contr3id, contrInfo.getControlId(), "Control Ids should match for device3.");
        assertEquals(contr3name, contrInfo.getName(), "Names should match for device3.");
        assertEquals(contr3descr, contrInfo.getDescription(),
            "Descriptions should match for device3.");
        assertNull(contrInfo.getCurrentValue(),
            "There should be no value for device 3's currentValue");
        continue;
      } else if (contrInfo.equals(contr4)) {
        assertEquals(deviceId1, contrInfo.getDeviceId(), "Device Ids should match for device4.");
        assertEquals(contr4id, contrInfo.getControlId(), "Control Ids should match for device4.");
        assertEquals(contr4name, contrInfo.getName(), "Names should match for device4.");
        assertEquals(contr4descr, contrInfo.getDescription(),
            "Descriptions should match for device4.");
        assertEquals(contr4val, contrInfo.getCurrentValue(),
            "Current Values should match for device4.");
        continue;
      } else {
        fail("Some unrelated object was in the list.");
      }
    }
  }

  @Test
  @DisplayName("TEST getDeviceControls (No entries in the DB(null))")
  void testGetDeviceControlsNoEntryNull() {
    Map<UUID, DeviceControlInfo> contrMap = null;

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(contrMap).when(this.controlRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceControlInfo> retListResp = this.controlService.getDeviceControls();

    // Verify that appropriate Repository method was called
    verify(this.controlRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getDeviceControls (No entries in the DB)")
  void testGetDeviceControlsNoEntry() {
    Map<UUID, DeviceControlInfo> contrMap = Collections.emptyMap();

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    doReturn(contrMap).when(this.controlRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceControlInfo> retListResp = this.controlService.getDeviceControls();

    // Verify that appropriate Repository method was called
    verify(this.controlRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getFilteredDeviceControls")
  void testGetFilteredDeviceControls() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID deviceId2 = UUID.fromString("00222222-1111-1111-1111-1111abcdef02");
    String device1name = "Shark - Test";
    String device2name = "Samsung - Test";
    String device1manuf = "Shark Inc.";
    String device2manuf = "Samsung Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device2comment = "Mini TV in the Bedroom";
    String device1address = "2607:f0d0:1002:31::1";
    String device2address = "2607:f0d0:1002:42::2";
    CommonDeviceState device1state = CommonDeviceState.OFF;
    CommonDeviceState device2state = CommonDeviceState.ON;
    Boolean device1isReg = true;
    Boolean device2isReg = true;
    Boolean device1isGrp = false;
    Boolean device2isGrp = false;
    UUID device1grpId = null;
    UUID device2grpId = null;

    UUID contr1id = UUID.fromString("00abcdef-1111-1111-1111-111111111101");
    UUID contr2id = UUID.fromString("00abcdef-2222-2222-2222-222222222202");
    UUID contr3id = UUID.fromString("00abcdef-3333-3333-3333-333333333303");
    UUID contr4id = UUID.fromString("00abcdef-4444-4444-4444-444444444404");
    String contr1name = "Name 1";
    String contr2name = "Name 2";
    String contr3name = "Name 3";
    String contr4name = "Name 4";
    String contr1descr = "Descr 1";
    String contr2descr = "Descr 2";
    String contr3descr = "Descr 3";
    String contr4descr = "Descr 4";
    String contr1val = "12.05";
    String contr2val = null;
    String contr3val = null;
    String contr4val = "15";

    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr1name) //
        .description(contr1descr) //
        .controlType(CommonControlType.DIRECT) //
        .value(contr1val) //
        .isManaged(false));
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr2name) //
        .description(contr2descr) //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    List<DeviceInfoAvailableControls> device2controls = new ArrayList<>();
    device2controls.add(new DeviceInfoAvailableControls() //
        .name(contr3name) //
        .description(contr3descr) //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device2controls.add(new DeviceInfoAvailableControls() //
        .name(contr4name) //
        .description(contr4descr) //
        .controlType(CommonControlType.DIRECT) //
        .value(contr4val) //
        .isManaged(false));

    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(deviceId1) //
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
        .deviceId(deviceId2) //
        .name(device2name) //
        .manufacturer(device2manuf) //
        .comment(device2comment) //
        .address(device2address) //
        .state(device2state) //
        .availableControls(device2controls) //
        .isRegistered(device2isReg) //
        .isGrouped(device2isGrp) //
        .groupId(device2grpId);

    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(contr1id) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);
    DeviceControlInfo contr2 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(contr2id) //
        .name(contr2name) //
        .description(contr2descr) //
        .currentValue(contr2val);
    DeviceControlInfo contr3 = new DeviceControlInfo() //
        .deviceId(deviceId2) //
        .controlId(contr3id) //
        .name(contr3name) //
        .description(contr3descr) //
        .currentValue(contr3val);
    DeviceControlInfo contr4 = new DeviceControlInfo() //
        .deviceId(deviceId2) //
        .controlId(contr4id) //
        .name(contr4name) //
        .description(contr4descr) //
        .currentValue(contr4val);

    Map<UUID, DeviceControlInfo> contrMap = new HashMap<>();
    contrMap.put(contr1id, contr1);
    contrMap.put(contr2id, contr2);
    contrMap.put(contr3id, contr3);
    contrMap.put(contr4id, contr4);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.deviceRepository.findById(eq(deviceId1))).thenReturn(device1);
    when(this.deviceRepository.findById(eq(deviceId2))).thenReturn(device2);
    doReturn(contrMap).when(this.controlRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceControlInfo> retListResp =
        this.controlService.getFilteredDeviceControls(deviceId1);

    // Verify that appropriate Repository method was called
    verify(this.controlRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must contain 2 entries in any order", retListResp.getObjList(),
        containsInAnyOrder(contr1, contr2));
    for (DeviceControlInfo contrInfo : retListResp.getObjList()) {
      if (contrInfo.equals(contr1)) {
        assertEquals(deviceId1, contrInfo.getDeviceId(), "Device Ids should match for device1.");
        assertEquals(contr1id, contrInfo.getControlId(), "Control Ids should match for device1.");
        assertEquals(contr1val, contrInfo.getCurrentValue(),
            "Current Values should match for device1.");
        continue;
      } else if (contrInfo.equals(contr2)) {
        assertEquals(deviceId1, contrInfo.getDeviceId(), "Device Ids should match for device2.");
        assertEquals(contr2id, contrInfo.getControlId(), "Control Ids should match for device2.");
        assertNull(contrInfo.getCurrentValue(),
            "There should be no value for device 2's currentValue");
        continue;
      } else {
        fail("Some unrelated object was in the list.");
      }
    }
  }

  @Test
  @DisplayName("TEST getFilteredDeviceControls (controlId = null)")
  void testGetFilteredDeviceControlsIdNull() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID deviceId2 = UUID.fromString("00222222-1111-1111-1111-1111abcdef02");
    String device1name = "Shark - Test";
    String device2name = "Samsung - Test";
    String device1manuf = "Shark Inc.";
    String device2manuf = "Samsung Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device2comment = "Mini TV in the Bedroom";
    String device1address = "2607:f0d0:1002:31::1";
    String device2address = "2607:f0d0:1002:42::2";
    CommonDeviceState device1state = CommonDeviceState.OFF;
    CommonDeviceState device2state = CommonDeviceState.ON;
    Boolean device1isReg = true;
    Boolean device2isReg = true;
    Boolean device1isGrp = false;
    Boolean device2isGrp = false;
    UUID device1grpId = null;
    UUID device2grpId = null;

    UUID contr1id = UUID.fromString("00abcdef-1111-1111-1111-111111111101");
    UUID contr2id = UUID.fromString("00abcdef-2222-2222-2222-222222222202");
    UUID contr3id = UUID.fromString("00abcdef-3333-3333-3333-333333333303");
    UUID contr4id = UUID.fromString("00abcdef-4444-4444-4444-444444444404");
    String contr1name = "Name 1";
    String contr2name = "Name 2";
    String contr3name = "Name 3";
    String contr4name = "Name 4";
    String contr1descr = "Descr 1";
    String contr2descr = "Descr 2";
    String contr3descr = "Descr 3";
    String contr4descr = "Descr 4";
    String contr1val = "12.05";
    String contr2val = null;
    String contr3val = null;
    String contr4val = "15";

    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr1name) //
        .description(contr1descr) //
        .controlType(CommonControlType.DIRECT) //
        .value(contr1val) //
        .isManaged(false));
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr2name) //
        .description(contr2descr) //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    List<DeviceInfoAvailableControls> device2controls = new ArrayList<>();
    device2controls.add(new DeviceInfoAvailableControls() //
        .name(contr3name) //
        .description(contr3descr) //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device2controls.add(new DeviceInfoAvailableControls() //
        .name(contr4name) //
        .description(contr4descr) //
        .controlType(CommonControlType.DIRECT) //
        .value(contr4val) //
        .isManaged(false));

    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(deviceId1) //
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
        .deviceId(deviceId2) //
        .name(device2name) //
        .manufacturer(device2manuf) //
        .comment(device2comment) //
        .address(device2address) //
        .state(device2state) //
        .availableControls(device2controls) //
        .isRegistered(device2isReg) //
        .isGrouped(device2isGrp) //
        .groupId(device2grpId);

    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(contr1id) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);
    DeviceControlInfo contr2 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(contr2id) //
        .name(contr2name) //
        .description(contr2descr) //
        .currentValue(contr2val);
    DeviceControlInfo contr3 = new DeviceControlInfo() //
        .deviceId(deviceId2) //
        .controlId(contr3id) //
        .name(contr3name) //
        .description(contr3descr) //
        .currentValue(contr3val);
    DeviceControlInfo contr4 = new DeviceControlInfo() //
        .deviceId(deviceId2) //
        .controlId(contr4id) //
        .name(contr4name) //
        .description(contr4descr) //
        .currentValue(contr4val);

    Map<UUID, DeviceControlInfo> contrMap = new HashMap<>();
    contrMap.put(contr1id, contr1);
    contrMap.put(contr2id, contr2);
    contrMap.put(contr3id, contr3);
    contrMap.put(contr4id, contr4);

    HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

    // Mock(s)
    when(this.deviceRepository.findById(eq(deviceId1))).thenReturn(device1);
    when(this.deviceRepository.findById(eq(deviceId2))).thenReturn(device2);
    doReturn(contrMap).when(this.controlRepository).findAll();

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call service
    CommonGetListResponse<DeviceControlInfo> retListResp =
        this.controlService.getFilteredDeviceControls(null);

    // Verify that appropriate Repository methods were NOT called
    verify(this.deviceRepository, times(0)).findById(arg1.capture());
    verify(this.controlRepository, times(0)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 400.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST getFilteredDeviceControls (device does not exist)")
  void testGetFilteredDeviceControlsDeviceNotExist() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID deviceId2 = UUID.fromString("00222222-1111-1111-1111-1111abcdef02");
    String device1name = "Shark - Test";
    String device1manuf = "Shark Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device1address = "2607:f0d0:1002:31::1";
    CommonDeviceState device1state = CommonDeviceState.OFF;
    Boolean device1isReg = true;
    Boolean device1isGrp = false;
    UUID device1grpId = null;

    UUID contr1id = UUID.fromString("00abcdef-1111-1111-1111-111111111101");
    UUID contr2id = UUID.fromString("00abcdef-2222-2222-2222-222222222202");
    UUID contr3id = UUID.fromString("00abcdef-3333-3333-3333-333333333303");
    UUID contr4id = UUID.fromString("00abcdef-4444-4444-4444-444444444404");
    String contr1name = "Name 1";
    String contr2name = "Name 2";
    String contr3name = "Name 3";
    String contr4name = "Name 4";
    String contr1descr = "Descr 1";
    String contr2descr = "Descr 2";
    String contr3descr = "Descr 3";
    String contr4descr = "Descr 4";
    String contr1val = "12.05";
    String contr2val = null;
    String contr3val = null;
    String contr4val = "15";

    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr1name) //
        .description(contr1descr) //
        .controlType(CommonControlType.DIRECT) //
        .value(contr1val) //
        .isManaged(false));
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr2name) //
        .description(contr2descr) //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));

    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(deviceId1) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);

    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(contr1id) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);
    DeviceControlInfo contr2 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(contr2id) //
        .name(contr2name) //
        .description(contr2descr) //
        .currentValue(contr2val);
    DeviceControlInfo contr3 = new DeviceControlInfo() //
        .deviceId(deviceId2) //
        .controlId(contr3id) //
        .name(contr3name) //
        .description(contr3descr) //
        .currentValue(contr3val);
    DeviceControlInfo contr4 = new DeviceControlInfo() //
        .deviceId(deviceId2) //
        .controlId(contr4id) //
        .name(contr4name) //
        .description(contr4descr) //
        .currentValue(contr4val);

    Map<UUID, DeviceControlInfo> contrMap = new HashMap<>();
    contrMap.put(contr1id, contr1);
    contrMap.put(contr2id, contr2);
    contrMap.put(contr3id, contr3);
    contrMap.put(contr4id, contr4);

    HttpStatus expectedStatus = HttpStatus.NOT_FOUND;

    // Mock(s)
    when(this.deviceRepository.findById(eq(deviceId1))).thenReturn(device1);
    when(this.deviceRepository.findById(eq(deviceId2))).thenReturn(null);
    doReturn(contrMap).when(this.controlRepository).findAll();

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call service
    CommonGetListResponse<DeviceControlInfo> retListResp =
        this.controlService.getFilteredDeviceControls(deviceId2);

    // Verify that appropriate Repository method was called
    verify(this.deviceRepository, times(1)).findById(arg1.capture());

    // Verify that appropriate Repository method was NOT called
    verify(this.controlRepository, times(0)).findAll();

    // Assertions
    assertEquals(deviceId2, arg1.getValue(), "Argument must match.");
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 404.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getFilteredDeviceControls (device is not registered)")
  void testGetFilteredDeviceControlsDeviceUnregistered() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID deviceId2 = UUID.fromString("00222222-1111-1111-1111-1111abcdef02");
    String device1name = "Shark - Test";
    String device2name = "Samsung - Test";
    String device1manuf = "Shark Inc.";
    String device2manuf = "Samsung Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device2comment = "Mini TV in the Bedroom";
    String device1address = "2607:f0d0:1002:31::1";
    String device2address = "2607:f0d0:1002:42::2";
    CommonDeviceState device1state = CommonDeviceState.OFF;
    CommonDeviceState device2state = CommonDeviceState.ON;
    Boolean device1isReg = false;
    Boolean device2isReg = true;
    Boolean device1isGrp = false;
    Boolean device2isGrp = false;
    UUID device1grpId = null;
    UUID device2grpId = null;

    UUID contr1id = UUID.fromString("00abcdef-1111-1111-1111-111111111101");
    UUID contr2id = UUID.fromString("00abcdef-2222-2222-2222-222222222202");
    UUID contr3id = UUID.fromString("00abcdef-3333-3333-3333-333333333303");
    UUID contr4id = UUID.fromString("00abcdef-4444-4444-4444-444444444404");
    String contr1name = "Name 1";
    String contr2name = "Name 2";
    String contr3name = "Name 3";
    String contr4name = "Name 4";
    String contr1descr = "Descr 1";
    String contr2descr = "Descr 2";
    String contr3descr = "Descr 3";
    String contr4descr = "Descr 4";
    String contr1val = "12.05";
    String contr2val = null;
    String contr3val = null;
    String contr4val = "15";

    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr1name) //
        .description(contr1descr) //
        .controlType(CommonControlType.DIRECT) //
        .value(contr1val) //
        .isManaged(false));
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr2name) //
        .description(contr2descr) //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    List<DeviceInfoAvailableControls> device2controls = new ArrayList<>();
    device2controls.add(new DeviceInfoAvailableControls() //
        .name(contr3name) //
        .description(contr3descr) //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device2controls.add(new DeviceInfoAvailableControls() //
        .name(contr4name) //
        .description(contr4descr) //
        .controlType(CommonControlType.DIRECT) //
        .value(contr4val) //
        .isManaged(false));

    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(deviceId1) //
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
        .deviceId(deviceId2) //
        .name(device2name) //
        .manufacturer(device2manuf) //
        .comment(device2comment) //
        .address(device2address) //
        .state(device2state) //
        .availableControls(device2controls) //
        .isRegistered(device2isReg) //
        .isGrouped(device2isGrp) //
        .groupId(device2grpId);

    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(contr1id) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);
    DeviceControlInfo contr2 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(contr2id) //
        .name(contr2name) //
        .description(contr2descr) //
        .currentValue(contr2val);
    DeviceControlInfo contr3 = new DeviceControlInfo() //
        .deviceId(deviceId2) //
        .controlId(contr3id) //
        .name(contr3name) //
        .description(contr3descr) //
        .currentValue(contr3val);
    DeviceControlInfo contr4 = new DeviceControlInfo() //
        .deviceId(deviceId2) //
        .controlId(contr4id) //
        .name(contr4name) //
        .description(contr4descr) //
        .currentValue(contr4val);

    Map<UUID, DeviceControlInfo> contrMap = new HashMap<>();
    contrMap.put(contr1id, contr1);
    contrMap.put(contr2id, contr2);
    contrMap.put(contr3id, contr3);
    contrMap.put(contr4id, contr4);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot list controls";
    String expectedDetail = "This action is not possible for an unregistered device.";

    // Mock(s)
    when(this.deviceRepository.findById(eq(deviceId1))).thenReturn(device1);
    when(this.deviceRepository.findById(eq(deviceId2))).thenReturn(device2);
    doReturn(contrMap).when(this.controlRepository).findAll();

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call service
    CommonGetListResponse<DeviceControlInfo> retListResp =
        this.controlService.getFilteredDeviceControls(deviceId1);

    // Verify that appropriate Repository method was called
    verify(this.deviceRepository, times(1)).findById(arg1.capture());

    // Verify that appropriate Repository method was NOT called
    verify(this.controlRepository, times(0)).findAll();

    // Assertions
    assertEquals(deviceId1, arg1.getValue(), "Argument must match.");
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 500.");
    assertNotNull(retListResp.getErrorResult(), "There should be an error returned.");
    assertNotNull(retListResp.getErrorResult().getLogId(),
        "Error object must have logId provided.");
    assertNotNull(retListResp.getErrorResult().getTimestamp(),
        "Error object must have timestamp provided.");
    assertEquals(ErrorType.ERROR, retListResp.getErrorResult().getType(),
        "Error object must have type = Error");
    assertNotNull(retListResp.getErrorResult().getType(), "Error object must have logId provided.");
    assertEquals(expectedErrMsg, retListResp.getErrorResult().getMessage(),
        "Error Message must match.");
    assertEquals(expectedDetail, retListResp.getErrorResult().getDetails(),
        "Error Details must match.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getFilteredDeviceControls (No entries in the DB(null)")
  void testGetFilteredDeviceControlsNoEntryNull() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID deviceId2 = UUID.fromString("00222222-1111-1111-1111-1111abcdef02");
    String device1name = "Shark - Test";
    String device2name = "Samsung - Test";
    String device1manuf = "Shark Inc.";
    String device2manuf = "Samsung Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device2comment = "Mini TV in the Bedroom";
    String device1address = "2607:f0d0:1002:31::1";
    String device2address = "2607:f0d0:1002:42::2";
    CommonDeviceState device1state = CommonDeviceState.OFF;
    CommonDeviceState device2state = CommonDeviceState.ON;
    Boolean device1isReg = true;
    Boolean device2isReg = true;
    Boolean device1isGrp = false;
    Boolean device2isGrp = false;
    UUID device1grpId = null;
    UUID device2grpId = null;

    String contr1name = "Name 1";
    String contr2name = "Name 2";
    String contr3name = "Name 3";
    String contr4name = "Name 4";
    String contr1descr = "Descr 1";
    String contr2descr = "Descr 2";
    String contr3descr = "Descr 3";
    String contr4descr = "Descr 4";
    String contr1val = "12.05";
    String contr4val = "15";

    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr1name) //
        .description(contr1descr) //
        .controlType(CommonControlType.DIRECT) //
        .value(contr1val) //
        .isManaged(false));
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr2name) //
        .description(contr2descr) //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    List<DeviceInfoAvailableControls> device2controls = new ArrayList<>();
    device2controls.add(new DeviceInfoAvailableControls() //
        .name(contr3name) //
        .description(contr3descr) //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device2controls.add(new DeviceInfoAvailableControls() //
        .name(contr4name) //
        .description(contr4descr) //
        .controlType(CommonControlType.DIRECT) //
        .value(contr4val) //
        .isManaged(false));

    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(deviceId1) //
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
        .deviceId(deviceId2) //
        .name(device2name) //
        .manufacturer(device2manuf) //
        .comment(device2comment) //
        .address(device2address) //
        .state(device2state) //
        .availableControls(device2controls) //
        .isRegistered(device2isReg) //
        .isGrouped(device2isGrp) //
        .groupId(device2grpId);

    Map<UUID, DeviceControlInfo> contrMap = null;

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.deviceRepository.findById(eq(deviceId1))).thenReturn(device1);
    when(this.deviceRepository.findById(eq(deviceId2))).thenReturn(device2);
    doReturn(contrMap).when(this.controlRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceControlInfo> retListResp =
        this.controlService.getFilteredDeviceControls(deviceId1);

    // Verify that appropriate Repository method was called
    verify(this.controlRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST getFilteredDeviceControls (No entries in the DB")
  void testGetFilteredDeviceControlsNoEntry() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID deviceId2 = UUID.fromString("00222222-1111-1111-1111-1111abcdef02");
    String device1name = "Shark - Test";
    String device2name = "Samsung - Test";
    String device1manuf = "Shark Inc.";
    String device2manuf = "Samsung Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device2comment = "Mini TV in the Bedroom";
    String device1address = "2607:f0d0:1002:31::1";
    String device2address = "2607:f0d0:1002:42::2";
    CommonDeviceState device1state = CommonDeviceState.OFF;
    CommonDeviceState device2state = CommonDeviceState.ON;
    Boolean device1isReg = true;
    Boolean device2isReg = true;
    Boolean device1isGrp = false;
    Boolean device2isGrp = false;
    UUID device1grpId = null;
    UUID device2grpId = null;

    String contr1name = "Name 1";
    String contr2name = "Name 2";
    String contr3name = "Name 3";
    String contr4name = "Name 4";
    String contr1descr = "Descr 1";
    String contr2descr = "Descr 2";
    String contr3descr = "Descr 3";
    String contr4descr = "Descr 4";
    String contr1val = "12.05";
    String contr4val = "15";

    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr1name) //
        .description(contr1descr) //
        .controlType(CommonControlType.DIRECT) //
        .value(contr1val) //
        .isManaged(false));
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr2name) //
        .description(contr2descr) //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    List<DeviceInfoAvailableControls> device2controls = new ArrayList<>();
    device2controls.add(new DeviceInfoAvailableControls() //
        .name(contr3name) //
        .description(contr3descr) //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device2controls.add(new DeviceInfoAvailableControls() //
        .name(contr4name) //
        .description(contr4descr) //
        .controlType(CommonControlType.DIRECT) //
        .value(contr4val) //
        .isManaged(false));

    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(deviceId1) //
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
        .deviceId(deviceId2) //
        .name(device2name) //
        .manufacturer(device2manuf) //
        .comment(device2comment) //
        .address(device2address) //
        .state(device2state) //
        .availableControls(device2controls) //
        .isRegistered(device2isReg) //
        .isGrouped(device2isGrp) //
        .groupId(device2grpId);

    Map<UUID, DeviceControlInfo> contrMap = Collections.emptyMap();

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.deviceRepository.findById(eq(deviceId1))).thenReturn(device1);
    when(this.deviceRepository.findById(eq(deviceId2))).thenReturn(device2);
    doReturn(contrMap).when(this.controlRepository).findAll();

    // Call service
    CommonGetListResponse<DeviceControlInfo> retListResp =
        this.controlService.getFilteredDeviceControls(deviceId1);

    // Verify that appropriate Repository method was called
    verify(this.controlRepository, times(1)).findAll();

    // Assertions
    assertNotNull(retListResp, "Return object is not null.");
    assertEquals(expectedStatus, retListResp.getResponse(), "Response code must be 200.");
    assertNull(retListResp.getErrorResult(), "There should be no error returned.");
    assertThat("The returned List must be empty.", retListResp.getObjList(),
        IsEmptyCollection.empty());
  }

  @Test
  @DisplayName("TEST registerControl")
  void testRegisterControl() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID deviceId2 = UUID.fromString("00111112-2222-1111-1111-1111abcdef02");
    String device1name = "Shark - Test";
    String device2name = "Samsung - Test";
    String device1manuf = "Shark Inc.";
    String device2manuf = "Samsung Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device2comment = "Mini TV in the Bedroom";
    String device1address = "2607:f0d0:1002:31::1";
    String device2address = "2607:f0d0:1002:42::2";
    CommonDeviceState device1state = CommonDeviceState.OFF;
    CommonDeviceState device2state = CommonDeviceState.ON;
    Boolean device1isReg = true;
    Boolean device2isReg = true;
    Boolean device1isGrp = false;
    Boolean device2isGrp = false;
    UUID device1grpId = null;
    UUID device2grpId = null;

    String contr1name = "Name 1";
    String contr2name = "Name 2";
    String contr3name = "Name 3";
    String contr4name = "Name 4";
    String contr1descr = "Descr 1";
    String contr2descr = "Descr 2";
    String contr3descr = "Descr 3";
    String contr4descr = "Descr 4";
    String contr1val = "12.05";
    String contr4val = "15";

    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr1name) //
        .description(contr1descr) //
        .controlType(CommonControlType.DIRECT) //
        .value(contr1val) //
        .isManaged(false));
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr2name) //
        .description(contr2descr) //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    List<DeviceInfoAvailableControls> device2controls = new ArrayList<>();
    device2controls.add(new DeviceInfoAvailableControls() //
        .name(contr3name) //
        .description(contr3descr) //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device2controls.add(new DeviceInfoAvailableControls() //
        .name(contr4name) //
        .description(contr4descr) //
        .controlType(CommonControlType.DIRECT) //
        .value(contr4val) //
        .isManaged(false));

    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(deviceId1) //
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
        .deviceId(deviceId2) //
        .name(device2name) //
        .manufacturer(device2manuf) //
        .comment(device2comment) //
        .address(device2address) //
        .state(device2state) //
        .availableControls(device2controls) //
        .isRegistered(device2isReg) //
        .isGrouped(device2isGrp) //
        .groupId(device2grpId);

    String contr1descr2 = "new description";

    ControlRegistration contrReg = new ControlRegistration();
    contrReg.setName(contr1name);
    contrReg.setDescription(contr1descr2);

    HttpStatus expectedStatus = HttpStatus.CREATED;

    // Mock(s)
    when(this.deviceRepository.findById(eq(deviceId1))).thenReturn(device1);
    when(this.deviceRepository.findById(eq(deviceId2))).thenReturn(device2);
    doReturn(Collections.emptyMap()).when(this.controlRepository).findAll();
    when(this.controlRepository.findById(any(UUID.class))).thenReturn(new DeviceControlInfo())
        .thenReturn(null);

    // Argument Captors
    ArgumentCaptor<DeviceControlInfo> arg1 = ArgumentCaptor.forClass(DeviceControlInfo.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg3 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call Service
    CommonAddResponse operResp = controlService.registerControl(deviceId1, contrReg);

    // Verify that appropriate Repository methods were called
    verify(this.controlRepository, times(1)).add(arg1.capture());
    verify(this.deviceRepository, times(1)).update(arg2.capture(), arg3.capture());

    UUID newControlId = arg1.getValue().getControlId();

    // Assertions
    assertNotNull(arg1.getValue(), "Argument(ControlInfo) must not be null.");
    assertNotNull(newControlId, "Argument's (controlId) must not be null.");
    assertEquals(deviceId1, arg1.getValue().getDeviceId(), "Argument's (deviceId) must match.");
    assertEquals(contr1name, arg1.getValue().getName(), "Argument's (name) must match.");
    assertEquals(contr1descr2, arg1.getValue().getDescription(),
        "Argument's (description) must match.");
    assertEquals(contr1val, arg1.getValue().getCurrentValue(), "Argument's (value) must match.");
    assertEquals(deviceId1, arg2.getValue(), "Argument must match.");
    assertEquals(device1, arg3.getValue(), "Argument must match.");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 201.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertEquals(newControlId, operResp.getId(), "controlId returned must match.");
  }

  @Test
  @DisplayName("TEST registerControl (control map returns null)")
  void testRegisterControlMapReturnsNull() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID deviceId2 = UUID.fromString("00111112-2222-1111-1111-1111abcdef02");
    String device1name = "Shark - Test";
    String device2name = "Samsung - Test";
    String device1manuf = "Shark Inc.";
    String device2manuf = "Samsung Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device2comment = "Mini TV in the Bedroom";
    String device1address = "2607:f0d0:1002:31::1";
    String device2address = "2607:f0d0:1002:42::2";
    CommonDeviceState device1state = CommonDeviceState.OFF;
    CommonDeviceState device2state = CommonDeviceState.ON;
    Boolean device1isReg = true;
    Boolean device2isReg = true;
    Boolean device1isGrp = false;
    Boolean device2isGrp = false;
    UUID device1grpId = null;
    UUID device2grpId = null;

    String contr1name = "Name 1";
    String contr2name = "Name 2";
    String contr3name = "Name 3";
    String contr4name = "Name 4";
    String contr1descr = "Descr 1";
    String contr2descr = "Descr 2";
    String contr3descr = "Descr 3";
    String contr4descr = "Descr 4";
    String contr1val = "12.05";
    String contr4val = "15";

    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr1name) //
        .description(contr1descr) //
        .controlType(CommonControlType.DIRECT) //
        .value(contr1val) //
        .isManaged(false));
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr2name) //
        .description(contr2descr) //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    List<DeviceInfoAvailableControls> device2controls = new ArrayList<>();
    device2controls.add(new DeviceInfoAvailableControls() //
        .name(contr3name) //
        .description(contr3descr) //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device2controls.add(new DeviceInfoAvailableControls() //
        .name(contr4name) //
        .description(contr4descr) //
        .controlType(CommonControlType.DIRECT) //
        .value(contr4val) //
        .isManaged(false));

    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(deviceId1) //
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
        .deviceId(deviceId2) //
        .name(device2name) //
        .manufacturer(device2manuf) //
        .comment(device2comment) //
        .address(device2address) //
        .state(device2state) //
        .availableControls(device2controls) //
        .isRegistered(device2isReg) //
        .isGrouped(device2isGrp) //
        .groupId(device2grpId);

    String contr1descr2 = "new description";

    ControlRegistration contrReg = new ControlRegistration();
    contrReg.setName(contr1name);
    contrReg.setDescription(contr1descr2);

    HttpStatus expectedStatus = HttpStatus.CREATED;

    // Mock(s)
    when(this.deviceRepository.findById(eq(deviceId1))).thenReturn(device1);
    when(this.deviceRepository.findById(eq(deviceId2))).thenReturn(device2);
    doReturn(null).when(this.controlRepository).findAll();

    // Argument Captors
    ArgumentCaptor<DeviceControlInfo> arg1 = ArgumentCaptor.forClass(DeviceControlInfo.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg3 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call Service
    CommonAddResponse operResp = controlService.registerControl(deviceId1, contrReg);

    // Verify that appropriate Repository methods were called
    verify(this.controlRepository, times(1)).add(arg1.capture());
    verify(this.deviceRepository, times(1)).update(arg2.capture(), arg3.capture());

    UUID newControlId = arg1.getValue().getControlId();

    // Assertions
    assertNotNull(arg1.getValue(), "Argument(ControlInfo) must not be null.");
    assertNotNull(newControlId, "Argument's (controlId) must not be null.");
    assertEquals(deviceId1, arg1.getValue().getDeviceId(), "Argument's (deviceId) must match.");
    assertEquals(contr1name, arg1.getValue().getName(), "Argument's (name) must match.");
    assertEquals(contr1descr2, arg1.getValue().getDescription(),
        "Argument's (description) must match.");
    assertEquals(contr1val, arg1.getValue().getCurrentValue(), "Argument's (value) must match.");
    assertEquals(deviceId1, arg2.getValue(), "Argument must match.");
    assertEquals(device1, arg3.getValue(), "Argument must match.");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 201.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
    assertEquals(newControlId, operResp.getId(), "controlId returned must match.");
  }

  @Test
  @DisplayName("TEST registerControl (device does not exist)")
  void testRegisterControlDeviceNotExist() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");

    UUID contr1id = UUID.fromString("00abcdef-1111-1111-1111-111111111101");

    String contr1name = "Name 1";

    String contr1descr2 = "new description";

    ControlRegistration contrReg = new ControlRegistration();
    contrReg.setName(contr1name);
    contrReg.setDescription(contr1descr2);

    HttpStatus expectedStatus = HttpStatus.NOT_FOUND;

    // Mock(s)
    when(this.deviceRepository.findById(eq(deviceId1))).thenReturn(null);
    when(this.controlRepository.findById(eq(contr1id))).thenReturn(null);
    doReturn(null).when(this.controlRepository).findAll();

    // Argument Captors
    ArgumentCaptor<DeviceControlInfo> arg1 = ArgumentCaptor.forClass(DeviceControlInfo.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg3 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call Service
    CommonAddResponse operResp = controlService.registerControl(deviceId1, contrReg);

    // Verify that appropriate Repository methods were NOT called
    verify(this.controlRepository, times(0)).add(arg1.capture());
    verify(this.deviceRepository, times(0)).update(arg2.capture(), arg3.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 500.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST registerControl (device is not registered)")
  void testRegisterControlDeviceUnregistered() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID deviceId2 = UUID.fromString("00111112-2222-1111-1111-1111abcdef02");
    String device1name = "Shark - Test";
    String device2name = "Samsung - Test";
    String device1manuf = "Shark Inc.";
    String device2manuf = "Samsung Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device2comment = "Mini TV in the Bedroom";
    String device1address = "2607:f0d0:1002:31::1";
    String device2address = "2607:f0d0:1002:42::2";
    CommonDeviceState device1state = CommonDeviceState.OFF;
    CommonDeviceState device2state = CommonDeviceState.ON;
    Boolean device1isReg = false;
    Boolean device2isReg = true;
    Boolean device1isGrp = false;
    Boolean device2isGrp = false;
    UUID device1grpId = null;
    UUID device2grpId = null;

    String contr1name = "Name 1";
    String contr2name = "Name 2";
    String contr3name = "Name 3";
    String contr4name = "Name 4";
    String contr1descr = "Descr 1";
    String contr2descr = "Descr 2";
    String contr3descr = "Descr 3";
    String contr4descr = "Descr 4";
    String contr1val = "12.05";
    String contr4val = "15";

    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr1name) //
        .description(contr1descr) //
        .controlType(CommonControlType.DIRECT) //
        .value(contr1val) //
        .isManaged(false));
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr2name) //
        .description(contr2descr) //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    List<DeviceInfoAvailableControls> device2controls = new ArrayList<>();
    device2controls.add(new DeviceInfoAvailableControls() //
        .name(contr3name) //
        .description(contr3descr) //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device2controls.add(new DeviceInfoAvailableControls() //
        .name(contr4name) //
        .description(contr4descr) //
        .controlType(CommonControlType.DIRECT) //
        .value(contr4val) //
        .isManaged(false));

    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(deviceId1) //
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
        .deviceId(deviceId2) //
        .name(device2name) //
        .manufacturer(device2manuf) //
        .comment(device2comment) //
        .address(device2address) //
        .state(device2state) //
        .availableControls(device2controls) //
        .isRegistered(device2isReg) //
        .isGrouped(device2isGrp) //
        .groupId(device2grpId);

    String contr1descr2 = "new description";

    ControlRegistration contrReg = new ControlRegistration();
    contrReg.setName(contr1name);
    contrReg.setDescription(contr1descr2);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot register control";
    String expectedDetail = String.format("Device %s is unregistered.", deviceId1);

    // Mock(s)
    when(this.deviceRepository.findById(eq(deviceId1))).thenReturn(device1);
    when(this.deviceRepository.findById(eq(deviceId2))).thenReturn(device2);
    doReturn(Collections.emptyMap()).when(this.controlRepository).findAll();

    // Argument Captors
    ArgumentCaptor<DeviceControlInfo> arg1 = ArgumentCaptor.forClass(DeviceControlInfo.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg3 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call Service
    CommonAddResponse operResp = controlService.registerControl(deviceId1, contrReg);

    // Verify that appropriate Repository methods were NOT called
    verify(this.controlRepository, times(0)).add(arg1.capture());
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
  @DisplayName("TEST registerControl (device has no available controls(null))")
  void testRegisterControlDeviceHasNoAvailControlsNull() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID deviceId2 = UUID.fromString("00111112-2222-1111-1111-1111abcdef02");
    String device1name = "Shark - Test";
    String device2name = "Samsung - Test";
    String device1manuf = "Shark Inc.";
    String device2manuf = "Samsung Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device2comment = "Mini TV in the Bedroom";
    String device1address = "2607:f0d0:1002:31::1";
    String device2address = "2607:f0d0:1002:42::2";
    CommonDeviceState device1state = CommonDeviceState.OFF;
    CommonDeviceState device2state = CommonDeviceState.ON;
    Boolean device1isReg = true;
    Boolean device2isReg = true;
    Boolean device1isGrp = false;
    Boolean device2isGrp = false;
    UUID device1grpId = null;
    UUID device2grpId = null;

    String contr1name = "Name 1";
    String contr3name = "Name 3";
    String contr4name = "Name 4";
    String contr3descr = "Descr 3";
    String contr4descr = "Descr 4";
    String contr4val = "15";

    List<DeviceInfoAvailableControls> device1controls = null;
    List<DeviceInfoAvailableControls> device2controls = new ArrayList<>();
    device2controls.add(new DeviceInfoAvailableControls() //
        .name(contr3name) //
        .description(contr3descr) //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device2controls.add(new DeviceInfoAvailableControls() //
        .name(contr4name) //
        .description(contr4descr) //
        .controlType(CommonControlType.DIRECT) //
        .value(contr4val) //
        .isManaged(false));

    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(deviceId1) //
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
        .deviceId(deviceId2) //
        .name(device2name) //
        .manufacturer(device2manuf) //
        .comment(device2comment) //
        .address(device2address) //
        .state(device2state) //
        .availableControls(device2controls) //
        .isRegistered(device2isReg) //
        .isGrouped(device2isGrp) //
        .groupId(device2grpId);

    String contr1descr2 = "new description";

    ControlRegistration contrReg = new ControlRegistration();
    contrReg.setName(contr1name);
    contrReg.setDescription(contr1descr2);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot register control";
    String expectedDetail = String.format("Device %s has no available controls.", deviceId1);

    // Mock(s)
    when(this.deviceRepository.findById(eq(deviceId1))).thenReturn(device1);
    when(this.deviceRepository.findById(eq(deviceId2))).thenReturn(device2);
    doReturn(Collections.emptyMap()).when(this.controlRepository).findAll();

    // Argument Captors
    ArgumentCaptor<DeviceControlInfo> arg1 = ArgumentCaptor.forClass(DeviceControlInfo.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg3 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call Service
    CommonAddResponse operResp = controlService.registerControl(deviceId1, contrReg);

    // Verify that appropriate Repository methods were NOT called
    verify(this.controlRepository, times(0)).add(arg1.capture());
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
  @DisplayName("TEST registerControl (device has no available controls)")
  void testRegisterControlDeviceHasNoAvailControls() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID deviceId2 = UUID.fromString("00111112-2222-1111-1111-1111abcdef02");
    String device1name = "Shark - Test";
    String device2name = "Samsung - Test";
    String device1manuf = "Shark Inc.";
    String device2manuf = "Samsung Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device2comment = "Mini TV in the Bedroom";
    String device1address = "2607:f0d0:1002:31::1";
    String device2address = "2607:f0d0:1002:42::2";
    CommonDeviceState device1state = CommonDeviceState.OFF;
    CommonDeviceState device2state = CommonDeviceState.ON;
    Boolean device1isReg = true;
    Boolean device2isReg = true;
    Boolean device1isGrp = false;
    Boolean device2isGrp = false;
    UUID device1grpId = null;
    UUID device2grpId = null;

    String contr1name = "Name 1";
    String contr3name = "Name 3";
    String contr4name = "Name 4";
    String contr3descr = "Descr 3";
    String contr4descr = "Descr 4";
    String contr4val = "15";

    List<DeviceInfoAvailableControls> device1controls = Collections.emptyList();
    List<DeviceInfoAvailableControls> device2controls = new ArrayList<>();
    device2controls.add(new DeviceInfoAvailableControls() //
        .name(contr3name) //
        .description(contr3descr) //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device2controls.add(new DeviceInfoAvailableControls() //
        .name(contr4name) //
        .description(contr4descr) //
        .controlType(CommonControlType.DIRECT) //
        .value(contr4val) //
        .isManaged(false));

    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(deviceId1) //
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
        .deviceId(deviceId2) //
        .name(device2name) //
        .manufacturer(device2manuf) //
        .comment(device2comment) //
        .address(device2address) //
        .state(device2state) //
        .availableControls(device2controls) //
        .isRegistered(device2isReg) //
        .isGrouped(device2isGrp) //
        .groupId(device2grpId);

    String contr1descr2 = "new description";

    ControlRegistration contrReg = new ControlRegistration();
    contrReg.setName(contr1name);
    contrReg.setDescription(contr1descr2);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot register control";
    String expectedDetail = String.format("Device %s has no available controls.", deviceId1);

    // Mock(s)
    when(this.deviceRepository.findById(eq(deviceId1))).thenReturn(device1);
    when(this.deviceRepository.findById(eq(deviceId2))).thenReturn(device2);
    doReturn(Collections.emptyMap()).when(this.controlRepository).findAll();

    // Argument Captors
    ArgumentCaptor<DeviceControlInfo> arg1 = ArgumentCaptor.forClass(DeviceControlInfo.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg3 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call Service
    CommonAddResponse operResp = controlService.registerControl(deviceId1, contrReg);

    // Verify that appropriate Repository methods were NOT called
    verify(this.controlRepository, times(0)).add(arg1.capture());
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
  @DisplayName("TEST registerControl (control name is not in the device's available controls)")
  void testRegisterControlNameNotInDeviceAvailableControls() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID deviceId2 = UUID.fromString("00111112-2222-1111-1111-1111abcdef02");
    String device1name = "Shark - Test";
    String device2name = "Samsung - Test";
    String device1manuf = "Shark Inc.";
    String device2manuf = "Samsung Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device2comment = "Mini TV in the Bedroom";
    String device1address = "2607:f0d0:1002:31::1";
    String device2address = "2607:f0d0:1002:42::2";
    CommonDeviceState device1state = CommonDeviceState.OFF;
    CommonDeviceState device2state = CommonDeviceState.ON;
    Boolean device1isReg = true;
    Boolean device2isReg = true;
    Boolean device1isGrp = false;
    Boolean device2isGrp = false;
    UUID device1grpId = null;
    UUID device2grpId = null;

    String contr1name = "Name 1";
    String contr2name = "Name 2";
    String contr3name = "Name 3";
    String contr4name = "Name 4";
    String contr1descr = "Descr 1";
    String contr2descr = "Descr 2";
    String contr3descr = "Descr 3";
    String contr4descr = "Descr 4";
    String contr1val = "12.05";
    String contr4val = "15";

    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr1name) //
        .description(contr1descr) //
        .controlType(CommonControlType.DIRECT) //
        .value(contr1val) //
        .isManaged(false));
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr2name) //
        .description(contr2descr) //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    List<DeviceInfoAvailableControls> device2controls = new ArrayList<>();
    device2controls.add(new DeviceInfoAvailableControls() //
        .name(contr3name) //
        .description(contr3descr) //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device2controls.add(new DeviceInfoAvailableControls() //
        .name(contr4name) //
        .description(contr4descr) //
        .controlType(CommonControlType.DIRECT) //
        .value(contr4val) //
        .isManaged(false));

    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(deviceId1) //
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
        .deviceId(deviceId2) //
        .name(device2name) //
        .manufacturer(device2manuf) //
        .comment(device2comment) //
        .address(device2address) //
        .state(device2state) //
        .availableControls(device2controls) //
        .isRegistered(device2isReg) //
        .isGrouped(device2isGrp) //
        .groupId(device2grpId);

    String contr1name2 = "new name - X";
    String contr1descr2 = "new description";

    ControlRegistration contrReg = new ControlRegistration();
    contrReg.setName(contr1name2);
    contrReg.setDescription(contr1descr2);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot register control";
    String expectedDetail = String.format("Control %s is not inside Device %s available controls.",
        contr1name2, deviceId1);

    // Mock(s)
    when(this.deviceRepository.findById(eq(deviceId1))).thenReturn(device1);
    when(this.deviceRepository.findById(eq(deviceId2))).thenReturn(device2);
    doReturn(Collections.emptyMap()).when(this.controlRepository).findAll();

    // Argument Captors
    ArgumentCaptor<DeviceControlInfo> arg1 = ArgumentCaptor.forClass(DeviceControlInfo.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg3 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call Service
    CommonAddResponse operResp = controlService.registerControl(deviceId1, contrReg);

    // Verify that appropriate Repository methods were NOT called
    verify(this.controlRepository, times(0)).add(arg1.capture());
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
  @DisplayName("TEST registerControl (device's available control entry is marked as registered)")
  void testRegisterControlAvailableControlEntryMarkedAsRegistered() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID deviceId2 = UUID.fromString("00111112-2222-1111-1111-1111abcdef02");
    String device1name = "Shark - Test";
    String device2name = "Samsung - Test";
    String device1manuf = "Shark Inc.";
    String device2manuf = "Samsung Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device2comment = "Mini TV in the Bedroom";
    String device1address = "2607:f0d0:1002:31::1";
    String device2address = "2607:f0d0:1002:42::2";
    CommonDeviceState device1state = CommonDeviceState.OFF;
    CommonDeviceState device2state = CommonDeviceState.ON;
    Boolean device1isReg = true;
    Boolean device2isReg = true;
    Boolean device1isGrp = false;
    Boolean device2isGrp = false;
    UUID device1grpId = null;
    UUID device2grpId = null;

    String contr1name = "Name 1";
    String contr2name = "Name 2";
    String contr3name = "Name 3";
    String contr4name = "Name 4";
    String contr1descr = "Descr 1";
    String contr2descr = "Descr 2";
    String contr3descr = "Descr 3";
    String contr4descr = "Descr 4";
    String contr1val = "12.05";
    String contr4val = "15";

    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr1name) //
        .description(contr1descr) //
        .controlType(CommonControlType.DIRECT) //
        .value(contr1val) //
        .isManaged(true));
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr2name) //
        .description(contr2descr) //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    List<DeviceInfoAvailableControls> device2controls = new ArrayList<>();
    device2controls.add(new DeviceInfoAvailableControls() //
        .name(contr3name) //
        .description(contr3descr) //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device2controls.add(new DeviceInfoAvailableControls() //
        .name(contr4name) //
        .description(contr4descr) //
        .controlType(CommonControlType.DIRECT) //
        .value(contr4val) //
        .isManaged(false));

    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(deviceId1) //
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
        .deviceId(deviceId2) //
        .name(device2name) //
        .manufacturer(device2manuf) //
        .comment(device2comment) //
        .address(device2address) //
        .state(device2state) //
        .availableControls(device2controls) //
        .isRegistered(device2isReg) //
        .isGrouped(device2isGrp) //
        .groupId(device2grpId);

    String contr1descr2 = "new description";

    ControlRegistration contrReg = new ControlRegistration();
    contrReg.setName(contr1name);
    contrReg.setDescription(contr1descr2);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    when(this.deviceRepository.findById(eq(deviceId1))).thenReturn(device1);
    when(this.deviceRepository.findById(eq(deviceId2))).thenReturn(device2);
    doReturn(null).when(this.controlRepository).findAll();

    // Argument Captors
    ArgumentCaptor<DeviceControlInfo> arg1 = ArgumentCaptor.forClass(DeviceControlInfo.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg3 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call Service
    CommonAddResponse operResp = controlService.registerControl(deviceId1, contrReg);

    // Verify that appropriate Repository methods were NOT called
    verify(this.controlRepository, times(0)).add(arg1.capture());
    verify(this.deviceRepository, times(0)).update(arg2.capture(), arg3.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST registerControl (control is already registered)")
  void testRegisterControlControlAlreadyRegistered() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    UUID deviceId2 = UUID.fromString("00111112-2222-1111-1111-1111abcdef02");
    String device1name = "Shark - Test";
    String device2name = "Samsung - Test";
    String device1manuf = "Shark Inc.";
    String device2manuf = "Samsung Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device2comment = "Mini TV in the Bedroom";
    String device1address = "2607:f0d0:1002:31::1";
    String device2address = "2607:f0d0:1002:42::2";
    CommonDeviceState device1state = CommonDeviceState.OFF;
    CommonDeviceState device2state = CommonDeviceState.ON;
    Boolean device1isReg = true;
    Boolean device2isReg = true;
    Boolean device1isGrp = false;
    Boolean device2isGrp = false;
    UUID device1grpId = null;
    UUID device2grpId = null;

    UUID contr1id = UUID.fromString("00abcdef-1111-1111-1111-111111111101");
    UUID contr2id = UUID.fromString("00abcdef-2222-2222-2222-222222222202");
    UUID contr3id = UUID.fromString("00abcdef-3333-3333-3333-333333333303");
    UUID contr4id = UUID.fromString("00abcdef-4444-4444-4444-444444444404");
    UUID contr5id = UUID.fromString("00abcdef-5555-4444-4444-444444444405");
    String contr1name = "Name 1";
    String contr2name = "Name 2";
    String contr3name = "Name 3";
    String contr4name = "Name 4";
    String contr1descr = "Descr 1";
    String contr2descr = "Descr 2";
    String contr3descr = "Descr 3";
    String contr4descr = "Descr 4";
    String contr1val = "12.05";
    String contr2val = null;
    String contr3val = null;
    String contr4val = "15";

    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr1name) //
        .description(contr1descr) //
        .controlType(CommonControlType.DIRECT) //
        .value(contr1val) //
        .isManaged(false));
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr2name) //
        .description(contr2descr) //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    List<DeviceInfoAvailableControls> device2controls = new ArrayList<>();
    device2controls.add(new DeviceInfoAvailableControls() //
        .name(contr3name) //
        .description(contr3descr) //
        .controlType(CommonControlType.SLIDER) //
        .isManaged(false));
    device2controls.add(new DeviceInfoAvailableControls() //
        .name(contr4name) //
        .description(contr4descr) //
        .controlType(CommonControlType.DIRECT) //
        .value(contr4val) //
        .isManaged(false));

    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(deviceId1) //
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
        .deviceId(deviceId2) //
        .name(device2name) //
        .manufacturer(device2manuf) //
        .comment(device2comment) //
        .address(device2address) //
        .state(device2state) //
        .availableControls(device2controls) //
        .isRegistered(device2isReg) //
        .isGrouped(device2isGrp) //
        .groupId(device2grpId);

    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(contr1id) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);
    DeviceControlInfo contr2 = new DeviceControlInfo() //
        .deviceId(deviceId2) //
        .controlId(contr2id) //
        .name(contr2name) //
        .description(contr2descr) //
        .currentValue(contr2val);
    DeviceControlInfo contr3 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(contr3id) //
        .name(contr3name) //
        .description(contr3descr) //
        .currentValue(contr3val);
    DeviceControlInfo contr4 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(contr4id) //
        .name(contr4name) //
        .description(contr4descr) //
        .currentValue(contr4val);

    Map<UUID, DeviceControlInfo> contrMap = new HashMap<>();
    contrMap.put(contr5id, null); // abnormal; only to check the stream
    contrMap.put(contr2id, contr2);
    contrMap.put(contr3id, contr3);
    contrMap.put(contr4id, contr4);
    contrMap.put(contr1id, contr1);

    String contr1descr2 = "new description";

    ControlRegistration contrReg = new ControlRegistration();
    contrReg.setName(contr1name);
    contrReg.setDescription(contr1descr2);

    HttpStatus expectedStatus = HttpStatus.CONFLICT;

    // Mock(s)
    when(this.deviceRepository.findById(eq(deviceId1))).thenReturn(device1);
    when(this.deviceRepository.findById(eq(deviceId2))).thenReturn(device2);
    doReturn(contrMap).when(this.controlRepository).findAll();

    // Argument Captors
    ArgumentCaptor<DeviceControlInfo> arg1 = ArgumentCaptor.forClass(DeviceControlInfo.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg3 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call Service
    CommonAddResponse operResp = controlService.registerControl(deviceId1, contrReg);

    // Verify that appropriate Repository methods were NOT called
    verify(this.controlRepository, times(0)).add(arg1.capture());
    verify(this.deviceRepository, times(0)).update(arg2.capture(), arg3.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 409.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST setDirectControl")
  void testSetDirectControl() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    String device1name = "Shark - Test";
    String device1manuf = "Shark Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device1address = "2607:f0d0:1002:31::1";

    CommonDeviceState device1state = CommonDeviceState.OFF;
    Boolean device1isReg = true;
    Boolean device1isGrp = false;
    UUID device1grpId = null;

    UUID contr1id = UUID.fromString("00abcdef-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";

    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr1name) //
        .description(contr1descr) //
        .controlType(CommonControlType.DIRECT) //
        .value(contr1val) //
        .isManaged(false));

    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(deviceId1) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);

    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(contr1id) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    String contr1val2 = "15.69";
    DirectControlValue contrSet = new DirectControlValue();
    contrSet.setNewValue(contr1val2);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.deviceRepository.findById(eq(deviceId1))).thenReturn(device1);
    when(this.controlRepository.findById(eq(contr1id))).thenReturn(contr1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceControlInfo> arg2 = ArgumentCaptor.forClass(DeviceControlInfo.class);
    ArgumentCaptor<UUID> arg3 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DirectControlValue> arg4 = ArgumentCaptor.forClass(DirectControlValue.class);

    // Call Service
    CommonOperResponse operResp = controlService.setDirectControl(contr1id, contrSet);

    // Verify that appropriate Repository methods were called
    verify(this.controlRepository, times(1)).update(arg1.capture(), arg2.capture());
    verify(this.controlOperation, times(1)).setDirect(arg3.capture(), arg4.capture());

    // Assertions
    assertEquals(contr1id, arg1.getValue(), "Argument must match");
    assertNotNull(arg2.getValue(), "Argument (ControlInfo) must not be null");
    assertEquals(contr1id, arg2.getValue().getControlId(), "Argument's (controlId) must match");
    assertEquals(deviceId1, arg2.getValue().getDeviceId(), "Argument's (deviceId) must match");
    assertEquals(contr1name, arg2.getValue().getName(), "Argument's (name) must match");
    assertEquals(contr1descr, arg2.getValue().getDescription(),
        "Argument's (description) must match");
    assertEquals(contr1val2, arg2.getValue().getCurrentValue(), "Argument's (value) must match");
    assertEquals(contr1id, arg3.getValue(), "Argument must match");
    assertEquals(contrSet, arg4.getValue(), "Argument must match");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST setDirectControl (control does not exist)")
  void testSetDirectControlControlNotExist() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    String device1name = "Shark - Test";
    String device1manuf = "Shark Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device1address = "2607:f0d0:1002:31::1";

    CommonDeviceState device1state = CommonDeviceState.OFF;
    Boolean device1isReg = true;
    Boolean device1isGrp = false;
    UUID device1grpId = null;

    UUID contr1id = UUID.fromString("00abcdef-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";

    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr1name) //
        .description(contr1descr) //
        .controlType(CommonControlType.DIRECT) //
        .value(contr1val) //
        .isManaged(false));

    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(deviceId1) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);

    String contr1val2 = "15.69";
    DirectControlValue contrSet = new DirectControlValue();
    contrSet.setNewValue(contr1val2);

    HttpStatus expectedStatus = HttpStatus.NOT_FOUND;

    // Mock(s)
    when(this.deviceRepository.findById(eq(deviceId1))).thenReturn(device1);
    when(this.controlRepository.findById(eq(contr1id))).thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceControlInfo> arg2 = ArgumentCaptor.forClass(DeviceControlInfo.class);
    ArgumentCaptor<UUID> arg3 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DirectControlValue> arg4 = ArgumentCaptor.forClass(DirectControlValue.class);

    // Call Service
    CommonOperResponse operResp = controlService.setDirectControl(contr1id, contrSet);

    // Verify that appropriate Repository methods were NOT called
    verify(this.controlRepository, times(0)).update(arg1.capture(), arg2.capture());
    verify(this.controlOperation, times(0)).setDirect(arg3.capture(), arg4.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 404.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST setDirectControl (device does not exist)")
  void testSetDirectControlDeviceNotExist() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");

    UUID contr1id = UUID.fromString("00abcdef-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";

    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr1name) //
        .description(contr1descr) //
        .controlType(CommonControlType.DIRECT) //
        .value(contr1val) //
        .isManaged(false));

    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(contr1id) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    String contr1val2 = "15.69";
    DirectControlValue contrSet = new DirectControlValue();
    contrSet.setNewValue(contr1val2);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot perform operation on control";
    String expectedDetail = "The device associated with this control does not exist.";

    // Mock(s)
    when(this.deviceRepository.findById(eq(deviceId1))).thenReturn(null);
    when(this.controlRepository.findById(eq(contr1id))).thenReturn(contr1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceControlInfo> arg2 = ArgumentCaptor.forClass(DeviceControlInfo.class);
    ArgumentCaptor<UUID> arg3 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DirectControlValue> arg4 = ArgumentCaptor.forClass(DirectControlValue.class);

    // Call Service
    CommonOperResponse operResp = controlService.setDirectControl(contr1id, contrSet);

    // Verify that appropriate Repository methods were NOT called
    verify(this.controlRepository, times(0)).update(arg1.capture(), arg2.capture());
    verify(this.controlOperation, times(0)).setDirect(arg3.capture(), arg4.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 500.");
    assertNotNull(operResp.getErrorResult(), "There should be an error returned.");
    assertNotNull(operResp.getErrorResult().getLogId(), "Error object must have logId provided.");
    assertNotNull(operResp.getErrorResult().getTimestamp(),
        "Error object must have timestamp provided.");
    assertEquals(ErrorType.EXCEPTION, operResp.getErrorResult().getType(),
        "Error object must have type = Error");
    assertNotNull(operResp.getErrorResult().getType(), "Error object must have logId provided.");
    assertEquals(expectedErrMsg, operResp.getErrorResult().getMessage(),
        "Error Message must match.");
    assertEquals(expectedDetail, operResp.getErrorResult().getDetails(),
        "Error Details must match.");
  }

  @Test
  @DisplayName("TEST setDirectControl (device is not registered)")
  void testSetDirectControlDeviceUnregistered() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    String device1name = "Shark - Test";
    String device1manuf = "Shark Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device1address = "2607:f0d0:1002:31::1";

    CommonDeviceState device1state = CommonDeviceState.OFF;
    Boolean device1isReg = false;
    Boolean device1isGrp = false;
    UUID device1grpId = null;

    UUID contr1id = UUID.fromString("00abcdef-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";

    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr1name) //
        .description(contr1descr) //
        .controlType(CommonControlType.DIRECT) //
        .value(contr1val) //
        .isManaged(false));

    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(deviceId1) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);

    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(contr1id) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    String contr1val2 = "15.69";
    DirectControlValue contrSet = new DirectControlValue();
    contrSet.setNewValue(contr1val2);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot perform operation on control";
    String expectedDetail = "The device associated with this control is not registered.";

    // Mock(s)
    when(this.deviceRepository.findById(eq(deviceId1))).thenReturn(device1);
    when(this.controlRepository.findById(eq(contr1id))).thenReturn(contr1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceControlInfo> arg2 = ArgumentCaptor.forClass(DeviceControlInfo.class);
    ArgumentCaptor<UUID> arg3 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DirectControlValue> arg4 = ArgumentCaptor.forClass(DirectControlValue.class);

    // Call Service
    CommonOperResponse operResp = controlService.setDirectControl(contr1id, contrSet);

    // Verify that appropriate Repository methods were NOT called
    verify(this.controlRepository, times(0)).update(arg1.capture(), arg2.capture());
    verify(this.controlOperation, times(0)).setDirect(arg3.capture(), arg4.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 500.");
    assertNotNull(operResp.getErrorResult(), "There should be an error returned.");
    assertNotNull(operResp.getErrorResult().getLogId(), "Error object must have logId provided.");
    assertNotNull(operResp.getErrorResult().getTimestamp(),
        "Error object must have timestamp provided.");
    assertEquals(ErrorType.EXCEPTION, operResp.getErrorResult().getType(),
        "Error object must have type = Error");
    assertNotNull(operResp.getErrorResult().getType(), "Error object must have logId provided.");
    assertEquals(expectedErrMsg, operResp.getErrorResult().getMessage(),
        "Error Message must match.");
    assertEquals(expectedDetail, operResp.getErrorResult().getDetails(),
        "Error Details must match.");
  }

  @Test
  @DisplayName("TEST setDirectControl (Control's deviceId is null)")
  void testSetDirectControlDeviceIdNull() {
    UUID contr1id = UUID.fromString("00abcdef-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";

    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr1name) //
        .description(contr1descr) //
        .controlType(CommonControlType.DIRECT) //
        .value(contr1val) //
        .isManaged(false));

    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(null) //
        .controlId(contr1id) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    String contr1val2 = "15.69";
    DirectControlValue contrSet = new DirectControlValue();
    contrSet.setNewValue(contr1val2);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot perform operation on control";
    String expectedDetail = "There is no deviceId associated with this control.";

    // Mock(s)
    when(this.controlRepository.findById(eq(contr1id))).thenReturn(contr1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceControlInfo> arg2 = ArgumentCaptor.forClass(DeviceControlInfo.class);
    ArgumentCaptor<UUID> arg3 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DirectControlValue> arg4 = ArgumentCaptor.forClass(DirectControlValue.class);

    // Call Service
    CommonOperResponse operResp = controlService.setDirectControl(contr1id, contrSet);

    // Verify that appropriate Repository methods were NOT called
    verify(this.controlRepository, times(0)).update(arg1.capture(), arg2.capture());
    verify(this.controlOperation, times(0)).setDirect(arg3.capture(), arg4.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 500.");
    assertNotNull(operResp.getErrorResult(), "There should be an error returned.");
    assertNotNull(operResp.getErrorResult().getLogId(), "Error object must have logId provided.");
    assertNotNull(operResp.getErrorResult().getTimestamp(),
        "Error object must have timestamp provided.");
    assertEquals(ErrorType.EXCEPTION, operResp.getErrorResult().getType(),
        "Error object must have type = Error");
    assertNotNull(operResp.getErrorResult().getType(), "Error object must have logId provided.");
    assertEquals(expectedErrMsg, operResp.getErrorResult().getMessage(),
        "Error Message must match.");
    assertEquals(expectedDetail, operResp.getErrorResult().getDetails(),
        "Error Details must match.");
  }

  @Test
  @DisplayName("TEST setSliderControl")
  void testSetSliderControl() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    String device1name = "Shark - Test";
    String device1manuf = "Shark Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device1address = "2607:f0d0:1002:31::1";

    CommonDeviceState device1state = CommonDeviceState.OFF;
    Boolean device1isReg = true;
    Boolean device1isGrp = false;
    UUID device1grpId = null;

    UUID contr1id = UUID.fromString("00abcdef-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";

    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr1name) //
        .description(contr1descr) //
        .controlType(CommonControlType.DIRECT) //
        .value(contr1val) //
        .isManaged(false));

    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(deviceId1) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);

    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(contr1id) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonSliderDirection direction = CommonSliderDirection.DOWN;
    SliderControlValue contrSet = new SliderControlValue();
    contrSet.setDirection(direction);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.deviceRepository.findById(eq(deviceId1))).thenReturn(device1);
    when(this.controlRepository.findById(eq(contr1id))).thenReturn(contr1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceControlInfo> arg2 = ArgumentCaptor.forClass(DeviceControlInfo.class);
    ArgumentCaptor<UUID> arg3 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<SliderControlValue> arg4 = ArgumentCaptor.forClass(SliderControlValue.class);

    // Call Service
    CommonOperResponse operResp = controlService.setSliderControl(contr1id, contrSet);

    // Verify that appropriate Repository method was called
    verify(this.controlOperation, times(1)).setSlider(arg3.capture(), arg4.capture());

    // Verify that appropriate Repository method was NOT called
    verify(this.controlRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertEquals(contr1id, arg3.getValue(), "Argument must match");
    assertEquals(contrSet, arg4.getValue(), "Argument must match");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST setSliderControl (control does not exist)")
  void testsetSliderControlControlNotExist() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    String device1name = "Shark - Test";
    String device1manuf = "Shark Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device1address = "2607:f0d0:1002:31::1";

    CommonDeviceState device1state = CommonDeviceState.OFF;
    Boolean device1isReg = true;
    Boolean device1isGrp = false;
    UUID device1grpId = null;

    UUID contr1id = UUID.fromString("00abcdef-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";

    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr1name) //
        .description(contr1descr) //
        .controlType(CommonControlType.DIRECT) //
        .value(contr1val) //
        .isManaged(false));

    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(deviceId1) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);

    CommonSliderDirection direction = CommonSliderDirection.DOWN;
    SliderControlValue contrSet = new SliderControlValue();
    contrSet.setDirection(direction);

    HttpStatus expectedStatus = HttpStatus.NOT_FOUND;

    // Mock(s)
    when(this.deviceRepository.findById(eq(deviceId1))).thenReturn(device1);
    when(this.controlRepository.findById(eq(contr1id))).thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceControlInfo> arg2 = ArgumentCaptor.forClass(DeviceControlInfo.class);
    ArgumentCaptor<UUID> arg3 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<SliderControlValue> arg4 = ArgumentCaptor.forClass(SliderControlValue.class);

    // Call Service
    CommonOperResponse operResp = controlService.setSliderControl(contr1id, contrSet);

    // Verify that appropriate Repository methods were NOT called
    verify(this.controlRepository, times(0)).update(arg1.capture(), arg2.capture());
    verify(this.controlOperation, times(0)).setSlider(arg3.capture(), arg4.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 404.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST setSliderControl (device does not exist)")
  void testsetSliderControlDeviceNotExist() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");

    UUID contr1id = UUID.fromString("00abcdef-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";

    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr1name) //
        .description(contr1descr) //
        .controlType(CommonControlType.DIRECT) //
        .value(contr1val) //
        .isManaged(false));

    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(contr1id) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonSliderDirection direction = CommonSliderDirection.DOWN;
    SliderControlValue contrSet = new SliderControlValue();
    contrSet.setDirection(direction);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot perform operation on control";
    String expectedDetail = "The device associated with this control does not exist.";

    // Mock(s)
    when(this.deviceRepository.findById(eq(deviceId1))).thenReturn(null);
    when(this.controlRepository.findById(eq(contr1id))).thenReturn(contr1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceControlInfo> arg2 = ArgumentCaptor.forClass(DeviceControlInfo.class);
    ArgumentCaptor<UUID> arg3 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<SliderControlValue> arg4 = ArgumentCaptor.forClass(SliderControlValue.class);

    // Call Service
    CommonOperResponse operResp = controlService.setSliderControl(contr1id, contrSet);

    // Verify that appropriate Repository methods were NOT called
    verify(this.controlRepository, times(0)).update(arg1.capture(), arg2.capture());
    verify(this.controlOperation, times(0)).setSlider(arg3.capture(), arg4.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 500.");
    assertNotNull(operResp.getErrorResult(), "There should be an error returned.");
    assertNotNull(operResp.getErrorResult().getLogId(), "Error object must have logId provided.");
    assertNotNull(operResp.getErrorResult().getTimestamp(),
        "Error object must have timestamp provided.");
    assertEquals(ErrorType.EXCEPTION, operResp.getErrorResult().getType(),
        "Error object must have type = Error");
    assertNotNull(operResp.getErrorResult().getType(), "Error object must have logId provided.");
    assertEquals(expectedErrMsg, operResp.getErrorResult().getMessage(),
        "Error Message must match.");
    assertEquals(expectedDetail, operResp.getErrorResult().getDetails(),
        "Error Details must match.");
  }

  @Test
  @DisplayName("TEST setSliderControl (device is not registered)")
  void testsetSliderControlDeviceUnregistered() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    String device1name = "Shark - Test";
    String device1manuf = "Shark Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device1address = "2607:f0d0:1002:31::1";

    CommonDeviceState device1state = CommonDeviceState.OFF;
    Boolean device1isReg = false;
    Boolean device1isGrp = false;
    UUID device1grpId = null;

    UUID contr1id = UUID.fromString("00abcdef-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";

    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr1name) //
        .description(contr1descr) //
        .controlType(CommonControlType.DIRECT) //
        .value(contr1val) //
        .isManaged(false));

    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(deviceId1) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);

    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(contr1id) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonSliderDirection direction = CommonSliderDirection.DOWN;
    SliderControlValue contrSet = new SliderControlValue();
    contrSet.setDirection(direction);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot perform operation on control";
    String expectedDetail = "The device associated with this control is not registered.";

    // Mock(s)
    when(this.deviceRepository.findById(eq(deviceId1))).thenReturn(device1);
    when(this.controlRepository.findById(eq(contr1id))).thenReturn(contr1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceControlInfo> arg2 = ArgumentCaptor.forClass(DeviceControlInfo.class);
    ArgumentCaptor<UUID> arg3 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<SliderControlValue> arg4 = ArgumentCaptor.forClass(SliderControlValue.class);

    // Call Service
    CommonOperResponse operResp = controlService.setSliderControl(contr1id, contrSet);

    // Verify that appropriate Repository methods were NOT called
    verify(this.controlRepository, times(0)).update(arg1.capture(), arg2.capture());
    verify(this.controlOperation, times(0)).setSlider(arg3.capture(), arg4.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 500.");
    assertNotNull(operResp.getErrorResult(), "There should be an error returned.");
    assertNotNull(operResp.getErrorResult().getLogId(), "Error object must have logId provided.");
    assertNotNull(operResp.getErrorResult().getTimestamp(),
        "Error object must have timestamp provided.");
    assertEquals(ErrorType.EXCEPTION, operResp.getErrorResult().getType(),
        "Error object must have type = Error");
    assertNotNull(operResp.getErrorResult().getType(), "Error object must have logId provided.");
    assertEquals(expectedErrMsg, operResp.getErrorResult().getMessage(),
        "Error Message must match.");
    assertEquals(expectedDetail, operResp.getErrorResult().getDetails(),
        "Error Details must match.");
  }

  @Test
  @DisplayName("TEST setSliderControl (Control's deviceId is null)")
  void testsetSliderControlDeviceIdNull() {
    UUID contr1id = UUID.fromString("00abcdef-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";

    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr1name) //
        .description(contr1descr) //
        .controlType(CommonControlType.DIRECT) //
        .value(contr1val) //
        .isManaged(false));

    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(null) //
        .controlId(contr1id) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    CommonSliderDirection direction = CommonSliderDirection.DOWN;
    SliderControlValue contrSet = new SliderControlValue();
    contrSet.setDirection(direction);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot perform operation on control";
    String expectedDetail = "There is no deviceId associated with this control.";

    // Mock(s)
    when(this.controlRepository.findById(eq(contr1id))).thenReturn(contr1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceControlInfo> arg2 = ArgumentCaptor.forClass(DeviceControlInfo.class);
    ArgumentCaptor<UUID> arg3 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<SliderControlValue> arg4 = ArgumentCaptor.forClass(SliderControlValue.class);

    // Call Service
    CommonOperResponse operResp = controlService.setSliderControl(contr1id, contrSet);

    // Verify that appropriate Repository methods were NOT called
    verify(this.controlRepository, times(0)).update(arg1.capture(), arg2.capture());
    verify(this.controlOperation, times(0)).setSlider(arg3.capture(), arg4.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 500.");
    assertNotNull(operResp.getErrorResult(), "There should be an error returned.");
    assertNotNull(operResp.getErrorResult().getLogId(), "Error object must have logId provided.");
    assertNotNull(operResp.getErrorResult().getTimestamp(),
        "Error object must have timestamp provided.");
    assertEquals(ErrorType.EXCEPTION, operResp.getErrorResult().getType(),
        "Error object must have type = Error");
    assertNotNull(operResp.getErrorResult().getType(), "Error object must have logId provided.");
    assertEquals(expectedErrMsg, operResp.getErrorResult().getMessage(),
        "Error Message must match.");
    assertEquals(expectedDetail, operResp.getErrorResult().getDetails(),
        "Error Details must match.");
  }

  @Test
  @DisplayName("TEST setToggleControl")
  void testsetToggleControl() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    String device1name = "Shark - Test";
    String device1manuf = "Shark Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device1address = "2607:f0d0:1002:31::1";

    CommonDeviceState device1state = CommonDeviceState.OFF;
    Boolean device1isReg = true;
    Boolean device1isGrp = false;
    UUID device1grpId = null;

    UUID contr1id = UUID.fromString("00abcdef-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";

    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr1name) //
        .description(contr1descr) //
        .controlType(CommonControlType.DIRECT) //
        .value(contr1val) //
        .isManaged(false));

    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(deviceId1) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);

    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(contr1id) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.deviceRepository.findById(eq(deviceId1))).thenReturn(device1);
    when(this.controlRepository.findById(eq(contr1id))).thenReturn(contr1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceControlInfo> arg2 = ArgumentCaptor.forClass(DeviceControlInfo.class);
    ArgumentCaptor<UUID> arg3 = ArgumentCaptor.forClass(UUID.class);

    // Call Service
    CommonOperResponse operResp = controlService.setToggleControl(contr1id);

    // Verify that appropriate Repository method was called
    verify(this.controlOperation, times(1)).setToggle(arg3.capture());

    // Verify that appropriate Repository method was NOT called
    verify(this.controlRepository, times(0)).update(arg1.capture(), arg2.capture());

    // Assertions
    assertEquals(contr1id, arg3.getValue(), "Argument must match");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST setToggleControl (control does not exist)")
  void testsetToggleControlControlNotExist() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    String device1name = "Shark - Test";
    String device1manuf = "Shark Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device1address = "2607:f0d0:1002:31::1";

    CommonDeviceState device1state = CommonDeviceState.OFF;
    Boolean device1isReg = true;
    Boolean device1isGrp = false;
    UUID device1grpId = null;

    UUID contr1id = UUID.fromString("00abcdef-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";

    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr1name) //
        .description(contr1descr) //
        .controlType(CommonControlType.DIRECT) //
        .value(contr1val) //
        .isManaged(false));

    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(deviceId1) //
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
    when(this.deviceRepository.findById(eq(deviceId1))).thenReturn(device1);
    when(this.controlRepository.findById(eq(contr1id))).thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceControlInfo> arg2 = ArgumentCaptor.forClass(DeviceControlInfo.class);
    ArgumentCaptor<UUID> arg3 = ArgumentCaptor.forClass(UUID.class);

    // Call Service
    CommonOperResponse operResp = controlService.setToggleControl(contr1id);

    // Verify that appropriate Repository methods were NOT called
    verify(this.controlRepository, times(0)).update(arg1.capture(), arg2.capture());
    verify(this.controlOperation, times(0)).setToggle(arg3.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 404.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST setToggleControl (device does not exist)")
  void testsetToggleControlDeviceNotExist() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");

    UUID contr1id = UUID.fromString("00abcdef-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";

    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr1name) //
        .description(contr1descr) //
        .controlType(CommonControlType.DIRECT) //
        .value(contr1val) //
        .isManaged(false));

    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(contr1id) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot perform operation on control";
    String expectedDetail = "The device associated with this control does not exist.";

    // Mock(s)
    when(this.deviceRepository.findById(eq(deviceId1))).thenReturn(null);
    when(this.controlRepository.findById(eq(contr1id))).thenReturn(contr1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceControlInfo> arg2 = ArgumentCaptor.forClass(DeviceControlInfo.class);
    ArgumentCaptor<UUID> arg3 = ArgumentCaptor.forClass(UUID.class);

    // Call Service
    CommonOperResponse operResp = controlService.setToggleControl(contr1id);

    // Verify that appropriate Repository methods were NOT called
    verify(this.controlRepository, times(0)).update(arg1.capture(), arg2.capture());
    verify(this.controlOperation, times(0)).setToggle(arg3.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 500.");
    assertNotNull(operResp.getErrorResult(), "There should be an error returned.");
    assertNotNull(operResp.getErrorResult().getLogId(), "Error object must have logId provided.");
    assertNotNull(operResp.getErrorResult().getTimestamp(),
        "Error object must have timestamp provided.");
    assertEquals(ErrorType.EXCEPTION, operResp.getErrorResult().getType(),
        "Error object must have type = Error");
    assertNotNull(operResp.getErrorResult().getType(), "Error object must have logId provided.");
    assertEquals(expectedErrMsg, operResp.getErrorResult().getMessage(),
        "Error Message must match.");
    assertEquals(expectedDetail, operResp.getErrorResult().getDetails(),
        "Error Details must match.");
  }

  @Test
  @DisplayName("TEST setToggleControl (device is not registered)")
  void testsetToggleControlDeviceUnregistered() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    String device1name = "Shark - Test";
    String device1manuf = "Shark Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device1address = "2607:f0d0:1002:31::1";

    CommonDeviceState device1state = CommonDeviceState.OFF;
    Boolean device1isReg = false;
    Boolean device1isGrp = false;
    UUID device1grpId = null;

    UUID contr1id = UUID.fromString("00abcdef-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";

    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr1name) //
        .description(contr1descr) //
        .controlType(CommonControlType.DIRECT) //
        .value(contr1val) //
        .isManaged(false));

    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(deviceId1) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);

    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(contr1id) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot perform operation on control";
    String expectedDetail = "The device associated with this control is not registered.";

    // Mock(s)
    when(this.deviceRepository.findById(eq(deviceId1))).thenReturn(device1);
    when(this.controlRepository.findById(eq(contr1id))).thenReturn(contr1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceControlInfo> arg2 = ArgumentCaptor.forClass(DeviceControlInfo.class);
    ArgumentCaptor<UUID> arg3 = ArgumentCaptor.forClass(UUID.class);

    // Call Service
    CommonOperResponse operResp = controlService.setToggleControl(contr1id);

    // Verify that appropriate Repository methods were NOT called
    verify(this.controlRepository, times(0)).update(arg1.capture(), arg2.capture());
    verify(this.controlOperation, times(0)).setToggle(arg3.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 500.");
    assertNotNull(operResp.getErrorResult(), "There should be an error returned.");
    assertNotNull(operResp.getErrorResult().getLogId(), "Error object must have logId provided.");
    assertNotNull(operResp.getErrorResult().getTimestamp(),
        "Error object must have timestamp provided.");
    assertEquals(ErrorType.EXCEPTION, operResp.getErrorResult().getType(),
        "Error object must have type = Error");
    assertNotNull(operResp.getErrorResult().getType(), "Error object must have logId provided.");
    assertEquals(expectedErrMsg, operResp.getErrorResult().getMessage(),
        "Error Message must match.");
    assertEquals(expectedDetail, operResp.getErrorResult().getDetails(),
        "Error Details must match.");
  }

  @Test
  @DisplayName("TEST setToggleControl (Control's deviceId is null)")
  void testsetToggleControlDeviceIdNull() {
    UUID contr1id = UUID.fromString("00abcdef-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";

    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr1name) //
        .description(contr1descr) //
        .controlType(CommonControlType.DIRECT) //
        .value(contr1val) //
        .isManaged(false));

    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(null) //
        .controlId(contr1id) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot perform operation on control";
    String expectedDetail = "There is no deviceId associated with this control.";

    // Mock(s)
    when(this.controlRepository.findById(eq(contr1id))).thenReturn(contr1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceControlInfo> arg2 = ArgumentCaptor.forClass(DeviceControlInfo.class);
    ArgumentCaptor<UUID> arg3 = ArgumentCaptor.forClass(UUID.class);

    // Call Service
    CommonOperResponse operResp = controlService.setToggleControl(contr1id);

    // Verify that appropriate Repository methods were NOT called
    verify(this.controlRepository, times(0)).update(arg1.capture(), arg2.capture());
    verify(this.controlOperation, times(0)).setToggle(arg3.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 500.");
    assertNotNull(operResp.getErrorResult(), "There should be an error returned.");
    assertNotNull(operResp.getErrorResult().getLogId(), "Error object must have logId provided.");
    assertNotNull(operResp.getErrorResult().getTimestamp(),
        "Error object must have timestamp provided.");
    assertEquals(ErrorType.EXCEPTION, operResp.getErrorResult().getType(),
        "Error object must have type = Error");
    assertNotNull(operResp.getErrorResult().getType(), "Error object must have logId provided.");
    assertEquals(expectedErrMsg, operResp.getErrorResult().getMessage(),
        "Error Message must match.");
    assertEquals(expectedDetail, operResp.getErrorResult().getDetails(),
        "Error Details must match.");
  }

  @Test
  @DisplayName("TEST modifyControl")
  void testModifyControl() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    String device1name = "Shark - Test";
    String device1manuf = "Shark Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device1address = "2607:f0d0:1002:31::1";

    CommonDeviceState device1state = CommonDeviceState.OFF;
    Boolean device1isReg = true;
    Boolean device1isGrp = false;
    UUID device1grpId = null;

    UUID contr1id = UUID.fromString("00abcdef-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    CommonControlType contr1type = CommonControlType.DIRECT;
    String contr1val = "12.05";

    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr1name) //
        .description(contr1descr) //
        .controlType(contr1type) //
        .value(contr1val) //
        .isManaged(true));

    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(deviceId1) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);

    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(contr1id) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    String contr1descr2 = "Modded Descr - 2";
    ControlModification contrMod = new ControlModification();
    contrMod.setDescription(contr1descr2);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.deviceRepository.findById(eq(deviceId1))).thenReturn(device1);
    when(this.controlRepository.findById(eq(contr1id))).thenReturn(contr1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceControlInfo> arg2 = ArgumentCaptor.forClass(DeviceControlInfo.class);
    ArgumentCaptor<UUID> arg3 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg4 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call Service
    CommonOperResponse operResp = controlService.modifyControl(contr1id, contrMod);

    // Verify that appropriate Repository methods were called
    verify(this.controlRepository, times(1)).update(arg1.capture(), arg2.capture());
    verify(this.deviceRepository, times(1)).update(arg3.capture(), arg4.capture());

    // Assertions
    assertEquals(contr1id, arg1.getValue(), "Argument must match");
    assertNotNull(arg2.getValue(), "Argument (ControlInfo) must not be null");
    assertEquals(contr1id, arg2.getValue().getControlId(), "Argument's (controlId) must match");
    assertEquals(deviceId1, arg2.getValue().getDeviceId(), "Argument's (deviceId) must match");
    assertEquals(contr1name, arg2.getValue().getName(), "Argument's (name) must match");
    assertEquals(contr1descr2, arg2.getValue().getDescription(),
        "Argument's (description) must match");
    assertEquals(deviceId1, arg3.getValue(), "Argument must match");
    assertNotNull(arg4.getValue(), "Argument (DeviceInfo) must not be null");
    assertEquals(deviceId1, arg4.getValue().getDeviceId(), "Argument's (deviceId) must match");
    assertEquals(device1name, arg4.getValue().getName(), "Argument's (name) must match");
    assertEquals(device1manuf, arg4.getValue().getManufacturer(),
        "Argument's (manufacturer) must match");
    assertEquals(device1comment, arg4.getValue().getComment(), "Argument's (comment) must match");
    assertEquals(device1address, arg4.getValue().getAddress(), "Argument's (address) must match");
    assertEquals(device1state, arg4.getValue().getState(), "Argument's (state) must match");
    assertEquals(contr1name, arg4.getValue().getAvailableControls().get(0).getName(),
        "Argument's (available controls - name) must match");
    assertEquals(contr1descr2, arg4.getValue().getAvailableControls().get(0).getDescription(),
        "Argument's (available controls - description) must match");
    assertEquals(contr1type, arg4.getValue().getAvailableControls().get(0).getControlType(),
        "Argument's (available controls - type) must match");
    assertEquals(contr1val, arg4.getValue().getAvailableControls().get(0).getValue(),
        "Argument's (available controls - value) must match");
    assertTrue(arg4.getValue().getAvailableControls().get(0).isIsManaged(),
        "Argument's (available controls - isManaged) must be true");
    assertEquals(device1isReg, arg4.getValue().isIsRegistered(),
        "Argument's (isRegistered) must match");
    assertEquals(device1isGrp, arg4.getValue().isIsGrouped(), "Argument's (isGrouped) must match");
    assertEquals(device1grpId, arg4.getValue().getGroupId(), "Argument's (groupId) must match");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyControl (control does not exist)")
  void testModifyControlControlNotExist() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    String device1name = "Shark - Test";
    String device1manuf = "Shark Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device1address = "2607:f0d0:1002:31::1";

    CommonDeviceState device1state = CommonDeviceState.OFF;
    Boolean device1isReg = true;
    Boolean device1isGrp = false;
    UUID device1grpId = null;

    UUID contr1id = UUID.fromString("00abcdef-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";

    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr1name) //
        .description(contr1descr) //
        .controlType(CommonControlType.DIRECT) //
        .value(contr1val) //
        .isManaged(true));

    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(deviceId1) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);

    String contr1descr2 = "Modded Descr - 2";
    ControlModification contrMod = new ControlModification();
    contrMod.setDescription(contr1descr2);

    HttpStatus expectedStatus = HttpStatus.NOT_FOUND;

    // Mock(s)
    when(this.deviceRepository.findById(eq(deviceId1))).thenReturn(device1);
    when(this.controlRepository.findById(eq(contr1id))).thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceControlInfo> arg2 = ArgumentCaptor.forClass(DeviceControlInfo.class);
    ArgumentCaptor<UUID> arg3 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg4 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call Service
    CommonOperResponse operResp = controlService.modifyControl(contr1id, contrMod);

    // Verify that appropriate Repository methods were NOT called
    verify(this.controlRepository, times(0)).update(arg1.capture(), arg2.capture());
    verify(this.deviceRepository, times(0)).update(arg3.capture(), arg4.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 404.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST modifyControl (device does not exist)")
  void testModifyControlDeviceNotExist() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");

    UUID contr1id = UUID.fromString("00abcdef-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";

    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr1name) //
        .description(contr1descr) //
        .controlType(CommonControlType.DIRECT) //
        .value(contr1val) //
        .isManaged(true));

    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(contr1id) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    String contr1descr2 = "Modded Descr - 2";
    ControlModification contrMod = new ControlModification();
    contrMod.setDescription(contr1descr2);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot modify control";
    String expectedDetail = "The device associated with this control does not exist.";

    // Mock(s)
    when(this.deviceRepository.findById(eq(deviceId1))).thenReturn(null);
    when(this.controlRepository.findById(eq(contr1id))).thenReturn(contr1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceControlInfo> arg2 = ArgumentCaptor.forClass(DeviceControlInfo.class);
    ArgumentCaptor<UUID> arg3 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg4 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call Service
    CommonOperResponse operResp = controlService.modifyControl(contr1id, contrMod);

    // Verify that appropriate Repository methods were NOT called
    verify(this.controlRepository, times(0)).update(arg1.capture(), arg2.capture());
    verify(this.deviceRepository, times(0)).update(arg3.capture(), arg4.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 500.");
    assertNotNull(operResp.getErrorResult(), "There should be an error returned.");
    assertNotNull(operResp.getErrorResult().getLogId(), "Error object must have logId provided.");
    assertNotNull(operResp.getErrorResult().getTimestamp(),
        "Error object must have timestamp provided.");
    assertEquals(ErrorType.EXCEPTION, operResp.getErrorResult().getType(),
        "Error object must have type = Error");
    assertNotNull(operResp.getErrorResult().getType(), "Error object must have logId provided.");
    assertEquals(expectedErrMsg, operResp.getErrorResult().getMessage(),
        "Error Message must match.");
    assertEquals(expectedDetail, operResp.getErrorResult().getDetails(),
        "Error Details must match.");
  }

  @Test
  @DisplayName("TEST modifyControl (device is not registered)")
  void testModifyControlDeviceUnregistered() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    String device1name = "Shark - Test";
    String device1manuf = "Shark Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device1address = "2607:f0d0:1002:31::1";

    CommonDeviceState device1state = CommonDeviceState.OFF;
    Boolean device1isReg = false;
    Boolean device1isGrp = false;
    UUID device1grpId = null;

    UUID contr1id = UUID.fromString("00abcdef-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";

    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr1name) //
        .description(contr1descr) //
        .controlType(CommonControlType.DIRECT) //
        .value(contr1val) //
        .isManaged(true));

    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(deviceId1) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);

    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(contr1id) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    String contr1descr2 = "Modded Descr - 2";
    ControlModification contrMod = new ControlModification();
    contrMod.setDescription(contr1descr2);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot modify control";
    String expectedDetail = "The device associated with this control is not registered.";

    // Mock(s)
    when(this.deviceRepository.findById(eq(deviceId1))).thenReturn(device1);
    when(this.controlRepository.findById(eq(contr1id))).thenReturn(contr1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceControlInfo> arg2 = ArgumentCaptor.forClass(DeviceControlInfo.class);
    ArgumentCaptor<UUID> arg3 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg4 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call Service
    CommonOperResponse operResp = controlService.modifyControl(contr1id, contrMod);

    // Verify that appropriate Repository methods were NOT called
    verify(this.controlRepository, times(0)).update(arg1.capture(), arg2.capture());
    verify(this.deviceRepository, times(0)).update(arg3.capture(), arg4.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 500.");
    assertNotNull(operResp.getErrorResult(), "There should be an error returned.");
    assertNotNull(operResp.getErrorResult().getLogId(), "Error object must have logId provided.");
    assertNotNull(operResp.getErrorResult().getTimestamp(),
        "Error object must have timestamp provided.");
    assertEquals(ErrorType.EXCEPTION, operResp.getErrorResult().getType(),
        "Error object must have type = Error");
    assertNotNull(operResp.getErrorResult().getType(), "Error object must have logId provided.");
    assertEquals(expectedErrMsg, operResp.getErrorResult().getMessage(),
        "Error Message must match.");
    assertEquals(expectedDetail, operResp.getErrorResult().getDetails(),
        "Error Details must match.");
  }

  @Test
  @DisplayName("TEST modifyControl (Control's deviceId is null)")
  void testModifyControlDeviceIdNull() {
    UUID contr1id = UUID.fromString("00abcdef-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";

    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr1name) //
        .description(contr1descr) //
        .controlType(CommonControlType.DIRECT) //
        .value(contr1val) //
        .isManaged(true));

    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(null) //
        .controlId(contr1id) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    String contr1descr2 = "Modded Descr - 2";
    ControlModification contrMod = new ControlModification();
    contrMod.setDescription(contr1descr2);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot modify control";
    String expectedDetail = "There is no deviceId associated with this control.";

    // Mock(s)
    when(this.controlRepository.findById(eq(contr1id))).thenReturn(contr1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceControlInfo> arg2 = ArgumentCaptor.forClass(DeviceControlInfo.class);
    ArgumentCaptor<UUID> arg3 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg4 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call Service
    CommonOperResponse operResp = controlService.modifyControl(contr1id, contrMod);

    // Verify that appropriate Repository methods were NOT called
    verify(this.controlRepository, times(0)).update(arg1.capture(), arg2.capture());
    verify(this.deviceRepository, times(0)).update(arg3.capture(), arg4.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 500.");
    assertNotNull(operResp.getErrorResult(), "There should be an error returned.");
    assertNotNull(operResp.getErrorResult().getLogId(), "Error object must have logId provided.");
    assertNotNull(operResp.getErrorResult().getTimestamp(),
        "Error object must have timestamp provided.");
    assertEquals(ErrorType.EXCEPTION, operResp.getErrorResult().getType(),
        "Error object must have type = Error");
    assertNotNull(operResp.getErrorResult().getType(), "Error object must have logId provided.");
    assertEquals(expectedErrMsg, operResp.getErrorResult().getMessage(),
        "Error Message must match.");
    assertEquals(expectedDetail, operResp.getErrorResult().getDetails(),
        "Error Details must match.");
  }

  @Test
  @DisplayName("TEST modifyControl (device has no available controls(null))")
  void testModifyControlDeviceHasNoAvailControlsNull() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    String device1name = "Shark - Test";
    String device1manuf = "Shark Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device1address = "2607:f0d0:1002:31::1";

    CommonDeviceState device1state = CommonDeviceState.OFF;
    Boolean device1isReg = true;
    Boolean device1isGrp = false;
    UUID device1grpId = null;

    UUID contr1id = UUID.fromString("00abcdef-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";

    List<DeviceInfoAvailableControls> device1controls = null;

    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(deviceId1) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);

    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(contr1id) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    String contr1descr2 = "Modded Descr - 2";
    ControlModification contrMod = new ControlModification();
    contrMod.setDescription(contr1descr2);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot modify control";
    String expectedDetail = String.format("Device %s has no available controls.", deviceId1);

    // Mock(s)
    when(this.deviceRepository.findById(eq(deviceId1))).thenReturn(device1);
    when(this.controlRepository.findById(eq(contr1id))).thenReturn(contr1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceControlInfo> arg2 = ArgumentCaptor.forClass(DeviceControlInfo.class);
    ArgumentCaptor<UUID> arg3 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg4 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call Service
    CommonOperResponse operResp = controlService.modifyControl(contr1id, contrMod);

    // Verify that appropriate Repository methods were NOT called
    verify(this.controlRepository, times(0)).update(arg1.capture(), arg2.capture());
    verify(this.deviceRepository, times(0)).update(arg3.capture(), arg4.capture());

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
  @DisplayName("TEST modifyControl (device has no available controls)")
  void testModifyControlDeviceHasNoAvailControls() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    String device1name = "Shark - Test";
    String device1manuf = "Shark Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device1address = "2607:f0d0:1002:31::1";

    CommonDeviceState device1state = CommonDeviceState.OFF;
    Boolean device1isReg = true;
    Boolean device1isGrp = false;
    UUID device1grpId = null;

    UUID contr1id = UUID.fromString("00abcdef-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";

    List<DeviceInfoAvailableControls> device1controls = Collections.emptyList();

    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(deviceId1) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);

    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(contr1id) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    String contr1descr2 = "Modded Descr - 2";
    ControlModification contrMod = new ControlModification();
    contrMod.setDescription(contr1descr2);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot modify control";
    String expectedDetail = String.format("Device %s has no available controls.", deviceId1);

    // Mock(s)
    when(this.deviceRepository.findById(eq(deviceId1))).thenReturn(device1);
    when(this.controlRepository.findById(eq(contr1id))).thenReturn(contr1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceControlInfo> arg2 = ArgumentCaptor.forClass(DeviceControlInfo.class);
    ArgumentCaptor<UUID> arg3 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg4 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call Service
    CommonOperResponse operResp = controlService.modifyControl(contr1id, contrMod);

    // Verify that appropriate Repository methods were NOT called
    verify(this.controlRepository, times(0)).update(arg1.capture(), arg2.capture());
    verify(this.deviceRepository, times(0)).update(arg3.capture(), arg4.capture());

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
  @DisplayName("TEST modifyControl (control name is not in the device's available controls)")
  void testModifyControlNameNotInDeviceAvailableControls() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    String device1name = "Shark - Test";
    String device1manuf = "Shark Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device1address = "2607:f0d0:1002:31::1";

    CommonDeviceState device1state = CommonDeviceState.OFF;
    Boolean device1isReg = true;
    Boolean device1isGrp = false;
    UUID device1grpId = null;

    UUID contr1id = UUID.fromString("00abcdef-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    CommonControlType contr1type = CommonControlType.DIRECT;
    String contr1val = "12.05";

    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr1name) //
        .description(contr1descr) //
        .controlType(contr1type) //
        .value(contr1val) //
        .isManaged(true));

    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(deviceId1) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);

    String contr1name2 = "Name 1 other";

    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(contr1id) //
        .name(contr1name2) //
        .description(contr1descr) //
        .currentValue(contr1val);

    String contr1descr2 = "Modded Descr - 2";
    ControlModification contrMod = new ControlModification();
    contrMod.setDescription(contr1descr2);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot modify control";
    String expectedDetail = String.format("Control %s is not inside Device %s available controls.",
        contr1name2, deviceId1);

    // Mock(s)
    when(this.deviceRepository.findById(eq(deviceId1))).thenReturn(device1);
    when(this.controlRepository.findById(eq(contr1id))).thenReturn(contr1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceControlInfo> arg2 = ArgumentCaptor.forClass(DeviceControlInfo.class);
    ArgumentCaptor<UUID> arg3 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg4 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call Service
    CommonOperResponse operResp = controlService.modifyControl(contr1id, contrMod);

    // Verify that appropriate Repository methods were NOT called
    verify(this.controlRepository, times(0)).update(arg1.capture(), arg2.capture());
    verify(this.deviceRepository, times(0)).update(arg3.capture(), arg4.capture());

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
  @DisplayName("TEST modifyControl (device's available control entry is marked as unregistered)")
  void testModifyControlAvailableControlEntryMarkedAsUnRegistered() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    String device1name = "Shark - Test";
    String device1manuf = "Shark Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device1address = "2607:f0d0:1002:31::1";

    CommonDeviceState device1state = CommonDeviceState.OFF;
    Boolean device1isReg = true;
    Boolean device1isGrp = false;
    UUID device1grpId = null;

    UUID contr1id = UUID.fromString("00abcdef-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    CommonControlType contr1type = CommonControlType.DIRECT;
    String contr1val = "12.05";

    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr1name) //
        .description(contr1descr) //
        .controlType(contr1type) //
        .value(contr1val) //
        .isManaged(false));

    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(deviceId1) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);

    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(contr1id) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    String contr1descr2 = "Modded Descr - 2";
    ControlModification contrMod = new ControlModification();
    contrMod.setDescription(contr1descr2);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot modify control";
    String expectedDetail = String.format("Control %s is not registered.", contr1name);

    // Mock(s)
    when(this.deviceRepository.findById(eq(deviceId1))).thenReturn(device1);
    when(this.controlRepository.findById(eq(contr1id))).thenReturn(contr1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceControlInfo> arg2 = ArgumentCaptor.forClass(DeviceControlInfo.class);
    ArgumentCaptor<UUID> arg3 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg4 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call Service
    CommonOperResponse operResp = controlService.modifyControl(contr1id, contrMod);

    // Verify that appropriate Repository methods were NOT called
    verify(this.controlRepository, times(0)).update(arg1.capture(), arg2.capture());
    verify(this.deviceRepository, times(0)).update(arg3.capture(), arg4.capture());

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
  @DisplayName("TEST deleteControl")
  void testDeleteControl() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    String device1name = "Shark - Test";
    String device1manuf = "Shark Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device1address = "2607:f0d0:1002:31::1";

    CommonDeviceState device1state = CommonDeviceState.OFF;
    Boolean device1isReg = true;
    Boolean device1isGrp = false;
    UUID device1grpId = null;

    UUID contr1id = UUID.fromString("00abcdef-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    CommonControlType contr1type = CommonControlType.DIRECT;
    String contr1val = "12.05";

    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr1name) //
        .description(contr1descr) //
        .controlType(contr1type) //
        .value(contr1val) //
        .isManaged(true));

    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(deviceId1) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);

    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(contr1id) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    HttpStatus expectedStatus = HttpStatus.OK;

    // Mock(s)
    when(this.deviceRepository.findById(eq(deviceId1))).thenReturn(device1);
    when(this.controlRepository.findById(eq(contr1id))).thenReturn(contr1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg3 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call Service
    CommonOperResponse operResp = controlService.deleteControl(contr1id);

    // Verify that appropriate Repository methods were called
    verify(this.controlRepository, times(1)).delete(arg1.capture());
    verify(this.deviceRepository, times(1)).update(arg2.capture(), arg3.capture());

    // Assertions
    assertEquals(contr1id, arg1.getValue(), "Argument must match");
    assertEquals(deviceId1, arg2.getValue(), "Argument must match");
    assertNotNull(arg3.getValue(), "Argument (DeviceInfo) must not be null");
    assertEquals(deviceId1, arg3.getValue().getDeviceId(), "Argument's (deviceId) must match");
    assertEquals(device1name, arg3.getValue().getName(), "Argument's (name) must match");
    assertEquals(device1manuf, arg3.getValue().getManufacturer(),
        "Argument's (manufacturer) must match");
    assertEquals(device1comment, arg3.getValue().getComment(), "Argument's (comment) must match");
    assertEquals(device1address, arg3.getValue().getAddress(), "Argument's (address) must match");
    assertEquals(device1state, arg3.getValue().getState(), "Argument's (state) must match");
    assertEquals(contr1name, arg3.getValue().getAvailableControls().get(0).getName(),
        "Argument's (available controls - name) must match");
    assertEquals(contr1descr, arg3.getValue().getAvailableControls().get(0).getDescription(),
        "Argument's (available controls - description) must match");
    assertEquals(contr1type, arg3.getValue().getAvailableControls().get(0).getControlType(),
        "Argument's (available controls - type) must match");
    assertEquals(contr1val, arg3.getValue().getAvailableControls().get(0).getValue(),
        "Argument's (available controls - value) must match");
    assertFalse(arg3.getValue().getAvailableControls().get(0).isIsManaged(),
        "Argument's (available controls - isManaged) must be false");
    assertEquals(device1isReg, arg3.getValue().isIsRegistered(),
        "Argument's (isRegistered) must match");
    assertEquals(device1isGrp, arg3.getValue().isIsGrouped(), "Argument's (isGrouped) must match");
    assertEquals(device1grpId, arg3.getValue().getGroupId(), "Argument's (groupId) must match");
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 200.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST deleteControl (control does not exist)")
  void testDeleteControlControlNotExist() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    String device1name = "Shark - Test";
    String device1manuf = "Shark Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device1address = "2607:f0d0:1002:31::1";

    CommonDeviceState device1state = CommonDeviceState.OFF;
    Boolean device1isReg = true;
    Boolean device1isGrp = false;
    UUID device1grpId = null;

    UUID contr1id = UUID.fromString("00abcdef-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";

    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr1name) //
        .description(contr1descr) //
        .controlType(CommonControlType.DIRECT) //
        .value(contr1val) //
        .isManaged(true));

    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(deviceId1) //
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
    when(this.deviceRepository.findById(eq(deviceId1))).thenReturn(device1);
    when(this.controlRepository.findById(eq(contr1id))).thenReturn(null);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg3 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call Service
    CommonOperResponse operResp = controlService.deleteControl(contr1id);

    // Verify that appropriate Repository methods were NOT called
    verify(this.controlRepository, times(0)).delete(arg1.capture());
    verify(this.deviceRepository, times(0)).update(arg2.capture(), arg3.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 404.");
    assertNull(operResp.getErrorResult(), "There should be no error returned.");
  }

  @Test
  @DisplayName("TEST deleteControl (device does not exist)")
  void testDeleteControlDeviceNotExist() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");

    UUID contr1id = UUID.fromString("00abcdef-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";

    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr1name) //
        .description(contr1descr) //
        .controlType(CommonControlType.DIRECT) //
        .value(contr1val) //
        .isManaged(true));

    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(contr1id) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot delete control";
    String expectedDetail = "The device associated with this control does not exist.";

    // Mock(s)
    when(this.deviceRepository.findById(eq(deviceId1))).thenReturn(null);
    when(this.controlRepository.findById(eq(contr1id))).thenReturn(contr1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg3 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call Service
    CommonOperResponse operResp = controlService.deleteControl(contr1id);

    // Verify that appropriate Repository methods were NOT called
    verify(this.controlRepository, times(0)).delete(arg1.capture());
    verify(this.deviceRepository, times(0)).update(arg2.capture(), arg3.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 500.");
    assertNotNull(operResp.getErrorResult(), "There should be an error returned.");
    assertNotNull(operResp.getErrorResult().getLogId(), "Error object must have logId provided.");
    assertNotNull(operResp.getErrorResult().getTimestamp(),
        "Error object must have timestamp provided.");
    assertEquals(ErrorType.EXCEPTION, operResp.getErrorResult().getType(),
        "Error object must have type = Error");
    assertNotNull(operResp.getErrorResult().getType(), "Error object must have logId provided.");
    assertEquals(expectedErrMsg, operResp.getErrorResult().getMessage(),
        "Error Message must match.");
    assertEquals(expectedDetail, operResp.getErrorResult().getDetails(),
        "Error Details must match.");
  }

  @Test
  @DisplayName("TEST deleteControl (device is not registered)")
  void testDeleteControlDeviceUnregistered() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    String device1name = "Shark - Test";
    String device1manuf = "Shark Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device1address = "2607:f0d0:1002:31::1";

    CommonDeviceState device1state = CommonDeviceState.OFF;
    Boolean device1isReg = false;
    Boolean device1isGrp = false;
    UUID device1grpId = null;

    UUID contr1id = UUID.fromString("00abcdef-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";

    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr1name) //
        .description(contr1descr) //
        .controlType(CommonControlType.DIRECT) //
        .value(contr1val) //
        .isManaged(true));

    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(deviceId1) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);

    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(contr1id) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot delete control";
    String expectedDetail = "The device associated with this control is not registered.";

    // Mock(s)
    when(this.deviceRepository.findById(eq(deviceId1))).thenReturn(device1);
    when(this.controlRepository.findById(eq(contr1id))).thenReturn(contr1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg3 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call Service
    CommonOperResponse operResp = controlService.deleteControl(contr1id);

    // Verify that appropriate Repository methods were NOT called
    verify(this.controlRepository, times(0)).delete(arg1.capture());
    verify(this.deviceRepository, times(0)).update(arg2.capture(), arg3.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 500.");
    assertNotNull(operResp.getErrorResult(), "There should be an error returned.");
    assertNotNull(operResp.getErrorResult().getLogId(), "Error object must have logId provided.");
    assertNotNull(operResp.getErrorResult().getTimestamp(),
        "Error object must have timestamp provided.");
    assertEquals(ErrorType.EXCEPTION, operResp.getErrorResult().getType(),
        "Error object must have type = Error");
    assertNotNull(operResp.getErrorResult().getType(), "Error object must have logId provided.");
    assertEquals(expectedErrMsg, operResp.getErrorResult().getMessage(),
        "Error Message must match.");
    assertEquals(expectedDetail, operResp.getErrorResult().getDetails(),
        "Error Details must match.");
  }

  @Test
  @DisplayName("TEST deleteControl (Control's deviceId is null)")
  void testDeleteControlDeviceIdNull() {
    UUID contr1id = UUID.fromString("00abcdef-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";

    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr1name) //
        .description(contr1descr) //
        .controlType(CommonControlType.DIRECT) //
        .value(contr1val) //
        .isManaged(true));

    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(null) //
        .controlId(contr1id) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot delete control";
    String expectedDetail = "There is no deviceId associated with this control.";

    // Mock(s)
    when(this.controlRepository.findById(eq(contr1id))).thenReturn(contr1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg3 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call Service
    CommonOperResponse operResp = controlService.deleteControl(contr1id);

    // Verify that appropriate Repository methods were NOT called
    verify(this.controlRepository, times(0)).delete(arg1.capture());
    verify(this.deviceRepository, times(0)).update(arg2.capture(), arg3.capture());

    // Assertions
    assertNotNull(operResp, "Return object is not null.");
    assertEquals(expectedStatus, operResp.getResponse(), "Response code must be 500.");
    assertNotNull(operResp.getErrorResult(), "There should be an error returned.");
    assertNotNull(operResp.getErrorResult().getLogId(), "Error object must have logId provided.");
    assertNotNull(operResp.getErrorResult().getTimestamp(),
        "Error object must have timestamp provided.");
    assertEquals(ErrorType.EXCEPTION, operResp.getErrorResult().getType(),
        "Error object must have type = Error");
    assertNotNull(operResp.getErrorResult().getType(), "Error object must have logId provided.");
    assertEquals(expectedErrMsg, operResp.getErrorResult().getMessage(),
        "Error Message must match.");
    assertEquals(expectedDetail, operResp.getErrorResult().getDetails(),
        "Error Details must match.");
  }

  @Test
  @DisplayName("TEST deleteControl (device has no available controls(null))")
  void testDeleteControlDeviceHasNoAvailControlsNull() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    String device1name = "Shark - Test";
    String device1manuf = "Shark Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device1address = "2607:f0d0:1002:31::1";

    CommonDeviceState device1state = CommonDeviceState.OFF;
    Boolean device1isReg = true;
    Boolean device1isGrp = false;
    UUID device1grpId = null;

    UUID contr1id = UUID.fromString("00abcdef-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";

    List<DeviceInfoAvailableControls> device1controls = null;

    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(deviceId1) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);

    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(contr1id) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot delete control";
    String expectedDetail = String.format("Device %s has no available controls.", deviceId1);

    // Mock(s)
    when(this.deviceRepository.findById(eq(deviceId1))).thenReturn(device1);
    when(this.controlRepository.findById(eq(contr1id))).thenReturn(contr1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg3 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call Service
    CommonOperResponse operResp = controlService.deleteControl(contr1id);

    // Verify that appropriate Repository methods were NOT called
    verify(this.controlRepository, times(0)).delete(arg1.capture());
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
  @DisplayName("TEST deleteControl (device has no available controls)")
  void testDeleteControlDeviceHasNoAvailControls() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    String device1name = "Shark - Test";
    String device1manuf = "Shark Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device1address = "2607:f0d0:1002:31::1";

    CommonDeviceState device1state = CommonDeviceState.OFF;
    Boolean device1isReg = true;
    Boolean device1isGrp = false;
    UUID device1grpId = null;

    UUID contr1id = UUID.fromString("00abcdef-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    String contr1val = "12.05";

    List<DeviceInfoAvailableControls> device1controls = Collections.emptyList();

    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(deviceId1) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);

    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(contr1id) //
        .name(contr1name) //
        .description(contr1descr) //
        .currentValue(contr1val);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot delete control";
    String expectedDetail = String.format("Device %s has no available controls.", deviceId1);

    // Mock(s)
    when(this.deviceRepository.findById(eq(deviceId1))).thenReturn(device1);
    when(this.controlRepository.findById(eq(contr1id))).thenReturn(contr1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg3 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call Service
    CommonOperResponse operResp = controlService.deleteControl(contr1id);

    // Verify that appropriate Repository methods were NOT called
    verify(this.controlRepository, times(0)).delete(arg1.capture());
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
  @DisplayName("TEST deleteControl (control name is not in the device's available controls)")
  void testDeleteControlNameNotInDeviceAvailableControls() {
    UUID deviceId1 = UUID.fromString("00111111-1111-1111-1111-1111abcdef01");
    String device1name = "Shark - Test";
    String device1manuf = "Shark Inc.";
    String device1comment = "An Old TV in the Living Room.";
    String device1address = "2607:f0d0:1002:31::1";

    CommonDeviceState device1state = CommonDeviceState.OFF;
    Boolean device1isReg = true;
    Boolean device1isGrp = false;
    UUID device1grpId = null;

    UUID contr1id = UUID.fromString("00abcdef-1111-1111-1111-111111111101");
    String contr1name = "Name 1";
    String contr1descr = "Descr 1";
    CommonControlType contr1type = CommonControlType.DIRECT;
    String contr1val = "12.05";

    List<DeviceInfoAvailableControls> device1controls = new ArrayList<>();
    device1controls.add(new DeviceInfoAvailableControls() //
        .name(contr1name) //
        .description(contr1descr) //
        .controlType(contr1type) //
        .value(contr1val) //
        .isManaged(true));

    DeviceInfo device1 = new DeviceInfo() //
        .deviceId(deviceId1) //
        .name(device1name) //
        .manufacturer(device1manuf) //
        .comment(device1comment) //
        .address(device1address) //
        .state(device1state) //
        .availableControls(device1controls) //
        .isRegistered(device1isReg) //
        .isGrouped(device1isGrp) //
        .groupId(device1grpId);

    String contr1name2 = "Name 1 other";

    DeviceControlInfo contr1 = new DeviceControlInfo() //
        .deviceId(deviceId1) //
        .controlId(contr1id) //
        .name(contr1name2) //
        .description(contr1descr) //
        .currentValue(contr1val);

    HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    String expectedErrMsg = "Cannot delete control";
    String expectedDetail = String.format("Control %s is not inside Device %s available controls.",
        contr1name2, deviceId1);

    // Mock(s)
    when(this.deviceRepository.findById(eq(deviceId1))).thenReturn(device1);
    when(this.controlRepository.findById(eq(contr1id))).thenReturn(contr1);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceInfo> arg3 = ArgumentCaptor.forClass(DeviceInfo.class);

    // Call Service
    CommonOperResponse operResp = controlService.deleteControl(contr1id);

    // Verify that appropriate Repository methods were NOT called
    verify(this.controlRepository, times(0)).delete(arg1.capture());
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
}
