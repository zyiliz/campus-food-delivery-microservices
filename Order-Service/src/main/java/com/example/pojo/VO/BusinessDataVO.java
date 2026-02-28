package com.example.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusinessDataVO {
    private BigDecimal turnover;        // 营业额
    private Integer validOrderCount;// 有效订单数
    private Double orderCompletionRate; // 订单完成率
    private Double unitPrice;       // 平均客单价
    private Integer newOrders;      // 新增订单数
}
