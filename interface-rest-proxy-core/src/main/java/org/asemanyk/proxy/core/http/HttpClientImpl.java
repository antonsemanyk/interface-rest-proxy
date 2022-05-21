package org.asemanyk.proxy.core.http;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.lang.reflect.Type;
import lombok.RequiredArgsConstructor;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.asemanyk.proxy.api.http.HttpClient;
import org.asemanyk.proxy.api.http.HttpMethod;
import org.asemanyk.proxy.api.http.HttpRequest;
import org.asemanyk.proxy.api.http.HttpResponse;

@RequiredArgsConstructor
public class HttpClientImpl implements HttpClient {

  private final ObjectMapper objectMapper;
  private final OkHttpClient client;

  @Override
  public HttpResponse execute(HttpRequest request, Type returnType) throws Exception {
    Request okRequest = buildOkRequest(request);
    Call okCall = client.newCall(okRequest);
    try (Response response = okCall.execute()) {
      if (!response.isSuccessful()) {
        return HttpResponse.builder()
            .code(response.code())
            .body(response.body() != null ? response.body().string() : null)
            .build();
      }
      JavaType javaType = TypeFactory.defaultInstance().constructType(returnType);
      if ("void".equals(javaType.getRawClass().getName()) || response.body() == null) {
        return HttpResponse.builder()
            .code(response.code())
            .build();
      }
      return HttpResponse.builder()
          .code(response.code())
          .body(objectMapper.readValue(response.body().byteStream(), javaType))
          .build();
    }
  }

  private Request buildOkRequest(HttpRequest request) throws Exception {
    if (HttpMethod.GET.equals(request.getMethod()) || HttpMethod.HEAD.equals(request.getMethod())) {
      return new Request.Builder()
          .method(request.getMethod().name(), null)
          .url(request.getUrl())
          .build();
    }
    byte[] body = objectMapper.writeValueAsBytes(request.getBody());
    return new Request.Builder()
        .method(request.getMethod().name(),
            RequestBody.create(body))
        .url(request.getUrl())
        .addHeader("Content-Type", "application/json")
        .build();
  }

  public static class Builder {

    private ObjectMapper objectMapper;
    private OkHttpClient okHttpClient;

    public Builder objectMapper(ObjectMapper objectMapper) {
      this.objectMapper = objectMapper;
      return this;
    }

    public Builder okHttpClient(OkHttpClient okHttpClient) {
      this.okHttpClient = okHttpClient;
      return this;
    }

    public HttpClient build() {
      if (this.objectMapper == null) {
        this.objectMapper = new ObjectMapper()
            .findAndRegisterModules()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
      }
      if (this.okHttpClient == null) {
        this.okHttpClient = new OkHttpClient.Builder()
            .build();
      }
      return new HttpClientImpl(objectMapper, okHttpClient);
    }
  }
}
