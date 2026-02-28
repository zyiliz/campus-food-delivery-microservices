package com.example.pojo.DTO;

import com.example.pojo.entity.SetmealDish;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SetmealAddDTO {

    private Long categoryId;

    @NotBlank(message = "套餐名不能为空！")
    private String name;

    private BigDecimal price;

    private Integer status = 0;

    private String code;

    @NotBlank(message = "商品描述不能为空")
    private String description;


    private String image;

    private List<SetmealDish> setmealDishes;
}
