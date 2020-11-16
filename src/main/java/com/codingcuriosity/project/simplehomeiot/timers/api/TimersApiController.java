package com.codingcuriosity.project.simplehomeiot.timers.api;

import com.codingcuriosity.project.simplehomeiot.common.Utils;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonAddResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonGetListResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonGetResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonOperResponse;
import com.codingcuriosity.project.simplehomeiot.timers.model.TimerInfo;
import com.codingcuriosity.project.simplehomeiot.timers.model.TimerModification;
import com.codingcuriosity.project.simplehomeiot.timers.model.TimerRegistration;
import com.codingcuriosity.project.simplehomeiot.timers.model.TimerRegistrationResponse;
import com.codingcuriosity.project.simplehomeiot.timers.model.TimerSetAction;
import com.codingcuriosity.project.simplehomeiot.timers.service.TimerService;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TimersApiController implements TimersApi {

  private final HttpServletRequest request;
  private final TimerService timerService;

  @Autowired
  public TimersApiController(HttpServletRequest request, TimerService timerService) {
    this.request = request;
    this.timerService = timerService;
  }

  @Override
  public ResponseEntity<?> getTimers(@Valid Integer skip, @Valid Integer limit) {
    if (Utils.isClientNoSupportJsonResponse(request)) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    CommonGetListResponse<TimerInfo> getListResp = timerService.getTimers(skip, limit);
    if (getListResp.isError()) {
      if (getListResp.getErrorResult() == null) {
        return ResponseEntity.status(getListResp.getResponse()).build();
      } else {
        return ResponseEntity.status(getListResp.getResponse()).body(getListResp.getErrorResult());
      }
    }
    return ResponseEntity.ok().body(getListResp.getObjList());
  }

  @Override
  public ResponseEntity<?> registerTimer(@Valid TimerRegistration body) {
    if (Utils.isClientNoSupportJsonResponse(request)) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    CommonAddResponse registerResponse = timerService.registerTimer(body);
    if (registerResponse.getId() != null) {
      TimerRegistrationResponse respBody =
          new TimerRegistrationResponse().timerId(registerResponse.getId());
      return ResponseEntity.status(HttpStatus.CREATED).body(respBody);
    } else if (registerResponse.getErrorResult() == null) {
      return ResponseEntity.status(registerResponse.getResponse()).build();
    } else {
      return ResponseEntity.status(registerResponse.getResponse())
          .body(registerResponse.getErrorResult());
    }
  }

  @Override
  public ResponseEntity<?> getTimer(UUID timerId) {
    if (Utils.isClientNoSupportJsonResponse(request)) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    CommonGetResponse<TimerInfo> getResp = timerService.getTimerById(timerId);
    if (getResp.isError()) {
      if (getResp.getErrorResult() != null) {
        return ResponseEntity.status(getResp.getResponse()).body(getResp.getErrorResult());
      } else {
        return ResponseEntity.status(getResp.getResponse()).build();
      }
    }

    TimerInfo timerInfo = getResp.getObj();
    return ResponseEntity.ok().body(timerInfo);
  }

  @Override
  public ResponseEntity<?> modifyTimer(UUID timerId, @Valid TimerModification body) {
    if (Utils.isClientNoSupportJsonResponse(request)) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    CommonOperResponse updateResponse = timerService.modifyTimer(timerId, body);
    if (updateResponse.getErrorResult() == null) {
      return ResponseEntity.status(updateResponse.getResponse()).build();
    } else {
      return ResponseEntity.status(updateResponse.getResponse())
          .body(updateResponse.getErrorResult());
    }
  }

  @Override
  public ResponseEntity<?> setActionToTimer(UUID timerId, @Valid TimerSetAction body) {
    if (Utils.isClientNoSupportJsonResponse(request)) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    CommonOperResponse patchResponse = timerService.setActionToTimer(timerId, body);
    if (patchResponse.getErrorResult() == null) {
      return ResponseEntity.status(patchResponse.getResponse()).build();
    } else {
      return ResponseEntity.status(patchResponse.getResponse())
          .body(patchResponse.getErrorResult());
    }
  }

  @Override
  public ResponseEntity<?> deleteTimer(UUID timerId) {
    if (Utils.isClientNoSupportJsonResponse(request)) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    CommonOperResponse deleteResponse = timerService.deleteTimer(timerId);
    if (deleteResponse.getErrorResult() == null) {
      return ResponseEntity.status(deleteResponse.getResponse()).build();
    } else {
      return ResponseEntity.status(deleteResponse.getResponse())
          .body(deleteResponse.getErrorResult());
    }
  }

}
