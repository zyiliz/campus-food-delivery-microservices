package com.example.cache;

import com.example.pojo.VO.DishPageVO;

import java.util.List;

public interface DishCache {

    void addDish(DishPageVO dishPageVO);

    DishPageVO getDish(Long id);

    void deleteDish(Long id);

    void deleteDishList(List<Long> ids);
}
