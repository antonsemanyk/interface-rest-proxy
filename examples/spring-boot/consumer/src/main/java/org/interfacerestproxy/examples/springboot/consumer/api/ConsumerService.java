package org.interfacerestproxy.examples.springboot.consumer.api;

import org.interfacerestproxy.examples.springboot.producer.api.model.SimpleDto;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/consumer")
public interface ConsumerService {

  @GetMapping
  Iterable<SimpleDto> list();

  @GetMapping("{id}")
  SimpleDto get(@PathVariable("id") String id);

  @PostMapping
  SimpleDto create(SimpleDto simpleDto);

  @DeleteMapping
  void delete(String id);

  @GetMapping("query/{simpleDto}")
  SimpleDto getFromQuery(@PathVariable SimpleDto simpleDto);

}
