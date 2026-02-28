package com.example.pojo.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {
    @NotBlank(message = "收货人不能为空！")
    private String consignee;

    @NotBlank(message = "性别不能为空！")
    private String sex;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号码必须为11位有效数字")
    private String phone;

    @NotBlank(message = "详细地址不能为空！")
    private String detail;

    private String label;
    private Integer isDefault;
}
