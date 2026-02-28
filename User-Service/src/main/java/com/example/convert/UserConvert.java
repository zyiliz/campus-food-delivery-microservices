package com.example.convert;

import com.example.pojo.DTO.UserDTO;
import com.example.pojo.VO.UserVo;
import com.example.pojo.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserConvert {

    User doToUser(UserDTO userDTO);

    UserVo doToVo(User user);



}
