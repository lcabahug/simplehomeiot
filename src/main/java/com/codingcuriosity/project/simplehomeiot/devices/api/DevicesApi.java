package com.codingcuriosity.project.simplehomeiot.devices.api;

import com.codingcuriosity.project.simplehomeiot.devices.model.DeviceModification;
import com.codingcuriosity.project.simplehomeiot.devices.model.DeviceRegistration;
import com.codingcuriosity.project.simplehomeiot.devices.model.DeviceSwitchOnOff;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface DevicesApi {

  @GetMapping(value = "/devices", produces = {"application/json"})
  public abstract ResponseEntity<?> getDevices(
      @Valid @RequestParam(value = "skip", required = false) Integer skip,
      @Valid @RequestParam(value = "limit", required = false) Integer limit,
      @Valid @RequestParam(value = "includeRegistered", required = false,
          defaultValue = "true") Boolean includeRegistered,
      @Valid @RequestParam(value = "includeUnregistered", required = false,
          defaultValue = "true") Boolean includeUnregistered);

  @PostMapping(value = "/devices", produces = {"application/json"}, consumes = {"application/json"})
  public abstract ResponseEntity<?> registerDevices(@Valid @RequestBody DeviceRegistration body);

  @GetMapping(value = "/device/{deviceId}", produces = {"application/json"})
  public abstract ResponseEntity<?> getDevice(@PathVariable("deviceId") UUID deviceId);

  @PutMapping(value = "/device/{deviceId}", produces = {"application/json"},
      consumes = {"application/json"})
  public abstract ResponseEntity<?> modifyDevice(@PathVariable("deviceId") UUID deviceId,
      @Valid @RequestBody DeviceModification body);

  @PatchMapping(value = "/device/{deviceId}", produces = {"application/json"},
      consumes = {"application/json"})
  public abstract ResponseEntity<?> switchOnOffDevice(@PathVariable("deviceId") UUID deviceId,
      @Valid @RequestBody DeviceSwitchOnOff body);

  @DeleteMapping(value = "/device/{deviceId}", produces = {"application/json"})
  public abstract ResponseEntity<?> deleteDevice(@PathVariable("deviceId") UUID deviceId);

}
