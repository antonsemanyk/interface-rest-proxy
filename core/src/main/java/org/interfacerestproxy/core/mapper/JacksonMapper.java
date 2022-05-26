package org.interfacerestproxy.core.mapper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public enum JacksonMapper {
  INSTANCE;

  JacksonMapper() {
    this.mapper = new ObjectMapper()
        .findAndRegisterModules()
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
  }

  final ObjectMapper mapper;
}
