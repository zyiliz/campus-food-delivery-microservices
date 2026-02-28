package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pojo.entity.SetmealDish;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface SetmealDishMapper extends BaseMapper<SetmealDish> {
    /**
     * 批量插入套餐菜品关联数据
     * @param setmealDishList 套餐菜品集合
     * @return 插入成功的条数
     */
    @Insert("<script>" +
            "INSERT INTO tb_setmeal_dish (setmeal_id, dish_id, name, price, copies) " +
            "VALUES " +
            "<foreach collection='list' item='setmealDish' separator=','> " +
            "(#{setmealDish.setmealId}, #{setmealDish.dishId}, #{setmealDish.name}, #{setmealDish.price}, #{setmealDish.copies}) " +
            "</foreach>" +
            "</script>")
    int batchInsertSetmealDish(@Param("list") List<SetmealDish> setmealDishList);

}
