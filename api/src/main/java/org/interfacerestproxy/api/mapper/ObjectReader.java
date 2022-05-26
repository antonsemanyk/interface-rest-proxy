package org.interfacerestproxy.api.mapper;

import java.lang.reflect.Type;

public interface ObjectReader {

  Object read(byte[] input, Type type);
}
