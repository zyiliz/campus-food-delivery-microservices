package com.example.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BusinessDataListVO {

    // 新增：每日日期（按天分组的核心字段）
    private String orderDate;
    // 原有字段
    private BigDecimal turnover; // 当日营业额
    private Integer validOrderCount; // 当日有效订单数
    private Double orderCompletionRate; // 当日订单完成率（计算）
    private Double unitPrice; // 当日客单价（计算）
    private Integer newOrders; // 当日总订单数
}
