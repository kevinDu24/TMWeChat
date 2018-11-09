package com.tm.wechat.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Created by LEO on 16/9/14.
 */
@Repository
public class RedisRepository {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Resource(name="stringRedisTemplate")
    ValueOperations<String, String> valOpsStr;

    @Autowired
    RedisTemplate<Object, Object> redisTemplate;

    @Resource(name="redisTemplate")
    ValueOperations<Object, Object> valOps;

    public void save(Object key, Object value, int time){
        valOps.set(key, value, time, TimeUnit.SECONDS);
    }

    /**
     * 永久保存
     * @param key
     * @param value
     */
    public void save(Object key, Object value){
        valOps.set(key, value);
    }

    public Object get(Object key){
        return valOps.get(key);
    }
}
