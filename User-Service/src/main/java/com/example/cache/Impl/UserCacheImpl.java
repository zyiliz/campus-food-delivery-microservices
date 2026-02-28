package com.example.cache.Impl;

import com.example.cache.UserCache;;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


@Component
@AllArgsConstructor
public class UserCacheImpl implements UserCache {

    private final StringRedisTemplate stringRedisTemplate;

    private final static String JWTKey = "JWTKey";

    private final static String key = "UserTable";

    private static final long EXPIRATION = 7 * 24 * 60 * 60 * 1000;

    private final static ObjectMapper mapper = new ObjectMapper();


    @Override
    public void insertToken(Long ID, String token) {
        String key = JWTKey+":"+ID;
        stringRedisTemplate.opsForValue().set(key,token);
        stringRedisTemplate.expire(key,EXPIRATION, TimeUnit.MILLISECONDS);
    }
}
