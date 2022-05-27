package org.interfacerestproxy.core.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.type.TypeFactory;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Optional;
import lombok.Data;
import org.interfacerestproxy.api.mapper.ObjectReader;
import org.junit.jupiter.api.Test;

@SuppressWarnings({"unchecked"})
class JacksonObjectReaderImplForCollectionTest {

  static final TypeFactory TYPE_FACTORY = TypeFactory.defaultInstance();

  ObjectReader objectReader = new JacksonObjectReaderImpl();

  @Test
  void verifyReadCollection() {
    Type javaType = TYPE_FACTORY.constructCollectionType(Collection.class, String.class);

    Collection<String> result = (Collection<String>) objectReader.read("[\"Hello World!\"]".getBytes(StandardCharsets.UTF_8), javaType);

    Optional<String> val = result.stream().findFirst();
    assertThat(val).isPresent();
    assertThat(val.get()).isEqualTo("Hello World!");
  }
}
