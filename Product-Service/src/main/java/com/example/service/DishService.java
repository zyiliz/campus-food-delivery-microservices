package com.example.service;

import com.example.pojo.DTO.DishInsertDTO;
import com.example.pojo.DTO.DishPageQueryDTO;
import com.example.pojo.DTO.DishStateUpdateDTO;
import com.example.pojo.DTO.DishUpdateDTO;
import com.example.pojo.VO.DishPageVO;
import com.example.pojo.VO.OverViewVO;
import com.example.result.PageResult;
import com.example.result.Result;

import java.math.BigDecimal;
import java.util.List;

public interface DishService {

    Result<String> insertDish(DishInsertDTO dishInsertDTO);

    Result<PageResult> getDishPage(DishPageQueryDTO dishPageQueryDTO);

    Result<DishPageVO> getDishById(Long id);

    Result<String> updateDish(DishUpdateDTO dishUpdateDTO);

    Result<String> updateState(DishStateUpdateDTO dishStateUpdateDTO);

    Result<String> deleteDish(List<Long> ids);

    Result<OverViewVO> overviewDishes();

    Result<List<Long>> getRecommendToAI(String category, BigDecimal price);

    List<String> getImagePathList();

    Result<List<DishPageVO>> getByCategoryName(String name);



}
