package com.codingcuriosity.project.simplehomeiot.zone.model;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.codingcuriosity.project.simplehomeiot.zones.model.ZoneInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ZoneInfoTest {

  @Test
  @DisplayName("TEST equals (setter vs builder)")
  void testEquals() {
    UUID zoneId1 = UUID.fromString("f9111111-1111-1111-1111-111111111111");
    UUID z1devId1 = UUID.fromString("e8111111-1111-1111-1111-111111111111");
    UUID z1devId2 = UUID.fromString("d7111111-1111-1111-1111-111111111111");
    List<UUID> zone1DevIds = new ArrayList<>();
    zone1DevIds.add(z1devId1);

    ZoneInfo zone1 = new ZoneInfo();
    zone1.setZoneId(zoneId1);
    zone1.setName("Name");
    zone1.setComment("Comment");
    zone1.setDeviceIds(zone1DevIds);
    zone1.addDeviceIdsItem(z1devId2);

    UUID zoneId2 = UUID.fromString("f9111111-1111-1111-1111-111111111111");
    UUID z2devId1 = UUID.fromString("e8111111-1111-1111-1111-111111111111");
    UUID z2devId2 = UUID.fromString("d7111111-1111-1111-1111-111111111111");
    List<UUID> zone2DevIds = new ArrayList<>();
    zone2DevIds.add(z2devId1);
    zone2DevIds.add(z2devId2);

    ZoneInfo zone2 = new ZoneInfo() //
        .zoneId(zoneId2) //
        .name("Name") //
        .comment("Comment") //
        .deviceIds(zone2DevIds);

    String zoneString = zone1.toString();

    // Assertions
    assertThat(zoneString, containsString("zoneId: " + zoneId1));
    assertThat(zoneString, containsString("name: Name"));
    assertThat(zoneString, containsString("comment: Comment"));
    assertThat(zoneString, containsString(zone1DevIds.toString()));
    assertTrue(zone1.equals(zone2), "Both objects must match.");
    assertTrue(zone1.equals(zone1), "Should match");
    assertFalse(zone1.equals(null), "Should not match");
    assertFalse(zone1.equals(new Object()), "Should not match");
  }
}
