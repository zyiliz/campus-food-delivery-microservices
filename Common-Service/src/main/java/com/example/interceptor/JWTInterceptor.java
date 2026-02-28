package com.example.interceptor;

import cn.hutool.core.util.StrUtil;
import com.example.entity.UserTLDTO;
import com.example.utils.JWTUtils;
import com.example.utils.UserContext;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.CustomLog;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Date;


public class JWTInterceptor implements HandlerInterceptor {

    @Autowired(required = false)
    private StringRedisTemplate redisTemplate;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //获取请求头
        String auth = request.getHeader("auth");
        //判断是否为空值
        if (StrUtil.isBlank(auth)){
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("请携带令牌登陆！");
            return false;
        }
        Long id;
            try {
                UserContext.setAuth(auth);
                Claims claims = JWTUtils.parseToken(auth);
                Date exp = claims.getExpiration();
                if (exp.before(new Date())){
                    response.setStatus(401);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("令牌已经过期！");
                    return false;
                }
                id = (Long) claims.get("id");
                UserContext.setUserID(id);
            }catch (Exception e){
                response.setStatus(401);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("请携带令牌登陆！");
                return false;
            }

            if (redisTemplate != null){
                String value = redisTemplate.opsForValue().get("JWTKey:"+id);
                if (value == null || !value.equals(auth)){
                    response.setStatus(401);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("令牌已失效，请重新登录");
                    return false; // 拦截！
                }else {
                    return true;
                }
            }

        return true;
    }
    //移除ThreadLocal，释放内存
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
            UserContext.removeUserID();
            UserContext.removeAuth();
    }
}
