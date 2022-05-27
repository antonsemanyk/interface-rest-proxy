package org.interfacerestproxy.core.mapper;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.interfacerestproxy.api.exception.ObjectMapperException;
import org.interfacerestproxy.api.mapper.ObjectReader;

@RequiredArgsConstructor
public class JacksonObjectReaderImpl implements ObjectReader {

  private static final TypeFactory TYPE_FACTORY = TypeFactory.defaultInstance();
  private final ObjectMapper objectMapper;

  public JacksonObjectReaderImpl() {
    this.objectMapper = JacksonMapper.INSTANCE.mapper;
  }

  public Object read(byte[] input, Type type) {
    JavaType javaType = TYPE_FACTORY.constructType(type);
    try {
      if (javaType.getRawClass().equals(Optional.class)) {
        JavaType genericType = Optional.ofNullable(javaType.getBindings().getBoundType(0))
            .orElse(TYPE_FACTORY.constructType(Object.class));
        return Optional.ofNullable(objectMapper.readValue(input, genericType));
      }
      return objectMapper.readValue(input, javaType);
    } catch (IOException ex) {
      throw new ObjectMapperException(ex);
    }
  }
}
