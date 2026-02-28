package com.example.service;

import com.example.pojo.DTO.SetmealAddDTO;
import com.example.pojo.DTO.SetmealPageDTO;
import com.example.pojo.VO.OverViewVO;
import com.example.pojo.VO.SetmealVO;
import com.example.result.PageResult;
import com.example.result.Result;

import java.util.List;

public interface SetmealService {

    Result<String> addSetmael(SetmealAddDTO setmealAddDTO);

    Result<PageResult> getSetmealPage(SetmealPageDTO s );

    Result<String> deleteSetmeal(List<Long> ids);

    Result<SetmealVO> getById(Long id);

    Result<OverViewVO> overviewSetmeals();
}
