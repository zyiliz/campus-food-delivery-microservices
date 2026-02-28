package com.example.convert;

import com.example.pojo.DTO.SetmealAddDTO;
import com.example.pojo.VO.SetmealVO;
import com.example.pojo.entity.Setmeal;
import com.example.pojo.entity.SetmealDish;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SetmealConvert {

    Setmeal toSetmeal(SetmealAddDTO setmealAddDTO);

    SetmealVO toSetmealVO(Setmeal setmeal);

}
