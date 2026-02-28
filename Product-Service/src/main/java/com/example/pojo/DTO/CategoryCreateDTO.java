package com.example.pojo.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryCreateDTO {
    @NotBlank(message = "分类名称不能为空！")
    private String name;

    private Integer sort = 0;

    @NotNull(message = "分类类型不能为空")
    private Integer type;

    private Integer status = 1;

}
