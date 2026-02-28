package com.example.result;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * 通用数据响应，包含两个实现类
 * by organwalk 2023-10-18
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,    // 用“名称”区分实现类
        property = "type",            // 类型标识的字段名（如返回体中的`type`字段）
        defaultImpl = DataSuccessRespond.class // 可选默认实现
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DataSuccessRespond.class, name = "success"),
        @JsonSubTypes.Type(value = DataFailRespond.class, name = "fail")
})
public interface DataRespond {

}
