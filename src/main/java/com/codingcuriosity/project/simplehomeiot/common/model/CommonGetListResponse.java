package com.codingcuriosity.project.simplehomeiot.common.model;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;

public class CommonGetListResponse<T> {
  private final List<T> objList;
  private final HttpStatus response;
  private final ErrorResult errResult;

  public CommonGetListResponse(List<T> obj, HttpStatus response, ErrorResult errResult) {
    this.objList = (obj == null ? null : new ArrayList<>(obj));
    this.response = response;
    this.errResult = errResult;
  }

  public List<T> getObjList() {
    return objList;
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
