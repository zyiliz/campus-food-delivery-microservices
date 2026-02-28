package com.example.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishPageQueryDTO {
    private Integer page;
    private Integer pageSize;
    private String name;
    private Long categoryId;
    private Integer status;

}
