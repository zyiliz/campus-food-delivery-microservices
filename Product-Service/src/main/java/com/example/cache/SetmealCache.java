package com.example.cache;

import com.example.pojo.VO.SetmealVO;

import java.util.List;

public interface SetmealCache {
    void addSetmeal(SetmealVO setmealVO);

    SetmealVO getSetmeal(Long id);

    void deleteSetmeal(Long id);

    void deleteSetmealList(List<Long> ids);
}
