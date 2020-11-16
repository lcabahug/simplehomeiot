package com.codingcuriosity.project.simplehomeiot.common.model;

import java.util.UUID;
import org.springframework.http.HttpStatus;

public class CommonAddResponse {
  private final UUID id;
  private final HttpStatus response;
  private final ErrorResult errResult;

  public CommonAddResponse(UUID id, HttpStatus response, ErrorResult result) {
    this.id = id;
    this.response = response;
    this.errResult = result;
  }

  public UUID getId() {
    return this.id;
  }

  public HttpStatus getResponse() {
    return this.response;
  }

  public ErrorResult getErrorResult() {
    return this.errResult;
  }
}
