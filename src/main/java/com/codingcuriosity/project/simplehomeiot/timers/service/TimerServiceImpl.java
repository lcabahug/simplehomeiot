package com.codingcuriosity.project.simplehomeiot.timers.service;

import com.codingcuriosity.project.simplehomeiot.common.AppLogger;
import com.codingcuriosity.project.simplehomeiot.common.Utils;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonAddResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonGetListResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonGetResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonOperResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.ErrorResult;
import com.codingcuriosity.project.simplehomeiot.common.model.ErrorType;
import com.codingcuriosity.project.simplehomeiot.controls.model.DeviceControlInfo;
import com.codingcuriosity.project.simplehomeiot.controls.repository.ControlRepository;
import com.codingcuriosity.project.simplehomeiot.logs.repository.ControllerLogRepository;
import com.codingcuriosity.project.simplehomeiot.timers.cvt.TimerConverter;
import com.codingcuriosity.project.simplehomeiot.timers.model.CommonTimerAction;
import com.codingcuriosity.project.simplehomeiot.timers.model.CommonTimerIntervalPattern;
import com.codingcuriosity.project.simplehomeiot.timers.model.CommonTimerType;
import com.codingcuriosity.project.simplehomeiot.timers.model.TimerInfo;
import com.codingcuriosity.project.simplehomeiot.timers.model.TimerInfoTimerTarget;
import com.codingcuriosity.project.simplehomeiot.timers.model.TimerModification;
import com.codingcuriosity.project.simplehomeiot.timers.model.TimerRegistration;
import com.codingcuriosity.project.simplehomeiot.timers.model.TimerSetAction;
import com.codingcuriosity.project.simplehomeiot.timers.repository.TimerRepository;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class TimerServiceImpl implements TimerService {

  // Max iteration is per minute
  private final static int MAXTIMES_PER_DAY = 24 * 60;
  private final static int MAXTIMES_PER_WEEK = 6;
  private final static int MAXTIMES_PER_MONTH = 29;
  private final static int MAXTIMES_PER_YEAR = 364;

  private final static Map<CommonTimerIntervalPattern, Integer> INTERVAL_MAX_MAP = //
      Map.of(CommonTimerIntervalPattern.TIMES_PER_DAY, MAXTIMES_PER_DAY, //
          CommonTimerIntervalPattern.DAYS_PER_WEEK, MAXTIMES_PER_WEEK, //
          CommonTimerIntervalPattern.DAYS_PER_MONTH, MAXTIMES_PER_MONTH,
          CommonTimerIntervalPattern.DAYS_PER_YEAR, MAXTIMES_PER_YEAR);

  private final TimerRepository timerRepository;
  private final ControlRepository controlRepository;
  private final ControllerLogRepository logRepository;

  @Autowired
  public TimerServiceImpl(TimerRepository timerRepository, ControllerLogRepository logRepository,
      ControlRepository controlRepository) {
    this.timerRepository = timerRepository;
    this.controlRepository = controlRepository;
    this.logRepository = logRepository;
  }

  private CommonOperResponse checkRequestCombination(CommonTimerType timerType,
      CommonTimerIntervalPattern intervalPattern, OffsetDateTime startDateTime,
      OffsetDateTime endDateTime, Integer intervalPeriod, String errMessage) {
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;

    if (intervalPeriod == null) {
      if (!CommonTimerIntervalPattern.CUSTOM.equals(intervalPattern)) {
        response = HttpStatus.BAD_REQUEST;
        return new CommonOperResponse(response, errResult);
      }
    } else if (intervalPeriod < 1) {
      response = HttpStatus.BAD_REQUEST;
      return new CommonOperResponse(response, errResult);
    }

    if (timerType == null) {
      response = HttpStatus.BAD_REQUEST;
      return new CommonOperResponse(response, errResult);
    }

    switch (timerType) {
      case ONCE: {
        if (startDateTime == null || endDateTime == null) {
          response = HttpStatus.CONFLICT;
        } else if (!startDateTime.equals(endDateTime)) {
          response = HttpStatus.CONFLICT;
        } else if (intervalPeriod != 1) {
          response = HttpStatus.CONFLICT;
        } else if (intervalPattern != null) {
          response = HttpStatus.CONFLICT;
        }
        break;
      }
      case INTERVAL: {
        if (intervalPattern == null) {
          response = HttpStatus.BAD_REQUEST;
          return new CommonOperResponse(response, errResult);
        }
        switch (intervalPattern) {
          case TIMES_PER_DAY:
            // Fall through
          case DAYS_PER_WEEK:
            // Fall through
          case DAYS_PER_MONTH:
            // Fall through
          case DAYS_PER_YEAR: {
            if (intervalPeriod > INTERVAL_MAX_MAP.get(intervalPattern)) {
              response = HttpStatus.BAD_REQUEST;
              break;
            } else if (startDateTime == null || endDateTime == null) {
              response = HttpStatus.CONFLICT;
              break;
            }
            int timeDiff = endDateTime.compareTo(startDateTime);
            if (timeDiff < 0) {
              response = HttpStatus.BAD_REQUEST;
            } else if (timeDiff == 0) {
              response = HttpStatus.CONFLICT;
            }
            break;
          }
          case DAILY:
            // Fall through
          case WEEKLY:
            // Fall through
          case MONTHLY:
            // Fall through
          case YEARLY: {
            if (intervalPeriod > 1) {
              response = HttpStatus.CONFLICT;
              break;
            } else if (startDateTime == null || endDateTime == null) {
              response = HttpStatus.CONFLICT;
              break;
            }
            int timeDiff = endDateTime.compareTo(startDateTime);
            if (timeDiff < 0) {
              response = HttpStatus.BAD_REQUEST;
            } else if (timeDiff == 0) {
              response = HttpStatus.CONFLICT;
            }
            break;
          }
          case CUSTOM: {
            if (intervalPeriod != null) {
              response = HttpStatus.CONFLICT;
            } else if (startDateTime != null || endDateTime != null) {
              response = HttpStatus.CONFLICT;
            }
            break;
          }
          default: {
            response = HttpStatus.BAD_REQUEST;
            break;
          }
        }
        break;
      }
      default: {
        response = HttpStatus.BAD_REQUEST;
        break;
      }
    }

    return new CommonOperResponse(response, errResult);
  }

  private CommonOperResponse checkSetDateTimes(List<OffsetDateTime> setDateTimes,
      CommonTimerIntervalPattern intervalPattern, Integer intervalPeriod, String errMessage) {
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;

    boolean hasSetDateTime = false;
    if (setDateTimes != null && !setDateTimes.isEmpty()) {
      hasSetDateTime = true;
    }

    if (hasSetDateTime) {
      if (!CommonTimerIntervalPattern.CUSTOM.equals(intervalPattern)) {
        response = HttpStatus.CONFLICT;
        return new CommonOperResponse(response, errResult);
      }
      boolean containsNullSetDateTimes = setDateTimes.stream().anyMatch(Objects::isNull);
      if (containsNullSetDateTimes) {
        response = HttpStatus.BAD_REQUEST;
        return new CommonOperResponse(response, errResult);
      }
    } else {
      if (CommonTimerIntervalPattern.CUSTOM.equals(intervalPattern)) {
        response = HttpStatus.CONFLICT;
        return new CommonOperResponse(response, errResult);
      }
    }

    return new CommonOperResponse(response, errResult);
  }

  private CommonOperResponse checkTimerTargets(List<TimerInfoTimerTarget> timerTargets,
      String errMessage) {
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;

    if (timerTargets == null || timerTargets.isEmpty()) {
      response = HttpStatus.BAD_REQUEST;
      return new CommonOperResponse(response, errResult);
    }

    boolean containsNullTimerTarget = timerTargets.stream().anyMatch(Objects::isNull);
    if (containsNullTimerTarget) {
      response = HttpStatus.BAD_REQUEST;
      return new CommonOperResponse(response, errResult);
    }

    Set<UUID> controlIdToFindList = new LinkedHashSet<>();
    List<CommonTimerAction> actionList = new ArrayList<>();

    for (TimerInfoTimerTarget target : timerTargets) {
      CommonTimerAction action = target.getAction();
      String dirVal = target.getDirectValue();
      if (action == null) {
        response = HttpStatus.BAD_REQUEST;
        return new CommonOperResponse(response, errResult);
      } else if (CommonTimerAction.DIRECT.equals(action)) {
        if (dirVal == null || dirVal.isEmpty()) {
          response = HttpStatus.BAD_REQUEST;
          return new CommonOperResponse(response, errResult);
        }
      } else {
        if (dirVal != null && !dirVal.isEmpty()) {
          response = HttpStatus.BAD_REQUEST;
          return new CommonOperResponse(response, errResult);
        }
      }

      UUID controlId = target.getControlId();
      controlIdToFindList.add(controlId);
      actionList.add(action);
    }

    // Some of the Id's provided for control Id are duplicates
    if (actionList.size() != controlIdToFindList.size()) {
      response = HttpStatus.BAD_REQUEST;
      return new CommonOperResponse(response, errResult);
    }

    List<DeviceControlInfo> controlInfoList =
        this.controlRepository.findManyById(controlIdToFindList);
    if (controlInfoList == null || controlInfoList.isEmpty()) {
      response = HttpStatus.INTERNAL_SERVER_ERROR;
      String errMsg = errMessage;
      String errDetails = "DB did not return a valid response for the controlId multi-query.";
      errResult = AppLogger.error(this, ErrorType.ERROR, errMsg, errDetails);
      return new CommonOperResponse(response, errResult);
    }

    boolean hasNull = controlInfoList.stream().anyMatch(Objects::isNull);
    if (hasNull) {
      response = HttpStatus.INTERNAL_SERVER_ERROR;
      String errMsg = errMessage;
      String errDetails = "Some controlIds provided in the action parameter do not exist.";
      errResult = AppLogger.error(this, ErrorType.ERROR, errMsg, errDetails);
      return new CommonOperResponse(response, errResult);
    }

    return new CommonOperResponse(response, errResult);
  }

  @Override
  public ControllerLogRepository getLogRepo() {
    return this.logRepository;
  }

  @Override
  public CommonGetListResponse<TimerInfo> getTimers(Integer skip, Integer limit) {
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;

    Map<UUID, TimerInfo> timerMap = this.timerRepository.findAll();
    if (timerMap == null || timerMap.isEmpty()) {
      return new CommonGetListResponse<>(Collections.emptyList(), response, errResult);
    }

    // Extract Timer Collection from Map
    Collection<TimerInfo> timerList = timerMap.values();

    // Set 'skip' filter
    int offset = 0;
    if (skip != null) {
      if (skip < 0) {
        response = HttpStatus.BAD_REQUEST;
        return new CommonGetListResponse<>(Collections.emptyList(), response, errResult);
      } else if (skip >= timerList.size()) {
        return new CommonGetListResponse<>(Collections.emptyList(), response, errResult);
      }
      offset = skip;
    }

    // Set 'limit' filter
    int limitVal = timerList.size();
    if (limit != null) {
      if (limit < 0) {
        response = HttpStatus.BAD_REQUEST;
        return new CommonGetListResponse<>(Collections.emptyList(), response, errResult);
      }
      limitVal = limit;
    }

    // Sort List based on Start DateTime (Ascending) and apply filters
    List<TimerInfo> sortedList = timerList.stream() //
        .sorted(Comparator.comparing(TimerInfo::getStartDateTime)) //
        .skip(offset) //
        .limit(limitVal) //
        .collect(Collectors.toCollection(ArrayList::new));

    // Also Sort the setDateTimes (if present) in Ascending manner
    sortedList.stream() //
        .filter(timerInfo -> (timerInfo.getSetDateTimes() != null)
            && (timerInfo.getSetDateTimes().size() > 1)) //
        .forEach(timerInfo -> {
          List<OffsetDateTime> sortedDateTimes = timerInfo.getSetDateTimes();
          Collections.sort(sortedDateTimes);
          timerInfo.setDateTimes(sortedDateTimes);
        });

    return new CommonGetListResponse<>(sortedList, response, errResult);
  }

  @Override
  public CommonGetResponse<TimerInfo> getTimerById(UUID timerId) {
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;

    TimerInfo timerInfo = this.timerRepository.findById(timerId);
    if (timerInfo == null) {
      response = HttpStatus.NOT_FOUND;
    }

    return new CommonGetResponse<>(timerInfo, response, errResult);
  }

  @Override
  public CommonAddResponse registerTimer(TimerRegistration timerRegistration) {
    HttpStatus response = HttpStatus.CREATED;
    ErrorResult errResult = null;
    UUID timerId = null;

    // Check the combinations if valid
    CommonOperResponse checkResp = checkRequestCombination(timerRegistration.getTimerType(),
        timerRegistration.getIntervalPattern(), timerRegistration.getStartDateTime(),
        timerRegistration.getEndDateTime(), timerRegistration.getIntervalPeriod(),
        "Cannot add timer");
    if (!HttpStatus.OK.equals(checkResp.getResponse())) {
      return new CommonAddResponse(timerId, checkResp.getResponse(), checkResp.getErrorResult());
    }

    checkResp = checkTimerTargets(timerRegistration.getTimerTarget(), "Cannot add timer");
    if (!HttpStatus.OK.equals(checkResp.getResponse())) {
      return new CommonAddResponse(timerId, checkResp.getResponse(), checkResp.getErrorResult());
    }

    checkResp = checkSetDateTimes(timerRegistration.getSetDateTimes(),
        timerRegistration.getIntervalPattern(), timerRegistration.getIntervalPeriod(),
        "Cannot add timer");
    if (!HttpStatus.OK.equals(checkResp.getResponse())) {
      return new CommonAddResponse(timerId, checkResp.getResponse(), checkResp.getErrorResult());
    }

    // Generate Timer ID
    timerId = Utils.generateId(id -> {
      TimerInfo existingTimer = this.timerRepository.findById(id);
      return (existingTimer != null);
    });

    TimerInfo timerInfo = TimerConverter.createTimerInfo(timerId, timerRegistration);

    this.timerRepository.add(timerInfo);

    return new CommonAddResponse(timerId, response, errResult);
  }

  @Override
  public CommonOperResponse modifyTimer(UUID timerId, TimerModification timerModInfo) {
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;

    TimerInfo timerInfo = this.timerRepository.findById(timerId);
    if (timerInfo == null) {
      response = HttpStatus.NOT_FOUND;
      return new CommonOperResponse(response, errResult);
    }

    // Check the combinations if valid
    CommonOperResponse checkResp = checkRequestCombination(timerModInfo.getTimerType(),
        timerModInfo.getIntervalPattern(), timerModInfo.getStartDateTime(),
        timerModInfo.getEndDateTime(), timerModInfo.getIntervalPeriod(), "Cannot modify timer");
    response = checkResp.getResponse();
    if (!HttpStatus.OK.equals(response)) {
      return new CommonOperResponse(response, checkResp.getErrorResult());
    }

    checkResp = checkTimerTargets(timerModInfo.getTimerTarget(), "Cannot modify timer");
    response = checkResp.getResponse();
    if (!HttpStatus.OK.equals(response)) {
      return new CommonOperResponse(response, checkResp.getErrorResult());
    }

    checkResp = checkSetDateTimes(timerModInfo.getSetDateTimes(), timerModInfo.getIntervalPattern(),
        timerModInfo.getIntervalPeriod(), "Cannot modify timer");
    response = checkResp.getResponse();
    if (!HttpStatus.OK.equals(response)) {
      return new CommonOperResponse(response, checkResp.getErrorResult());
    }

    TimerInfo updatedTimer = TimerConverter.getModifiedTimerInfo(timerInfo, timerModInfo);

    this.timerRepository.update(timerId, updatedTimer);

    return new CommonOperResponse(response, errResult);
  }

  @Override
  public CommonOperResponse setActionToTimer(UUID timerId, TimerSetAction timerPatchInfo) {
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;

    TimerInfo timerInfo = this.timerRepository.findById(timerId);
    if (timerInfo == null) {
      response = HttpStatus.NOT_FOUND;
      return new CommonOperResponse(response, errResult);
    }

    // Check the combinations if valid
    CommonOperResponse checkResp =
        checkTimerTargets(timerPatchInfo.getTimerTarget(), "Cannot set action to timer");
    response = checkResp.getResponse();
    if (!HttpStatus.OK.equals(response)) {
      return new CommonOperResponse(response, checkResp.getErrorResult());
    }

    TimerInfo updatedTimer = TimerConverter.getPatchedTimerInfo(timerInfo, timerPatchInfo);

    this.timerRepository.update(timerId, updatedTimer);

    return new CommonOperResponse(response, errResult);
  }

  @Override
  public CommonOperResponse deleteTimer(UUID timerId) {
    HttpStatus response = HttpStatus.OK;
    ErrorResult errResult = null;

    TimerInfo timerInfo = this.timerRepository.findById(timerId);
    if (timerInfo == null) {
      response = HttpStatus.NOT_FOUND;
      return new CommonOperResponse(response, errResult);
    }

    this.timerRepository.delete(timerId);

    return new CommonOperResponse(response, errResult);
  }

}
