package org.asemanyk.proxy.core;

import java.util.ServiceLoader;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.asemanyk.proxy.api.InterfaceAnnotationsProcessor;
import org.asemanyk.proxy.api.InterfaceRestProxyFactory;
import org.asemanyk.proxy.api.http.HttpClient;
import org.asemanyk.proxy.api.mapper.ObjectMapperFactory;
import org.asemanyk.proxy.core.http.OkHttpClientImpl;
import org.asemanyk.proxy.core.mapper.ObjectMapperFactoryImpl;

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
