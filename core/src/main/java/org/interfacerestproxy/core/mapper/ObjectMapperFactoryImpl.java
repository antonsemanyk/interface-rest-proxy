package org.interfacerestproxy.core.mapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.interfacerestproxy.api.exception.ObjectMapperException;
import org.interfacerestproxy.api.http.ContentType;
import org.interfacerestproxy.api.mapper.ObjectMapperFactory;
import org.interfacerestproxy.api.mapper.ObjectReader;
import org.interfacerestproxy.api.mapper.ObjectWriter;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ObjectMapperFactoryImpl implements ObjectMapperFactory {

  public static final ObjectMapperFactory DEFAULT = new ObjectMapperFactoryImpl(
      Map.of(ContentType.APPLICATION_JSON, new JacksonObjectReaderImpl()),
      Map.of(ContentType.APPLICATION_JSON, new JacksonObjectWriterImpl())
  );

  private final Map<ContentType, ObjectReader> readers;
  private final Map<ContentType, ObjectWriter> writers;

  @Override
  public ObjectReader getReader(ContentType contentType) {
    return Optional.ofNullable(readers.get(contentType))
        .orElseThrow(() -> new ObjectMapperException("No ObjectReaders found for contentType: " + contentType.getValue()));
  }

  @Override
  public ObjectWriter getWriter(ContentType contentType) {
    return Optional.ofNullable(writers.get(contentType))
        .orElseThrow(() -> new ObjectMapperException("No ObjectWriters found for contentType: " + contentType.getValue()));
  }

  @Override
  public boolean supports(ContentType contentType) {
    return readers.containsKey(contentType) && writers.containsKey(contentType);
  }

  public static class Builder {

    private final Map<ContentType, ObjectReader> readers = new HashMap<>();
    private final Map<ContentType, ObjectWriter> writers = new HashMap<>();

    public Builder add(ContentType contentType, ObjectReader reader, ObjectWriter writer) {
      if (contentType == null) {
        throw new IllegalArgumentException("ContentType cannot be null");
      }
      if (reader == null) {
        throw new IllegalArgumentException("Reader cannot be null");
      }
      if (writer == null) {
        throw new IllegalArgumentException("Writer cannot be null");
      }
      readers.put(contentType, reader);
      writers.put(contentType, writer);
      return this;
    }

    public ObjectMapperFactory build() {
      if (readers.isEmpty() || writers.isEmpty()) {
        throw new IllegalStateException("At least one Reader and one Writer are required for correct work");
      }
      return new ObjectMapperFactoryImpl(readers, writers);
    }

  }
}
