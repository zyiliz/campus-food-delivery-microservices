package com.example.result;

import lombok.Data;

import java.io.Serializable;
@Data
public class Result<T> implements Serializable {
    private Integer code;
    private String msg;
    private T data;


    public Result(){}

    public static <T> Result<T> success(T data){
        Result<T> result = new Result<>();
        result.code = 200;
        result.msg = "success";
        result.data = data;
        return result;
    }

    public static  <T> Result<T> success(){
        return success(null);
    }
    //默认失败
    public static <T> Result<T> fail(){
        Result<T> result = new Result<>();
        result.code = 500;
        result.msg = "系统繁忙，请稍后再试";
        return result;
    }
    //自定义失败，携带信息
    public static  <T> Result<T> fail(String msg){
        Result<T> result = new Result<>();
        result.code = 500;
        result.msg = msg;
        return result;
    }

    //高级失败，携带状态码和信息
    public static  <T> Result<T> fail(Integer code,String msg){
        Result<T> result = new Result<>();
        result.code = code;
        result.msg = msg;
        return result;
    }
}
