package com.example;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.example.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class Test1 {

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Test
    public void test(){
        redisTemplate.opsForValue().set("user:1000","test");

        System.out.println("--------------");

        String s = redisTemplate.opsForValue().get("user:1000");
        System.out.println(s);
    }


}
