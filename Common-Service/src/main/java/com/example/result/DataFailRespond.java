package com.example.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用数据型失败响应
 * by organwalk 2023-10-18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataFailRespond implements DataRespond {
    private Integer code;
    private String msg;

    public DataFailRespond(String msg) {
        this.code = 5005;
        this.msg = msg;
    }
}
