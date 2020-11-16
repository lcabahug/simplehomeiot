package com.codingcuriosity.project.simplehomeiot.timers.api;

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
import com.codingcuriosity.project.simplehomeiot.timers.model.TimerInfo;
import com.codingcuriosity.project.simplehomeiot.timers.model.TimerModification;
import com.codingcuriosity.project.simplehomeiot.timers.model.TimerRegistration;
import com.codingcuriosity.project.simplehomeiot.timers.model.TimerRegistrationResponse;
import com.codingcuriosity.project.simplehomeiot.timers.model.TimerSetAction;
import com.codingcuriosity.project.simplehomeiot.timers.service.TimerService;
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
public class TimersApiControllerTest {

  TimersApiController timersApiController;

  HttpServletRequest request;

  TimerService timerService;

  @BeforeEach
  void setMocksAndInitRepo() {
    this.request = mock(HttpServletRequest.class);
    this.timerService = mock(TimerService.class);

    this.timersApiController = new TimersApiController(this.request, this.timerService);
  }

  @Test
  @DisplayName("TEST getTimers (header = application/json)")
  void testGetTimers() {
    String header = "application/json";
    Integer skip = 0;
    Integer limit = 2;

    TimerInfo timerInfo = new TimerInfo();
    List<TimerInfo> timerInfoList = new ArrayList<>();
    timerInfoList.add(timerInfo);
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonGetListResponse<TimerInfo> resp =
        new CommonGetListResponse<>(timerInfoList, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.timerService.getTimers(eq(skip), eq(limit))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<Integer> arg1 = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Integer> arg2 = ArgumentCaptor.forClass(Integer.class);

    // Call method
    ResponseEntity<?> responseEntity = timersApiController.getTimers(skip, limit);

    // Verify that appropriate service methods were called
    verify(this.timerService, times(1)).getTimers(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(skip, arg1.getValue(), "Argument must match.");
    assertEquals(limit, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a body.");
    assertEquals(timerInfoList, responseEntity.getBody(), "Response Body must match");
  }

  @Test
  @DisplayName("TEST getTimers (header = */*)")
  void testGetTimersHeaderAll() {
    String header = "*/*";
    Integer skip = 0;
    Integer limit = 2;

    TimerInfo timerInfo = new TimerInfo();
    List<TimerInfo> timerInfoList = new ArrayList<>();
    timerInfoList.add(timerInfo);
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonGetListResponse<TimerInfo> resp =
        new CommonGetListResponse<>(timerInfoList, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.timerService.getTimers(eq(skip), eq(limit))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<Integer> arg1 = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Integer> arg2 = ArgumentCaptor.forClass(Integer.class);

    // Call method
    ResponseEntity<?> responseEntity = timersApiController.getTimers(skip, limit);

    // Verify that appropriate service methods were called
    verify(this.timerService, times(1)).getTimers(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(skip, arg1.getValue(), "Argument must match.");
    assertEquals(limit, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a body.");
    assertEquals(timerInfoList, responseEntity.getBody(), "Response Body must match");
  }

  @Test
  @DisplayName("TEST getTimers (header = plain/text (invalid))")
  void testGetTimersInvalidHeader() {
    String header = "plain/text";
    Integer skip = 0;
    Integer limit = 2;

    TimerInfo timerInfo = new TimerInfo();
    List<TimerInfo> timerInfoList = new ArrayList<>();
    timerInfoList.add(timerInfo);
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonGetListResponse<TimerInfo> resp =
        new CommonGetListResponse<>(timerInfoList, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.timerService.getTimers(eq(skip), eq(limit))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<Integer> arg1 = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Integer> arg2 = ArgumentCaptor.forClass(Integer.class);

    // Call method
    ResponseEntity<?> responseEntity = timersApiController.getTimers(skip, limit);

    // Verify that appropriate service methods were NOT called
    verify(this.timerService, times(0)).getTimers(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST getTimers (error response body provided)")
  void testGetTimersWithErrorResponseBody() {
    String header = "application/json";
    Integer skip = 0;
    Integer limit = 2;

    TimerInfo timerInfo = new TimerInfo();
    List<TimerInfo> timerInfoList = new ArrayList<>();
    timerInfoList.add(timerInfo);
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
    CommonGetListResponse<TimerInfo> resp =
        new CommonGetListResponse<>(timerInfoList, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.timerService.getTimers(eq(skip), eq(limit))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<Integer> arg1 = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Integer> arg2 = ArgumentCaptor.forClass(Integer.class);

    // Call method
    ResponseEntity<?> responseEntity = timersApiController.getTimers(skip, limit);

    // Verify that appropriate service methods were called
    verify(this.timerService, times(1)).getTimers(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(skip, arg1.getValue(), "Argument must match.");
    assertEquals(limit, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a response body.");
    assertEquals(errResult, responseEntity.getBody(), "Response body must match.");
  }

  @Test
  @DisplayName("TEST getTimers (error response body not provided)")
  void testGetTimersNoErrorResponseBody() {
    String header = "application/json";
    Integer skip = 0;
    Integer limit = 2;

    TimerInfo timerInfo = new TimerInfo();
    List<TimerInfo> timerInfoList = new ArrayList<>();
    timerInfoList.add(timerInfo);
    HttpStatus response = HttpStatus.INTERNAL_SERVER_ERROR;
    ErrorResult errResult = null;
    CommonGetListResponse<TimerInfo> resp =
        new CommonGetListResponse<>(timerInfoList, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.timerService.getTimers(eq(skip), eq(limit))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<Integer> arg1 = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Integer> arg2 = ArgumentCaptor.forClass(Integer.class);

    // Call method
    ResponseEntity<?> responseEntity = timersApiController.getTimers(skip, limit);

    // Verify that appropriate service methods were called
    verify(this.timerService, times(1)).getTimers(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(skip, arg1.getValue(), "Argument must match.");
    assertEquals(limit, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST registerTimer (header = application/json)")
  void testRegisterTimer() {
    String header = "application/json";
    UUID idd = UUID.fromString("c1111111-9999-1111-1111-111111111111");

    TimerRegistration body = new TimerRegistration();

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonAddResponse resp = new CommonAddResponse(idd, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.timerService.registerTimer(eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<TimerRegistration> arg1 = ArgumentCaptor.forClass(TimerRegistration.class);

    // Call method
    ResponseEntity<?> responseEntity = timersApiController.registerTimer(body);

    // Verify that appropriate service methods were called
    verify(this.timerService, times(1)).registerTimer(arg1.capture());

    // Assertions
    assertEquals(body, arg1.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a body.");
    assertThat(responseEntity.getBody(), instanceOf(TimerRegistrationResponse.class));
  }

  @Test
  @DisplayName("TEST registerTimer (header = */*)")
  void testRegisterTimerHeaderAll() {
    String header = "*/*";
    UUID idd = UUID.fromString("c1111111-9999-1111-1111-111111111111");

    TimerRegistration body = new TimerRegistration();

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonAddResponse resp = new CommonAddResponse(idd, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.timerService.registerTimer(eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<TimerRegistration> arg1 = ArgumentCaptor.forClass(TimerRegistration.class);

    // Call method
    ResponseEntity<?> responseEntity = timersApiController.registerTimer(body);

    // Verify that appropriate service methods were called
    verify(this.timerService, times(1)).registerTimer(arg1.capture());

    // Assertions
    assertEquals(body, arg1.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a body.");
    assertThat(responseEntity.getBody(), instanceOf(TimerRegistrationResponse.class));
  }

  @Test
  @DisplayName("TEST registerTimer (header = plain/text (invalid))")
  void testRegisterTimerInvalidHeader() {
    String header = "plain/text";
    UUID idd = UUID.fromString("c1111111-9999-1111-1111-111111111111");

    TimerRegistration body = new TimerRegistration();

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonAddResponse resp = new CommonAddResponse(idd, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.timerService.registerTimer(eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<TimerRegistration> arg1 = ArgumentCaptor.forClass(TimerRegistration.class);

    // Call method
    ResponseEntity<?> responseEntity = timersApiController.registerTimer(body);

    // Verify that appropriate service methods were NOT called
    verify(this.timerService, times(0)).registerTimer(arg1.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST registerTimer (error response body not provided)")
  void testRegisterTimerNoErrorResponseBody() {
    String header = "application/json";

    TimerRegistration body = new TimerRegistration();

    HttpStatus response = HttpStatus.INTERNAL_SERVER_ERROR;
    ErrorResult errResult = null;
    CommonAddResponse resp = new CommonAddResponse(null, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.timerService.registerTimer(eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<TimerRegistration> arg1 = ArgumentCaptor.forClass(TimerRegistration.class);

    // Call method
    ResponseEntity<?> responseEntity = timersApiController.registerTimer(body);

    // Verify that appropriate service methods were called
    verify(this.timerService, times(1)).registerTimer(arg1.capture());

    // Assertions
    assertEquals(body, arg1.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST registerTimer (error response body provided)")
  void testRegisterTimerWithErrorResponseBody() {
    String header = "application/json";

    TimerRegistration body = new TimerRegistration();

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
    when(this.timerService.registerTimer(eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<TimerRegistration> arg1 = ArgumentCaptor.forClass(TimerRegistration.class);

    // Call method
    ResponseEntity<?> responseEntity = timersApiController.registerTimer(body);

    // Verify that appropriate service methods were called
    verify(this.timerService, times(1)).registerTimer(arg1.capture());

    // Assertions
    assertEquals(body, arg1.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a response body.");
    assertEquals(errResult, responseEntity.getBody(), "Response body must match.");
  }

  @Test
  @DisplayName("TEST getTimer (header = application/json)")
  void testGetTimer() {
    String header = "application/json";
    UUID id1 = UUID.fromString("d1111111-1111-1111-1111-111111111111");

    TimerInfo timerInfo = new TimerInfo();
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonGetResponse<TimerInfo> resp = new CommonGetResponse<>(timerInfo, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.timerService.getTimerById(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = timersApiController.getTimer(id1);

    // Verify that appropriate service methods were called
    verify(this.timerService, times(1)).getTimerById(arg1.capture());

    // Assertions
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a body.");
    assertEquals(timerInfo, responseEntity.getBody(), "Response Body must match");
  }

  @Test
  @DisplayName("TEST getTimer (header = */*)")
  void testGetTimerHeaderAll() {
    String header = "*/*";
    UUID id1 = UUID.fromString("d1111111-1111-1111-1111-111111111111");

    TimerInfo timerInfo = new TimerInfo();
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonGetResponse<TimerInfo> resp = new CommonGetResponse<>(timerInfo, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.timerService.getTimerById(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = timersApiController.getTimer(id1);

    // Verify that appropriate service methods were called
    verify(this.timerService, times(1)).getTimerById(arg1.capture());

    // Assertions
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a body.");
    assertEquals(timerInfo, responseEntity.getBody(), "Response Body must match");
  }

  @Test
  @DisplayName("TEST getTimer (header = plain/text (invalid))")
  void testGetTimerInvalidHeader() {
    String header = "plain/text";
    UUID id1 = UUID.fromString("d1111111-1111-1111-1111-111111111111");

    TimerInfo timerInfo = new TimerInfo();
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonGetResponse<TimerInfo> resp = new CommonGetResponse<>(timerInfo, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.timerService.getTimerById(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = timersApiController.getTimer(id1);

    // Verify that appropriate service methods were NOT called
    verify(this.timerService, times(0)).getTimerById(arg1.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST getTimer (error response body provided)")
  void testGetTimerWithErrorResponseBody() {
    String header = "application/json";
    UUID id1 = UUID.fromString("d1111111-1111-1111-1111-111111111111");

    TimerInfo timerInfo = new TimerInfo();
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
    CommonGetResponse<TimerInfo> resp = new CommonGetResponse<>(timerInfo, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.timerService.getTimerById(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = timersApiController.getTimer(id1);

    // Verify that appropriate service methods were called
    verify(this.timerService, times(1)).getTimerById(arg1.capture());

    // Assertions
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a response body.");
    assertEquals(errResult, responseEntity.getBody(), "Response body must match.");
  }

  @Test
  @DisplayName("TEST getTimer (error response body not provided)")
  void testGetTimerNoErrorResponseBody() {
    String header = "application/json";
    UUID id1 = UUID.fromString("d1111111-1111-1111-1111-111111111111");

    TimerInfo timerInfo = new TimerInfo();
    HttpStatus response = HttpStatus.INTERNAL_SERVER_ERROR;
    ErrorResult errResult = null;
    CommonGetResponse<TimerInfo> resp = new CommonGetResponse<>(timerInfo, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.timerService.getTimerById(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = timersApiController.getTimer(id1);

    // Verify that appropriate service methods were called
    verify(this.timerService, times(1)).getTimerById(arg1.capture());

    // Assertions
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST modifyTimer (header = application/json)")
  void testModifyTimer() {
    String header = "application/json";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    TimerModification body = new TimerModification();

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.timerService.modifyTimer(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerModification> arg2 = ArgumentCaptor.forClass(TimerModification.class);

    // Call method
    ResponseEntity<?> responseEntity = timersApiController.modifyTimer(id1, body);

    // Verify that appropriate service methods were called
    verify(this.timerService, times(1)).modifyTimer(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(body, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST modifyTimer (header = */*)")
  void testModifyTimerHeaderAll() {
    String header = "*/*";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    TimerModification body = new TimerModification();

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.timerService.modifyTimer(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerModification> arg2 = ArgumentCaptor.forClass(TimerModification.class);

    // Call method
    ResponseEntity<?> responseEntity = timersApiController.modifyTimer(id1, body);

    // Verify that appropriate service methods were called
    verify(this.timerService, times(1)).modifyTimer(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(body, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST modifyTimer (header = plain/text (invalid))")
  void testModifyTimerInvalidHeader() {
    String header = "plain/text";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    TimerModification body = new TimerModification();

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.timerService.modifyTimer(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerModification> arg2 = ArgumentCaptor.forClass(TimerModification.class);

    // Call method
    ResponseEntity<?> responseEntity = timersApiController.modifyTimer(id1, body);

    // Verify that appropriate service methods were NOT called
    verify(this.timerService, times(0)).modifyTimer(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST modifyTimer (error response body provided)")
  void testModifyTimerWithErrorResponseBody() {
    String header = "application/json";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    TimerModification body = new TimerModification();

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
    when(this.timerService.modifyTimer(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerModification> arg2 = ArgumentCaptor.forClass(TimerModification.class);

    // Call method
    ResponseEntity<?> responseEntity = timersApiController.modifyTimer(id1, body);

    // Verify that appropriate service methods were called
    verify(this.timerService, times(1)).modifyTimer(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(body, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a response body.");
    assertEquals(errResult, responseEntity.getBody(), "Response body must match.");
  }

  @Test
  @DisplayName("TEST setActionToTimer (header = application/json)")
  void testSetActionToTimer() {
    String header = "application/json";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    TimerSetAction body = new TimerSetAction();

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.timerService.setActionToTimer(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerSetAction> arg2 = ArgumentCaptor.forClass(TimerSetAction.class);

    // Call method
    ResponseEntity<?> responseEntity = timersApiController.setActionToTimer(id1, body);

    // Verify that appropriate service methods were called
    verify(this.timerService, times(1)).setActionToTimer(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(body, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST setActionToTimer (header = */*)")
  void testSetActionToTimerHeaderAll() {
    String header = "*/*";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    TimerSetAction body = new TimerSetAction();

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.timerService.setActionToTimer(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerSetAction> arg2 = ArgumentCaptor.forClass(TimerSetAction.class);

    // Call method
    ResponseEntity<?> responseEntity = timersApiController.setActionToTimer(id1, body);

    // Verify that appropriate service methods were called
    verify(this.timerService, times(1)).setActionToTimer(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(body, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST setActionToTimer (header = plain/text (invalid))")
  void testSetActionToTimerInvalidHeader() {
    String header = "plain/text";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    TimerSetAction body = new TimerSetAction();

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.timerService.setActionToTimer(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerSetAction> arg2 = ArgumentCaptor.forClass(TimerSetAction.class);

    // Call method
    ResponseEntity<?> responseEntity = timersApiController.setActionToTimer(id1, body);

    // Verify that appropriate service methods were NOT called
    verify(this.timerService, times(0)).setActionToTimer(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST setActionToTimer (error response body provided)")
  void testSetActionToTimerWithErrorResponseBody() {
    String header = "application/json";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    TimerSetAction body = new TimerSetAction();

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
    when(this.timerService.setActionToTimer(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<TimerSetAction> arg2 = ArgumentCaptor.forClass(TimerSetAction.class);

    // Call method
    ResponseEntity<?> responseEntity = timersApiController.setActionToTimer(id1, body);

    // Verify that appropriate service methods were called
    verify(this.timerService, times(1)).setActionToTimer(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(body, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a response body.");
    assertEquals(errResult, responseEntity.getBody(), "Response body must match.");
  }

  @Test
  @DisplayName("TEST deleteTimer (header = application/json)")
  void testDeleteTimer() {
    String header = "application/json";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.timerService.deleteTimer(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = timersApiController.deleteTimer(id1);

    // Verify that appropriate service methods were called
    verify(this.timerService, times(1)).deleteTimer(arg1.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST deleteTimer (header = */*)")
  void testDeleteTimerHeaderAll() {
    String header = "*/*";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.timerService.deleteTimer(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = timersApiController.deleteTimer(id1);

    // Verify that appropriate service methods were called
    verify(this.timerService, times(1)).deleteTimer(arg1.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST deleteTimer (header = plain/text (invalid))")
  void testDeleteTimerInvalidHeader() {
    String header = "plain/text";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.timerService.deleteTimer(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = timersApiController.deleteTimer(id1);

    // Verify that appropriate service methods were NOT called
    verify(this.timerService, times(0)).deleteTimer(arg1.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST deleteTimer (error response body provided)")
  void testDeleteTimerWithErrorResponseBody() {
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
    when(this.timerService.deleteTimer(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = timersApiController.deleteTimer(id1);

    // Verify that appropriate service methods were called
    verify(this.timerService, times(1)).deleteTimer(arg1.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a response body.");
    assertEquals(errResult, responseEntity.getBody(), "Response body must match.");
  }
}
