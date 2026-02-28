package com.example.cache.Impl;

import com.example.cache.OrderCache;
import com.example.pojo.VO.DishVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class OrderCacheImpl implements OrderCache {
    private final StringRedisTemplate redisTemplate;

    private final String key = "cart:";

    private static final long EXPIRATION = 30 * 60 * 1000;


    @Override
    public void insertCard(Long id, DishVO dishVO) {

    }
}
