package com.example.cache.Impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.example.cache.DishCache;
import com.example.pojo.VO.DishPageVO;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
@AllArgsConstructor
public class DishCacheImpl implements DishCache {

    private final StringRedisTemplate redisTemplate;

    private final static String key = "dish:";

    // 基础过期时间：7天（转成秒，适配Redis默认单位）
    private static final long BASE_EXPIRE_SECONDS = 7 * 24 * 3600L;
    // 随机偏移量范围：5~30分钟
    private static final int MIN_OFFSET_SECONDS = 5 * 60;
    private static final int MAX_OFFSET_SECONDS = 30 * 60;

    private final ObjectMapper objectMapper;


    @Override
    public void addDish(DishPageVO dishPageVO) {
        String newKey = key+dishPageVO.getId();
        String value = null;
        try{
                value = objectMapper.writeValueAsString(dishPageVO);
                redisTemplate.opsForValue().set(newKey,value);
                long daysRandomExpire = get7DaysRandomExpire();
                redisTemplate.expire(newKey,daysRandomExpire, TimeUnit.SECONDS);
        }catch (IOException e){
            throw new RuntimeException("数据转换异常！",e);
        }
    }

    @Override
    public DishPageVO getDish(Long id) {
        String newKey  = key+id;
        String s = redisTemplate.opsForValue().get(newKey);
        DishPageVO dishPageVO = null;
        try{
           dishPageVO  = objectMapper.readValue(s, DishPageVO.class);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
        return dishPageVO;
    }

    @Override
    public void deleteDish(Long id) {
        String newKey = key+id;
        redisTemplate.delete(newKey);
    }

    @Override
    public void deleteDishList(List<Long> ids) {
        if (ids == null || ids.isEmpty()){
            return;
        }
        List<String> keyList = ids.stream()
                .map(id->key+id)
                .toList();
        redisTemplate.delete(keyList);
    }


    /**
     * 动态生成7天±随机偏移的过期时间（秒）
     * 每次调用生成不同的随机值，彻底分散缓存过期点
     */
    public static long get7DaysRandomExpire() {
        // 生成[MIN_OFFSET_SECONDS, MAX_OFFSET_SECONDS)的随机整数
        long randomOffset = RandomUtil.randomInt(MIN_OFFSET_SECONDS, MAX_OFFSET_SECONDS);
        // 最终过期时间 = 7天 + 随机偏移
        return BASE_EXPIRE_SECONDS + randomOffset;
    }
}
