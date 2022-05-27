package org.interfacerestproxy.spring.web.integration.impl;

import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.interfacerestproxy.spring.web.integration.api.IntegrationController;
import org.interfacerestproxy.spring.web.integration.api.model.SimplePojo;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IntegrationControllerImpl implements IntegrationController {

  @Override
  public SimplePojo getSimplePojo(String id) {
    return SimplePojo.builder()
        .id(id)
        .value("foo-bar")
        .build();
  }

  @Override
  public Optional<SimplePojo> getOptionalPojo(String id) {
    if (StringUtils.isEmpty(id)) {
      return Optional.empty();
    }
    return Optional.of(getSimplePojo(id));
  }
}
