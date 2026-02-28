package com.example.pojo.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdersRejectionDTO {
    @NotNull(message = "订单号不能为空！")
    private Integer id;
    private String rejectionReason;
}
