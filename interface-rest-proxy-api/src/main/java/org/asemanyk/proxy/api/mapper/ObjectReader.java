package org.asemanyk.proxy.api.mapper;

import java.lang.reflect.Type;
import org.asemanyk.proxy.api.http.ContentType;

public interface ObjectReader {

  Object read(byte[] input, ContentType contentType, Type type);
}
