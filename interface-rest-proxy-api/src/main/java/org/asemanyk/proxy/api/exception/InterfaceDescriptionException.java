package org.asemanyk.proxy.api.exception;

public class InterfaceDescriptionException extends RuntimeException {

  public InterfaceDescriptionException(String message) {
    super(message);
  }

  public InterfaceDescriptionException(String message, Throwable cause) {
    super(message, cause);
  }

  public InterfaceDescriptionException(Throwable cause) {
    super(cause);
  }
}
