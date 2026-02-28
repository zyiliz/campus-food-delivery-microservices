package com.example.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@TableName("tb_category")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @TableId(type = IdType.ASSIGN_ID,value = "id")
    private Long id;
    private String name;
    private Integer sort;
    private Integer type;
    private Integer status;
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
