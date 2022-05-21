package org.asemanyk.proxy.spring.web.internal;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

public class SimpleTypes {

  private static final Set<Class<?>> SIMPLE_TYPES = Set.of(
      CharSequence.class,
      Optional.class,
      OptionalInt.class,
      OptionalLong.class,
      OptionalDouble.class,
      AtomicLong.class,
      AtomicInteger.class,
      Map.class,
      Iterable.class,
      Duration.class,
      LocalTime.class
  );
  private static final List<Predicate<Class<?>>> SIMPLE_TYPE_PREDICATES = List.of(
      Class::isPrimitive,
      Class::isEnum,
      Class::isArray
  );


  static boolean isSimpleType(Class<?> clazz) {
    return SIMPLE_TYPE_PREDICATES.stream().anyMatch(p -> p.test(clazz)) ||
        SIMPLE_TYPES.stream().anyMatch(c -> c.isAssignableFrom(clazz));
  }

}
