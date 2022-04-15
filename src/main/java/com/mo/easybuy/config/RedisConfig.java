package com.mo.easybuy.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;

/**
 * author mozihao
 * create 2022-03-07 23:44
 * Description
 */
//@Configuration
//@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Serializable> redisCacheTemplate(RedisConnectionFactory redisConnectFactory){
        RedisTemplate<String,Serializable> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectFactory);
        //将redisTemplate默认改为stringRedis序列化方式
        RedisSerializer stringSerializer = new StringRedisSerializer();
        //使用GenericJackson2JsonRedisSerializer来序列化和反序列化redis的value值
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setValueSerializer(genericJackson2JsonRedisSerializer);
        //hash字符串序列化方法
        template.setHashKeySerializer(stringSerializer);
        template.setHashValueSerializer(genericJackson2JsonRedisSerializer);

        System.out.println("序列化器设置成功");
        return template;
    }
}
