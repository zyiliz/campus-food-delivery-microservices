package com.example.entity.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MerchantRequest {
    @NotBlank(message = "商店名不能为空！")
    private String name;
    @NotBlank(message = "地址不能为空！")
    private String address;
    @Pattern(regexp = "^1\\d{10}$", message = "mobile为11位数的手机号字符串")
    private String phone;
    private String status;
}
