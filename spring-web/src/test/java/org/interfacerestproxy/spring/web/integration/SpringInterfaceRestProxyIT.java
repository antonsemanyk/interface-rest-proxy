package org.interfacerestproxy.spring.web.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.UUID;
import org.interfacerestproxy.api.InterfaceRestProxyFactory;
import org.interfacerestproxy.core.InterfaceRestProxyFactoryImpl;
import org.interfacerestproxy.spring.web.integration.api.IntegrationController;
import org.interfacerestproxy.spring.web.integration.api.model.SimplePojo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(
    webEnvironment = WebEnvironment.RANDOM_PORT
)
class SpringInterfaceRestProxyIT {

  static final String BASE_URL_TEMPLATE = "http://localhost:%d";
  static final InterfaceRestProxyFactory PROXY_FACTORY = new InterfaceRestProxyFactoryImpl.Builder().build();

  @LocalServerPort
  int port;

  IntegrationController proxy;

  @BeforeEach
  void setUp() {
    proxy = PROXY_FACTORY.proxyForInterface(IntegrationController.class, String.format(BASE_URL_TEMPLATE, port));
  }

  @Test
  void verifyGetSimplePojo() {
    String id = UUID.randomUUID().toString();

    SimplePojo result = proxy.getSimplePojo(id);

    assertThat(result).isEqualTo(SimplePojo.builder()
        .id(id)
        .value("foo-bar")
        .build());
  }

  @Test
  void verifyGetOptionalPojo() {
    String id = UUID.randomUUID().toString();

    Optional<SimplePojo> result = proxy.getOptionalPojo(id);

    assertThat(result).isPresent();
    assertThat(result.get()).isEqualTo(SimplePojo.builder()
        .id(id)
        .value("foo-bar")
        .build());
  }

  @Test
  void verifyGetOptionalPojoEmpty() {
    Optional<SimplePojo> result = proxy.getOptionalPojo("");

    assertThat(result).isEmpty();
  }
}
