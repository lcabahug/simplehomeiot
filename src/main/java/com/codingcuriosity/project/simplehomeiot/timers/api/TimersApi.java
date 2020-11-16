package com.codingcuriosity.project.simplehomeiot.timers.api;

import com.codingcuriosity.project.simplehomeiot.timers.model.TimerModification;
import com.codingcuriosity.project.simplehomeiot.timers.model.TimerRegistration;
import com.codingcuriosity.project.simplehomeiot.timers.model.TimerSetAction;
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

public interface TimersApi {

  @GetMapping(value = "/timers", produces = {"application/json"})
  public abstract ResponseEntity<?> getTimers(
      @Valid @RequestParam(value = "skip", required = false) Integer skip,
      @Valid @RequestParam(value = "limit", required = false) Integer limit);

  @PostMapping(value = "/timers", produces = {"application/json"}, consumes = {"application/json"})
  public abstract ResponseEntity<?> registerTimer(@Valid @RequestBody TimerRegistration body);

  @GetMapping(value = "/timer/{timerId}", produces = {"application/json"})
  public abstract ResponseEntity<?> getTimer(@PathVariable("timerId") UUID timerId);

  @PutMapping(value = "/timer/{timerId}", produces = {"application/json"},
      consumes = {"application/json"})
  public abstract ResponseEntity<?> modifyTimer(@PathVariable("timerId") UUID timerId,
      @Valid @RequestBody TimerModification body);

  @PatchMapping(value = "/timer/{timerId}", produces = {"application/json"},
      consumes = {"application/json"})
  public abstract ResponseEntity<?> setActionToTimer(@PathVariable("timerId") UUID timerId,
      @Valid @RequestBody TimerSetAction body);

  @DeleteMapping(value = "/timer/{timerId}", produces = {"application/json"})
  public abstract ResponseEntity<?> deleteTimer(@PathVariable("timerId") UUID timerId);

}
