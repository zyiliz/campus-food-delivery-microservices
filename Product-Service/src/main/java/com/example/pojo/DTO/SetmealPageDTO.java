package com.example.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SetmealPageDTO {
    private Long categoryId;
    private Integer page;
    private Integer pageSize;
}
