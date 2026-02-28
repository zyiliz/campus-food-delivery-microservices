package com.example.cache.Impl;

import cn.hutool.core.util.RandomUtil;
import com.example.cache.SetmealCache;
import com.example.pojo.VO.SetmealVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@AllArgsConstructor
public class SetmealCacheImpl implements SetmealCache {
    private final StringRedisTemplate redisTemplate;

    private final static String key = "setmeal:";

    // 基础过期时间：7天（转成秒，适配Redis默认单位）
    private static final long BASE_EXPIRE_SECONDS = 7 * 24 * 3600L;
    // 随机偏移量范围：5~30分钟
    private static final int MIN_OFFSET_SECONDS = 5 * 60;
    private static final int MAX_OFFSET_SECONDS = 30 * 60;

    private final ObjectMapper objectMapper;




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

    @Override
    public void addSetmeal(SetmealVO setmealVO) {
        String newKey = key+setmealVO.getId();
        String value = null;
        try {
            value = objectMapper.writeValueAsString(setmealVO);
            redisTemplate.opsForValue().set(newKey,value);
            long daysRandomExpire = get7DaysRandomExpire();
            redisTemplate.expire(newKey,daysRandomExpire, TimeUnit.SECONDS);
        }catch (IOException e){
            throw new RuntimeException(e);
        }

    }

    @Override
    public SetmealVO getSetmeal(Long id) {
        String newKey = key+id;
        String s = redisTemplate.opsForValue().get(newKey);
        SetmealVO setmealVO = null;
        try{
            setmealVO = objectMapper.readValue(s,SetmealVO.class);
        }catch (IOException e){throw  new RuntimeException(e);}
        return setmealVO;
    }

    @Override
    public void deleteSetmeal(Long id) {
        String newKey = key + id;
        redisTemplate.delete(newKey);
    }

    @Override
    public void deleteSetmealList(List<Long> ids) {
        List<String> keyList = ids.stream()
                .map(id -> key+id)
                .toList();
        redisTemplate.delete(keyList);
    }
}
