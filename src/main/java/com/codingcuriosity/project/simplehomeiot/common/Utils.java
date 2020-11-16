package com.codingcuriosity.project.simplehomeiot.common;

import java.util.UUID;
import java.util.function.Predicate;
import javax.servlet.http.HttpServletRequest;

public class Utils {

  public static UUID generateId(Predicate<UUID> isIdExist) {
    UUID id;
    do {
      id = UUID.randomUUID();
      // Repeatedly generates a new Id as long as the condition remains true
    } while (isIdExist.test(id));
    return id;
  }

  public static boolean isClientNoSupportJsonResponse(HttpServletRequest request) {
    String accept = request.getHeader("Accept");
    if (accept == null || (!accept.contains("application/json") && !accept.equals("*/*"))) {
      // Client is unable to accept json response format
      return true;
    } else {
      return false;
    }
  }
}
