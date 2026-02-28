package com.example.cache.Impl;

import com.example.cache.ResourceCache;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
public class ResourceCacheImpl implements ResourceCache {

    private final StringRedisTemplate redisTemplate;

    private static final String key = "image:temp:set";



    @Override
    public void addImagePath(String path) {
        double l = (double) System.currentTimeMillis();
        redisTemplate.opsForZSet().add(key,path,l);
    }

    @Override
    public void deleteImagePath(List<String> del_list) {
        if (!del_list.isEmpty()){
            redisTemplate.opsForZSet().remove(key,del_list);
        }
    }

    @Override
    public Set<String> getImagePathList() {
        ZSetOperations<String, String> stringStringZSetOperations = redisTemplate.opsForZSet();
        return stringStringZSetOperations.range(key,0,-1);
    }

    @Override
    public void deleteAllImagePath() {
        redisTemplate.delete(key);
    }


}
