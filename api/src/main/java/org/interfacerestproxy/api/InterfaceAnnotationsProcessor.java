package org.interfacerestproxy.api;

import org.interfacerestproxy.api.descriptor.ApiDescriptor;

public interface InterfaceAnnotationsProcessor {

  <T> ApiDescriptor process(Class<T> interfaceClass);
}
