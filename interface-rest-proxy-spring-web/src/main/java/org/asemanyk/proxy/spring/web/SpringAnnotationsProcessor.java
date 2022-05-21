package org.asemanyk.proxy.spring.web;

import static org.asemanyk.proxy.api.http.HttpMethod.DELETE;
import static org.asemanyk.proxy.api.http.HttpMethod.GET;
import static org.asemanyk.proxy.api.http.HttpMethod.PATCH;
import static org.asemanyk.proxy.api.http.HttpMethod.POST;
import static org.asemanyk.proxy.api.http.HttpMethod.PUT;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.asemanyk.proxy.api.InterfaceAnnotationsProcessor;
import org.asemanyk.proxy.api.descriptor.ApiDescriptor;
import org.asemanyk.proxy.api.descriptor.ApiMethod;
import org.asemanyk.proxy.api.descriptor.ApiParam;
import org.asemanyk.proxy.api.descriptor.ApiParamType;
import org.asemanyk.proxy.api.exception.InterfaceDescriptionException;
import org.asemanyk.proxy.api.http.HttpMethod;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

public class SpringAnnotationsProcessor implements InterfaceAnnotationsProcessor {

  @Override
  public <T> ApiDescriptor process(Class<T> interfaceClass) {
    RequestMapping requestMapping = interfaceClass.getDeclaredAnnotation(RequestMapping.class);
    if (requestMapping == null) {
      throw new InterfaceDescriptionException("Interface " + interfaceClass + " should be annotated with @RequestMapping");
    }
    String basePath = Arrays.stream(requestMapping.value())
        .findFirst()
        .orElse("");
    Map<Method, ApiMethod> apiMethods = Arrays.stream(interfaceClass.getDeclaredMethods())
        .collect(Collectors.toMap(Function.identity(), method -> buildApiMethod(method, basePath)));
    return ApiDescriptor.builder()
        .basePath(basePath)
        .consumes(requestMapping.consumes())
        .produces(requestMapping.produces())
        .methods(apiMethods)
        .build();
  }

  private ApiMethod buildApiMethod(Method method, String basePath) {
    HttpMethod httpMethod = null;
    String path = "";
    String[] consumes = new String[0];
    String[] produces = new String[0];
    GetMapping getMapping = method.getDeclaredAnnotation(GetMapping.class);
    if (getMapping != null) {
      httpMethod = GET;
      path = Arrays.stream(getMapping.value()).findFirst().orElse("");
      produces = getMapping.produces();
    }
    PostMapping postMapping = method.getDeclaredAnnotation(PostMapping.class);
    if (postMapping != null) {
      httpMethod = POST;
      path = Arrays.stream(postMapping.value()).findFirst().orElse("");
      consumes = postMapping.consumes();
      produces = postMapping.produces();
    }
    PutMapping putMapping = method.getDeclaredAnnotation(PutMapping.class);
    if (putMapping != null) {
      httpMethod = PUT;
      path = Arrays.stream(putMapping.value()).findFirst().orElse("");
      consumes = putMapping.consumes();
      produces = putMapping.produces();
    }
    PatchMapping patchMapping = method.getDeclaredAnnotation(PatchMapping.class);
    if (patchMapping != null) {
      httpMethod = PATCH;
      path = Arrays.stream(patchMapping.value()).findFirst().orElse("");
      consumes = patchMapping.consumes();
      produces = patchMapping.produces();
    }
    DeleteMapping deleteMapping = method.getDeclaredAnnotation(DeleteMapping.class);
    if (deleteMapping != null) {
      httpMethod = DELETE;
      path = Arrays.stream(deleteMapping.value()).findFirst().orElse("");
      consumes = deleteMapping.consumes();
      produces = deleteMapping.produces();
    }
    RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
    if (requestMapping != null) {
      httpMethod = Arrays.stream(requestMapping.method())
          .findFirst()
          .map(RequestMethod::name)
          .map(HttpMethod::valueOf)
          .orElseThrow(() -> new InterfaceDescriptionException("Cannot identify HTTP method for " + method));
      path = Arrays.stream(requestMapping.value()).findFirst().orElse("");
    }
    String url = ("/" + basePath + "/" + path)
        .replaceAll("(?<!(http:|https:))/{2,3}", "/");

    Type returnType = method.getGenericReturnType();

    List<ApiParam> params = new ArrayList<>();
    for (int idx = 0; idx < method.getParameters().length; idx++) {
      Parameter parameter = method.getParameters()[idx];
      ApiParam apiParam = buildApiParam(parameter, idx);
      if (apiParam.getType() != null) {
        params.add(apiParam);
      }
    }

    return ApiMethod.builder()
        .httpMethod(Optional.ofNullable(httpMethod)
            .orElseThrow(() -> new InterfaceDescriptionException("Cannot identify HTTP method for " + method)))
        .path(url)
        .consumes(consumes)
        .produces(produces)
        .returnType(returnType)
        .params(params)
        .build();
  }

  private ApiParam buildApiParam(Parameter parameter, int idx) {
    ApiParamType paramType = null;
    String name = null;
    PathVariable pathVariable = parameter.getDeclaredAnnotation(PathVariable.class);
    if (pathVariable != null) {
      name = pathVariable.value();
      paramType = ApiParamType.PATH;
    }
    RequestParam requestParam = parameter.getDeclaredAnnotation(RequestParam.class);
    if (requestParam != null) {
      name = requestParam.value();
      paramType = ApiParamType.QUERY;
    }
    RequestBody requestBody = parameter.getDeclaredAnnotation(RequestBody.class);
    if (requestBody != null) {
      paramType = ApiParamType.BODY;
    }
    parameter.getType();
    return ApiParam.builder()
        .idx(idx)
        .name(name)
        .type(paramType)
        .build();
  }
}
