package com.codingcuriosity.project.simplehomeiot.zone.api;

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
import com.codingcuriosity.project.simplehomeiot.zones.api.ZonesApiController;
import com.codingcuriosity.project.simplehomeiot.zones.model.ZoneCreation;
import com.codingcuriosity.project.simplehomeiot.zones.model.ZoneInfo;
import com.codingcuriosity.project.simplehomeiot.zones.model.ZoneModification;
import com.codingcuriosity.project.simplehomeiot.zones.model.ZoneRegistrationResponse;
import com.codingcuriosity.project.simplehomeiot.zones.model.ZoneSetDevice;
import com.codingcuriosity.project.simplehomeiot.zones.service.ZoneService;
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
public class ZonesApiControllerTest {

  ZonesApiController zonesApiController;

  HttpServletRequest request;

  ZoneService zoneService;

  @BeforeEach
  void setMocksAndInitRepo() {
    this.request = mock(HttpServletRequest.class);
    this.zoneService = mock(ZoneService.class);

    this.zonesApiController = new ZonesApiController(this.request, this.zoneService);
  }

  @Test
  @DisplayName("TEST getZones (header = application/json)")
  void testGetZones() {
    String header = "application/json";
    Integer skip = 0;
    Integer limit = 2;

    ZoneInfo zoneInfo = new ZoneInfo();
    List<ZoneInfo> zoneInfoList = new ArrayList<>();
    zoneInfoList.add(zoneInfo);
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonGetListResponse<ZoneInfo> resp =
        new CommonGetListResponse<>(zoneInfoList, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.zoneService.getZones(eq(skip), eq(limit))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<Integer> arg1 = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Integer> arg2 = ArgumentCaptor.forClass(Integer.class);

    // Call method
    ResponseEntity<?> responseEntity = zonesApiController.getZones(skip, limit);

    // Verify that appropriate service methods were called
    verify(this.zoneService, times(1)).getZones(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(skip, arg1.getValue(), "Argument must match.");
    assertEquals(limit, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a body.");
    assertEquals(zoneInfoList, responseEntity.getBody(), "Response Body must match");
  }

  @Test
  @DisplayName("TEST getZones (header = */*)")
  void testGetZonesHeaderAll() {
    String header = "*/*";
    Integer skip = 0;
    Integer limit = 2;

    ZoneInfo zoneInfo = new ZoneInfo();
    List<ZoneInfo> zoneInfoList = new ArrayList<>();
    zoneInfoList.add(zoneInfo);
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonGetListResponse<ZoneInfo> resp =
        new CommonGetListResponse<>(zoneInfoList, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.zoneService.getZones(eq(skip), eq(limit))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<Integer> arg1 = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Integer> arg2 = ArgumentCaptor.forClass(Integer.class);

    // Call method
    ResponseEntity<?> responseEntity = zonesApiController.getZones(skip, limit);

    // Verify that appropriate service methods were called
    verify(this.zoneService, times(1)).getZones(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(skip, arg1.getValue(), "Argument must match.");
    assertEquals(limit, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a body.");
    assertEquals(zoneInfoList, responseEntity.getBody(), "Response Body must match");
  }

  @Test
  @DisplayName("TEST getZones (header = plain/text (invalid))")
  void testGetZonesInvalidHeader() {
    String header = "plain/text";
    Integer skip = 0;
    Integer limit = 2;

    ZoneInfo zoneInfo = new ZoneInfo();
    List<ZoneInfo> zoneInfoList = new ArrayList<>();
    zoneInfoList.add(zoneInfo);
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonGetListResponse<ZoneInfo> resp =
        new CommonGetListResponse<>(zoneInfoList, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.zoneService.getZones(eq(skip), eq(limit))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<Integer> arg1 = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Integer> arg2 = ArgumentCaptor.forClass(Integer.class);

    // Call method
    ResponseEntity<?> responseEntity = zonesApiController.getZones(skip, limit);

    // Verify that appropriate service methods were NOT called
    verify(this.zoneService, times(0)).getZones(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST getZones (error response body provided)")
  void testGetZonesWithErrorResponseBody() {
    String header = "application/json";
    Integer skip = 0;
    Integer limit = 2;

    ZoneInfo zoneInfo = new ZoneInfo();
    List<ZoneInfo> zoneInfoList = new ArrayList<>();
    zoneInfoList.add(zoneInfo);
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
    CommonGetListResponse<ZoneInfo> resp =
        new CommonGetListResponse<>(zoneInfoList, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.zoneService.getZones(eq(skip), eq(limit))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<Integer> arg1 = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Integer> arg2 = ArgumentCaptor.forClass(Integer.class);

    // Call method
    ResponseEntity<?> responseEntity = zonesApiController.getZones(skip, limit);

    // Verify that appropriate service methods were called
    verify(this.zoneService, times(1)).getZones(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(skip, arg1.getValue(), "Argument must match.");
    assertEquals(limit, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a response body.");
    assertEquals(errResult, responseEntity.getBody(), "Response body must match.");
  }

  @Test
  @DisplayName("TEST getZones (error response body not provided)")
  void testGetZonesNoErrorResponseBody() {
    String header = "application/json";
    Integer skip = 0;
    Integer limit = 2;

    ZoneInfo zoneInfo = new ZoneInfo();
    List<ZoneInfo> zoneInfoList = new ArrayList<>();
    zoneInfoList.add(zoneInfo);
    HttpStatus response = HttpStatus.INTERNAL_SERVER_ERROR;
    ErrorResult errResult = null;
    CommonGetListResponse<ZoneInfo> resp =
        new CommonGetListResponse<>(zoneInfoList, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.zoneService.getZones(eq(skip), eq(limit))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<Integer> arg1 = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Integer> arg2 = ArgumentCaptor.forClass(Integer.class);

    // Call method
    ResponseEntity<?> responseEntity = zonesApiController.getZones(skip, limit);

    // Verify that appropriate service methods were called
    verify(this.zoneService, times(1)).getZones(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(skip, arg1.getValue(), "Argument must match.");
    assertEquals(limit, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST createZone (header = application/json)")
  void testCreateZone() {
    String header = "application/json";
    UUID idd = UUID.fromString("c1111111-9999-1111-1111-111111111111");

    ZoneCreation body = new ZoneCreation();

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonAddResponse resp = new CommonAddResponse(idd, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.zoneService.registerZone(eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<ZoneCreation> arg1 = ArgumentCaptor.forClass(ZoneCreation.class);

    // Call method
    ResponseEntity<?> responseEntity = zonesApiController.createZone(body);

    // Verify that appropriate service methods were called
    verify(this.zoneService, times(1)).registerZone(arg1.capture());

    // Assertions
    assertEquals(body, arg1.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a body.");
    assertThat(responseEntity.getBody(), instanceOf(ZoneRegistrationResponse.class));
  }

  @Test
  @DisplayName("TEST createZone (header = */*)")
  void testCreateZoneHeaderAll() {
    String header = "*/*";
    UUID idd = UUID.fromString("c1111111-9999-1111-1111-111111111111");

    ZoneCreation body = new ZoneCreation();

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonAddResponse resp = new CommonAddResponse(idd, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.zoneService.registerZone(eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<ZoneCreation> arg1 = ArgumentCaptor.forClass(ZoneCreation.class);

    // Call method
    ResponseEntity<?> responseEntity = zonesApiController.createZone(body);

    // Verify that appropriate service methods were called
    verify(this.zoneService, times(1)).registerZone(arg1.capture());

    // Assertions
    assertEquals(body, arg1.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a body.");
    assertThat(responseEntity.getBody(), instanceOf(ZoneRegistrationResponse.class));
  }

  @Test
  @DisplayName("TEST createZone (header = plain/text (invalid))")
  void testCreateZoneInvalidHeader() {
    String header = "plain/text";
    UUID idd = UUID.fromString("c1111111-9999-1111-1111-111111111111");

    ZoneCreation body = new ZoneCreation();

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonAddResponse resp = new CommonAddResponse(idd, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.zoneService.registerZone(eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<ZoneCreation> arg1 = ArgumentCaptor.forClass(ZoneCreation.class);

    // Call method
    ResponseEntity<?> responseEntity = zonesApiController.createZone(body);

    // Verify that appropriate service methods were NOT called
    verify(this.zoneService, times(0)).registerZone(arg1.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST createZone (error response body not provided)")
  void testCreateZoneNoErrorResponseBody() {
    String header = "application/json";

    ZoneCreation body = new ZoneCreation();

    HttpStatus response = HttpStatus.INTERNAL_SERVER_ERROR;
    ErrorResult errResult = null;
    CommonAddResponse resp = new CommonAddResponse(null, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.zoneService.registerZone(eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<ZoneCreation> arg1 = ArgumentCaptor.forClass(ZoneCreation.class);

    // Call method
    ResponseEntity<?> responseEntity = zonesApiController.createZone(body);

    // Verify that appropriate service methods were called
    verify(this.zoneService, times(1)).registerZone(arg1.capture());

    // Assertions
    assertEquals(body, arg1.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST createZone (error response body provided)")
  void testCreateZoneWithErrorResponseBody() {
    String header = "application/json";

    ZoneCreation body = new ZoneCreation();

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
    when(this.zoneService.registerZone(eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<ZoneCreation> arg1 = ArgumentCaptor.forClass(ZoneCreation.class);

    // Call method
    ResponseEntity<?> responseEntity = zonesApiController.createZone(body);

    // Verify that appropriate service methods were called
    verify(this.zoneService, times(1)).registerZone(arg1.capture());

    // Assertions
    assertEquals(body, arg1.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a response body.");
    assertEquals(errResult, responseEntity.getBody(), "Response body must match.");
  }

  @Test
  @DisplayName("TEST getZone (header = application/json)")
  void testGetZone() {
    String header = "application/json";
    UUID id1 = UUID.fromString("d1111111-1111-1111-1111-111111111111");

    ZoneInfo zoneInfo = new ZoneInfo();
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonGetResponse<ZoneInfo> resp = new CommonGetResponse<>(zoneInfo, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.zoneService.getZoneById(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = zonesApiController.getZone(id1);

    // Verify that appropriate service methods were called
    verify(this.zoneService, times(1)).getZoneById(arg1.capture());

    // Assertions
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a body.");
    assertEquals(zoneInfo, responseEntity.getBody(), "Response Body must match");
  }

  @Test
  @DisplayName("TEST getZone (header = */*)")
  void testGetZoneHeaderAll() {
    String header = "*/*";
    UUID id1 = UUID.fromString("d1111111-1111-1111-1111-111111111111");

    ZoneInfo zoneInfo = new ZoneInfo();
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonGetResponse<ZoneInfo> resp = new CommonGetResponse<>(zoneInfo, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.zoneService.getZoneById(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = zonesApiController.getZone(id1);

    // Verify that appropriate service methods were called
    verify(this.zoneService, times(1)).getZoneById(arg1.capture());

    // Assertions
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a body.");
    assertEquals(zoneInfo, responseEntity.getBody(), "Response Body must match");
  }

  @Test
  @DisplayName("TEST getZone (header = plain/text (invalid))")
  void testGetZoneInvalidHeader() {
    String header = "plain/text";
    UUID id1 = UUID.fromString("d1111111-1111-1111-1111-111111111111");

    ZoneInfo zoneInfo = new ZoneInfo();
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonGetResponse<ZoneInfo> resp = new CommonGetResponse<>(zoneInfo, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.zoneService.getZoneById(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = zonesApiController.getZone(id1);

    // Verify that appropriate service methods were NOT called
    verify(this.zoneService, times(0)).getZoneById(arg1.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST getZone (error response body provided)")
  void testGetZoneWithErrorResponseBody() {
    String header = "application/json";
    UUID id1 = UUID.fromString("d1111111-1111-1111-1111-111111111111");

    ZoneInfo zoneInfo = new ZoneInfo();
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
    CommonGetResponse<ZoneInfo> resp = new CommonGetResponse<>(zoneInfo, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.zoneService.getZoneById(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = zonesApiController.getZone(id1);

    // Verify that appropriate service methods were called
    verify(this.zoneService, times(1)).getZoneById(arg1.capture());

    // Assertions
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a response body.");
    assertEquals(errResult, responseEntity.getBody(), "Response body must match.");
  }

  @Test
  @DisplayName("TEST getZone (error response body not provided)")
  void testGetZoneNoErrorResponseBody() {
    String header = "application/json";
    UUID id1 = UUID.fromString("d1111111-1111-1111-1111-111111111111");

    ZoneInfo zoneInfo = new ZoneInfo();
    HttpStatus response = HttpStatus.INTERNAL_SERVER_ERROR;
    ErrorResult errResult = null;
    CommonGetResponse<ZoneInfo> resp = new CommonGetResponse<>(zoneInfo, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.zoneService.getZoneById(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = zonesApiController.getZone(id1);

    // Verify that appropriate service methods were called
    verify(this.zoneService, times(1)).getZoneById(arg1.capture());

    // Assertions
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST modifyZone (header = application/json)")
  void testModifyZone() {
    String header = "application/json";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    ZoneModification body = new ZoneModification();

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.zoneService.modifyZone(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ZoneModification> arg2 = ArgumentCaptor.forClass(ZoneModification.class);

    // Call method
    ResponseEntity<?> responseEntity = zonesApiController.modifyZone(id1, body);

    // Verify that appropriate service methods were called
    verify(this.zoneService, times(1)).modifyZone(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(body, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST modifyZone (header = */*)")
  void testModifyZoneHeaderAll() {
    String header = "*/*";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    ZoneModification body = new ZoneModification();

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.zoneService.modifyZone(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ZoneModification> arg2 = ArgumentCaptor.forClass(ZoneModification.class);

    // Call method
    ResponseEntity<?> responseEntity = zonesApiController.modifyZone(id1, body);

    // Verify that appropriate service methods were called
    verify(this.zoneService, times(1)).modifyZone(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(body, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST modifyZone (header = plain/text (invalid))")
  void testModifyZoneInvalidHeader() {
    String header = "plain/text";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    ZoneModification body = new ZoneModification();

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.zoneService.modifyZone(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ZoneModification> arg2 = ArgumentCaptor.forClass(ZoneModification.class);

    // Call method
    ResponseEntity<?> responseEntity = zonesApiController.modifyZone(id1, body);

    // Verify that appropriate service methods were NOT called
    verify(this.zoneService, times(0)).modifyZone(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST modifyZone (error response body provided)")
  void testModifyZoneWithErrorResponseBody() {
    String header = "application/json";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    ZoneModification body = new ZoneModification();

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
    when(this.zoneService.modifyZone(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ZoneModification> arg2 = ArgumentCaptor.forClass(ZoneModification.class);

    // Call method
    ResponseEntity<?> responseEntity = zonesApiController.modifyZone(id1, body);

    // Verify that appropriate service methods were called
    verify(this.zoneService, times(1)).modifyZone(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(body, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a response body.");
    assertEquals(errResult, responseEntity.getBody(), "Response body must match.");
  }

  @Test
  @DisplayName("TEST addDeviceToZone (header = application/json)")
  void testAddDeviceToZone() {
    String header = "application/json";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    ZoneSetDevice body = new ZoneSetDevice();

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.zoneService.setDevicesToZone(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ZoneSetDevice> arg2 = ArgumentCaptor.forClass(ZoneSetDevice.class);

    // Call method
    ResponseEntity<?> responseEntity = zonesApiController.addDeviceToZone(id1, body);

    // Verify that appropriate service methods were called
    verify(this.zoneService, times(1)).setDevicesToZone(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(body, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST addDeviceToZone (header = */*)")
  void testAddDeviceToZoneHeaderAll() {
    String header = "*/*";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    ZoneSetDevice body = new ZoneSetDevice();

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.zoneService.setDevicesToZone(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ZoneSetDevice> arg2 = ArgumentCaptor.forClass(ZoneSetDevice.class);

    // Call method
    ResponseEntity<?> responseEntity = zonesApiController.addDeviceToZone(id1, body);

    // Verify that appropriate service methods were called
    verify(this.zoneService, times(1)).setDevicesToZone(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(body, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST addDeviceToZone (header = plain/text (invalid))")
  void testAddDeviceToZoneInvalidHeader() {
    String header = "plain/text";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    ZoneSetDevice body = new ZoneSetDevice();

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.zoneService.setDevicesToZone(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ZoneSetDevice> arg2 = ArgumentCaptor.forClass(ZoneSetDevice.class);

    // Call method
    ResponseEntity<?> responseEntity = zonesApiController.addDeviceToZone(id1, body);

    // Verify that appropriate service methods were NOT called
    verify(this.zoneService, times(0)).setDevicesToZone(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST addDeviceToZone (error response body provided)")
  void testAddDeviceToZoneWithErrorResponseBody() {
    String header = "application/json";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    ZoneSetDevice body = new ZoneSetDevice();

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
    when(this.zoneService.setDevicesToZone(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<ZoneSetDevice> arg2 = ArgumentCaptor.forClass(ZoneSetDevice.class);

    // Call method
    ResponseEntity<?> responseEntity = zonesApiController.addDeviceToZone(id1, body);

    // Verify that appropriate service methods were called
    verify(this.zoneService, times(1)).setDevicesToZone(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(body, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a response body.");
    assertEquals(errResult, responseEntity.getBody(), "Response body must match.");
  }

  @Test
  @DisplayName("TEST deleteZone (header = application/json)")
  void testDeleteZone() {
    String header = "application/json";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.zoneService.deleteZone(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = zonesApiController.deleteZone(id1);

    // Verify that appropriate service methods were called
    verify(this.zoneService, times(1)).deleteZone(arg1.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST deleteZone (header = */*)")
  void testDeleteZoneHeaderAll() {
    String header = "*/*";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.zoneService.deleteZone(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = zonesApiController.deleteZone(id1);

    // Verify that appropriate service methods were called
    verify(this.zoneService, times(1)).deleteZone(arg1.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST deleteZone (header = plain/text (invalid))")
  void testDeleteZoneInvalidHeader() {
    String header = "plain/text";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.zoneService.deleteZone(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = zonesApiController.deleteZone(id1);

    // Verify that appropriate service methods were NOT called
    verify(this.zoneService, times(0)).deleteZone(arg1.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST deleteZone (error response body provided)")
  void testDeleteZoneWithErrorResponseBody() {
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
    when(this.zoneService.deleteZone(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = zonesApiController.deleteZone(id1);

    // Verify that appropriate service methods were called
    verify(this.zoneService, times(1)).deleteZone(arg1.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a response body.");
    assertEquals(errResult, responseEntity.getBody(), "Response body must match.");
  }
}
