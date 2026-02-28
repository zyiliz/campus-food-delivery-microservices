package com.example.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressVO {
    private String consignee;
    private String sex;
    private String phone;
    private String detail;
    private String label;
    private Boolean isDefault;
}
