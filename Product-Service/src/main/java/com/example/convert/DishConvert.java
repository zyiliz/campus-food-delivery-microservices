package com.example.convert;

import com.example.pojo.DTO.DishInsertDTO;
import com.example.pojo.VO.DishPageVO;
import com.example.pojo.entity.Dish;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DishConvert {

    Dish toDish(DishInsertDTO dishInsertDTO);

    DishPageVO toDishPageVO(Dish dish);

    List<DishPageVO> toDishPageVOList(List<Dish> list);

}
