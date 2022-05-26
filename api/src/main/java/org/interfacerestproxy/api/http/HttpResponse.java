package org.interfacerestproxy.api.http;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class HttpResponse {

  int code;
  Object body;

  public boolean isSuccessful() {
    return code >= 200 && code < 300;
  }
}
