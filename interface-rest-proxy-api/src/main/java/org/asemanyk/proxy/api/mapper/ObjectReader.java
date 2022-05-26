package org.asemanyk.proxy.api.mapper;

import java.lang.reflect.Type;

public interface ObjectReader {

  Object read(byte[] input, Type type);
}
