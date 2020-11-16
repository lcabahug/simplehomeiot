package com.codingcuriosity.project.simplehomeiot.zones.cvt;

import com.codingcuriosity.project.simplehomeiot.zones.model.ZoneCreation;
import com.codingcuriosity.project.simplehomeiot.zones.model.ZoneInfo;
import com.codingcuriosity.project.simplehomeiot.zones.model.ZoneModification;
import com.codingcuriosity.project.simplehomeiot.zones.model.ZoneSetDevice;
import java.util.UUID;

public class ZoneConverter {

  public static ZoneInfo createZoneInfo(UUID zoneId, ZoneCreation zoneRegInfo) {
    ZoneInfo retObj = new ZoneInfo();
    retObj.setZoneId(zoneId);
    retObj.setName(zoneRegInfo.getName());
    retObj.setComment(zoneRegInfo.getComment());
    retObj.setDeviceIds(zoneRegInfo.getDeviceIds());
    return retObj;
  }

  public static ZoneInfo getModifiedZoneInfo(ZoneInfo zoneInfo, ZoneModification zoneModInfo) {
    ZoneInfo retObj = new ZoneInfo();
    retObj.setZoneId(zoneInfo.getZoneId());
    retObj.setName(zoneModInfo.getName());
    retObj.setComment(zoneModInfo.getComment());
    retObj.setDeviceIds(zoneModInfo.getDeviceIds());
    return retObj;
  }

  public static ZoneInfo getPatchedZoneInfo(ZoneInfo zoneInfo, ZoneSetDevice zonePatchInfo) {
    ZoneInfo retObj = new ZoneInfo();
    retObj.setZoneId(zoneInfo.getZoneId());
    retObj.setName(zoneInfo.getName());
    retObj.setComment(zoneInfo.getComment());
    retObj.setDeviceIds(zonePatchInfo.getDeviceIds());
    return retObj;
  }
}
