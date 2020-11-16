package com.codingcuriosity.project.simplehomeiot.controls.api;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonAddResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonGetListResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonOperResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.ErrorResult;
import com.codingcuriosity.project.simplehomeiot.common.model.ErrorType;
import com.codingcuriosity.project.simplehomeiot.controls.model.ControlModification;
import com.codingcuriosity.project.simplehomeiot.controls.model.ControlRegistration;
import com.codingcuriosity.project.simplehomeiot.controls.model.ControlRegistrationResponse;
import com.codingcuriosity.project.simplehomeiot.controls.model.DeviceControlInfo;
import com.codingcuriosity.project.simplehomeiot.controls.model.DirectControlValue;
import com.codingcuriosity.project.simplehomeiot.controls.model.SliderControlValue;
import com.codingcuriosity.project.simplehomeiot.controls.service.ControlService;
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
public class ControlsApiControllerTest {

  ControlsApiController controlsApiController;

  HttpServletRequest request;

  ControlService controlService;

  @BeforeEach
  void setMocksAndInitRepo() {
    this.request = mock(HttpServletRequest.class);
    this.controlService = mock(ControlService.class);

    this.controlsApiController = new ControlsApiController(this.request, this.controlService);
  }

  @Test
  @DisplayName("TEST getDevicesControls (header = application/json)")
  void testGetDevicesControls() {
    String header = "application/json";

    DeviceControlInfo retObj = new DeviceControlInfo();
    List<DeviceControlInfo> retObjList = new ArrayList<>();
    retObjList.add(retObj);
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonGetListResponse<DeviceControlInfo> resp =
        new CommonGetListResponse<>(retObjList, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    doReturn(resp).when(this.controlService).getDeviceControls();

    // Call method
    ResponseEntity<?> responseEntity = controlsApiController.getDevicesControls();

    // Verify that appropriate service methods were called
    verify(this.controlService, times(1)).getDeviceControls();

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a body.");
  }

  @Test
  @DisplayName("TEST getDevicesControls (header = */*)")
  void testGetDevicesControlsHeaderAll() {
    String header = "*/*";

    DeviceControlInfo retObj = new DeviceControlInfo();
    List<DeviceControlInfo> retObjList = new ArrayList<>();
    retObjList.add(retObj);
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonGetListResponse<DeviceControlInfo> resp =
        new CommonGetListResponse<>(retObjList, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    doReturn(resp).when(this.controlService).getDeviceControls();

    // Call method
    ResponseEntity<?> responseEntity = controlsApiController.getDevicesControls();

    // Verify that appropriate service methods were called
    verify(this.controlService, times(1)).getDeviceControls();

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a body.");
    assertEquals(retObjList, responseEntity.getBody(), "Response body must match.");
  }

  @Test
  @DisplayName("TEST getDevicesControls (header = plain/text (invalid))")
  void testGetDevicesControlsInvalidHeader() {
    String header = "plain/text";

    DeviceControlInfo retObj = new DeviceControlInfo();
    List<DeviceControlInfo> retObjList = new ArrayList<>();
    retObjList.add(retObj);
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonGetListResponse<DeviceControlInfo> resp =
        new CommonGetListResponse<>(retObjList, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    doReturn(resp).when(this.controlService).getDeviceControls();

    // Call method
    ResponseEntity<?> responseEntity = controlsApiController.getDevicesControls();

    // Verify that appropriate service methods were NOT called
    verify(this.controlService, times(0)).getDeviceControls();

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST getDevicesControls (no response body)")
  void testGetDevicesControlsNoBody() {
    String header = "application/json";

    List<DeviceControlInfo> retObjList = null;
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonGetListResponse<DeviceControlInfo> resp =
        new CommonGetListResponse<>(retObjList, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    doReturn(resp).when(this.controlService).getDeviceControls();

    // Call method
    ResponseEntity<?> responseEntity = controlsApiController.getDevicesControls();

    // Verify that appropriate service methods were called
    verify(this.controlService, times(1)).getDeviceControls();

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST getDevicesControls (error response body provided)")
  void testGetDevicesControlsWithErrorResponseBody() {
    String header = "application/json";

    List<DeviceControlInfo> retObjList = null;
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
    CommonGetListResponse<DeviceControlInfo> resp =
        new CommonGetListResponse<>(retObjList, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    doReturn(resp).when(this.controlService).getDeviceControls();

    // Call method
    ResponseEntity<?> responseEntity = controlsApiController.getDevicesControls();

    // Verify that appropriate service methods were called
    verify(this.controlService, times(1)).getDeviceControls();

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a response body.");
    assertEquals(errResult, responseEntity.getBody(), "Response body must match.");
  }

  @Test
  @DisplayName("TEST getDevicesControls (error response body not provided)")
  void testGetDevicesControlsNoErrorResponseBody() {
    String header = "application/json";

    List<DeviceControlInfo> retObjList = null;
    HttpStatus response = HttpStatus.INTERNAL_SERVER_ERROR;
    ErrorResult errResult = null;
    CommonGetListResponse<DeviceControlInfo> resp =
        new CommonGetListResponse<>(retObjList, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    doReturn(resp).when(this.controlService).getDeviceControls();

    // Call method
    ResponseEntity<?> responseEntity = controlsApiController.getDevicesControls();

    // Verify that appropriate service methods were called
    verify(this.controlService, times(1)).getDeviceControls();

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no response body.");
  }

  @Test
  @DisplayName("TEST getFilteredDeviceControls (header = application/json)")
  void testGetFilteredDeviceControls() {
    String header = "application/json";
    UUID id1 = UUID.fromString("b1111111-1111-1111-1111-111111111111");

    DeviceControlInfo retObj = new DeviceControlInfo();
    List<DeviceControlInfo> retObjList = new ArrayList<>();
    retObjList.add(retObj);
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonGetListResponse<DeviceControlInfo> resp =
        new CommonGetListResponse<>(retObjList, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.controlService.getFilteredDeviceControls(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = controlsApiController.getDeviceControls(id1);

    // Verify that appropriate service methods were called
    verify(this.controlService, times(1)).getFilteredDeviceControls(arg1.capture());

    // Assertions
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a body.");
  }

  @Test
  @DisplayName("TEST getFilteredDeviceControls (header = */*)")
  void testGetFilteredDeviceControlsHeaderAll() {
    String header = "*/*";
    UUID id1 = UUID.fromString("b1111111-1111-1111-1111-111111111111");

    DeviceControlInfo retObj = new DeviceControlInfo();
    List<DeviceControlInfo> retObjList = new ArrayList<>();
    retObjList.add(retObj);
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonGetListResponse<DeviceControlInfo> resp =
        new CommonGetListResponse<>(retObjList, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.controlService.getFilteredDeviceControls(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = controlsApiController.getDeviceControls(id1);

    // Verify that appropriate service methods were called
    verify(this.controlService, times(1)).getFilteredDeviceControls(arg1.capture());

    // Assertions
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a body.");
    assertEquals(retObjList, responseEntity.getBody(), "Response body must match.");
  }

  @Test
  @DisplayName("TEST getFilteredDeviceControls (header = plain/text (invalid))")
  void testGetFilteredDeviceControlsInvalidHeader() {
    String header = "plain/text";
    UUID id1 = UUID.fromString("b1111111-1111-1111-1111-111111111111");

    DeviceControlInfo retObj = new DeviceControlInfo();
    List<DeviceControlInfo> retObjList = new ArrayList<>();
    retObjList.add(retObj);
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonGetListResponse<DeviceControlInfo> resp =
        new CommonGetListResponse<>(retObjList, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.controlService.getFilteredDeviceControls(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = controlsApiController.getDeviceControls(id1);

    // Verify that appropriate service methods were NOT called
    verify(this.controlService, times(0)).getFilteredDeviceControls(arg1.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST getFilteredDeviceControls (no response body)")
  void testGetFilterDeviceControlsNoBody() {
    String header = "application/json";
    UUID id1 = UUID.fromString("b1111111-1111-1111-1111-111111111111");

    List<DeviceControlInfo> retObjList = null;
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonGetListResponse<DeviceControlInfo> resp =
        new CommonGetListResponse<>(retObjList, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.controlService.getFilteredDeviceControls(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = controlsApiController.getDeviceControls(id1);

    // Verify that appropriate service methods were called
    verify(this.controlService, times(1)).getFilteredDeviceControls(arg1.capture());

    // Assertions
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST getFilteredDeviceControls (error response body provided)")
  void testGetFilteredDeviceControlsWithErrorResponseBody() {
    String header = "application/json";
    UUID id1 = UUID.fromString("b1111111-1111-1111-1111-111111111111");

    List<DeviceControlInfo> retObjList = null;
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
    CommonGetListResponse<DeviceControlInfo> resp =
        new CommonGetListResponse<>(retObjList, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.controlService.getFilteredDeviceControls(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = controlsApiController.getDeviceControls(id1);

    // Verify that appropriate service methods were called
    verify(this.controlService, times(1)).getFilteredDeviceControls(arg1.capture());

    // Assertions
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a response body.");
    assertEquals(errResult, responseEntity.getBody(), "Response body must match.");
  }

  @Test
  @DisplayName("TEST getFilteredDeviceControls (error response body not provided)")
  void testGetFilteredDeviceControlsNoErrorResponseBody() {
    String header = "application/json";
    UUID id1 = UUID.fromString("b1111111-1111-1111-1111-111111111111");

    List<DeviceControlInfo> retObjList = null;
    HttpStatus response = HttpStatus.INTERNAL_SERVER_ERROR;
    ErrorResult errResult = null;
    CommonGetListResponse<DeviceControlInfo> resp =
        new CommonGetListResponse<>(retObjList, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.controlService.getFilteredDeviceControls(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = controlsApiController.getDeviceControls(id1);

    // Verify that appropriate service methods were called
    verify(this.controlService, times(1)).getFilteredDeviceControls(arg1.capture());

    // Assertions
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no response body.");
  }

  @Test
  @DisplayName("TEST registerControl (header = application/json)")
  void testRegisterDevicesControls() {
    String header = "application/json";
    UUID id1 = UUID.fromString("b1111111-1111-1111-1111-111111111111");
    UUID idc = UUID.fromString("b1111111-9999-1111-1111-111111111111");

    ControlRegistration body = new ControlRegistration();

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonAddResponse resp = new CommonAddResponse(idc, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.controlService.registerControl(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ControlRegistration> arg2 = ArgumentCaptor.forClass(ControlRegistration.class);

    // Call method
    ResponseEntity<?> responseEntity = controlsApiController.registerDevicesControls(id1, body);

    // Verify that appropriate service methods were called
    verify(this.controlService, times(1)).registerControl(arg1.capture(), arg2.capture());

    // Assertions
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(body, arg2.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a body.");
    assertThat(responseEntity.getBody(), instanceOf(ControlRegistrationResponse.class));
  }

  @Test
  @DisplayName("TEST registerControl (header = */*)")
  void testRegisterDevicesControlsHeaderAll() {
    String header = "*/*";
    UUID id1 = UUID.fromString("b1111111-1111-1111-1111-111111111111");
    UUID idc = UUID.fromString("b1111111-9999-1111-1111-111111111111");

    ControlRegistration body = new ControlRegistration();

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonAddResponse resp = new CommonAddResponse(idc, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.controlService.registerControl(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ControlRegistration> arg2 = ArgumentCaptor.forClass(ControlRegistration.class);

    // Call method
    ResponseEntity<?> responseEntity = controlsApiController.registerDevicesControls(id1, body);

    // Verify that appropriate service methods were called
    verify(this.controlService, times(1)).registerControl(arg1.capture(), arg2.capture());

    // Assertions
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(body, arg2.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a body.");
    assertThat(responseEntity.getBody(), instanceOf(ControlRegistrationResponse.class));
  }

  @Test
  @DisplayName("TEST registerControl (header = plain/text (invalid))")
  void testRegisterDevicesControlsInvalidHeader() {
    String header = "plain/text";
    UUID id1 = UUID.fromString("b1111111-1111-1111-1111-111111111111");
    UUID idc = UUID.fromString("b1111111-9999-1111-1111-111111111111");

    ControlRegistration body = new ControlRegistration();

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonAddResponse resp = new CommonAddResponse(idc, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.controlService.registerControl(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ControlRegistration> arg2 = ArgumentCaptor.forClass(ControlRegistration.class);

    // Call method
    ResponseEntity<?> responseEntity = controlsApiController.registerDevicesControls(id1, body);

    // Verify that appropriate service methods were NOT called
    verify(this.controlService, times(0)).registerControl(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST registerControl (error response body provided)")
  void testRegisterDevicesControlsWithErrorResponseBody() {
    String header = "application/json";
    UUID id1 = UUID.fromString("b1111111-1111-1111-1111-111111111111");

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

    ControlRegistration body = new ControlRegistration();

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.controlService.registerControl(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ControlRegistration> arg2 = ArgumentCaptor.forClass(ControlRegistration.class);

    // Call method
    ResponseEntity<?> responseEntity = controlsApiController.registerDevicesControls(id1, body);

    // Verify that appropriate service methods were called
    verify(this.controlService, times(1)).registerControl(arg1.capture(), arg2.capture());

    // Assertions
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(body, arg2.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a response body.");
    assertEquals(errResult, responseEntity.getBody(), "Response body must match.");
  }

  @Test
  @DisplayName("TEST registerControl (error response body not provided)")
  void testRegisterDevicesControlsNoErrorResponseBody() {
    String header = "application/json";
    UUID id1 = UUID.fromString("b1111111-1111-1111-1111-111111111111");

    ControlRegistration body = new ControlRegistration();

    HttpStatus response = HttpStatus.INTERNAL_SERVER_ERROR;
    ErrorResult errResult = null;
    CommonAddResponse resp = new CommonAddResponse(null, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.controlService.registerControl(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ControlRegistration> arg2 = ArgumentCaptor.forClass(ControlRegistration.class);

    // Call method
    ResponseEntity<?> responseEntity = controlsApiController.registerDevicesControls(id1, body);

    // Verify that appropriate service methods were called
    verify(this.controlService, times(1)).registerControl(arg1.capture(), arg2.capture());

    // Assertions
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no response body.");
  }

  @Test
  @DisplayName("TEST setDirectControl (header = application/json)")
  void testSetDirectControl() {
    String header = "application/json";
    UUID id1 = UUID.fromString("b1111111-1111-1111-1111-111111111111");

    DirectControlValue body = new DirectControlValue();

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.controlService.setDirectControl(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DirectControlValue> arg2 = ArgumentCaptor.forClass(DirectControlValue.class);

    // Call method
    ResponseEntity<?> responseEntity = controlsApiController.setDirectControl(id1, body);

    // Verify that appropriate service methods were called
    verify(this.controlService, times(1)).setDirectControl(arg1.capture(), arg2.capture());

    // Assertions
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(body, arg2.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST setDirectControl (header = */*)")
  void testSetDirectControlHeaderAll() {
    String header = "*/*";
    UUID id1 = UUID.fromString("b1111111-1111-1111-1111-111111111111");

    DirectControlValue body = new DirectControlValue();

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.controlService.setDirectControl(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DirectControlValue> arg2 = ArgumentCaptor.forClass(DirectControlValue.class);

    // Call method
    ResponseEntity<?> responseEntity = controlsApiController.setDirectControl(id1, body);

    // Verify that appropriate service methods were called
    verify(this.controlService, times(1)).setDirectControl(arg1.capture(), arg2.capture());

    // Assertions
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(body, arg2.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST setDirectControl (header = plain/text (invalid))")
  void testSetDirectControlInvalidHeader() {
    String header = "plain/text";
    UUID id1 = UUID.fromString("b1111111-1111-1111-1111-111111111111");

    DirectControlValue body = new DirectControlValue();

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.controlService.setDirectControl(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DirectControlValue> arg2 = ArgumentCaptor.forClass(DirectControlValue.class);

    // Call method
    ResponseEntity<?> responseEntity = controlsApiController.setDirectControl(id1, body);

    // Verify that appropriate service methods were NOT called
    verify(this.controlService, times(0)).setDirectControl(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST setDirectControl (error response body provided)")
  void testSetDirectControlWithErrorResponseBody() {
    String header = "application/json";
    UUID id1 = UUID.fromString("b1111111-1111-1111-1111-111111111111");

    DirectControlValue body = new DirectControlValue();

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
    when(this.controlService.setDirectControl(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DirectControlValue> arg2 = ArgumentCaptor.forClass(DirectControlValue.class);

    // Call method
    ResponseEntity<?> responseEntity = controlsApiController.setDirectControl(id1, body);

    // Verify that appropriate service methods were called
    verify(this.controlService, times(1)).setDirectControl(arg1.capture(), arg2.capture());

    // Assertions
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(body, arg2.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a response body.");
    assertEquals(errResult, responseEntity.getBody(), "Response body must match.");
  }

  @Test
  @DisplayName("TEST setDirectControl (error response body not provided)")
  void testSetDirectControlNoErrorResponseBody() {
    String header = "application/json";
    UUID id1 = UUID.fromString("b1111111-1111-1111-1111-111111111111");

    DirectControlValue body = new DirectControlValue();

    HttpStatus response = HttpStatus.INTERNAL_SERVER_ERROR;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.controlService.setDirectControl(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<DirectControlValue> arg2 = ArgumentCaptor.forClass(DirectControlValue.class);

    // Call method
    ResponseEntity<?> responseEntity = controlsApiController.setDirectControl(id1, body);

    // Verify that appropriate service methods were called
    verify(this.controlService, times(1)).setDirectControl(arg1.capture(), arg2.capture());

    // Assertions
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(body, arg2.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no response body.");
  }

  @Test
  @DisplayName("TEST setSliderControl (header = application/json)")
  void testSetSliderControl() {
    String header = "application/json";
    UUID id1 = UUID.fromString("b1111111-1111-1111-1111-111111111111");

    SliderControlValue body = new SliderControlValue();

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.controlService.setSliderControl(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<SliderControlValue> arg2 = ArgumentCaptor.forClass(SliderControlValue.class);

    // Call method
    ResponseEntity<?> responseEntity = controlsApiController.setSliderControl(id1, body);

    // Verify that appropriate service methods were called
    verify(this.controlService, times(1)).setSliderControl(arg1.capture(), arg2.capture());

    // Assertions
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(body, arg2.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST setSliderControl (header = */*)")
  void testSetSliderControlHeaderAll() {
    String header = "*/*";
    UUID id1 = UUID.fromString("b1111111-1111-1111-1111-111111111111");

    SliderControlValue body = new SliderControlValue();

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.controlService.setSliderControl(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<SliderControlValue> arg2 = ArgumentCaptor.forClass(SliderControlValue.class);

    // Call method
    ResponseEntity<?> responseEntity = controlsApiController.setSliderControl(id1, body);

    // Verify that appropriate service methods were called
    verify(this.controlService, times(1)).setSliderControl(arg1.capture(), arg2.capture());

    // Assertions
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(body, arg2.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST setSliderControl (header = plain/text (invalid))")
  void testSetSliderControlInvalidHeader() {
    String header = "plain/text";
    UUID id1 = UUID.fromString("b1111111-1111-1111-1111-111111111111");

    SliderControlValue body = new SliderControlValue();

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.controlService.setSliderControl(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<SliderControlValue> arg2 = ArgumentCaptor.forClass(SliderControlValue.class);

    // Call method
    ResponseEntity<?> responseEntity = controlsApiController.setSliderControl(id1, body);

    // Verify that appropriate service methods were NOT called
    verify(this.controlService, times(0)).setSliderControl(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST setSliderControl (error response body provided)")
  void testSetSliderControlWithErrorResponseBody() {
    String header = "application/json";
    UUID id1 = UUID.fromString("b1111111-1111-1111-1111-111111111111");

    SliderControlValue body = new SliderControlValue();

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
    when(this.controlService.setSliderControl(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<SliderControlValue> arg2 = ArgumentCaptor.forClass(SliderControlValue.class);

    // Call method
    ResponseEntity<?> responseEntity = controlsApiController.setSliderControl(id1, body);

    // Verify that appropriate service methods were called
    verify(this.controlService, times(1)).setSliderControl(arg1.capture(), arg2.capture());

    // Assertions
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(body, arg2.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a response body.");
    assertEquals(errResult, responseEntity.getBody(), "Response body must match.");
  }

  @Test
  @DisplayName("TEST setSliderControl (error response body not provided)")
  void testSetSliderControlNoErrorResponseBody() {
    String header = "application/json";
    UUID id1 = UUID.fromString("b1111111-1111-1111-1111-111111111111");

    SliderControlValue body = new SliderControlValue();

    HttpStatus response = HttpStatus.INTERNAL_SERVER_ERROR;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.controlService.setSliderControl(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<SliderControlValue> arg2 = ArgumentCaptor.forClass(SliderControlValue.class);

    // Call method
    ResponseEntity<?> responseEntity = controlsApiController.setSliderControl(id1, body);

    // Verify that appropriate service methods were called
    verify(this.controlService, times(1)).setSliderControl(arg1.capture(), arg2.capture());

    // Assertions
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(body, arg2.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no response body.");
  }

  @Test
  @DisplayName("TEST setToggleControl (header = application/json)")
  void testSetToggleControl() {
    String header = "application/json";
    UUID id1 = UUID.fromString("b1111111-1111-1111-1111-111111111111");

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.controlService.setToggleControl(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = controlsApiController.setToggleControl(id1);

    // Verify that appropriate service methods were called
    verify(this.controlService, times(1)).setToggleControl(arg1.capture());

    // Assertions
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST setToggleControl (header = */*)")
  void testSetToggleControlHeaderAll() {
    String header = "*/*";
    UUID id1 = UUID.fromString("b1111111-1111-1111-1111-111111111111");

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.controlService.setToggleControl(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = controlsApiController.setToggleControl(id1);

    // Verify that appropriate service methods were called
    verify(this.controlService, times(1)).setToggleControl(arg1.capture());

    // Assertions
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST setToggleControl (header = plain/text (invalid))")
  void testSetToggleControlInvalidHeader() {
    String header = "plain/text";
    UUID id1 = UUID.fromString("b1111111-1111-1111-1111-111111111111");

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.controlService.setToggleControl(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = controlsApiController.setToggleControl(id1);

    // Verify that appropriate service methods were NOT called
    verify(this.controlService, times(0)).setToggleControl(arg1.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST setToggleControl (error response body provided)")
  void testSetToggleControlWithErrorResponseBody() {
    String header = "application/json";
    UUID id1 = UUID.fromString("b1111111-1111-1111-1111-111111111111");

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
    when(this.controlService.setToggleControl(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = controlsApiController.setToggleControl(id1);

    // Verify that appropriate service methods were called
    verify(this.controlService, times(1)).setToggleControl(arg1.capture());

    // Assertions
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a response body.");
    assertEquals(errResult, responseEntity.getBody(), "Response body must match.");
  }

  @Test
  @DisplayName("TEST setToggleControl (error response body not provided)")
  void testSetToggleControlNoErrorResponseBody() {
    String header = "application/json";
    UUID id1 = UUID.fromString("b1111111-1111-1111-1111-111111111111");

    HttpStatus response = HttpStatus.INTERNAL_SERVER_ERROR;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.controlService.setToggleControl(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = controlsApiController.setToggleControl(id1);

    // Verify that appropriate service methods were called
    verify(this.controlService, times(1)).setToggleControl(arg1.capture());

    // Assertions
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no response body.");
  }

  @Test
  @DisplayName("TEST modifyDeviceControls (header = application/json)")
  void testModifyDeviceControls() {
    String header = "application/json";
    UUID id1 = UUID.fromString("a1111111-1111-1111-1111-111111111111");

    ControlModification body = new ControlModification();

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.controlService.modifyControl(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ControlModification> arg2 = ArgumentCaptor.forClass(ControlModification.class);

    // Call method
    ResponseEntity<?> responseEntity = controlsApiController.modifyDeviceControls(id1, body);

    // Verify that appropriate service methods were called
    verify(this.controlService, times(1)).modifyControl(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(body, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST modifyDeviceControls (header = */*)")
  void testModifyDeviceControlsHeaderAll() {
    String header = "*/*";
    UUID id1 = UUID.fromString("a1111111-1111-1111-1111-111111111111");

    ControlModification body = new ControlModification();

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.controlService.modifyControl(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ControlModification> arg2 = ArgumentCaptor.forClass(ControlModification.class);

    // Call method
    ResponseEntity<?> responseEntity = controlsApiController.modifyDeviceControls(id1, body);

    // Verify that appropriate service methods were called
    verify(this.controlService, times(1)).modifyControl(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(body, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST modifyDeviceControls (header = plain/text (invalid))")
  void testModifyDeviceControlsInvalidHeader() {
    String header = "plain/text";
    UUID id1 = UUID.fromString("a1111111-1111-1111-1111-111111111111");

    ControlModification body = new ControlModification();

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.controlService.modifyControl(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ControlModification> arg2 = ArgumentCaptor.forClass(ControlModification.class);

    // Call method
    ResponseEntity<?> responseEntity = controlsApiController.modifyDeviceControls(id1, body);

    // Verify that appropriate service methods were NOT called
    verify(this.controlService, times(0)).modifyControl(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST modifyDeviceControls (error response body provided)")
  void testModifyDeviceControlsWithErrorResponseBody() {
    String header = "application/json";
    UUID id1 = UUID.fromString("a1111111-1111-1111-1111-111111111111");

    ControlModification body = new ControlModification();

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
    when(this.controlService.modifyControl(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ControlModification> arg2 = ArgumentCaptor.forClass(ControlModification.class);

    // Call method
    ResponseEntity<?> responseEntity = controlsApiController.modifyDeviceControls(id1, body);

    // Verify that appropriate service methods were called
    verify(this.controlService, times(1)).modifyControl(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(body, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a response body.");
    assertEquals(errResult, responseEntity.getBody(), "Response body must match.");
  }

  @Test
  @DisplayName("TEST deleteControl (header = application/json)")
  void testDeleteControl() {
    String header = "application/json";
    UUID id1 = UUID.fromString("a1111111-1111-1111-1111-111111111111");

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.controlService.deleteControl(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = controlsApiController.deleteControl(id1);

    // Verify that appropriate service methods were called
    verify(this.controlService, times(1)).deleteControl(arg1.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST deleteControl (header = */*)")
  void testDeleteControlHeaderAll() {
    String header = "*/*";
    UUID id1 = UUID.fromString("a1111111-1111-1111-1111-111111111111");

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.controlService.deleteControl(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = controlsApiController.deleteControl(id1);

    // Verify that appropriate service methods were called
    verify(this.controlService, times(1)).deleteControl(arg1.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST deleteControl (header = plain/text (invalid))")
  void testDeleteControlInvalidHeader() {
    String header = "plain/text";
    UUID id1 = UUID.fromString("a1111111-1111-1111-1111-111111111111");

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.controlService.deleteControl(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = controlsApiController.deleteControl(id1);

    // Verify that appropriate service methods were NOT called
    verify(this.controlService, times(0)).deleteControl(arg1.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST deleteControl (response body provided)")
  void testDeleteControlWithResponseBody() {
    String header = "application/json";
    UUID id1 = UUID.fromString("a1111111-1111-1111-1111-111111111111");

    UUID errorLogId = UUID.randomUUID();
    OffsetDateTime errorTimestamp = OffsetDateTime.now();
    ErrorType errorType = ErrorType.ERROR;
    String errorMessage = "test message";
    String errorDetails = "test details";
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = new ErrorResult();
    errResult.setType(errorType);
    errResult.setTimestamp(errorTimestamp);
    errResult.setMessage(errorMessage);
    errResult.setDetails(errorDetails);
    errResult.setLogId(errorLogId);
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.controlService.deleteControl(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = controlsApiController.deleteControl(id1);

    // Verify that appropriate service methods were called
    verify(this.controlService, times(1)).deleteControl(arg1.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a response body.");
    assertEquals(errResult, responseEntity.getBody(), "Response body must match.");
  }
}
