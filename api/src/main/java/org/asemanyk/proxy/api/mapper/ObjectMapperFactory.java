package org.asemanyk.proxy.api.mapper;

import org.asemanyk.proxy.api.http.ContentType;

public interface ObjectMapperFactory {
  ObjectReader getReader(ContentType contentType);
  ObjectWriter getWriter(ContentType contentType);
  boolean supports(ContentType contentType);
}
