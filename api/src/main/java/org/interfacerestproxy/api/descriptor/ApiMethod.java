package org.interfacerestproxy.api.descriptor;

import java.lang.reflect.Type;
import java.util.List;
import lombok.Builder;
import lombok.Value;
import org.interfacerestproxy.api.http.HttpMethod;

@Value
@Builder
public class ApiMethod {

  HttpMethod httpMethod;
  String path;
  String[] headers;
  String[] consumes;
  String[] produces;
  Type returnType;
  List<ApiParam> params;
}
