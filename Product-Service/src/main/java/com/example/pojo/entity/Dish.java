package com.example.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_dish")
public class Dish {
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    private String name;

    @TableField("category_id")
    private Long categoryId;

    private BigDecimal price;

    private String image;

    private String Description;

    private Integer status;

    private Integer sales;

    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    public static final Integer sold =  1;

    public static final Integer discontinued =  0;
}
