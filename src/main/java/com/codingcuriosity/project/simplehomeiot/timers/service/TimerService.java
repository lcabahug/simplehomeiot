package com.codingcuriosity.project.simplehomeiot.timers.service;

import com.codingcuriosity.project.simplehomeiot.common.LogItem;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonAddResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonGetListResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonGetResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonOperResponse;
import com.codingcuriosity.project.simplehomeiot.timers.model.TimerInfo;
import com.codingcuriosity.project.simplehomeiot.timers.model.TimerModification;
import com.codingcuriosity.project.simplehomeiot.timers.model.TimerRegistration;
import com.codingcuriosity.project.simplehomeiot.timers.model.TimerSetAction;
import java.util.UUID;

public interface TimerService extends LogItem {

  public abstract CommonGetListResponse<TimerInfo> getTimers(Integer skip, Integer limit);

  public abstract CommonGetResponse<TimerInfo> getTimerById(UUID timerId);

  public abstract CommonAddResponse registerTimer(TimerRegistration timerRegistration);

  public abstract CommonOperResponse modifyTimer(UUID timerId, TimerModification timerModInfo);

  public abstract CommonOperResponse setActionToTimer(UUID timerId, TimerSetAction timerPatchInfo);

  public abstract CommonOperResponse deleteTimer(UUID timerId);

}
