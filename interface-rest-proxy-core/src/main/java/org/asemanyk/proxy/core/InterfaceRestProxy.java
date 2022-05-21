package org.asemanyk.proxy.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import lombok.extern.slf4j.Slf4j;
import org.asemanyk.proxy.api.InterfaceAnnotationsProcessor;
import org.asemanyk.proxy.api.descriptor.ApiDescriptor;
import org.asemanyk.proxy.api.descriptor.ApiMethod;
import org.asemanyk.proxy.api.descriptor.ApiParam;
import org.asemanyk.proxy.api.descriptor.ApiParamType;
import org.asemanyk.proxy.api.http.HttpClient;
import org.asemanyk.proxy.api.http.HttpRequest;
import org.asemanyk.proxy.api.http.HttpResponse;
import org.asemanyk.proxy.core.http.UrlBuilder;

@Slf4j
public class InterfaceRestProxy<T> implements InvocationHandler {

  private final String baseUrl;
  private final ApiDescriptor apiDescriptor;
  private final HttpClient httpClient;

  public InterfaceRestProxy(Class<T> interfaceClass, String baseUrl, InterfaceAnnotationsProcessor annotationsProcessor,
      ObjectMapper objectMapper, HttpClient httpClient) {
    this.baseUrl = baseUrl;
    this.httpClient = httpClient;
    this.apiDescriptor = annotationsProcessor.process(interfaceClass);
    log.info(this.apiDescriptor.toString());
  }

  @SuppressWarnings("unchecked")
  public static <T> T forInterface(Class<T> interfaceClass, String baseUrl,
      InterfaceAnnotationsProcessor annotationsProcessor,
      ObjectMapper objectMapper, HttpClient httpClient) {
    InterfaceRestProxy<T> handler = new InterfaceRestProxy<>(interfaceClass, baseUrl, annotationsProcessor,
        objectMapper,
        httpClient);
    return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, handler);
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Exception {
    ApiMethod apiMethod = apiDescriptor.getMethods().get(method);
    log.debug(apiMethod.toString());
    Object body = apiMethod.getParams().stream()
        .filter(param -> ApiParamType.BODY.equals(param.getType()))
        .findFirst()
        .map(ApiParam::getIdx)
        .map(idx -> args[idx])
        .orElse(null);
    UrlBuilder urlBuilder = new UrlBuilder(baseUrl).addPathSegment(apiMethod.getPath());
    apiMethod.getParams().stream()
        .filter(param -> ApiParamType.PATH.equals(param.getType()))
        .forEach((param) -> urlBuilder.addPathParameter(param.getName(), args[param.getIdx()].toString()));
    apiMethod.getParams().stream()
        .filter(param -> ApiParamType.QUERY.equals(param.getType()))
        .forEach((param) -> urlBuilder.addQueryParameter(param.getName(), args[param.getIdx()].toString()));
    HttpRequest httpRequest = HttpRequest.builder()
        .method(apiMethod.getHttpMethod())
        .url(urlBuilder.toString())
        .body(body)
        .build();

    HttpResponse httpResponse = httpClient.execute(httpRequest, apiMethod.getReturnType());
    if (!httpResponse.isSuccessful()) {
      // TODO: log.error(httpResponse.getBody());
    }
    return httpResponse.getBody();
  }
}
