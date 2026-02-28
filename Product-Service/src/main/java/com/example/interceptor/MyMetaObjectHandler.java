package com.example.interceptor;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
//元对象处理器
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        // 对create_time字段执行插入填充，值为当前时间
        strictInsertFill(metaObject,"createTime", LocalDateTime.class,LocalDateTime.now());
        strictInsertFill(metaObject,"updateTime",LocalDateTime.class,LocalDateTime.now());
//        metaObject.setValue("create_time",new Date());
    }

    @Override
    public void updateFill(MetaObject metaObject){
        strictUpdateFill(metaObject,"updateTime",LocalDateTime.class,LocalDateTime.now());
    }
}
