package com.e_connect.order_service.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.stereotype.Component;

import com.e_connect.order_service.Constants;

import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;

@Component
public class RedisConfig {

  @Bean
  public RedisConnectionFactory redisConnectionFactory() {
    LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory();
    return lettuceConnectionFactory;
  }

  @Bean
  public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
    GenericJacksonJsonRedisSerializer genericJacksonJsonRedisSerializer = createPolymorphicSerializer();
    RedisCacheManager redisCacheManager = RedisCacheManager.builder(redisConnectionFactory)
        .withCacheConfiguration(Constants.PAYMENT_TYPE_DESCRIPTION_LIST_CACHE,
            RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofDays(5))
                .serializeValuesWith(SerializationPair
                    .fromSerializer(genericJacksonJsonRedisSerializer)))
        .withCacheConfiguration(Constants.PAYMENT_TYPE_DESCRIPTION_CACHE,
            RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofDays(5))
                .serializeValuesWith(SerializationPair
                    .fromSerializer(genericJacksonJsonRedisSerializer)))
        .withCacheConfiguration(Constants.STATUS_LIST_CACHE,
            RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofDays(5))
                .serializeValuesWith(SerializationPair
                    .fromSerializer(genericJacksonJsonRedisSerializer)))
        .withCacheConfiguration(Constants.STATUS_NAME_CACHE,
            RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofDays(5))
                .serializeValuesWith(SerializationPair
                    .fromSerializer(genericJacksonJsonRedisSerializer)))
        .build();

    return redisCacheManager;
  }

  @Bean
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactory);
    redisTemplate.setValueSerializer(createPolymorphicSerializer());
    return redisTemplate;
  }

    public static GenericJacksonJsonRedisSerializer createPolymorphicSerializer() {

        BasicPolymorphicTypeValidator typeValidator = BasicPolymorphicTypeValidator
            .builder()
            .allowIfBaseType(Object.class)
            .build();

        return GenericJacksonJsonRedisSerializer.builder()
            .enableDefaultTyping(typeValidator)
            .build();
    }
}
