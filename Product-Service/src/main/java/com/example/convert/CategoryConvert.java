package com.example.convert;

import com.example.pojo.DTO.CategoryCreateDTO;
import com.example.pojo.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryConvert {
    Category toCategory(CategoryCreateDTO categoryCreateDTO);


}
