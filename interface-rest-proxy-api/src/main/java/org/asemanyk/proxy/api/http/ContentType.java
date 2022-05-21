package org.asemanyk.proxy.api.http;

import java.util.Arrays;
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
    return Arrays.stream(values())
        .filter(type -> type.value.equals(value))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Unknown/unsupported content-type: " + value));
  }
}
