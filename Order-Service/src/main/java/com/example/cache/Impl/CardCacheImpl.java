package com.example.cache.Impl;

import cn.hutool.core.util.ObjectUtil;
import com.example.Exception.BusinessException;
import com.example.cache.CardCache;
import com.example.pojo.DTO.AddCardDTO;
import com.example.pojo.VO.DishPageVO;
import com.example.pojo.VO.SetmealVO;
import com.example.pojo.VO.ShoppingCardVO;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class CardCacheImpl implements CardCache {

    private final StringRedisTemplate stringRedisTemplate;

    private final static String key="card:";

    private final static String dishField = "dish:";

    private final static String setmealField = "setmeal:";

    private static final long EXPIRATION = 12;

    private final ObjectMapper objectMapper;


    @Override
    public void addCard(AddCardDTO addCardDTO, Long userId) {
        String newKey = key+userId;
        String newField = addCardDTO.getDishId() != null?dishField+addCardDTO.getDishId()
                :setmealField+addCardDTO.getSetmealId();
        String Json = null;
        Object o = stringRedisTemplate.opsForHash().get(newKey, newField);
        try {
            if (ObjectUtil.isNull(o)){
                Json = objectMapper.writeValueAsString(addCardDTO);
            }else {
                AddCardDTO addCardDTO1 = objectMapper.readValue((JsonParser) o,AddCardDTO.class);
                addCardDTO1.setNumber(addCardDTO1.getNumber()+addCardDTO.getNumber());
                Json = objectMapper.writeValueAsString(addCardDTO1);

            }
        }catch (IOException e){
            throw new RuntimeException("购物车数据转换异常！",e);
        }

        if (Json != null) {
            stringRedisTemplate.opsForHash().put(newKey,newField,Json);
            stringRedisTemplate.expire(newKey,EXPIRATION,TimeUnit.HOURS);
        }
    }


    @Override
    public List<AddCardDTO> getCard(Long userId) {
        String newKey = key+userId;
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(newKey);
        Collection<Object> values = entries.values();
        List<AddCardDTO> list= values.stream().map(item -> {
            AddCardDTO cardDTO = null;
            try {
                cardDTO = objectMapper.readValue((JsonParser) item, AddCardDTO.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return cardDTO;
        }).toList();
        return list;
    }

    @Override
    public Boolean judgeDishExist(Long userId, Long id) {
        String newKey = key +userId;
        String newField = dishField +id;
        Object o = stringRedisTemplate.opsForHash().get(newKey, newField);
        if (ObjectUtil.isNull(o)){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public Boolean judgeSetmealExist(Long userId, Long id) {
        String newKey = key +userId;
        String newField = setmealField +id;
        Object o = stringRedisTemplate.opsForHash().get(newKey, newField);
        if (ObjectUtil.isNull(o)){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void clearCart(Long userId) {
        String newKey = key+userId;
        stringRedisTemplate.delete(newKey);
    }

    @Override
    public void subCart(Long userId, AddCardDTO addCardDTO) {
        String newKey = key+userId;
        String newField = addCardDTO.getDishId() != null ?dishField+addCardDTO.getDishId()
                :setmealField+addCardDTO.getSetmealId();
        Object o = stringRedisTemplate.opsForHash().get(newKey, newField);
        try{
            AddCardDTO cardDTO = objectMapper.readValue((JsonParser) o, AddCardDTO.class);
            if (ObjectUtil.isNotNull(cardDTO) && cardDTO.getNumber()>=addCardDTO.getNumber()){
               if (cardDTO.getNumber() - addCardDTO.getNumber() == 0){
                   stringRedisTemplate.opsForHash().delete(newKey,newField);
               }else{
                   cardDTO.setNumber(cardDTO.getNumber()-addCardDTO.getNumber());
                   stringRedisTemplate.opsForHash().put(newKey,newField,cardDTO);
                   stringRedisTemplate.expire(newKey,EXPIRATION,TimeUnit.HOURS);
               }
            }
        }catch (IOException e){
            throw new RuntimeException(e);
        }

    }
}
