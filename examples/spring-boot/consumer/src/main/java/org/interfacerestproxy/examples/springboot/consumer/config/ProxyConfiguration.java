package org.interfacerestproxy.examples.springboot.consumer.config;

import org.interfacerestproxy.api.InterfaceRestProxyFactory;
import org.interfacerestproxy.core.InterfaceRestProxyFactoryImpl;
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
