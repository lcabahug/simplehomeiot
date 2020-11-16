package com.codingcuriosity.project.simplehomeiot.common.exception;

import com.codingcuriosity.project.simplehomeiot.common.AppLogger;
import com.codingcuriosity.project.simplehomeiot.common.LogItem;
import com.codingcuriosity.project.simplehomeiot.common.model.ErrorResult;
import com.codingcuriosity.project.simplehomeiot.common.model.ErrorType;
import com.codingcuriosity.project.simplehomeiot.logs.repository.ControllerLogRepository;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler implements LogItem {

  @Autowired
  ControllerLogRepository logRepository;

  // Custom Validation Handling
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> customValidationErrorHandling(
      MethodArgumentNotValidException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }

  // Handling for the Standard Errors thrown by the Controller
  @ExceptionHandler(InvalidControllerStateException.class)
  public ResponseEntity<?> standardExceptionHandling(InvalidControllerStateException exception) {
    ErrorResult errResult = AppLogger.error(this, ErrorType.ERROR, exception.getMessage(),
        ExceptionUtils.getStackTrace(exception));
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errResult);
  }

  // Global Exception Handling
  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> globalExceptionHandling(Exception exception) {
    ErrorResult errResult =
        AppLogger.critical(this, exception.toString(), ExceptionUtils.getStackTrace(exception));
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errResult);
  }

  @Override
  public ControllerLogRepository getLogRepo() {
    return this.logRepository;
  }
}
