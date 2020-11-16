package com.codingcuriosity.project.simplehomeiot.zones.service;

import com.codingcuriosity.project.simplehomeiot.common.LogItem;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonAddResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonGetListResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonGetResponse;
import com.codingcuriosity.project.simplehomeiot.common.model.CommonOperResponse;
import com.codingcuriosity.project.simplehomeiot.zones.model.ZoneCreation;
import com.codingcuriosity.project.simplehomeiot.zones.model.ZoneInfo;
import com.codingcuriosity.project.simplehomeiot.zones.model.ZoneModification;
import com.codingcuriosity.project.simplehomeiot.zones.model.ZoneSetDevice;
import java.util.UUID;

public interface ZoneService extends LogItem {

  public abstract CommonGetListResponse<ZoneInfo> getZones(Integer skip, Integer limit);

  public abstract CommonGetResponse<ZoneInfo> getZoneById(UUID zoneId);

  public abstract CommonAddResponse registerZone(ZoneCreation zoneCreation);

  public abstract CommonOperResponse modifyZone(UUID zoneId, ZoneModification zoneModInfo);

  public abstract CommonOperResponse setDevicesToZone(UUID zoneId, ZoneSetDevice zonePatchInfo);

  public abstract CommonOperResponse deleteZone(UUID zoneId);

}
