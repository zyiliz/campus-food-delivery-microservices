package com.example.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoPurchasedDishDTO {
    // 共同购买的菜品ID
    private Long dishId;
    // 菜品名称
    private String name;
    // 共同出现的频率
    private Integer frequency;
}
