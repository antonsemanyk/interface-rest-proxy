package org.interfacerestproxy.spring.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.interfacerestproxy.api.InterfaceAnnotationsProcessor;
import org.interfacerestproxy.api.descriptor.ApiDescriptor;
import org.interfacerestproxy.api.exception.InterfaceDescriptionException;
import org.interfacerestproxy.spring.web.interfaces.EmptyInterface;
import org.interfacerestproxy.spring.web.interfaces.EmptyInterfaceWithEmptyMapping;
import org.interfacerestproxy.spring.web.interfaces.EmptyInterfaceWithMapping;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SpringAnnotationsProcessorTest {

  InterfaceAnnotationsProcessor processor = new SpringAnnotationsProcessor();

  @Test
  @DisplayName("Should throw InterfaceDescriptionException for no @RequestMapping")
  void processEmptyInterface() {
    assertThatThrownBy(() -> processor.process(EmptyInterface.class))
        .hasMessage("Interface " + EmptyInterface.class + " should be annotated with @RequestMapping")
        .isInstanceOf(InterfaceDescriptionException.class);
  }

  @Test
  @DisplayName("Should use empty basePath when not specified in @RequestMapping")
  void processEmptyRequestMappingOnInterface() {
    ApiDescriptor apiDescriptor = processor.process(EmptyInterfaceWithEmptyMapping.class);

    assertThat(apiDescriptor.getBasePath()).isEqualTo("");
  }

  @Test
  @DisplayName("Should use basePath from @RequestMapping when specified")
  void processRequestMappingOnInterface() {
    ApiDescriptor apiDescriptor = processor.process(EmptyInterfaceWithMapping.class);

    assertThat(apiDescriptor.getBasePath()).isEqualTo("/api");
  }
}
