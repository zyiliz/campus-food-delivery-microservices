package com.example.service;

import com.example.pojo.DTO.LoginDTO;
import com.example.pojo.DTO.UserDTO;
import com.example.pojo.VO.LoginVO;
import com.example.pojo.VO.UserVo;
import com.example.result.MsgRespond;
import com.example.result.Result;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {


    Result<String> register(UserDTO userDTO);

    Result<LoginVO> login(LoginDTO loginDTO, String ip);

    Result<UserVo> getUserInfo();
}
