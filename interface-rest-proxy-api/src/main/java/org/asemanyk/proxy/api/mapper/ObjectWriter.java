package org.asemanyk.proxy.api.mapper;

import org.asemanyk.proxy.api.http.ContentType;

public interface ObjectWriter {

  String write(Object obj, ContentType contentType);

}
