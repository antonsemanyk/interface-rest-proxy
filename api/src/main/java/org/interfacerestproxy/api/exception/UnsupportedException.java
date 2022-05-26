package org.interfacerestproxy.api.exception;

public class UnsupportedException extends IllegalStateException {

  public UnsupportedException(String s) {
    super(s);
  }

  public UnsupportedException(String message, Throwable cause) {
    super(message, cause);
  }
}
