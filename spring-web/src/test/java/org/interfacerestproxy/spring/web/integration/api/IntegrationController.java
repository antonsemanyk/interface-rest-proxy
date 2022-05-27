package org.interfacerestproxy.spring.web.integration.api;

import java.util.Optional;
import org.interfacerestproxy.spring.web.integration.api.model.SimplePojo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/api")
public interface IntegrationController {

  @GetMapping("/simplePojo")
  SimplePojo getSimplePojo(@RequestParam("id") String id);

  @GetMapping("/optionalPojo")
  Optional<SimplePojo> getOptionalPojo(@RequestParam("id") String id);
}
