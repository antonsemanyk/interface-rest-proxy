package org.interfacerestproxy.api.descriptor;

import java.lang.reflect.Method;
import java.util.Map;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ApiDescriptor {

  String basePath;
  String[] params;
  String[] headers;
  String[] consumes;
  String[] produces;
  Map<Method, ApiMethod> methods;
}
