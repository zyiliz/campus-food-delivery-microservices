package com.example.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_order")
public class Order {
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    @TableField(value = "order_no")
    private String orderNo;

    @TableField(value = "user_id")
    private Long userId;

    private Integer status;

    private String address;

    @TableField(value = "total_amount")
    private BigDecimal totalAmount;

    @TableField(value = "pay_method")
    private Integer payMethod;

    @TableField(value = "pay_time")
    private LocalDateTime payTime;

    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    private String phone;

    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(value = "cannel_time")
    private LocalDateTime cannelTime;

    @TableField(value = "delivery_time")
    private LocalDateTime deliveryTime;

    @TableField(value = "rejection_reason")
    private String rejectionReason;
    public static final Integer Unpaid = 1;

    public static final Integer Waiting = 2;

    public static final Integer Delivery = 3;

    public static final Integer Completed = 4;

    public static final Integer Canceled = 5;

    public static final Integer Confirm= 6;
}
