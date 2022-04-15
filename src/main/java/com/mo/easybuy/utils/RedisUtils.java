package com.mo.easybuy.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * author mozihao
 * create 2022-03-07 23:46
 * Description
 */
public class RedisUtils {
    @Autowired
    public RedisTemplate redisTemplate;

    /**
     * 根据key删除
     * @param key
     */
    public void delRedis(String key){
        redisTemplate.delete(key);
    }

    /**
     * 根据key获取list
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List getListByKey(String key, long start, long end){
        return redisTemplate.opsForList().range(key,start,end);
    }

    /**
     * 向右添加
     * @param key
     * @param list
     */
    public void rightPushAll(String key, List list){
        delRedis(key); //先删除再push
        redisTemplate.opsForList().rightPushAll(key,list);
    }
}