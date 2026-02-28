package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pojo.DTO.CoPurchasedDishDTO;
import com.example.pojo.VO.DishTopVO;
import com.example.pojo.entity.OrderDetail;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {

    // 用@Insert注解实现批量插入
    void batchInsertByAnnotation(@Param("list") List<OrderDetail> orderDetails);


    @Select("SELECT " +
            "od.name as dishName, " +  // 直接取 name，避开 ID 冲突风险
            "SUM(od.quantity) as count " + // 🔥 必须用 SUM，不能用 COUNT！
            "FROM tb_order o " +
            "INNER JOIN tb_order_detail od ON o.id = od.order_id " +
            "WHERE o.create_time >= #{begin} " +
            "AND o.create_time <= #{end} " +
            "AND o.status = #{status} " +
            "GROUP BY od.name " +        // 🔥 按名字分组最稳妥
            "ORDER BY count DESC " +
            "LIMIT 10")
    List<DishTopVO> getTop10DishByDateRange(
            @Param("begin") LocalDateTime begin,
            @Param("end") LocalDateTime end,
            @Param("status") Integer status
    );

    @Select("SELECT " +
            "od.name as dishName, " +  // 直接取 name，避开 ID 冲突风险
            "SUM(od.quantity) as count " + // 🔥 必须用 SUM，不能用 COUNT！
            "FROM tb_order o " +
            "INNER JOIN tb_order_detail od ON o.id = od.order_id " +
            "WHERE o.create_time >= #{begin} " +
            "AND o.create_time <= #{end} " +
            "AND o.status = #{status} " +
            "GROUP BY od.name " +        // 🔥 按名字分组最稳妥
            "ORDER BY count DESC " +
            "LIMIT 5")
    List<DishTopVO> getTop5DishByDateRange(
            @Param("begin") LocalDateTime begin,
            @Param("end") LocalDateTime end,
            @Param("status") Integer status
    );

    /**
     * 查询与多个目标dishId共同出现最多的前5个菜品（仅统计已完成订单）
     * @param targetDishIds 传入的5个目标dishId列表
     * @return 共同出现的Top5菜品
     */
    List<CoPurchasedDishDTO> getTop5CoOccurrenceDishes(@Param("targetDishIds") List<Long> targetDishIds);
}
