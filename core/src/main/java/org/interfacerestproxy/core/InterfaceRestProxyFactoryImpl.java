package org.interfacerestproxy.core;

import java.util.ServiceLoader;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.interfacerestproxy.api.InterfaceAnnotationsProcessor;
import org.interfacerestproxy.api.InterfaceRestProxyFactory;
import org.interfacerestproxy.api.http.HttpClient;
import org.interfacerestproxy.api.mapper.ObjectMapperFactory;
import org.interfacerestproxy.core.http.OkHttpClientImpl;
import org.interfacerestproxy.core.mapper.ObjectMapperFactoryImpl;

/**
 * @author Anton Semanyk
 */
@RequiredArgsConstructor
public class InterfaceRestProxyFactoryImpl implements InterfaceRestProxyFactory {

  private final InterfaceAnnotationsProcessor annotationsProcessor;
  private final HttpClient httpClient;

  @Override
  public <T> T proxyForInterface(Class<T> interfaceClass, String baseUrl) {
    return InterfaceRestProxy.forInterface(interfaceClass, baseUrl, annotationsProcessor, httpClient);
  }

  @NoArgsConstructor
  public static class Builder {

    private InterfaceAnnotationsProcessor annotationsProcessor;
    private ObjectMapperFactory objectMapperFactory;
    private HttpClient httpClient;

    public Builder annotationProcessor(InterfaceAnnotationsProcessor annotationsProcessor) {
      this.annotationsProcessor = annotationsProcessor;
      return this;
    }

    public Builder objectMapperFactory(ObjectMapperFactory objectMapperFactory) {
      this.objectMapperFactory = objectMapperFactory;
      return this;
    }

    public Builder httpClient(HttpClient httpClient) {
      this.httpClient = httpClient;
      return this;
    }

    public InterfaceRestProxyFactory build() {
      if (this.annotationsProcessor == null) {
        this.annotationsProcessor = ServiceLoader.load(InterfaceAnnotationsProcessor.class)
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("No InterfaceAnnotationsProcessor implementations found"));
      }
      if (this.objectMapperFactory == null) {
        this.objectMapperFactory = ObjectMapperFactoryImpl.DEFAULT;
      }
      if (this.httpClient == null) {
        this.httpClient = new OkHttpClientImpl.Builder()
            .objectMapperFactory(objectMapperFactory)
            .build();
      }
      return new InterfaceRestProxyFactoryImpl(annotationsProcessor, httpClient);
    }
  }
}
