package com.example.cache.Impl;

import com.example.cache.DishCache;
import com.example.pojo.VO.DishPageVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

@AllArgsConstructor
@Component
public class DishCacheImpl implements DishCache {

    private final static String key = "dish:";

    private final ObjectMapper objectMapper;

    private final StringRedisTemplate redisTemplate;

    @Override
    public DishPageVO getDish(Long id) {
        String newKey = key+id;
        String s = redisTemplate.opsForValue().get(newKey);
        DishPageVO dishPageVO = null;
        try{
            dishPageVO = objectMapper.readValue(s,DishPageVO.class);
        }catch (IOException e){
            throw  new RuntimeException(e);
        }
        return dishPageVO;
    }
}
