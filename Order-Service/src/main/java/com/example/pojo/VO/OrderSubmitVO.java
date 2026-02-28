package com.example.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderSubmitVO {
    private String orderNo;
    private BigDecimal totalAmount;
    private LocalDateTime createTime;
}
