package com.example.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrdersPageQueryDTO {

    private Integer page;

    private Integer pageSize;

    private Long userId;

    private Integer status;

    private Integer payMethod;

    private String phone;

    private String orderNo;

    private LocalDateTime payTime;

    private LocalDateTime createTime;



}
