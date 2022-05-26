package org.interfacerestproxy.api.mapper;

import org.interfacerestproxy.api.http.ContentType;

public interface ObjectMapperFactory {
  ObjectReader getReader(ContentType contentType);
  ObjectWriter getWriter(ContentType contentType);
  boolean supports(ContentType contentType);
}
