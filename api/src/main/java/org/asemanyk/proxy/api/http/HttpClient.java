package org.asemanyk.proxy.api.http;

import java.lang.reflect.Type;

public interface HttpClient {

  HttpResponse execute(HttpRequest request, Type returnType) throws Exception;
}
