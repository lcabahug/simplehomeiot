package com.codingcuriosity.project.simplehomeiot.groups.api;

import com.codingcuriosity.project.simplehomeiot.groups.model.EnableDisableGroup;
import com.codingcuriosity.project.simplehomeiot.groups.model.GroupModification;
import com.codingcuriosity.project.simplehomeiot.groups.model.GroupRegistration;
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

public interface GroupsApi {

  @GetMapping(value = "/groups", produces = {"application/json"})
  public abstract ResponseEntity<?> getGroups(
      @Valid @RequestParam(value = "skip", required = false) Integer skip,
      @Valid @RequestParam(value = "limit", required = false) Integer limit);

  @PostMapping(value = "/groups", produces = {"application/json"}, consumes = {"application/json"})
  public abstract ResponseEntity<?> registerGroup(@Valid @RequestBody GroupRegistration body);

  @GetMapping(value = "/group/{groupId}", produces = {"application/json"})
  public abstract ResponseEntity<?> getGroup(@PathVariable("groupId") UUID groupId);

  @PutMapping(value = "/group/{groupId}", produces = {"application/json"},
      consumes = {"application/json"})
  public abstract ResponseEntity<?> modifyGroup(@PathVariable("groupId") UUID groupId,
      @Valid @RequestBody GroupModification body);

  @PatchMapping(value = "/group/{groupId}", produces = {"application/json"},
      consumes = {"application/json"})
  public abstract ResponseEntity<?> enableDisableGroup(@PathVariable("groupId") UUID groupId,
      @Valid @RequestBody EnableDisableGroup body);

  @DeleteMapping(value = "/group/{groupId}", produces = {"application/json"})
  public abstract ResponseEntity<?> deleteGroup(@PathVariable("groupId") UUID groupId);

  @PatchMapping(value = "/group/{groupId}/device/{deviceId}", produces = {"application/json"})
  public abstract ResponseEntity<?> addDeviceToGroup(@PathVariable("groupId") UUID groupId,
      @PathVariable("deviceId") UUID deviceId);

  @DeleteMapping(value = "/group/{groupId}/device/{deviceId}", produces = {"application/json"})
  public abstract ResponseEntity<?> deleteDeviceFromGroup(@PathVariable("groupId") UUID groupId,
      @PathVariable("deviceId") UUID deviceId);

}
