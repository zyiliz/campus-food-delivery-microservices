package com.example.pojo.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPageQueryDTO {

    private Integer page;

    private Integer pageSize;

    @Range(min = 1, max = 5, message = "订单状态只有1-5")
    private Integer status;

}
