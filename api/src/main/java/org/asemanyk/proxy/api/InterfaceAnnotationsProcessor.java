package org.asemanyk.proxy.api;

import org.asemanyk.proxy.api.descriptor.ApiDescriptor;

public interface InterfaceAnnotationsProcessor {

  <T> ApiDescriptor process(Class<T> interfaceClass);
}
