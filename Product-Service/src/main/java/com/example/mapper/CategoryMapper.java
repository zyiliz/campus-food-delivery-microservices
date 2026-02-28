package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pojo.VO.CategoryVO;
import com.example.pojo.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
    @Select("SELECT name FROM tb_category")
    List<String> listAllCategoryNames();

    @Select(("SELECT id,name FROM tb_category"))
    List<CategoryVO> getAllCatrgoryList();


}
