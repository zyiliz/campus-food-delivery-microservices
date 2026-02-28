package com.example.config;

import com.example.interceptor.OpenFeignInterceptor;
import feign.RequestInterceptor;
import feign.codec.Decoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.HttpMessageConverterCustomizer;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;


@Configuration
@ConditionalOnClass(RequestInterceptor.class)
public class FeignConfig {

    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    // 注入 HTTP 消息转换器自定义器的提供者（可选，无自定义时可传递 null）
    @Autowired(required = false)
    private ObjectProvider<HttpMessageConverterCustomizer> converterCustomizers;

    @Bean
    public OpenFeignInterceptor openFeignInterceptor(){
        return new OpenFeignInterceptor();
    }

    @Bean
    public Decoder feignDecoder(){
        return new SpringDecoder(messageConverters,converterCustomizers);
    }


}
