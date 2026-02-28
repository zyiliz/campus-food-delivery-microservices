package com.example.interceptor;

import com.example.utils.UserContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


public class AdminInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String role = UserContext.getRole();
        String requestURL = request.getRequestURI();
        if (role.equals("admin")){
            return true;
        }
        if (requestURL.contains("admin")){
            System.out.println("被管理员拦截器拦截！");
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":403,\"message\":\"权限不足，仅管理员可访问此路径\"}");
            return false;
        }
        return true;
    }
}
