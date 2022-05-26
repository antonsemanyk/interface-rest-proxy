package org.interfacerestproxy.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.interfacerestproxy.api.InterfaceAnnotationsProcessor;
import org.interfacerestproxy.api.descriptor.ApiDescriptor;
import org.interfacerestproxy.api.descriptor.ApiMethod;
import org.interfacerestproxy.api.descriptor.ApiParam;
import org.interfacerestproxy.api.descriptor.ApiParamType;
import org.interfacerestproxy.api.http.ContentType;
import org.interfacerestproxy.api.http.HttpClient;
import org.interfacerestproxy.api.http.HttpRequest;
import org.interfacerestproxy.api.http.HttpResponse;
import org.interfacerestproxy.core.http.UrlBuilder;

@Slf4j
public class InterfaceRestProxy<T> implements InvocationHandler {

  private final String baseUrl;
  private final ApiDescriptor apiDescriptor;
  private final HttpClient httpClient;

  public InterfaceRestProxy(Class<T> interfaceClass, String baseUrl, InterfaceAnnotationsProcessor annotationsProcessor,
      HttpClient httpClient) {
    this.baseUrl = baseUrl;
    this.httpClient = httpClient;
    this.apiDescriptor = annotationsProcessor.process(interfaceClass);
    log.info(this.apiDescriptor.toString());
  }

  @SuppressWarnings("unchecked")
  public static <T> T forInterface(Class<T> interfaceClass, String baseUrl,
      InterfaceAnnotationsProcessor annotationsProcessor,
      HttpClient httpClient) {
    InterfaceRestProxy<T> handler = new InterfaceRestProxy<>(interfaceClass, baseUrl, annotationsProcessor,
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
    ContentType contentType = getContentType(apiDescriptor, apiMethod);
    HttpRequest httpRequest = HttpRequest.builder()
        .method(apiMethod.getHttpMethod())
        .url(urlBuilder.toString())
        .contentType(contentType)
        .body(body)
        .build();

    HttpResponse httpResponse = httpClient.execute(httpRequest, apiMethod.getReturnType());
    if (!httpResponse.isSuccessful()) {
      // TODO: log.error(httpResponse.getBody());
    }
    return httpResponse.getBody();
  }

  private ContentType getContentType(ApiDescriptor apiDescriptor, ApiMethod apiMethod) {
    List<String> contentTypes = List.of();
    if (ArrayUtils.isNotEmpty(apiMethod.getConsumes())) {
      contentTypes = Arrays.asList(apiMethod.getConsumes());
    } else if (ArrayUtils.isNotEmpty(apiDescriptor.getConsumes())) {
      contentTypes = Arrays.asList(apiDescriptor.getConsumes());
    }
    return contentTypes.stream()
        .map(ContentType::fromValueNullable)
        .filter(Objects::nonNull)
        .sorted()
        .findFirst()
        .orElse(ContentType.APPLICATION_JSON);
  }
}
