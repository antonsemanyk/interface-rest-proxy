package org.interfacerestproxy.core.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.type.TypeFactory;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import lombok.Data;
import org.interfacerestproxy.api.mapper.ObjectReader;
import org.junit.jupiter.api.Test;

@SuppressWarnings({"unchecked", "rawtypes"})
class JacksonObjectReaderImplForOptionalTest {

  static final TypeFactory TYPE_FACTORY = TypeFactory.defaultInstance();

  ObjectReader objectReader = new JacksonObjectReaderImpl();

  @Test
  void verifyReadOptional() {
    Type type = TYPE_FACTORY.constructParametricType(Optional.class, String.class);

    Optional<String> result = (Optional<String>) objectReader.read("\"Hello World!\"".getBytes(StandardCharsets.UTF_8), type);

    assertThat(result).isPresent();
    assertThat(result.get()).isEqualTo("Hello World!");
  }

  @Test
  void verifyReadRawOptional() {
    Type type = TYPE_FACTORY.constructType(Optional.class);

    Optional result = (Optional) objectReader.read("\"Hello World!\"".getBytes(StandardCharsets.UTF_8), type);

    assertThat(result).isPresent();
    assertThat(result.get()).isEqualTo("Hello World!");
  }

  @Test
  void verifyReadEmptyOptional() {
    Type type = TYPE_FACTORY.constructParametricType(Optional.class, String.class);

    Optional<String> result = (Optional<String>) objectReader.read("null".getBytes(StandardCharsets.UTF_8), type);

    assertThat(result).isEmpty();
  }

  @Test
  void testReadOptionalWrapped() {
    OptionalWrapper result = (OptionalWrapper) objectReader.read("{\"field\":\"Hello World!\"}".getBytes(StandardCharsets.UTF_8), OptionalWrapper.class);

    assertThat(result).isNotNull();
    assertThat(result.field).isPresent();
    assertThat(result.field.get()).isEqualTo("Hello World!");
  }

  @Data
  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  static class OptionalWrapper {
    private Optional<String> field;
  }
}
