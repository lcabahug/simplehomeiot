package com.codingcuriosity.project.simplehomeiot.common;

import com.codingcuriosity.project.simplehomeiot.common.model.ErrorResult;
import com.codingcuriosity.project.simplehomeiot.common.model.ErrorType;
import com.codingcuriosity.project.simplehomeiot.logs.model.CommonLogLevel;
import com.codingcuriosity.project.simplehomeiot.logs.model.ControllerLogInfo;
import com.codingcuriosity.project.simplehomeiot.logs.repository.ControllerLogRepository;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AppLogger {

  private static void logMessage(ControllerLogRepository controllerLogRepository, UUID logId,
      OffsetDateTime timestamp, CommonLogLevel level, Object caller, String message,
      String details) {

    // Create formatted return object
    ControllerLogInfo logInfo = new ControllerLogInfo();
    logInfo.setLogId(logId);
    logInfo.setLevel(level);
    logInfo.setTimestamp(timestamp);
    logInfo.setTitle(message);
    logInfo.setMessage(details);
    controllerLogRepository.add(logInfo);
  }

  private static void logNewMessage(ControllerLogRepository controllerLogRepository,
      CommonLogLevel level, Object caller, String message, String details) {

    // Generate Timestamp
    OffsetDateTime timestamp = OffsetDateTime.now();

    // Generate UUID
    UUID logId = Utils.generateId(id -> {
      ControllerLogInfo logItem = controllerLogRepository.findById(id);
      return (logItem != null);
    });

    logMessage(controllerLogRepository, logId, timestamp, level, caller, message, details);
  }

  private static ErrorResult logError(ControllerLogRepository controllerLogRepository,
      CommonLogLevel level, Object caller, ErrorType type, String message, String details) {
    // Generate Timestamp
    OffsetDateTime timestamp = OffsetDateTime.now();

    // Generate UUID
    UUID logId = Utils.generateId(id -> {
      ControllerLogInfo logItem = controllerLogRepository.findById(id);
      return (logItem != null);
    });

    // Log to DB
    logMessage(controllerLogRepository, logId, timestamp, level, caller, message, details);

    // Create formatted return object
    ErrorResult result = new ErrorResult();
    result.setLogId(logId);
    result.setTimestamp(timestamp);
    result.setType(type);
    result.setMessage(message);
    result.setDetails(details);
    return result;
  }

  public static ErrorResult critical(LogItem caller, String message, String details) {
    // Log on the controller's stdout
    Logger logger = LogManager.getLogger(caller);
    logger.fatal(message);

    // Log on DB as controller log
    return logError(caller.getLogRepo(), CommonLogLevel.CRITICAL, caller, ErrorType.EXCEPTION,
        message, details);
  }

  public static ErrorResult error(LogItem caller, ErrorType type, String message, String details) {
    // Log on the controller's stdout
    Logger logger = LogManager.getLogger(caller);
    logger.error(message);

    // Log on DB as controller log
    return logError(caller.getLogRepo(), CommonLogLevel.ERROR, caller, type, message, details);
  }

  public static void warn(LogItem caller, String message, String details) {
    // Log on the controller's stdout
    Logger logger = LogManager.getLogger(caller);
    logger.warn(message);

    // Log on DB as controller log
    logNewMessage(caller.getLogRepo(), CommonLogLevel.WARNING, caller, message, details);
  }

  public static void info(LogItem caller, String message, String details) {
    // Log on the controller's stdout
    Logger logger = LogManager.getLogger(caller);
    logger.info(message);

    // Log on DB as controller log
    logNewMessage(caller.getLogRepo(), CommonLogLevel.INFO, caller, message, details);
  }

  public static void debug(LogItem caller, String message, String details) {
    // Log on the controller's stdout
    Logger logger = LogManager.getLogger(caller);
    logger.debug(message);

    // Log on DB as controller log
    logNewMessage(caller.getLogRepo(), CommonLogLevel.DEBUG, caller, message, details);
  }

  public static void trace(LogItem caller, String message, String details) {
    // Log on the controller's stdout
    Logger logger = LogManager.getLogger(caller);
    logger.trace(message);

    // Log on DB as controller log
    logNewMessage(caller.getLogRepo(), CommonLogLevel.TRACE, caller, message, details);
  }
}
