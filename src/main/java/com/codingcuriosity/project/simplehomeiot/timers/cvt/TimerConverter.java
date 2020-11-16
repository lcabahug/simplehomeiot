package com.codingcuriosity.project.simplehomeiot.timers.cvt;

import com.codingcuriosity.project.simplehomeiot.timers.model.TimerInfo;
import com.codingcuriosity.project.simplehomeiot.timers.model.TimerModification;
import com.codingcuriosity.project.simplehomeiot.timers.model.TimerRegistration;
import com.codingcuriosity.project.simplehomeiot.timers.model.TimerSetAction;
import java.util.UUID;

public class TimerConverter {

  public static TimerInfo createTimerInfo(UUID timerId, TimerRegistration timerRegInfo) {
    TimerInfo retObj = new TimerInfo();
    retObj.setTimerId(timerId);
    retObj.setTimerType(timerRegInfo.getTimerType());
    retObj.setIntervalPattern(timerRegInfo.getIntervalPattern());
    retObj.setStartDateTime(timerRegInfo.getStartDateTime());
    retObj.setEndDateTime(timerRegInfo.getEndDateTime());
    retObj.setIntervalPeriod(timerRegInfo.getIntervalPeriod());
    retObj.setSetDateTimes(timerRegInfo.getSetDateTimes());
    retObj.setTimerTarget(timerRegInfo.getTimerTarget());
    return retObj;
  }

  public static TimerInfo getModifiedTimerInfo(TimerInfo timerInfo,
      TimerModification timerModInfo) {
    TimerInfo retObj = new TimerInfo();
    retObj.setTimerId(timerInfo.getTimerId());
    retObj.setTimerType(timerModInfo.getTimerType());
    retObj.setIntervalPattern(timerModInfo.getIntervalPattern());
    retObj.setStartDateTime(timerModInfo.getStartDateTime());
    retObj.setEndDateTime(timerModInfo.getEndDateTime());
    retObj.setIntervalPeriod(timerModInfo.getIntervalPeriod());
    retObj.setSetDateTimes(timerModInfo.getSetDateTimes());
    retObj.setTimerTarget(timerModInfo.getTimerTarget());
    return retObj;
  }

  public static TimerInfo getPatchedTimerInfo(TimerInfo timerInfo, TimerSetAction timerPatchInfo) {
    TimerInfo retObj = new TimerInfo();
    retObj.setTimerId(timerInfo.getTimerId());
    retObj.setTimerType(timerInfo.getTimerType());
    retObj.setIntervalPattern(timerInfo.getIntervalPattern());
    retObj.setStartDateTime(timerInfo.getStartDateTime());
    retObj.setEndDateTime(timerInfo.getEndDateTime());
    retObj.setIntervalPeriod(timerInfo.getIntervalPeriod());
    retObj.setSetDateTimes(timerInfo.getSetDateTimes());
    retObj.setTimerTarget(timerPatchInfo.getTimerTarget());
    return retObj;
  }
}
