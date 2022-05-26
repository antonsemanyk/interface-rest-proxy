package org.interfacerestproxy.core.http;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.io.IOException;
import java.lang.reflect.Type;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.interfacerestproxy.api.exception.HttpClientException;
import org.interfacerestproxy.api.http.ContentType;
import org.interfacerestproxy.api.http.HttpClient;
import org.interfacerestproxy.api.http.HttpMethod;
import org.interfacerestproxy.api.http.HttpRequest;
import org.interfacerestproxy.api.http.HttpResponse;
import org.interfacerestproxy.api.mapper.ObjectMapperFactory;
import org.interfacerestproxy.api.mapper.ObjectReader;
import org.interfacerestproxy.api.mapper.ObjectWriter;
import org.interfacerestproxy.core.mapper.ObjectMapperFactoryImpl;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OkHttpClientImpl implements HttpClient {

  private final ObjectMapperFactory objectMapperFactory;
  private final OkHttpClient client;

  @Override
  public HttpResponse execute(HttpRequest request, Type returnType) {
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
      ObjectReader objectReader = objectMapperFactory.getReader(ContentType.fromValue(response.header("Content-Type")));
      return HttpResponse.builder()
          .code(response.code())
          .body(objectReader.read(response.body().bytes(), javaType))
          .build();
    } catch (IOException ex) {
      throw new HttpClientException(ex);
    }
  }

  private Request buildOkRequest(HttpRequest request) {
    if (HttpMethod.GET.equals(request.getMethod()) || HttpMethod.HEAD.equals(request.getMethod())) {
      return new Request.Builder()
          .method(request.getMethod().name(), null)
          .url(request.getUrl())
          .build();
    }
    ObjectWriter objectWriter = objectMapperFactory.getWriter(request.getContentType());
    byte[] body = objectWriter.write(request.getBody());
    return new Request.Builder()
        .method(request.getMethod().name(),
            RequestBody.create(body))
        .url(request.getUrl())
        .addHeader("Content-Type", request.getContentType().getValue())
        .build();
  }

  public static class Builder {

    private ObjectMapperFactory objectMapperFactory;
    private OkHttpClient okHttpClient;

    public Builder objectMapperFactory(ObjectMapperFactory objectMapperFactory) {
      this.objectMapperFactory = objectMapperFactory;
      return this;
    }

    public Builder okHttpClient(OkHttpClient okHttpClient) {
      this.okHttpClient = okHttpClient;
      return this;
    }

    public HttpClient build() {
      if (this.objectMapperFactory == null) {
        this.objectMapperFactory = ObjectMapperFactoryImpl.DEFAULT;
      }
      if (this.okHttpClient == null) {
        this.okHttpClient = new OkHttpClient.Builder()
            .build();
      }
      return new OkHttpClientImpl(objectMapperFactory, okHttpClient);
    }
  }
}
