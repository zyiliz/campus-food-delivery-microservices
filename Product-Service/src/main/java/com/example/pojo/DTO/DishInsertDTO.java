package com.example.pojo.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishInsertDTO {
    @NotBlank(message = "菜品名不能为空！")
    private String name;

    @NotNull(message = "种类不能为空！")
    private Long categoryId;

    @NotNull(message = "价格不能为空！")
    @DecimalMin(value = "0.00", inclusive = true, message = "商品价格不能小于0") // 允许价格为0（如赠品）
    private BigDecimal price;

    private String image;


    private String description;
    private Integer status;
}
