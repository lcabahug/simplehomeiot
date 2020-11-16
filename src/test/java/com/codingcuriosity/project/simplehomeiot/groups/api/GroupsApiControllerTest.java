package com.codingcuriosity.project.simplehomeiot.groups.api;

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
import com.codingcuriosity.project.simplehomeiot.groups.model.EnableDisableGroup;
import com.codingcuriosity.project.simplehomeiot.groups.model.GroupInfo;
import com.codingcuriosity.project.simplehomeiot.groups.model.GroupModification;
import com.codingcuriosity.project.simplehomeiot.groups.model.GroupRegistration;
import com.codingcuriosity.project.simplehomeiot.groups.model.GroupRegistrationResponse;
import com.codingcuriosity.project.simplehomeiot.groups.service.GroupService;
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
public class GroupsApiControllerTest {

  GroupsApiController groupsApiController;

  HttpServletRequest request;

  GroupService groupService;

  @BeforeEach
  void setMocksAndInitRepo() {
    this.request = mock(HttpServletRequest.class);
    this.groupService = mock(GroupService.class);

    this.groupsApiController = new GroupsApiController(this.request, this.groupService);
  }

  @Test
  @DisplayName("TEST getGroups (header = application/json)")
  void testGetGroups() {
    String header = "application/json";
    Integer skip = 0;
    Integer limit = 2;

    GroupInfo groupInfo = new GroupInfo();
    List<GroupInfo> groupInfoList = new ArrayList<>();
    groupInfoList.add(groupInfo);
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonGetListResponse<GroupInfo> resp =
        new CommonGetListResponse<>(groupInfoList, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.groupService.getGroups(eq(skip), eq(limit))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<Integer> arg1 = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Integer> arg2 = ArgumentCaptor.forClass(Integer.class);

    // Call method
    ResponseEntity<?> responseEntity = groupsApiController.getGroups(skip, limit);

    // Verify that appropriate service methods were called
    verify(this.groupService, times(1)).getGroups(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(skip, arg1.getValue(), "Argument must match.");
    assertEquals(limit, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a body.");
    assertEquals(groupInfoList, responseEntity.getBody(), "Response Body must match");
  }

  @Test
  @DisplayName("TEST getGroups (header = */*)")
  void testGetGroupsHeaderAll() {
    String header = "*/*";
    Integer skip = 0;
    Integer limit = 2;

    GroupInfo groupInfo = new GroupInfo();
    List<GroupInfo> groupInfoList = new ArrayList<>();
    groupInfoList.add(groupInfo);
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonGetListResponse<GroupInfo> resp =
        new CommonGetListResponse<>(groupInfoList, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.groupService.getGroups(eq(skip), eq(limit))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<Integer> arg1 = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Integer> arg2 = ArgumentCaptor.forClass(Integer.class);

    // Call method
    ResponseEntity<?> responseEntity = groupsApiController.getGroups(skip, limit);

    // Verify that appropriate service methods were called
    verify(this.groupService, times(1)).getGroups(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(skip, arg1.getValue(), "Argument must match.");
    assertEquals(limit, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a body.");
    assertEquals(groupInfoList, responseEntity.getBody(), "Response Body must match");
  }

  @Test
  @DisplayName("TEST getGroups (header = plain/text (invalid))")
  void testGetGroupsInvalidHeader() {
    String header = "plain/text";
    Integer skip = 0;
    Integer limit = 2;

    GroupInfo groupInfo = new GroupInfo();
    List<GroupInfo> groupInfoList = new ArrayList<>();
    groupInfoList.add(groupInfo);
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonGetListResponse<GroupInfo> resp =
        new CommonGetListResponse<>(groupInfoList, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.groupService.getGroups(eq(skip), eq(limit))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<Integer> arg1 = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Integer> arg2 = ArgumentCaptor.forClass(Integer.class);

    // Call method
    ResponseEntity<?> responseEntity = groupsApiController.getGroups(skip, limit);

    // Verify that appropriate service methods were NOT called
    verify(this.groupService, times(0)).getGroups(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST getGroups (error response body provided)")
  void testGetGroupsWithErrorResponseBody() {
    String header = "application/json";
    Integer skip = 0;
    Integer limit = 2;

    GroupInfo groupInfo = new GroupInfo();
    List<GroupInfo> groupInfoList = new ArrayList<>();
    groupInfoList.add(groupInfo);
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
    CommonGetListResponse<GroupInfo> resp =
        new CommonGetListResponse<>(groupInfoList, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.groupService.getGroups(eq(skip), eq(limit))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<Integer> arg1 = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Integer> arg2 = ArgumentCaptor.forClass(Integer.class);

    // Call method
    ResponseEntity<?> responseEntity = groupsApiController.getGroups(skip, limit);

    // Verify that appropriate service methods were called
    verify(this.groupService, times(1)).getGroups(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(skip, arg1.getValue(), "Argument must match.");
    assertEquals(limit, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a response body.");
    assertEquals(errResult, responseEntity.getBody(), "Response body must match.");
  }

  @Test
  @DisplayName("TEST getGroups (error response body not provided)")
  void testGetGroupsNoErrorResponseBody() {
    String header = "application/json";
    Integer skip = 0;
    Integer limit = 2;

    GroupInfo groupInfo = new GroupInfo();
    List<GroupInfo> groupInfoList = new ArrayList<>();
    groupInfoList.add(groupInfo);
    HttpStatus response = HttpStatus.INTERNAL_SERVER_ERROR;
    ErrorResult errResult = null;
    CommonGetListResponse<GroupInfo> resp =
        new CommonGetListResponse<>(groupInfoList, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.groupService.getGroups(eq(skip), eq(limit))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<Integer> arg1 = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Integer> arg2 = ArgumentCaptor.forClass(Integer.class);

    // Call method
    ResponseEntity<?> responseEntity = groupsApiController.getGroups(skip, limit);

    // Verify that appropriate service methods were called
    verify(this.groupService, times(1)).getGroups(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(skip, arg1.getValue(), "Argument must match.");
    assertEquals(limit, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST registerGroup (header = application/json)")
  void testRegisterGroup() {
    String header = "application/json";
    UUID idd = UUID.fromString("c1111111-9999-1111-1111-111111111111");

    GroupRegistration body = new GroupRegistration();

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonAddResponse resp = new CommonAddResponse(idd, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.groupService.registerGroup(eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<GroupRegistration> arg1 = ArgumentCaptor.forClass(GroupRegistration.class);

    // Call method
    ResponseEntity<?> responseEntity = groupsApiController.registerGroup(body);

    // Verify that appropriate service methods were called
    verify(this.groupService, times(1)).registerGroup(arg1.capture());

    // Assertions
    assertEquals(body, arg1.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a body.");
    assertThat(responseEntity.getBody(), instanceOf(GroupRegistrationResponse.class));
  }

  @Test
  @DisplayName("TEST registerGroup (header = */*)")
  void testRegisterGroupHeaderAll() {
    String header = "*/*";
    UUID idd = UUID.fromString("c1111111-9999-1111-1111-111111111111");

    GroupRegistration body = new GroupRegistration();

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonAddResponse resp = new CommonAddResponse(idd, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.groupService.registerGroup(eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<GroupRegistration> arg1 = ArgumentCaptor.forClass(GroupRegistration.class);

    // Call method
    ResponseEntity<?> responseEntity = groupsApiController.registerGroup(body);

    // Verify that appropriate service methods were called
    verify(this.groupService, times(1)).registerGroup(arg1.capture());

    // Assertions
    assertEquals(body, arg1.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a body.");
    assertThat(responseEntity.getBody(), instanceOf(GroupRegistrationResponse.class));
  }

  @Test
  @DisplayName("TEST registerGroup (header = plain/text (invalid))")
  void testRegisterGroupInvalidHeader() {
    String header = "plain/text";
    UUID idd = UUID.fromString("c1111111-9999-1111-1111-111111111111");

    GroupRegistration body = new GroupRegistration();

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonAddResponse resp = new CommonAddResponse(idd, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.groupService.registerGroup(eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<GroupRegistration> arg1 = ArgumentCaptor.forClass(GroupRegistration.class);

    // Call method
    ResponseEntity<?> responseEntity = groupsApiController.registerGroup(body);

    // Verify that appropriate service methods were NOT called
    verify(this.groupService, times(0)).registerGroup(arg1.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST registerGroup (error response body not provided)")
  void testRegisterGroupNoErrorResponseBody() {
    String header = "application/json";

    GroupRegistration body = new GroupRegistration();

    HttpStatus response = HttpStatus.INTERNAL_SERVER_ERROR;
    ErrorResult errResult = null;
    CommonAddResponse resp = new CommonAddResponse(null, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.groupService.registerGroup(eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<GroupRegistration> arg1 = ArgumentCaptor.forClass(GroupRegistration.class);

    // Call method
    ResponseEntity<?> responseEntity = groupsApiController.registerGroup(body);

    // Verify that appropriate service methods were called
    verify(this.groupService, times(1)).registerGroup(arg1.capture());

    // Assertions
    assertEquals(body, arg1.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST registerGroup (error response body provided)")
  void testRegisterGroupWithErrorResponseBody() {
    String header = "application/json";

    GroupRegistration body = new GroupRegistration();

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
    when(this.groupService.registerGroup(eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<GroupRegistration> arg1 = ArgumentCaptor.forClass(GroupRegistration.class);

    // Call method
    ResponseEntity<?> responseEntity = groupsApiController.registerGroup(body);

    // Verify that appropriate service methods were called
    verify(this.groupService, times(1)).registerGroup(arg1.capture());

    // Assertions
    assertEquals(body, arg1.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a response body.");
    assertEquals(errResult, responseEntity.getBody(), "Response body must match.");
  }

  @Test
  @DisplayName("TEST getGroup (header = application/json)")
  void testGetGroup() {
    String header = "application/json";
    UUID id1 = UUID.fromString("d1111111-1111-1111-1111-111111111111");

    GroupInfo groupInfo = new GroupInfo();
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonGetResponse<GroupInfo> resp = new CommonGetResponse<>(groupInfo, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.groupService.getGroupById(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = groupsApiController.getGroup(id1);

    // Verify that appropriate service methods were called
    verify(this.groupService, times(1)).getGroupById(arg1.capture());

    // Assertions
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a body.");
    assertEquals(groupInfo, responseEntity.getBody(), "Response Body must match");
  }

  @Test
  @DisplayName("TEST getGroup (header = */*)")
  void testGetGroupHeaderAll() {
    String header = "*/*";
    UUID id1 = UUID.fromString("d1111111-1111-1111-1111-111111111111");

    GroupInfo groupInfo = new GroupInfo();
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonGetResponse<GroupInfo> resp = new CommonGetResponse<>(groupInfo, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.groupService.getGroupById(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = groupsApiController.getGroup(id1);

    // Verify that appropriate service methods were called
    verify(this.groupService, times(1)).getGroupById(arg1.capture());

    // Assertions
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a body.");
    assertEquals(groupInfo, responseEntity.getBody(), "Response Body must match");
  }

  @Test
  @DisplayName("TEST getGroup (header = plain/text (invalid))")
  void testGetGroupInvalidHeader() {
    String header = "plain/text";
    UUID id1 = UUID.fromString("d1111111-1111-1111-1111-111111111111");

    GroupInfo groupInfo = new GroupInfo();
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonGetResponse<GroupInfo> resp = new CommonGetResponse<>(groupInfo, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.groupService.getGroupById(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = groupsApiController.getGroup(id1);

    // Verify that appropriate service methods were NOT called
    verify(this.groupService, times(0)).getGroupById(arg1.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST getGroup (error response body provided)")
  void testGetGroupWithErrorResponseBody() {
    String header = "application/json";
    UUID id1 = UUID.fromString("d1111111-1111-1111-1111-111111111111");

    GroupInfo groupInfo = new GroupInfo();
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
    CommonGetResponse<GroupInfo> resp = new CommonGetResponse<>(groupInfo, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.groupService.getGroupById(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = groupsApiController.getGroup(id1);

    // Verify that appropriate service methods were called
    verify(this.groupService, times(1)).getGroupById(arg1.capture());

    // Assertions
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a response body.");
    assertEquals(errResult, responseEntity.getBody(), "Response body must match.");
  }

  @Test
  @DisplayName("TEST getGroup (error response body not provided)")
  void testGetGroupNoErrorResponseBody() {
    String header = "application/json";
    UUID id1 = UUID.fromString("d1111111-1111-1111-1111-111111111111");

    GroupInfo groupInfo = new GroupInfo();
    HttpStatus response = HttpStatus.INTERNAL_SERVER_ERROR;
    ErrorResult errResult = null;
    CommonGetResponse<GroupInfo> resp = new CommonGetResponse<>(groupInfo, response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.groupService.getGroupById(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = groupsApiController.getGroup(id1);

    // Verify that appropriate service methods were called
    verify(this.groupService, times(1)).getGroupById(arg1.capture());

    // Assertions
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST modifyGroup (header = application/json)")
  void testModifyGroup() {
    String header = "application/json";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    GroupModification body = new GroupModification();

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.groupService.modifyGroup(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<GroupModification> arg2 = ArgumentCaptor.forClass(GroupModification.class);

    // Call method
    ResponseEntity<?> responseEntity = groupsApiController.modifyGroup(id1, body);

    // Verify that appropriate service methods were called
    verify(this.groupService, times(1)).modifyGroup(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(body, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST modifyGroup (header = */*)")
  void testModifyGroupHeaderAll() {
    String header = "*/*";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    GroupModification body = new GroupModification();

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.groupService.modifyGroup(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<GroupModification> arg2 = ArgumentCaptor.forClass(GroupModification.class);

    // Call method
    ResponseEntity<?> responseEntity = groupsApiController.modifyGroup(id1, body);

    // Verify that appropriate service methods were called
    verify(this.groupService, times(1)).modifyGroup(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(body, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST modifyGroup (header = plain/text (invalid))")
  void testModifyGroupInvalidHeader() {
    String header = "plain/text";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    GroupModification body = new GroupModification();

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.groupService.modifyGroup(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<GroupModification> arg2 = ArgumentCaptor.forClass(GroupModification.class);

    // Call method
    ResponseEntity<?> responseEntity = groupsApiController.modifyGroup(id1, body);

    // Verify that appropriate service methods were NOT called
    verify(this.groupService, times(0)).modifyGroup(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST modifyGroup (error response body provided)")
  void testModifyGroupWithErrorResponseBody() {
    String header = "application/json";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    GroupModification body = new GroupModification();

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
    when(this.groupService.modifyGroup(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<GroupModification> arg2 = ArgumentCaptor.forClass(GroupModification.class);

    // Call method
    ResponseEntity<?> responseEntity = groupsApiController.modifyGroup(id1, body);

    // Verify that appropriate service methods were called
    verify(this.groupService, times(1)).modifyGroup(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(body, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a response body.");
    assertEquals(errResult, responseEntity.getBody(), "Response body must match.");
  }

  @Test
  @DisplayName("TEST enableDisableGroup (header = application/json)")
  void testEnableDisableGroup() {
    String header = "application/json";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    EnableDisableGroup body = new EnableDisableGroup();

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.groupService.enableDisableGroup(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<EnableDisableGroup> arg2 = ArgumentCaptor.forClass(EnableDisableGroup.class);

    // Call method
    ResponseEntity<?> responseEntity = groupsApiController.enableDisableGroup(id1, body);

    // Verify that appropriate service methods were called
    verify(this.groupService, times(1)).enableDisableGroup(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(body, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST enableDisableGroup (header = */*)")
  void testEnableDisableGroupHeaderAll() {
    String header = "*/*";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    EnableDisableGroup body = new EnableDisableGroup();

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.groupService.enableDisableGroup(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<EnableDisableGroup> arg2 = ArgumentCaptor.forClass(EnableDisableGroup.class);

    // Call method
    ResponseEntity<?> responseEntity = groupsApiController.enableDisableGroup(id1, body);

    // Verify that appropriate service methods were called
    verify(this.groupService, times(1)).enableDisableGroup(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(body, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST enableDisableGroup (header = plain/text (invalid))")
  void testEnableDisableGroupInvalidHeader() {
    String header = "plain/text";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    EnableDisableGroup body = new EnableDisableGroup();

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.groupService.enableDisableGroup(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<EnableDisableGroup> arg2 = ArgumentCaptor.forClass(EnableDisableGroup.class);

    // Call method
    ResponseEntity<?> responseEntity = groupsApiController.enableDisableGroup(id1, body);

    // Verify that appropriate service methods were NOT called
    verify(this.groupService, times(0)).enableDisableGroup(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST enableDisableGroup (error response body provided)")
  void testEnableDisableGroupWithErrorResponseBody() {
    String header = "application/json";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    EnableDisableGroup body = new EnableDisableGroup();

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
    when(this.groupService.enableDisableGroup(eq(id1), eq(body))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<EnableDisableGroup> arg2 = ArgumentCaptor.forClass(EnableDisableGroup.class);

    // Call method
    ResponseEntity<?> responseEntity = groupsApiController.enableDisableGroup(id1, body);

    // Verify that appropriate service methods were called
    verify(this.groupService, times(1)).enableDisableGroup(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(body, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a response body.");
    assertEquals(errResult, responseEntity.getBody(), "Response body must match.");
  }

  @Test
  @DisplayName("TEST deleteGroup (header = application/json)")
  void testDeleteGroup() {
    String header = "application/json";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.groupService.deleteGroup(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = groupsApiController.deleteGroup(id1);

    // Verify that appropriate service methods were called
    verify(this.groupService, times(1)).deleteGroup(arg1.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST deleteGroup (header = */*)")
  void testDeleteGroupHeaderAll() {
    String header = "*/*";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.groupService.deleteGroup(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = groupsApiController.deleteGroup(id1);

    // Verify that appropriate service methods were called
    verify(this.groupService, times(1)).deleteGroup(arg1.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST deleteGroup (header = plain/text (invalid))")
  void testDeleteGroupInvalidHeader() {
    String header = "plain/text";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.groupService.deleteGroup(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = groupsApiController.deleteGroup(id1);

    // Verify that appropriate service methods were NOT called
    verify(this.groupService, times(0)).deleteGroup(arg1.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST deleteGroup (error response body provided)")
  void testDeleteGroupWithErrorResponseBody() {
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
    when(this.groupService.deleteGroup(eq(id1))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = groupsApiController.deleteGroup(id1);

    // Verify that appropriate service methods were called
    verify(this.groupService, times(1)).deleteGroup(arg1.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a response body.");
    assertEquals(errResult, responseEntity.getBody(), "Response body must match.");
  }

  @Test
  @DisplayName("TEST addDeviceToGroup (header = application/json)")
  void testAddDeviceToGroup() {
    String header = "application/json";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");
    UUID devId = UUID.fromString("c1111111-0000-1111-1111-111111111111");

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.groupService.addDeviceToGroup(eq(id1), eq(devId))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = groupsApiController.addDeviceToGroup(id1, devId);

    // Verify that appropriate service methods were called
    verify(this.groupService, times(1)).addDeviceToGroup(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(devId, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST addDeviceToGroup (header = */*)")
  void testAddDeviceToGroupHeaderAll() {
    String header = "*/*";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");
    UUID devId = UUID.fromString("c1111111-0000-1111-1111-111111111111");

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.groupService.addDeviceToGroup(eq(id1), eq(devId))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = groupsApiController.addDeviceToGroup(id1, devId);

    // Verify that appropriate service methods were called
    verify(this.groupService, times(1)).addDeviceToGroup(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(devId, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST addDeviceToGroup (header = plain/text (invalid))")
  void testAddDeviceToGroupInvalidHeader() {
    String header = "plain/text";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");
    UUID devId = UUID.fromString("c1111111-0000-1111-1111-111111111111");

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.groupService.addDeviceToGroup(eq(id1), eq(devId))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = groupsApiController.addDeviceToGroup(id1, devId);

    // Verify that appropriate service methods were NOT called
    verify(this.groupService, times(0)).addDeviceToGroup(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST addDeviceToGroup (error response body provided)")
  void testAddDeviceToGroupWithErrorResponseBody() {
    String header = "application/json";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");
    UUID devId = UUID.fromString("c1111111-0000-1111-1111-111111111111");

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
    when(this.groupService.addDeviceToGroup(eq(id1), eq(devId))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = groupsApiController.addDeviceToGroup(id1, devId);

    // Verify that appropriate service methods were called
    verify(this.groupService, times(1)).addDeviceToGroup(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(devId, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a response body.");
    assertEquals(errResult, responseEntity.getBody(), "Response body must match.");
  }

  @Test
  @DisplayName("TEST deleteDeviceFromGroup (header = application/json)")
  void testDeleteDeviceFromGroup() {
    String header = "application/json";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");
    UUID devId = UUID.fromString("c1111111-0000-1111-1111-111111111111");

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.groupService.deleteDeviceFromGroup(eq(id1), eq(devId))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = groupsApiController.deleteDeviceFromGroup(id1, devId);

    // Verify that appropriate service methods were called
    verify(this.groupService, times(1)).deleteDeviceFromGroup(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(devId, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST deleteDeviceFromGroup (header = */*)")
  void testDeleteDeviceFromGroupHeaderAll() {
    String header = "*/*";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");
    UUID devId = UUID.fromString("c1111111-0000-1111-1111-111111111111");

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.groupService.deleteDeviceFromGroup(eq(id1), eq(devId))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = groupsApiController.deleteDeviceFromGroup(id1, devId);

    // Verify that appropriate service methods were called
    verify(this.groupService, times(1)).deleteDeviceFromGroup(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(devId, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST deleteDeviceFromGroup (header = plain/text (invalid))")
  void testDeleteDeviceFromGroupInvalidHeader() {
    String header = "plain/text";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");
    UUID devId = UUID.fromString("c1111111-0000-1111-1111-111111111111");

    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;
    CommonOperResponse resp = new CommonOperResponse(response, errResult);

    // Mocks
    when(this.request.getHeader(eq("Accept"))).thenReturn(header);
    when(this.groupService.deleteDeviceFromGroup(eq(id1), eq(devId))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = groupsApiController.deleteDeviceFromGroup(id1, devId);

    // Verify that appropriate service methods were NOT called
    verify(this.groupService, times(0)).deleteDeviceFromGroup(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode(),
        "Http Status Code must match.");
    assertNull(responseEntity.getBody(), "There is no body.");
  }

  @Test
  @DisplayName("TEST deleteDeviceFromGroup (error response body provided)")
  void testDeleteDeviceFromGroupWithErrorResponseBody() {
    String header = "application/json";
    UUID id1 = UUID.fromString("c1111111-1111-1111-1111-111111111111");
    UUID devId = UUID.fromString("c1111111-0000-1111-1111-111111111111");

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
    when(this.groupService.deleteDeviceFromGroup(eq(id1), eq(devId))).thenReturn(resp);

    // Argument Captors
    ArgumentCaptor<UUID> arg1 = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<UUID> arg2 = ArgumentCaptor.forClass(UUID.class);

    // Call method
    ResponseEntity<?> responseEntity = groupsApiController.deleteDeviceFromGroup(id1, devId);

    // Verify that appropriate service methods were called
    verify(this.groupService, times(1)).deleteDeviceFromGroup(arg1.capture(), arg2.capture());

    // Assertions
    assertNotNull(responseEntity, "Response is not null.");
    assertEquals(id1, arg1.getValue(), "Argument must match.");
    assertEquals(devId, arg2.getValue(), "Argument must match.");
    assertEquals(response, responseEntity.getStatusCode(), "Http Status Code must match.");
    assertNotNull(responseEntity.getBody(), "There is a response body.");
    assertEquals(errResult, responseEntity.getBody(), "Response body must match.");
  }
}
