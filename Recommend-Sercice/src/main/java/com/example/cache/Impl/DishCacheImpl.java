package com.example.cache.Impl;

import com.example.cache.DishCache;
import com.example.pojo.VO.DishPageVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DishCacheImpl implements DishCache {
    private final StringRedisTemplate redisTemplate;
    private final String key = "dish:";

    private final ObjectMapper objectMapper;

    @Override
    public List<DishPageVO> getCommendList(List<Long> ids) {
        if (ids == null || ids.isEmpty()){
            return null;
        }
        List<String> keyList = ids.stream()
                .map(id -> key+id)
                .toList();
        List<String> list = redisTemplate.opsForValue().multiGet(keyList);
        List<DishPageVO> resultList = new ArrayList<>();
        if (list == null || list.isEmpty()){
            return null;
        }
        for (String i :list){
            if (i == null){continue;}
            try{
                DishPageVO dishPageVO = objectMapper.readValue(i, DishPageVO.class);
                resultList.add(dishPageVO);
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }
        return resultList;
    }
}
