package com.codingcuriosity.project.simplehomeiot.controls.api;

import com.codingcuriosity.project.simplehomeiot.controls.model.ControlModification;
import com.codingcuriosity.project.simplehomeiot.controls.model.ControlRegistration;
import com.codingcuriosity.project.simplehomeiot.controls.model.DirectControlValue;
import com.codingcuriosity.project.simplehomeiot.controls.model.SliderControlValue;
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

public interface ControlsApi {

  @GetMapping(value = "/controls/devices", produces = {"application/json"})
  public abstract ResponseEntity<?> getDevicesControls();

  @GetMapping(value = "/controls/device/{deviceId}", produces = {"application/json"})
  public abstract ResponseEntity<?> getDeviceControls(@PathVariable("deviceId") UUID deviceId);

  @PostMapping(value = "/controls/device/{deviceId}", produces = {"application/json"},
      consumes = {"application/json"})
  public abstract ResponseEntity<?> registerDevicesControls(@PathVariable("deviceId") UUID deviceId,
      @Valid @RequestBody ControlRegistration body);

  @PatchMapping(value = "/controls/device/direct/{controlId}", produces = {"application/json"},
      consumes = {"application/json"})
  public abstract ResponseEntity<?> setDirectControl(@PathVariable("controlId") UUID controlId,
      @Valid @RequestBody DirectControlValue body);

  @PatchMapping(value = "/controls/device/slider/{controlId}", produces = {"application/json"},
      consumes = {"application/json"})
  public abstract ResponseEntity<?> setSliderControl(@PathVariable("controlId") UUID controlId,
      @Valid @RequestBody SliderControlValue body);

  @PatchMapping(value = "/controls/device/toggle/{controlId}", produces = {"application/json"})
  public abstract ResponseEntity<?> setToggleControl(@PathVariable("controlId") UUID controlId);

  @PutMapping(value = "/control/{controlId}", produces = {"application/json"},
      consumes = {"application/json"})
  public abstract ResponseEntity<?> modifyDeviceControls(@PathVariable("controlId") UUID controlId,
      @Valid @RequestBody ControlModification body);

  @DeleteMapping(value = "/control/{controlId}", produces = {"application/json"})
  public abstract ResponseEntity<?> deleteControl(@PathVariable("controlId") UUID controlId);

}
