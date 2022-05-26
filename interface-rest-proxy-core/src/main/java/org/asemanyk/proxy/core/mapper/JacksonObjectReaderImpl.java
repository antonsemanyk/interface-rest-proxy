package org.asemanyk.proxy.core.mapper;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.io.IOException;
import java.lang.reflect.Type;
import lombok.RequiredArgsConstructor;
import org.asemanyk.proxy.api.exception.ObjectMapperException;
import org.asemanyk.proxy.api.mapper.ObjectReader;

@RequiredArgsConstructor
public class JacksonObjectReaderImpl implements ObjectReader {

  private final ObjectMapper objectMapper;

  public JacksonObjectReaderImpl() {
    this.objectMapper = JacksonMapper.INSTANCE.mapper;
  }

  public Object read(byte[] input, Type type) {
    JavaType javaType = TypeFactory.defaultInstance().constructType(type);
    try {
      return objectMapper.readValue(input, javaType);
    } catch (IOException ex) {
      throw new ObjectMapperException(ex);
    }
  }
}
