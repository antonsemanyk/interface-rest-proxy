package org.asemanyk.proxy.core.mapper;

import java.lang.reflect.Type;
import org.asemanyk.proxy.api.http.ContentType;
import org.asemanyk.proxy.api.mapper.ObjectReader;

public class ObjectReaderImpl implements ObjectReader {
  public Object read(byte[] input, ContentType contentType, Type type) {
    return input;
  }
}
