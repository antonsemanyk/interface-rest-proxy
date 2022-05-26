package org.interfacerestproxy.api.exception;

import lombok.Getter;

@Getter
public class HttpException extends RuntimeException {

  private final int code;
  private final String body;

  public HttpException(int code, String body) {
    this.code = code;
    this.body = body;
  }
}
