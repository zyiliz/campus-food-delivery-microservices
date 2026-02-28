package com.example.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class AddressVO {
    private Long id;
    private String consignee;
    private String sex;
    private String phone;
    private String detail;
    private String label;
    private Integer isDefault;
}
