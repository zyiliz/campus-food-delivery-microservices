package com.example.service.Impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.cache.UserCache;
import com.example.client.ResourceClient;
import com.example.convert.UserConvert;
import com.example.mapper.UserMapper;
import com.example.pojo.DTO.LoginDTO;
import com.example.pojo.DTO.UserDTO;
import com.example.pojo.VO.LoginVO;
import com.example.pojo.VO.UserVo;
import com.example.pojo.entity.User;
import com.example.result.DataRespond;
import com.example.result.DataSuccessRespond;
import com.example.result.MsgRespond;
import com.example.result.Result;
import com.example.service.UserService;
import com.example.utils.JWTUtils;
import com.example.utils.UserContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserConvert userConvert;
    private final UserCache userCache;
    private final ResourceClient resourceClient;



    @Override
    public Result<String> register(UserDTO userDTO) {
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<User>()
                .eq(User::getPhone,userDTO.getPhone())
                .or()
                .eq(User::getUsername,userDTO.getUsername());
        User exit = userMapper.selectOne(lqw);
        if (ObjectUtil.isNotNull(exit)){
            log.warn("【用户注册】失败，手机号或用户名已经存在,手机号：{},用户名：{}",userDTO.getPhone(),userDTO.getUsername());
            return Result.fail("该用户已经存在！");
        }
        if (!Objects.equals(userDTO.getConfirmPassword(), userDTO.getPassword())){
            return Result.fail("两次输入的秘密不一致，请重新输入！");
        }
        String hash = BCrypt.hashpw(userDTO.getPassword(),BCrypt.gensalt());
        userDTO.setPassword(hash);
        User user = userConvert.doToUser(userDTO);
        int insert = userMapper.insert(user);

        if (insert>0){
            log.info("【用户注册】用户创建成功，用户id:{},手机号：{}",user.getId(),user.getPhone());
            return Result.success("用户创建成功！");
        }else {
            log.info("【用户注册】用户创建成功，用户信息：{}",userDTO);
            return Result.fail("用户创建失败，请稍后再试");
        }
    }

    @Override
    public Result<LoginVO> login(LoginDTO loginDTO, String ip) {
        log.info("【用户登录】尝试登陆,用户名；{}，ip；{}",loginDTO.getUsername(),ip);
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<User>()
                .eq(User::getUsername,loginDTO.getUsername());
        User user = userMapper.selectOne(lqw);
        if (ObjectUtil.isNull(user)){
            return Result.fail("用户不存在！");
        }
        if (!BCrypt.checkpw(loginDTO.getPassword(),user.getPassword())){
            return Result.fail("密码错误，请重新输入!");
        }
        String role;
        if (user.getRole() == 0){
            role = "admin";
        }else {
            role = "student";
        }
        String s = JWTUtils.generateToken(user.getId(),role);
        userCache.insertToken(user.getId(),s);
        LoginVO loginVO = LoginVO.builder()
                .auth(s)
                .role(user.getRole())
                .build();
        return Result.success(loginVO);
    }

    @Override
    public Result<UserVo> getUserInfo() {
        Long id = UserContext.getId();
        User user = userMapper.selectById(id);
        if (ObjectUtil.isNull(user)){
            return Result.fail("用户不存在！");
        }
        UserVo userVo = userConvert.doToVo(user);
        return Result.success(userVo);
    }

    private String uploadImage(MultipartFile file){
        if (file.isEmpty()){
            return  "头像不能为空！";
        }
        DataRespond upload = resourceClient.upload(file);
        if (upload instanceof DataSuccessRespond dataSuccessRespond){
            String data = (String) dataSuccessRespond.getData();
            return data;
        }
        return null;
    }

    private String getPreview(String fileName){
        DataRespond preview = resourceClient.getPreview(fileName);
        if (preview instanceof DataSuccessRespond dataSuccessRespond){
            String filePath = (String) dataSuccessRespond.getData();
            return  filePath;
        }
        return  null;
    }
}
