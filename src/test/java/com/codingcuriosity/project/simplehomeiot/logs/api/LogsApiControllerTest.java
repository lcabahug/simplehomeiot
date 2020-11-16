package com.codingcuriosity.project.simplehomeiot.logs.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonGetListResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonOperResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.ErrorResult;
import com.codingcuriosity.project.simplehomeiot.common.model.ErrorType;
import com.codingcuriosity.project.simplehomeiot.logs.model.ControllerLogInfo;
import com.codingcuriosity.project.simplehomeiot.logs.model.DeviceLogInfo;
import com.codingcuriosity.project.simplehomeiot.logs.service.ControllerLogService;
import com.codingcuriosity.project.simplehomeiot.logs.service.DeviceLogService;
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
public class LogsApiControllerTest {

  LogsApiController logsApiController;

  HttpServletRequest request;

  ControllerLogService controllerLogService;

  DeviceLogService deviceLogService;

  @BeforeEach
  void setMocksAndInitRepo() {
    this.request = mock(HttpServletRequest.class);
    this.controllerLogService = mock(ControllerLogService.class);
    this.deviceLogService = mock(DeviceLogService.class);

    this.logsApiController =
        new LogsApiController(this.request, this.controllerLogService, this.deviceLogService);
  }

  @Test
  @DisplayName("TEST getControllerLogs (header = application/json)")
  void testGetControllerLogs() {
    String header = "application/json";
    Integer skip = 0;
    Integer limit = 2;

    ControllerLogInfo logInfo = new ControllerLogInfo();
    List<ControllerLogInfo> logInfoList = new ArrayList<>();
    logInfoList.add(logInfo);
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonGetListResponse<ControllerLogInfo> resp =
        new CommonGetListResponse<>(logInfoList, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.controllerLogService.getLogInfo(eq(skip), eq(limit))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<Integer> arg1 = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Integer> arg2 = ArgumentCaptor.forClass(Integer.class);

    // Call method
    ResponseEntity<?> responseEntity = logsApiController.getControllerLogs(skip, limit);

    // Verify that appropriate service methods were called
    verify(this.controllerLogService, times(1)).getLogInfo(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(skip, arg1.getValue(), "Argument must match.");
    assertEquals(limit, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a body.");
    assertEquals(logInfoList, responseEntity.getBody(), "Response Body must match");
  }

  @Test
  @DisplayName("TEST getControllerLogs (header = */*)")
  void testGetControllerLogsHeaderAll() {
    String header = "*/*";
    Integer skip = 0;
    Integer limit = 2;

    ControllerLogInfo logInfo = new ControllerLogInfo();
    List<ControllerLogInfo> logInfoList = new ArrayList<>();
    logInfoList.add(logInfo);
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonGetListResponse<ControllerLogInfo> resp =
        new CommonGetListResponse<>(logInfoList, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.controllerLogService.getLogInfo(eq(skip), eq(limit))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<Integer> arg1 = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Integer> arg2 = ArgumentCaptor.forClass(Integer.class);

    // Call method
    ResponseEntity<?> responseEntity = logsApiController.getControllerLogs(skip, limit);

    // Verify that appropriate service methods were called
    verify(this.controllerLogService, times(1)).getLogInfo(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(skip, arg1.getValue(), "Argument must match.");
    assertEquals(limit, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a body.");
    assertEquals(logInfoList, responseEntity.getBody(), "Response Body must match");
  }

  @Test
  @DisplayName("TEST getControllerLogs (header = plain/text (invalid))")
  void testGetControllerLogsInvalidHeader() {
    String header = "plain/text";
    Integer skip = 0;
    Integer limit = 2;

    ControllerLogInfo logInfo = new ControllerLogInfo();
    List<ControllerLogInfo> logInfoList = new ArrayList<>();
    logInfoList.add(logInfo);
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonGetListResponse<ControllerLogInfo> resp =
        new CommonGetListResponse<>(logInfoList, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.controllerLogService.getLogInfo(eq(skip), eq(limit))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<Integer> arg1 = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Integer> arg2 = ArgumentCaptor.forClass(Integer.class);

    // Call method
    ResponseEntity<?> responseEntity = logsApiController.getControllerLogs(skip, limit);

    // Verify that appropriate service methods were NOT called
    verify(this.controllerLogService, times(0)).getLogInfo(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST getControllerLogs (error response body provided)")
  void testGetControllerLogsWithErrorResponseBody() {
    String header = "application/json";
    Integer skip = 0;
    Integer limit = 2;

    ControllerLogInfo logInfo = new ControllerLogInfo();
    List<ControllerLogInfo> logInfoList = new ArrayList<>();
    logInfoList.add(logInfo);
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
    CommonGetListResponse<ControllerLogInfo> resp =
        new CommonGetListResponse<>(logInfoList, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.controllerLogService.getLogInfo(eq(skip), eq(limit))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<Integer> arg1 = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Integer> arg2 = ArgumentCaptor.forClass(Integer.class);

    // Call method
    ResponseEntity<?> responseEntity = logsApiController.getControllerLogs(skip, limit);

    // Verify that appropriate service methods were called
    verify(this.controllerLogService, times(1)).getLogInfo(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(skip, arg1.getValue(), "Argument must match.");
    assertEquals(limit, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a response body.");
    assertEquals(errResult, responseEntity.getBody(), "Response body must match.");
  }

  @Test
  @DisplayName("TEST getControllerLogs (error response body not provided)")
  void testGetControllerLogsNoErrorResponseBody() {
    String header = "application/json";
    Integer skip = 0;
    Integer limit = 2;

    ControllerLogInfo logInfo = new ControllerLogInfo();
    List<ControllerLogInfo> logInfoList = new ArrayList<>();
    logInfoList.add(logInfo);
    HttpStatus response = HttpStatus.INTERNAL_SERVER_ERROR;
    ErrorResult errResult = null;
    CommonGetListResponse<ControllerLogInfo> resp =
        new CommonGetListResponse<>(logInfoList, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.controllerLogService.getLogInfo(eq(skip), eq(limit))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<Integer> arg1 = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Integer> arg2 = ArgumentCaptor.forClass(Integer.class);

    // Call method
    ResponseEntity<?> responseEntity = logsApiController.getControllerLogs(skip, limit);

    // Verify that appropriate service methods were called
    verify(this.controllerLogService, times(1)).getLogInfo(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(skip, arg1.getValue(), "Argument must match.");
    assertEquals(limit, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST deleteControllerLogs (header = application/json)")
  void testDeleteControllerLogs() {
    String header = "application/json";

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.controllerLogService.deleteLogs()).thenReturn(resp);

    // Call method
    ResponseEntity<?> responseEntity = logsApiController.deleteControllerLogs();

    // Verify that appropriate service methods were called
    verify(this.controllerLogService, times(1)).deleteLogs();

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST deleteControllerLogs (header = */*)")
  void testDeleteControllerLogsHeaderAll() {
    String header = "*/*";

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.controllerLogService.deleteLogs()).thenReturn(resp);

    // Call method
    ResponseEntity<?> responseEntity = logsApiController.deleteControllerLogs();

    // Verify that appropriate service methods were called
    verify(this.controllerLogService, times(1)).deleteLogs();

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST deleteControllerLogs (header = plain/text (invalid))")
  void testDeleteControllerLogsInvalidHeader() {
    String header = "plain/text";

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.controllerLogService.deleteLogs()).thenReturn(resp);

    // Call method
    ResponseEntity<?> responseEntity = logsApiController.deleteControllerLogs();

    // Verify that appropriate service methods were NOT called
    verify(this.controllerLogService, times(0)).deleteLogs();

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST deleteControllerLogs (error response body provided)")
  void testDeleteControllerLogsWithErrorResponseBody() {
    String header = "application/json";

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
    when(this.controllerLogService.deleteLogs()).thenReturn(resp);

    // Call method
    ResponseEntity<?> responseEntity = logsApiController.deleteControllerLogs();

    // Verify that appropriate service methods were called
    verify(this.controllerLogService, times(1)).deleteLogs();

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a response body.");
    assertEquals(errResult, responseEntity.getBody(), "Response body must match.");
  }

  @Test
  @DisplayName("TEST getAllDevicesLogs (header = application/json)")
  void testGetAllDevicesLogs() {
    String header = "application/json";
    Integer skip = 0;
    Integer limit = 2;

    DeviceLogInfo logInfo = new DeviceLogInfo();
    List<DeviceLogInfo> logInfoList = new ArrayList<>();
    logInfoList.add(logInfo);
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonGetListResponse<DeviceLogInfo> resp =
        new CommonGetListResponse<>(logInfoList, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.deviceLogService.getLogInfo(eq(skip), eq(limit))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<Integer> arg1 = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Integer> arg2 = ArgumentCaptor.forClass(Integer.class);

    // Call method
    ResponseEntity<?> responseEntity = logsApiController.getAllDevicesLogs(skip, limit);

    // Verify that appropriate service methods were called
    verify(this.deviceLogService, times(1)).getLogInfo(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(skip, arg1.getValue(), "Argument must match.");
    assertEquals(limit, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a body.");
    assertEquals(logInfoList, responseEntity.getBody(), "Response Body must match");
  }

  @Test
  @DisplayName("TEST getAllDevicesLogs (header = */*)")
  void testGetAllDevicesLogsHeaderAll() {
    String header = "*/*";
    Integer skip = 0;
    Integer limit = 2;

    DeviceLogInfo logInfo = new DeviceLogInfo();
    List<DeviceLogInfo> logInfoList = new ArrayList<>();
    logInfoList.add(logInfo);
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonGetListResponse<DeviceLogInfo> resp =
        new CommonGetListResponse<>(logInfoList, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.deviceLogService.getLogInfo(eq(skip), eq(limit))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<Integer> arg1 = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Integer> arg2 = ArgumentCaptor.forClass(Integer.class);

    // Call method
    ResponseEntity<?> responseEntity = logsApiController.getAllDevicesLogs(skip, limit);

    // Verify that appropriate service methods were called
    verify(this.deviceLogService, times(1)).getLogInfo(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(skip, arg1.getValue(), "Argument must match.");
    assertEquals(limit, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a body.");
    assertEquals(logInfoList, responseEntity.getBody(), "Response Body must match");
  }

  @Test
  @DisplayName("TEST getAllDevicesLogs (header = plain/text (invalid))")
  void testGetAllDevicesLogsInvalidHeader() {
    String header = "plain/text";
    Integer skip = 0;
    Integer limit = 2;

    DeviceLogInfo logInfo = new DeviceLogInfo();
    List<DeviceLogInfo> logInfoList = new ArrayList<>();
    logInfoList.add(logInfo);
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonGetListResponse<DeviceLogInfo> resp =
        new CommonGetListResponse<>(logInfoList, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.deviceLogService.getLogInfo(eq(skip), eq(limit))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<Integer> arg1 = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Integer> arg2 = ArgumentCaptor.forClass(Integer.class);

    // Call method
    ResponseEntity<?> responseEntity = logsApiController.getAllDevicesLogs(skip, limit);

    // Verify that appropriate service methods were NOT called
    verify(this.deviceLogService, times(0)).getLogInfo(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST getAllDevicesLogs (error response body provided)")
  void testGetAllDevicesLogsWithErrorResponseBody() {
    String header = "application/json";
    Integer skip = 0;
    Integer limit = 2;

    DeviceLogInfo logInfo = new DeviceLogInfo();
    List<DeviceLogInfo> logInfoList = new ArrayList<>();
    logInfoList.add(logInfo);
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
    CommonGetListResponse<DeviceLogInfo> resp =
        new CommonGetListResponse<>(logInfoList, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.deviceLogService.getLogInfo(eq(skip), eq(limit))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<Integer> arg1 = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Integer> arg2 = ArgumentCaptor.forClass(Integer.class);

    // Call method
    ResponseEntity<?> responseEntity = logsApiController.getAllDevicesLogs(skip, limit);

    // Verify that appropriate service methods were called
    verify(this.deviceLogService, times(1)).getLogInfo(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(skip, arg1.getValue(), "Argument must match.");
    assertEquals(limit, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a response body.");
    assertEquals(errResult, responseEntity.getBody(), "Response body must match.");
  }

  @Test
  @DisplayName("TEST getAllDevicesLogs (error response body not provided)")
  void testGetAllDevicesLogsNoErrorResponseBody() {
    String header = "application/json";
    Integer skip = 0;
    Integer limit = 2;

    DeviceLogInfo logInfo = new DeviceLogInfo();
    List<DeviceLogInfo> logInfoList = new ArrayList<>();
    logInfoList.add(logInfo);
    HttpStatus response = HttpStatus.INTERNAL_SERVER_ERROR;
    ErrorResult errResult = null;
    CommonGetListResponse<DeviceLogInfo> resp =
        new CommonGetListResponse<>(logInfoList, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.deviceLogService.getLogInfo(eq(skip), eq(limit))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<Integer> arg1 = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Integer> arg2 = ArgumentCaptor.forClass(Integer.class);

    // Call method
    ResponseEntity<?> responseEntity = logsApiController.getAllDevicesLogs(skip, limit);

    // Verify that appropriate service methods were called
    verify(this.deviceLogService, times(1)).getLogInfo(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(skip, arg1.getValue(), "Argument must match.");
    assertEquals(limit, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST deleteDevicesLogs (header = application/json)")
  void testDeleteDevicesLogs() {
    String header = "application/json";

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.deviceLogService.deleteAllLogs()).thenReturn(resp);

    // Call method
    ResponseEntity<?> responseEntity = logsApiController.deleteDevicesLogs();

    // Verify that appropriate service methods were called
    verify(this.deviceLogService, times(1)).deleteAllLogs();

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST deleteDevicesLogs (header = */*)")
  void testDeleteDevicesLogsHeaderAll() {
    String header = "*/*";

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.deviceLogService.deleteAllLogs()).thenReturn(resp);

    // Call method
    ResponseEntity<?> responseEntity = logsApiController.deleteDevicesLogs();

    // Verify that appropriate service methods were called
    verify(this.deviceLogService, times(1)).deleteAllLogs();

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST deleteDevicesLogs (header = plain/text (invalid))")
  void testDeleteDevicesLogsInvalidHeader() {
    String header = "plain/text";

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.deviceLogService.deleteAllLogs()).thenReturn(resp);

    // Call method
    ResponseEntity<?> responseEntity = logsApiController.deleteDevicesLogs();

    // Verify that appropriate service methods were NOT called
    verify(this.deviceLogService, times(0)).deleteAllLogs();

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST deleteDevicesLogs (error response body provided)")
  void testDeleteDevicesLogsWithErrorResponseBody() {
    String header = "application/json";

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
    when(this.deviceLogService.deleteAllLogs()).thenReturn(resp);

    // Call method
    ResponseEntity<?> responseEntity = logsApiController.deleteDevicesLogs();

    // Verify that appropriate service methods were called
    verify(this.deviceLogService, times(1)).deleteAllLogs();

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a response body.");
    assertEquals(errResult, responseEntity.getBody(), "Response body must match.");
  }

  @Test
  @DisplayName("TEST getDeviceLogs (header = application/json)")
  void testGetDeviceLogs() {
    String header = "application/json";
    UUID id1 = UUID.fromString("d1111111-1111-1111-1111-111111111111");
    Integer skip = 0;
    Integer limit = 2;

    DeviceLogInfo logInfo = new DeviceLogInfo();
    List<DeviceLogInfo> logInfoList = new ArrayList<>();
    logInfoList.add(logInfo);
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonGetListResponse<DeviceLogInfo> resp =
        new CommonGetListResponse<>(logInfoList, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.deviceLogService.getFilteredLogInfo(eq(id1), eq(skip), eq(limit))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<Integer> arg2 = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Integer> arg3 = ArgumentCaptor.forClass(Integer.class);

    // Call method
    ResponseEntity<?> responseEntity = logsApiController.getDeviceLogs(id1, skip, limit);

    // Verify that appropriate service methods were called
    verify(this.deviceLogService, times(1)).getFilteredLogInfo(arg1.capture(), arg2.capture(),
        arg3.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(skip, arg2.getValue(), "Argument must match.");
    assertEquals(limit, arg3.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a body.");
    assertEquals(logInfoList, responseEntity.getBody(), "Response Body must match");
  }

  @Test
  @DisplayName("TEST getDeviceLogs (header = */*)")
  void testGetDeviceLogsHeaderAll() {
    String header = "*/*";
    UUID id1 = UUID.fromString("d1111111-1111-1111-1111-111111111111");
    Integer skip = 0;
    Integer limit = 2;

    DeviceLogInfo logInfo = new DeviceLogInfo();
    List<DeviceLogInfo> logInfoList = new ArrayList<>();
    logInfoList.add(logInfo);
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonGetListResponse<DeviceLogInfo> resp =
        new CommonGetListResponse<>(logInfoList, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.deviceLogService.getFilteredLogInfo(eq(id1), eq(skip), eq(limit))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<Integer> arg2 = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Integer> arg3 = ArgumentCaptor.forClass(Integer.class);

    // Call method
    ResponseEntity<?> responseEntity = logsApiController.getDeviceLogs(id1, skip, limit);

    // Verify that appropriate service methods were called
    verify(this.deviceLogService, times(1)).getFilteredLogInfo(arg1.capture(), arg2.capture(),
        arg3.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(skip, arg2.getValue(), "Argument must match.");
    assertEquals(limit, arg3.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a body.");
    assertEquals(logInfoList, responseEntity.getBody(), "Response Body must match");
  }

  @Test
  @DisplayName("TEST getDeviceLogs (header = plain/text (invalid))")
  void testGetDeviceLogsInvalidHeader() {
    String header = "plain/text";
    UUID id1 = UUID.fromString("d1111111-1111-1111-1111-111111111111");
    Integer skip = 0;
    Integer limit = 2;

    DeviceLogInfo logInfo = new DeviceLogInfo();
    List<DeviceLogInfo> logInfoList = new ArrayList<>();
    logInfoList.add(logInfo);
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonGetListResponse<DeviceLogInfo> resp =
        new CommonGetListResponse<>(logInfoList, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.deviceLogService.getFilteredLogInfo(eq(id1), eq(skip), eq(limit))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<Integer> arg2 = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Integer> arg3 = ArgumentCaptor.forClass(Integer.class);

    // Call method
    ResponseEntity<?> responseEntity = logsApiController.getDeviceLogs(id1, skip, limit);

    // Verify that appropriate service methods were NOT called
    verify(this.deviceLogService, times(0)).getFilteredLogInfo(arg1.capture(), arg2.capture(),
        arg3.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST getDeviceLogs (error response body provided)")
  void testGetDeviceLogsWithErrorResponseBody() {
    String header = "application/json";
    UUID id1 = UUID.fromString("d1111111-1111-1111-1111-111111111111");
    Integer skip = 0;
    Integer limit = 2;

    DeviceLogInfo logInfo = new DeviceLogInfo();
    List<DeviceLogInfo> logInfoList = new ArrayList<>();
    logInfoList.add(logInfo);
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
    CommonGetListResponse<DeviceLogInfo> resp =
        new CommonGetListResponse<>(logInfoList, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.deviceLogService.getFilteredLogInfo(eq(id1), eq(skip), eq(limit))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<Integer> arg2 = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Integer> arg3 = ArgumentCaptor.forClass(Integer.class);

    // Call method
    ResponseEntity<?> responseEntity = logsApiController.getDeviceLogs(id1, skip, limit);

    // Verify that appropriate service methods were called
    verify(this.deviceLogService, times(1)).getFilteredLogInfo(arg1.capture(), arg2.capture(),
        arg3.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(skip, arg2.getValue(), "Argument must match.");
    assertEquals(limit, arg3.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a response body.");
    assertEquals(errResult, responseEntity.getBody(), "Response body must match.");
  }

  @Test
  @DisplayName("TEST getDeviceLogs (error response body not provided)")
  void testGetDeviceLogsNoErrorResponseBody() {
    String header = "application/json";
    UUID id1 = UUID.fromString("d1111111-1111-1111-1111-111111111111");
    Integer skip = 0;
    Integer limit = 2;

    DeviceLogInfo logInfo = new DeviceLogInfo();
    List<DeviceLogInfo> logInfoList = new ArrayList<>();
    logInfoList.add(logInfo);
    HttpStatus response = HttpStatus.INTERNAL_SERVER_ERROR;
    ErrorResult errResult = null;
    CommonGetListResponse<DeviceLogInfo> resp =
        new CommonGetListResponse<>(logInfoList, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.deviceLogService.getFilteredLogInfo(eq(id1), eq(skip), eq(limit))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<Integer> arg2 = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Integer> arg3 = ArgumentCaptor.forClass(Integer.class);

    // Call method
    ResponseEntity<?> responseEntity = logsApiController.getDeviceLogs(id1, skip, limit);

    // Verify that appropriate service methods were called
    verify(this.deviceLogService, times(1)).getFilteredLogInfo(arg1.capture(), arg2.capture(),
        arg3.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(skip, arg2.getValue(), "Argument must match.");
    assertEquals(limit, arg3.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST deleteDeviceLogs (header = application/json)")
  void testDeleteDeviceLogs() {
    String header = "application/json";
    UUID id1 = UUID.fromString("d1111111-1111-1111-1111-111111111111");

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.deviceLogService.deleteSpecificLogs(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = logsApiController.deleteDeviceLogs(id1);

    // Verify that appropriate service methods were called
    verify(this.deviceLogService, times(1)).deleteSpecificLogs(arg1.capture());

    // Assertions
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST deleteDeviceLogs (header = */*)")
  void testDeleteDeviceLogsHeaderAll() {
    String header = "*/*";
    UUID id1 = UUID.fromString("d1111111-1111-1111-1111-111111111111");

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.deviceLogService.deleteSpecificLogs(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = logsApiController.deleteDeviceLogs(id1);

    // Verify that appropriate service methods were called
    verify(this.deviceLogService, times(1)).deleteSpecificLogs(arg1.capture());

    // Assertions
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST deleteDeviceLogs (header = plain/text (invalid))")
  void testDeleteDeviceLogsInvalidHeader() {
    String header = "plain/text";
    UUID id1 = UUID.fromString("d1111111-1111-1111-1111-111111111111");

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.deviceLogService.deleteSpecificLogs(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = logsApiController.deleteDeviceLogs(id1);

    // Verify that appropriate service methods were NOT called
    verify(this.deviceLogService, times(0)).deleteSpecificLogs(arg1.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST deleteDeviceLogs (error response body provided)")
  void testDeleteDeviceLogsWithErrorResponseBody() {
    String header = "application/json";
    UUID id1 = UUID.fromString("d1111111-1111-1111-1111-111111111111");

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
    when(this.deviceLogService.deleteSpecificLogs(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = logsApiController.deleteDeviceLogs(id1);

    // Verify that appropriate service methods were called
    verify(this.deviceLogService, times(1)).deleteSpecificLogs(arg1.capture());

    // Assertions
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a response body.");
    assertEquals(errResult, responseEntity.getBody(), "Response body must match.");
  }
}
