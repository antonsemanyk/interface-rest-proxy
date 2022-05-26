package org.interfacerestproxy.api.http;

import java.util.Arrays;
import java.util.Optional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ContentType {

  APPLICATION_JSON("application/json"),
  APPLICATION_XML("application/xml");

  private final String value;

  public String getValue() {
    return value;
  }

  public static ContentType fromValue(String value) {
    return Optional.ofNullable(fromValueNullable(value))
        .orElseThrow(() -> new IllegalArgumentException("Unknown/unsupported Content-Type: " + value));
  }

  public static ContentType fromValueNullable(String value) {
    return Arrays.stream(values())
        .filter(type -> type.value.equals(value))
        .findFirst()
        .orElse(null);
  }
}
