package com.codingcuriosity.project.simplehomeiot.common.model;

import javax.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;

public class CommonGetResponse<T> {
  private final T resultObj;
  private final HttpStatus response;
  private final ErrorResult errResult;

  public CommonGetResponse(T resultObj, @NotNull HttpStatus response, ErrorResult errResult) {
    this.resultObj = resultObj;
    this.response = response;
    this.errResult = errResult;
  }

  public T getObj() {
    return this.resultObj;
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
