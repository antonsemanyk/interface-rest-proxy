package org.interfacerestproxy.api.exception;

public class ObjectMapperException extends RuntimeException {

  public ObjectMapperException(String message) {
    super(message);
  }

  public ObjectMapperException(Throwable cause) {
    super(cause);
  }
}
