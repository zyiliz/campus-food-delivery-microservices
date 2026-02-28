package com.example.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@TableName("tb_user")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class User {
    @TableId(type = IdType.ASSIGN_ID,value = "id")
    private Long id;

    private String username;
    private String password;
    private String phone;
    private String avatar;
    private Integer role = 1;
    private Integer status = 1;
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
