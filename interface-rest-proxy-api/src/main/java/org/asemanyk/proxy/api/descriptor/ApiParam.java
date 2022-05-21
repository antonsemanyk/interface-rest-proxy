package org.asemanyk.proxy.api.descriptor;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ApiParam {

  int idx;
  String name;
  ApiParamType type;
}
