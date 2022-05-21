package org.asemanyk.proxy.api.http;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class HttpRequest {

  HttpMethod method;
  String url;
  Object body;
}
