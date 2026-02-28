package com.example.config;

import com.example.interceptor.AdminInterceptor;
import com.example.interceptor.JWTInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@AutoConfiguration
public class InterceptorConfig implements WebMvcConfigurer {

    @Bean
    public JWTInterceptor jwtInterceptor() {
        return new JWTInterceptor();
        // 放心，这里虽然是 new，但 Spring 会接管它，
        // 拦截器内部的 @Autowired StringRedisTemplate 依然会生效！
    }

    @Bean
    public AdminInterceptor adminInterceptor() {
        return new AdminInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(jwtInterceptor())
                .addPathPatterns("/**")  //拦截所有路径
                .excludePathPatterns(
                        "/**/*login*",    // 匹配任意位置包含"login"的路径
                        "/**/*register*",  // 匹配任意位置包含"register"的路径
                        "/**/*uploadUsrImage*"
                );
        registry.addInterceptor(adminInterceptor())
                .addPathPatterns("/**/*admin*");
    }
}
