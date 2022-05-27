package org.interfacerestproxy.core.internal;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.interfacerestproxy.api.descriptor.ApiDescriptor;
import org.interfacerestproxy.api.descriptor.ApiMethod;
import org.interfacerestproxy.api.descriptor.ApiParam;
import org.interfacerestproxy.api.descriptor.ApiParamType;
import org.interfacerestproxy.api.http.ContentType;
import org.interfacerestproxy.api.http.HttpRequest;
import org.interfacerestproxy.core.http.UrlBuilder;

@NoArgsConstructor
public class HttpRequestBuilder {

  public HttpRequest buildHttpRequest(String baseUrl, ApiDescriptor apiDescriptor, ApiMethod apiMethod,
      Object[] args) {
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
    return HttpRequest.builder()
        .method(apiMethod.getHttpMethod())
        .url(urlBuilder.toString())
        .contentType(contentType)
        .body(body)
        .build();
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
