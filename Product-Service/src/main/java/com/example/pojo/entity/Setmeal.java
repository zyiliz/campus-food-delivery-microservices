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
@TableName("tb_setmeal")
public class Setmeal {
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    @TableField(value = "category_id")
    private Long categoryId;
    private BigDecimal price;
    private Integer status;
    private String code;
    private String description;
    private String image;

    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(value = "create_user")
    private Long createUser;

    @TableField(value = "update_user")
    private Long updateUser;

    public static final Integer sold =  1;

    public static final Integer discontinued =  0;

}
