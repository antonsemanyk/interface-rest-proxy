package org.asemanyk.proxy.core;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ServiceLoader;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.asemanyk.proxy.api.InterfaceAnnotationsProcessor;
import org.asemanyk.proxy.api.InterfaceRestProxyFactory;
import org.asemanyk.proxy.api.http.HttpClient;
import org.asemanyk.proxy.core.http.HttpClientImpl;

/**
 * @author Anton Semanyk
 */
@RequiredArgsConstructor
public class InterfaceRestProxyFactoryImpl implements InterfaceRestProxyFactory {

  private final InterfaceAnnotationsProcessor annotationsProcessor;
  private final ObjectMapper objectMapper;
  private final HttpClient httpClient;

  @Override
  public <T> T proxyForInterface(Class<T> interfaceClass, String baseUrl) {
    return InterfaceRestProxy.forInterface(interfaceClass, baseUrl, annotationsProcessor, objectMapper, httpClient);
  }

  @NoArgsConstructor
  public static class Builder {

    private InterfaceAnnotationsProcessor annotationsProcessor;
    private ObjectMapper objectMapper;
    private HttpClient httpClient;

    public Builder annotationProcessor(InterfaceAnnotationsProcessor annotationsProcessor) {
      this.annotationsProcessor = annotationsProcessor;
      return this;
    }

    public Builder objectMapper(ObjectMapper objectMapper) {
      this.objectMapper = objectMapper;
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
      if (this.objectMapper == null) {
        this.objectMapper = new ObjectMapper()
            .findAndRegisterModules()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
      }
      if (this.httpClient == null) {
        this.httpClient = new HttpClientImpl.Builder()
            .objectMapper(objectMapper)
            .build();
      }
      return new InterfaceRestProxyFactoryImpl(annotationsProcessor, objectMapper, httpClient);
    }
  }
}
