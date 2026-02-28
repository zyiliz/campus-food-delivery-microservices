package com.example.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("address_book")
public class Address {

    @TableId(type = IdType.ASSIGN_ID,value = "id")
    private Long id;

    @TableField(value = "user_id")
    private Long userId;

    private String consignee;
    private String sex;
    private String phone;
    private String detail;
    private String label;

    @TableField(value = "is_default")
    private Integer isDefault = 0;

    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(value = "is_deleted")
    private Integer isDeleted = 0;
}
