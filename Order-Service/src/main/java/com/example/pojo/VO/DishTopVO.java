package com.example.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishTopVO {

    private Long dishId;

    private String dishName;

    private Long count;

}
