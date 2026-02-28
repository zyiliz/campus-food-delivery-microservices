package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pojo.VO.DishPageVO;
import com.example.pojo.entity.Dish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {

    // 仅按分类名+价格小于指定值查询
    @Select({
            "<script>",
            "SELECT tb_dish.id ",
            "FROM tb_dish ",
            "INNER JOIN tb_category ON tb_dish.category_id = tb_category.id ",
            "<where>",
            "   <if test='categoryName != null and categoryName != \"\"'>",
            "       AND tb_category.name = #{categoryName}",
            "   </if>",
            "   <if test='price != null'>",
            "       AND tb_dish.price = #{price}",
            "   </if>",
            "</where>",
            "</script>"
    })
    List<Long> selectDishIdsByCategoryAndPriceGt(
            @Param("categoryName") String categoryName,
            @Param("price") BigDecimal price
    );

    /**
     * 获取近24小时内更新的菜品的image集合
     * @return 近24小时更新的菜品image列表
     */
    @Select("SELECT image FROM tb_dish WHERE update_time >= DATE_SUB(NOW(), INTERVAL 24 HOUR)")
    List<String> getRecent24HoursDishImages();


    @Select("SELECT * FROM  tb_dish WHERE name = #{name}")
    List<Dish> getByCategoryName(String name);


}
