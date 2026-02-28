package com.example.pojo.VO;

import com.example.pojo.entity.Order;
import com.example.pojo.entity.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class  OrderVO {

    private Long id;

    private String orderNo;

    private Integer status;

    private String address;

    private BigDecimal totalAmount;

    private Integer payMethod;

    private LocalDateTime payTime;

    private List<OrderDetail> orderDetails;

    private String phone;

    private LocalDateTime cannelTime;

    private LocalDateTime deliveryTime;

    private String rejectionReason;

}
