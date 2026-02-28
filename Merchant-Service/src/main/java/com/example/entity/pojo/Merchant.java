package com.example.entity.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@TableName("merchant")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Merchant {
    @TableId(type = IdType.ASSIGN_ID,value = "merchant_id")
    private String merchantId;
    private String name;
    private String address;
    private String phone;
    private String status;
    @TableField(fill = FieldFill.INSERT, value = "create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creatTime;
}
