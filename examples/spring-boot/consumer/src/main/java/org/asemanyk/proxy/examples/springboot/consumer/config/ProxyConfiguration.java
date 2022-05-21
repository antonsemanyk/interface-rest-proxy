package org.asemanyk.proxy.examples.springboot.consumer.config;

import org.asemanyk.proxy.api.InterfaceRestProxyFactory;
import org.asemanyk.proxy.core.InterfaceRestProxyFactoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProxyConfiguration {

  @Bean
  public InterfaceRestProxyFactory interfaceRestProxyFactory() {
    return new InterfaceRestProxyFactoryImpl.Builder()
        .build();
  }
}
