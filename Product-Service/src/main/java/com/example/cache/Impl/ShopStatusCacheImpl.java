package com.example.cache.Impl;

import cn.hutool.core.util.ObjectUtil;
import com.example.cache.ShopStatusCache;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ShopStatusCacheImpl implements ShopStatusCache {

    private final StringRedisTemplate stringRedisTemplate;

    private final static String key = "Shop:Status";


    @Override
    public void updateShopStatus(Integer status) {
        //不设置过期时间
        stringRedisTemplate.opsForValue().set(key, String.valueOf(status));

    }

    @Override
    public Integer getShopStatus() {
        String s = stringRedisTemplate.opsForValue().get(key);
        if (s != null){
            Integer result = Integer.valueOf(s);
            return result;
        }else{
            stringRedisTemplate.opsForValue().set(key, String.valueOf(0));
            return 0;
        }
    }
}
