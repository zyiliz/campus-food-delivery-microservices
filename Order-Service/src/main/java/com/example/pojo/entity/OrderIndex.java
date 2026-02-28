package com.example.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("order_index")
public class OrderIndex {
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    @TableField(value = "order_no")
    private String orderNo;

    @TableField(value = "user_id")
    private Long userId;
    private Integer status;

    private BigDecimal amount;

    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
