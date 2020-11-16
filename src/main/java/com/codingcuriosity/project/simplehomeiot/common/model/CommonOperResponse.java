package com.codingcuriosity.project.simplehomeiot.common.model;

import org.springframework.http.HttpStatus;

public class CommonOperResponse {
  private final HttpStatus response;
  private final ErrorResult errResult;

  public CommonOperResponse(HttpStatus response, ErrorResult result) {
    this.response = response;
    this.errResult = result;
  }

  public HttpStatus getResponse() {
    return this.response;
  }

  public ErrorResult getErrorResult() {
    return this.errResult;
  }

  public boolean isError() {
    return (!HttpStatus.OK.equals(this.response));
  }
}
