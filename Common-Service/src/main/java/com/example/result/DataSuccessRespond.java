package com.example.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用数据型成功响应
 * by organwalk 2023-10-18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataSuccessRespond implements DataRespond {
    private Integer code;
    private String msg;
    private Object data;

    public DataSuccessRespond(String msg, Object data) {
        this.code = 2002;
        this.msg = msg;
        this.data = data;
    }
}
