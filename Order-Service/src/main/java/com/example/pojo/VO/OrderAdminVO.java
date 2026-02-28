package com.example.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class OrderAdminVO {

    private Long id;
    private String orderNo;
    private Integer status;
    private BigDecimal totalAmount;
    private LocalDateTime createTime;
    private String phone;
    private List<String> orderDishList;

}
