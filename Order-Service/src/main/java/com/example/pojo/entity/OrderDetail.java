package com.example.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_order_detail")
public class OrderDetail {
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    @TableField(value = "order_id")
    private Long orderId;

    @TableField(value = "dish_id")
    private Long dishId;

    private String name;

    private BigDecimal price;

    private Integer quantity;

    private String image;
}
