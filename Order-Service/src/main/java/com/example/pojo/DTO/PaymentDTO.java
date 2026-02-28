package com.example.pojo.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentDTO {

    @NotBlank(message = "订单号不能为空")
    private String orderNo;

    @NotNull(message = "支付方式不能为空")
    @Range(min = 1, max = 2, message = "支付方式只能是1或2")
    private Integer payMethod;
}
