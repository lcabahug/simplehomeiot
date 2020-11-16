package com.codingcuriosity.project.simplehomeiot.zones.api;

import com.codingcuriosity.project.simplehomeiot.zones.model.ZoneCreation;
import com.codingcuriosity.project.simplehomeiot.zones.model.ZoneModification;
import com.codingcuriosity.project.simplehomeiot.zones.model.ZoneSetDevice;
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

public interface ZonesApi {

  @GetMapping(value = "/zones", produces = {"application/json"})
  public abstract ResponseEntity<?> getZones(
      @Valid @RequestParam(value = "skip", required = false) Integer skip,
      @Valid @RequestParam(value = "limit", required = false) Integer limit);

  @PostMapping(value = "/zones", produces = {"application/json"}, consumes = {"application/json"})
  public abstract ResponseEntity<?> createZone(@Valid @RequestBody ZoneCreation body);

  @GetMapping(value = "/zone/{zoneId}", produces = {"application/json"})
  public abstract ResponseEntity<?> getZone(@PathVariable("zoneId") UUID zoneId);

  @PutMapping(value = "/zone/{zoneId}", produces = {"application/json"},
      consumes = {"application/json"})
  public abstract ResponseEntity<?> modifyZone(@PathVariable("zoneId") UUID zoneId,
      @Valid @RequestBody ZoneModification body);

  @PatchMapping(value = "/zone/{zoneId}", produces = {"application/json"},
      consumes = {"application/json"})
  public abstract ResponseEntity<?> addDeviceToZone(@PathVariable("zoneId") UUID zoneId,
      @Valid @RequestBody ZoneSetDevice body);

  @DeleteMapping(value = "/zone/{zoneId}", produces = {"application/json"})
  public abstract ResponseEntity<?> deleteZone(@PathVariable("zoneId") UUID zoneId);

}
