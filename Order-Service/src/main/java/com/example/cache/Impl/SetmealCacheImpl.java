package com.example.cache.Impl;

import com.example.cache.SetmealCache;
import com.example.pojo.VO.SetmealVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@AllArgsConstructor
public class SetmealCacheImpl implements SetmealCache {
    private final static String key = "setmeal:";

    private final ObjectMapper objectMapper;

    private final StringRedisTemplate redisTemplate;

    @Override
    public SetmealVO getSetmeal(Long id) {
        String newKey = key+id;
        String s = redisTemplate.opsForValue().get(newKey);
        SetmealVO setmealVO = null;
        try {
            setmealVO = objectMapper.readValue(s,SetmealVO.class);
        }catch (IOException e){
            throw  new RuntimeException(e);
        }
        return setmealVO;
    }
}
