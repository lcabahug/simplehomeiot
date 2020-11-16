package com.codingcuriosity.project.simplehomeiot.devices.model;

import com.codingcuriosity.project.simplehomeiot.common.model.ErrorResult;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;

public class CommonGetMapResponse<K, V> {
  private final Map<K, V> objMap;
  private final HttpStatus response;
  private final ErrorResult errResult;

  public CommonGetMapResponse(Map<K, V> objMap, HttpStatus response, ErrorResult errResult) {
    this.objMap = (objMap == null ? null : new HashMap<>(objMap));
    this.response = response;
    this.errResult = errResult;
  }

  public Map<K, V> getObjMap() {
    return objMap;
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
