package com.example.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCardDTO {

    private String name;
    private BigDecimal price;
    private String image;
    private Integer number = 1;
    private Long userId;
    private Long dishId;
    private Long setmealId;


}
