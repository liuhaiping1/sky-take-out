package com.sky.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * RedisTemplate配置类，可以不写
 */
@Configuration
@Slf4j
public class RedisConfiguration {
    //工厂模式，RedisConnectionFactory才能设置序列化器
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory){
        log.info("初始化RedisTemplate.........");
        //设置序列化器为StringRedisSerializer,默认是JdkSerializationRedisSerializer
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //注意：不推荐修改Value的序列化器！！！
        //redisTemplate.setValueSerializer(new StringRedisSerializer());

        //通过Redis工厂创建对象
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }
}
