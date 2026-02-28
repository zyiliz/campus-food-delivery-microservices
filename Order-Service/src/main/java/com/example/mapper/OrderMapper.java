package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pojo.DTO.DishFrequencyDTO;
import com.example.pojo.VO.BusinessDataListVO;
import com.example.pojo.VO.BusinessDataVO;
import com.example.pojo.VO.OrderAdminVO;
import com.example.pojo.entity.Order;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    @Select("""
            SELECT IFNULL(SUM(total_amount), 0)\s
                    FROM `tb_order`\s
                    WHERE create_time >= #{startTime}\s
                      AND create_time <= #{endTime}
                      AND status = #{status}\s""")
    BigDecimal selectOrderAmountSumByDateRangeAndStatus(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("status") Integer status
    );

    @Select("SELECT " +
            "DATE_FORMAT(create_time, '%Y-%m-%d') as orderDate, " +
            "IFNULL(SUM(total_amount), 0) as turnover, " +
            "COUNT(CASE WHEN status = #{status} THEN id END) as validOrderCount, " +
            "COUNT(id) as newOrders " +
            "FROM tb_order " +
            "WHERE create_time >= #{beginTime} AND create_time <= #{endTime} " +
            "GROUP BY orderDate " +
            "ORDER BY orderDate ASC")
    List<BusinessDataListVO> getDailyBusinessData(
            @Param("beginTime") LocalDateTime beginTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("status") Integer status
    );

    /**
     * 根据用户ID+订单状态，查询出现频率前五的菜品ID
     * @param userId 目标用户ID
     * @param status 订单状态
     * @return 菜品频率列表（前5）
     */
    @Select("SELECT " +
            "  od.dish_id AS dishId, " +
            "  COUNT(od.dish_id) AS frequency " +
           "FROM " +
            "  tb_order o " +
            "INNER JOIN " +
            "  tb_order_detail od ON o.id = od.order_id " +
            "WHERE " +
            "  o.user_id = #{userId} " +
            "  AND o.status = #{status} " +
            "GROUP BY " +
           "  od.dish_id " +
          "ORDER BY " +
            "  frequency DESC " +
            "LIMIT 5")
    List<DishFrequencyDTO> listTop5DishByUserIdAndStatus(
            @Param("userId") Long userId,
            @Param("status") Integer status
   );


    /**
     * 查询所有订单的核心信息 + 对应的菜品名称列表
     */
    @Select("SELECT " +
            "id, " +
            "order_no AS orderNo, " + // 别名对应VO的orderNo字段
            "status, " +
            "total_amount AS totalAmount, " + // 别名对应VO的totalAmount字段
            "create_time AS createTime, " + // 别名对应VO的createTime字段
            "phone " +
            "FROM tb_order")
    @Results(id = "orderAdminVOResultMap", value = {
            // 标记id为主键，MyBatis通过id合并同一订单的结果
            @Result(property = "id", column = "id", id = true),
            // 普通字段映射（表字段别名 → VO属性）
            @Result(property = "orderNo", column = "orderNo"),
            @Result(property = "status", column = "status"),
            @Result(property = "totalAmount", column = "totalAmount"),
            @Result(property = "createTime", column = "createTime"),
            @Result(property = "phone", column = "phone"),
            // 一对多关联：通过订单id查对应的菜品名称列表
            @Result(
                    property = "orderDishList", // 对应VO的orderDishList字段
                    column = "id", // 传递当前订单的id作为子查询参数
                    many = @Many(
                            select = "getOrderDetailNamesByOrderId", // 子查询方法
                            fetchType = FetchType.LAZY // 懒加载（访问orderDishList时才执行子查询，也可设为EAGER立即加载）
                    )
            )
    })
    List<OrderAdminVO> listAllOrderAdminVO();


    /**
     * 子查询：根据订单id，查对应的订单详情中的菜品名称
     */
    @Select("SELECT name FROM tb_order_detail WHERE order_id = #{orderId}")
    List<String> getOrderDetailNamesByOrderId(Long orderId);


    /**
     * 查询指定status的订单核心信息 + 对应的菜品名称列表
     * @param status 订单状态（比如xxx可以是1/2/"已支付"等，根据你的表字段类型调整）
     */
    @Select("SELECT " +
            "id, " +
            "order_no AS orderNo, " +
            "status, " +
            "total_amount AS totalAmount, " +
            "create_time AS createTime, " +
            "phone " +
            "FROM tb_order " +
            "WHERE status = #{status}") // 新增status条件
    @ResultMap("orderAdminVOResultMap")
    List<OrderAdminVO> listOrderAdminVOByStatus(Object status); // 入参类型根据你的status字段调整（Integer/String）



}
