package com.example.demo.config;

import java.util.HashMap;
import java.util.Map;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@org.springframework.cache.annotation.CacheConfig(cacheNames = "transactionCache")
public class CacheConfig {

  private Map<String, Long> map = new HashMap<>();

  @Cacheable(key = "#key")
  public Long get(String key) {
    return map.get(key);
  }

  @CachePut(key = "#key")
  public Long put(String key, Long value) {
    map.put(key, value);
    return value;
  }

  @CacheEvict(key = "#key")
  public Long remove(String key) {
    return map.remove(key);
  }
}
