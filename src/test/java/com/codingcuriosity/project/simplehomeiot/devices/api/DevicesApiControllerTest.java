package com.codingcuriosity.project.simplehomeiot.devices.api;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonAddResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonGetListResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonGetResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonOperResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.ErrorResult;
import com.codingcuriosity.project.simplehomeiot.common.model.ErrorType;
import com.codingcuriosity.project.simplehomeiot.devices.model.DeviceInfo;
import com.codingcuriosity.project.simplehomeiot.devices.model.DeviceModification;
import com.codingcuriosity.project.simplehomeiot.devices.model.DeviceRegistration;
import com.codingcuriosity.project.simplehomeiot.devices.model.DeviceRegistrationResponse;
import com.codingcuriosity.project.simplehomeiot.devices.model.DeviceSwitchOnOff;
import com.codingcuriosity.project.simplehomeiot.devices.service.DeviceService;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class DevicesApiControllerTest {

  DevicesApiController devicesApiController;

  HttpServletRequest request;

  DeviceService deviceService;

  @BeforeEach
  void setMocksAndInitRepo() {
    this.request = mock(HttpServletRequest.class);
    this.deviceService = mock(DeviceService.class);

    this.devicesApiController = new DevicesApiController(this.request, this.deviceService);
  }

  @Test
  @DisplayName("TEST getDevices (header = application/json)")
  void testGetDevices() {
    String header = "application/json";
    Integer skip = 0;
    Integer limit = 2;
    Boolean includeRegistered = true;
    Boolean includeUnregistered = false;

    DeviceInfo deviceInfo = new DeviceInfo();
    List<DeviceInfo> deviceInfoList = new ArrayList<>();
    deviceInfoList.add(deviceInfo);
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonGetListResponse<DeviceInfo> resp =
        new CommonGetListResponse<>(deviceInfoList, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.deviceService.getDevices(eq(skip), eq(limit), eq(includeRegistered),
        eq(includeUnregistered))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<Integer> arg1 = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Integer> arg2 = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Boolean> arg3 = ArgumentCaptor.forClass(Boolean.class);
    ArgumentCaptor<Boolean> arg4 = ArgumentCaptor.forClass(Boolean.class);

    // Call method
    ResponseEntity<?> responseEntity =
        devicesApiController.getDevices(skip, limit, includeRegistered, includeUnregistered);

    // Verify that appropriate service methods were called
    verify(this.deviceService, times(1)).getDevices(arg1.capture(), arg2.capture(), arg3.capture(),
        arg4.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(skip, arg1.getValue(), "Argument must match.");
    assertEquals(limit, arg2.getValue(), "Argument must match.");
    assertEquals(includeRegistered, arg3.getValue(), "Argument must match.");
    assertEquals(includeUnregistered, arg4.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a body.");
    assertEquals(deviceInfoList, responseEntity.getBody(), "Response Body must match");
  }

  @Test
  @DisplayName("TEST getDevices (header = */*)")
  void testGetDevicesHeaderAll() {
    String header = "*/*";
    Integer skip = 0;
    Integer limit = 2;
    Boolean includeRegistered = true;
    Boolean includeUnregistered = false;

    DeviceInfo deviceInfo = new DeviceInfo();
    List<DeviceInfo> deviceInfoList = new ArrayList<>();
    deviceInfoList.add(deviceInfo);
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonGetListResponse<DeviceInfo> resp =
        new CommonGetListResponse<>(deviceInfoList, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.deviceService.getDevices(eq(skip), eq(limit), eq(includeRegistered),
        eq(includeUnregistered))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<Integer> arg1 = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Integer> arg2 = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Boolean> arg3 = ArgumentCaptor.forClass(Boolean.class);
    ArgumentCaptor<Boolean> arg4 = ArgumentCaptor.forClass(Boolean.class);

    // Call method
    ResponseEntity<?> responseEntity =
        devicesApiController.getDevices(skip, limit, includeRegistered, includeUnregistered);

    // Verify that appropriate service methods were called
    verify(this.deviceService, times(1)).getDevices(arg1.capture(), arg2.capture(), arg3.capture(),
        arg4.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(skip, arg1.getValue(), "Argument must match.");
    assertEquals(limit, arg2.getValue(), "Argument must match.");
    assertEquals(includeRegistered, arg3.getValue(), "Argument must match.");
    assertEquals(includeUnregistered, arg4.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a body.");
    assertEquals(deviceInfoList, responseEntity.getBody(), "Response Body must match");
  }

  @Test
  @DisplayName("TEST getDevices (header = plain/text (invalid))")
  void testGetDevicesInvalidHeader() {
    String header = "plain/text";
    Integer skip = 0;
    Integer limit = 2;
    Boolean includeRegistered = true;
    Boolean includeUnregistered = false;

    DeviceInfo deviceInfo = new DeviceInfo();
    List<DeviceInfo> deviceInfoList = new ArrayList<>();
    deviceInfoList.add(deviceInfo);
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonGetListResponse<DeviceInfo> resp =
        new CommonGetListResponse<>(deviceInfoList, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.deviceService.getDevices(eq(skip), eq(limit), eq(includeRegistered),
        eq(includeUnregistered))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<Integer> arg1 = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Integer> arg2 = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Boolean> arg3 = ArgumentCaptor.forClass(Boolean.class);
    ArgumentCaptor<Boolean> arg4 = ArgumentCaptor.forClass(Boolean.class);

    // Call method
    ResponseEntity<?> responseEntity =
        devicesApiController.getDevices(skip, limit, includeRegistered, includeUnregistered);

    // Verify that appropriate service methods were NOT called
    verify(this.deviceService, times(0)).getDevices(arg1.capture(), arg2.capture(), arg3.capture(),
        arg4.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST getDevices (error response body provided)")
  void testGetDevicesWithErrorResponseBody() {
    String header = "application/json";
    Integer skip = 0;
    Integer limit = 2;
    Boolean includeRegistered = true;
    Boolean includeUnregistered = false;

    DeviceInfo deviceInfo = new DeviceInfo();
    List<DeviceInfo> deviceInfoList = new ArrayList<>();
    deviceInfoList.add(deviceInfo);
    UUID errorLogId = UUID.randomUUID();
    OffsetDateTime errorTimestamp = OffsetDateTime.now();
    ErrorType errorType = ErrorType.ERROR;
    String errorMessage = "test message";
    String errorDetails = "test details";
    HttpStatus response = HttpStatus.INTERNAL_SERVER_ERROR;
    ErrorResult errResult = new ErrorResult();
    errResult.setType(errorType);
    errResult.setTimestamp(errorTimestamp);
    errResult.setMessage(errorMessage);
    errResult.setDetails(errorDetails);
    errResult.setLogId(errorLogId);
    CommonGetListResponse<DeviceInfo> resp =
        new CommonGetListResponse<>(deviceInfoList, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.deviceService.getDevices(eq(skip), eq(limit), eq(includeRegistered),
        eq(includeUnregistered))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<Integer> arg1 = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Integer> arg2 = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Boolean> arg3 = ArgumentCaptor.forClass(Boolean.class);
    ArgumentCaptor<Boolean> arg4 = ArgumentCaptor.forClass(Boolean.class);

    // Call method
    ResponseEntity<?> responseEntity =
        devicesApiController.getDevices(skip, limit, includeRegistered, includeUnregistered);

    // Verify that appropriate service methods were called
    verify(this.deviceService, times(1)).getDevices(arg1.capture(), arg2.capture(), arg3.capture(),
        arg4.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(skip, arg1.getValue(), "Argument must match.");
    assertEquals(limit, arg2.getValue(), "Argument must match.");
    assertEquals(includeRegistered, arg3.getValue(), "Argument must match.");
    assertEquals(includeUnregistered, arg4.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a response body.");
    assertEquals(errResult, responseEntity.getBody(), "Response body must match.");
  }

  @Test
  @DisplayName("TEST getDevices (error response body not provided)")
  void testGetDevicesNoErrorResponseBody() {
    String header = "application/json";
    Integer skip = 0;
    Integer limit = 2;
    Boolean includeRegistered = true;
    Boolean includeUnregistered = false;

    DeviceInfo deviceInfo = new DeviceInfo();
    List<DeviceInfo> deviceInfoList = new ArrayList<>();
    deviceInfoList.add(deviceInfo);
    HttpStatus response = HttpStatus.INTERNAL_SERVER_ERROR;
    ErrorResult errResult = null;
    CommonGetListResponse<DeviceInfo> resp =
        new CommonGetListResponse<>(deviceInfoList, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.deviceService.getDevices(eq(skip), eq(limit), eq(includeRegistered),
        eq(includeUnregistered))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<Integer> arg1 = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Integer> arg2 = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Boolean> arg3 = ArgumentCaptor.forClass(Boolean.class);
    ArgumentCaptor<Boolean> arg4 = ArgumentCaptor.forClass(Boolean.class);

    // Call method
    ResponseEntity<?> responseEntity =
        devicesApiController.getDevices(skip, limit, includeRegistered, includeUnregistered);

    // Verify that appropriate service methods were called
    verify(this.deviceService, times(1)).getDevices(arg1.capture(), arg2.capture(), arg3.capture(),
        arg4.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(skip, arg1.getValue(), "Argument must match.");
    assertEquals(limit, arg2.getValue(), "Argument must match.");
    assertEquals(includeRegistered, arg3.getValue(), "Argument must match.");
    assertEquals(includeUnregistered, arg4.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST registerDevices (header = application/json)")
  void testRegisterDevices() {
    String header = "application/json";
    UUID idd = UUID.fromString("c1111111-9999-1111-1111-111111111111");

    DeviceRegistration body = new DeviceRegistration();

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonAddResponse resp = new CommonAddResponse(idd, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.deviceService.registerDevice(eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<DeviceRegistration> arg1 = ArgumentCaptor.forClass(DeviceRegistration.class);

    // Call method
    ResponseEntity<?> responseEntity = devicesApiController.registerDevices(body);

    // Verify that appropriate service methods were called
    verify(this.deviceService, times(1)).registerDevice(arg1.capture());

    // Assertions
    assertEquals(body, arg1.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a body.");
    assertThat(responseEntity.getBody(), instanceOf(DeviceRegistrationResponse.class));
  }

  @Test
  @DisplayName("TEST registerDevices (header = */*)")
  void testRegisterDevicesHeaderAll() {
    String header = "*/*";
    UUID idd = UUID.fromString("c1111111-9999-1111-1111-111111111111");

    DeviceRegistration body = new DeviceRegistration();

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonAddResponse resp = new CommonAddResponse(idd, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.deviceService.registerDevice(eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<DeviceRegistration> arg1 = ArgumentCaptor.forClass(DeviceRegistration.class);

    // Call method
    ResponseEntity<?> responseEntity = devicesApiController.registerDevices(body);

    // Verify that appropriate service methods were called
    verify(this.deviceService, times(1)).registerDevice(arg1.capture());

    // Assertions
    assertEquals(body, arg1.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a body.");
    assertThat(responseEntity.getBody(), instanceOf(DeviceRegistrationResponse.class));
  }

  @Test
  @DisplayName("TEST registerDevices (header = plain/text (invalid))")
  void testRegisterDevicesInvalidHeader() {
    String header = "plain/text";
    UUID idd = UUID.fromString("c1111111-9999-1111-1111-111111111111");

    DeviceRegistration body = new DeviceRegistration();

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonAddResponse resp = new CommonAddResponse(idd, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.deviceService.registerDevice(eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<DeviceRegistration> arg1 = ArgumentCaptor.forClass(DeviceRegistration.class);

    // Call method
    ResponseEntity<?> responseEntity = devicesApiController.registerDevices(body);

    // Verify that appropriate service methods were NOT called
    verify(this.deviceService, times(0)).registerDevice(arg1.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST registerDevices (error response body not provided)")
  void testRegisterDevicesNoErrorResponseBody() {
    String header = "application/json";

    DeviceRegistration body = new DeviceRegistration();

    HttpStatus response = HttpStatus.INTERNAL_SERVER_ERROR;
    ErrorResult errResult = null;
    CommonAddResponse resp = new CommonAddResponse(null, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.deviceService.registerDevice(eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<DeviceRegistration> arg1 = ArgumentCaptor.forClass(DeviceRegistration.class);

    // Call method
    ResponseEntity<?> responseEntity = devicesApiController.registerDevices(body);

    // Verify that appropriate service methods were called
    verify(this.deviceService, times(1)).registerDevice(arg1.capture());

    // Assertions
    assertEquals(body, arg1.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST registerDevices (error response body provided)")
  void testRegisterDevicesWithErrorResponseBody() {
    String header = "application/json";

    DeviceRegistration body = new DeviceRegistration();

    UUID errorLogId = UUID.randomUUID();
    OffsetDateTime errorTimestamp = OffsetDateTime.now();
    ErrorType errorType = ErrorType.ERROR;
    String errorMessage = "test message";
    String errorDetails = "test details";
    HttpStatus response = HttpStatus.INTERNAL_SERVER_ERROR;
    ErrorResult errResult = new ErrorResult();
    errResult.setType(errorType);
    errResult.setTimestamp(errorTimestamp);
    errResult.setMessage(errorMessage);
    errResult.setDetails(errorDetails);
    errResult.setLogId(errorLogId);
    CommonAddResponse resp = new CommonAddResponse(null, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.deviceService.registerDevice(eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<DeviceRegistration> arg1 = ArgumentCaptor.forClass(DeviceRegistration.class);

    // Call method
    ResponseEntity<?> responseEntity = devicesApiController.registerDevices(body);

    // Verify that appropriate service methods were called
    verify(this.deviceService, times(1)).registerDevice(arg1.capture());

    // Assertions
    assertEquals(body, arg1.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a response body.");
    assertEquals(errResult, responseEntity.getBody(), "Response body must match.");
  }

  @Test
  @DisplayName("TEST getDevice (header = application/json)")
  void testGetDevice() {
    String header = "application/json";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    DeviceInfo deviceInfo = new DeviceInfo();
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonGetResponse<DeviceInfo> resp = new CommonGetResponse<>(deviceInfo, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.deviceService.getDeviceById(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = devicesApiController.getDevice(id1);

    // Verify that appropriate service methods were called
    verify(this.deviceService, times(1)).getDeviceById(arg1.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a body.");
    assertEquals(deviceInfo, responseEntity.getBody(), "Response Body must match");
  }

  @Test
  @DisplayName("TEST getDevice (header = */*)")
  void testGetDeviceHeaderAll() {
    String header = "*/*";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    DeviceInfo deviceInfo = new DeviceInfo();
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonGetResponse<DeviceInfo> resp = new CommonGetResponse<>(deviceInfo, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.deviceService.getDeviceById(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = devicesApiController.getDevice(id1);

    // Verify that appropriate service methods were called
    verify(this.deviceService, times(1)).getDeviceById(arg1.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a body.");
    assertEquals(deviceInfo, responseEntity.getBody(), "Response Body must match");
  }

  @Test
  @DisplayName("TEST getDevice (header = plain/text (invalid))")
  void testGetDeviceInvalidHeader() {
    String header = "plain/text";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    DeviceInfo deviceInfo = new DeviceInfo();
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonGetResponse<DeviceInfo> resp = new CommonGetResponse<>(deviceInfo, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.deviceService.getDeviceById(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = devicesApiController.getDevice(id1);

    // Verify that appropriate service methods were NOT called
    verify(this.deviceService, times(0)).getDeviceById(arg1.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST getDevice (error response body provided)")
  void testGetDeviceWithErrorResponseBody() {
    String header = "application/json";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    DeviceInfo deviceInfo = new DeviceInfo();
    UUID errorLogId = UUID.randomUUID();
    OffsetDateTime errorTimestamp = OffsetDateTime.now();
    ErrorType errorType = ErrorType.ERROR;
    String errorMessage = "test message";
    String errorDetails = "test details";
    HttpStatus response = HttpStatus.INTERNAL_SERVER_ERROR;
    ErrorResult errResult = new ErrorResult();
    errResult.setType(errorType);
    errResult.setTimestamp(errorTimestamp);
    errResult.setMessage(errorMessage);
    errResult.setDetails(errorDetails);
    errResult.setLogId(errorLogId);
    CommonGetResponse<DeviceInfo> resp = new CommonGetResponse<>(deviceInfo, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.deviceService.getDeviceById(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = devicesApiController.getDevice(id1);

    // Verify that appropriate service methods were called
    verify(this.deviceService, times(1)).getDeviceById(arg1.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a response body.");
    assertEquals(errResult, responseEntity.getBody(), "Response body must match.");
  }

  @Test
  @DisplayName("TEST getDevice (error response body not provided)")
  void testGetDeviceNoErrorResponseBody() {
    String header = "application/json";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    DeviceInfo deviceInfo = new DeviceInfo();
    HttpStatus response = HttpStatus.INTERNAL_SERVER_ERROR;
    ErrorResult errResult = null;
    CommonGetResponse<DeviceInfo> resp = new CommonGetResponse<>(deviceInfo, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.deviceService.getDeviceById(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = devicesApiController.getDevice(id1);

    // Verify that appropriate service methods were called
    verify(this.deviceService, times(1)).getDeviceById(arg1.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST modifyDevice (header = application/json)")
  void testModifyDevice() {
    String header = "application/json";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    DeviceModification body = new DeviceModification();

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.deviceService.modifyDevice(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceModification> arg2 = ArgumentCaptor.forClass(DeviceModification.class);

    // Call method
    ResponseEntity<?> responseEntity = devicesApiController.modifyDevice(id1, body);

    // Verify that appropriate service methods were called
    verify(this.deviceService, times(1)).modifyDevice(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(body, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST modifyDevice (header = */*)")
  void testModifyDeviceHeaderAll() {
    String header = "*/*";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    DeviceModification body = new DeviceModification();

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.deviceService.modifyDevice(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceModification> arg2 = ArgumentCaptor.forClass(DeviceModification.class);

    // Call method
    ResponseEntity<?> responseEntity = devicesApiController.modifyDevice(id1, body);

    // Verify that appropriate service methods were called
    verify(this.deviceService, times(1)).modifyDevice(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(body, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST modifyDevice (header = plain/text (invalid))")
  void testModifyDeviceInvalidHeader() {
    String header = "plain/text";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    DeviceModification body = new DeviceModification();

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.deviceService.modifyDevice(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceModification> arg2 = ArgumentCaptor.forClass(DeviceModification.class);

    // Call method
    ResponseEntity<?> responseEntity = devicesApiController.modifyDevice(id1, body);

    // Verify that appropriate service methods were NOT called
    verify(this.deviceService, times(0)).modifyDevice(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST modifyDevice (error response body provided)")
  void testModifyDeviceWithErrorResponseBody() {
    String header = "application/json";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    DeviceModification body = new DeviceModification();

    UUID errorLogId = UUID.randomUUID();
    OffsetDateTime errorTimestamp = OffsetDateTime.now();
    ErrorType errorType = ErrorType.ERROR;
    String errorMessage = "test message";
    String errorDetails = "test details";
    HttpStatus response = HttpStatus.INTERNAL_SERVER_ERROR;
    ErrorResult errResult = new ErrorResult();
    errResult.setType(errorType);
    errResult.setTimestamp(errorTimestamp);
    errResult.setMessage(errorMessage);
    errResult.setDetails(errorDetails);
    errResult.setLogId(errorLogId);
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.deviceService.modifyDevice(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceModification> arg2 = ArgumentCaptor.forClass(DeviceModification.class);

    // Call method
    ResponseEntity<?> responseEntity = devicesApiController.modifyDevice(id1, body);

    // Verify that appropriate service methods were called
    verify(this.deviceService, times(1)).modifyDevice(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(body, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a response body.");
    assertEquals(errResult, responseEntity.getBody(), "Response body must match.");
  }

  @Test
  @DisplayName("TEST switchOnOffDevice (header = application/json)")
  void testSwitchOnOffDevice() {
    String header = "application/json";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    DeviceSwitchOnOff body = new DeviceSwitchOnOff();

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.deviceService.switchDeviceOnOff(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceSwitchOnOff> arg2 = ArgumentCaptor.forClass(DeviceSwitchOnOff.class);

    // Call method
    ResponseEntity<?> responseEntity = devicesApiController.switchOnOffDevice(id1, body);

    // Verify that appropriate service methods were called
    verify(this.deviceService, times(1)).switchDeviceOnOff(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(body, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST switchOnOffDevice (header = */*)")
  void testSwitchOnOffDeviceHeaderAll() {
    String header = "*/*";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    DeviceSwitchOnOff body = new DeviceSwitchOnOff();

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.deviceService.switchDeviceOnOff(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceSwitchOnOff> arg2 = ArgumentCaptor.forClass(DeviceSwitchOnOff.class);

    // Call method
    ResponseEntity<?> responseEntity = devicesApiController.switchOnOffDevice(id1, body);

    // Verify that appropriate service methods were called
    verify(this.deviceService, times(1)).switchDeviceOnOff(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(body, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST switchOnOffDevice (header = plain/text (invalid))")
  void testSwitchOnOffDeviceInvalidHeader() {
    String header = "plain/text";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    DeviceSwitchOnOff body = new DeviceSwitchOnOff();

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.deviceService.switchDeviceOnOff(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceSwitchOnOff> arg2 = ArgumentCaptor.forClass(DeviceSwitchOnOff.class);

    // Call method
    ResponseEntity<?> responseEntity = devicesApiController.switchOnOffDevice(id1, body);

    // Verify that appropriate service methods were NOT called
    verify(this.deviceService, times(0)).switchDeviceOnOff(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST switchOnOffDevice (error response body provided)")
  void testSwitchOnOffDeviceWithErrorResponseBody() {
    String header = "application/json";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    DeviceSwitchOnOff body = new DeviceSwitchOnOff();

    UUID errorLogId = UUID.randomUUID();
    OffsetDateTime errorTimestamp = OffsetDateTime.now();
    ErrorType errorType = ErrorType.ERROR;
    String errorMessage = "test message";
    String errorDetails = "test details";
    HttpStatus response = HttpStatus.INTERNAL_SERVER_ERROR;
    ErrorResult errResult = new ErrorResult();
    errResult.setType(errorType);
    errResult.setTimestamp(errorTimestamp);
    errResult.setMessage(errorMessage);
    errResult.setDetails(errorDetails);
    errResult.setLogId(errorLogId);
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.deviceService.switchDeviceOnOff(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DeviceSwitchOnOff> arg2 = ArgumentCaptor.forClass(DeviceSwitchOnOff.class);

    // Call method
    ResponseEntity<?> responseEntity = devicesApiController.switchOnOffDevice(id1, body);

    // Verify that appropriate service methods were called
    verify(this.deviceService, times(1)).switchDeviceOnOff(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(body, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a response body.");
    assertEquals(errResult, responseEntity.getBody(), "Response body must match.");
  }

  @Test
  @DisplayName("TEST deleteDevice (header = application/json)")
  void testDeleteDevice() {
    String header = "application/json";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.deviceService.deleteDevice(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = devicesApiController.deleteDevice(id1);

    // Verify that appropriate service methods were called
    verify(this.deviceService, times(1)).deleteDevice(arg1.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST deleteDevice (header = */*)")
  void testDeleteDeviceHeaderAll() {
    String header = "*/*";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.deviceService.deleteDevice(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = devicesApiController.deleteDevice(id1);

    // Verify that appropriate service methods were called
    verify(this.deviceService, times(1)).deleteDevice(arg1.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST deleteDevice (header = plain/text (invalid))")
  void testDeleteDeviceInvalidHeader() {
    String header = "plain/text";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.deviceService.deleteDevice(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = devicesApiController.deleteDevice(id1);

    // Verify that appropriate service methods were NOT called
    verify(this.deviceService, times(0)).deleteDevice(arg1.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST deleteDevice (error response body provided)")
  void testDeleteDeviceWithErrorResponseBody() {
    String header = "application/json";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    UUID errorLogId = UUID.randomUUID();
    OffsetDateTime errorTimestamp = OffsetDateTime.now();
    ErrorType errorType = ErrorType.ERROR;
    String errorMessage = "test message";
    String errorDetails = "test details";
    HttpStatus response = HttpStatus.INTERNAL_SERVER_ERROR;
    ErrorResult errResult = new ErrorResult();
    errResult.setType(errorType);
    errResult.setTimestamp(errorTimestamp);
    errResult.setMessage(errorMessage);
    errResult.setDetails(errorDetails);
    errResult.setLogId(errorLogId);
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.deviceService.deleteDevice(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = devicesApiController.deleteDevice(id1);

    // Verify that appropriate service methods were called
    verify(this.deviceService, times(1)).deleteDevice(arg1.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a response body.");
    assertEquals(errResult, responseEntity.getBody(), "Response body must match.");
  }
}
