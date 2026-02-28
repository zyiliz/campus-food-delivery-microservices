package com.example.Exception;


public class BusinessException extends RuntimeException{


    public BusinessException(String message) {
        super(message); // 把错误信息传给父类
    }

    public BusinessException(Integer code,String message) {
        super(message); // 把错误信息传给父类
    }
}
