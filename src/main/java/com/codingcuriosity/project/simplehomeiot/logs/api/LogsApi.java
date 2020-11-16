package com.codingcuriosity.project.simplehomeiot.logs.api;

import java.util.UUID;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public interface LogsApi {

  @GetMapping(value = "/logs/controller", produces = {"application/json"})
  public abstract ResponseEntity<?> getControllerLogs(
      @Valid @RequestParam(value = "skip", required = false) Integer skip,
      @Valid @RequestParam(value = "limit", required = false) Integer limit);

  @DeleteMapping(value = "/logs/controller", produces = {"application/json"})
  public abstract ResponseEntity<?> deleteControllerLogs();

  @GetMapping(value = "/logs/devices", produces = {"application/json"})
  public abstract ResponseEntity<?> getAllDevicesLogs(
      @Valid @RequestParam(value = "skip", required = false) Integer skip,
      @Valid @RequestParam(value = "limit", required = false) Integer limit);

  @DeleteMapping(value = "/logs/devices", produces = {"application/json"})
  public abstract ResponseEntity<?> deleteDevicesLogs();

  @GetMapping(value = "/logs/device/{deviceId}", produces = {"application/json"})
  public abstract ResponseEntity<?> getDeviceLogs(@PathVariable("deviceId") UUID deviceId,
      @Valid @RequestParam(value = "skip", required = false) Integer skip,
      @Valid @RequestParam(value = "limit", required = false) Integer limit);

  @DeleteMapping(value = "/logs/device/{deviceId}", produces = {"application/json"})
  public abstract ResponseEntity<?> deleteDeviceLogs(@PathVariable("deviceId") UUID deviceId);

}

