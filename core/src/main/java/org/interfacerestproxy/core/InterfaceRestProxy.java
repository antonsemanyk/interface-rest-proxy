package org.interfacerestproxy.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import lombok.extern.slf4j.Slf4j;
import org.interfacerestproxy.api.InterfaceAnnotationsProcessor;
import org.interfacerestproxy.api.descriptor.ApiDescriptor;
import org.interfacerestproxy.api.descriptor.ApiMethod;
import org.interfacerestproxy.api.exception.HttpException;
import org.interfacerestproxy.api.http.HttpClient;
import org.interfacerestproxy.api.http.HttpRequest;
import org.interfacerestproxy.api.http.HttpResponse;
import org.interfacerestproxy.core.internal.HttpRequestBuilder;

@Slf4j
public class InterfaceRestProxy<T> implements InvocationHandler {

  private final String baseUrl;
  private final ApiDescriptor apiDescriptor;
  private final HttpClient httpClient;
  private final HttpRequestBuilder httpRequestBuilder;

  public InterfaceRestProxy(Class<T> interfaceClass, String baseUrl, InterfaceAnnotationsProcessor annotationsProcessor,
      HttpRequestBuilder httpRequestBuilder,
      HttpClient httpClient) {
    this.baseUrl = baseUrl;
    this.httpClient = httpClient;
    this.apiDescriptor = annotationsProcessor.process(interfaceClass);
    this.httpRequestBuilder = httpRequestBuilder;
    log.info(this.apiDescriptor.toString());
  }

  @SuppressWarnings("unchecked")
  public static <T> T forInterface(Class<T> interfaceClass, String baseUrl,
      InterfaceAnnotationsProcessor annotationsProcessor,
      HttpRequestBuilder httpRequestBuilder,
      HttpClient httpClient) {
    InterfaceRestProxy<T> handler = new InterfaceRestProxy<>(interfaceClass, baseUrl, annotationsProcessor,
        httpRequestBuilder, httpClient);
    return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, handler);
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Exception {
    ApiMethod apiMethod = apiDescriptor.getMethods().get(method);
    log.debug(apiMethod.toString());

    HttpRequest httpRequest = httpRequestBuilder.buildHttpRequest(baseUrl, apiDescriptor, apiMethod, args);

    HttpResponse httpResponse = httpClient.execute(httpRequest, apiMethod.getReturnType());

    if (!httpResponse.isSuccessful()) {
      // TODO: log.error(httpResponse.getBody());
      throw new HttpException(httpResponse.getCode(), (String) httpResponse.getBody());
    }
    return httpResponse.getBody();
  }
}
