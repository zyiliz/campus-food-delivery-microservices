package com.example.pojo.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "请输入密码")
    private String confirmPassword;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号码必须为11位有效数字")
    private String phone;

    @NotBlank(message = "请添加头像")
    private String avatar;
}
