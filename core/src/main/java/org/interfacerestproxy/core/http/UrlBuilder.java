package org.interfacerestproxy.core.http;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import okhttp3.HttpUrl;

public class UrlBuilder {

  private final List<String> pathSegments = new LinkedList<>();
  private final Map<String, String> pathParameters = new LinkedHashMap<>();
  private final HttpUrl.Builder builder;


  public UrlBuilder(String baseUrl) {
    builder = HttpUrl.get(baseUrl).newBuilder();
  }

  public UrlBuilder addPathSegment(String pathSegment) {
    pathSegments.add(pathSegment);
    return this;
  }

  public UrlBuilder addPathParameter(String name, String value) {
    pathParameters.put(name, value);
    return this;
  }

  public UrlBuilder addQueryParameter(String name, String value) {
    builder.addEncodedQueryParameter(name, value);
    return this;
  }

  public String toString() {
    return build().uri().normalize().toString();
  }

  private HttpUrl build() {
    if (!pathSegments.isEmpty()) {
      String path = String.join("/", pathSegments);
      for (Entry<String, String> entry : pathParameters.entrySet()) {
        path = path.replace("{" + entry.getKey() + "}", entry.getValue());
      }
      builder.addEncodedPathSegments(path);
    }
    return builder.build();
  }
}
