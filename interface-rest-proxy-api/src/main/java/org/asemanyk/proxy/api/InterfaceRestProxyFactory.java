package org.asemanyk.proxy.api;

public interface InterfaceRestProxyFactory {

  <T> T proxyForInterface(Class<T> interfaceClass, String baseUrl);
}
