package org.asemanyk.proxy.examples.springboot.consumer.impl;

import org.asemanyk.proxy.api.InterfaceRestProxyFactory;
import org.asemanyk.proxy.examples.springboot.consumer.api.ConsumerService;
import org.asemanyk.proxy.examples.springboot.producer.api.ProducerService;
import org.asemanyk.proxy.examples.springboot.producer.api.model.SimpleDto;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerServiceImpl implements ConsumerService {

  private final ProducerService producerServiceProxy;

  public ConsumerServiceImpl(InterfaceRestProxyFactory interfaceRestProxyFactory) {
    this.producerServiceProxy = interfaceRestProxyFactory.proxyForInterface(ProducerService.class,
        "http://localhost:8080");
  }

  @Override
  public Iterable<SimpleDto> list() {
    return producerServiceProxy.list();
  }

  @Override
  public SimpleDto get(String id) {
    return producerServiceProxy.get(id);
  }

  @Override
  public SimpleDto create(SimpleDto simpleDto) {
    return producerServiceProxy.create(simpleDto);
  }

  @Override
  public void delete(String id) {
    producerServiceProxy.delete(id);
  }

  @Override
  public SimpleDto getFromQuery(SimpleDto simpleDto) {
    return producerServiceProxy.getFromQuery(simpleDto, "123");
  }
}
