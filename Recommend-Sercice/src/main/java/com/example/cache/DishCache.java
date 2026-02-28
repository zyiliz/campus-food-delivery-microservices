package com.example.cache;

import com.example.pojo.VO.DishPageVO;

import java.util.List;

public interface DishCache {
    List<DishPageVO> getCommendList(List<Long> ids);
}
