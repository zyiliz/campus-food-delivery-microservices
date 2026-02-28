package com.example.service;

import com.example.pojo.DTO.CategoryCreateDTO;
import com.example.pojo.DTO.CategoryPageQueryDTO;
import com.example.pojo.DTO.CategoryUpdateDTO;
import com.example.pojo.VO.CategoryVO;
import com.example.result.PageResult;
import com.example.result.Result;

import java.util.List;

public interface CategoryService {
    //加入品种
    Result<String> insertCategory(CategoryCreateDTO categoryCreateDTO);

    //查询
    Result<PageResult> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);


    //删除种类
    Result<String> deleteCategory(Long id);


    //更新种类
    Result<String> updateCategory(CategoryUpdateDTO categoryUpdateDTO);


    //启用/禁用分类接口
    Result<String> updateState(Long id,Boolean status);

    Result<List<String>> getCategoryList();

    Result<List<CategoryVO>> getALlCategoryList();
}
