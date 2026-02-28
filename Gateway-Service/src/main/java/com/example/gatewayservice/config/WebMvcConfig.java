package com.example.gatewayservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")             // 允许所有路径
                .allowedOriginPatterns("*")    // 允许所有来源
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的方法
                .allowedHeaders("*")           // 允许所有请求头
                .allowCredentials(true)        // 允许携带 Cookie/Auth 头
                .maxAge(3600);                 // 预检请求有效期
    }
}
