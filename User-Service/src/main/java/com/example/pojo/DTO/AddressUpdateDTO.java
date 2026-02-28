package com.example.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressUpdateDTO {
    private Long id;
    private String consignee;
    private String sex;
    private String phone;
    private String detail;
    private String label;
    private Integer isDefault;
}
