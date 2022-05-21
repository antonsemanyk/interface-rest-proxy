package org.asemanyk.proxy.examples.springboot.producer.impl;

import java.util.List;
import org.asemanyk.proxy.examples.springboot.producer.api.ProducerService;
import org.asemanyk.proxy.examples.springboot.producer.api.model.SimpleDto;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProducerServiceImpl implements ProducerService {

  @Override
  public Iterable<SimpleDto> list() {
    return List.of();
  }

  @Override
  public SimpleDto get(String id) {
    return SimpleDto.builder()
        .id(id)
        .name("SimpleDto")
        .build();
  }

  @Override
  public SimpleDto create(SimpleDto simpleDto) {
    return null;
  }

  @Override
  public void delete(String id) {

  }

  @Override
  public SimpleDto getFromQuery(SimpleDto simpleDto, String other) {
    return simpleDto;
  }
}
