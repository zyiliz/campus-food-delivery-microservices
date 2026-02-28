package com.example.pojo.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryPageQueryDTO {

    private Integer page;
    private Integer pageSize;
    private String name;
    private Integer type;
}
