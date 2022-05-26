package org.interfacerestproxy.core.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.interfacerestproxy.api.exception.ObjectMapperException;
import org.interfacerestproxy.api.mapper.ObjectWriter;

@RequiredArgsConstructor
public class JacksonObjectWriterImpl implements ObjectWriter {

  private final ObjectMapper objectMapper;

  public JacksonObjectWriterImpl() {
    this.objectMapper = JacksonMapper.INSTANCE.mapper;
  }

  @Override
  public byte[] write(Object obj) {
    try {
      return objectMapper.writeValueAsBytes(obj);
    } catch (JsonProcessingException ex) {
      throw new ObjectMapperException(ex);
    }
  }
}
