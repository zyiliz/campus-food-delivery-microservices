package com.example.controller;

import com.example.pojo.DTO.LoginDTO;
import com.example.pojo.DTO.UserDTO;
import com.example.pojo.VO.LoginVO;
import com.example.pojo.VO.UserVo;
import com.example.result.MsgRespond;
import com.example.result.Result;
import com.example.service.UserService;
import com.example.utils.IPUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public Result<String> register(@Validated @RequestBody UserDTO userDTO){
        return userService.register(userDTO);
    }

    @PostMapping("/login")
    public Result<LoginVO> login(@Validated @RequestBody LoginDTO loginDTO, HttpServletRequest request){
        String ip = IPUtils.getIpAddress(request);
        return userService.login(loginDTO,ip);
    }

    @GetMapping("/me")
    public Result<UserVo> getUserInfo(){
        return userService.getUserInfo();
    }
}
